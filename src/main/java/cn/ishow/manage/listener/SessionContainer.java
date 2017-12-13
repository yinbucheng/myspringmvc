package cn.ishow.manage.listener;

import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SessionContainer {

    //最多允许多少人同时在线
    public static Integer MAX_COUNT = 1000;

    private Map<String,HttpSession> sessionMap = new ConcurrentHashMap<>();
    private Set<String> treeIds = new TreeSet<>();//这里主要是为了防止内存溢出

    private SessionContainer(){

    }

    public void put(String uuid,HttpSession session){
        remove(uuid);
        if(treeIds.size()>MAX_COUNT){
            synchronized (this) {
                if (treeIds.size() > MAX_COUNT) {
                    //删掉前100个
                    List<String> temps = new ArrayList<>();
                    int tempCount = 1;
                    for (String tempKey : treeIds) {
                        temps.add(tempKey);
                        if (tempCount > 100)
                            break;
                        tempCount++;
                    }
                    for (String key : temps) {
                        treeIds.remove(key);
                        sessionMap.remove(key);
                    }
                }
            }
        }
        sessionMap.put(uuid,session);
        treeIds.add(uuid);
    }

    public void remove(String uuid){
        HttpSession session = sessionMap.remove(uuid);
        if(session!=null){
            session.invalidate();
        }
    }

    public static SessionContainer getInstance(){
        return  Inner.instance;
    }

    private static class Inner{
        private static SessionContainer instance =new SessionContainer();
    }
}
