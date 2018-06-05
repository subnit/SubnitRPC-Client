package com.subnit.rpc.client.nio;

import com.alibaba.fastjson.JSONObject;
import com.subnit.rpc.util.MethodDTO;
import lombok.Data;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * description:
 * date : create in 21:37 2018/6/4
 * modified by :
 *
 * @author subo
 */
@Data
public class NioSocketClient {
    private  String ip;
    private  Integer port;
    private SocketChannel socketChannel;


    @SuppressWarnings("unchecked")
    public  <T> T create(final Object target) {

        return (T) Proxy.newProxyInstance(target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                new InvocationHandler() {

                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        socketChannel = SocketChannel.open();
                        socketChannel.configureBlocking(false);
                        socketChannel.connect(new InetSocketAddress(ip, port));
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

                        buffer.clear();
                        StringBuffer result = new StringBuffer();
                        while (socketChannel.read(buffer) != -1) {
                            result.append(Arrays.toString(buffer.array()));
                        }
                        socketChannel.close();
                        return result;
                    }
                } );
    }


    public  String buildSendingMessage(Object target, Method method, Object[] args) {
        String ClassName = target.getClass().getName();
        String methodName = method.getName();
        Class<?>[] parameterTypes = method.getParameterTypes();
        String parameterTypesString = JSONObject.toJSONString(parameterTypes);
        String argsString = JSONObject.toJSONString(args);
        return JSONObject.toJSONString(new MethodDTO(ClassName, methodName, parameterTypesString, argsString));
    }
}
