package com.rishiqing.mid.user

import com.rishiqing.domain.user.CommonUser
import grails.transaction.Transactional

@Transactional
class AuthBizService {
    def userBizService

    def checkSwitchAuth(CommonUser user, String username) {
        userBizService.getSiblingUsers(user).any { Map it ->
            it.username == username
        }
    }
}
