package com.shaogezhu.easy.rpc.core.common;

import java.io.Serializable;
import java.util.Arrays;

import static com.shaogezhu.easy.rpc.core.common.constants.RpcConstants.MAGIC_NUMBER;

/**
 * @Author peng
 * @Date 2023/2/24
 * @description: 传输协议体，解决网络中的拆包和粘包问题
 */
public class RpcProtocol implements Serializable {

    private static final long serialVersionUID = 2669293150219020249L;

    /**
     * 魔法数,在做服务通讯的时候定义的一个安全检测，确认当前请求的协议是否合法。
     */
    private short magicNumber = MAGIC_NUMBER;
    /**
     * 协议传输核心数据的长度
     */
    private int contentLength;
    /**
     * 传输的数据
     */
    private byte[] content;

    public RpcProtocol(byte[] content) {
        this.contentLength = content.length;
        this.content = content;
    }

    public short getMagicNumber() {
        return magicNumber;
    }

    public void setMagicNumber(short magicNumber) {
        this.magicNumber = magicNumber;
    }

    public int getContentLength() {
        return contentLength;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "RpcProtocol{" +
                "contentLength=" + contentLength +
                ", content=" + Arrays.toString(content) +
                '}';
    }
}
