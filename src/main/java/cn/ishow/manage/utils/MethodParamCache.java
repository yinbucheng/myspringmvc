package cn.ishow.manage.utils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 方法名称和里面参数名称对应缓存
 */
public  class MethodParamCache {

    private Map<String,List<String>> paramMap = new HashMap<>();
    private MethodParamCache(){

    }

    public static MethodParamCache getInstance(){
        return Inner.methodParamCache;
    }

    public void put(String methodName,List<String> paramsName){
        paramMap.put(methodName,paramsName);
    }

    public List<String> get(String methodName){
        return paramMap.get(methodName);
    }

    private static class Inner{
        private static MethodParamCache methodParamCache = new MethodParamCache();
    }
}
