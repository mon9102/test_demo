package com.testDemo.jdk8.optional;

import org.junit.jupiter.api.Test;

import java.util.Optional;

/**
 * @Auther: zouren
 * @Date: 2019/3/26 14:24
 * @Description: Optional是Java8提供的为了解决null安全问题的一个API
 */
public class OptionalTest {
    private String name;
    /**
     * @Description: 下面这样的代码 改为newGetName
     * @Param: u
     * @Return: java.lang.String
     * @Author: zouren
     * @CreateDate: 2019/3/26 14:35
     */
    public static String oldGetName(User u) {
        if (u == null){
            return "Unknown";
        }
        return u.name;
    }
    public static String newGetName(User u) {
        return Optional.ofNullable(u)
                .map(user->user.name)
                .orElse("Unknown");
    }



    /**
     * @Description: 改为 newGetChampionName
     * @Param: comp  
     * @Return: java.lang.String
     * @Author: zouren
     * @CreateDate: 2019/3/26 14:36
     */
    public static String oldGetChampionName(Competition comp) throws IllegalArgumentException {
        if (comp != null) {
            CompResult result = comp.getResult();
            if (result != null) {
                User champion = result.getChampion();
                if (champion != null) {
                    return champion.getName();
                }
            }
        }
        throw new IllegalArgumentException("The value of param comp isn't available.");
    }
    public static String newGetChampionName(Competition comp) throws IllegalArgumentException {
        return Optional.ofNullable(comp)
                .map(c->c.getResult())
                .map(r->r.getChampion())
                .map(u->u.getName())
                .orElseThrow(()->new IllegalArgumentException("The value of param comp isn't available."));
    }
    /**
     * @Description:  为空则不打印可以这么写
     * @Author: zouren
     * @CreateDate: 2019/3/26 14:37
     */
    @Test
    public void printlnTest(){
        String param = "aaa";
        Optional<String> string = Optional.of(param);

        string.ifPresent(System.out::println);

    }
    /**
     * @Description: Optional可以用来检验参数的合法性
     * @Param: name
     * @Author: zouren
     * @CreateDate: 2019/3/26 14:41
     */
    public void setName(String name) throws IllegalArgumentException {
        this.name = Optional.ofNullable(name).filter(User::isNameValid)
                .orElseThrow(()->new IllegalArgumentException("Invalid username."));
    }

}
