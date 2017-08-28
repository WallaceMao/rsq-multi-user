package com.rishiqing.mid.common

import com.rishiqing.domain.user.Team
import com.rishiqing.domain.user.User
import com.rishiqing.domain.user.UserJoinTeamHistory
import grails.transaction.Transactional

@Transactional
class UserCommonService {
    def teamDaoService
    def userDaoService

    /**
     * 给成员添加团队
     * @param user
     * @param team
     * @param type
     */
    void userAddTeam(User user, Team team, String type){

        // 保存加入团队记录
        UserJoinTeamHistory history = new UserJoinTeamHistory(
                type: type,
                superUser: user.superUser,
                user: user,
                team: team
        )
        teamDaoService.saveHistory(history)

        user.team = team
        userDaoService.saveUser(user)
    }

    /**
     * 团队中移除成员
     * @param user
     * @param type
     * @return
     */
    void userRemoveTeam(User user, String type) {

        // 保存退出团队记录
        UserJoinTeamHistory history = new UserJoinTeamHistory(
                type: type,
                superUser: user.superUser,
                user: user,
                team: user.team
        )
        teamDaoService.saveHistory(history)

        user.team = null
        userDaoService.saveUser(user)
    }
}
