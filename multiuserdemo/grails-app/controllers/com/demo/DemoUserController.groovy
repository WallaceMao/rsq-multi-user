package com.demo

import groovy.sql.Sql

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet

class DemoUserController {
    def dataSource

    Sql sql
    Connection conn

    def initController(){
        sql = new Sql(dataSource)
        conn = sql.getDataSource().getConnection()
    }

    def initData() {

        println "----start-----${new Date()}"
        StringBuilder sb = new StringBuilder()
        sb.append("insert into demo_user (version, name, date_created) values ")
        long num = 1000000
        long start = 1
        long batch = 10
        for(;start <= num; start++) {
            sb.append("(0, '")
            .append("用户-${batch}-${start}")
            sb.append("',now())")
            if(start != num){
                sb.append(",")
            }
        }

        Sql sql = new Sql(dataSource)
        List list = sql.executeInsert(sb.toString())
        sql.close()
        println "----end---${new Date()}"
//        long num = 1000
//        long start = 1
//        for(;start < num; start++){
//            DemoUser u = new DemoUser("用户" + start, new Date())
//            u.save()
//        }
        render "demo user"
    }

    def autoIncrement(){
        Sql sql = new Sql(dataSource)
        String strLockTable = "lock table `demo_user` write;"
        String strUnlockTable = "unlock tables;"
        println "----start----${new Date()}"
        sql.execute(strLockTable)
        println "----after lock table----${new Date()}"
        sql.execute(strUnlockTable)
        println "----after unlock table----${new Date()}"

        render "success"
    }

    def testDataSourceAutoCommit(){
        Sql sql = new Sql(dataSource)
        Connection conn = null;

        try {
            conn = sql.getDataSource().getConnection()

            conn.setAutoCommit(false)

            StringBuilder sb = new StringBuilder()
            sb.append("lock table `demo_user` write;")
//            sb.append("insert into demo_user (version, name, date_created) values ")
//            .append("(0, 'mytest1122', now())")
            PreparedStatement p = conn.prepareStatement(sb.toString())
            p.execute()

            if(true){
                throw new RuntimeException("-----------test------------exception-----------")
            }

            sb = new StringBuilder()
            sb.append("insert into demo_user (version, name, date_created) values ")
                    .append("(1, 'mytest1122', now())")
            p = conn.prepareStatement(sb.toString())
            p.execute()

            conn.commit()

        } catch (Exception e) {
            conn.rollback()
            e.printStackTrace()
        } finally {
            if(null != conn){
                conn.close()
            }
        }
        render "success testDataSourceAutoCommit ${new Date()}"
    }

    def testDataSource(){
        initController()

        try {
            StringBuilder sb = new StringBuilder()
            sb.append("insert into demo_user (version, name, date_created) values ")
                    .append("(0, 'mytest0001122', now())")
            PreparedStatement p = conn.prepareStatement(sb.toString())
            p.execute()

            render "success testDataSource ${new Date()}"

        } catch (Exception e) {
            e.printStackTrace()
        } finally {
            if(null != conn){
                conn.close()
            }
        }
    }



    def testLock(){
        initController()

        PreparedStatement pstmt = null
        ResultSet rs = null

        try {
            Date handleStart = new Date();

            Logger.log("处理 Todo id 自增长开始", handleStart)
//            println "----处理 Todo id 自增长开始" + "     ${new Date().format("yyyy-MM-dd HH:mm:ss")}";

            // 设置自动提交为false，在添加完所有要插入的数据之后，批量进行插入。
            conn.setAutoCommit(false);
            // 执行写锁定
            String lock = "lock table `demo_user` write;";
            // 预编译
            pstmt = conn.prepareStatement(lock);
            // 执行
            pstmt.execute();
            Logger.log("demo_user表已被锁住", handleStart)
            // 获取要生成的日程列表的长度
            String query1 = "select max(id) from `demo_user`;";
            // 预编译
            pstmt = conn.prepareStatement(query1);
            // 执行查询，获取结果集
            rs = pstmt.executeQuery();
            // 获取结果集
            Long oldAutoIncrement = null;
            // 取值
            while(rs.next()){
                oldAutoIncrement = rs.getLong(1) + 1;
            }
            Logger.log("获取到自增长id为：${oldAutoIncrement}", handleStart)
            // 获取要插入的日程的数量
            Long size = 7
            if(oldAutoIncrement){
                // 获取新的自增长值 = 老的自增长 + 插入日程的长度;
                Long newAutoIncrement = oldAutoIncrement + size;
                // 更新表
                String query2 = "alter table `demo_user` AUTO_INCREMENT = ?";
                // 预编译
                pstmt = conn.prepareStatement(query2);
                // 设置参数，改为新的自增长值
                pstmt.setLong(1,newAutoIncrement);
                // 运行 sql
                pstmt.execute();
                Logger.log("重设demo_user表的数据库自增长id：${newAutoIncrement}", handleStart)
            } else {
                Logger.log("自增长设置失败", handleStart)
                throw new Exception("自增长设置失败!");
            }

            // 执行解锁
            String unlock = "unlock table;";
            //　预编译
            pstmt = conn.prepareStatement(unlock);
            //　执行
            pstmt.execute();
            Logger.log("demo_user表解锁成功", handleStart)

            // 进行提交
            conn.commit();
            Logger.log("commit成功", handleStart)

            // 重新设置提交
            conn.setAutoCommit(true);
            Logger.log("重设setAutoCommit为true成功", handleStart)

            Logger.log("处理Todo id 自增长结束", handleStart)

//            Date handleEnd = new Date();
//            println "处理Todo id 自增长结束，耗时 : " + (handleEnd.getTime() - handleStart.getTime()) + "ms" + "     ${new Date().format("yyyy-MM-dd HH:mm:ss")}";
            // 返回老的自增长的值
            render "success test lock oldAutoIncrement is ${oldAutoIncrement}, size is ${size}";
        } catch (Exception e) {
            e.printStackTrace()
            render "failed" + e
        } finally {
            if(null != conn){
                conn.close()
            }
            if(null != rs){
                rs.close()
            }
            if(null != pstmt){
                pstmt.close()
            }
        }
    }
}
