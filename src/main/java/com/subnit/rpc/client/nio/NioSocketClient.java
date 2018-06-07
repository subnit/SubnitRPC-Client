package com.subnit.rpc.client.nio;

import com.alibaba.fastjson.JSONObject;
import com.subnit.rpc.util.MethodInfoStr;
import lombok.Data;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * description:
 * date : create in 21:37 2018/6/4
 * modified by :
 *
 * @author subo
 */
@Data
public class NioSocketClient {
    private SocketChannel socketChannel;
    private ByteBuffer buffer = ByteBuffer.allocate(1024);
    private Selector selector = null;


    public NioSocketClient init(String serverIp, Integer port) {
        try {
            System.out.println("------客户端要启动了--------");
            selector = Selector.open();
            InetSocketAddress isa = new InetSocketAddress(serverIp, port);
            socketChannel = SocketChannel.open(isa);
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return this;
    }



    @SuppressWarnings("unchecked")
    public  <T> T create(final Object target) {

        return (T) Proxy.newProxyInstance(target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                new InvocationHandler() {

                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        String message = buildSendingMessage(target, method, args);
                        buffer.clear();
                        buffer.put(message.getBytes());
                        buffer.flip();

                        if (socketChannel.isConnectionPending()) {
                            socketChannel.finishConnect();
                        }


                            while(buffer.hasRemaining()) {
                            socketChannel.write(buffer);
                        }
                        return getResult();
                    }
                } );
    }


    private String getResult() {
        try {
            while (selector.select() > 0) {
                for (SelectionKey sk : selector.selectedKeys()) {
                    selector.selectedKeys().remove(sk);
                    if (sk.isReadable()) {
                        SocketChannel sc = (SocketChannel) sk.channel();
                        buffer.clear();
                        StringBuilder result = new StringBuilder();
                        sc.read(buffer);
                        buffer.flip();
                        while (buffer.hasRemaining()) {
                            result.append((char)buffer.get());
                        }
                        return result.toString();
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }


    private  String buildSendingMessage(Object target, Method method, Object[] args) {
        String ClassName = target.getClass().getName();
        String methodName = method.getName();
        Class<?>[] parameterTypes = method.getParameterTypes();
        String parameterTypesString = JSONObject.toJSONString(parameterTypes);
        String argsString = JSONObject.toJSONString(args);
        return JSONObject.toJSONString(new MethodInfoStr(ClassName, methodName, parameterTypesString, argsString));
    }
}
