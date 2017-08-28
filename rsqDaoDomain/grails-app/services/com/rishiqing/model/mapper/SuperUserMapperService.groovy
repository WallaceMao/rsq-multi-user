package com.rishiqing.model.mapper

import com.rishiqing.domain.user.CommonUser
import com.rishiqing.domain.user.SuperUser

class SuperUserMapperService {
    def userMapService

    Map toMap(SuperUser superUser) {
        Map result = null

        if(superUser){
            result = [
                    id: superUser.id,
                    mainUser: userMapService.toSimpleMap((CommonUser)superUser.mainUser),
                    defaultLoginUser: userMapService.toSimpleMap((CommonUser)superUser.defaultLoginUser)
            ]
        }
        result
    }
}
