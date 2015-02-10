package com.pixelimpressions.www.jsonparsingexample;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by mikie on 2/10/15.
 */
public class ServiceHandler {

    public static final int GET = 1;
    public static final int POST = 2;

    public ServiceHandler() {

    }

    /**
     * uses apache HTTPClient and offers both POST and GET options to
     * get the data
     *
     * @param url    url to make the request
     * @param method http request method
     * @return
     */
    public String makeServiceCall(String url, int method) {
        return this.makeServiceCall(url, method, null);
    }

    private String makeServiceCall(String url, int method, List<NameValuePair> params) {
        String response = null;
        try {

            //http client
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpEntity httpEntity = null;
            HttpResponse httpResponse = null;

            //checking the http request method type
            if (method == POST) {
                HttpPost httpPost = new HttpPost(url);
                //adding post parameters
                if (params != null) {
                    httpPost.setEntity(new UrlEncodedFormEntity(params));
                }
                httpResponse = httpClient.execute(httpPost);
            } else if (method == GET) {
                //appending params to the url
                if (params != null) {
                    String paramString = URLEncodedUtils.format(params, "utf-8");
                    url += "?" + paramString;
                }
                HttpGet httpGet = new HttpGet(url);
                httpResponse = httpClient.execute(httpGet);
            }
            httpEntity = httpResponse.getEntity();
            response = EntityUtils.toString(httpEntity);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * This method uses the recommended httpUrlConnection class with GET to get
     * the data from the server.The response is converted into string to make it easy
     * to parse.This is lightweight and also better implemented tha the apache
     * httpclient class
     *
     * @param url url for the data
     * @return a string JSON
     */
    public String makeGetServiceCall(String url) {
        InputStream inputStream = null;
        int len = 500;
        try {
            URL requestUrl = new URL(url);
            //establish the connection
            HttpURLConnection httpURLConnection = (HttpURLConnection) requestUrl.openConnection();
            //make some settings
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestProperty("charset", "utf-8");

            //start the query
            httpURLConnection.connect();

            //get the stream
            inputStream = httpURLConnection.getInputStream();

            //Convert the response to String
            BufferedReader bReader = new BufferedReader(
                    new InputStreamReader(inputStream));
            StringBuilder sBuilder = new StringBuilder();
            String line;
            while ((line = bReader.readLine()) != null) {
                sBuilder.append(line + "\n");
            }
            bReader.close();
            return sBuilder.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //close the stream
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
