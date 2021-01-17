package com.xsm.mybatis.binding;

/**
 * @author xsm
 * @Date 2021/1/17 18:03
 */
public class MapperMethod<T> {
    private String sql;

    private Class<T> type;

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Class<T> getType() {
        return type;
    }

    public void setType(Class<T> type) {
        this.type = type;
    }
}
