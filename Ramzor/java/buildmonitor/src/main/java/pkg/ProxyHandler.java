package pkg;

import java.io.IOException;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class ProxyHandler {

    private static HttpClient client = null;
    private static HttpPost httpPost = null;
    private static String url = null;

    public static void Init(String webServerURL) {
        url = webServerURL;
        HttpHost proxy = new HttpHost("proxy", 8080, "http");
        client = new DefaultHttpClient();
        client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
    }

    public static void postValue(Integer percentage) throws ClientProtocolException, IOException {
        String urlFull = url + "?value=" + URLEncoder.encode(percentage.toString(), "UTF-8");
        httpPost = new HttpPost(urlFull);
        HttpResponse response = client.execute(httpPost);
        HttpEntity entity = response.getEntity();
        EntityUtils.consume(entity);
        System.out.println("Post to Webserver:" + response.getStatusLine().getStatusCode());
    }

}
