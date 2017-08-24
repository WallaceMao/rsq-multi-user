package com.rishiqing.mid.dao

import com.rishiqing.domain.user.CommonUser
import com.rishiqing.domain.user.SuperUser
import com.rishiqing.domain.user.Team
import com.rishiqing.domain.user.User
import com.rishiqing.domain.user.UserJoinTeamHistory
import com.rishiqing.mid.UsefulSpecification
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import grails.test.runtime.FreshRuntime
import grails.validation.ValidationException
import spock.lang.Ignore
import spock.lang.IgnoreRest
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@FreshRuntime
@TestFor(DaoUserService)
@Mock([User,CommonUser, SuperUser, Team, UserJoinTeamHistory])
class DaoUserServiceSpec extends UsefulSpecification {

    @Shared User userWithNone
    @Shared User userWithoutSuperUser
    @Shared User userWithoutTeam
    @Shared User userCommon
    @Shared Team team
    @Shared Team team2
    @Shared SuperUser superUser
    @Shared SuperUser superUser2

    def setupSpec(){}

    def cleanupSpec(){

    }

    def setup() {}

    def cleanup() {}

    //  初始化场景的方法
    def beforeSetup(){
        userWithNone = new CommonUser('user1@qq.com', 'user1', '用户1')

        userWithoutSuperUser = new CommonUser('user2@qq.com', 'user2', '用户2')
        team = new Team("user1team")
        userWithoutSuperUser.team = team

        userWithoutTeam = new CommonUser('user3@qq.com', 'user3', '用户3')
        superUser = new SuperUser(userWithoutTeam, userWithoutTeam)
        userWithoutTeam.superUser = superUser

        userCommon = new CommonUser('user4@qq.com', 'user4', '用户4')
        superUser2 = new SuperUser(userCommon, userCommon)
        team2 = new Team("user4team")
        userCommon.superUser = superUser2
        userCommon.team = team2
    }

    void "userCheckSuperUser: when user.superUser is null add superUser; when user.superUser is not null return superUser"() {
        when:
        service.userCheckSuperUser(userWithoutSuperUser)

        then:
        userWithoutSuperUser.superUser != null
        userWithoutSuperUser.superUser.mainUser == userWithoutSuperUser
        userWithoutSuperUser.superUser.defaultLoginUser == userWithoutSuperUser

        when:
        service.userCheckSuperUser(userCommon)

        then:
        userCommon.superUser.is(superUser2)
    }

    @Unroll
    void "test userRemoveTeam type is #typeTable , result table is #resultTable"(){
        when:
        service.userRemoveTeam(userCommon, typeTable)

        then:
        userCommon.team == null
        UserJoinTeamHistory.count() == 1
        UserJoinTeamHistory history = UserJoinTeamHistory.get(1)
        history.team == team2
        history.dateCreated != null
        history.superUser == superUser2
        history.user == userCommon
        history.type == resultTable

        where:
        typeTable << [
                UserJoinTeamHistory.TYPE_QUIT_TEAM,
                UserJoinTeamHistory.TYPE_DISMISS_TEAM,
                UserJoinTeamHistory.TYPE_REMOVE_TEAM
        ]
        resultTable << [
                UserJoinTeamHistory.TYPE_QUIT_TEAM,
                UserJoinTeamHistory.TYPE_DISMISS_TEAM,
                UserJoinTeamHistory.TYPE_REMOVE_TEAM
        ]
    }
    @Unroll
    void "userRemoveTeam with user: [#userTable] and type: [#typeTable] should throw exception: [#exceptionTable]"(){

        when:
        service.userRemoveTeam(userTable, typeTable)

        then:
        thrown(exceptionTable)

        where:
        userTable << [
                null,
                userWithNone,
                userWithoutSuperUser,
                userWithoutTeam,
                userCommon
        ]
        typeTable << [
                UserJoinTeamHistory.TYPE_QUIT_TEAM,
                UserJoinTeamHistory.TYPE_QUIT_TEAM,
                UserJoinTeamHistory.TYPE_QUIT_TEAM,
                UserJoinTeamHistory.TYPE_QUIT_TEAM,
                null
        ]

        exceptionTable << [
                NullPointerException,
                ValidationException,
                ValidationException,
                ValidationException,
                ValidationException,
        ]
    }

    @Unroll
    void "test userAddTeam type is #typeTable and result type is #resultTable"(){

        when:
        service.userAddTeam(userWithoutTeam, team, typeTable)

        then:
        userWithoutTeam.team == team
        UserJoinTeamHistory.count() == 1
        UserJoinTeamHistory history = UserJoinTeamHistory.get(1)
        history.team == team
        history.dateCreated != null
        history.superUser == superUser
        history.user == userWithoutTeam
        history.type == resultTable

        where:
        typeTable << [
                UserJoinTeamHistory.TYPE_CREATE_TEAM,
                UserJoinTeamHistory.TYPE_JOIN_TEAM
        ]
        resultTable << [
                UserJoinTeamHistory.TYPE_CREATE_TEAM,
                UserJoinTeamHistory.TYPE_JOIN_TEAM
        ]
    }

    @Unroll
    void "userAddTeam with user: [#userTable], team: [#teamTable], type: [#typeTable] should throw exception: [#exceptionTable]"(){

        when:
        service.userAddTeam(userTable, teamTable, typeTable)

        then:
        thrown(exceptionTable)

        where:
        userTable << [
                null,
                userWithoutSuperUser,
                userWithoutTeam,
                userWithoutTeam
        ]
        teamTable << [team, team, null, team]
        typeTable << [
                UserJoinTeamHistory.TYPE_JOIN_TEAM,
                UserJoinTeamHistory.TYPE_JOIN_TEAM,
                UserJoinTeamHistory.TYPE_JOIN_TEAM,
                null
        ]
        exceptionTable << [
                NullPointerException,
                ValidationException,
                ValidationException,
                ValidationException,
        ]
    }
}
