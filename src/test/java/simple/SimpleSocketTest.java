package simple;

import org.junit.Test;
import com.subnit.rpc.client.simple.SimpleSocketClient;
import com.subnit.rpc.service.HelloService;
import com.subnit.rpc.service.impl.HelloServiceImpl;

/**
 * 描述:
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
        SimpleSocketClient.port = 8888;
        helloRpc = SimpleSocketClient.create(helloRpc);
        System.out.println(helloRpc.hello("rpc"));
    }


}
