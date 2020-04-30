/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.http;

import com.http.util.HttpClientforSSLOne;
import com.http.util.HttpClientforSSLDual;
import com.http.util.HttpClientforSSLInterface;
import org.springframework.stereotype.Component;

/**
 *
 * @author root
 */
@Component
public class HttpClientforSSLConfig {

    public HttpClientforSSLInterface getHttpClientforSSL(String Dual) {
        if (Dual.equalsIgnoreCase("1")) {
            return new HttpClientforSSLOne();
        }
        if (Dual.equalsIgnoreCase("2")) {
            return new HttpClientforSSLDual();
        }
        return null;
    }
}
