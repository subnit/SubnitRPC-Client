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
        NioSocketClient client = nioSocketClient.init("127.0.0.1", 9999);
        helloRpc = client.create(helloRpc);
        System.out.println(helloRpc.hello("rpc"));
    }


}
