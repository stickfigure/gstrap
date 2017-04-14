package com.voodoodyne.gstrap.test;

import com.google.common.collect.Iterables;
import com.google.common.reflect.TypeToken;
import com.google.inject.AbstractModule;
import com.google.inject.BindingAnnotation;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.servlet.RequestScoped;
import com.google.inject.util.Modules;
import com.voodoodyne.gstrap.test.servlet.FakeHttpServletRequest;
import com.voodoodyne.gstrap.test.servlet.FakeHttpServletResponse;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.api.extension.TestExtensionContext;

import javax.inject.Qualifier;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Extension which creates a guice injector and puts it in the Store.
 * Test is expected to implement GuicyTest.
 * Does NOT inject on the test object; use GuiceInjectExtension for that.
 *
 * Some of this is borrowed from:
 * https://github.com/JeffreyFalgout/junit5-guice-extension/blob/master/src/main/java/name/falgout/jeffrey/testing/junit5/GuiceExtension.java
 */
public class GuiceExtension implements BeforeEachCallback, ParameterResolver {
	private static final Namespace NAMESPACE = Namespace.create(GuiceExtension.class);

	public interface GuicyTest {
		Module module();
	}

	@Override
	public void beforeEach(final TestExtensionContext context) throws Exception {
		final GuicyTest testInstance = (GuicyTest)context.getTestInstance();
		final Module module = testInstance.module();

		final RequestScope requestScope = new RequestScope();

		final AbstractModule basicTestModule = new AbstractModule() {
			@Override
			protected void configure() {
				this.bindScope(RequestScoped.class, requestScope);
				this.bind(RequestScope.class).toInstance(requestScope);

				this.bind(HttpServletRequest.class).to(FakeHttpServletRequest.class);
				this.bind(HttpServletResponse.class).to(FakeHttpServletResponse.class);
			}
		};

		final Injector injector = Guice.createInjector(Modules.override(basicTestModule).with(module));
		context.getStore(NAMESPACE).put(Injector.class, injector);

		final ScopeRequestFilter scopeFilter = injector.getInstance(ScopeRequestFilter.class);
		injector.getInstance(Requestor.class).addFilter(scopeFilter);
	}

	@Override
	public boolean supports(final ParameterContext parameterContext, final ExtensionContext extensionContext) throws ParameterResolutionException {
		final Parameter parameter = parameterContext.getParameter();
		if (getBindingAnnotations(parameter).size() > 1)
			return false;

		final Injector injector = getInjector(extensionContext);
		if (injector == null)
			return false;

		final Key<?> key = getKey(parameter);

		return injector.getExistingBinding(key) != null;
	}

	@Override
	public Object resolve(final ParameterContext parameterContext, final ExtensionContext extensionContext) throws ParameterResolutionException {
		final Parameter parameter = parameterContext.getParameter();
		final Key<?> key = getKey(parameter);
		final Injector injector = getInjector(extensionContext);

		return injector.getInstance(key);

	}

	/** Get the injector from the Store */
	public static Injector getInjector(final ExtensionContext context) {
		return context.getStore(NAMESPACE).get(Injector.class, Injector.class);
	}

	private static List<Annotation> getBindingAnnotations(final AnnotatedElement element) {
		return Arrays.stream(element.getAnnotations())
				.filter(GuiceExtension::isBindingAnnotation)
				.collect(Collectors.toList());
	}

	private static boolean isBindingAnnotation(final Annotation annotation) {
		Class<? extends Annotation> annotationType = annotation.annotationType();
		return annotationType.isAnnotationPresent(Qualifier.class)
				|| annotationType.isAnnotationPresent(BindingAnnotation.class);
	}

	private static Key<?> getKey(Parameter parameter) {
		final TypeToken<?> classType = TypeToken.of(parameter.getDeclaringExecutable().getDeclaringClass());
		final Type resolvedType = classType.resolveType(parameter.getParameterizedType()).getType();

		Optional<Key<?>> key = getOnlyBindingAnnotation(parameter).map(annotation -> Key.get(resolvedType, annotation));
		return key.orElse(Key.get(resolvedType));
	}

	/**
	 * @throws IllegalArgumentException if the given element has more than one binding annotation.
	 */
	private static Optional<? extends Annotation> getOnlyBindingAnnotation(AnnotatedElement element) {
		return Optional.ofNullable(Iterables.getOnlyElement(getBindingAnnotations(element), null));
	}
}
