package com.subnit.rpc.client.netty;

import com.subnit.rpc.client.nio.NioSocketClient;
import com.subnit.rpc.service.HelloService;
import com.subnit.rpc.service.impl.HelloServiceImpl;
import org.junit.Test;

/**
 * description:
 * date : create in 18:35 2018/6/7
 * modified by :
 *
 * @author subo
 */
public class RpcClientTest {
    @Test
    public void clientTest() {
        HelloService helloRpc = new HelloServiceImpl();
        RpcClient client = new RpcClient();
        helloRpc = client.create(helloRpc);
        System.out.println(helloRpc.hello("rpc"));


    }
}
