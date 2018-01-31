package com.rishiqing.test.functional.rest.team

import com.rishiqing.demo.util.http.RsqRestResponse
import com.rishiqing.demo.util.http.RsqRestUtil
import com.rishiqing.test.functional.BaseApi
import com.rishiqing.test.functional.ConfigUtil
import com.rishiqing.test.functional.api.AccountApi
import com.rishiqing.test.functional.api.CorpusApi
import com.rishiqing.test.functional.api.PlanApi
import com.rishiqing.test.functional.api.TeamApi
import com.rishiqing.test.functional.util.db.DomainUtil
import com.rishiqing.test.functional.util.db.SqlPrepare
import com.rishiqing.test.functional.util.db.SqlUtil
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Stepwise
import spock.lang.Unroll

/**
 * 多用户加入团队相关的测试用例
 * Created by  on 2017/9/12.Wallace
 */
@Stepwise
class TeamUserDataApiSpec extends BaseApi {
    @Shared def suiteEnv = ConfigUtil.config.suite.teamUserData

    def setupSpec(){
//        RsqRestUtil.config([proxy: ['127.0.0.1': 5555]])

//          使用SqlPrepare中的genCleanMultiUserUsers生成SQL语句，做账号清理
        if(ConfigUtil.config?.dataSource) {
            String str = SqlPrepare.genCleanMultiUserUsers([
                    domainRegexp: "@${suiteEnv.emailDomain}\$"
            ])
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
        registerUser.id = resp.jsonMap.id

        then: '验证注册结果'
        AccountApi.checkRegister(resp)

        where:
        registerUser << [
                suiteEnv.teamUser1,
                suiteEnv.teamUser2,
                suiteEnv.teamUser3
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
                suiteEnv.teamUser2
        ]
        envTeam << [
                suiteEnv.team1ForCreate,
                suiteEnv.team2ForCreate
        ]
    }

    /**
     * target：user-3
     * user-1：邀请user-3加入team-b1——》邀请成功
     * user-3：接受user-1的邀请加入team-b1——加入成功
     */
    def "user1 邀请 user3 加入 team1 --> user3 接受邀请并加入 team1"(){
        given:
        RsqRestResponse resp

        when: 'user1登录邀请user3加入team1'
        resp = TeamApi.loginAndDirectInviteAndJoinInTeam(
                suiteEnv.team1ForCreate as Map,
                suiteEnv.teamUser1 as Map,
                suiteEnv.teamUser3 as Map)
        then:
        TeamApi.checkSuccess(resp)

        when: '获取user3的公司'
        resp = AccountApi.loginAndFetchUserSiblings(suiteEnv.teamUser3 as Map)
        then:
        AccountApi.checkFetchUserSiblings(resp, [teamList: [suiteEnv.team1ForCreate], noTeamList: []])
    }

    /**
     * user3 在 team1 团队中添加了私人文集、私人计划、公司文集、公司计划
     * user3 然后退出team1
     * 检查user3 私人文集、私人计划可以带走，公司文集、公司计划不能带走
     */
    def "user3 在 team1 中添加私人和公司的文集、计划 --> user3 退出 team1 --> 检查user3" (){
        given:
        RsqRestResponse resp
        Map privateAttribute = [attribute: 'person']
        Map publicAttribute = [attribute: 'company']
        Map planPrivate
        Map planPublic
        Map corpusPrivate
        Map corpusPublic

        when: '登录'
        resp = AccountApi.login(suiteEnv.teamUser3 as Map)
        then:
        AccountApi.checkLogin(resp)

        when: '创建私人计划'
        Map planToCreate = DomainUtil.genRandomPlan(privateAttribute)
        resp = PlanApi.createPlan(planToCreate)
        planPrivate = resp.jsonMap
        then:
        PlanApi.checkCreatePlan(resp)
        when: '添加成员'
        PlanApi.addPlanMember([id: planPrivate.id, members: [
                suiteEnv.teamUser1,
                suiteEnv.teamUser3
        ]])
        then:
        PlanApi.checkAddPlanMember(resp, [userRolesLength: 2])

        when: '创建公司计划'
        planToCreate = DomainUtil.genRandomPlan(publicAttribute)
        resp = PlanApi.createPlan(planToCreate)
        planPublic = resp.jsonMap
        then:
        PlanApi.checkCreatePlan(resp)
        when: '添加成员'
        PlanApi.addPlanMember([id: planPublic.id, members: [
                suiteEnv.teamUser1,
                suiteEnv.teamUser3
        ]])
        then:
        PlanApi.checkAddPlanMember(resp, [userRolesLength: 2])

        when: '获取计划列表'
        resp = PlanApi.getPlanGroupList()
        then: '应该有2个计划，一个私人计划，一个公司计划'
        PlanApi.checkGetPlanGroupList(resp, [planListLength: 2])

        when: '创建私人文集'
        Map corpusToCreate = DomainUtil.genRandomCorpus(privateAttribute)
        resp = CorpusApi.createCorpus(corpusToCreate)
        corpusPrivate = resp.jsonMap
        then:
        CorpusApi.checkCreateCorpus(resp)
        when: '私人文集添加成员'
        CorpusApi.addCorpusMember([id: corpusPrivate.id, members: [
                suiteEnv.teamUser1,
                suiteEnv.teamUser3
        ]])
        then:
        CorpusApi.checkAddCorpusMember(resp, [userRolesLength: 2])

        when: '创建公司文集'
        corpusToCreate = DomainUtil.genRandomCorpus(publicAttribute)
        resp = CorpusApi.createCorpus(corpusToCreate)
        corpusPublic = resp.jsonMap
        then:
        CorpusApi.checkCreateCorpus(resp)
        when: '公司文集添加成员'
        CorpusApi.addCorpusMember([id: corpusPublic.id, members: [
                suiteEnv.teamUser1,
                suiteEnv.teamUser3
        ]])
        then:
        CorpusApi.checkAddCorpusMember(resp)

        when: '获取文集列表'
        resp = CorpusApi.getCorpusList()
        then: '应该有2个文集，一个私人文集，一个公司文集'
        CorpusApi.checkGetCorpusList(resp, [corpusListLength: 2])

        when: 'user3退出团队'
        resp = TeamApi.quitTeam()
        //  3秒时间用于异步执行
        Thread.sleep(3000)
        then:
        TeamApi.checkQuitTeam(resp)

        when: '获取计划列表'
        resp = PlanApi.getPlanGroupList()
        then: '退出团队后，计划应该只剩下1个'
        PlanApi.checkGetPlanGroupList(resp, [planListLength: 1])

        when: '获取文集列表'
        resp = CorpusApi.getCorpusList()
        then: '退出团队后，文集应该只剩下1个文集'
        CorpusApi.checkGetCorpusList(resp, [corpusListLength: 1])
    }

    /**
     * target：user-3
     * user-2：邀请user-3加入team-2——》邀请成功
     * user-3：接受user-2的邀请加入team-2——加入成功
     */
    @Ignore
    def "user2 邀请 user3 加入 team2 --> user3 接受邀请并加入 team2"(){
        given:
        RsqRestResponse resp

        when: 'user2登录邀请user3加入team2'
        resp = TeamApi.loginAndDirectInviteAndJoinInTeam(
                suiteEnv.team1ForCreate as Map,
                suiteEnv.teamUser2 as Map,
                suiteEnv.teamUser3 as Map)
        then:
        TeamApi.checkSuccess(resp)

        when: '获取user3的公司'
        resp = AccountApi.loginAndFetchUserSiblings(suiteEnv.teamUser3 as Map)
        then:
        AccountApi.checkFetchUserSiblings(resp, [teamList: [suiteEnv.team2ForCreate], noTeamList: []])
    }

    /**
     * 检查user3的私人文集、私人计划是否带到team2
     */
    @Ignore
    def "检查 user3 的私人文集、私人计划是否带到 team2"(){
        //  TODO
    }
}
