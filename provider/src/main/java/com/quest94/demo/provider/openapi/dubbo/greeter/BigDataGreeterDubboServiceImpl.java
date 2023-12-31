package com.quest94.demo.provider.openapi.dubbo.greeter;

import com.quest94.demo.openapi.dubbo.greeter.GreeterDubboService;
import com.quest94.demo.provider.common.assertion.ParameterAssert;
import com.quest94.demo.provider.service.greeter.BigDataGreeterService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

@DubboService(group ="bigData", protocol = {"tri", "dubbo"})
public class BigDataGreeterDubboServiceImpl implements GreeterDubboService {

    @Autowired
    private BigDataGreeterService bigDataGreeterService;

    @Override
    public String sayHello(String name) {
        ParameterAssert.hasText(name, "name");
        System.out.println(name + " 执行了bigData服务");
        return bigDataGreeterService.sayHello(name);
    }

}
