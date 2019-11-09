package com.itcast;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableApolloConfig
@SpringBootApplication
public class CH1132GatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(CH1132GatewayApplication.class, args);
	}

}
