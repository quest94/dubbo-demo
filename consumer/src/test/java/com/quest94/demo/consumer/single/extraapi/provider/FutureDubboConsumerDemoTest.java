package com.quest94.demo.consumer.single.extraapi.provider;

import com.quest94.demo.consumer.single.extraapi.provider.base.BaseProviderDubboConsumerDemoSingleTest;
import com.quest94.demo.openapi.dubbo.greeter.GreeterDubboService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;


public class FutureDubboConsumerDemoTest extends BaseProviderDubboConsumerDemoSingleTest {

    @DubboReference(group ="future", timeout = 3000)
    private GreeterDubboService greeterDubboService;

    @Test
    public void test() {

        // 调用直接返回CompletableFuture
        CompletableFuture<String> future = greeterDubboService.sayHelloFuture("世界");

        future.whenComplete((v, t) -> {
            if (t != null) {
                t.printStackTrace();
            } else {
                System.out.println("Response: " + v);
            }
        });

        try {
            System.out.println("线程暂停");
            Thread.sleep(3000);
            System.out.println("线程恢复");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

}
