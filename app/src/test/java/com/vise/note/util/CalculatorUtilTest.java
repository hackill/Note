package com.vise.note.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by xyy on 16/6/25.
 */
public class CalculatorUtilTest {

    private CalculatorUtil calculatorUtil;

    @Before
    public void setUp() throws Exception {
        calculatorUtil = new CalculatorUtil();
    }

    @After
    public void tearDown() throws Exception {
        calculatorUtil = null;
    }

    @Test
    public void testPlus() throws Exception {
        assertEquals(6d, calculatorUtil.plus(1d, 5d), 0);
    }

    @Test
    public void testMinus() throws Exception {
        assertEquals(-4d, calculatorUtil.minus(1d, 5d), 0);
    }

    @Test
    public void testMultiply() throws Exception {
        assertEquals(5d, calculatorUtil.multiply(1d, 5d), 0);
    }

    @Test
    public void testDivide() throws Exception {
        assertEquals(0.2d, calculatorUtil.divide(1d, 5d), 0);
    }
}