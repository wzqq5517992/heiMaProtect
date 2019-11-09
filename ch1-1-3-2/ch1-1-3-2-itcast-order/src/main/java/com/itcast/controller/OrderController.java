package com.itcast.controller;

import com.itcast.dto.OrderInfo;
import com.itcast.utils.JsonUtils;
import com.itcast.utils.ResponseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {
    private Logger logger = LoggerFactory.getLogger(OrderController.class);

    @PostMapping(value = "/pay")
    @ResponseBody
    public String pay(@RequestBody OrderInfo orderInfo) {
        String strJson = null;
        strJson = JsonUtils.toText(ResponseUtils.success("下单成功",orderInfo));
        return strJson;
    }
}

