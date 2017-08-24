package com.rishiqing.test.functional

/**
 * Created by  on 2017/8/16.Wallace
 * 读取配置文件的工具类
 */
class ConfigUtil {
    static ConfigObject config
    static {
        init()
    }
    private static void init(){
        String path = "${new File('.').getAbsolutePath()}${File.separator}test${File.separator}functional${File.separator}FunctionalTestConfig.groovy"
        String env = System.getProperty("geb.env") ?: "local"
        config = new ConfigSlurper(env).parse(new File(path).toURI().toURL())
    }
}
