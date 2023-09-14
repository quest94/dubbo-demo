package com.demo.consumer.single;

import com.demo.consumer.single.base.BaseSingleDubboConsumerDemoTest;
import com.demo.dubbo.GreeterService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Method;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Bean;

public class ClusterDubboConsumerDemoTest extends BaseSingleDubboConsumerDemoTest {


    @DubboReference(group ="timeout", cluster = "failfast",
//    @DubboReference(group ="timeout", cluster = "failover",
            methods = {
                    @Method(name = "sayHello", oninvoke = "methodInvokeListener.oninvoke",
                            onreturn = "methodInvokeListener.onreturn",
                            onthrow = "methodInvokeListener.onthrow")
            }
    )
    private GreeterService greeterService;

    @Test
    public void test() {

        System.out.println(greeterService.sayHello("世界"));

    }

    @Bean
    public MethodListenerDubboConsumerDemoTest.MethodInvokeListener methodInvokeListener() {
        return new MethodListenerDubboConsumerDemoTest.MethodInvokeListener();
    }

}
