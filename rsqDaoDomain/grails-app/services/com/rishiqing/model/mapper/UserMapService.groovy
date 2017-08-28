package com.rishiqing.model.mapper

import com.rishiqing.domain.user.CommonUser

class UserMapService {
    def teamMapperService
    //  默认关闭事务，在需要使用数据库事务的时候开启
    static transactional = false

    Map toSimpleMap(CommonUser user) {
        Map result = null
        if(user){
            result = [
                    id: user.id,
                    username: user.username,
                    realName: user.realName,
                    phoneNumber: user.phoneNumber
            ]
        }
        result
    }

    /**
     * 转换为带有team对象的map
     * @param user
     * @return
     */
     Map toMapWithTeam(CommonUser user) {
         Map result = null
         if(user){
             result = [
                     id: user.id,
                     username: user.username,
                     realName: user.realName,
                     phoneNumber: user.phoneNumber,
                     team: teamMapperService.toSimpleMap(user.team)
             ]
         }
         result
    }
}
