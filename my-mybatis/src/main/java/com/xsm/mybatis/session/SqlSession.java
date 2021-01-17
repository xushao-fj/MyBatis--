package com.xsm.mybatis.session;

/**
 * @author xsm
 * @Date 2021/1/17 17:44
 */
public interface SqlSession {

    /**
     *
     * @param statement
     * @param <T>
     * @return
     */
    <T> T selectOne(String statement);
}
