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
