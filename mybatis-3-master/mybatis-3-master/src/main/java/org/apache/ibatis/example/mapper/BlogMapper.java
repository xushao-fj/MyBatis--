package org.apache.ibatis.example.mapper;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.example.po.Blog;

/**
 * @author xsm
 * @Date 2021/1/16 12:11
 */
public interface BlogMapper {

  @Select("SELECT * FROM blog WHERE id = #{id}")
  Blog selectBlog(int id);
}
