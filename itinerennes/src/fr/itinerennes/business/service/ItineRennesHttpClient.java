package fr.itinerennes.business.service;

import static android.os.Build.DEVICE;
import static android.os.Build.MODEL;
import static android.os.Build.VERSION.RELEASE;
import static android.os.Build.VERSION.SDK_INT;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import android.content.Context;

import com.googlecode.androidannotations.annotations.EBean;

import fr.itinerennes.utils.VersionUtils;

/**
 * HttpClient tuned for ItineRennes.
 * 
 * <ul>
 * <li>custom ConnectionManager</li>
 * <li>custom UserAgent</li>
 * </ul>
 * 
 * @author Jeremie Huchet
 */
@EBean
public class ItineRennesHttpClient extends DefaultHttpClient {

    public ItineRennesHttpClient(final Context context) {
        super(buildConnectionManager(), buildClientParams(context));
    }

    private static ClientConnectionManager buildConnectionManager() {
        final SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", new PlainSocketFactory(), 80));
        registry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));

        final HttpParams cxParams = new BasicHttpParams();
        ConnManagerParams.setMaxTotalConnections(cxParams, 5);
        HttpConnectionParams.setConnectionTimeout(cxParams, 60000);

        return new ThreadSafeClientConnManager(
                cxParams, registry);
    }

    private static HttpParams buildClientParams(final Context context) {
        final HttpParams clientParams = new BasicHttpParams();
        clientParams.setParameter(HttpProtocolParams.USER_AGENT, buildUserAgent(context));
        return clientParams;
    }

    private static Object buildUserAgent(Context context) {
        final String appVersion = VersionUtils.getCurrent(context);
        return String.format("ItineRennes/%s (Android/%s; SDK %s; %s; %s)",
                appVersion, RELEASE, SDK_INT, MODEL, DEVICE);
    }
}
