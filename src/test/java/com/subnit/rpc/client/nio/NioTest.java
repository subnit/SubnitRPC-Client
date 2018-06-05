package com.subnit.rpc.client.nio;

import com.subnit.rpc.client.simple.SimpleSocketClient;
import com.subnit.rpc.service.HelloService;
import com.subnit.rpc.service.impl.HelloServiceImpl;
import org.junit.Test;

/**
 * description:
 * date : create in 22:19 2018/6/4
 * modified by :
 *
 * @author subo
 */


public class NioTest {


    @Test
    public void NioClientTest() {
        HelloService helloRpc = new HelloServiceImpl();
        NioSocketClient nioSocketClient = new NioSocketClient();
        nioSocketClient.setPort(9999);
        nioSocketClient.setIp("127.0.0.1");
        helloRpc = nioSocketClient.create(helloRpc);
        System.out.println(helloRpc.hello("rpc"));
    }


}
