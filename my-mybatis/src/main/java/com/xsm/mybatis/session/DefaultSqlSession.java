package com.xsm.mybatis.session;

import com.xsm.mybatis.executor.Executor;

/**
 * @author xsm
 * @Date 2021/1/17 17:45
 */
public class DefaultSqlSession implements SqlSession{

    /** 配置类*/
    private Configuration configuration;

    /** 执行器*/
    private Executor executor;

    @Override
    public <T> T selectOne(String statement) {
        return null;
    }
}
