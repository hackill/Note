package com.het.bleparselib.utils;

/**
 * @Description: 相关转换工具类
 * @author: <a href="http://xiaoyaoyou1212.360doc.com">DAWI</a>
 * @date: 2016-07-08 17:01
 */
public class ConvertUtil {

    /**
     * int数值转换为byte数组，高位在前
     * @param value
     * @param n
     * @return
     */
    public static byte[] intToBytesHigh(int value, int n){
        byte[] src = new byte[n];
        for(int i = 0; i < n; i++){
            src[i] = (byte) ((value >> (8 * (n - i - 1))) & 0xFF);
        }
        return src;
    }

    /**
     * int数值转换为byte数组，低位在前
     * @param value
     * @param n
     * @return
     */
    public static byte[] intToBytesLow(int value, int n){
        byte[] src = new byte[n];
        for(int i = 0; i < n; i++){
            src[i] = (byte) ((value >> (8 * i)) & 0xFF);
        }
        return src;
    }

    /**
     * byte数组转换为int，高位在前
     * @param bytes
     * @param offset
     * @return
     */
    public static int bytesToIntHigh(byte[] bytes, int offset){
        int value = 0;
        if(bytes == null || bytes.length == 0){
            return value;
        }
        for(int i = 0; i < bytes.length; i++){
            value += (int) ((bytes[i] & 0xFF) << (8 * (bytes.length - i - 1)));
        }
        return value;
    }

    /**
     * byte数组转换为int，低位在前
     * @param bytes
     * @param offset
     * @return
     */
    public static int bytesToIntLow(byte[] bytes, int offset){
        int value = 0;
        if(bytes == null || bytes.length == 0){
            return value;
        }
        for(int i = 0; i < bytes.length; i++){
            value += (int) ((bytes[i] & 0xFF) << (8 * i));
        }
        return value;
    }
}
