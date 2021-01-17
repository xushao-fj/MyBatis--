package com.xsm.mybatis.executor;

import com.xsm.mybatis.binding.MapperMethod;
import com.xsm.mybatis.statement.StatementHandler;

import java.sql.SQLException;
import java.util.List;

/**
 * @author xsm
 * @Date 2021/1/17 18:06
 */
public class SimpleExecutor implements Executor {

    @Override
    public <T> T query(MapperMethod mapperMethod, Object parameter) {
        StatementHandler statementHandler = new StatementHandler();
        try {
            return statementHandler.query(mapperMethod, parameter);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
