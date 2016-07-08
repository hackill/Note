package com.het.bleparselib;

import android.content.Context;

import com.het.bleparselib.utils.CRCUtil;
import com.het.bleparselib.utils.ConvertUtil;
import com.vise.common_bluetooth.utils.BleLog;
import com.vise.common_bluetooth.utils.HexUtil;

/**
 * @Description: 解析数据包帮助类
 * @author: <a href="http://xiaoyaoyou1212.360doc.com">DAWI</a>
 * @date: 2016-06-30 13:59
 */
public class ParsePacketHelper {

    private byte[] buffer = new byte[60];
    private int bufferIndex = 0;
    private Context context;

    public ParsePacketHelper setContext(Context context) {
        this.context = context;
        return this;
    }

    /**
     * 解析包
     * @param bytes
     */
    public void parsePacket(byte[] bytes){
        if(bytes == null){
            return;
        }
        BleLog.i("receive packet:"+ HexUtil.encodeHexStr(bytes));
        if (0 != bufferIndex) {//如果当前buffer有数据，就直接拷贝
            if (bytes[bufferIndex] == CmdConstant.COMMAND_START_FLAG) {
                return;
            }
            System.arraycopy(bytes, 0, buffer, bufferIndex, bytes.length);
        } else {//如果没有数据，判断当前的数据头部是不是0x3A
            if(bytes[0] == CmdConstant.COMMAND_START_FLAG && bufferIndex == 0){
                System.arraycopy(bytes, 0, buffer, 0, bytes.length);
            }
        }
        //数据包拷进来后要移位
        bufferIndex += bytes.length;
        byte[] data = new byte[bufferIndex];
        System.arraycopy(buffer, 0, data, 0, data.length);
        if(isRightPacket(data)){
            bufferIndex = 0;
            buffer = new byte[60];
            parseCommand(data, new byte[]{data[4], data[5]});
        }
    }

    private void parseCommand(byte[] firstCmd, byte[] secondCmd) {

    }

    /**
     * 判定数据包是否正确
     * @param bytes
     * @return
     */
    private boolean isRightPacket(byte[] bytes){
        if(bytes == null || bytes.length < 7){
            return false;
        }
        byte[] checkData = new byte[bytes.length - 2];
        System.arraycopy(bytes, 1, checkData, 0, bytes.length - 2);
        boolean flag = (bytes[0] == CmdConstant.COMMAND_START_FLAG)
                && ((bytes.length - 3) == ConvertUtil.bytesToIntHigh(new byte[]{bytes[1], bytes[2]}, 0))
                && (bytes[bytes.length-1] == CRCUtil.calcCrc8(checkData));
        return flag;
    }

}
