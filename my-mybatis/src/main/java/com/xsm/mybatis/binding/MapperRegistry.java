package com.xsm.mybatis.binding;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xsm
 * @Date 2021/1/17 18:02
 */
public class MapperRegistry {

    public void setKnowMappers(Map<String, MapperMethod> knowMappers) {
        this.knowMappers = knowMappers;
    }

    private Map<String, MapperMethod> knowMappers;

    public Map<String, MapperMethod> getKnowMappers() {
        return knowMappers;
    }
}
