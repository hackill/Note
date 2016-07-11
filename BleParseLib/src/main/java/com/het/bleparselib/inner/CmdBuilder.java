package com.het.bleparselib.inner;

/**
 * @Description: 构建命令
 * @author: <a href="http://xiaoyaoyou1212.360doc.com">DAWI</a>
 * @date: 2016-06-30 10:48
 */
public class CmdBuilder {
    private ICmdAssemble skinCmdAssemble;

    public CmdBuilder() {
        this.skinCmdAssemble = new CmdAssembleImp();
    }

    public CmdBuilder setStartFlag(byte startFlag) {
        this.skinCmdAssemble.setStartFlag(startFlag);
        return this;
    }

    public CmdBuilder setDataLength(byte[] dataLength) {
        this.skinCmdAssemble.setDataLength(dataLength);
        return this;
    }

    public CmdBuilder setProtocolVersion(byte protocolVersion) {
        this.skinCmdAssemble.setProtocolVersion(protocolVersion);
        return this;
    }

    public CmdBuilder setCommandFlag(byte[] commandFlag) {
        this.skinCmdAssemble.setCommandFlag(commandFlag);
        return this;
    }

    public CmdBuilder setData(byte[] data) {
        this.skinCmdAssemble.setData(data);
        return this;
    }

    public CmdBuilder setCheckCode(byte checkCode) {
        this.skinCmdAssemble.setCheckCode(checkCode);
        return this;
    }

    public byte[] assembleCommand() {
        return this.skinCmdAssemble.assembleCommand();
    }
}
