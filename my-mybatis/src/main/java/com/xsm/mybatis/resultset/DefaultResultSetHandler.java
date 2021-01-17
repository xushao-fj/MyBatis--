package com.xsm.mybatis.resultset;

import com.xsm.mybatis.binding.MapperMethod;

import java.sql.PreparedStatement;

/**
 * @author xsm
 * @Date 2021/1/17 18:11
 */
public class DefaultResultSetHandler {
    public <T> T handle(PreparedStatement preparedStatement, MapperMethod mapperMethod, Object parameter) {
        return null;
    }
}
