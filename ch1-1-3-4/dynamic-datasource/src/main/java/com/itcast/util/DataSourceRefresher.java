package com.itcast.util;

import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import com.itcast.ds.DynamicDataSource;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 获取apollo配置动态推送数据，进行新老数据源切换，并关闭老数据源连接
 */
@Component
public class DataSourceRefresher implements ApplicationContextAware {

  private static final Logger logger = LoggerFactory.getLogger(DataSourceRefresher.class);

  private ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

  @Autowired
  private DynamicDataSource dynamicDataSource;

  @Autowired
  private DataSourceManager dataSourceManager;

  @Autowired
  private ApplicationContext applicationContext;

  @ApolloConfigChangeListener(interestedKeyPrefixes = "spring.datasource.")
  public void onChange(ConfigChangeEvent changeEvent) {
    refreshDataSource(changeEvent.changedKeys());
  }

  private synchronized void refreshDataSource(Set<String> changedKeys) {
    try {
      logger.info("Refreshing data source");

      /**
       * rebind configuration beans(更新变动配置的值以及相应的bean的属性值，主要是存在@ConfigurationProperties注解的bean(默认情况下@ConfigurationProperties注解标注的配置类是不会实时更新的))
       */
      this.applicationContext.publishEvent(new EnvironmentChangeEvent(changedKeys));
      //创建新数据源
      DataSource newDataSource = dataSourceManager.createAndTestDataSource();
      //将老数据源设置为新数据源，进行新老数据源切换，并将老数据源信息返回
      DataSource oldDataSource = dynamicDataSource.setDataSource(newDataSource);
      //处理老数据源连接，获取到老的连接池，并且校验是否有活动链接，直到没有任何活动链接时，关闭老的连接，防止连接遗漏，新老数据源切换流程完毕，新数据源生效。
      asyncTerminate(oldDataSource);

      logger.info("Finished refreshing data source");
    } catch (Throwable ex) {
      logger.error("Refreshing data source failed", ex);
    }
  }

/**
 * 在切换到新数据源后，获取老的数据源连接池，并且校验是否有活动链接，直到没有任何活动链接时，
 * 我们在关闭老的连接，避免链接遗漏。
 * 启动scheduledExecutorService周期调度线程池，检测老数据源连接是否关闭
 */
  private void asyncTerminate(DataSource dataSource) {
    DataSourceTerminationTask task = new DataSourceTerminationTask(dataSource, scheduledExecutorService);

    //start now
    scheduledExecutorService.schedule(task, 0, TimeUnit.MILLISECONDS);
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }
}
