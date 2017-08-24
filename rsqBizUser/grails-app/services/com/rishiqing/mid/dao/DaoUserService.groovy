package com.rishiqing.mid.dao

import com.rishiqing.domain.user.SuperUser
import com.rishiqing.domain.user.Team
import com.rishiqing.domain.user.User
import com.rishiqing.domain.user.UserJoinTeamHistory
import grails.transaction.Transactional

@Transactional
class DaoUserService {

    /**
     * 检查user的superUser是否存在，如果不能存在，则创建superUser
     * @param user
     * @return
     */
    def userCheckSuperUser(User user){
        SuperUser superUser = user.superUser
        if(null == superUser){
            superUser = new SuperUser(user, user);
            superUser.save(failOnError: true)

            user.superUser = superUser
            user.save(failOnError: true)
        }

    }

    /**
     * 将user的team设置为null，同时保存退出团度id历史记录
     * @param user
     * @param removeType："q"(quit)表示主动退出，"r"(remove)表示被管理员移除
     */
    def userRemoveTeam(User user, String type) {

        // 保存退出团队记录
        UserJoinTeamHistory history = new UserJoinTeamHistory(
                type: type,
                superUser: user.superUser,
                user: user,
                team: user.team
        )
        history.save(failOnError: true)

        user.team = null
        user.save(failOnError: true)
    }

    /**
     * 新增team
     * @param user
     * @param type
     */
    def userAddTeam(User user, Team team, String type){

        // 保存加入团队记录
        UserJoinTeamHistory history = new UserJoinTeamHistory(
                type: type,
                superUser: user.superUser,
                user: user,
                team: team
        )
        history.save(failOnError: true)

        user.team = team
        user.save(failOnError: true)
    }
}
