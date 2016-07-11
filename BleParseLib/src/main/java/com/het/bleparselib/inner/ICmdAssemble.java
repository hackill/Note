package com.het.bleparselib.inner;

/**
 * @Description: 命令组装接口
 * @author: <a href="http://xiaoyaoyou1212.360doc.com">DAWI</a>
 * @date: 2016-06-29 17:24
 */
public interface ICmdAssemble {

    /*设置起始标志*/
    void setStartFlag(byte startFlag);

    /*设置数据长度*/
    void setDataLength(byte[] dataLength);

    /*设置协议版本*/
    void setProtocolVersion(byte protocolVersion);

    /*设置命令字标识*/
    void setCommandFlag(byte[] commandFlag);

    /*设置数据*/
    void setData(byte[] data);

    /*设置校验码*/
    void setCheckCode(byte checkCode);

    /*组装命令*/
    byte[] assembleCommand();
}
