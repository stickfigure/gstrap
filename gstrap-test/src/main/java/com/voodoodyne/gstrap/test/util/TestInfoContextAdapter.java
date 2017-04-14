package com.voodoodyne.gstrap.test.util;

import lombok.Data;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.lang.reflect.Method;
import java.util.Optional;
import java.util.Set;

/**
 */
@Data
public class TestInfoContextAdapter implements TestInfo {

	private final ExtensionContext context;

	@Override
	public String getDisplayName() {
		return context.getDisplayName();
	}

	@Override
	public Set<String> getTags() {
		return context.getTags();
	}

	@Override
	public Optional<Class<?>> getTestClass() {
		return context.getTestClass();
	}

	@Override
	public Optional<Method> getTestMethod() {
		return context.getTestMethod();
	}
}
