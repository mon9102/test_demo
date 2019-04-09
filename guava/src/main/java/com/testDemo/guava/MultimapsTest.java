package com.testDemo.guava;

import com.google.common.base.Function;
import com.google.common.collect.*;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

/**
 * @Auther: zouren
 * @Date: 2019/4/9 14:16
 * @Description: 一键多值的Map
 * 有时候我们需要这样的数据类型Map<String,Collection<String>>，guava中的Multimap就是为了解决这类问题的。
 * <p>
 * Multimap的实现
 * <p>
 * Multimap提供了丰富的实现，所以你可以用它来替代程序里的Map<K, Collection<V>>，具体的实现如下：
 * <p>
 * 实现	                    Key实现	        Value实现
 * ArrayListMultimap	   HashMap	        ArrayList
 * HashMultimap	           HashMap	        HashSet
 * LinkedListMultimap	   LinkedHashMap	LinkedList
 * LinkedHashMultimap	   LinkedHashMap	LinkedHashSet
 * TreeMultimap	           TreeMap	        TreeSet
 * ImmutableListMultimap   ImmutableMap	    ImmutableList
 * ImmutableSetMultimap	   ImmutableMap	    ImmutableSet
 */
public class MultimapsTest {
    public static void main(String... args) {
        Multimap<String, String> myMultimap = ArrayListMultimap.create();

        // 添加键值对
        myMultimap.put("Fruits", "Bannana");
        //给Fruits元素添加另一个元素
        myMultimap.put("Fruits", "Apple");
        myMultimap.put("Fruits", "Pear");
        myMultimap.put("Vegetables", "Carrot");

        // 获得multimap的size
        int size = myMultimap.size();
        System.out.println(size);  // 4

        // 获得Fruits对应的所有的值
        Collection<String> fruits = myMultimap.get("Fruits");
        System.out.println(fruits); // [Bannana, Apple, Pear]

        Collection<String> vegetables = myMultimap.get("Vegetables");
        System.out.println(vegetables); // [Carrot]

        //遍历Mutlimap
        for (String value : myMultimap.values()) {
            System.out.println(value);
        }

        // Removing a single value
        myMultimap.remove("Fruits", "Pear");
        System.out.println(myMultimap.get("Fruits")); // [Bannana, Pear]

        // Remove all values for a key
        myMultimap.removeAll("Fruits");
        System.out.println(myMultimap.get("Fruits")); // [] (Empty Collection!)
    }

    /**
     * 分片集合
     */
    @Test
    public void index(){
        Map<String,String> row1 =  ImmutableMap.of("type","blog","id","292","author", "john");

        Map<String,String> row2=  ImmutableMap.of("type","blog","id","291","author", "john1");

        Map<String,String> row3=   ImmutableMap.of("type","new","id","391","author", "3john1");
        Map<String,String> row4=    ImmutableMap.of("type","new","id","321","author", "2john1");
        List<Map<String, String>> listOfMaps = Lists.newArrayList(row1,row2,row3,row4);
        Multimap<String, Map<String, String>> partitionedMap = Multimaps.index(listOfMaps, new Function<Map<String, String>, String>() {
	            public String apply(final Map<String, String> from) {
                        return from.get("type");
                   }
	    });
        System.out.printf(partitionedMap.toString());
    }
}
