package com.subnit.rpc.client.simple;

import org.junit.Test;
import com.subnit.rpc.service.HelloService;
import com.subnit.rpc.service.impl.HelloServiceImpl;

/**
 * description:
 * date : create in 22:19 2018/6/4
 * modified by :
 *
 * @author subo
 */


public class SimpleSocketTest {


    @Test
    public void ClientTest() {
        HelloService helloRpc = new HelloServiceImpl();
        SimpleSocketClient.ip = "127.0.0.1";
        SimpleSocketClient.port = 9999;
        helloRpc = SimpleSocketClient.create(helloRpc);
        System.out.println(helloRpc.hello("rpc"));
    }


}
