package com.rishiqing.model.dao

import com.rishiqing.domain.user.CommonUser
import com.rishiqing.domain.user.SuperUser
import com.rishiqing.domain.user.Team
import com.rishiqing.domain.user.User

class UserDaoService {

    User findWhere(){

    }

    List<User> findAllWhere(){

    }

    User findUserById(long id){
        User.findById(id)
    }

    List<CommonUser> findAllUsersBySuperUser(SuperUser superUser) {
        CommonUser.findAllBySuperUser(superUser)
    }
    List<CommonUser> findAllUserByTeam(Team team){
        CommonUser.findAllByTeam(team)
    }

    User saveUser(User user){
        user.save(failOnError: true)
    }

    CommonUser findSiblingUserByTeam(User user, Team team){
        CommonUser.findBySuperUserAndTeam(user.superUser, team)
    }
    List<CommonUser> findAllSiblingUsersByTeam(User user, Team team){
        CommonUser.findAllBySuperUserAndTeam(user.superUser, team)
    }

    /**
     * 如果user的superUser不存在，则保存superUser信息
     * @param user
     */
    void checkSuperUser(User user){
        SuperUser superUser = user.superUser
        if(null == superUser){
            superUser = new SuperUser(user, user);
            superUser.save(failOnError: true)

            user.superUser = superUser
            user.save(failOnError: true)
        }
    }
}
