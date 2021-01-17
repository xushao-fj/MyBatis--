
- 执行demo
```java
public class MyBatisMain {

  public static void main(String[] args) throws IOException {
    String resource = "mybatis-config.xml";
    InputStream inputStream = Resources.getResourceAsStream(resource);

    SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
    SqlSession session = sqlSessionFactory.openSession();
//    BlogMapper mapper = session.getMapper(BlogMapper.class);
//    Blog blog = mapper.selectBlog(113);
//    System.out.println(blog);
    Blog o = session.selectOne("org.apache.ibatis.example.mapper.BlogMapper.selectBlog", 113);
    System.out.println(o);
  }
}
```

### 获取数据库源
-> org.apache.ibatis.session.SqlSessionFactoryBuilder#build(java.io.InputStream, java.lang.String, java.util.Properties)  
-> org.apache.ibatis.builder.xml.XMLConfigBuilder#parse  
-> org.apache.ibatis.builder.xml.XMLConfigBuilder#environmentsElement

总结:

1. 先读取`mybatis-comfig.xml`配置文件
2. 通过`XMLConfigBuilder`解析配置信息, 并通过配置读取数据库配置等信息
3. 通过配置信息生成 事务管理器, datasource等核心组件, 并通过factory持有, 并设置到 environment中, 准备 `Environment` 的mybatis环境

### 获取执行语句
-> org.apache.ibatis.session.SqlSessionFactoryBuilder.build(java.io.InputStream)  
-> org.apache.ibatis.builder.xml.XMLConfigBuilder.parse  
-> org.apache.ibatis.builder.xml.XMLConfigBuilder.parseConfiguration  
-> org.apache.ibatis.builder.xml.XMLConfigBuilder.mapperElement  
-> org.apache.ibatis.builder.xml.XMLMapperBuilder.configurationElement  
-> org.apache.ibatis.builder.xml.XMLStatementBuilder.parseStatementNode  
-> org.apache.ibatis.session.Configuration.addMappedStatemen

### 执行操作源码过程

-> org.apache.ibatis.session.defaults.DefaultSqlSessionFactory.openSession() 获取session  
-> org.apache.ibatis.session.Configuration.newExecutor  
-> org.apache.ibatis.session.defaults.DefaultSqlSession.selectOne(java.lang.String, java.lang.Object)  
-> org.apache.ibatis.session.defaults.DefaultSqlSession.selectList(java.lang.String, java.lang.Object)  
-> org.apache.ibatis.executor.CachingExecutor.query(org.apache.ibatis.mapping.MappedStatement, java.lang.Object, org.apache.ibatis.session.RowBounds, org.apache.ibatis.session.ResultHandler)  
-> org.apache.ibatis.executor.CachingExecutor.query(org.apache.ibatis.mapping.MappedStatement, java.lang.Object, org.apache.ibatis.session.RowBounds, org.apache.ibatis.session.ResultHandler, org.apache.ibatis.cache.CacheKey, org.apache.ibatis.mapping.BoundSql)  
-> org.apache.ibatis.executor.BaseExecutor.queryFromDatabase  
-> org.apache.ibatis.executor.SimpleExecutor.doQuery  
-> org.apache.ibatis.executor.statement.PreparedStatementHandler.query  
-> org.apache.ibatis.executor.resultset.DefaultResultSetHandler.handleResultSets


![mybatis执行流程](https://cdn.nlark.com/yuque/0/2020/png/1387387/1607609572382-bc2438a1-fe6e-460d-9348-6f78069f8cd2.png?x-oss-process=image%2Fwatermark%2Ctype_d3F5LW1pY3JvaGVp%2Csize_20%2Ctext_6bKB54-t5a2m6Zmi5Ye65ZOB%2Ccolor_FFFFFF%2Cshadow_50%2Ct_80%2Cg_se%2Cx_10%2Cy_10)





