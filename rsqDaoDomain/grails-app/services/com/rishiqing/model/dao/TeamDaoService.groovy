package com.rishiqing.model.dao

import com.rishiqing.domain.user.SuperUser
import com.rishiqing.domain.user.Team
import com.rishiqing.domain.user.User
import com.rishiqing.domain.user.UserJoinTeamHistory

class TeamDaoService {

    Team findTeamById(long id){
        Team.findById(id)
    }

    Team saveTeam(Team team){
        team.save(failOnError: true)
    }

    void saveHistory(UserJoinTeamHistory history){
        history.save(failOnError: true)
    }

    UserJoinTeamHistory findJoinHistory(User user, Team team){
        SuperUser su = user.superUser
        UserJoinTeamHistory.findBySuperUserAndTeam(su, team)
    }
}
