/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.http;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import java.io.File;
import org.apache.http.client.methods.HttpPost;

/**
 *
 * @author root
 */
public class HttpGetTest {

    public static void main(String[] args) {

        CloseableHttpClient httpClient = null;

        try {
            SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(new File("/servers.keystore"), "123456".toCharArray()).build();
            SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext, new HostnameVerifier() {
                public boolean verify(String s, SSLSession sslSession) {
                    return true;
                }
            });

            PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(RegistryBuilder.<ConnectionSocketFactory>create().register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", sslConnectionSocketFactory).build());
            connectionManager.setMaxTotal(20);
            connectionManager.setDefaultMaxPerRoute(20);
            httpClient = HttpClients.custom().setSSLSocketFactory(sslConnectionSocketFactory).setConnectionManager(connectionManager).build();
            HttpPost httpPost = new HttpPost("https://10.11.22.228:8443/");
            System.out.println("Executing request " + httpPost.getRequestLine());
            CloseableHttpResponse response = httpClient.execute(httpPost);
            System.out.println("----------------------------------------");
            System.out.println(response.getStatusLine());
            System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != httpClient) {
                    httpClient.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
