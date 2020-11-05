package com.http.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;
import java.util.Map;

import javax.net.ssl.SSLContext;
import org.apache.http.StatusLine;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

/**
 * 使用 httpclient4.5 进行 https 通讯， 采用双向认证， 连接池管理connection
 *
 *
 */
@Component
public class HttpClientforSSLDual implements HttpClientforSSLInterface{

    public static HttpClientConnectionManager CONNECTION_MANAGER = null;

    /**
     * 初始化 connection manager.
     *
     * @param keyStoreFile
     * @param keyStorePass
     * @param trustStoreFile
     * @param trustStorePass
     * @throws Exception
     */
    
    @Override
    public void init(String keyStoreFile, String keyStorePass,
            String trustStoreFile, String trustStorePass) throws Exception {
        System.out.println("init conection pool...");

        InputStream ksis = new FileInputStream(new File(keyStoreFile));
        InputStream tsis = new FileInputStream(new File(trustStoreFile));

        KeyStore ks = KeyStore.getInstance("PKCS12");
        ks.load(ksis, keyStorePass.toCharArray());

        KeyStore ts = KeyStore.getInstance("JKS");
        ts.load(tsis, trustStorePass.toCharArray());

        SSLContext sslContext = SSLContexts.custom()
                .loadKeyMaterial(ks, keyStorePass.toCharArray())
                // 如果有 服务器证书
                .loadTrustMaterial(ts, new TrustSelfSignedStrategy())
                // 如果没有服务器证书，可以采用自定义 信任机制
                // .loadTrustMaterial(null, new TrustStrategy() {
                //
                // // 信任所有
                // public boolean isTrusted(X509Certificate[] arg0,
                // String arg1) throws CertificateException {
                // return true;
                // }
                //
                // })
                .build();
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslContext, new String[] { "TLSv1.2" }, null,
                SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

        Registry<ConnectionSocketFactory> registry = RegistryBuilder
                .<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", sslsf).build();
        ksis.close();
        tsis.close();
        CONNECTION_MANAGER = new PoolingHttpClientConnectionManager(registry);

    }

    /**
     * do post
     *
     * @param url
     * @param params
     * @throws Exception
     */
    public void post(String url, String params) throws Exception {
        if (CONNECTION_MANAGER == null) {
            return;
        }
        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(CONNECTION_MANAGER).build();
        HttpPost httpPost = new HttpPost(url);

        httpPost.setEntity(new StringEntity(params,
                ContentType.APPLICATION_JSON));

        CloseableHttpResponse resp = httpClient.execute(httpPost);
        System.out.println(resp.getStatusLine());
        InputStream respIs = resp.getEntity().getContent();
        String content = convertStreamToString(respIs);
        System.out.println(content);
        EntityUtils.consume(resp.getEntity());
    }

    @Override
    public String post(String url, Map<String, String> headers,
            String jsonStr) throws Exception {
        String re = null;
        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(CONNECTION_MANAGER).build();
        HttpPost httpPost = new HttpPost(url);
        if (headers != null) {
            for (Map.Entry<String, String> e : headers.entrySet()) {
                httpPost.addHeader(e.getKey(), e.getValue());
            }
        }
        httpPost.setEntity(new StringEntity(jsonStr,
                ContentType.APPLICATION_JSON));
        CloseableHttpResponse resp = httpClient.execute(httpPost);
        StatusLine statusLine = resp.getStatusLine();
        System.out.println(statusLine);
        if (statusLine.getStatusCode() == 200) {
            InputStream respIs = resp.getEntity().getContent();
            re = convertStreamToString(respIs);
        } else {

            re = statusLine.getReasonPhrase();
        }
        return re;
    }

    public static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
//        // 服务地址
//        String url = "https://localhost:8443";
//        // 服务参数，这里接口的参数采用 json 格式传递
//        String params = "{\"merchantCode\": \"www.demo.com\","
//                + "\"sessionId\": \"10000011\"," + "\"userName\": \"jack\","
//                + "\"idNumber\": \"432652515\"," + "\"cardNo\": \"561231321\","
//                + "\"phoneNo\": \"\"}";
//        // 私钥证书
//        String keyStoreFile = "D:\\Program Files\\Java\\jdk1.8.0_191\\jre\\lib\\security\\client.p12";
//        String keyStorePass = "123456";
//
//        // 配置信任证书库及密码
//        String trustStoreFile = "D:\\Program Files\\Java\\jdk1.8.0_191\\jre\\lib\\security\\cacerts";
//        String trustStorePass = "changeit";
//
//        HttpClientforSSLBackUp obj = new HttpClientforSSLBackUp();
//        try {
//            obj.init(keyStoreFile, keyStorePass, trustStoreFile, trustStorePass);
//            obj.post(url, params);
////            for (int i = 0; i < 10; i++) {
////                obj.post(url, params);
////            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
