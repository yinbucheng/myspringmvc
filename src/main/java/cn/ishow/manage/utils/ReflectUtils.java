package cn.ishow.manage.utils;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

import java.util.LinkedList;
import java.util.List;

public class ReflectUtils {

    /**
     * 顺序获取方法上面参数名称
     * @param target
     * @param methodName
     * @return
     */
    public static List<String> listParamNames(Class target, String methodName){
        ClassPool pool = ClassPool.getDefault();
        try{
            CtClass ctClass = pool.get(target.getName());
            CtMethod ctMethod = ctClass.getDeclaredMethod(methodName);
            MethodInfo methodInfo = ctMethod.getMethodInfo();
            CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
            LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute
                    .getAttribute(LocalVariableAttribute.tag);
            List<String> paramNames = new LinkedList<String>();
            if (attr != null) {
                int len = ctMethod.getParameterTypes().length;
                int pos = Modifier.isStatic(ctMethod.getModifiers()) ? 0 : 1;
                for (int i = 0; i < len; i++) {
                    String key = attr.variableName(i + pos);
                    paramNames.add(key);
                }
            }
            return paramNames;

        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }
}
