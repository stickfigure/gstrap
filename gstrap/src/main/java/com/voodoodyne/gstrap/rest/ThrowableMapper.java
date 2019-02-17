package com.voodoodyne.gstrap.rest;

import com.voodoodyne.gstrap.lang.Types;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.spi.Failure;

import javax.inject.Singleton;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.List;

/**
 * Translates exceptions in our application into a nice format understandable by programs and humans alike.
 * Leaves alone the exception responses that are internal to JAXRS.
 */
@Provider
@Singleton
@Slf4j
public class ThrowableMapper implements ExceptionMapper<Throwable> {

	@RequiredArgsConstructor
	public class ErrorBody {
		private final Throwable t;

		public String getMessage() {
			return t.getMessage();
		}

		public String getType() {
			return t.getClass().getSimpleName();
		}

		public List<String> getTypes() {
			return Types.getTypes(t, ClientException.class, RuntimeException.class, Exception.class, Throwable.class);
		}

		public ErrorBody getCause() {
			return t.getCause() == null ? null : new ErrorBody(t.getCause());
		}
	}

	@Override
	public Response toResponse(Throwable exception) {
		if (exception instanceof WebApplicationException) {
			return ((WebApplicationException)exception).getResponse();
		} else if (exception instanceof Failure) {
			return ((Failure)exception).getResponse();
		} else {
			log.error("Exception hit the top level handler", exception);

			final Status status = (exception instanceof ClientException)
					? ((ClientException)exception).getStatus()
					: Status.INTERNAL_SERVER_ERROR;

			return Response
					.status(status)
					.type(MediaType.APPLICATION_JSON_TYPE)
					.entity(new ErrorBody(exception))
					.build();
		}
	}
}
