package cn.ishow.manage.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 读取配置文件工具类
 */
public abstract class ReadUtils {
    private static Map<String,Object> cache = new HashMap<>();

    private static Properties properties = null;

    static {
        properties = new Properties();
        try {
            properties.load(ReadUtils.class.getResourceAsStream("/config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static Object read(String key){
        Object value = cache.get(key);
        if(value==null){
            synchronized (ReadUtils.class){
                value = cache.get(key);
                if(value==null) {
                    value = properties.get(key);
                    cache.put(key, value);
                }
            }
        }
        return value;
    }

}
