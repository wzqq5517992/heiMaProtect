package com.itcast.util;

import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariPoolMXBean;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataSourceTerminationTask implements Runnable {

  private static final Logger logger = LoggerFactory.getLogger(DataSourceTerminationTask.class);
  private static final int MAX_RETRY_TIMES = 10;
  private static final int RETRY_DELAY_IN_MILLISECONDS = 5000;

  private final DataSource dataSourceToTerminate;
  private final ScheduledExecutorService scheduledExecutorService;

  private volatile int retryTimes;

  public DataSourceTerminationTask(DataSource dataSourceToTerminate,
      ScheduledExecutorService scheduledExecutorService) {
    this.dataSourceToTerminate = dataSourceToTerminate;
    this.scheduledExecutorService = scheduledExecutorService;
    this.retryTimes = 0;
  }

  @Override
  public void run() {
    //老数据源的活动连接大于0，但重试时间大于最大重试时间，强制关闭老数据源连接，否则继续保持老数据源连接，然后定时继续检测老的数据源连接是否还存在
    if (terminate(dataSourceToTerminate)) {
      logger.info("Data source {} terminated successfully!", dataSourceToTerminate);
    } else {
      scheduledExecutorService.schedule(this, RETRY_DELAY_IN_MILLISECONDS, TimeUnit.MILLISECONDS);
    }
  }

  private boolean terminate(DataSource dataSource) {
    logger.info("Trying to terminate data source: {}", dataSource);

    try {
      //如果是Hikari数据源，进行动态数据源切换
      if (dataSource instanceof HikariDataSource) {
        return terminateHikariDataSource((HikariDataSource) dataSource);
      }

      logger.error("Not supported data source: {}", dataSource);

      return true;
    } catch (Throwable ex) {
      logger.warn("Terminating data source {} failed, will retry in {} ms, error message: {}", dataSource,
          RETRY_DELAY_IN_MILLISECONDS, ex.getMessage());
      return false;
    } finally {
      retryTimes++;
    }
  }


  //老数据源的活动连接大于0，但重试时间大于最大重试时间，强制关闭老数据源连接并返回true，否则返回false，但继续保持老数据源连接
  private boolean terminateHikariDataSource(HikariDataSource dataSource) {
    HikariPoolMXBean poolMXBean = dataSource.getHikariPoolMXBean();

    //驱除空闲连接
    poolMXBean.softEvictConnections();

    //活动连接大于0并且重试时间小于最大重试时间，返回false
    if (poolMXBean.getActiveConnections() > 0 && retryTimes < MAX_RETRY_TIMES) {
      logger.warn("Data source {} still has {} active connections, will retry in {} ms.", dataSource,
          poolMXBean.getActiveConnections(), RETRY_DELAY_IN_MILLISECONDS);
      return false;
    }

    //活动连接大于0，但重试时间大于最大重试时间，强制关闭老数据源连接
    if (poolMXBean.getActiveConnections() > 0) {
      logger.warn("Retry times({}) >= {}, force closing data source {}, with {} active connections!", retryTimes,
          MAX_RETRY_TIMES, dataSource, poolMXBean.getActiveConnections());
    }

    dataSource.close();

    return true;
  }
}
