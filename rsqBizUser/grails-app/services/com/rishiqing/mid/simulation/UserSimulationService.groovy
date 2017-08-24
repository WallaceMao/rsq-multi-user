package com.rishiqing.mid.simulation

import com.rishiqing.domain.user.CommonUser
import com.rishiqing.domain.user.User
import grails.transaction.Transactional

@Transactional
class UserSimulationService {

    def getScene() {
        return "000000"
//        return [
//                users: buildUser()
//        ]
    }

    def buildUser(){
        User user = new CommonUser('user2@qq.com', 'user2', "用户2")
        user.save()
        return [user]
    }

    def buildTeam(){}
}
