package com.het.bleparselib.inner;

import com.het.bleparselib.utils.CRCUtil;
import com.het.bleparselib.utils.ConvertUtil;
import com.vise.common_bluetooth.utils.BleLog;
import com.vise.common_bluetooth.utils.HexUtil;

import java.nio.ByteBuffer;

/**
 * @Description: 完成命令组装
 * @author: <a href="http://xiaoyaoyou1212.360doc.com">DAWI</a>
 * @date: 2016-06-29 17:34
 */
public class CmdAssembleImp implements ICmdAssemble {

    private byte startFlag = CmdConstant.COMMAND_START_FLAG;
    private byte[] dataLength;
    private byte protocolVersion = CmdConstant.COMMAND_PROTOCOL_VERSION;
    private byte[] commandFlag;
    private byte[] data;
    private byte checkCode;

    @Override
    public void setStartFlag(byte startFlag) {
        this.startFlag = startFlag;
    }

    @Override
    public void setDataLength(byte[] dataLength) {
        this.dataLength = dataLength;
    }

    @Override
    public void setProtocolVersion(byte protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    @Override
    public void setCommandFlag(byte[] commandFlag) {
        this.commandFlag = commandFlag;
    }

    @Override
    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public void setCheckCode(byte checkCode) {
        this.checkCode = checkCode;
    }

    @Override
    public byte[] assembleCommand() {
        int length = 0;
        if(data != null){
            length = 7 + data.length;
            dataLength = ConvertUtil.intToBytesHigh(4 + data.length, 2);
        } else{
            length = 7;
            dataLength = ConvertUtil.intToBytesHigh(4, 2);
        }
        ByteBuffer buffer = ByteBuffer.allocate(length);
        buffer.put(startFlag);
        buffer.put(dataLength);
        buffer.put(protocolVersion);
        if(commandFlag != null){
            buffer.put(commandFlag);
        } else{
            buffer.put(new byte[2]);
        }
        if(data != null){
            buffer.put(data);
        } else{
            buffer.put(new byte[0]);
        }
        byte[] checkData = new byte[length - 2];
        System.arraycopy(buffer.array(), 1, checkData, 0, length - 2);
        BleLog.i("check data:"+ HexUtil.encodeHexStr(checkData));
        checkCode = CRCUtil.calcCrc8(checkData);
        buffer.put(checkCode);
        BleLog.i("send packet:"+ HexUtil.encodeHexStr(buffer.array()));
        return buffer.array();
    }

}
