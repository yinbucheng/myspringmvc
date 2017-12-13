package cn.ishow.manage.web;

import cn.ishow.manage.annotation.Controller;
import cn.ishow.manage.annotation.RequestMapping;
import cn.ishow.manage.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/system")
public class SystemController {

    @RequestMapping("/test")
    @ResponseBody
    public Object test(String name,Integer age,String sex){
        Map<String,Object> map = new HashMap<>();
        map.put("code",200);
        map.put("msg","hello world");
        map.put("name",name);
        map.put("age",age);
        map.put("sex",sex);
        return map;
    }


    @RequestMapping("/hello")
    @ResponseBody
    public Object hello(){
        Map<String,Object> map = new HashMap<>();
        map.put("code",200);
        map.put("msg","hello world");
        return map;
    }


}
