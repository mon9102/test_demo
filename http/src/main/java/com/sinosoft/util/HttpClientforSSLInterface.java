/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sinosoft.util;

import java.util.Map;

/**
 *
 * @author root
 */
public interface HttpClientforSSLInterface {
    
    void init(String keyStoreFile, String keyStorePass, String trustStoreFile, String trustStorePass) throws Exception;
    
    String post(String url, Map<String, String> header, String jsonStr) throws Exception;
}
