package fr.itinerennes.business.http.keolis;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import fr.itinerennes.beans.LineTransportIcon;
import fr.itinerennes.business.http.HttpResponseHandler;
import fr.itinerennes.exceptions.GenericException;

/**
 * Handles http responses containing bike stations in json format.
 * 
 * <pre>
 * "baseurl":"http:\/\/data.keolis-rennes.com\/uploads\/tx_icsinfotrafic\/",
 * "line":[
 *   {
 *     "name":"1",
 *     "picto":"LM1.png"
 *   }
 * ]
 * </pre>
 * 
 * @author Jérémie Huchet
 */
public final class LineIconsHttpResponseHandler extends
        HttpResponseHandler<List<LineTransportIcon>> {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory
            .getLogger(LineIconsHttpResponseHandler.class);

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.business.http.HttpResponseHandler#handleContent(java.lang.String)
     */
    @Override
    protected List<LineTransportIcon> handleContent(final String content) throws GenericException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("handleContent.start");
        }

        final JSONObject data = KeoUtils.getServiceResponse(content);

        List<LineTransportIcon> listIcons = null;

        if (null != data) {

            listIcons = new ArrayList<LineTransportIcon>();
            final String baseUrl = data.optString("baseurl");
            final JSONArray jsonIcons = data.optJSONArray("line");
            for (int i = 0; !jsonIcons.isNull(i); i++) {
                listIcons.add(convertJsonObjectToLineTransportIcon(baseUrl,
                        jsonIcons.optJSONObject(i)));
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("handleContent.end");
        }
        return listIcons;
    }

    /**
     * Converts a json object to a bean representing a line icon.
     * 
     * @param baseUrl
     *            the base url of the icon
     * @param jsonObject
     *            the json object to convert
     * @return the bean representing the given json object
     */
    private LineTransportIcon convertJsonObjectToLineTransportIcon(final String baseUrl,
            final JSONObject jsonObject) {

        final LineTransportIcon icon = new LineTransportIcon();
        icon.setLine(jsonObject.optString("name"));
        icon.setIconUrl(baseUrl + jsonObject.optString("picto"));
        return icon;
    }
}
