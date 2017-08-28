package com.rishiqing.model.dao

import com.rishiqing.domain.user.CommonUser
import com.rishiqing.domain.user.SuperUser
import grails.transaction.Transactional

@Transactional
class SuperUserDaoService {

    SuperUser findSuperUserById(long id) {
        SuperUser.findById(id)
    }

    SuperUser findSuperUserByUser(CommonUser user){
        user.superUser
    }

    void setMainUser(CommonUser user){
        SuperUser superUser = user.superUser
        superUser.mainUser = user
        superUser.save(failOnError: true)
    }
}
