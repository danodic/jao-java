package com.danodic.jao.parser.expressions;

import static com.danodic.jao.parser.expressions.ExpressionSupport.seconds;

import org.testng.annotations.Test;

public class ExpresssionsSupportTest {

    @Test
    public void validateSeconds() {
        assert seconds(1f) == 1000L;
        assert seconds(1.5f) == 1500L;
        assert seconds(1.75f) == 1750L;
        assert seconds(1.755f) == 1755L;
    }
    
}