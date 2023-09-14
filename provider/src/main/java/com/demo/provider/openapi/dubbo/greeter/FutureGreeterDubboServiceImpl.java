package com.demo.provider.openapi.dubbo.greeter;

import com.demo.common.assertion.ParameterAssert;
import com.demo.openapi.dubbo.greeter.GreeterDubboService;
import com.demo.provider.service.greeter.FutureGreeterService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.CompletableFuture;

@DubboService(group ="future")
public class FutureGreeterDubboServiceImpl implements GreeterDubboService {

    @Autowired
    private FutureGreeterService futureGreeterService;

    @Override
    public String sayHello(String name) {
        ParameterAssert.hasText(name, "name");
        System.out.println(name + " 执行了同步服务");
        return futureGreeterService.sayHello(name);
    }

    @Override
    public CompletableFuture<String> sayHelloFuture(String name) {
        ParameterAssert.hasText(name, "name");
        System.out.println(name + " 执行了异步方法返回值服务");
        return futureGreeterService.sayHelloFuture(name);
    }
}
