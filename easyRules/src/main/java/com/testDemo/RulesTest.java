package com.testDemo;

import com.testDemo.rule.*;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.jeasy.rules.core.RulesEngineParameters;
import org.junit.jupiter.api.Test;

/**
 * @Auther: zouren
 * @Date: 2019/3/28 11:18
 * @Description:
 */
public class RulesTest {
    @Test
    public void oldTest(){
        for(int i = 1; i <= 100; i++) {
            if (((i % 5) == 0) && ((i % 7) == 0))
                System.out.print("fizzbuzz");
            else if ((i % 5) == 0) System.out.print("fizz");
            else if ((i % 7) == 0) System.out.print("buzz");
            else System.out.print(i);
            System.out.println();
        }
        System.out.println();
    }
    @Test
    public void newTest(){
        // create a rules engine
        RulesEngineParameters parameters = new RulesEngineParameters().skipOnFirstAppliedRule(true);
        RulesEngine fizzBuzzEngine = new DefaultRulesEngine(parameters);
        org.slf4j.LoggerFactory a;
        // create rules
        Rules rules = new Rules();
//        rules.register(new FizzRule());
//        rules.register(new BuzzRule());
        rules.register(new FizzBuzzGroupRule());
//        rules.register(new NonFizzBuzzRule());
//        rules.register(new NoFizzBuzzGroupRule());

        // fire rules
        Facts facts = new Facts();
        for (int i = 5; i <= 100; i++) {
            facts.put("number", i);
            fizzBuzzEngine.fire(rules, facts);
            System.out.println();
        }

    }
}
