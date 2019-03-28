package com.testDemo.rule;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Fact;
import org.jeasy.rules.annotation.Rule;

/**
 * @Auther: zouren
 * @Date: 2019/3/28 11:17
 * @Description:
 */
@Rule(name = "NoFizzRule", description = "NoFizzRule description", priority = 1)
public class NoFizzRule {

        @Condition
        public boolean isFizz(@Fact("number") Integer number) {
            return number % 5 != 0;
        }
        @Action
        public void printFizz(@Fact("number") Integer number) {
            System.out.print(number+"==no fizz");
        }


}
