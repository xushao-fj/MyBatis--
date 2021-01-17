package org.apache.ibatis.example;

import org.apache.ibatis.example.mapper.BlogMapper;
import org.apache.ibatis.example.po.Blog;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author xsm
 * @Date 2021/1/16 11:39
 */
public class MyBatisCache {

  public static void main(String[] args) throws IOException {
    // mybatis-config.xml中的settings配置中开启二级缓存
    // 然后在对应mapper.xml中添加 <cache>标签

    String resource = "mybatis-config.xml";
    InputStream inputStream = Resources.getResourceAsStream(resource);
    SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

    //
    SqlSession session = sqlSessionFactory.openSession();
    Blog o = session.selectOne("org.apache.ibatis.example.mapper.BlogMapper.selectBlog", 113);
    System.out.println(o);

    SqlSession session1 = sqlSessionFactory.openSession();
    Blog o1 = session1.selectOne("org.apache.ibatis.example.mapper.BlogMapper.selectBlog", 113);
    System.out.println(o1);

    // 查看SQL执行,只执行了一次
  }

}
