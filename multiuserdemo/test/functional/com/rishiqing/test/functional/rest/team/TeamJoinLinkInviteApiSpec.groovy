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
 * 链接邀请测试用例
 * Created by  on 2017/9/4.Wallace
 */
@Stepwise
class TeamJoinLinkInviteApiSpec extends BaseApi {
    @Shared def suiteEnv = ConfigUtil.config.suite.teamJoin

    def setupSpec(){
//        RsqRestUtil.config([proxy: ['127.0.0.1': 5555]])

//          使用SqlPrepare中的genCleanMultiUserUsers生成SQL语句，做账号清理
        if(ConfigUtil.config?.dataSource) {
            SqlUtil.execute(SqlPrepare.genCleanMultiUserUsers([
                    domainRegexp: "@${ConfigUtil.config.suite.teamJoin.emailDomain}\$"
            ]))
        }
    }

    def setup(){
        RsqRestUtil.clearCookies();
    }

    def cleanup(){}

    @Unroll
    def "注册相关用户：#registerUser"(){
        when: '注册用户'
        RsqRestResponse resp = AccountApi.register(registerUser as Map)

        then: '验证注册结果'
        AccountApi.checkRegister(resp)

        where:
        registerUser << [
                suiteEnv.teamManager,
                suiteEnv.userWithEmailForJoinLinkRegistered,
                suiteEnv.userWithPhoneForJoinLinkRegistered,
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

    @Unroll
    def "团队管理员 #teamManager 创建团队 #mainTeam"(){
        given:
        RsqRestResponse resp

        when:
        resp = TeamApi.loginAndCreateTeam(teamManager as Map, mainTeam as Map)
        then:
        TeamApi.checkCreateTeam(resp)

        where:
        teamManager = suiteEnv.teamManager
        mainTeam = suiteEnv.teamForJoin
    }

    @Unroll
    def "团队管理员 #mainLinkJoinUser 登录，并生成团队 #mainLinkJoinTeam 的邀请链接"(){
        when: '登录'
        RsqRestResponse resp = AccountApi.login(mainLinkJoinUser as Map)

        then: '验证登录'
        AccountApi.checkLogin(resp)

        when: '批量邀请用户'
        resp = TeamApi.linkInvite()

        then:
        TeamApi.checkSuccess(resp)

        when:
        mainLinkJoinTeam.t = resp.json.token
        resp = AccountApi.logout()

        then:
        AccountApi.checkLogout(resp)

        where:
        mainLinkJoinTeam = suiteEnv.teamForJoin
        mainLinkJoinUser = suiteEnv.teamManager
    }

    @Unroll
    def "未注册过的用户 #unregisteredLinkJoinUser 通过邀请链接直接加入到团队 #unregisteredLinkJoinTeam"(){
        given:
        RsqRestResponse resp

        when: '注册用户'
        resp = AccountApi.register(unregisteredLinkJoinUser as Map, unregisteredLinkJoinTeam as Map)

        then: '验证注册结果'
        AccountApi.checkRegister(resp)

        when:
        resp = AccountApi.fetchLoginInfo()

        then:
        resp.status == 200
        resp.json.id != null
        resp.json.team != null
        resp.json.team.name == unregisteredLinkJoinTeam.name

        when:
        resp = AccountApi.logout()

        then:
        AccountApi.checkLogout(resp)

        where:
        unregisteredLinkJoinTeam = suiteEnv.teamForJoin
        unregisteredLinkJoinUser << [
                suiteEnv.userWithEmailForJoinLink,
                suiteEnv.userWithPhoneForJoinLink
        ]
    }

    @Unroll
    def "已经注册的用户 #registeredLinkJoinUser 通过链接加入团队 #registeredLinkJoinTeam"(){
        given:
        RsqRestResponse resp

        when:
        resp = TeamApi.joinInTeamOutside(registeredLinkJoinTeam as Map, registeredLinkJoinUser as Map)

        then:
        TeamApi.checkJoinInTeamOutside(resp)

        when:
        resp = AccountApi.fetchLoginInfo()

        then:
        resp.status == 200
        resp.json.id != null
        resp.json.team != null
        resp.json.team.name == registeredLinkJoinTeam.name

        when:
        resp = AccountApi.logout()

        then:
        AccountApi.checkLogout(resp)

        where:
        registeredLinkJoinTeam = suiteEnv.teamForJoin
        registeredLinkJoinUser << [
                suiteEnv.userWithEmailForJoinLinkRegistered,
                suiteEnv.userWithPhoneForJoinLinkRegistered
        ]
    }
}
