package cn.ishow.manage.domain;

import java.io.Serializable;
import java.lang.reflect.Method;


/**
 * 方法数据结构
 */
public class BeanDefination implements Serializable{
    private Object target;
    private Method method;

    public BeanDefination(Object target, Method method) {
        this.target = target;
        this.method = method;
    }

    public BeanDefination() {
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }
}
