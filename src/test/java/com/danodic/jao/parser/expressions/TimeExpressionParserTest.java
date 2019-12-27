package com.danodic.jao.parser.expressions;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TimeExpressionParserTest {

    @DataProvider
    public Object [][] provideTimeExpressions() {
        return new Object[][] {
                new Object[] {"seconds 1", 1000L},
                new Object[] {"second 1", 1000L},
                new Object[] {"SECOND 1", 1000L},
                new Object[] {"SECONDS 1", 1000L},
                new Object[] {"Seconds 1", 1000L},
                new Object[] {"second 2", 2000L},
                new Object[] {"seconds 2", 2000L},
                new Object[] {"seconds 1.5", 1500L},
                new Object[] {"seconds 1.55", 1550L},
            };
    }

    @Test(dataProvider = "provideTimeExpressions")
    public void testTimeExpression(String expression, long expected) {
        assert TimeExpressionParser.parseExpression(expression) == expected;
    }
    
}