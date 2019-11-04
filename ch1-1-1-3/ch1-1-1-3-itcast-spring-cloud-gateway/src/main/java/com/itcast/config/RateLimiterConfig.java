package com.itcast.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

/**
 * 路由限流配置
 */
@Configuration
public class RateLimiterConfig {

    /**
     * 针对来源IP的限流
     * @return
     */
    /*@Bean(value = "ipKeyResolver")
	public KeyResolver ipKeyResolver() {
		return exchange -> Mono.just(exchange.getRequest().getRemoteAddress().getAddress().getHostAddress());
	}*/

    /**
     * 按照Path/接口限流：限流规则即可作用在路径上
     * @return
     */
    @Bean(value = "pathKeyResolver")
    public KeyResolver pathKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getPath().value());
    }

    /**
     * 用户限流
     * 使用这种方式限流，请求路径中必须携带userId参数。
     * @return
     */
    /*@Bean(value = "userKeyResolver")
    public KeyResolver userKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getQueryParams().getFirst("userId"));
    }*/


}
