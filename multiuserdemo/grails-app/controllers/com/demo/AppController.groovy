package com.demo

import com.rishiqing.domain.user.User
import com.rishiqing.util.ConvertUtil
import grails.plugin.springsecurity.SpringSecurityUtils
import org.codehaus.groovy.grails.web.json.JSONObject

class AppController {
    def userBizService
    def authBizService
    def springSecurityService

    def index() {
        User currentUser = springSecurityService.getCurrentUser()

        //  如果没有切换过，那么直接跳转到superUser的mainUser
        if(!SpringSecurityUtils.isSwitched()){
            User mainUser = userBizService.getMainUser(currentUser)
            if(mainUser.id != currentUser.id){
                springSecurityService.reauthenticate(mainUser.username)
                currentUser = springSecurityService.getCurrentUser()
            }
        }
        render(view: "index", model: [user: currentUser])
    }

    def switchUser(){
        User user = springSecurityService.getCurrentUser()
        Map params = ConvertUtil.JSON2Map(request.JSON as JSONObject)
        String username = params.username

        //TODO 做权限验证！！！！
        if(authBizService.checkSwitchAuth(user, username)){
            println "=======current User: ${user.username}, params: ${params}"
            redirect(uri: "/j_spring_security_switch_user", params: [j_username: username])
        }else{
            render "error"
        }
    }
}
