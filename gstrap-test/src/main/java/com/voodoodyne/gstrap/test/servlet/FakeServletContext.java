/*
 */

package com.voodoodyne.gstrap.test.servlet;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Set;

/**
 * Mock ServletContext useful for guice injection in test cases where there is no container.
 * All methods throw UnsupportedOperationException.
 */
public class FakeServletContext implements ServletContext
{
	/** Turns out a dummy version of this method is useful */
	@Override
	public String getRealPath(String path)
	{
		if (!path.startsWith("/"))
			path = "/" + path;
		
		return "war" + path;
	}

	@Override
	public Object getAttribute(String arg0)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public Enumeration<String> getAttributeNames()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public ServletContext getContext(String arg0)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public String getContextPath()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public String getInitParameter(String arg0)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public Enumeration<String> getInitParameterNames()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public int getMajorVersion()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public String getMimeType(String arg0)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public int getMinorVersion()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public RequestDispatcher getNamedDispatcher(String arg0)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public RequestDispatcher getRequestDispatcher(String arg0)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public URL getResource(String arg0) throws MalformedURLException
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public InputStream getResourceAsStream(String arg0)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<String> getResourcePaths(String arg0)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public String getServerInfo()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public Servlet getServlet(String arg0) throws ServletException
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public String getServletContextName()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public Enumeration<String> getServletNames()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public Enumeration<Servlet> getServlets()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void log(String arg0)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void log(String arg0, Throwable arg1)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void log(Exception arg0, String arg1)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void removeAttribute(String arg0)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void setAttribute(String arg0, Object arg1)
	{
		throw new UnsupportedOperationException();
	}
}
