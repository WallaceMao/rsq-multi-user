package com.rishiqing.mid.user

import com.rishiqing.domain.user.CommonUser
import com.rishiqing.domain.user.DemoUser
import com.rishiqing.domain.user.SuperUser
import com.rishiqing.domain.user.Team
import com.rishiqing.domain.user.User
import com.rishiqing.domain.user.UserJoinTeamHistory
import com.rishiqing.mid.exception.MidUserAuthException
import com.rishiqing.mid.exception.MidUserLimitException
import com.rishiqing.mid.exception.MidUserMissingParamsException
import com.rishiqing.mid.util.UserUtil
import grails.transaction.Transactional

@Transactional
class UserBizService {
    def userDaoService
    def userMapService
    def teamDaoService
    def teamMapService
    def superUserDaoService
    def superUserMapService

    def userCommonService

    def grailsApplication

    Map toMapWithTeam(User user){
        userMapService.toMapWithTeam((CommonUser)user)
    }

    /**
     * 获取兄弟用户
     * @param user
     * @return
     */
    List<Map> getSiblingUsers(CommonUser user) {
        if(!user){
            return null;
        }
        SuperUser superUser = superUserDaoService.findSuperUserByUser(user)

        //  如果superUser不存在，则返回当前用户
        List list = []
        if(superUser){
            list = userDaoService.findAllUsersBySuperUser(superUser)
        }else{
            list << user
        }

        //  CommonUser转换为Map
        list.collect { CommonUser it ->
            userMapService.toMapWithTeam(it)
        }
    }

    /**
     * 获取主用户
     * @param user
     * @return
     */
    Map getMainUser(CommonUser user){
        if(!user){
            return null
        }
        SuperUser su = superUserDaoService.findSuperUserByUser(user)
        userMapService.toSimpleMap(su.mainUser)
    }

    /**
     * 获取superUser用户
     * @param user
     * @return
     */
    Map getParentSuperUser(CommonUser user){
        if(!user){
            return null
        }
        SuperUser su = superUserDaoService.findSuperUserByUser(user)
        superUserMapService.toMap(su)
    }

    /**
     * 创建公司
     * @param currentUser：当前用户
     * @param team
     * @return creator
     */
    Map createTeam(CommonUser currentUser, Map teamParams){
        if(null == currentUser || null == teamParams){
            throw new MidUserMissingParamsException("--rsq--:params is missing, currentUser:${currentUser}, team:${teamParams}")
        }

        //  首先检查currentUser的superUser是否存在，不存在会保存
        userDaoService.checkSuperUser(currentUser)

        //  保存creator
        User creator = UserUtil.generateRandomUser()
        creator.superUser = currentUser.superUser
        userDaoService.saveUser(creator)

        //  保存team
        Team team = new Team(teamParams)
        team.createdBy = creator.id
        teamDaoService.saveTeam(team)

        //  设置用户的team为新创建的team
        userCommonService.userAddTeam(creator, team, UserJoinTeamHistory.TYPE_CREATE_TEAM)

        userMapService.toMapWithTeam((CommonUser)creator)
    }

    /**
     * 退出团队。
     * currentUser退出team：
     * @param currentUser：当前用户，可以是与team相关的user具有同一个superUser的任意一个user
     * @param team
     * @return teamCommonUser原来与team相关的teamCommonUser
     */
    Map quitTeam(User currentUser, Map teamParams){
        if(null == currentUser || null == teamParams){
            throw new MidUserMissingParamsException("--rsq--:params is missing, currentUser:${currentUser}, team:${teamParams}")
        }

        //  首先检查currentUser的superUser是否存在，不存在会保存
        userDaoService.checkSuperUser(currentUser)

        Team team = teamDaoService.findTeamById((long)teamParams.id)
        CommonUser teamCommonUser = userDaoService.findSiblingUserByTeam(currentUser, team)
        if(null == teamCommonUser){
            throw new MidUserAuthException("--rsq--:user [${currentUser}] and team[${team}] not found")
        }
        //TODO  如果是创建者退出团队，怎么办？

        //  设置user的team为null
        userCommonService.userRemoveTeam(teamCommonUser, UserJoinTeamHistory.TYPE_QUIT_TEAM);

        userMapService.toMapWithTeam((CommonUser)teamCommonUser)

    }

    /**
     * 受邀请加入团队
     * @param currentUser
     * @param teamParams
     * @return
     */
    Map joinTeam(User currentUser, Map teamParams){
        if(null == currentUser || null == teamParams){
            throw new MidUserMissingParamsException("--rsq--:params is missing, user:${currentUser}, team:${teamParams}")
        }

        Team team = teamDaoService.findTeamById((long)teamParams.id)
        //  首先检查currentUser的superUser是否存在，不存在会保存
        userDaoService.checkSuperUser(currentUser)

        //  查找user的sibling user中是否有已经加入过team的user，如果有就重用
        CommonUser teamCommonUser
        UserJoinTeamHistory history = teamDaoService.findJoinHistory(currentUser, team)
        if(history){
            teamCommonUser = history.user
        }else{
            //  TODO  获取到targetUser，即用来关联team的user
            teamCommonUser = UserUtil.generateRandomUser()
            teamCommonUser.superUser = superUserDaoService.findSuperUserByUser(currentUser)
            userDaoService.saveUser(teamCommonUser)
        }

        userCommonService.userAddTeam(teamCommonUser, team, UserJoinTeamHistory.TYPE_JOIN_TEAM)
    }

    /**
     * 解散团队，设置团队的所有成员的team为null
     * @param currentUser：当前用户，可以是与team相关的user具有同一个superUser的任意一个user
     * @param team
     */
    void dismissTeam(User currentUser, Map teamParams){
        if(null == currentUser || null == teamParams){
            throw new MidUserMissingParamsException("--rsq--:params is missing, currentUser:${currentUser}, team:${teamParams}")
        }

        //  首先检查currentUser的superUser是否存在，不存在会保存
        userDaoService.checkSuperUser(currentUser)

        Team team = teamDaoService.findTeamById((long)teamParams.id)
        CommonUser teamCommonUser = userDaoService.findSiblingUserByTeam(currentUser, team)

        if(null == teamCommonUser){
            throw new MidUserAuthException("--rsq--:user [${currentUser}] is not the creator of team[${team}]")
        }

        //TODO 设置team的所有用户的team为null
        int num = grailsApplication.config.rsq.plugin.mid.user.dismissMaxNumber ?: 100
        if(User.countByTeam(team) > num){
            throw new MidUserLimitException("--rsq--:reach limit number for team[${team}] when DISMISS team, limit number is ${num}")
        }

        //  暂时使用迭代的方式
        List<CommonUser> userList = userDaoService.findAllUserByTeam(team)
        userList.each { CommonUser user ->
            userCommonService.userRemoveTeam(user, UserJoinTeamHistory.TYPE_DISMISS_TEAM)
        }
    }

    /**
     * 设置主团队
     * @param currentUser：当前用户，可以是与team相关的user具有同一个superUser的任意一个user
     * @param team
     */
    void setMain(User currentUser, Map teamParams){
        if(null == currentUser || null == teamParams){
            throw new MidUserMissingParamsException("--rsq--:params is missing, currentUser:${currentUser}, team:${teamParams}")
        }

        //  首先检查currentUser的superUser是否存在，不存在会保存
        userDaoService.checkSuperUser(currentUser)

        Team team = teamDaoService.findTeamById((long)teamParams.id)
        CommonUser teamCommonUser = userDaoService.findSiblingUserByTeam(currentUser, team)
        if(null == teamCommonUser){
            throw new MidUserAuthException("--rsq--:user [${teamCommonUser}] and team[${team}] not found")
        }

        superUserDaoService.setMainUser(teamCommonUser)
    }

    def testWhere(){

    }

}
