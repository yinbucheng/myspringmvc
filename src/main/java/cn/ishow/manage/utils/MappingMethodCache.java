package cn.ishow.manage.utils;

import cn.ishow.manage.domain.BeanDefination;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 将映射与方法对应起来
 */
public class MappingMethodCache {
    private Map<String,BeanDefination> mappingCahe = new HashMap<>();

    public static MappingMethodCache getInstance(){
        return Inner.instance;
    }

    public void put(String mapper,BeanDefination method){
        BeanDefination temp = mappingCahe.get(mapper);
       if(temp!=null){
           throw new RuntimeException(mapper+"该映射对应的方法不唯一。");
       }
        mappingCahe.put(mapper,method);
    }

    public BeanDefination get(String mapper){
        return mappingCahe.get(mapper);
    }

    private static class Inner{
      private   static MappingMethodCache instance = new MappingMethodCache();
    }
}
