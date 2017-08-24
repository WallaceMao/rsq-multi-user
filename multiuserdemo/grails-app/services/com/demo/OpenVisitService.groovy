package com.demo

import com.rishiqing.domain.user.CommonUser
import com.rishiqing.domain.user.User
import grails.async.Promise

class OpenVisitService {

    def midwareUserService

    def saveUser(String from) {
        CommonUser u = CommonUser.findByUsername("user")

        String str = u.getUsername()
        Promise p = CommonUser.async.task {
            // Long running task
            try {
                println "-------000000-------${new Date()}"
                CommonUser u1 = CommonUser.findByUsername(str)
//                u1.refresh()
                println "----save User from async, u.version is ${u1.version}"
                u1.realName = "userInService-saveUser-${new Date()}"

                println "-------333311111-------${new Date()}"

                u1.save(flush: true)

                println "-------11111-------${new Date()}"
            } catch (Exception e) {
                e.printStackTrace()
            }
        }

    }

    def useAsync(){
        String str = "user"
        Promise p = User.async.task {
            // Long running task
            println "-------000000-------${new Date()}"
            CommonUser u1 = CommonUser.findByUsername(str)
            u1.refresh()
            println "----save User from async, u.version is ${u1.version}"
            u1.realName = "userInService-async-${new Date()}"

            Thread.sleep(2000)

            if(!u1.save(flush: true)){
                throw new RuntimeException(u1.errors.toString())
            }
            println "-------11111-------${new Date()}"
        }
// block until result is called
        println "-------22222-------${new Date()}"
//        def result = p.get()

//        println "result-----${result}---${new Date()}"
    }

    def testVisit(){
        render midwareUserService.getTestText()
    }
}
