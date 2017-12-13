package cn.ishow.manage.system;

import cn.ishow.manage.annotation.Controller;
import cn.ishow.manage.annotation.RequestMapping;
import cn.ishow.manage.annotation.ResponseBody;
import cn.ishow.manage.domain.BeanDefination;
import cn.ishow.manage.utils.MappingMethodCache;
import cn.ishow.manage.utils.MethodParamCache;
import cn.ishow.manage.utils.ReadUtils;
import cn.ishow.manage.utils.ReflectUtils;
import com.alibaba.fastjson.JSON;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class DispatchServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            doServlet(req, resp);
    }

    private void doServlet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            RequestContextHodler.setAttribute(req,resp);
           Object value =  invokeMethod(req, resp);
           String str = (String) value;
            resp.setHeader("Content-type", "text/html;charset=UTF-8");
            resp.setCharacterEncoding("utf-8");
            resp.getWriter().write(str);
        } catch (Exception e) {
           throw new RuntimeException(e);
        }finally {
            RequestContextHodler.remove();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
               doServlet(req, resp);
    }

    private Object invokeMethod(HttpServletRequest request,HttpServletResponse response)throws Exception{
        String uri = request.getRequestURI();
        BeanDefination beanDefination = MappingMethodCache.getInstance().get(uri);
        if(beanDefination==null){
            return "{\"code\":500,\"msg\":\"no mapping method find\"}";
        }
        Object target = beanDefination.getTarget();
        Method method = beanDefination.getMethod();
        String methodName = method.getName();

       List<String> paramsName = null;
       paramsName = MethodParamCache.getInstance().get(methodName);
       if(paramsName==null) {
        paramsName =  ReflectUtils.listParamNames(target.getClass(), methodName);
        MethodParamCache.getInstance().put(methodName,paramsName);
       }

       if(paramsName==null||paramsName.size()==0) {
           Object object =  method.invoke(target, null);
           return parseObjectToString(method, object);
       }

       //参数抓换
        List<String> values = new ArrayList<>();
        if(paramsName!=null&&paramsName.size()>0){
            for(String paramName:paramsName){
               String value =  request.getParameter(paramName);
              values.add(value);
            }
        }

        Class[] paramterTyps = method.getParameterTypes();
        List<Object> params = new ArrayList<>(values.size());
        for(int i=0;i<paramterTyps.length;i++){
            Class paramterType = paramterTyps[i];
            String value = values.get(i);
            if(paramterType.equals(String.class)){
                params.add(value);
            }else if(paramterType.equals(Integer.class)){
                Integer temp = Integer.parseInt(value);
                params.add(temp);
            }else if(paramterType.equals(Long.class)){
                Long temp = Long.parseLong(value);
                params.add(temp);
            }else if(paramterType.equals(Boolean.class)){
                Boolean temp = Boolean.parseBoolean(value);
                params.add(temp);
            }else{
                throw new RuntimeException("目前还没提供该类型支持");
            }
        }

        Object object =    method.invoke(target,params.toArray());
         return parseObjectToString(method, object);

    }

    private Object parseObjectToString(Method method, Object object) {
        boolean flag = method.isAnnotationPresent(ResponseBody.class);
        if(flag){
            return JSON.toJSONString(object);
        }else{
            return object;
        }
    }

    @Override
    public void init() throws ServletException {
        super.init();
        String webPackage = (String) ReadUtils.read("web_scan");
        String totalPath = resovleTotalPath(webPackage);
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>totalPath:"+totalPath);
        List<String> classNames = new ArrayList<>();
        //解析className
        parseClassName(totalPath, webPackage, classNames);
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>classNames:"+classNames);
        resolveMethodMapping(classNames);
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>解析完成................");
    }

    /**
     * 产生该包下的全路径
     * @param webPackage
     * @return
     */
    private String resovleTotalPath(String webPackage) {
        //扫码所有的包并把其放入到访问关系和方法放入到内存中
        File f = new File(getClass().getResource("/").getPath());
        String totalPath = f.getAbsolutePath();
        System.out.println(totalPath);
        String pageName = getClass().getPackage().getName().replace(".","\\");
        totalPath = totalPath.replace(pageName,"");
        String  packagePath = webPackage.replace(".","\\");
        totalPath=totalPath+"\\"+packagePath;
        return totalPath;
    }

    private void resolveMethodMapping(List<String> classNames) {
        if(classNames.size()!=0){
            for(String className:classNames){
                try{
                  Class clazz = Class.forName(className);
                  Object target = clazz.newInstance();
                  boolean flag = clazz.isAnnotationPresent(Controller.class);
                  if(!flag)
                      continue;
                 RequestMapping head = (RequestMapping) clazz.getAnnotation(RequestMapping.class);
                 String headUrl = head.value();
                 Method[]  methods = clazz.getMethods();
                 if(methods==null||methods.length==0)
                     continue;
                 for(Method method:methods){
                    RequestMapping body =  method.getAnnotation(RequestMapping.class);
                    if(body==null)
                        continue;;
                  String totalUrl = headUrl + body.value();

                    MappingMethodCache.getInstance().put(totalUrl,new BeanDefination(target,method));
                 }
                }catch (Exception e){
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void parseClassName(String totalPath, String webPackage, List<String> classNames) {
        File path = new File(totalPath);
        if(path.exists()){
           File[] childs =  path.listFiles();
           if(childs!=null&&childs.length>0){
               for(File child:childs){
                  String fileName =  child.getName();
                  if(fileName.endsWith(".class")){
                      String temp = fileName.replace(".class","");
                      classNames.add(webPackage+"."+temp);
                  }
               }
           }
        }
    }

}
