package com.itcast.service;


import org.apache.dubbo.config.annotation.Service;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//@Service暴露服务
@Service
public class OrderServiceImpl implements OrderService{
    private Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Override
    public String pay(String message){
        String orderId = UUID.randomUUID().toString()+message;
        logger.info(orderId);
        return orderId;
    }

}
