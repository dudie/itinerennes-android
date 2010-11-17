package fr.itinerennes.business.http;

import org.apache.http.HttpResponse;

import fr.itinerennes.ErrorCodeContants;
import fr.itinerennes.exceptions.GenericException;

/**
 * Classes implementing this interface may handle response retrieved with a
 * {@link GenericHttpService}.
 * 
 * @author Jérémie Huchet
 */
public class HttpResponseHandler<T> {

	/** The HTTP success result code. */
	private static final int HTTP_SUCCESS = 200;

	/**
	 * Check the code of the given HTTP response is equals to {@value #HTTP_SUCCESS}.
	 * 
	 * @param response
	 *            the HTTP response to handle
	 * @return null
	 * @throws GenericException
	 *             the response code is different from {@value #HTTP_SUCCESS}
	 */
	public T handleResponse(final HttpResponse response) throws GenericException {

		if (response == null || response.getStatusLine() == null
		        || response.getStatusLine().getStatusCode() != HTTP_SUCCESS) {
			throw new GenericException(ErrorCodeContants.NETWORK,
			        "HTTP response code different from 200");
		}
		return null;
	}
}
