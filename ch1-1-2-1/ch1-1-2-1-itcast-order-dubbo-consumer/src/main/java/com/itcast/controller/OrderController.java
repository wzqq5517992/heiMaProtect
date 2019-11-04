package com.itcast.controller;

import com.itcast.dto.OrderInfo;
import com.itcast.service.OrderService;
import com.itcast.utils.JsonUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {
    private Logger logger = LoggerFactory.getLogger(OrderController.class);

    //@Reference引用服务
    @Reference
    private OrderService orderService;

    @PostMapping(value = "/pay")
    @ResponseBody
    public String pay(@RequestBody OrderInfo orderInfo) {
        String strJson = null;
        String orderId = orderService.pay(orderInfo.getGoodsId());
        OrderInfo orderInfoNew = new OrderInfo();
        orderInfoNew.setId(orderId);
        orderInfoNew.setCampaignId(orderInfo.getCampaignId());
        orderInfoNew.setGoodsId(orderInfo.getGoodsId());
        orderInfoNew.setUserId(orderInfo.getUserId());
        strJson = JsonUtils.toText(orderInfoNew);
        return strJson;
    }
}
