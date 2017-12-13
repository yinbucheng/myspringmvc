package cn.ishow.manage.web;

import cn.ishow.manage.annotation.Controller;
import cn.ishow.manage.annotation.RequestMapping;
import cn.ishow.manage.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

    @RequestMapping("/login")
    @ResponseBody
    public Object login(String loginName,String password){
        Map<String,Object> map = new HashMap<>();
        if(loginName.equals("yinchong")&&password.equals("123")) {
            map.put("code",200);
            map.put("msg","登录成功");
            return map;
        }
        map.put("code",500);
        map.put("msg","登录失败");
        return map;
    }

    @RequestMapping("/register")
    @ResponseBody
    public Object register(String loginName,String password){
        Map<String,Object> map = new HashMap<>();
        map.put("code",200);
        map.put("loginName",loginName);
        map.put("password",password);
        return map;
    }
}
