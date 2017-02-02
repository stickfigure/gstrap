/*
 * $Id$
 */

package servlet;

import com.google.common.io.BaseEncoding;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

/**
 * <p>A filter which requires very simple basic auth.  Just derive a subclass
 * that implements the getRealm() and authenticate() methods.</p>
 * 
 * <p>Does not interact with the J2EE security constraints
 * in any way, so it can be used to enable basic auth in Google App Engine.</p>
 * 
 * <p>Requires commons-codec.</p>
 *
 * @author Jeff Schnitzer
 */
@Slf4j
abstract public class BasicAuthFilter extends AbstractFilter {
	/**
	 * The header name for authorization
	 */
	public static final String AUTH_HEADER = "Authorization";

	/** */
	private String authenticateHeader;

	/**
	 * @return the ream which is advertised to clients
	 */
	abstract public String getRealm();

	/**
	 * @return true if the username/pw combo are valid
	 */
	abstract public boolean authenticate(String username, String password);

	/**
	 * Create the authorization header value
	 */
	public static String authHeader(String username, String password) {
			StringBuilder buf = new StringBuilder(username).append(':').append(password);

			// There is no standard for charset, might as well use utf-8
			byte[] bytes = buf.toString().getBytes(StandardCharsets.UTF_8);

			return "Basic " + BaseEncoding.base64().encode(bytes);
	}

	/**
	 * Useful tool:  Add http basic auth credentials to a connection
	 */
	public static void addBasicAuth(final URLConnection conn, final String username, final String password) {
		String header = authHeader(username, password);
		conn.setRequestProperty(AUTH_HEADER, header);

		if (log.isDebugEnabled())
			log.debug("Authorization header is: " + header);
	}

	/** */
	@Override
	public void init(FilterConfig cfg) throws ServletException {
		super.init(cfg);

		this.authenticateHeader = "Basic realm=\"" + this.getRealm() + "\"";
	}

	/** */
	@Override
	public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
		boolean good = false;

		try {
			final String authorization = request.getHeader("Authorization");
			if (authorization != null && authorization.startsWith("Basic ")) {
				String base64AuthInfo = authorization.substring("Basic ".length());
				
				// There is no charset standard for basic auth, utf-8 is as good as any
				String authInfo = new String(BaseEncoding.base64().decode(base64AuthInfo), StandardCharsets.UTF_8);
				String[] authParts = authInfo.split(":");

				if (this.authenticate(authParts[0], authParts[1])) {
					good = true;
				} else {
					if (log.isWarnEnabled())
						log.warn("Failed auth attempt for: " + authParts[0]);
				}
			} else {
				if (log.isWarnEnabled())
					log.warn("Bad authorization header: " + authorization);
			}
		} catch (Exception ex) {
			if (log.isWarnEnabled())
				log.warn("Error trying to parse authorization header", ex);
		}

		if (good) {
			chain.doFilter(request, response);
		} else {
			// return auth required
			response.addHeader("WWW-Authenticate", this.authenticateHeader);
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		}
	}
}