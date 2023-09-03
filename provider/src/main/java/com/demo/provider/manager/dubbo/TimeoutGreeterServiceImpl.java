package com.demo.provider.manager.dubbo;

import com.demo.dubbo.GreeterService;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.RpcContext;

import java.util.concurrent.TimeUnit;


@DubboService(version = "timeout", timeout = 1000, parameters = "enable-timeout-countdown:true")
public class TimeoutGreeterServiceImpl implements GreeterService {

    @Override
    public String sayHello(String name) {
        System.out.println(name + " 执行了timeout服务");

        // 服务执行2秒
        // 服务超时时间为1秒，但是执行了2秒，服务端会把任务执行完的
        // 服务的超时时间，是指如果服务执行时间超过了指定的超时时间则会打印一个warning日志
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(name + " 执行结束");

        URL url = RpcContext.getServiceContext().getUrl();
        return String.format("%s：%s, Hello %s", url.getProtocol(), url.getPort(), name);  // 正常访问
    }

}