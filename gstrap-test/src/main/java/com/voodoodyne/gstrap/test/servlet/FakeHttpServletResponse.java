/*
 */

package com.voodoodyne.gstrap.test.servlet;

import com.google.inject.servlet.RequestScoped;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * HttpServletResponse which does nothing.
 * 
 * All methods will throw UnsupportedOperationException.
 */
@RequestScoped
public class FakeHttpServletResponse extends HttpServletResponseWrapper {
	/** Create a stub interface via dynamic proxy that does nothing */
	private static HttpServletResponse makeStub() {
		return (HttpServletResponse)Proxy.newProxyInstance(
				Thread.currentThread().getContextClassLoader(),
				new Class<?>[] { HttpServletResponse.class },
				(proxy, method, args) -> {
					throw new UnsupportedOperationException();
				});
	}
	
	final Map<String, Object> attrs = new HashMap<>();
	
	public FakeHttpServletResponse() {
		// Can't actually pass null here
		super(makeStub());
	}
}
