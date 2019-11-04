package com.itcast.controller;

import com.itcast.dto.OrderInfo;
import com.itcast.utils.JsonUtils;
import com.itcast.utils.ResponseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Value("${server.port}")
    String port;

    @Value("${spring.application.name}")
    String appName;

    //持Reactive的操作类为 ReactiveRedisTemplate
    @Autowired
    private ReactiveRedisTemplate reactiveRedisTemplate;

    @PostMapping(value = "/pay")
    public Mono<OrderInfo> pay(@RequestBody OrderInfo orderInfo) {
        String key = "order_" + orderInfo.getId();
        ReactiveValueOperations<String, OrderInfo> operations = reactiveRedisTemplate.opsForValue();
        System.out.println("缓存赋值：appName: "+appName + "    port: " + port);
        return operations.getAndSet(key, orderInfo);
    }

    @PostMapping(value = "/findByOrderId")
    public Mono<OrderInfo> findByOrderId(@RequestBody OrderInfo orderInfo) {
        String key = "order_" + orderInfo.getId();
        ReactiveValueOperations<String, OrderInfo> operations = reactiveRedisTemplate.opsForValue();
        Mono<OrderInfo> orderNew = operations.get(key);
        System.out.println("缓存查询：appName: "+appName + "    port: " + port);
        return orderNew;
    }

    @PostMapping(value = "/deleteByOrderId")
    public Mono<Long> deleteByOrderId(@RequestBody OrderInfo orderInfo) {
        String key = "order_" + orderInfo.getId();
        System.out.println("缓存删除：appName: "+appName + "    port: " + port);
        return reactiveRedisTemplate.delete(key);
    }
}
