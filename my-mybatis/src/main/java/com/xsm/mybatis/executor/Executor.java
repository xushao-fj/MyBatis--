package com.xsm.mybatis.executor;

import com.xsm.mybatis.binding.MapperMethod;

/**
 * @author xsm
 * @Date 2021/1/17 17:46
 */
public interface Executor {

    <T> T query(MapperMethod mapperMethod, Object parameter);
}
