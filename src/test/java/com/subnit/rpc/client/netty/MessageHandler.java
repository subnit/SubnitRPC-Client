package com.subnit.rpc.client.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * description:
 * date : create in 20:25 2018/6/7
 * modified by :
 *
 * @author subo
 */
public class MessageHandler extends SimpleChannelInboundHandler {
    private String message;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {

    }
}
