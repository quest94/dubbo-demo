package com.demo.provider.manager.dubbo;

import com.demo.dubbo.GreeterService;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.RpcContext;


@DubboService(group ="default")
public class DefaultGreeterServiceImpl implements GreeterService {

    @Override
    public String sayHello(String name) {
        System.out.println(name + " 执行了default服务");
        return String.format("您好 %s！", name);
    }


}
