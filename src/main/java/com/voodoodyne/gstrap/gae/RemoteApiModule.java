package com.voodoodyne.gstrap.gae;

import com.google.apphosting.utils.remoteapi.RemoteApiServlet;
import com.google.inject.servlet.ServletModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Singleton;

/**
 * Sets up the remote api on a particular path
 */
@RequiredArgsConstructor
@Slf4j
public class RemoteApiModule extends ServletModule {

	private final String remoteApiPath;

	@Override
	protected void configureServlets() {
		bind(RemoteApiServlet.class).in(Singleton.class);
		serve(remoteApiPath).with(RemoteApiServlet.class);
	}
}
