package com.shaogezhu.easy.rpc.core.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

import static com.shaogezhu.easy.rpc.core.common.constants.RpcConstants.MAGIC_NUMBER;

/**
 * @Author peng
 * @Date 2023/2/24
 * @description: RPC解码器
 */
public class RpcDecoder extends ByteToMessageDecoder {

    /**
     * 协议的开头部分的标准长度
     */
    public final int BASE_LENGTH = 2 + 4;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) {
        if (byteBuf.readableBytes() >= BASE_LENGTH) {
            //防止收到一些体积过大的数据包 目前限制在1000大小
            if (byteBuf.readableBytes() > 1000) {
                byteBuf.skipBytes(byteBuf.readableBytes());
            }

            int beginReader = byteBuf.readerIndex();
            byteBuf.markReaderIndex();
            if (byteBuf.readShort() != MAGIC_NUMBER) {
                // 不是魔数开头，说明是非法的客户端发来的数据包
                ctx.close();
                return;
            }

            int length = byteBuf.readInt();
            //说明剩余的数据包不是完整的，这里需要重置下读索引
            if (byteBuf.readableBytes() < length) {
                byteBuf.readerIndex(beginReader);
                return;
            }
            byte[] data = new byte[length];
            byteBuf.readBytes(data);
            RpcProtocol rpcProtocol = new RpcProtocol(data);

            out.add(rpcProtocol);
        }
    }
}
