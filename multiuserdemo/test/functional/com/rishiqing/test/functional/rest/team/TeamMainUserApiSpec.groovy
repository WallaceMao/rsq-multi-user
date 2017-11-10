package com.rishiqing.test.functional.rest.team

import com.rishiqing.demo.util.http.RsqRestResponse
import com.rishiqing.demo.util.http.RsqRestUtil
import com.rishiqing.test.functional.ConfigUtil
import com.rishiqing.test.functional.api.AccountApi
import com.rishiqing.test.functional.api.TeamApi
import com.rishiqing.test.functional.util.db.SqlPrepare
import com.rishiqing.test.functional.util.db.SqlUtil
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Stepwise
import spock.lang.Unroll

/**
 * 主用户的设置与获取:
 * 1  获取主用户
 * 2  设置主用户
 * 3  登录后默认登录主用户
 * Created by  on 2017/9/6.Wallace
 */
@Stepwise
class TeamMainUserApiSpec extends BaseTeamApi {
    @Shared def suiteEnv = ConfigUtil.config.suite.teamMainUser

    def setupSpec(){
//        RsqRestUtil.config([proxy: ['127.0.0.1': 5555]])

//          使用SqlPrepare中的genCleanMultiUserUsers生成SQL语句，做账号清理
        if(ConfigUtil.config?.dataSource) {
            String str = SqlPrepare.genCleanMultiUserUsers([
                    domainRegexp: "@${suiteEnv.emailDomain}\$"
            ])
            println "clean database: ${str}"
            SqlUtil.execute(str)
        }
    }

    def setup(){
        RsqRestUtil.clearCookies()
    }

    @Unroll
    def "注册相关用户：#registerUser"(){
        when: '注册用户'
        RsqRestResponse resp = AccountApi.register(registerUser as Map)

        then: '验证注册结果'
        AccountApi.checkRegister(resp)

        where:
        registerUser << [
                suiteEnv.teamUser1,
                suiteEnv.teamUser2,
//                suiteEnv.teamUser3,
//                suiteEnv.teamUser4,
//                suiteEnv.teamUser5,
//                suiteEnv.teamUser6,
//                suiteEnv.teamUser7
        ]
    }
    /**
     * stepwise时暂停3000ms等待用户注册成功
     */
    def "等待3000ms，以便服务器生成默认的日程、计划、笔记等数据"(){
        when:
        Thread.sleep(3000)
        then:
        true
    }

    /**
     * user-b2：创建team-b2——》创建成功
     * user-b3：创建team-b3——》创建成功
     * user-b3：创建team-b4——》创建成功
     * user-b5：创建team-b5——》创建成功
     * user-b6：创建team-b6——》创建成功
     * user-b7：创建team-b7——》创建公司
     * user-b8：创建team-b8——》创建成功
     */
    @Unroll
    def "#envTeamCreator 创建 #envTeam"(){
        when: '用户登录'
        //  新注册的用户会有生成默认的日程、计划、笔记，这里加1秒的等待时间，保证生成完成
        RsqRestResponse resp = TeamApi.loginAndCreateTeam(envTeamCreator as Map, envTeam as Map)
        then: '验证登录'
        TeamApi.checkCreateTeam(resp)

        where:
        envTeamCreator << [
                suiteEnv.teamUser2,
//                suiteEnv.teamUser3,
//                suiteEnv.teamUser3,
//                suiteEnv.teamUser5,
//                suiteEnv.teamUser6,
//                suiteEnv.teamUser7,
//                suiteEnv.teamUser8
        ]
        envTeam << [
                suiteEnv.team2ForCreate,
//                suiteEnv.team3ForCreate,
//                suiteEnv.team4ForCreate,
//                suiteEnv.team5ForCreate,
//                suiteEnv.team6ForCreate,
//                suiteEnv.team7ForCreate,
//                suiteEnv.team8ForCreate
        ]
    }

    /**
     * user-c1：个人用户，只能设置当前用户为主用户
     */
    def "user1 is not team user and main user is user1"(){
        given:
        RsqRestResponse resp

        when:
        resp = AccountApi.loginAndCheck(suiteEnv.teamUser1 as Map)
        long userId = suiteEnv.teamUser1.id
        println "----id----${userId}"
        resp = AccountApi.loginAndSetMainUser(suiteEnv.teamUser1 as Map, [userId: userId])

        then:
        AccountApi.checkSetMainUser(resp, [userId: userId])
    }

    /**
     * user-c2：创建team-c2，只能设置当前用户为主用户
     */
    def "user2 create team2 -> main user can only set to user2"(){
        given:
        RsqRestResponse resp

        when:
        resp = AccountApi.loginAndCheck(suiteEnv.teamUser2 as Map)
        long userId = suiteEnv.teamUser2.id
        println "----id----${userId}"
        resp = AccountApi.loginAndSetMainUser(suiteEnv.teamUser2 as Map, [userId: userId])

        then:
        AccountApi.checkSetMainUser(resp, [userId: userId])
    }

    /**
     * target；user-c3
     * user-c3：创建team-c3——》创建成功
     * user-c3：创建team-c4——》创建成功
     * user-c3：设置team-c4为主team——》team-c4相关的user设置为主用户
     * user-c3登录默认进入team-c4
     */
    @Ignore
    def "user3 set team4 main user ->"(){}

    /**
     * target：user-c5
     * user-c5：创建team-c5——》创建成功
     * user-c6：创建team-c6——》创建成功
     * user-c6：邀请user-c5加入team-c6
     * user-c5：接受邀请加入team-c6
     * user-c5：设置team-c6为主team——》team-c6相关的user设置为主用户
     * user-c5登录默认进入team-c6
     */
    @Ignore
    def "user6 invite user5 to team6 -> user5 set team6 main team"(){}

    /**
     * user-c7：创建team-c7——》创建成功
     * user-c7：创建team-c8——》创建成功
     * user-c7：退出team-c8——》退出成功
     * user-c7：设置team-c8相关的user设置为主用户（是否可以设置？）
     * user-c7登录默认进入team-c8相关的user
     */
    @Ignore
    def "user7 quit team8 -> user7 set team8 user main team"(){}
}
