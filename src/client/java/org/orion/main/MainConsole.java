package org.orion.main;

import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.orion.common.HttpUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class MainConsole {
    public static void main(String[] args) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://localhost:8080/upload");

        HttpResponse response = httpClient.execute(httpPost);
        JSONObject jsonObject = HttpUtils.getJSONFromResponse(response);
        System.out.println(jsonObject.get("Dick"));
    }
}
