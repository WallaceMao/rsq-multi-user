package com.rishiqing.test.functional.rest.team

import com.rishiqing.demo.util.http.RsqRestResponse
import com.rishiqing.demo.util.http.RsqRestUtil
import com.rishiqing.test.functional.BaseApi
import com.rishiqing.test.functional.ConfigUtil
import com.rishiqing.test.functional.api.AccountApi
import com.rishiqing.test.functional.api.TeamApi
import com.rishiqing.test.functional.util.db.SqlPrepare
import com.rishiqing.test.functional.util.db.SqlUtil
import spock.lang.Shared
import spock.lang.Stepwise
import spock.lang.Unroll

/**
 * 团队创建相关的用例
 * Created by  on 2017/9/6.Wallace
 */
@Stepwise
class TeamCreateApiSpec extends BaseApi {
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
        RsqRestUtil.clearCookies()
    }

    @Unroll
    def "注册相关的用户：#registerUser"(){
        when: '注册用户'
        RsqRestResponse resp = AccountApi.register(registerUser as Map)

        then: '验证注册结果'
        AccountApi.checkRegister(resp)

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
    def "等待3000ms，以便服务器生成默认的日程、计划、笔记等数据"(){
        when:
        Thread.sleep(3000)
        then:
        true
    }

    /**
     * 测试直接创建两家公司
     * user1：创建team1——》创建成功并携带文集
     * user1：创建team2——》创建成功，user1有两家公司
     */
    def "user1 创建团队 team1 和 team2"(){
        given:
        RsqRestResponse resp

        when: '用户1创建团队1'
        resp = TeamApi.loginAndCreateTeam(suiteEnv.teamUser1 as Map, suiteEnv.team1ForCreate as Map)
        then:
        TeamApi.checkCreateTeam(resp)

        when: 'user1创建team2'
        AccountApi.loginAndCheck(suiteEnv.teamUser1)
        resp = TeamApi.createTeam(suiteEnv.teamUser1, suiteEnv.team2ForCreate)
        then:
        TeamApi.checkCreateTeam(resp)

        when: '当前用户是团队4的user'
        resp = AccountApi.fetchMe()
        then:
        AccountApi.checkFetchMe(resp, [team: suiteEnv.team2ForCreate])

        when:"获取用户1所有团队的列表"
        resp = AccountApi.fetchUserSiblings()
        then:
        AccountApi.checkFetchUserSiblings(resp, [teamList: [suiteEnv.team1ForCreate, suiteEnv.team2ForCreate]])
        AccountApi.logoutAndCheck()
    }

    /**
     * 测试有加入有创建的情况
     * user2：创建team3——》创建成功并携带文集
     * user3：加入team3——》加入成功，不携带文集
     * user3：创建team4——》创建成功并携带文集，user3有两家公司
     */
    def "user2 创建 team3 然后邀请 user3 --> user3 加入 team3 --> user3 创建 team4"(){
        given:
        RsqRestResponse resp

        when: 'user2创建team3'
        resp = TeamApi.loginAndCreateTeam(suiteEnv.teamUser2, suiteEnv.team3ForCreate)
        then:
        TeamApi.checkCreateTeam(resp)

        when: 'user2邀请用户3'
        resp = TeamApi.loginAndDirectInviteAndJoinInTeam(
                suiteEnv.team3ForCreate as Map,
                suiteEnv.teamUser2 as Map,
                suiteEnv.teamUser3 as Map)
        then:
        TeamApi.checkJoinInTeam(resp)

        when: '用户3创建团队4'
        AccountApi.loginAndCheck(suiteEnv.teamUser3)
        resp = TeamApi.createTeam(suiteEnv.teamUser3, suiteEnv.team4ForCreate)
        then:
        TeamApi.checkCreateTeam(resp)

        when: '当前用户是团队4的user'
        resp = AccountApi.fetchMe()
        then:
        AccountApi.checkFetchMe(resp, [team: suiteEnv.team4ForCreate])

        when:"获取用户所有团队的列表"
        resp = AccountApi.fetchUserSiblings()
        then:
        AccountApi.checkFetchUserSiblings(resp, [teamList: [suiteEnv.team3ForCreate, suiteEnv.team4ForCreate]])
        AccountApi.logoutAndCheck()
    }

    /**
     * 测试退出后创建
     * user4：创建team5——》创建成功并携带文集
     * user4：退出team5——》退出成功
     * user4：创建team6——》创建成功，user4有一家公司
     */
    def "user4 创建 team5 --> user4 退出 team5 --> user4 创建 team6"(){
        given:
        RsqRestResponse resp

        when: 'user4 创建team5'
        resp = TeamApi.loginAndCreateTeam(suiteEnv.teamUser4, suiteEnv.team5ForCreate)
        then:
        TeamApi.checkCreateTeam(resp)

        when: 'user4退出当前所在团队，即team5'
        resp = TeamApi.loginAndQuitTeam(suiteEnv.teamUser4)
        then:
        TeamApi.checkQuitTeam(resp)

        when: '获取用户的team列表'
        //  TODO  这个地方看需要获取team还是获取user
        resp = AccountApi.loginAndFetchUserSiblings(suiteEnv.teamUser4)
        then:
        AccountApi.checkFetchUserSiblings(resp, [teamList: [], noTeamList: [suiteEnv.teamUser4]])

        when: 'user4创建团队6'
        resp = TeamApi.loginAndCreateTeam(suiteEnv.teamUser4, suiteEnv.team6ForCreate)
        then:
        TeamApi.checkCreateTeam(resp)

        when: '获取用户的team列表'
        resp = AccountApi.loginAndFetchUserSiblings(suiteEnv.teamUser4)
        then:
        AccountApi.checkFetchUserSiblings(resp, [teamList: [suiteEnv.team6ForCreate], noTeamList: [suiteEnv.teamUser4]])
    }

    /**
     * 测试加入后创建
     * user5：创建team7——》创建成功并携带文集
     * user6：加入team7——》加入成功，不携带文集
     * user6：退出team7——》退出成功
     * user6：创建team8——》创建成功并携带文集，user6有一家公司
     */
    def "user5 创建 team7 然后邀请 user6 --> user6 加入 team7 --> user6 退出 team7 --> user6 创建 team8"(){
        given:
        RsqRestResponse resp

        when: 'user5 登录创建团队7'
        resp = TeamApi.loginAndCreateTeam(suiteEnv.teamUser5, suiteEnv.team7ForCreate)
        then:
        TeamApi.checkCreateTeam(resp)

        when: '用户5邀请用户6'
        resp = TeamApi.loginAndDirectInviteAndJoinInTeam(
                suiteEnv.team7ForCreate as Map,
                suiteEnv.teamUser5 as Map,
                suiteEnv.teamUser6 as Map)
        then:
        TeamApi.checkJoinInTeam(resp)

        when: '用户6登录退出团队7'
        resp = TeamApi.loginAndQuitTeam(suiteEnv.teamUser6)
        then:
        TeamApi.checkQuitTeam(resp)

        when: '用户6登录'
        resp = AccountApi.loginAndCheck(suiteEnv.teamUser6)
        resp = TeamApi.createTeam(suiteEnv.teamUser6, suiteEnv.team8ForCreate)
        then:
        TeamApi.checkCreateTeam(resp)

        when: '当前用户是团队6的user'
        resp = AccountApi.fetchMe()
        then:
        AccountApi.checkFetchMe(resp, [team: suiteEnv.team8ForCreate])

        when:"获取用户所有团队的列表"
        resp = AccountApi.fetchUserSiblings()
        then:
        AccountApi.checkFetchUserSiblings(resp, [teamList: [suiteEnv.team8ForCreate]])
        AccountApi.logoutAndCheck()
    }
}
