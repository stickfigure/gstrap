/*
 */

package com.voodoodyne.gstrap.rest;

import com.google.inject.Injector;
import org.jboss.resteasy.plugins.guice.ModuleProcessor;
import org.jboss.resteasy.plugins.server.servlet.FilterDispatcher;
import org.jboss.resteasy.spi.Registry;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;


/**
 * More "Guicy" way of integrating Resteasy than the way provided by Resteasy.
 */
@Singleton
public class GuiceResteasyFilterDispatcher extends FilterDispatcher
{
	private Injector injector;

	@Inject
	public GuiceResteasyFilterDispatcher(Injector injector) {
		this.injector = injector;
	}

	/**
	 */
	@Override
	public void init(FilterConfig cfg) throws ServletException {
		super.init(cfg);

		final ServletContext context = cfg.getServletContext();
		final Registry registry = (Registry)context.getAttribute(Registry.class.getName());
		final ResteasyProviderFactory providerFactory = (ResteasyProviderFactory)context.getAttribute(ResteasyProviderFactory.class.getName());
		final ModuleProcessor processor = new ModuleProcessor(registry, providerFactory);

		processor.processInjector(injector);
	}
}
