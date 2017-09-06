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
 * 直接邀请测试用例
 * Created by  on 2017/9/4.Wallace
 */
class TeamJoinDirectInviteApiSpec extends BaseApiGebSpec {
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
                suiteEnv.userWithEmailForDirectInviteRegistered,
                suiteEnv.userWithPhoneForDirectInviteRegistered
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
    def "direct invite user #directInviteUser by #mainInviteUser"(){
        when: '登录'
        RsqRestResponse resp = login(mainInviteUser)

        then: '验证登录'
        println resp.body
        resp.status == 200
        resp.json.success == true

        when: '验证是否已经注册'
        Map params
        if(directInviteUser.username){
            params = [u: directInviteUser.username]
        }else if(directInviteUser.phone){
            params = [phone: directInviteUser.phone]
        }
        resp = RsqRestUtil.get("${baseUrl}${path}v2/register/registerVerify"){
            header 'X-Requested-With', 'XMLHttpRequest'
            queryParams params
        }

        then:
        println resp.body
        resp.status == 200
        resp.json.registed == verifiedResult

        when: '直接邀请用户'
        Map inviteParams = [
                account: directInviteUser.username?:directInviteUser.phone,
                deptId: 'unDept',
                password: directInviteUser.password,
                realName: directInviteUser.realName
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
            directInviteUser.t = resp.jsonMap.inviteResult[0].t
            println "----token is----${directInviteUser}"
        }
        resp = logout()

        then:
        resp.status == 200

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
        RsqRestResponse resp = login(directInvitedUnregisteredLoginUser)

        then: '验证登录'
        println resp.body
        resp.status == 200
        resp.json.success == true

        when: '验证登录后是否加入到团队'
        resp = RsqRestUtil.post("${baseUrl}${path}login/authAjax"){
            header 'X-Requested-With', 'XMLHttpRequest'
        }

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
    def "accept direct invite user #directInviteLoginUser login and accept invite"(){
        when: '登录'
        RsqRestResponse resp = login(directInviteLoginUser)

        then: '验证登录'
        println resp.body
        resp.status == 200
        resp.json.success == true

        when:
        println "-----accept token is------${directInviteLoginUser.t}"
        Map params = [
                t: directInviteLoginUser.t
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
        resp.json.team.name == directInviteLoginTeam.name

        when:
        resp = logout()

        then:
        resp.status == 200

        where:
        directInviteLoginTeam = suiteEnv.teamForJoin
        directInviteLoginUser << [
                suiteEnv.userWithEmailForDirectInviteRegistered,
                suiteEnv.userWithPhoneForDirectInviteRegistered
        ]
    }
}
