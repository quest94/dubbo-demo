package com.quest94.demo.consumer.single.extraapi.provider;

import com.quest94.demo.consumer.single.extraapi.provider.base.BaseProviderDubboConsumerDemoSingleTest;
import com.quest94.demo.openapi.dubbo.greeter.GreeterDubboService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Method;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Bean;

public class ClusterDubboConsumerDemoTest extends BaseProviderDubboConsumerDemoSingleTest {


    @DubboReference(group ="timeout", cluster = "failfast",
//    @DubboReference(group ="timeout", cluster = "failover",
            methods = {
                    @Method(name = "sayHello", oninvoke = "methodInvokeListener.oninvoke",
                            onreturn = "methodInvokeListener.onreturn",
                            onthrow = "methodInvokeListener.onthrow")
            }
    )
    private GreeterDubboService greeterDubboService;

    @Test
    public void test() {

        System.out.println(greeterDubboService.sayHello("世界"));

    }

    @Bean
    public MethodListenerDubboConsumerDemoTest.MethodInvokeListener methodInvokeListener() {
        return new MethodListenerDubboConsumerDemoTest.MethodInvokeListener();
    }

}
