package com.rishiqing.test.functional.api.team

import com.rishiqing.demo.util.http.RsqRestResponse
import com.rishiqing.demo.util.http.RsqRestUtil
import com.rishiqing.test.functional.BaseApiGebSpec

/**
 * Created by  on 2017/9/11.Wallace
 */
class BaseTeamApiGebSpec extends BaseApiGebSpec {
    /**
     * 创建团队
     * @param userParams
     * @param teamParams
     * @return
     */
    RsqRestResponse createTeam(Map userParams, Map teamParams){
        Map params = [
                name: teamParams.name,
                contacts: userParams.realName,
                phoneNumber: teamParams.phoneNumber,
                validate: '1577'
        ]
        RsqRestUtil.post("${baseUrl}${path}team/createTeam"){
            header 'X-Requested-With', 'XMLHttpRequest'
            fields params
        }
    }
    void checkCreateTeam(RsqRestResponse resp){
        assert resp.status == 200
        assert resp.json.success == true
        assert resp.json.team != null
    }

    RsqRestResponse quitTeam(){
        RsqRestUtil.post("${baseUrl}${path}team/quit"){
            header 'X-Requested-With', 'XMLHttpRequest'
        }
    }
    void checkQuitTeam(RsqRestResponse resp){
        assert resp.status == 200
    }

    /**
     * 直接邀请
     * @param inviteUserParam
     * @return
     */
    RsqRestResponse directInvite(Map inviteUserParam){
        Map inviteParams = [
                account: inviteUserParam.username?:inviteUserParam.phone,
                deptId: 'unDept',
                password: inviteUserParam.password,
                realName: inviteUserParam.realName
        ]
        RsqRestUtil.post("${baseUrl}${path}v2/invite/directInvite"){
            header 'content-type', 'application/x-www-form-urlencoded;charset=UTF-8'
            header 'X-Requested-With', 'XMLHttpRequest'
            fields inviteParams
        }
    }
    Map checkDirectInvite(RsqRestResponse resp, Map expect = [:]){
        assert resp.status == 200
        assert resp.json.success == true
        assert resp.json.inviteSuccess == expect.inviteSuccess ?: 1
        Map result = [t: 0]
        if(resp.jsonMap.inviteResult.size() != 0){
            result.t = resp.jsonMap.inviteResult[0].t
        }
        result
    }

    /**
     * 登录后加入团队
     * @param invitedUserPam
     * @return
     */
    RsqRestResponse joinInTeam(Map invitedUserPam){
        Map params = [
                t: invitedUserPam.t
        ]
        RsqRestUtil.post("${baseUrl}${path}v2/invite/inviteJoinInTeam"){
            header 'content-type', 'application/x-www-form-urlencoded;charset=UTF-8'
            header 'X-Requested-With', 'XMLHttpRequest'
            fields params
        }
    }
    void checkJoinInTeam(RsqRestResponse resp){
        assert resp.status == 200
    }

    /**
     * 获取当前用户
     * @return
     */
    RsqRestResponse fetchMe(){
        RsqRestUtil.get("${baseUrl}${path}multiuser/fetchMe"){
            header 'X-Requested-With', 'XMLHttpRequest'
        }
    }
    void checkFetchMe(RsqRestResponse resp, Map expect = [:]){
        assert resp.status == 200
        if(expect){
            assert resp.json.result.team.name == expect.team.name
        }
    }

    /**
     * 获取团队数量
     * @return
     */
    RsqRestResponse fetchUserSiblings(){
        RsqRestUtil.post("${baseUrl}${path}multiuser/fetchUserSiblings"){
            header 'X-Requested-With', 'XMLHttpRequest'
        }
    }
    void checkFetchUserSiblings(RsqRestResponse resp, Map expect = [:]){
        assert resp.status == 200
        assert resp.json.errcode == 0
        List userList = resp.jsonMap.result
        if(expect.teamList){
            //  只读取有团队的user
            List teamList = userList.grep { it.team != null }
            assert teamList.size() == expect.teamList.size()
            expect.teamList.eachWithIndex {team, index ->
                assert teamList[index].team.name == team.name
            }
        }
        if(expect.noTeamList){
            //  检查没有团队的用户
            List noTeamList = userList.grep { it.team == null }
            assert noTeamList.size() == expect.noTeamList.size()
            expect.noTeamList.eachWithIndex {user, index ->
            }
        }
    }

    /**
     * 用来做通用的http 200 返回检查
     * @param resp
     */
    void checkSuccess(RsqRestResponse resp){
        assert resp.status == 200
    }

    /**
     * teamMember是团队中的人，邀请别人加入团队
     * inviteParams是被邀请人的参数
     * @param teamMember
     * @param inviteParams
     * @return
     */
    RsqRestResponse loginAndDirectInviteAndJoinInTeam(Map teamMember, Map inviteParams){
        //  邀请人登录
        RsqRestResponse resp = login(teamMember)
        checkLogin(resp)
        //  邀请人邀请别人
        resp = directInvite(inviteParams)
        Map result = checkDirectInvite(resp)
        //  保存邀请token
        inviteParams.t = result.t
        //  邀请人退出
        resp = logout()
        checkLogout(resp)

        //  受邀请人登录
        resp = login(inviteParams)
        checkLogin(resp)
        //  受邀请人接受邀请加入团队
        resp = joinInTeam(inviteParams)
        checkJoinInTeam(resp)
        //  受邀请人退出
        RsqRestResponse logoutResp = logout()
        checkLogout(logoutResp)

        resp
    }

    RsqRestResponse loginAndFetchUserSiblings(Map loginUser){
        //  登录
        RsqRestResponse resp = login(loginUser)
        checkLogin(resp)
        //  获取兄弟用户数
        resp = fetchUserSiblings()
        //  注销
        def logoutResp = logout()
        checkLogout(logoutResp)

        resp
    }

    RsqRestResponse loginAndCreateTeam(Map loginUser, Map teamParams){
        //  登录
        RsqRestResponse resp = login(loginUser)
        checkLogin(resp)
        //  创建团队
        resp = createTeam(loginUser, teamParams)
        //  注销
        def logoutResp = logout()
        checkLogout(logoutResp)

        resp
    }

    RsqRestResponse loginAndQuitTeam(Map loginUser){
        //  登录
        RsqRestResponse resp = login(loginUser)
        checkLogin(resp)
        //  获取兄弟用户数
        resp = quitTeam()
        //  注销
        def logoutResp = logout()
        checkLogout(logoutResp)

        resp
    }
}
