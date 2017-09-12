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
 * 链接邀请测试用例
 * Created by  on 2017/9/4.Wallace
 */
class TeamJoinLinkInviteApiSpec extends BaseApiGebSpec {
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
        RsqRestResponse resp = register(registerUser)

        then: '验证注册结果'
        println resp.body
        resp.status == 200

        where:
        registerUser << [
                suiteEnv.teamManager,
                suiteEnv.userWithEmailForJoinLinkRegistered,
                suiteEnv.userWithPhoneForJoinLinkRegistered,
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
    def "main user #mainLinkJoinUser login and generate team invite link #mainLinkJoinTeam"(){
        when: '登录'
        RsqRestResponse resp = login(mainLinkJoinUser)

        then: '验证登录'
        println resp.body
        resp.status == 200
        resp.json.success == true

        when: '批量邀请用户'
        Map inviteParams = [
                deptId: 'unDept'
        ]
        resp = RsqRestUtil.post("${baseUrl}${path}v2/invite/linkInvite"){
            header 'X-Requested-With', 'XMLHttpRequest'
            fields inviteParams
        }
        String link = resp.json.link
        String tk = link.split("\\?")[1].split("=")[1]
        println "----team token is $tk"

        then:
        //  link "http://www.rishiqing.com/i?t=xxxxxxxx"类型
        resp.status == 200

        when:
        mainLinkJoinTeam.t = tk
        resp = logout()

        then:
        resp.status == 200

        where:
        mainLinkJoinTeam = suiteEnv.teamForJoin
        mainLinkJoinUser = suiteEnv.teamManager
    }

    @Unroll
    def "unregistered user #unregisteredLinkJoinUser use the link to join team #unregisteredLinkJoinTeam"(){
        when: '注册用户'
        Map params
        if(unregisteredLinkJoinUser.username){
            params = [
                    'username': unregisteredLinkJoinUser.username,
                    'password': unregisteredLinkJoinUser.password,
                    'realName': unregisteredLinkJoinUser.realName,
                    'NECaptchaValidate': 'random_test_validate_code',
                    t: unregisteredLinkJoinTeam.t
            ]
        }else if(unregisteredLinkJoinUser.phone){
            params = [
                    'phone': unregisteredLinkJoinUser.phone,
                    'password': unregisteredLinkJoinUser.password,
                    'realName': unregisteredLinkJoinUser.realName,
                    'valicode': 'random_validate_code',
                    t: unregisteredLinkJoinTeam.t
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

        when:
        resp = RsqRestUtil.post("${baseUrl}${path}login/authAjax"){
            header 'X-Requested-With', 'XMLHttpRequest'
        }

        then:
        resp.status == 200
        resp.json.id != null
        resp.json.team != null
        resp.json.team.name == unregisteredLinkJoinTeam.name

        when:
        resp = logout()

        then:
        resp.status == 200

        where:
        unregisteredLinkJoinTeam = suiteEnv.teamForJoin
        unregisteredLinkJoinUser << [
                suiteEnv.userWithEmailForJoinLink,
                suiteEnv.userWithPhoneForJoinLink
        ]
    }

    @Unroll
    def "registered user #registeredLinkJoinUser use the link to join team #registeredLinkJoinTeam"(){
        when:
        println "-----accept team token is------${registeredLinkJoinTeam.t}"
        Map params
        if(registeredLinkJoinUser.username){
            params = [
                    u: registeredLinkJoinUser.username,
                    p: registeredLinkJoinUser.password,
                    t: registeredLinkJoinTeam.t
            ]
        }else if(registeredLinkJoinUser.phone){
            params = [
                    phone: registeredLinkJoinUser.phone,
                    p: registeredLinkJoinUser.password,
                    t: registeredLinkJoinTeam.t
            ]
        }
        RsqRestResponse resp = RsqRestUtil.post("${baseUrl}${path}v2/invite/inviteJoinTeam"){
            header 'content-type', 'application/x-www-form-urlencoded;charset=UTF-8'
            header 'X-Requested-With', 'XMLHttpRequest'
            fields params
        }

        then:
        println resp.body
        resp.status == 200

        when:
        resp = RsqRestUtil.post("${baseUrl}${path}login/authAjax"){
            header 'X-Requested-With', 'XMLHttpRequest'
        }

        then:
        resp.status == 200
        resp.json.id != null
        resp.json.team != null
        resp.json.team.name == registeredLinkJoinTeam.name

        when:
        resp = logout()

        then:
        resp.status == 200

        where:
        registeredLinkJoinTeam = suiteEnv.teamForJoin
        registeredLinkJoinUser << [
                suiteEnv.userWithEmailForJoinLinkRegistered,
                suiteEnv.userWithPhoneForJoinLinkRegistered
        ]
    }
}
