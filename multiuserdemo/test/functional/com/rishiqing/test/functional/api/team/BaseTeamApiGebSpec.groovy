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
    void checkDirectInvite(RsqRestResponse resp, Map expect = [:]){
        assert resp.status == 200
        assert resp.json.success == true
        assert resp.json.inviteSuccess == expect.inviteSuccess ?: 1
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
    RsqRestResponse fetchTeams(){
        RsqRestUtil.post("${baseUrl}${path}multiuser/fetchUserSiblings"){
            header 'X-Requested-With', 'XMLHttpRequest'
        }
    }
    void checkFetchTeams(RsqRestResponse resp, Map expect = [:]){
        assert resp.status == 200
        assert resp.json.errcode == 0
        if(expect){
            //  只读取有团队的user
            List userList = resp.jsonMap.result
            List teamList = userList.grep { it.team != null }
            assert teamList.size() == expect.list.size()
            expect.list.eachWithIndex {team, index ->
                assert teamList[index].team.name == team.name
            }
        }
    }
}
