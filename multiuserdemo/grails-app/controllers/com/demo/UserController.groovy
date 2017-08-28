package com.demo

import com.rishiqing.domain.user.CommonUser
import com.rishiqing.domain.user.User
import com.rishiqing.util.ConvertUtil
import grails.converters.JSON
import org.codehaus.groovy.grails.web.json.JSONObject

class UserController {
    def userBizService
    def springSecurityService

    def fetchMe(){
        def m
        try {
            User u = springSecurityService.currentUser
            m = [errcode: 0, result: userBizService.toMapWithTeam(u)]
        }catch (Exception e){
            log.error("----fetchMe error----", e)
            m = [errcode: 1]
        }
        render m as JSON
    }

    /**
     * 获取SuperUser，包含team
     * @return
     */
    def fetchSiblings() {
        def m
        try {
            List list = userBizService.getSiblingUsers(springSecurityService.currentUser)
            m = [errcode: 0, result: list]
        }catch (Exception e){
            log.error("----fetchSiblings error----", e)
            m = [errcode: 1]
        }
        render m as JSON
    }

    def createTeam() {
        def m
        try {
            CommonUser user = springSecurityService.currentUser
            Map params = ConvertUtil.JSON2Map(request.JSON as JSONObject)
            Map userMap = userBizService.createTeam(user, params)
            m = [errcode: 0, result: userMap]
        }catch (Exception e){
            e.printStackTrace()
            log.error("----createTeam error----", e)
            m = [errcode: 1]
        }
        render m as JSON
    }

    def quitTeam() {
        def m
        try {
            CommonUser user = springSecurityService.currentUser
            Map params = ConvertUtil.JSON2Map(request.JSON as JSONObject)

            println "user::::$user.id, $user.username"
            Map userMap = userBizService.quitTeam(user, params)

            //  跳转到mainUser中重新做授权
            Map mainUser = userBizService.getMainUser(user)
            springSecurityService.reauthenticate((String)mainUser.username)

            m = [errcode: 0, result: userMap]
        }catch (Exception e){
            log.error("----quitTeam error----", e)
            m = [errcode: 1]
        }
        render m as JSON
    }
}
