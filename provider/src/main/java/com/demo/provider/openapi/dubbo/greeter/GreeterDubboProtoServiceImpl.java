package com.demo.provider.openapi.dubbo.greeter;

import com.demo.openapi.dubbo.greeter.DubboGreeterDubboProtoServiceTriple;
import com.demo.openapi.dubbo.greeter.GreeterDubboProtoReply;
import com.demo.openapi.dubbo.greeter.GreeterDubboProtoRequest;
import com.demo.provider.common.assertion.ParameterAssert;
import com.demo.provider.service.greeter.DefaultGreeterService;
import com.google.common.base.Stopwatch;
import org.apache.dubbo.common.stream.StreamObserver;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@DubboService(group = "proto", protocol = "tri")
public class GreeterDubboProtoServiceImpl extends DubboGreeterDubboProtoServiceTriple.GreeterDubboProtoServiceImplBase {

    @Autowired
    private DefaultGreeterService defaultGreeterService;

    @Override
    public GreeterDubboProtoReply greet(GreeterDubboProtoRequest request) {
        ParameterAssert.hasText(request.getName(), "name");
        System.out.println(request.getName() + " 执行了triple服务");
        String greeting = defaultGreeterService.sayHello(request.getName());
        return GreeterDubboProtoReply.newBuilder().setMessage(greeting).build();
    }

    @Override
    public StreamObserver<GreeterDubboProtoRequest> greetStream(StreamObserver<GreeterDubboProtoReply> responseObserver) {
        ParameterAssert.notNull(responseObserver, "responseObserver");
        final Stopwatch stopwatchOuter = Stopwatch.createStarted();
        String methodFlag = "greetStream";
        String methodFlagOuter = methodFlag + ".outer";
        printReceivedMessage("received", methodFlagOuter, stopwatchOuter);
        StreamObserver<GreeterDubboProtoRequest> requestStreamObserver = new StreamObserver<GreeterDubboProtoRequest>() {

            {
                printMessage(stopwatchOuter + " construct", methodFlag + ".init");
            }

            private final Stopwatch stopwatch = Stopwatch.createStarted();
            private final StringBuilder sb = new StringBuilder(" [");


            public void onNext(GreeterDubboProtoRequest GreeterDubboProtoRequest) {
                String methodFlagInternal = methodFlag + ".onNext";
                printReceivedMessage(GreeterDubboProtoRequest.getName(), methodFlagInternal, stopwatch);
                sleepAndRunAndPrintSentMessage(2, methodFlagInternal, stopwatch, () -> {
                    sb.append(GreeterDubboProtoRequest.getName()).append("; ");
                    responseObserver.onNext(buildGreeterDubboProtoReply(GreeterDubboProtoRequest));
                });
            }

            private GreeterDubboProtoReply buildGreeterDubboProtoReply(GreeterDubboProtoRequest GreeterDubboProtoRequest) {
                return GreeterDubboProtoReply.newBuilder()
                        .setMessage("Server " + stopwatch + " [" + GreeterDubboProtoRequest.getName() + "]")
                        .build();
            }


            public void onError(Throwable throwable) {
                printMessage(throwable, methodFlag + ".onError");
                responseObserver.onError(new IllegalStateException("Stream err"));
            }


            public void onCompleted() {
                String methodFlagInternal = methodFlag + ".onCompleted";
                printReceivedMessage("completed", methodFlagInternal, stopwatch);
                sleepAndRunAndPrintSentMessage(2, methodFlagInternal, stopwatch,
                        () -> {
                            sb.insert(0, stopwatch).append("]");
                            responseObserver.onNext(GreeterDubboProtoReply.newBuilder()
                                    .setMessage(sb.toString())
                                    .build());
                            responseObserver.onCompleted();
                        });
            }
        };
        sleep(3, methodFlagOuter);
        printEndMessage("end", methodFlagOuter, stopwatchOuter);
        return requestStreamObserver;
    }

    @Override
    public void greetServerStream(GreeterDubboProtoRequest request, StreamObserver<GreeterDubboProtoReply> responseObserver) {
        ParameterAssert.notNull(responseObserver, "responseObserver");
        Stopwatch stopwatchOuter = Stopwatch.createStarted();
        String methodFlag = "greetServerStream";
        String methodFlagOuter = methodFlag + ".outer";
        printReceivedMessage("received", methodFlagOuter, stopwatchOuter);
        sleepAndRunAndPrintForMessage(1, methodFlagOuter, stopwatchOuter,
                () -> responseObserver.onNext(buildGreeterDubboProtoReply(request, stopwatchOuter)));
        responseObserver.onCompleted();
        printEndMessage("end", methodFlagOuter, stopwatchOuter);
    }

    private GreeterDubboProtoReply buildGreeterDubboProtoReply(GreeterDubboProtoRequest GreeterDubboProtoRequest, Stopwatch stopwatch) {
        return GreeterDubboProtoReply.newBuilder()
                .setMessage("Server " + stopwatch + " [" + GreeterDubboProtoRequest.getName() + "]")
                .build();
    }

    private void sleep(int timeout, String methodFlag) {
        sleepAndRun(timeout, methodFlag, null, null);
    }

    private void sleepAndRunAndPrintForMessage(int timeout, String methodFlag, Stopwatch stopwatch, Runnable runnable) {
        IntStream.rangeClosed(1, 10)
                .forEach((i) -> sleepAndRun(timeout, methodFlag + "-For", stopwatch, runnable));
    }

    private void sleepAndRunAndPrintSentMessage(int timeout, String methodFlagInternal, Stopwatch stopwatch, Runnable runnable) {
        sleepAndRun(timeout, methodFlagInternal + "-Sent", stopwatch, runnable);
    }

    private void sleepAndRun(int timeout, String methodFlag, Stopwatch stopwatchOuter, Runnable runnable) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        try {
            TimeUnit.SECONDS.sleep(timeout);
        } catch (InterruptedException e) {
            printMessage(e, "sleepAndRun[" + methodFlag + "]");
        }
        if (Objects.isNull(methodFlag)) {
            return;
        }
        StringBuilder message = new StringBuilder();
        if (Objects.nonNull(stopwatchOuter)) {
            message.append(stopwatchOuter).append(" [");
        }
        message.append("sleep_elapsed ").append(stopwatch);
        if (Objects.nonNull(runnable)) {
            stopwatch = Stopwatch.createStarted();
            runnable.run();
            message.append(", runnable_elapsed ").append(stopwatch);
        }
        if (Objects.nonNull(stopwatchOuter)) {
            message.append(']');
        }
        printMessage(message.toString(), methodFlag);
    }

    private void printMessage(Throwable throwable, String methodFlag) {
        printMessage("请求流被写入了异常", methodFlag);
        throwable.printStackTrace();
    }

    private void printEndMessage(String message, String methodFlag, Stopwatch stopwatch) {
        printMessage(stopwatch + " [" + message + "]", methodFlag + "-End");
    }

    private void printReceivedMessage(String message, String methodFlag, Stopwatch stopwatch) {
        printMessage(stopwatch + " [" + message + "]", methodFlag + "-Received");
    }

    private void printMessage(String message, String methodFlag) {
        String now = DateTimeFormatter.ISO_TIME.format(LocalTime.now());
        System.out.println(now + " [" + Thread.currentThread().getName() + "] " + methodFlag + "：" + message);
    }

}
