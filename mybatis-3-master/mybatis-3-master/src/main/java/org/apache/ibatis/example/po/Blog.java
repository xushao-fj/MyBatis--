package org.apache.ibatis.example.po;

/**
 * @author xsm
 * @Date 2021/1/16 12:02
 */
public class Blog {

  private Integer id;

  private String name;

  public Blog() {
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "Blog{" +
      "id=" + id +
      ", name='" + name + '\'' +
      '}';
  }
}
