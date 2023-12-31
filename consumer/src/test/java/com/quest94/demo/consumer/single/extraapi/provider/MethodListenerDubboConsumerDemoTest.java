package com.quest94.demo.consumer.single.extraapi.provider;

import com.quest94.demo.consumer.single.extraapi.provider.base.BaseProviderDubboConsumerDemoSingleTest;
import com.quest94.demo.openapi.dubbo.greeter.GreeterDubboService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Method;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.TimeUnit;

public class MethodListenerDubboConsumerDemoTest extends BaseProviderDubboConsumerDemoSingleTest {

    @DubboReference(group ="default",
//    @DubboReference(group ="timeout", timeout = 3000,
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
    public MethodInvokeListener methodInvokeListener() {
        return new MethodInvokeListener(5000);
    }

    public static class MethodInvokeListener {

        private final ThreadLocal<Long> startTime = new ThreadLocal<>();

        private final int sleepMillis;

        public MethodInvokeListener() {
            sleepMillis = 0;
        }

        public MethodInvokeListener(int sleepMillis) {
            this.sleepMillis = sleepMillis;
        }

        public void oninvoke(String name) {
            startTime.set(System.currentTimeMillis());
            System.out.println("sayHello oninvoke: " + name);
            sleep(sleepMillis);
        }

        public void onreturn(String result, String name) {
            long interval = System.currentTimeMillis() - startTime.get();
            System.out.println("sayHello onreturn: " + interval + ", " + name + ", " + result);
            sleep(sleepMillis);
        }

        public void onthrow(Exception exception, String name) {
            long interval = System.currentTimeMillis() - startTime.get();
            System.out.println("sayHello onthrow: " + interval + ", " + name + ", " + exception.getMessage());
            sleep(sleepMillis);
        }

        private void sleep(int sleepMillis) {
            if (sleepMillis <= 0) {
                return;
            }
            try {
                TimeUnit.MILLISECONDS.sleep(sleepMillis);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

}
