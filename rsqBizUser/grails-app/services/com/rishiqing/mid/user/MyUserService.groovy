package com.rishiqing.mid.user

import com.rishiqing.domain.user.CommonUser
import com.rishiqing.domain.user.User
import grails.transaction.Transactional

/**
 * Created by  on 2017/7/28.Wallace
 */
@Transactional
class MyUserService {
    def springSecurityService
    def testText(){
        User loginUser = springSecurityService.getCurrentUser()
        log.error("------user.username:${loginUser.username}")
        CommonUser u = CommonUser.findByUsername("user@qq.com")
        return "----this is a test text: user is ${u.realName}, loginUser is ${loginUser.username}"
    }

    def testTransaction(){
        CommonUser u = CommonUser.findByUsername("user@qq.com")

        u.realName = "transactionUser1----"
        if(!u.save()){
            println "save realName error:" + u.errors
            return "fail"
        }

        u.dian = 999999
        if(!u.save()){
            println "save dian error:" + u.errors
            return "fail"
        }
        return "success"
    }

}
