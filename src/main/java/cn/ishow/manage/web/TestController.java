package cn.ishow.manage.web;

import cn.ishow.manage.annotation.Controller;
import cn.ishow.manage.annotation.RequestMapping;
import cn.ishow.manage.annotation.ResponseBody;
import cn.ishow.manage.system.RequestContextHodler;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/test")
public class TestController {

    @RequestMapping("/testSession")
    @ResponseBody
    public Object testSession(){
      RequestContextHodler.ServletAttribute attribute = RequestContextHodler.getAttribute();
      attribute.getSession().setAttribute("id","test");
      Map<String,Object> map = new HashMap<>();
      map.put("code",200);
      map.put("msg","success");
      return map;
    }
}
