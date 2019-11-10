package com.itcast.util;

import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 动态数据源管理类：对HikariDataSource动态数据源进行管理
 */
@Component
public class DataSourceManager {

  private static final Logger logger = LoggerFactory.getLogger(DataSourceManager.class);

  @Autowired
  private CustomizedConfigurationPropertiesBinder binder;

  //数据源配置文件中信息绑定到DataSourceProperties中
  @Autowired
  private DataSourceProperties dataSourceProperties;

  /**
   * create a hikari data source
   *
   */
  public HikariDataSource createDataSource() {
    //实例化一个HikariDataSource对象，并从dataSourceProperties获取数据源信息
    HikariDataSource dataSource = dataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    if (StringUtils.hasText(dataSourceProperties.getName())) {
      dataSource.setPoolName(dataSourceProperties.getName());
    }
    //将HikariDataSource的bean 类型 注解等信息包装传递给CustomizedConfigurationPropertiesBinder处理
    Bindable<?> target = Bindable.of(HikariDataSource.class).withExistingValue(dataSource);
    //将属性资源文件中的值绑定到Bean中的（CustomizedConfigurationPropertiesBinder：委托配置绑定器来处理）
    this.binder.bind("spring.datasource.hikari", target);
    return dataSource;
  }

  public HikariDataSource createAndTestDataSource() throws SQLException {
    HikariDataSource newDataSource = createDataSource();
    try {
      testConnection(newDataSource);
    } catch (SQLException ex) {
      logger.error("Testing connection for data source failed: {}", newDataSource.getJdbcUrl(), ex);
      newDataSource.close();
      throw ex;
    }

    return newDataSource;
  }

  private void testConnection(DataSource dataSource) throws SQLException {
    Connection connection = dataSource.getConnection();
    connection.close();
  }
}
