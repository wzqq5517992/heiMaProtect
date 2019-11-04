package com.itcast.filter;

import com.alibaba.fastjson.JSONObject;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * token校验全局过滤器
 */
@Component
public class AuthGatewayFilter implements GlobalFilter, Ordered{

    private static final String AUTHORIZE_TOKEN = "token";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest requestNew = exchange.getRequest();
        HttpHeaders headers = requestNew.getHeaders();
        String token = headers.getFirst(AUTHORIZE_TOKEN);
        if (token == null) {
            ServerHttpResponse response = exchange.getResponse();
            JSONObject message = new JSONObject();
            //提示信息
            message.put("status", -1);
            message.put("data", "token为空");
            byte[] bits = message.toJSONString().getBytes(StandardCharsets.UTF_8);
            DataBuffer buffer = response.bufferFactory().wrap(bits);
            //返回401状态码
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            //指定编码，否则在浏览器中会中文乱码
            response.getHeaders().add("Content-Type", "text/plain;charset=UTF-8");
            return response.writeWith(Mono.just(buffer));
        }
        return chain.filter(exchange);
    }

    //Ordered接口用来指定执行拦截器顺序（数字越小执行优先级越高）
    @Override
    public int getOrder() {
        return -1000;
    }
}