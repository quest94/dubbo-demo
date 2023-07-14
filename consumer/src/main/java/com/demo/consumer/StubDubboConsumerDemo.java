package com.demo.consumer;

import com.demo.DemoService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;

@EnableAutoConfiguration
public class StubDubboConsumerDemo {


    //    @DubboReference(version = "timeout", timeout = 1000, stub = "com.demo.DemoServiceStub")
    @DubboReference(version = "timeout", timeout = 1000, stub = "true")
    private DemoService demoService;

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(StubDubboConsumerDemo.class);

        DemoService demoService = context.getBean(DemoService.class);

        System.out.println((demoService.sayHello("周瑜")));


    }

}