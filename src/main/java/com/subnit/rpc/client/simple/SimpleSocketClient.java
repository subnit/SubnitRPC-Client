package com.subnit.rpc.client.simple;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Socket;

/**
 * 描述:
 * date : create in 21:37 2018/6/4
 * modified by :
 *
 * @author subo
 */
public class SimpleSocketClient {
    public static String ip;
    public static Integer port;

    @SuppressWarnings("unchecked")
    public static <T> T create(final Object target) {

        return (T) Proxy.newProxyInstance(target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                new InvocationHandler() {

                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        Socket socket = new Socket(ip, port);
                        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                       try {
                           out.writeUTF(target.getClass().getName());
                           out.writeUTF(method.getName());
                           out.writeObject(method.getParameterTypes());
                           out.writeObject(args);
                           ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
                           try {
                               Object result = input.readObject();
                               if (result instanceof Throwable) {
                                   throw (Throwable) result;
                               }
                               return result;
                           } finally {
                               input.close();
                           }
                       } finally {
                           out.close();
                           socket.close();
                       }

                    }
                } );
    }

}
