package fr.itinerennes;

/**
 * Contains all error codes.
 * 
 * @author Jérémie Huchet
 */
public class ErrorCodeContants {

	/** A problem avoid the application to retrieve data from the network. */
	public static final short NETWORK = 0;

	/** A problem occurred while parsing the service response data and parsing it as Json. */
	public static final short JSON_MALFORMED = 1;

	/** The response from the Keolis API contains an error message. */
	public static final short KEOLIS_REQUEST_ERROR = 2;

	/** Unable to call the API. */
	public static final short API_CALL_FAILED = 3;
}
