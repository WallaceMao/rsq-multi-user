package com.rishiqing.test.functional.rest

import com.rishiqing.demo.util.http.RsqRestResponse
import com.rishiqing.demo.util.http.RsqRestUtil
import com.rishiqing.test.functional.BaseApi
import com.rishiqing.test.functional.ConfigUtil
import com.rishiqing.test.functional.api.AccountApi
import com.rishiqing.test.functional.api.PlanApi
import com.rishiqing.test.functional.api.ScheduleApi
import com.rishiqing.test.functional.api.TeamApi
import com.rishiqing.test.functional.util.db.DomainUtil
import com.rishiqing.test.functional.util.db.SqlPrepare
import com.rishiqing.test.functional.util.db.SqlUtil
import org.codehaus.groovy.grails.web.json.JSONObject
import org.junit.Ignore
import spock.lang.Shared
import spock.lang.Stepwise
import spock.lang.Unroll

/**
 * Created by  on 2017/8/30.Wallace
 */
//@Ignore
@Stepwise
class MultiUserApiSpec extends BaseApi {

    @Shared def userEnv = ConfigUtil.config.suite.multiuser

    def setupSpec(){
//        RsqRestUtil.config([proxy: ['127.0.0.1': 5555]])

//          使用SqlPrepare中的genCleanMultiUserUsers生成SQL语句，做账号清理
        if(ConfigUtil.config?.dataSource) {
            SqlUtil.execute(SqlPrepare.genCleanMultiUserUsers([
                    domainRegexp: "@${ConfigUtil.config.global.testEmailDomain}\$"
            ]))
        }
    }

    def setup(){
        RsqRestUtil.clearCookies()
    }

    def cleanup(){}

    @Unroll
    def "注册用户 #emailUser"(){
        when: '注册用户'
        RsqRestResponse resp = AccountApi.register(emailUser as Map)

        then: '验证注册结果'
        AccountApi.checkRegister(resp)

        where:
        emailUser << [
                userEnv.userForPersonal,
                userEnv.userForTeamCreate,
                userEnv.userForInvitePersonal,
                userEnv.userForInviteTeam
        ]
    }

    @Unroll
    def "用户: #teamLoginUser 创建团队 #teamCreated"(){
        when: '创建团队'
        RsqRestResponse resp = TeamApi.loginAndCreateTeam(teamLoginUser as Map, teamCreated as Map)
        then: '验证创建团队'
        TeamApi.checkCreateTeam(resp)

        where:
        teamLoginUser << [userEnv.userForInviteTeam, userEnv.userForTeamCreate]
        teamCreated << [userEnv.teamForAnother, userEnv.teamForCreate]
    }

    @Ignore
    @Unroll
    def "个人用户 #loginUser 登录，然后创建 schedule/plan/summary"(){
        when: '登录'
        RsqRestResponse resp = AccountApi.login(loginUser as Map)
        then:
        AccountApi.checkLogin(resp)
        JSONObject resultUser = resp.json

        when: '创建日程'
        Map randomTodo = DomainUtil.genRandomTodo(resultUser.getLong("id"))
        resp = ScheduleApi.createTodo(randomTodo)

        then:
//        println resp.body
        resp.status == 200
        resp.json.id
        resp.json.pTitle == randomTodo.pTitle

        when: '创建计划'
        Map randomPlan = DomainUtil.genRandomPlan()
        resp = PlanApi.createPlan(randomPlan)

        then:
//        println resp.body
        resp.status == 200
        resp.json.id
        resp.json.name == randomPlan.name

        when:
        resp = AccountApi.logout()

        then:
        AccountApi.checkLogout(resp)

        where:
        loginUser << [userEnv.userForPersonal]

    }

    @Unroll
    def "公司创建者 #mainUser 邀请用户 #invitedUser 加入公司"(){
        when: '登录'
        RsqRestResponse resp = AccountApi.login(mainUser as Map)

        then: '验证登录'
        AccountApi.checkLogin(resp)

        when: '验证是否已经注册'
        resp = AccountApi.registerVerify(invitedUser as Map)

        then:
        AccountApi.checkRegisterVerify(resp, [registered: verifiedResult])

        when: '直接邀请用户'
        resp = TeamApi.directInvite(invitedUser as Map)

        then:
        TeamApi.checkDirectInvite(resp, [inviteSuccess: 1])

        when:
        if(resp.jsonMap.inviteResult.size() != 0){
            invitedUser.t = resp.jsonMap.inviteResult[0].t
        }
        resp = AccountApi.logout()

        then:
        AccountApi.checkLogout(resp)

        where:
        mainUser = userEnv.userForTeamCreate
        invitedUser << [
                userEnv.userForInvitePersonal,
                userEnv.userForInviteTeam,
                userEnv.userForInviteNotRegistered
        ]
        verifiedResult << [true, true, false]
    }

    @Unroll
    def "用户 #acceptInvitedUser 登录，然后接受邀请，加入到团队 #acceptInvitedTeam，那么该用户当前的团队列表包括 #teamList"(){
        when: '登录'
        RsqRestResponse resp = AccountApi.login(acceptInvitedUser as Map)

        then: '验证登录'
        AccountApi.checkLogin(resp)

        when:
        resp = TeamApi.joinInTeam(acceptInvitedUser as Map)

        then:
        TeamApi.checkJoinInTeam(resp, [team: acceptInvitedTeam])

        when:
        resp = AccountApi.fetchUserSiblings()

        then:
        AccountApi.checkFetchUserSiblings(resp, [teamList: teamList])
        AccountApi.logoutAndCheck()

        where:
        acceptInvitedTeam = userEnv.teamForCreate
        acceptInvitedUser << [
                userEnv.userForInvitePersonal,
                userEnv.userForInviteTeam
        ]
        teamList << [
                [userEnv.teamForCreate],
                [userEnv.teamForAnother, userEnv.teamForCreate]
        ]
    }

    @Unroll
    def "#quitTeamUser 退出团队 #mainTeam， 用户目前的团队列表为：#teamList"(){
        when: '登录'
        RsqRestResponse resp = AccountApi.login(quitTeamUser as Map)

        then: '验证登录'
        AccountApi.checkLogin(resp)

        when: '切换到mainTeam的superUser'
        resp = AccountApi.fetchUserSiblings()
        List userList = resp.jsonMap.list
        Map mainTeamUser = (Map)userList.find{ it.teamName == mainTeam.name }
        resp = AccountApi.switchUser(mainTeamUser)
        resp = AccountApi.fetchLoginInfo()
        println resp.json

        then:
        resp.status == 200
        resp.json.success == true
        resp.json.team.name == mainTeamUser.team.name

        when: '退出团队'
        resp = TeamApi.quitTeam()

        then:
        TeamApi.checkQuitTeam(resp)

        when:
        resp = AccountApi.fetchUserSiblings()

        then:
        AccountApi.checkFetchUserSiblings(resp, [teamList: teamList])

        when:
        resp = AccountApi.logout()

        then:
        AccountApi.checkLogout(resp)

        where:
        mainTeam = userEnv.teamForCreate
        quitTeamUser << [
//                userEnv.userForInvitePersonal,
                userEnv.userForInviteTeam,
//                userEnv.userForInviteNotRegistered
        ]
        teamList << [
//                [],
                [userEnv.teamForAnother],
//                []
        ]
    }

    @Ignore
    @Unroll
    def "用户 #reinvitedmainUser 登录，再次邀请 #reinvitedUser 加入团队"(){
        when: '登录'
        RsqRestResponse resp = AccountApi.login(reinvitedmainUser as Map)

        then: '验证登录'
        AccountApi.checkLogin(resp)

        when: '验证是否已经注册'
        resp = AccountApi.registerVerify(reinvitedUser as Map)

        then:
        AccountApi.checkRegisterVerify(resp, [registered: verifiedResult])

        when: '直接邀请用户'
        resp = TeamApi.directInvite(reinvitedUser as Map)

        then:
//        返回值：{"success":true,"inviteResult":[{"id":521898,"dateCreated":"2017-08-31 17:37:58","deleteUser":false,"hasAvatar":false,"username":"421503610@qq.com","t":"bd307b793ed6436595575ce949d3f6c3"}],"unconfirmed":1,"inviteSuccess":1,"inviteFailure":0}
        TeamApi.checkDirectInvite(resp, [inviteSuccess: 1])

        when:
        if(resp.jsonMap.inviteResult.size() != 0){
            reinvitedUser.t = resp.jsonMap.inviteResult[0].t
        }

        then:
        true

        where:
        reinvitedmainUser = userEnv.userForTeamCreate
        reinvitedUser << [
                userEnv.userForInvitePersonal,
                userEnv.userForInviteTeam,
                userEnv.userForInviteNotRegistered
        ]
        verifiedResult << [true, true, true]
    }

    @Ignore
    @Unroll
    def "用户 #acceptReinviteUser 接受邀请，加入团队 #acceptReinvitedTeam"(){
        when: '登录'
        RsqRestResponse resp = AccountApi.login(acceptReinviteUser as Map)

        then: '验证登录'
        AccountApi.checklogin(resp)

        when:
        resp = TeamApi.joinInTeam(acceptReinviteUser as Map)

        then:
        TeamApi.checkJoinInTeam(resp, [team: acceptReinvitedTeam])

        when:
        resp = AccountApi.fetchLoginInfo()

        then:
        resp.status == 200
        resp.json.id != null
        resp.json.team != null
        resp.json.team.name == acceptReinvitedTeam.name

        when:
        resp = AccountApi.logout()

        then:
        AccountApi.checkLogout(resp)

        where:
        acceptReinvitedTeam = userEnv.teamForCreate
        acceptReinviteUser << [
                userEnv.userForInvitePersonal,
                userEnv.userForInviteTeam
        ]
    }

    @Ignore
    @Unroll
    def "用户 #deletedMainUser 删除团队"(){
        when: '登录'
        Map params = [
                '_spring_security_remember_me': true,
                'j_password': deletedMainUser.password,
                'j_username': deletedMainUser.username
        ]
        RsqRestResponse resp = RsqRestUtil.post("${baseUrl}${path}j_spring_security_check"){
            header 'content-type', 'application/x-www-form-urlencoded;charset=UTF-8'
            header 'X-Requested-With', 'XMLHttpRequest'
            fields params
        }

        then: '验证登录'
        println resp.body
        resp.status == 200
        resp.json.success == true

        when:
        resp = TeamApi.deleteTeam(deletedMainUser as Map)

        then:
        TeamApi.checkDeleteTeam(resp)

        when:
        resp = AccountApi.fetchLoginInfo()

        then:
        resp.status == 200
        resp.json.id != null
        resp.json.team == null

        when:
        resp = AccountApi.logout()

        then:
        AccountApi.checkLogout(resp)

        where:
        deletedMainUser = userEnv.userForTeamCreate
    }

    @Ignore
    @Unroll
    def "团队解散后，团队成员 #deletedOriginUser 自动退出团队"(){
        when: '登录'
        RsqRestResponse resp = AccountApi.login(deletedOriginUser as Map)

        then: '验证登录'
        AccountApi.checkLogin(resp)

        when:
        resp = AccountApi.fetchLoginInfo()

        then:
        resp.status == 200
        resp.json.id != null
        resp.json.team == null

        when:
        resp = AccountApi.logout()

        then:
        AccountApi.checkLogout(resp)

        where:
        deletedOriginUser << [
                userEnv.userForInvitePersonal,
                userEnv.userForInviteTeam
        ]
    }
}
