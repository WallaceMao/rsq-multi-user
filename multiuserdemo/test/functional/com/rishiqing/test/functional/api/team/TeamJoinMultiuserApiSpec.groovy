package com.rishiqing.test.functional.api.team

import com.rishiqing.demo.util.http.RsqRestResponse
import com.rishiqing.demo.util.http.RsqRestUtil
import com.rishiqing.test.functional.ConfigUtil
import com.rishiqing.test.functional.util.SqlPrepare
import com.rishiqing.test.functional.util.SqlUtil
import spock.lang.Ignore
import spock.lang.IgnoreRest
import spock.lang.Shared
import spock.lang.Stepwise
import spock.lang.Unroll

/**
 * 多用户加入团队相关的测试用例
 * Created by  on 2017/9/12.Wallace
 */
@Stepwise
class TeamJoinMultiuserApiSpec extends BaseTeamApiGebSpec {
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
    def "register relative users #registerUser"(){
        when: '注册用户'
        RsqRestResponse resp = register(registerUser)

        then: '验证注册结果'
        checkRegister(resp)

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
     */
    def "sleep for user register"(){
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
    def "#envTeamCreator create #envTeam"(){
        when: '用户登录'
        //  新注册的用户会有生成默认的日程、计划、笔记，这里加1秒的等待时间，保证生成完成
        RsqRestResponse resp = loginAndCreateTeam(envTeamCreator, envTeam)
        then: '验证登录'
        checkCreateTeam(resp)

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
    def "user1 invite user2 to join team1 --> user2 accept and join team1 --> user2 has 2 teams"(){
        given:
        RsqRestResponse resp

        when: 'user1登录邀请user2加入team1'
        resp = loginAndDirectInviteAndJoinInTeam(suiteEnv.teamUser1, suiteEnv.teamUser2)
        then:
        checkSuccess(resp)

        when: '获取b2的公司'
        resp = loginAndFetchUserSiblings(suiteEnv.teamUser2)
        then:
        checkFetchUserSiblings(resp, [teamList: [suiteEnv.team2ForCreate, suiteEnv.team1ForCreate], noTeamList: []])
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
    def "user3 invite user5 to team3 --> user4 invite user5 to team4 --> user5 has 2 teams"(){
        given:
        RsqRestResponse resp

        when: '用户3邀请用户5加入team3'
        resp = loginAndDirectInviteAndJoinInTeam(suiteEnv.teamUser3, suiteEnv.teamUser5)
        then:
        checkSuccess(resp)

        when: '用户4邀请用户5加入team3'
        resp = loginAndDirectInviteAndJoinInTeam(suiteEnv.teamUser4, suiteEnv.teamUser5)
        then:
        checkSuccess(resp)

        when: '获取user5的公司'
        resp = loginAndFetchUserSiblings(suiteEnv.teamUser5)
        then:
        checkFetchUserSiblings(resp, [teamList: [suiteEnv.team3ForCreate, suiteEnv.team4ForCreate], noTeamList: []])
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
    def "user6 quit team6 --> user7 invite user6 to team7 --> user6 join team7 --> user6 has 1 teams and 2 user"(){
        given:
        RsqRestResponse resp

        when:
        resp = loginAndQuitTeam(suiteEnv.teamUser6)
        then:
        checkQuitTeam(resp)

        when:
        resp = loginAndDirectInviteAndJoinInTeam(suiteEnv.teamUser7, suiteEnv.teamUser6)
        then:
        checkJoinInTeam(resp)

        when:
        resp = loginAndFetchUserSiblings(suiteEnv.teamUser6)
        then:
        checkFetchUserSiblings(resp, [teamList: [suiteEnv.team7ForCreate], noTeamList: [suiteEnv.teamUser6]])
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
    def "user8 invite user10 to team8 --> user10 quit team8 --> user9 invite user10 to team9 -> user10 has 1 teams and 2 user"(){
        given:
        RsqRestResponse resp

        when: 'user8邀请user10加入team8'
        resp = loginAndDirectInviteAndJoinInTeam(suiteEnv.teamUser8, suiteEnv.teamUser10)
        then:
        checkJoinInTeam(resp)

        when: 'user10退出team8'
        resp = loginAndQuitTeam(suiteEnv.teamUser10)
        then:
        checkQuitTeam(resp)

        when: 'user9邀请user10加入team9'
        resp = loginAndDirectInviteAndJoinInTeam(suiteEnv.teamUser9, suiteEnv.teamUser10)
        then:
        checkJoinInTeam(resp)

        when: 'user10有1家公司，2个user'
        resp = loginAndFetchUserSiblings(suiteEnv.teamUser10)
        then:
        checkFetchUserSiblings(resp, [teamList: [suiteEnv.team9ForCreate], noTeamList: [suiteEnv.teamUser10]])
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
    def "user11 invite user12 to team11 --> user11 quit team11 --> user12 invite user11 to team11 -> user11 has 1 team and 1 user"(){
        given:
        RsqRestResponse resp

        when: 'user11邀请user12加入team11'
        resp = loginAndDirectInviteAndJoinInTeam(suiteEnv.teamUser11, suiteEnv.teamUser12)
        then:
        checkJoinInTeam(resp)

        when: 'user11退出team11'
        resp = loginAndQuitTeam(suiteEnv.teamUser11)
        then:
        checkQuitTeam(resp)

        when: 'user12邀请user11重新加入team11'
        resp = loginAndDirectInviteAndJoinInTeam(suiteEnv.teamUser12, suiteEnv.teamUser11)
        then:
        checkJoinInTeam(resp)

        when: 'user11有1家公司，1个用户'
        resp = loginAndFetchUserSiblings(suiteEnv.teamUser11)
        then:
        checkFetchUserSiblings(resp, [teamList: [suiteEnv.team11ForCreate], noTeamList: []])
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
    def "user13 invite user14 to team13 --> user14 quit team13 --> user13 invite user14 to team13 again -> user14 has 1 team and 1 user"(){
        given:
        RsqRestResponse resp

        when: 'user13邀请user14加入team13'
        resp = loginAndDirectInviteAndJoinInTeam(suiteEnv.teamUser13, suiteEnv.teamUser14)
        then:
        checkJoinInTeam(resp)

        when: 'user14退出user13'
        resp = loginAndQuitTeam(suiteEnv.teamUser14)
        then:
        checkQuitTeam(resp)

        when: 'user13再次邀请user14加入team13'
        resp = loginAndDirectInviteAndJoinInTeam(suiteEnv.teamUser13, suiteEnv.teamUser14)
        then:
        checkJoinInTeam(resp)

        when: 'user14有1个team，1个user'
        resp = loginAndFetchUserSiblings(suiteEnv.teamUser14)
        then:
        checkFetchUserSiblings(resp, [teamList: [suiteEnv.team13ForCreate], noTeamList: []])
    }
}
