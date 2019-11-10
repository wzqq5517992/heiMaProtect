package com.itcast.config;

import com.itcast.ds.DynamicDataSource;
import com.itcast.util.DataSourceManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class RefreshableDataSourceConfig {

  @Bean
  public DynamicDataSource dataSource(DataSourceManager dataSourceManager) {
    DataSource actualDataSource = dataSourceManager.createDataSource();
    return new DynamicDataSource(actualDataSource);
  }
}
