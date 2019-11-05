package com.itcast.service;


import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

//@Service 来标注其为 Dubbo 的一个服务(暴露服务)
@Service(version = "${dubbo.service.version}")
public class OrderServiceImpl implements OrderService{

    private Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);


    @Override
    @Async
    public CompletableFuture<String> payAsync(String message){
        String orderNo = UUID.randomUUID().toString()+message;
        logger.info("dubbo异步service返回订单号："+orderNo);
        return CompletableFuture.completedFuture(orderNo);
    }

}
