package com.testDemo.rule;

import org.jeasy.rules.support.UnitRuleGroup;

/**
 * @Auther: zouren
 * @Date: 2019/3/28 11:21
 * @Description:
 */

public class FizzBuzzGroupRule extends UnitRuleGroup {

    public FizzBuzzGroupRule() {
        addRule(new BuzzRule());
        addRule(new FizzRule());
    }

    @Override
    public int getPriority() {
        return 0;
    }
}

