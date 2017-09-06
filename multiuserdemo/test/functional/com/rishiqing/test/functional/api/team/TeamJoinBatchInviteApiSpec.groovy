package com.rishiqing.test.functional.api.team

import com.rishiqing.demo.util.http.RsqRestResponse
import com.rishiqing.demo.util.http.RsqRestUtil
import com.rishiqing.test.functional.BaseApiGebSpec
import com.rishiqing.test.functional.ConfigUtil
import com.rishiqing.test.functional.util.SqlPrepare
import com.rishiqing.test.functional.util.SqlUtil
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Unroll

/**
 * 批量邀请测试用例
 * Created by  on 2017/9/4.Wallace
 */
class TeamJoinBatchInviteApiSpec extends BaseApiGebSpec {
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
    def "register relative users #registerUser"(){
        when: '注册用户'
        Map params
        if(registerUser.username){
            params = [
                    'username': registerUser.username,
                    'password': registerUser.password,
                    'realName': registerUser.realName,
                    'NECaptchaValidate': 'random_test_validate_code'
            ]
        }else if(registerUser.phone){
            params = [
                    'phone': registerUser.phone,
                    'password': registerUser.password,
                    'realName': registerUser.realName,
                    'valicode': 'random_validate_code'
            ]
        }
        RsqRestResponse resp = RsqRestUtil.post("${baseUrl}${path}v2/register"){
            header 'content-type', 'application/x-www-form-urlencoded;charset=UTF-8'
            header 'X-Requested-With', 'XMLHttpRequest'
            fields params
        }

        then: '验证注册结果'
        println resp.body
        resp.status == 200

        where:
        registerUser << [
                suiteEnv.teamManager,
                suiteEnv.userWithEmailForBatchInviteRegistered,
                suiteEnv.userWithPhoneForBatchInviteRegistered
        ]
    }

    @Unroll
    def "team manager #teamManager create the team #mainTeam"(){
        when: '登录'
        RsqRestResponse resp = login(teamManager)

        then: '验证登录'
        println resp.body
        resp.status == 200
        resp.json.success == true

        when: '创建团队'
        //  测试环境下手机号和验证码不做验证
        Map teamParams = [
                name: mainTeam.name,
                contacts: teamManager.realName,
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
        resp = logout()

        then:
        resp.status == 200

        where:
        teamManager << [suiteEnv.teamManager]
        mainTeam << [suiteEnv.teamForJoin]
    }

    @Unroll
    def "batch invite user #batchInviteUser1, #batchInviteUser2 by #mainBatchInviteUser"(){
        when: '登录'
        RsqRestResponse resp = login(mainBatchInviteUser)

        then: '验证登录'
        println resp.body
        resp.status == 200
        resp.json.success == true

        when: '批量邀请用户'
        Map inviteParams = [
                accounts: [batchInviteUser1.username, batchInviteUser2.phone],
                deptId: 'unDept'
        ]
        resp = RsqRestUtil.post("${baseUrl}${path}v2/invite/batchInvite"){
            header 'X-Requested-With', 'XMLHttpRequest'
            fields inviteParams
        }

        then:
//        返回值：{"success":true,"inviteResult":[{"id":521898,"dateCreated":"2017-08-31 17:37:58","deleteUser":false,"hasAvatar":false,"username":"421503610@qq.com","t":"bd307b793ed6436595575ce949d3f6c3"}],"unconfirmed":1,"inviteSuccess":1,"inviteFailure":0}
        println "----resp.body:${resp.body}"
        resp.status == 200
        resp.json.success == true
        resp.json.inviteSuccess == 2
        resp.jsonMap.inviteResult.size() == 2

        when:
        batchInviteUser1.t = resp.jsonMap.inviteResult[0].t
        batchInviteUser2.t = resp.jsonMap.inviteResult[1].t
        resp = logout()

        then:
        resp.status == 200

        where:
        mainBatchInviteUser = suiteEnv.teamManager
        batchInviteUser1 = suiteEnv.userWithEmailForBatchInviteRegistered
        batchInviteUser2 = suiteEnv.userWithPhoneForBatchInviteRegistered
    }

    @Unroll
    def "accept batch invited user #batchInviteLoginUser login and accept invite"(){
        when: '登录'
        RsqRestResponse resp = login(batchInviteLoginUser)

        then: '验证登录'
        println resp.body
        resp.status == 200
        resp.json.success == true

        when:
        println "-----accept token is------${batchInviteLoginUser.t}"
        Map params = [
                t: batchInviteLoginUser.t
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
        resp.json.team.name == batchInviteLoginTeam.name

        when:
        resp = logout()

        then:
        resp.status == 200

        where:
        batchInviteLoginTeam = suiteEnv.teamForJoin
        batchInviteLoginUser << [
                suiteEnv.userWithEmailForBatchInviteRegistered,
                suiteEnv.userWithPhoneForBatchInviteRegistered
        ]
    }
}
