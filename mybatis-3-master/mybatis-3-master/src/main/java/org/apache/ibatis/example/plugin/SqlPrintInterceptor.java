package org.apache.ibatis.example.plugin;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;

/**
 * @author xsm
 * @Date 2021/1/17 13:46
 * SQL执行时间输出 插件
 */
@Intercepts(value = {
  @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
  @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class,
    RowBounds.class, ResultHandler.class}),
  @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class,
    RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class})})
public class SqlPrintInterceptor implements Interceptor {

  private static final Logger log = LoggerFactory.getLogger(SqlPrintInterceptor.class);

  private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  @Override
  public Object intercept(Invocation invocation) throws Throwable {
    MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
    Object parameterObject = null;
    if (invocation.getArgs().length > 1) {
      parameterObject = invocation.getArgs()[1];
    }
    long start = System.currentTimeMillis();
    Object result = invocation.proceed();
    String statementId = mappedStatement.getId();
    BoundSql boundSql = mappedStatement.getBoundSql(parameterObject);
    Configuration configuration = mappedStatement.getConfiguration();
    String sql = getSql(boundSql, parameterObject, configuration);
    long end = System.currentTimeMillis();
    long timing = end - start;
    log.info("执行SQL耗时: {} ms, - Id: {} - Sql: {}", timing, statementId, sql);
    return result;
  }

  private String getSql(BoundSql boundSql, Object parameterObject, Configuration configuration) {
    String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
    List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
    TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
    if (parameterMappings != null) {
      for (int i = 0; i < parameterMappings.size(); i++) {
        ParameterMapping parameterMapping = parameterMappings.get(i);
        if (parameterMapping.getMode() != ParameterMode.OUT) {
          Object value;
          String propertyName = parameterMapping.getProperty();
          if (boundSql.hasAdditionalParameter(propertyName)) {
            value = boundSql.getAdditionalParameter(propertyName);
          } else if (parameterObject == null) {
            value = null;
          } else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
            value = parameterObject;
          } else {
            MetaObject metaObject = configuration.newMetaObject(parameterObject);
            value = metaObject.getValue(propertyName);
          }
          sql = replacePlaceholder(sql, value);
        }
      }
    }
    return sql;
  }

  private String replacePlaceholder(String sql, Object propertyValue) {
    String result;
    if (propertyValue != null) {
      if (propertyValue instanceof String) {
        result = "'" + propertyValue + "'";
      } else if (propertyValue instanceof Date) {
        result = "'" + DATE_FORMAT.format(propertyValue) + "'";
      } else {
        result = propertyValue.toString();
      }
    } else {
      result = "null";
    }
    return sql.replaceFirst("\\?", Matcher.quoteReplacement(result));
  }

  /**
   * 将插件包装设置到 executor中
   * @param target
   * @return
   */
  @Override
  public Object plugin(Object target) {
    if (target instanceof Executor) {
      // 把被拦截对象生成一个代理对象
      return Plugin.wrap(target, this);
    }
    return target;
  }

  @Override
  public void setProperties(Properties properties) {
    // 可以自定义一些属性
  }
}
