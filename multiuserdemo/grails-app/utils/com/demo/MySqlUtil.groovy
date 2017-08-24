package com.demo

import com.mysql.jdbc.Driver
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource
import groovy.sql.Sql

import javax.sql.DataSource

/**
 * Created by  on 2017/8/24.Wallace
 */
class MySqlUtil {
    static def mysqlDriver(){
        return "hello"
    }
        static def execute(String strSql){
            Sql sql
            try {
                DataSource ds = new MysqlDataSource()
                ds.setURL("jdbc:mysql://localhost:3306/multi_user_demo_dev?autoReconnect=true&characterEncoding=utf-8&useSSL=false")
                ds.setUser("root")
                ds.setPassword("root")
//                Map ds = [
//                        url: "jdbc:mysql://localhost:3306/multi_user_demo_dev?autoReconnect=true&characterEncoding=utf-8&useSSL=false",
//                        username: 'root',
//                        password: 'root',
//                        driverClassName: 'com.mysql.jdbc.Driver'
//                ]
//                sql = Sql.newInstance(url: ds.url, user: ds.username, password: ds.password, driver: ds.driverClassName)
                sql = new Sql(ds)
                sql.execute(strSql)
            }  finally {
                if(sql){
                    sql.close()
                }
            }
        }

        public static void main(String[] args) {
        }
}
