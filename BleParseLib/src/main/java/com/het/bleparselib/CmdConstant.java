package com.het.bleparselib;

/**
 * @Description: 命令常量
 * @author: <a href="http://xiaoyaoyou1212.360doc.com">DAWI</a>
 * @date: 2016-06-30 13:46
 */
public class CmdConstant {

    public static final byte COMMAND_START_FLAG = 0x3A;

    public static final byte COMMAND_PROTOCOL_VERSION = 0x01;
    /*测肤仪发送命令*/
    public static final byte[] COMMAND_GET_DEVICE_TIME_FOR_SKIN = new byte[]{0x00, 0x22};
    public static final byte[] COMMAND_SET_DEVICE_TIME_FOR_SKIN = new byte[]{0x00, 0x23};
    public static final byte[] COMMAND_GET_DEVICE_RUN_STATUS_FOR_SKIN = new byte[]{0x00, 0x37};
    public static final byte[] COMMAND_SEND_DEVICE_CONFIG_FOR_SKIN = new byte[]{0x00, 0x40};
    /*测肤仪应答命令*/
    public static final byte[] COMMAND_RECEIVE_GET_DEVICE_TIME_FOR_SKIN = new byte[]{(byte) 0xA0, 0x22};
    public static final byte[] COMMAND_RECEIVE_SET_DEVICE_TIME_FOR_SKIN = new byte[]{(byte) 0xA0, 0x23};
    public static final byte[] COMMAND_RECEIVE_GET_DEVICE_RUN_STATUS_FOR_SKIN = new byte[]{(byte) 0xA0, 0x37};
    public static final byte[] COMMAND_RECEIVE_SEND_DEVICE_CONFIG_FOR_SKIN = new byte[]{(byte) 0xA0, 0x40};
}
