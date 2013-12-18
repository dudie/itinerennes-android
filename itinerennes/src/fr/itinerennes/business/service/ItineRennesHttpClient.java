package fr.itinerennes.business.service;

import static android.os.Build.DEVICE;
import static android.os.Build.MODEL;
import static android.os.Build.VERSION.RELEASE;
import static android.os.Build.VERSION.SDK_INT;

import java.io.IOException;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
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
import org.apache.http.protocol.HttpContext;

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
public class ItineRennesHttpClient implements HttpClient {

    private final HttpClient delegate;

    public ItineRennesHttpClient(final Context context) {
        final HttpParams clientParams = new BasicHttpParams();
        clientParams.setParameter(HttpProtocolParams.USER_AGENT, buildUserAgent(context));
        delegate = new DefaultHttpClient(buildConnectionManager(), clientParams);
    }

    private Object buildUserAgent(Context context) {
        final String appVersion = VersionUtils.getCurrent(context);
        return String.format("ItineRennes/%s (Android/%s; SDK %s; %s; %s)",
                appVersion, RELEASE, SDK_INT, MODEL, DEVICE);
    }

    private ClientConnectionManager buildConnectionManager() {
        final SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", new PlainSocketFactory(), 80));
        registry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));

        final HttpParams cxParams = new BasicHttpParams();
        ConnManagerParams.setMaxTotalConnections(cxParams, 5);
        HttpConnectionParams.setConnectionTimeout(cxParams, 60000);

        return new ThreadSafeClientConnManager(
                cxParams, registry);
    }

    @Override
    public HttpResponse execute(HttpHost target, HttpRequest request,
            HttpContext context) throws IOException, ClientProtocolException {
        return delegate.execute(target, request, context);
    }

    @Override
    public <T> T execute(HttpHost arg0, HttpRequest arg1,
            ResponseHandler<? extends T> arg2, HttpContext arg3)
            throws IOException, ClientProtocolException {
        return delegate.execute(arg0, arg1, arg2, arg3);
    }

    @Override
    public <T> T execute(HttpHost arg0, HttpRequest arg1,
            ResponseHandler<? extends T> arg2) throws IOException,
            ClientProtocolException {
        return delegate.execute(arg0, arg1, arg2);
    }

    @Override
    public HttpResponse execute(HttpHost target, HttpRequest request)
            throws IOException, ClientProtocolException {
        return delegate.execute(target, request);
    }

    @Override
    public HttpResponse execute(HttpUriRequest request, HttpContext context)
            throws IOException, ClientProtocolException {
        return delegate.execute(request, context);
    }

    @Override
    public <T> T execute(HttpUriRequest arg0,
            ResponseHandler<? extends T> arg1, HttpContext arg2)
            throws IOException, ClientProtocolException {
        return delegate.execute(arg0, arg1, arg2);
    }

    @Override
    public <T> T execute(HttpUriRequest arg0, ResponseHandler<? extends T> arg1)
            throws IOException, ClientProtocolException {
        return delegate.execute(arg0, arg1);
    }

    @Override
    public HttpResponse execute(HttpUriRequest request) throws IOException,
            ClientProtocolException {
        return delegate.execute(request);
    }

    @Override
    public ClientConnectionManager getConnectionManager() {
        return delegate.getConnectionManager();
    }

    @Override
    public HttpParams getParams() {
        return delegate.getParams();
    }
}
