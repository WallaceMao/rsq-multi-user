package com.rishiqing.test.functional.rest.team

import com.rishiqing.demo.util.http.RsqRestResponse
import com.rishiqing.demo.util.http.RsqRestUtil
import com.rishiqing.test.functional.ConfigUtil
import com.rishiqing.test.functional.api.AccountApi
import com.rishiqing.test.functional.api.TeamApi
import com.rishiqing.test.functional.util.db.SqlPrepare
import com.rishiqing.test.functional.util.db.SqlUtil
import spock.lang.Shared
import spock.lang.Stepwise
import spock.lang.Unroll

/**
 * 多用户加入团队相关的测试用例
 * Created by  on 2017/9/12.Wallace
 */
@Stepwise
class TeamJoinMultiuserApiSpec extends BaseTeamApi {
    @Shared def suiteEnv = ConfigUtil.config.suite.teamJoinMultiUser

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
        RsqRestUtil.clearCookies();
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
                suiteEnv.teamUser3,
                suiteEnv.teamUser4,
                suiteEnv.teamUser5,
                suiteEnv.teamUser6,
                suiteEnv.teamUser7,
                suiteEnv.teamUser8,
                suiteEnv.teamUser9,
                suiteEnv.teamUser10,
                suiteEnv.teamUser11,
                suiteEnv.teamUser12,
                suiteEnv.teamUser13,
                suiteEnv.teamUser14
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
     * user-b1：创建team-b1——》创建成功
     * user-b2：创建team-b2——》创建成功
     * user-b3：创建team-b3——》创建成功
     * user-b4：创建team-b4——》创建成功
     * user-b6：创建team-b6——》创建成功
     * user-b7：创建team-b7——》创建公司
     * user-b8：创建team-b8——》创建成功
     * user-b9：创建team-b9——》创建成功
     * user-b11：创建team-b11——》创建成功
     * user-b13：创建team-b13——》创建成功
     */
    @Unroll
    def "#envTeamCreator 创建公司 #envTeam"(){
        when: '用户登录'
        //  新注册的用户会有生成默认的日程、计划、笔记，这里加1秒的等待时间，保证生成完成
        RsqRestResponse resp = TeamApi.loginAndCreateTeam(
                envTeamCreator as Map,
                envTeam as Map)
        then: '验证登录'
        TeamApi.checkCreateTeam(resp)

        where:
        envTeamCreator << [
                suiteEnv.teamUser1,
                suiteEnv.teamUser2,
                suiteEnv.teamUser3,
                suiteEnv.teamUser4,
                suiteEnv.teamUser6,
                suiteEnv.teamUser7,
                suiteEnv.teamUser8,
                suiteEnv.teamUser9,
                suiteEnv.teamUser11,
                suiteEnv.teamUser13
        ]
        envTeam << [
                suiteEnv.team1ForCreate,
                suiteEnv.team2ForCreate,
                suiteEnv.team3ForCreate,
                suiteEnv.team4ForCreate,
                suiteEnv.team6ForCreate,
                suiteEnv.team7ForCreate,
                suiteEnv.team8ForCreate,
                suiteEnv.team9ForCreate,
                suiteEnv.team11ForCreate,
                suiteEnv.team13ForCreate
        ]
    }

    /**
     * target：user-b2
     * user-b1：创建team-b1——》创建成功
     * user-b2：创建team-b2——》创建成功
     * user-b1：邀请user-b2加入team-b1——》邀请成功
     * user-b2：接受user-b1的邀请加入team-b1——加入成功
     * user-b2有两家公司，两个user
     */
    def "user1 邀请 user2 加入 team1 --> user2 接受邀请并加入 team1 --> user2 当前拥有2个公司"(){
        given:
        RsqRestResponse resp

        when: 'user1登录邀请user2加入team1'
        resp = TeamApi.loginAndDirectInviteAndJoinInTeam(
                suiteEnv.team1ForCreate as Map,
                suiteEnv.teamUser1 as Map,
                suiteEnv.teamUser2 as Map)
        then:
        TeamApi.checkSuccess(resp)

        when: '获取b2的公司'
        resp = AccountApi.loginAndFetchUserSiblings(suiteEnv.teamUser2 as Map)
        then:
        AccountApi.checkFetchUserSiblings(resp, [teamList: [suiteEnv.team2ForCreate, suiteEnv.team1ForCreate], noTeamList: []])
    }
    /**
     * target：user-b5
     * user-b3：创建team-b3——》创建成功
     * user-b4：创建team-b4——》创建成功
     * user-b5
     * user-b3：邀请user-b5加入team-b3——》邀请成功
     * user-b5：接受邀请加入team-b3——》接受成功
     * user-b4：邀请user-b5加入team-b4——》邀请成功
     * user-b5：接受邀请加入team-b3——》接受成功
     * user-b5有两家公司，两个user
     */
    def "user3 邀请 user5 加入 team3 --> user4 邀请 user5 加入 team4 --> user5 拥有2个公司"(){
        given:
        RsqRestResponse resp

        when: '用户3邀请用户5加入team3'
        resp = TeamApi.loginAndDirectInviteAndJoinInTeam(
                suiteEnv.team1ForCreate as Map,
                suiteEnv.teamUser3 as Map,
                suiteEnv.teamUser5 as Map)
        then:
        TeamApi.checkSuccess(resp)

        when: '用户4邀请用户5加入team3'
        resp = TeamApi.loginAndDirectInviteAndJoinInTeam(
                suiteEnv.team1ForCreate as Map,
                suiteEnv.teamUser4 as Map,
                suiteEnv.teamUser5 as Map)
        then:
        TeamApi.checkSuccess(resp)

        when: '获取user5的公司'
        resp = AccountApi.loginAndFetchUserSiblings(suiteEnv.teamUser5)
        then:
        AccountApi.checkFetchUserSiblings(resp, [teamList: [suiteEnv.team3ForCreate, suiteEnv.team4ForCreate], noTeamList: []])
    }
    /**
     * target：user-b6
     * user-b6：创建team-b6——》创建成功
     * user-b7：创建team-b7——》创建公司
     * user-b6：退出team-b6——》退出成功
     * user-b7：邀请user-b6加入team-b7——》邀请成功
     * user-b6：接受邀请加入team-b6——》接受成功
     * user-b6有一家公司，但是有两个user
     */
    def "user6 退出 team6 --> user7 邀请 user6 加入 team7 --> user6 加入 team7 --> user6 拥有1个公司2个账户"(){
        given:
        RsqRestResponse resp

        when:
        resp = TeamApi.loginAndQuitTeam(suiteEnv.teamUser6 as Map)
        then:
        TeamApi.checkQuitTeam(resp)

        when:
        resp = TeamApi.loginAndDirectInviteAndJoinInTeam(
                suiteEnv.team7ForCreate as Map,
                suiteEnv.teamUser7 as Map,
                suiteEnv.teamUser6 as Map)
        then:
        TeamApi.checkJoinInTeam(resp)

        when:
        resp = AccountApi.loginAndFetchUserSiblings(suiteEnv.teamUser6 as Map)
        then:
        AccountApi.checkFetchUserSiblings(resp, [teamList: [suiteEnv.team7ForCreate], noTeamList: [suiteEnv.teamUser6]])
    }
    /**
     * target：user-b10
     * user-b8：创建team-b8——》创建成功
     * user-b9：创建team-b9——》创建成功
     * user-b10
     * user-b8：邀请user-b10加入team-b8——》邀请成功
     * user-b10：接受邀请加入team-b8——》接受成功
     * user-b10：退出team-b8——退出成功
     * user-b9：邀请user-b10加入team-b9——》邀请成功
     * user-b10：接受邀请加入team-b9——》接受成功
     * user-b10有一家公司，但是有两个user
     */
    def "user8 邀请 user10 加入 team8 --> user10 退出 team8 --> user9 邀请 user10 加入 team9 -> user10 拥有1个公司2个账户"(){
        given:
        RsqRestResponse resp

        when: 'user8邀请user10加入team8'
        resp = TeamApi.loginAndDirectInviteAndJoinInTeam(
                suiteEnv.team8ForCreate as Map,
                suiteEnv.teamUser8 as Map,
                suiteEnv.teamUser10 as Map)
        then:
        TeamApi.checkJoinInTeam(resp)

        when: 'user10退出team8'
        resp = TeamApi.loginAndQuitTeam(suiteEnv.teamUser10 as Map)
        then:
        TeamApi.checkQuitTeam(resp)

        when: 'user9邀请user10加入team9'
        resp = TeamApi.loginAndDirectInviteAndJoinInTeam(
                suiteEnv.team9ForCreate as Map,
                suiteEnv.teamUser9 as Map,
                suiteEnv.teamUser10 as Map)
        then:
        TeamApi.checkJoinInTeam(resp)

        when: 'user10有1家公司，2个user'
        resp = AccountApi.loginAndFetchUserSiblings(suiteEnv.teamUser10 as Map)
        then:
        AccountApi.checkFetchUserSiblings(resp, [teamList: [suiteEnv.team9ForCreate], noTeamList: [suiteEnv.teamUser10]])
    }
    /**
     * target：user-b11
     * user-b11：创建team-b11——》创建成功
     * user-b11：邀请user-b12加入team-b11——》邀请成功
     * user-b12：接受邀请加入team-b11——》接受成功
     * user-b11：退出team-b11——》退出成功
     * user-b12：邀请user-b11加入team-b11——》邀请成功
     * user-b11：接受邀请加入team-b11——》接受成功
     * user-b11有一家公司，一个user
     */
    def "user11 邀请 user12 加入 team11 --> user11 退出 team11 --> user12 邀请 user11 加入 team11 -> user11 拥有1个公司1个账户"(){
        given:
        RsqRestResponse resp

        when: 'user11邀请user12加入team11'
        resp = TeamApi.loginAndDirectInviteAndJoinInTeam(
                suiteEnv.team11ForCreate as Map,
                suiteEnv.teamUser11 as Map,
                suiteEnv.teamUser12 as Map)
        then:
        TeamApi.checkJoinInTeam(resp)

        when: 'user11退出team11'
        resp = TeamApi.loginAndQuitTeam(suiteEnv.teamUser11 as Map)
        then:
        TeamApi.checkQuitTeam(resp)

        when: 'user12邀请user11重新加入team11'
        resp = TeamApi.loginAndDirectInviteAndJoinInTeam(
                suiteEnv.team11ForCreate as Map,
                suiteEnv.teamUser12 as Map,
                suiteEnv.teamUser11 as Map)
        then:
        TeamApi.checkJoinInTeam(resp)

        when: 'user11有1家公司，1个用户'
        resp = AccountApi.loginAndFetchUserSiblings(suiteEnv.teamUser11 as Map)
        then:
        AccountApi.checkFetchUserSiblings(resp, [teamList: [suiteEnv.team11ForCreate], noTeamList: []])
    }
    /**
     * target：b14
     * user-b13：创建team-b13——》创建成功
     * user-b14：为个人用户
     * user-b13：邀请user-b14加入team-b13——》邀请成功
     * user-b14：接受user-b13的邀请加入team-b13——加入成功
     * user-b14：退出team-b13——》退出成功
     * user-b13：再次邀请user-b14加入team-b13——》邀请成功
     * user-b14：再次接受user-b13的邀请加入team-b13——加入成功
     * user-b14有一家公司，一个user
     */
    def "user13 邀请 user14 加入 team13 --> user14 退出 team13 --> user13 邀请 user14 重新加入 team13  --> user14 拥有1个公司1个账户"(){
        given:
        RsqRestResponse resp

        when: 'user13邀请user14加入team13'
        resp = TeamApi.loginAndDirectInviteAndJoinInTeam(
                suiteEnv.team13ForCreate as Map,
                suiteEnv.teamUser13 as Map,
                suiteEnv.teamUser14 as Map)
        then:
        TeamApi.checkJoinInTeam(resp)

        when: 'user14退出user13'
        resp = TeamApi.loginAndQuitTeam(suiteEnv.teamUser14 as Map)
        then:
        TeamApi.checkQuitTeam(resp)

        when: 'user13再次邀请user14加入team13'
        resp = TeamApi.loginAndDirectInviteAndJoinInTeam(
                suiteEnv.team13ForCreate as Map,
                suiteEnv.teamUser13 as Map,
                suiteEnv.teamUser14 as Map)
        then:
        TeamApi.checkJoinInTeam(resp)

        when: 'user14有1个team，1个user'
        resp = AccountApi.loginAndFetchUserSiblings(suiteEnv.teamUser14 as Map)
        then:
        AccountApi.checkFetchUserSiblings(resp, [teamList: [suiteEnv.team13ForCreate], noTeamList: []])
    }
}
