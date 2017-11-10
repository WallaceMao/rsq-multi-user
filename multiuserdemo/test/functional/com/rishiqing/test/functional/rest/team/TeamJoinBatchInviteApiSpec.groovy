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
 * 批量邀请测试用例
 * Created by  on 2017/9/4.Wallace
 */
@Stepwise
class TeamJoinBatchInviteApiSpec extends BaseApi {
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
    def "注册相关用户：#registerUser"(){
        when: '注册用户'
        RsqRestResponse resp = AccountApi.register(registerUser as Map)

        then: '验证注册结果'
        resp.status == 200

        where:
        registerUser << [
                suiteEnv.teamManager,
                suiteEnv.userWithEmailForBatchInviteRegistered,
                suiteEnv.userWithPhoneForBatchInviteRegistered
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

        when: '用户1创建团队1'
        Map managerMap = suiteEnv.teamManager
        Map teamMap = suiteEnv.teamForJoin
        resp = TeamApi.loginAndCreateTeam(managerMap, teamMap)
        then:
        TeamApi.checkCreateTeam(resp)
        AccountApi.logoutAndCheck()
    }

    @Unroll
    def "由 #mainBatchInviteUser 批量邀请 #batchInviteUser1 和 #batchInviteUser2 加入团队"(){
        expect:
        TeamApi.loginAndBatchInviteAndJoinInTeam(
                batchInviteLoginTeam as Map,
                mainBatchInviteUser as Map,
                [batchInviteUser1, batchInviteUser2]
        )

        where:
        mainBatchInviteUser = suiteEnv.teamManager
        batchInviteLoginTeam = suiteEnv.teamForJoin
        batchInviteUser1 = suiteEnv.userWithEmailForBatchInviteRegistered
        batchInviteUser2 = suiteEnv.userWithPhoneForBatchInviteRegistered
    }
}
