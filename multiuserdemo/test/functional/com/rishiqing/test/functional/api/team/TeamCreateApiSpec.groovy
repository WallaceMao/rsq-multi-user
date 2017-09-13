package com.rishiqing.test.functional.api.team

import com.rishiqing.demo.util.http.RsqRestResponse
import com.rishiqing.demo.util.http.RsqRestUtil
import com.rishiqing.test.functional.ConfigUtil
import com.rishiqing.test.functional.util.SqlPrepare
import com.rishiqing.test.functional.util.SqlUtil
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Stepwise
import spock.lang.Unroll

/**
 * Created by  on 2017/9/6.Wallace
 */
@Stepwise
class TeamCreateApiSpec extends BaseTeamApiGebSpec {
    @Shared def suiteEnv = ConfigUtil.config.suite.teamCreate

    def setupSpec(){
//        RsqRestUtil.config([proxy: ['127.0.0.1': 5555]])

//          使用SqlPrepare中的genCleanMultiUserUsers生成SQL语句，做账号清理
        if(ConfigUtil.config?.dataSource) {
            SqlUtil.execute(SqlPrepare.genCleanMultiUserUsers([
                    domainRegexp: "@${suiteEnv.emailDomain}\$"
            ]))
        }
    }

    def setup(){
        RsqRestUtil.clearCookies();
    }

    @Unroll
    def "register relative users #registerUser"(){
        when: '注册用户'
        RsqRestResponse resp = register(registerUser)

        then: '验证注册结果'
        checkRegister(resp)

        where:
        registerUser << [
                suiteEnv.teamUser1,
                suiteEnv.teamUser2,
                suiteEnv.teamUser3,
                suiteEnv.teamUser4,
                suiteEnv.teamUser5,
                suiteEnv.teamUser6
        ]
    }

    /**
     * stepwise时暂停3000ms等待用户注册成功
     * 新注册的用户会有生成默认的日程、计划、笔记，这里加1秒的等待时间，保证生成完成
     */
    def "sleep for user register"(){
        when:
        Thread.sleep(3000)
        then:
        true
    }

    /**
     * user1：创建team1——》创建成功并携带文集
     * user1：创建team2——》创建成功，user1有两家公司
     */
    def "user1 create team1 and team2"(){
        given:
        RsqRestResponse resp

        when: '用户1创建团队1'
        resp = loginAndCreateTeam(suiteEnv.teamUser1, suiteEnv.team1ForCreate)
        then:
        checkCreateTeam(resp)

        when: 'user1创建team2'
        resp = loginAndCheck(suiteEnv.teamUser1)
        resp = createTeam(suiteEnv.teamUser1, suiteEnv.team2ForCreate)
        then:
        checkCreateTeam(resp)

        when: '当前用户是团队4的user'
        resp = fetchMe()
        then:
        checkFetchMe(resp, [team: suiteEnv.team2ForCreate])

        when:"获取用户1所有团队的列表"
        resp = fetchUserSiblings()
        then:
        checkFetchUserSiblings(resp, [teamList: [suiteEnv.team1ForCreate, suiteEnv.team2ForCreate]])
        logoutAndCheck()
    }

    /**
     * user2：创建team3——》创建成功并携带文集
     * user3：加入team3——》加入成功，不携带文集
     * user3：创建team4——》创建成功并携带文集，user3有两家公司
     */
    def "user2 create team3 and invite user3 --> user3 join team3 --> user3 create team4"(){
        given:
        RsqRestResponse resp

        when: 'user2创建team3'
        resp = loginAndCreateTeam(suiteEnv.teamUser2, suiteEnv.team3ForCreate)
        then:
        checkCreateTeam(resp)

        when: 'user2邀请用户3'
        resp = loginAndDirectInviteAndJoinInTeam(suiteEnv.teamUser2, suiteEnv.teamUser3)
        then:
        checkJoinInTeam(resp)

        when: '用户3创建团队4'
        resp = loginAndCheck(suiteEnv.teamUser3)
        resp = createTeam(suiteEnv.teamUser3, suiteEnv.team4ForCreate)
        then:
        checkCreateTeam(resp)

        when: '当前用户是团队4的user'
        resp = fetchMe()
        then:
        checkFetchMe(resp, [team: suiteEnv.team4ForCreate])

        when:"获取用户所有团队的列表"
        resp = fetchUserSiblings()
        then:
        checkFetchUserSiblings(resp, [teamList: [suiteEnv.team3ForCreate, suiteEnv.team4ForCreate]])
        logoutAndCheck()
    }

    /**
     * user4：创建team5——》创建成功并携带文集
     * user4：退出team5——》退出成功
     * user4：创建team6——》创建成功，user4有一家公司
     */
    def "user4 create team5 --> user4 quit team5 --> user4 create team6"(){
        given:
        RsqRestResponse resp

        when: 'user4 创建team5'
        resp = loginAndCreateTeam(suiteEnv.teamUser4, suiteEnv.team5ForCreate)
        then:
        checkCreateTeam(resp)

        when: 'user4退出当前所在团队，即team5'
        resp = loginAndQuitTeam(suiteEnv.teamUser4)
        then:
        checkQuitTeam(resp)

        when: '获取用户的team列表'
        //  TODO  这个地方看需要获取team还是获取user
        resp = loginAndFetchUserSiblings(suiteEnv.teamUser4)
        then:
        checkFetchUserSiblings(resp, [teamList: [], noTeamList: [suiteEnv.teamUser4]])

        when: 'user4创建团队6'
        resp = loginAndCreateTeam(suiteEnv.teamUser4, suiteEnv.team6ForCreate)
        then:
        checkCreateTeam(resp)

        when: '获取用户的team列表'
        resp = loginAndFetchUserSiblings(suiteEnv.teamUser4)
        then:
        checkFetchUserSiblings(resp, [teamList: [suiteEnv.team6ForCreate], noTeamList: [suiteEnv.teamUser4]])
    }

    /**
     * user5：创建team7——》创建成功并携带文集
     * user6：加入team7——》加入成功，不携带文集
     * user6：退出team7——》退出成功
     * user6：创建team8——》创建成功并携带文集，user6有一家公司
     */
    def "user5 create team7 and invite user6 --> user6 join team7 --> user6 quit team7 --> user6 create team8"(){
        given:
        RsqRestResponse resp

        when: 'user5 登录创建团队7'
        resp = loginAndCreateTeam(suiteEnv.teamUser5, suiteEnv.team7ForCreate)
        then:
        checkCreateTeam(resp)

        when: '用户5邀请用户6'
        resp = loginAndDirectInviteAndJoinInTeam(suiteEnv.teamUser5, suiteEnv.teamUser6)
        then:
        checkJoinInTeam(resp)

        when: '用户6登录退出团队7'
        resp = loginAndQuitTeam(suiteEnv.teamUser6)
        then:
        checkQuitTeam(resp)

        when: '用户6登录'
        resp = loginAndCheck(suiteEnv.teamUser6)
        resp = createTeam(suiteEnv.teamUser6, suiteEnv.team8ForCreate)
        then:
        checkCreateTeam(resp)

        when: '当前用户是团队6的user'
        resp = fetchMe()
        then:
        checkFetchMe(resp, [team: suiteEnv.team8ForCreate])

        when:"获取用户所有团队的列表"
        resp = fetchUserSiblings()
        then:
        checkFetchUserSiblings(resp, [teamList: [suiteEnv.team8ForCreate]])
        logoutAndCheck()
    }
}
