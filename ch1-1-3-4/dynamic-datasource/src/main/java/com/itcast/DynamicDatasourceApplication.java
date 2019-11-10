package com.itcast;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import com.itcast.entity.User;
import com.itcast.mapper.UserMapper;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableApolloConfig
@SpringBootApplication
@MapperScan("com.itcast.mapper")
public class DynamicDatasourceApplication implements CommandLineRunner {

  private static final Logger logger = LoggerFactory.getLogger(DynamicDatasourceApplication.class);

  public static void main(String[] args) {
    SpringApplication.run(DynamicDatasourceApplication.class, args);
  }

  @Autowired
  private UserMapper userRepository;

  @Override
  public void run(String... args) throws Exception {
    Executors.newSingleThreadExecutor().submit(() -> {
      while (true) {
        try {
            User user =  userRepository.selectByPrimaryKey("1");
            logger.info("查询用户名称："+user.getName());
          TimeUnit.SECONDS.sleep(1);
        } catch (Throwable ex) {
          ex.printStackTrace();
        }
      }
    });
  }
}
