package com.itcast;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

@SpringBootApplication
@EnableApolloConfig
public class EncryptApplication implements CommandLineRunner, EnvironmentAware {

    public static void main(String[] args) {
        SpringApplication.run(EncryptApplication.class, args);
    }

    @Value("${test.input}")
    private String input;

    private Environment environment;

    @Override
    public void run(String... args) throws Exception {
        while (true) {
            System.out.println("test.input 值 :" + input);
            TimeUnit.SECONDS.sleep(60);
        }
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
