package com.testDemo.guava;

import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.HashSet;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Sets.newHashSet;

/**
 * @Auther: zouren
 * @Date: 2019/4/9 14:53
 * @Description:
 */
public class MapsTest {
    public static void main(String[] args) {
        {
            Map<String, String> mapA = newHashMap();
            mapA.put("a", "a1");
            mapA.put("b", "a1");
            mapA.put("c", "a1");
            mapA.put("d", "a1");
            mapA.put("e", "a1");
            Map<String, String> mapB = newHashMap();

            mapB.put("c", "a2");
            mapB.put("d", "a2");
            mapB.put("e", "a2");
            mapB.put("f", "a2");
            mapB.put("g", "a2");


            MapDifference<String, String> differenceMap = Maps.difference(mapA, mapB);

            System.out.println("  differenceMap.areEqual() : " + differenceMap.areEqual());
            //相同的key  c,d,e
            Map<String, MapDifference.ValueDifference<String>> entriesDiffering = differenceMap.entriesDiffering();
            for (Map.Entry<String, MapDifference.ValueDifference<String>> entry : entriesDiffering.entrySet()) {
                System.out.println("entriesDiffering 键 key ：" + entry.getKey() + " 值value ：" + entry.getValue());
            }
            System.out.println(" =======================");
            ////左边 排除相同  a,b
            Map<String, String> entriesOnlyOnLeft = differenceMap.entriesOnlyOnLeft();
            for (Map.Entry<String, String> entry : entriesOnlyOnLeft.entrySet()) {
                System.out.println("entriesOnlyOnLeft 键 key ：" + entry.getKey() + " 值value ：" + entry.getValue());
            }
            System.out.println(" =======================");
            ////右边 排除相同 f,g
            Map<String, String> entriesOnlyOnRight = differenceMap.entriesOnlyOnRight();
            for (Map.Entry<String, String> entry : entriesOnlyOnRight.entrySet()) {
                System.out.println("entriesOnlyOnRight 键 key ：" + entry.getKey() + " 值value ：" + entry.getValue());
            }
            System.out.println(" =======================");
            Map<String, String> entriesInCommon = differenceMap.entriesInCommon();
            for (Map.Entry<String, String> entry : entriesInCommon.entrySet()) {
                System.out.println("entriesInCommon 键 key ：" + entry.getKey() + " 值value ：" + entry.getValue());
            }


        }
    }
}
