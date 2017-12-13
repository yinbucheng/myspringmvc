package cn.ishow.manage.test;

import cn.ishow.manage.utils.ReadUtils;

import java.io.File;

public class Test {

    public static void main(String[] args) {
        test1();
//       String value = (String) ReadUtils.read("web_scan");
//        System.out.println(value);
    }

    private static void test1() {
        String path = System.getProperty("java.class.path");
        System.out.println("path="+path);
        Package temp =  Test.class.getPackage();
        System.out.println(temp.getName());

        String path2 =  System.getProperty("user.dir");
        System.out.println(path2);

        path =  System.getProperty("java.class.path");
        System.out.println(path);

        // 第一种：获取类加载的根路径   D:\git\daotie\daotie\target\classes
        File f = new File(Test.class.getResource("/").getPath());
        System.out.println(f);

        // 获取当前类的所在工程路径; 如果不加“/”  获取当前类的加载目录  D:\git\daotie\daotie\target\classes\my
        File f2 = new File(Test.class.getResource("").getPath());
        System.out.println(f2);
    }
}
