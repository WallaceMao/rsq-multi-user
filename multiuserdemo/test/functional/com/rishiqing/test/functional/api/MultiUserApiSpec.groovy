package com.rishiqing.test.functional.api

import com.rishiqing.demo.util.http.RsqRestResponse
import com.rishiqing.demo.util.http.RsqRestUtil
import com.rishiqing.test.functional.BaseApiGebSpec
import com.rishiqing.test.functional.ConfigUtil
import com.rishiqing.test.functional.util.DomainUtil
import com.rishiqing.test.functional.util.SqlPrepare
import com.rishiqing.test.functional.util.SqlUtil
import org.codehaus.groovy.grails.web.json.JSONArray
import org.codehaus.groovy.grails.web.json.JSONObject
import spock.lang.Ignore
import spock.lang.IgnoreRest
import spock.lang.Shared
import spock.lang.Stepwise
import spock.lang.Unroll

/**
 * Created by  on 2017/8/30.Wallace
 */
//@Ignore
@Stepwise
class MultiUserApiSpec extends BaseApiGebSpec {

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
        RsqRestUtil.clearCookies();
    }

    def cleanup(){}

    @Unroll
    def "register user used in multi user test, the user is #emailUser"(){
        when: '注册用户'
        Map params = [
                'username': emailUser.username,
                'password': emailUser.password,
                'realName': emailUser.realName,
                'NECaptchaValidate': 'random_test_validate_code'
        ]
        RsqRestResponse resp = RsqRestUtil.post("${baseUrl}${path}v2/register"){
            header 'content-type', 'application/x-www-form-urlencoded;charset=UTF-8'
            header 'X-Requested-With', 'XMLHttpRequest'
            fields params
        }

        then: '验证注册结果'
        resp.status == 200

        where:
        emailUser << [
                userEnv.userForPersonal,
                userEnv.userForTeamCreate,
                userEnv.userForInvitePersonal,
                userEnv.userForInviteTeam
        ]
    }

    @Unroll
    def "loginUser: #teamLoginUser create team for"(){
        when: '登录'
        Map params = [
                '_spring_security_remember_me': true,
                'j_password': teamLoginUser.password,
                'j_username': teamLoginUser.username
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

        when: '创建团队'
        //  测试环境下手机号和验证码不做验证
        Map teamParams = [
                name: teamCreated.name,
                contacts: teamLoginUser.realName,
                phoneNumber: '13810360752',
                validate: '1577'
        ]
        resp = RsqRestUtil.post("${baseUrl}${path}team/createTeam"){
            header 'X-Requested-With', 'XMLHttpRequest'
            fields teamParams
        }

        then:
        println resp.body
        resp.status == 200

        when:
        resp = RsqRestUtil.get("${baseUrl}${path}logout/index"){
            header 'X-Requested-With', 'XMLHttpRequest'
        }

        then:
        resp.status == 200

        where:
        teamLoginUser << [userEnv.userForInviteTeam, userEnv.userForTeamCreate]
        teamCreated << [userEnv.teamForAnother, userEnv.teamForCreate]
    }

    @Unroll
    def "personal loginUser #loginUser login and add schedule/plan/summary"(){
        when: '登录'
        Map params = [
                '_spring_security_remember_me': true,
                'j_password': loginUser.password,
                'j_username': loginUser.username
        ]
        RsqRestResponse resp = RsqRestUtil.post("${baseUrl}${path}j_spring_security_check"){
            header 'content-type', 'application/x-www-form-urlencoded;charset=UTF-8'
            header 'X-Requested-With', 'XMLHttpRequest'
            fields params
        }

        then: '验证登录'
        println "loginUser is======${resp.body}"
        resp.status == 200
        resp.json.success == true
        JSONObject resultUser = resp.json

        when: '创建日程'
        Map randomTodo = DomainUtil.genRandomTodo(resultUser.getLong("id"))
        resp = RsqRestUtil.post("${baseUrl}${path}v2/todo/"){
            header 'X-Requested-With', 'XMLHttpRequest'
            fields randomTodo
        }

        then:
//        println resp.body
        resp.status == 200
        resp.json.id
        resp.json.pTitle == randomTodo.pTitle

        when: '创建计划'
        Map randomPlan = DomainUtil.genRandomPlan()
        resp = RsqRestUtil.post("${baseUrl}${path}v2/kanbans/"){
            header 'X-Requested-With', 'XMLHttpRequest'
            fields randomPlan
        }

        then:
//        println resp.body
        resp.status == 200
        resp.json.id
        resp.json.name == randomPlan.name

        when:
        resp = RsqRestUtil.get("${baseUrl}${path}logout/index"){
            header 'X-Requested-With', 'XMLHttpRequest'
        }

        then:
        resp.status == 200

        where:
        loginUser << [userEnv.userForPersonal]

    }

    @Unroll
    def "mainUser #mainUser invite user #invitedUser to main team"(){
        when: '登录'
        Map params = [
                '_spring_security_remember_me': true,
                'j_password': mainUser.password,
                'j_username': mainUser.username
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

        when: '验证是否已经注册'
        resp = RsqRestUtil.get("${baseUrl}${path}v2/register/registerVerify"){
            header 'X-Requested-With', 'XMLHttpRequest'
            queryParams ([u: invitedUser.username])
        }

        then:
        println resp.body
        resp.status == 200
        resp.json.registed == verifiedResult

        when: '直接邀请用户'
        Map inviteParams = [
                account: invitedUser.username,
                deptId: 'unDept',
                password: invitedUser.password,
                realName: invitedUser.realName
        ]
        resp = RsqRestUtil.post("${baseUrl}${path}v2/invite/directInvite"){
            header 'content-type', 'application/x-www-form-urlencoded;charset=UTF-8'
            header 'X-Requested-With', 'XMLHttpRequest'
            fields inviteParams
        }

        then:
//        返回值：{"success":true,"inviteResult":[{"id":521898,"dateCreated":"2017-08-31 17:37:58","deleteUser":false,"hasAvatar":false,"username":"421503610@qq.com","t":"bd307b793ed6436595575ce949d3f6c3"}],"unconfirmed":1,"inviteSuccess":1,"inviteFailure":0}
        println "----resp.body:${resp.body}"
        resp.status == 200
        resp.json.success == true
        resp.json.inviteSuccess == 1

        when:
        if(resp.jsonMap.inviteResult.size() != 0){
            invitedUser.t = resp.jsonMap.inviteResult[0].t
        }
        resp = RsqRestUtil.get("${baseUrl}${path}logout/index"){
            header 'X-Requested-With', 'XMLHttpRequest'
        }

        then:
        resp.status == 200

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
    def "user #acceptInvitedUser accept again the invitation and join the team #acceptInvitedTeam"(){
        when: '登录'
        Map params = [
                '_spring_security_remember_me': true,
                'j_password': acceptInvitedUser.password,
                'j_username': acceptInvitedUser.username
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
        println "-----accept token is------${acceptInvitedUser.t}"
        params = [
                t: acceptInvitedUser.t
        ]
        resp = RsqRestUtil.post("${baseUrl}${path}v2/invite/inviteJoinInTeam"){
            header 'content-type', 'application/x-www-form-urlencoded;charset=UTF-8'
            header 'X-Requested-With', 'XMLHttpRequest'
            fields params
        }

        then:
        resp.status == 200

        when:
        resp = RsqRestUtil.post("${baseUrl}${path}login/authAjax"){
            header 'X-Requested-With', 'XMLHttpRequest'
        }

        then:
        resp.status == 200
        resp.json.id != null
        resp.json.team != null
        resp.json.team.name == acceptInvitedTeam.name

        when:
        resp = RsqRestUtil.get("${baseUrl}${path}logout/index"){
            header 'X-Requested-With', 'XMLHttpRequest'
        }

        then:
        resp.status == 200

        where:
        acceptInvitedTeam = userEnv.teamForCreate
        acceptInvitedUser << [
                userEnv.userForInvitePersonal,
                userEnv.userForInviteTeam
        ]
    }

    @Unroll
    def "join team #mainTeam successfully and then quit team #quitTeamUser"(){
        when: '登录'
        Map params = [
                '_spring_security_remember_me': true,
                'j_password': quitTeamUser.password,
                'j_username': quitTeamUser.username
        ]
        println "params----${params}"
        RsqRestResponse resp = RsqRestUtil.post("${baseUrl}${path}j_spring_security_check"){
            header 'content-type', 'application/x-www-form-urlencoded;charset=UTF-8'
            header 'X-Requested-With', 'XMLHttpRequest'
            fields params
        }

        then: '验证登录'
        println resp.body
        resp.status == 200
        resp.json.success == true
        resp.json.team != null
        resp.json.team.name == mainTeam.name

        when: '退出团队'
        resp = RsqRestUtil.post("${baseUrl}${path}team/quit"){
            header 'X-Requested-With', 'XMLHttpRequest'
        }

        then:
        println "------resp.body----${resp.body}"
        resp.status == 200

        when:
        resp = RsqRestUtil.post("${baseUrl}${path}login/authAjax"){
            header 'X-Requested-With', 'XMLHttpRequest'
        }

        then:
        resp.status == 200
        resp.json.id != null
        resp.json.team == null

        when:
        resp = RsqRestUtil.get("${baseUrl}${path}logout/index"){
            header 'X-Requested-With', 'XMLHttpRequest'
        }

        then:
        resp.status == 200

        where:
        mainTeam = userEnv.teamForCreate
        quitTeamUser << [
                userEnv.userForInvitePersonal,
                //TODO  这里会出现用户名密码错误-----等更新多用户版本之后再看一下
                userEnv.userForInviteTeam,
                userEnv.userForInviteNotRegistered
        ]
    }

    @Unroll
    def "mainUser #reinvitedmainUser login and again invite user #reinvitedUser to main team"(){
        when: '登录'
        Map params = [
                '_spring_security_remember_me': true,
                'j_password': reinvitedmainUser.password,
                'j_username': reinvitedmainUser.username
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

        when: '验证是否已经注册'
        resp = RsqRestUtil.get("${baseUrl}${path}v2/register/registerVerify"){
            header 'X-Requested-With', 'XMLHttpRequest'
            queryParams ([u: reinvitedUser.username])
        }

        then:
        println resp.body
        resp.status == 200
        resp.json.registed == verifiedResult

        when: '直接邀请用户'
        Map inviteParams = [
                account: reinvitedUser.username,
                deptId: 'unDept',
                password: reinvitedUser.password,
                realName: reinvitedUser.realName
        ]
        resp = RsqRestUtil.post("${baseUrl}${path}v2/invite/directInvite"){
            header 'content-type', 'application/x-www-form-urlencoded;charset=UTF-8'
            header 'X-Requested-With', 'XMLHttpRequest'
            fields inviteParams
        }

        then:
//        返回值：{"success":true,"inviteResult":[{"id":521898,"dateCreated":"2017-08-31 17:37:58","deleteUser":false,"hasAvatar":false,"username":"421503610@qq.com","t":"bd307b793ed6436595575ce949d3f6c3"}],"unconfirmed":1,"inviteSuccess":1,"inviteFailure":0}
        println "----resp.body:${resp.body}"
        resp.status == 200
        resp.json.success == true
        resp.json.inviteSuccess == 1

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

    @Unroll
    def "user #acceptReinviteUser accept invite and join the team #acceptReinvitedTeam"(){
        when: '登录'
        Map params = [
                '_spring_security_remember_me': true,
                'j_password': acceptReinviteUser.password,
                'j_username': acceptReinviteUser.username
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
        println "-----reinvite accept token is------${acceptReinviteUser.t}"
        params = [
                t: acceptReinviteUser.t
        ]
        resp = RsqRestUtil.post("${baseUrl}${path}v2/invite/inviteJoinInTeam"){
            header 'content-type', 'application/x-www-form-urlencoded;charset=UTF-8'
            header 'X-Requested-With', 'XMLHttpRequest'
            fields params
        }

        then:
        resp.status == 200

        when:
        resp = RsqRestUtil.post("${baseUrl}${path}login/authAjax"){
            header 'X-Requested-With', 'XMLHttpRequest'
        }

        then:
        resp.status == 200
        resp.json.id != null
        resp.json.team != null
        resp.json.team.name == acceptReinvitedTeam.name

        when:
        resp = RsqRestUtil.get("${baseUrl}${path}logout/index"){
            header 'X-Requested-With', 'XMLHttpRequest'
        }

        then:
        resp.status == 200

        where:
        acceptReinvitedTeam = userEnv.teamForCreate
        acceptReinviteUser << [
                userEnv.userForInvitePersonal,
                userEnv.userForInviteTeam
        ]
    }

    @Unroll
    def "user #deletedMainUser delete the team"(){
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
        params = [
                password: deletedMainUser.password
        ]
        resp = RsqRestUtil.post("${baseUrl}${path}team/deleteTeam"){
            header 'content-type', 'application/x-www-form-urlencoded;charset=UTF-8'
            header 'X-Requested-With', 'XMLHttpRequest'
            fields params
        }

        then:
        resp.status == 200

        when:
        resp = RsqRestUtil.post("${baseUrl}${path}login/authAjax"){
            header 'X-Requested-With', 'XMLHttpRequest'
        }

        then:
        resp.status == 200
        resp.json.id != null
        resp.json.team == null

        when:
        resp = RsqRestUtil.get("${baseUrl}${path}logout/index"){
            header 'X-Requested-With', 'XMLHttpRequest'
        }

        then:
        resp.status == 200

        where:
        deletedMainUser = userEnv.userForTeamCreate
    }

    @Unroll
    def "after team is deleted, original user #deletedOriginUser should auto quit the team"(){
        when: '登录'
        Map params = [
                '_spring_security_remember_me': true,
                'j_password': deletedOriginUser.password,
                'j_username': deletedOriginUser.username
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
        resp = RsqRestUtil.post("${baseUrl}${path}login/authAjax"){
            header 'X-Requested-With', 'XMLHttpRequest'
        }

        then:
        resp.status == 200
        resp.json.id != null
        resp.json.team == null

        when:
        resp = RsqRestUtil.get("${baseUrl}${path}"){
            header 'X-Requested-With', 'XMLHttpReqlogout/indexuest'
        }

        then:
        resp.status == 200

        where:
        deletedOriginUser << [
                userEnv.userForInvitePersonal,
                userEnv.userForInviteTeam
        ]
    }
}
