package com.rishiqing.model.mapper

import com.rishiqing.domain.user.Team
import grails.transaction.Transactional

@Transactional
class TeamMapperService {

    /**
     * 转换为基本的team，只转换基本对象
     * @param team
     * @return
     */
    Map toSimpleMap(Team team) {
        Map result = null

        if(team){
            result = [
                    id: team.id,
                    name: team.name,
                    phoneNumber: team.phoneNumber
            ]
        }
        result
    }
}
