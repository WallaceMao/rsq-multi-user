package com.demo

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource
import com.rishiqing.domain.user.CommonUser
import com.rishiqing.domain.user.Team
import com.rishiqing.domain.user.User
import groovy.sql.Sql
import org.h2.jdbcx.JdbcDataSource

import javax.sql.DataSource
import java.sql.Connection
import java.sql.DriverManager

class OpenVisitController {
    def springSecurityService
    def myUserService
    def openVisitService
    def midwareUserService
    def dataSource = 'rsqbeta'

    def index() {
//        String username1 = "user1",
//               password1 = "user1password",
//               realName1 = "用户1",
//               username2 = "user2",
//               password2 = "user2password",
//               realName2 = "用户2";
//
//        User u = new User(username1, password1, realName1)
//        if(!u.save(flush: true)){
//            println u.errors
//        }

        CommonUser u = CommonUser.findByUsername("user")
        u.realName = new Date().getTime()
        if(!u.save(flush: true)){
            println u.errors
        }

        u.realName = "233333"
        if(!u.save()){
            println u.errors
        }

        render "hhhhh"
    }

    def delaySave(){
        println "-------aaaaa-------${new Date()}"

        CommonUser u = CommonUser.findByUsername("user")
        u.realName = "delayUser-${new Date()}"

        Thread.sleep(5000)

//        u.save(flush: true)
        println "~~~~~~org user.version:${u.version}"

        openVisitService.saveUser(u, "delay")

        render "delay save successfully"
    }

    def save(){
        println "-------aaaaa-------${new Date()}"

        CommonUser u = CommonUser.findByUsername("user")
//        u.setRealName("user-${new Date()}")

//        println ">>>>>org user.version:${u.version}"
//        if(!u.save(flush: true)){
//            println u.errors
//        }

//        println ">>>>>user.version:${u.version}"

        openVisitService.saveUser("inst")

//        println ">>>>>final user.version:${u.version}"

        render "save successfully"
    }

    def saveDemoUser(){
//        DemoUser du = DemoUser.findById(1L)
//        du.setName("demoUser-${new Date()}")
//
//        if(!du.save(flush: true)){
//            println du.errors
//        }
//
//        render "save demo user successfully"
    }

    def async(){
        println "-------aaaaa-------${new Date()}"

        openVisitService.useAsync()
        println "-------bbbbb-------${new Date()}"

        render "hhhhhh"
    }

    def aaa(){
        render myUserService.testText()
    }

    def bbb(){
        CommonUser user = springSecurityService.currentUser
        Team t = new Team("hhhhhhh")
        t.createdBy = String.valueOf(user.id)
        t.save()
        render myUserService.testTransction()
    }

    def testVisit(){
//        Sql sql = new Sql(dataSource)
        MySqlUtil.execute("select * from user")
//        Map ds = [
//                url:"jdbc:h2:mem:devDb;MVCC=TRUE;LOCK_TIMEOUT=10000;DB_CLOSE_ON_EXIT=FALSE",
//                username: 'sa',
//                password: '',
//                driverClassName: 'org.h2.Driver'
//        ]
//        Sql sql = Sql.newInstance(ds.url, ds.username, ds.password, ds.driverClassName)

//        DataSource ds = new MysqlDataSource()
//        ds.setURL("jdbc:mysql://localhost:3306/temp_prod?autoReconnect=true&characterEncoding=utf-8&useSSL=false")
//        ds.setUser("root")
//        ds.setPassword("root")
//
//        def sql = new Sql(ds)
//
//        sql.execute("select * from user")
        render "hello-${new Date()}"
    }
}
