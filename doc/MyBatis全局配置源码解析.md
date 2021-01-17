## MyBatis 插件执行原理
插件是MyBatis提供的一个扩展机制, 通过插件机制可以在SQL执行过程中的某些点上做一些自定义操作.  
实现一个插件, 首先需要让插件实现类实现`Interceptor`接口.然后在插件类上添加`@Intercepts`和`@Signature`注解,用于指定想要拦截的目标方法.MyBatis允许拦截接口中的一些方法.

1. 插件配置
- xml方式
```xml
<plugins>
    <plugin interceptor="org.apache.ibatis.example.plugin.SqlPrintInterceptor"/>
  </plugins>
```
- spring 配置方式
2. 存储入口  
org.apache.batis.builder.xml.XMLConfigBuilder#pluginElement

```java
 // 添加插件
  private void pluginElement(XNode parent) throws Exception {
    if (parent != null) {
      for (XNode child : parent.getChildren()) {
        String interceptor = child.getStringAttribute("interceptor");
        Properties properties = child.getChildrenAsProperties();
        Interceptor interceptorInstance = (Interceptor) resolveClass(interceptor).getDeclaredConstructor().newInstance();
        interceptorInstance.setProperties(properties); // 设置属性
        configuration.addInterceptor(interceptorInstance);
      }
    }
  }
```
org.apache.ibatis.session.Configuration.addInterceptor  将插件添加 configuration中`InterceptorChain` -> 持有一个 插件的List

插件解析首先是获取配置(mybatis-config.xml中的plugins配置,或者spring @Bean配置方式),然后再解析拦截器类型,并实例化拦截器.最后向拦截器中设置属性,并将拦截器添加到Configuration中

3. 使用

在获取一个`Sqlsession`中将插件添加进去

org.apache.ibatis.session.defaults.DefaultSqlSessionFactory#openSessionFromDataSource  
org.apache.ibatis.session.Configuration#newExecutor(org.apache.ibatis.transaction.Transaction, org.apache.ibatis.session.ExecutorType)

```java
public Executor newExecutor(Transaction transaction, ExecutorType executorType) {
    executorType = executorType == null ? defaultExecutorType : executorType;
    executorType = executorType == null ? ExecutorType.SIMPLE : executorType;
    Executor executor;
    if (ExecutorType.BATCH == executorType) {
      executor = new BatchExecutor(this, transaction);
    } else if (ExecutorType.REUSE == executorType) {
      executor = new ReuseExecutor(this, transaction);
    } else {
      executor = new SimpleExecutor(this, transaction);
    }
    if (cacheEnabled) {
      executor = new CachingExecutor(executor);
    }
    // 添加插件处理, 并生成 executor代理
    executor = (Executor) interceptorChain.pluginAll(executor);
    return executor;
  }
```
org.apache.ibatis.plugin.InterceptorChain#pluginAll  
org.apache.ibatis.plugin.Plugin#wrap  
在wrap方法中, 会处理插件中的`@Intercepts`注解中要拦截的sql方法(query, insert, update), 并生成`Executor`接口的代理类
org.apache.ibatis.plugin.Plugin.invoke
```java
 @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    try {
      Set<Method> methods = signatureMap.get(method.getDeclaringClass()); // 获取被拦截的方法签名
      if (methods != null && methods.contains(method)) {// 如果当前执行的方法属于被拦截的方法,那就执行代理对象的方法intercept
        return interceptor.intercept(new Invocation(target, method, args));
      }
      return method.invoke(target, args); // 如果没有方法被代理,则直接调用原方法
    } catch (Exception e) {
      throw ExceptionUtil.unwrapThrowable(e);
    }
  }
```

4. 总结:


