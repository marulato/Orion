package org.orion.common;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;

public class Http {

    private String url;
    private CloseableHttpClient httpClient;

    public Http(String url) {
        this.url = url;
        httpClient = HttpClientUtil.getDefaultClient();
    }

    public String post() {
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpPost);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            HttpClientUtil.close(null, httpPost, response);
        }
        return null;
    }

}
