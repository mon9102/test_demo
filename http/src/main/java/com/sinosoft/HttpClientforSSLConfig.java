/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sinosoft;

import com.sinosoft.util.HttpClientforSSLDual;
import com.sinosoft.util.HttpClientforSSLInterface;
import com.sinosoft.util.HttpClientforSSLOne;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
