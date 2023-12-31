package com.quest94.demo.provider.openapi.dubbo.greeter;

import com.quest94.demo.openapi.dubbo.greeter.GreeterDubboService;
import com.quest94.demo.provider.common.assertion.ParameterAssert;
import com.quest94.demo.provider.service.greeter.ExceptionGreeterService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;


@DubboService(group ="exception")
public class ExceptionGreeterDubboServiceImpl implements GreeterDubboService {

    @Autowired
    private ExceptionGreeterService exceptionGreeterService;

    @Override
    public String sayHello(String name) {
        ParameterAssert.hasText(name, "name");
        System.out.println(name + " 执行了exception服务");
        return exceptionGreeterService.sayHello(name);
    }

}
