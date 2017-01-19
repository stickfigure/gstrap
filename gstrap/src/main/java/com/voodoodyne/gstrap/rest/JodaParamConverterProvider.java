package com.voodoodyne.gstrap.rest;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;

import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.ws.rs.ext.Provider;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * Allows us to use things like DateTime as JAXRS parameters. Joda doesn't have a String constructor or valueOf().
 */
@Provider
public class JodaParamConverterProvider implements ParamConverterProvider {
	@Override
	public <T> ParamConverter<T> getConverter(final Class<T> aClass, final Type type, final Annotation[] annotations) {
		if (aClass == DateTime.class) {
			return new ParamConverter<T>() {
				@Override
				public T fromString(final String s) {
					return aClass.cast(new DateTime(s));
				}

				@Override
				public String toString(final T t) {
					return t.toString();
				}
			};
		} else if (aClass == LocalDateTime.class) {
			return new ParamConverter<T>() {
				@Override
				public T fromString(final String s) {
					return aClass.cast(new LocalDateTime(s));
				}

				@Override
				public String toString(final T t) {
					return t.toString();
				}
			};
		}

		return null;
	}
}
