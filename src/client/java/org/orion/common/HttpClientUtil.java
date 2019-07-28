package org.orion.common;

import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpClientUtil {

    public static CloseableHttpClient getDefaultClient() {
        return HttpClients.createDefault();
    }

    public static void setJSONPostDefaultHeader(HttpPost httpPost) {
        httpPost.setHeader("Content-Type", "application/json;charset=utf8");
    }

    public static void setTextPostDefaultHeader(HttpPost httpPost) {
        httpPost.setHeader("Content-Type", "application/text;charset=utf8");
    }

    public static List<NameValuePair> getDefaultPostParams(Map<String, String> paramPair) {
        List<NameValuePair> pairs = new ArrayList<>();
        if (paramPair != null && !paramPair.isEmpty()) {
            paramPair.forEach((key, value) -> {
                NameValuePair nameValuePair = new BasicNameValuePair(key, value);
                pairs.add(nameValuePair);
            });
        }
        return pairs;
    }

    public static StringEntity getEntityPostParams(Object entity) {
        if (entity != null) {
            JSONObject jsonEntity = JSONObject.fromObject(entity);
            String jsonString = jsonEntity.toString();
            StringEntity stringEntity = new StringEntity(jsonString, "UTF-8");
            return stringEntity;
        }
        return null;
    }

    public static String generateUrlParams(String... keyAndValues) {
        if (keyAndValues != null && keyAndValues.length > 0 && keyAndValues.length % 2 == 0) {
            StringBuilder url = new StringBuilder();
            url.append("?");
            for(int i = 0; i < keyAndValues.length; i++) {
                if (i % 2 == 0) {
                    url.append(keyAndValues[i]).append("=");
                } else {
                    url.append(keyAndValues[i]).append("&");
                }
            }
            url.deleteCharAt(url.length() - 1);
            return url.toString();
        }
        return "";
    }

    public static URI getLocalHttpURI(String path, Map<String, String> paramPair) {
        List<NameValuePair> requestParams = getDefaultPostParams(paramPair);
        URI uri = null;
        try {
            uri = new URIBuilder().setScheme("http").setHost("localhost").setPort(8080).setPath(path).
                    setParameters(requestParams).build();

        } catch (Exception e) {

        } finally {
            return uri;
        }
    }

    public static void setPostRequestParams(HttpPost httpPost, Map<String, String> paramPair) {
        List<NameValuePair> requestParams = getDefaultPostParams(paramPair);
        if (!requestParams.isEmpty()) {
            try {
                HttpEntity httpEntity = new UrlEncodedFormEntity(requestParams, "UTF-8");
                httpPost.setEntity(httpEntity);
            } catch (Exception e) {

            }
        }
    }

    public static void setPostRequestParams(HttpPost httpPost, Object entity) {
        if (entity != null) {
            StringEntity reqEntity = getEntityPostParams(entity);
            httpPost.setEntity(reqEntity);
        }
    }

    public static void setDefaultRequestConfig(HttpGet httpGet) {
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(10000).setConnectionRequestTimeout(5000).
                setRedirectsEnabled(true).build();
        httpGet.setConfig(requestConfig);
    }

    public static JSONObject getJSONFromResponse(HttpResponse response) {
       JSONObject jsonObject = null;
       try {
           jsonObject = JSONObject.fromObject(EntityUtils.toString(response.getEntity()));
       } catch (Exception e) {

       } finally {
           return jsonObject;
       }
    }

    public static void close(CloseableHttpClient client, HttpRequestBase request, CloseableHttpResponse response) {
        try {
            if (response != null) {
                response.close();
            }
            if (request != null) {
                request.releaseConnection();
            }
            if (client != null) {
                client.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
