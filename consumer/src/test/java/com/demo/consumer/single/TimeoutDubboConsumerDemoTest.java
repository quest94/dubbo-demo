package com.demo.consumer.single;

import com.demo.consumer.single.base.BaseSingleDubboConsumerDemoTest;
import com.demo.openapi.dubbo.greeter.GreeterDubboService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.jupiter.api.Test;


public class TimeoutDubboConsumerDemoTest extends BaseSingleDubboConsumerDemoTest {

    @DubboReference(group ="timeout")
    private GreeterDubboService greeterDubboService;

    @Test
    public void test() {

        try {
            // 服务调用超时时间为1秒，如果这1秒内没有收到服务结果，则会报错
            System.out.println(greeterDubboService.sayHello("世界"));
        } catch (Exception e) {
            System.out.println("执行出现了异常");
            e.printStackTrace();
        }

    }

}
