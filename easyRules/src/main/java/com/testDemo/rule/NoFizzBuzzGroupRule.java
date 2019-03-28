package com.testDemo.rule;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Fact;
import org.jeasy.rules.support.ActivationRuleGroup;
import org.jeasy.rules.support.UnitRuleGroup;

/**
 * @Auther: zouren
 * @Date: 2019/3/28 11:21
 * @Description:
 */

public class NoFizzBuzzGroupRule extends ActivationRuleGroup {

    public NoFizzBuzzGroupRule() {
        addRule(new NoBuzzRule());
        addRule(new NoFizzRule());
    }

    @Override
    public int getPriority() {
        return 3;
    }

}

