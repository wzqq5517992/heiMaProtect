package com.itcast.controller;

import com.itcast.dto.OrderInfo;
import com.itcast.service.OrderService;
import com.itcast.utils.JsonUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/order")
public class OrderController {
    private Logger logger = LoggerFactory.getLogger(OrderController.class);

    //@Reference引用服务
    @Reference(version = "${dubbo.service.version}")
    private OrderService orderService;

    @PostMapping(value = "/pay")
    @ResponseBody
    public String pay(@RequestBody OrderInfo orderInfo) throws ExecutionException, InterruptedException {
        logger.info("入参信息："+JsonUtils.toText(orderInfo));
        String strJson = null;
        OrderInfo orderInfoNew = new OrderInfo();
        //dubbo异步调用
        CompletableFuture<String> future = orderService.payAsync(orderInfo.getGoodsId());
        future.whenComplete((orderNo, exception) ->{
            if (exception == null) {
                logger.info("异步调用返回订单号："+orderNo);
            } else {
                exception.printStackTrace();
            }
        });
        //异步调用返回结果
        String orderNo = future.get();
        orderInfoNew.setId(orderNo);
        orderInfoNew.setCampaignId(orderInfo.getCampaignId());
        orderInfoNew.setGoodsId(orderInfo.getGoodsId());
        orderInfoNew.setUserId(orderInfo.getUserId());
        strJson = JsonUtils.toText(orderInfoNew);
        logger.info("出参信息："+strJson);
        return strJson;
    }
}
