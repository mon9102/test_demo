package com.testDemo.utils;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPObject;
import jdk.nashorn.internal.scripts.JS;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.junit.jupiter.api.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Auther: zouren
 * @Date: 2020/1/4 12:38
 * @Description: 大于：&gt;
 * <p>
 * 小于：&lt;
 * <p>
 * 大于等于：&gt;=
 * <p>
 * 小于等于：&lt;=
 */
public class StringUtilsTest {
    @Test
    public void containsIgnoreCase() {

        System.out.println(StringUtils.containsIgnoreCase("asdf-aa", "AS"));
        System.out.println(StringUtils.containsIgnoreCase("asdf-aa", "B"));
    }

    @Test
    public void joinWith() {

        System.out.println(StringUtils.joinWith("asdf-aa", "AS", "CC"));
        System.out.println(StringUtils.appendIfMissing("asdf-aa", "B"));
    }

    @Test
    public void subStr() {
        String symbol = "abvcefghijk";
        System.out.println(StringUtils.substring(symbol, -6));
        System.out.println(StringUtils.substring(symbol, 0, 6));
    }

    @Test
    public void test222() {
        String order = "id  asc , aa desC";

        String[] aa = StringUtils.stripAll(order.split(","), " ");
        System.out.println(aa);
    }

    @Test
    public void test33() {
        String content = FileUtil.readString("d:/yuanmeng.txt", "utf-8");
        String[] aa = content.split(",");
        int y = 0;
        for (int i = 0; i < aa.length; i++) {
            if (i % 15 == 0) {
                y = 0;
                System.out.print(",");
//                System.out.println();
//                System.out.print(aa[i]);
            } else {
                y++;
                if (y == 1) {
                    System.out.print("'" + aa[i].trim() + "'");
                }

//                System.out.print(",");
//                System.out.print(aa[i]);
            }
        }
    }

    @Test
    public void testJSON() {
        String content = FileUtil.readString("d:/yuanmeng202012111-37.txt", "utf-8");
        JSONObject data = JSONObject.parseObject(content);
        JSONArray array = data.getJSONArray("data");
        List<String> codes = array.stream().map(row -> ((JSONObject) row).getString("tranCode")).collect(Collectors.toList());
        for (int i = 0; i < codes.size(); i++) {
            System.out.print(",'" + codes.get(i).trim() + "'");
        }
        System.out.println();
        System.out.println(codes.size());
//        System.out.println("\n,'7396728','7396729','7396730','7396731','7396732','7396733','7396734','7396735','7396736','7396737','7396738','7396739','7396740','7396741','7396742','7396743','7396744','7396745','7396746','7396747','7396748','7396749','7396750','7396751','7396752','7396753','7396754','7396755','7396756','7396757','7396758','7396759','7396760','7396761','7396762','7396763','7396764','7396765','7396766','7396767','7396768','7396769','7396770','7396771','7396772','7396773','7396774','7396775','7396776','7396777','7396778','7396779','7396780','7396781','7396782','7396783','7396784','7396785','7396786','7396787','7396788','7396789','7396790','7396791','7396792','7396793','7396794','7396795','7396796','7396797','7396840','7396799','7396800','7396801','7396802','7396803','7396804','7396805','7396806','7396807','7396808','7396809','7396810','7396798','7396812','7396813','7396814','7396815','7396816','7396817','7396818','7396819','7396820','7396821','7396822','7396823','7396824','7396825','7396826','7396827','7396828','7396829','7396830','7396831','7396832','7396833','7396834','7396835','7396836','7396837','7396838','7396839','7396811'\n");

    }

    public String getSno(String sn) {
        sn = sn.replaceAll("\\D","");
        String ip = "";
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        String ipsn = null;
        if (ip != null) {
            ipsn = ip.substring(ip.length() - 3);
            ipsn = ipsn.replace(".", "");
        }
        else {
            int random = (int) (Math.random() * 10 + 1);
            ipsn = String.valueOf(random);
        }
        return sn + ipsn + getCount();
    }
    private static int totalCount = 0;

    private int getCount() {
        ++totalCount;
        if (totalCount > 9999) {
            totalCount = 0;
        }
        return totalCount;

    }
    public static List<Map<String, Object>> convertListData(Map<String, Object> paramRecRelatesMap, String key) {
        List<Map<String, Object>> paramRecRelatesList = new ArrayList<Map<String, Object>>();
        Object obj = paramRecRelatesMap.get(key);
        if (obj instanceof java.util.Map) {
            paramRecRelatesList.add((Map<String, Object>) paramRecRelatesMap.get(key));
        }
        else if (obj instanceof java.util.List) {
            paramRecRelatesList = (List<Map<String, Object>>) paramRecRelatesMap.get(key);
        }
        return paramRecRelatesList;
    }


}
