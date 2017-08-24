package com.rishiqing.mid.user

import com.demo.DemoUser
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
class MidUserService {

    def grailsApplication
    def daoUserService

    DemoUser createDemoUser(){
        DemoUser userWithoutSuperUser = new DemoUser("hhhhh", 12)
        userWithoutSuperUser.save(flush: true)
        return userWithoutSuperUser
    }

    /**
     * 获取兄弟用户
     * @param user
     * @return
     */
    List<CommonUser> getSiblingUsers(CommonUser user) {
        if(!user){
            return null;
        }
        List<CommonUser> list = CommonUser.findAllBySuperUser(user.superUser)

        if(list.size() == 0){
            list << user
        }

        return list
    }

    /**
     * 获取superUser用户
     * @param user
     * @return
     */
    SuperUser getParentSuperUser(User user){
        if(!user){
            return null
        }
        return user.superUser
    }

    /**
     * 获取隶属于同一个SuperUser的子用户
     * @param user
     * @return
     */
    List<User> getChildrenUsers(SuperUser superUser){
        if(!superUser){
            return null
        }
        return User.findAllBySuperUser(superUser)
    }

    /**
     * 创建公司
     * @param currentUser：当前用户
     * @param team
     * @return creator
     */
    User createTeam(User currentUser, Team team){
        if(null == currentUser || null == team){
            throw new MidUserMissingParamsException("--rsq--:params is missing, currentUser:${currentUser}, team:${team}")
        }

        //  首先检查currentUser的superUser是否存在，不存在会保存
        daoUserService.userCheckSuperUser(currentUser)

        //  保存creator
        User creator = UserUtil.generateRandomUser()
        creator.superUser = currentUser.superUser
        creator.save(failOnError: true)

        //  保存team
        team.createdBy = creator
        team.save(failOnError: true)

        daoUserService.userAddTeam(creator, team, UserJoinTeamHistory.TYPE_CREATE_TEAM)

        return creator
    }
    /**
     * 退出团队。
     * currentUser退出team：
     * @param currentUser：当前用户，可以是与team相关的user具有同一个superUser的任意一个user
     * @param team
     * @return teamCommonUser原来与team相关的teamCommonUser
     */
    User quitTeam(User currentUser, Team team){
        if(null == currentUser || null == team){
            throw new MidUserMissingParamsException("--rsq--:params is missing, currentUser:${currentUser}, team:${team}")
        }

        //  首先检查currentUser的superUser是否存在，不存在会保存
        daoUserService.userCheckSuperUser(currentUser)

        //  获取到superUser
        SuperUser superUser = currentUser.superUser
        User teamCommonUser = User.findBySuperUserAndTeam(superUser, team)
        if(null == teamCommonUser){
            throw new MidUserAuthException("--rsq--:user with superUser[${superUser}] and team[${team}] not found")
        }
        //TODO  如果是创建者退出团队，怎么办？

        //  设置user的team为null
        daoUserService.userRemoveTeam(teamCommonUser, UserJoinTeamHistory.TYPE_QUIT_TEAM);

        return teamCommonUser
    }

    /**
     * 解散团队，设置团队的所有成员的team为null
     * @param currentUser：当前用户，可以是与team相关的user具有同一个superUser的任意一个user
     * @param team
     */
    def dismissTeam(User currentUser, Team team){
        if(null == currentUser || null == team){
            throw new MidUserMissingParamsException("--rsq--:params is missing, currentUser:${currentUser}, team:${team}")
        }

        //  首先检查currentUser的superUser是否存在，不存在会保存
        daoUserService.userCheckSuperUser(currentUser)

        //  获取到superUser
        SuperUser superUser = currentUser.superUser
        User teamCommonUser = User.findByIdAndSuperUser(Long.valueOf(team.createdBy), superUser)
        if(null == teamCommonUser){
            throw new MidUserAuthException("--rsq--:user with id[${team.createdBy}] is not the creator of team[${team}]")
        }

        //TODO 设置team的所有用户的team为null
        int num = grailsApplication.config.rsq.plugin.mid.user.dismissMaxNumber ?: 100
        if(User.countByTeam(team) > num){
            throw new MidUserLimitException("--rsq--:reach limit number for team[${team}] when DISMISS team, limit number is ${num}")
        }

        //  暂时使用迭代的方式
        List<CommonUser> userList = CommonUser.findAllByTeam(team)
        Iterator<CommonUser> it = userList.iterator()
        while (it.hasNext()){
            CommonUser user = it.next()
            daoUserService.userRemoveTeam(user, UserJoinTeamHistory.TYPE_DISMISS_TEAM)
        }

    }

    /**
     * 设置主团队
     * @param currentUser：当前用户，可以是与team相关的user具有同一个superUser的任意一个user
     * @param team
     */
    def setMain(User currentUser, Team team){
        if(null == currentUser || null == team){
            throw new MidUserMissingParamsException("--rsq--:params is missing, currentUser:${currentUser}, team:${team}")
        }

        //  首先检查currentUser的superUser是否存在，不存在会保存
        daoUserService.userCheckSuperUser(currentUser)

        SuperUser superUser = currentUser.superUser
        User teamCommonUser = User.findBySuperUserAndTeam(superUser, team)
        if(null == teamCommonUser){
            throw new MidUserAuthException("--rsq--:user with superUser[${superUser}] and team[${team}] not found")
        }

        superUser.mainUser = teamCommonUser
        superUser.save(failOnError: true)
    }

}
