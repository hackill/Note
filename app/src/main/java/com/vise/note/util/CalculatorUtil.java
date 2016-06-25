package com.vise.note.util;

/**
 * 计算相关工具类
 * Created by xyy on 16/6/25.
 */
public class CalculatorUtil {
    public double plus(double a, double b){
        return a + b;
    }

    public double minus(double a, double b){
        return a - b;
    }

    public double multiply(double a, double b){
        return a * b;
    }

    public double divide(double a, double b) throws Exception {
        if(b == 0){
            throw new Exception("除数不能为零！");
        }
        return a / b;
    }

}
