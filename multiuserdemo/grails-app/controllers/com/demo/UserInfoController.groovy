package com.demo

import com.rishiqing.domain.user.User


class UserInfoController {

    def springSecurityService

    def index() {
        User user = springSecurityService.getCurrentUser()

        log.info("------user.username:${user.username}")
        render(view: "index", model: [tag: 'index', user: user])
    }

    def listTeam(){
        User user = springSecurityService.getCurrentUser()
        render(view: "listTeam", model: [tag: "listTeam",user: user])
    }

    def createTeam(){
        User user = springSecurityService.getCurrentUser()
        render(view: "createTeam", model: [tag: "createTeam",user: user])
    }

    def quitTeam(){
        User user = springSecurityService.getCurrentUser()
        render(view: "quitTeam", model: [tag: "quitTeam",user: user])
    }

    def dismissTeam(){
        User user = springSecurityService.getCurrentUser()
        render(view: "dismissTeam", model: [tag: "dismissTeam",user: user])
    }

    def setMainTeam(){
        User user = springSecurityService.getCurrentUser()
        render(view: "setMainTeam", model: [tag: "setMainTeam",user: user])
    }

    def invite(){
        User user = springSecurityService.getCurrentUser()
        render(view: "invite", model: [tag: "invite",user: user])
    }

    def switchUser(){
        User user = springSecurityService.getCurrentUser()
        println "=======current User: ${user.username}, params: ${params}"
        redirect(uri: "/j_spring_security_switch_user", params: [j_username: params["j_username"]])
    }

    def testBlock(){
        println "--------====dddddooooo="
        render "success testBlock ${new Date()}"
    }
}
