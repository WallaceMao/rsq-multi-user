package com.rishiqing.test.functional.util

import com.mysql.jdbc.Driver
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource
import com.rishiqing.test.functional.ConfigUtil
import groovy.sql.Sql

import javax.sql.DataSource
import java.sql.DriverManager

/**
 * Created by  on 2017/8/24.Wallace
 */
class SqlUtil {
    static def execute(String strSql){
        Sql sql
        try {
            Map conf = ConfigUtil.config.dataSource

            if("com.mysql.jdbc.Driver" == conf?.driverClassName){
                DataSource ds = new MysqlDataSource()
                ds.setURL((String)conf.url)
                ds.setUser((String)conf.username)
                ds.setPassword((String)conf.password)
                sql = new Sql(ds)
            }else{
                throw new RuntimeException("unsupported driver class, driver class name is ${conf?.driverClassName}")
            }
//            有bug，用Sql.newInstance的方式居然不行？
//            sql = Sql.newInstance(url: ds.url, user: ds.username, password: ds.password, driver: ds.driverClassName)
            sql.execute(strSql)
        }  finally {
            if(sql){
                sql.close()
            }
        }
    }

    public static void main(String[] args) {
//        println System.getProperty("user.home")
        System.setProperty("geb.env", "rsqbeta")
        println "------${ConfigUtil.config.dataSource}"
//        Map emailUser = ConfigUtil.config.users.emailUsers[0]
//        println "-----${emailUser}"
//        String sql = SqlPrepare.genCleanEmailUsers(emailUser)
//        execute(sql)
    }
}
