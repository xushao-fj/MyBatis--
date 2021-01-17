package com.xsm.mybatis.statement;

import com.xsm.mybatis.binding.MapperMethod;
import com.xsm.mybatis.resultset.DefaultResultSetHandler;
import com.xsm.mybatis.util.DbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author xsm
 * @Date 2021/1/17 18:09
 * 数据库连接
 */
public class StatementHandler {

    DefaultResultSetHandler resultSetHandler;

    public StatementHandler() {
        resultSetHandler = new DefaultResultSetHandler();
    }

    public <T> T query(MapperMethod mapperMethod, Object parameter) throws SQLException {
        Connection connection = DbUtil.getConnection();
        String sql = String.format(mapperMethod.getSql(), parameter);
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.execute();
        return resultSetHandler.handle(preparedStatement, mapperMethod, parameter);
    }
}
