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
 * 直接邀请测试用例
 * Created by  on 2017/9/4.Wallace
 */
@Stepwise
class TeamJoinDirectInviteApiSpec extends BaseApi {
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
        RsqRestUtil.clearCookies()
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
                suiteEnv.userWithEmailForDirectInviteRegistered,
                suiteEnv.userWithPhoneForDirectInviteRegistered
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
    def "管理员 #mainInviteUser 直接邀请用户 #directInviteUser"(){
        when: '登录'
        RsqRestResponse resp = AccountApi.login(mainInviteUser as Map)

        then: '验证登录'
        AccountApi.checkLogin(resp)

        when: '验证是否已经注册'
        resp = AccountApi.registerVerify(directInviteUser as Map)

        then:
        AccountApi.checkRegisterVerify(resp, [registered: verifiedResult])

        when: '直接邀请用户'
        resp = TeamApi.directInvite(directInviteUser as Map)

        then:
//        返回值：{"success":true,"inviteResult":[{"id":521898,"dateCreated":"2017-08-31 17:37:58","deleteUser":false,"hasAvatar":false,"username":"421503610@qq.com","t":"bd307b793ed6436595575ce949d3f6c3"}],"unconfirmed":1,"inviteSuccess":1,"inviteFailure":0}
        TeamApi.checkDirectInvite(resp, [inviteSuccess: 1])

        when:
        if(resp.jsonMap.inviteResult.size() != 0){
            directInviteUser.t = resp.jsonMap.inviteResult[0].t
        }
        resp = AccountApi.logout()

        then:
        AccountApi.checkLogout(resp)

        where:
        mainInviteUser = suiteEnv.teamManager
        directInviteUser << [
                suiteEnv.userWithEmailForDirectInvite,
                suiteEnv.userWithPhoneForDirectInvite,
                suiteEnv.userWithEmailForDirectInviteRegistered,
                suiteEnv.userWithPhoneForDirectInviteRegistered
        ]
        verifiedResult << [false, false, true, true]
    }

    @Unroll
    def "未注册的用户 #directInvitedUnregisteredLoginUser 直接加入到团队中"(){
        when: '登录'
        RsqRestResponse resp = AccountApi.login(directInvitedUnregisteredLoginUser as Map)

        then: '验证登录'
        AccountApi.checkLogin(resp)

        when: '验证登录后是否加入到团队'
        resp = AccountApi.fetchLoginInfo()

        then:
        resp.status == 200
        resp.json.id != null
        resp.json.team != null
        resp.json.team.name == directInvitedUnregisteredLoginTeam.name

        where:
        directInvitedUnregisteredLoginTeam = suiteEnv.teamForJoin
        directInvitedUnregisteredLoginUser << [
                suiteEnv.userWithEmailForDirectInvite,
                suiteEnv.userWithPhoneForDirectInvite
        ]
    }

    @Unroll
    def "已经注册的用户 #directInviteLoginUser 登录后，直接加入团队"(){
        when: '登录'
        RsqRestResponse resp = AccountApi.login(directInviteLoginUser as Map)

        then: '验证登录'
        AccountApi.checkLogin(resp)

        when:
        Map params = [
                t: directInviteLoginUser.t
        ]
        resp = TeamApi.joinInTeam(params)

        then:
        TeamApi.checkJoinInTeam(resp, [team: directInviteLoginTeam])

        when:
        resp = AccountApi.fetchLoginInfo()

        then:
        resp.status == 200
        resp.json.id != null
        resp.json.team != null
        resp.json.team.name == directInviteLoginTeam.name

        when:
        resp = AccountApi.logout()

        then:
        AccountApi.checkLogout(resp)

        where:
        directInviteLoginTeam = suiteEnv.teamForJoin
        directInviteLoginUser << [
                suiteEnv.userWithEmailForDirectInviteRegistered,
                suiteEnv.userWithPhoneForDirectInviteRegistered
        ]
    }
}
