package com.rishiqing.mid.user

import com.rishiqing.domain.user.CommonUser
import com.rishiqing.domain.user.SuperUser
import com.rishiqing.domain.user.Team
import com.rishiqing.domain.user.User
import com.rishiqing.domain.user.UserJoinTeamHistory
import com.rishiqing.mid.exception.MidUserAuthException
import com.rishiqing.mid.exception.MidUserMissingParamsException
import com.rishiqing.mid.util.UserUtil
import grails.transaction.Transactional

/**
 * 邀请的过程分为两步：
 （1）邀请。根据邮箱或者手机号邀请用户，如果邮箱或者手机号存在，就获取以前的用户，如果不存在，就创建新帐号
 （2）加入团队。 查找user的兄弟user是否加如果，如果加入过，则重用以前的user，如果没有加入过，则新建team相关的user
 */
@Transactional
class MidTeamService {
    def daoUserService

    /**
     * 在用户已经保存到数据库的情况下，在team中增加user
     * 检查user的兄弟用户（包括自己）中是否有曾经加入过team的，如果有，则直接重用以前的user，如果没有则新建user
     * @param team
     * @param user
     * @return
     */
    def addMember(Team team, User user) {
        if(null == user || null == team){
            throw new MidUserMissingParamsException("--rsq--:params is missing, team:${team}, user:${user}")
        }

        //  首先检查currentUser的superUser是否存在，不存在会保存
        daoUserService.userCheckSuperUser(user)

        //  查找user的sibling user中是否有已经加入过team的user，如果有就重用
        CommonUser teamCommonUser
        UserJoinTeamHistory history = UserJoinTeamHistory.findBySuperUserAndTeam(user.superUser, team)
        if(history){
            teamCommonUser = history.user
        }else{
            //  TODO  获取到targetUser，即用来关联team的user
            teamCommonUser = UserUtil.generateRandomUser()
            teamCommonUser.superUser = user.superUser
            teamCommonUser.save(failOnError: true)
        }

        daoUserService.userAddTeam(teamCommonUser, team, UserJoinTeamHistory.TYPE_JOIN_TEAM)
    }

    /**
     * 从team中移除与currentUser.superUser下的user
     * @param team
     * @param user：准备从team中移除的user
     * @return
     */
    User removeMember(Team team, User user) {
        if(null == user || null == team){
            throw new MidUserMissingParamsException("--rsq--:params is missing, team:${team}, user:${user}")
        }
        //  首先检查currentUser的superUser是否存在，不存在会保存
        daoUserService.userCheckSuperUser(user)

        //  获取到superUser
        SuperUser superUser = user.superUser
        List<CommonUser> teamCommonUserList = CommonUser.findAllBySuperUserAndTeam(superUser, team)
        if(teamCommonUserList.size() == 0){
            throw new MidUserAuthException("--rsq--:user with superUser[${superUser}] and team[${team}] not found")
        }
        //TODO  如果是创建者退出团队，怎么办？

        //  设置user的team为null
        Iterator<CommonUser> it = teamCommonUserList.iterator()
        while(it.hasNext()){
            CommonUser teamCommonUser = it.next()
            daoUserService.userRemoveTeam(teamCommonUser, UserJoinTeamHistory.TYPE_REMOVE_TEAM);
        }

        return teamCommonUserList.get(0)
    }
}
