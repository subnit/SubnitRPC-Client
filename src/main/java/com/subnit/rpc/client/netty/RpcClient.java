package com.subnit.rpc.client.netty;

import com.alibaba.fastjson.JSONObject;
import com.subnit.rpc.util.MethodInfoStr;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.nio.ByteBuffer;

/**
 * description:
 * date : create in 16:40 2018/6/7
 * modified by :
 *
 * @author subo
 */
public class RpcClient {
    ResultHandler resultHandler = new ResultHandler();

    @SuppressWarnings("unchecked")
    public <T> T create(final Object target) {

        return (T) Proxy.newProxyInstance(target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                new InvocationHandler() {

                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                        String message = buildSendingMessage(target, method, args);

                        EventLoopGroup group = new NioEventLoopGroup();
                        try {
                            Bootstrap b = new Bootstrap();
                            b.group(group)
                                    .channel(NioSocketChannel.class)
                                    .option(ChannelOption.TCP_NODELAY, true)
                                    .handler(new ChannelInitializer<SocketChannel>() {
                                        @Override
                                        public void initChannel(SocketChannel ch) throws Exception {
                                            ChannelPipeline pipeline = ch.pipeline();
                                            pipeline.addLast("handler", resultHandler);
                                        }
                                    });


                            ByteBuf buf = Unpooled.buffer();
                            buf.writeBytes(message.getBytes());
                            ChannelFuture future = b.connect("127.0.0.1", 9999).sync();
                            future.channel().writeAndFlush(buf).sync();
                            future.channel().closeFuture().sync();
                        } finally {
                            group.shutdownGracefully();
                        }

                        ByteBuffer response = (ByteBuffer) resultHandler.getResponse();
                        return new String(response.array());

                    }
                });
    }


    private String buildSendingMessage(Object target, Method method, Object[] args) {
        String ClassName = target.getClass().getName();
        String methodName = method.getName();
        Class<?>[] parameterTypes = method.getParameterTypes();
        String parameterTypesString = JSONObject.toJSONString(parameterTypes);
        String argsString = JSONObject.toJSONString(args);
        return JSONObject.toJSONString(new MethodInfoStr(ClassName, methodName, parameterTypesString, argsString));
    }


}
