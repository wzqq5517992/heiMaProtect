package com.itcast.ds;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.concurrent.atomic.AtomicReference;
import javax.sql.DataSource;

/**
 * 动态数据源类：确保修改新老数据源对象引用时的线程安全性
 */
public class DynamicDataSource implements DataSource {
  //无锁对象原子引用，保证修改对象引用时的线程安全性
  private final AtomicReference<DataSource> dataSourceAtomicReference;

  public DynamicDataSource(DataSource dataSource) {
    dataSourceAtomicReference = new AtomicReference<>(dataSource);
  }

  /**
   * 将老数据源设置为新数据源，并将老数据源信息返回
   */
  public DataSource setDataSource(DataSource newDataSource){
    return dataSourceAtomicReference.getAndSet(newDataSource);
  }

  @Override
  public Connection getConnection() throws SQLException {
    return dataSourceAtomicReference.get().getConnection();
  }

  @Override
  public Connection getConnection(String username, String password) throws SQLException {
    return dataSourceAtomicReference.get().getConnection(username, password);
  }

  @Override
  public <T> T unwrap(Class<T> iface) throws SQLException {
    return dataSourceAtomicReference.get().unwrap(iface);
  }

  @Override
  public boolean isWrapperFor(Class<?> iface) throws SQLException {
    return dataSourceAtomicReference.get().isWrapperFor(iface);
  }

  @Override
  public PrintWriter getLogWriter() throws SQLException {
    return dataSourceAtomicReference.get().getLogWriter();
  }

  @Override
  public void setLogWriter(PrintWriter out) throws SQLException {
    dataSourceAtomicReference.get().setLogWriter(out);
  }

  @Override
  public void setLoginTimeout(int seconds) throws SQLException {
    dataSourceAtomicReference.get().setLoginTimeout(seconds);
  }

  @Override
  public int getLoginTimeout() throws SQLException {
    return dataSourceAtomicReference.get().getLoginTimeout();
  }

  @Override
  public java.util.logging.Logger getParentLogger() throws SQLFeatureNotSupportedException {
    return dataSourceAtomicReference.get().getParentLogger();
  }
}
