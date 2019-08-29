package com.testDemo.trieMap;


import net.sf.triemap.TrieMap;

/**
 * @Auther: zouren
 * @Date: 2019/8/29 09:27
 * @Description:
 */
public class TriemapDemo {
    public static void main(String[] args) {
        TrieMap trieMap = new TrieMap();
        trieMap.add("ac");
        trieMap.add("bc");
        trieMap.add("ac1");
        String str =trieMap.getBestMatchingPath("ac1");
        System.out.println(str);


    }
}
