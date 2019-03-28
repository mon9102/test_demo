package com.testDemo.rule;
import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Fact;
import org.jeasy.rules.annotation.Rule;
/**
 * @Auther: zouren
 * @Date: 2019/3/28 11:20
 * @Description:
 */
@Rule(name = "BuzzRule", description = "BuzzRule description", priority = 2)
public class BuzzRule {

    @Condition
    public boolean isBuzz(@Fact("number") Integer number) {
        return number % 7 == 0;
    }

    @Action
    public void printBuzz(@Fact("number") Integer number) {
        System.out.print(number+"==buzz");
    }
}

