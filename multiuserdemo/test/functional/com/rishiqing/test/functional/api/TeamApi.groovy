package com.rishiqing.test.functional.api

import com.rishiqing.demo.util.http.RsqRestResponse
import com.rishiqing.demo.util.http.RsqRestUtil
import com.rishiqing.test.functional.ConfigUtil

class TeamApi {
    private static final String baseUrl = ConfigUtil.config.baseUrl
    private static final String path = ConfigUtil.config.path

    private TeamApi(){}

    /**
     * 创建团队
     * @param userParams
     * @param teamParams
     * @return
     */
    public static final RsqRestResponse createTeam(Map userParams, Map teamParams){
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
    public static final void checkCreateTeam(RsqRestResponse resp){
        assert resp.status == 200
//        assert resp.json.success == true
//        assert resp.json.team != null
    }

    public static final RsqRestResponse quitTeam(){
        RsqRestUtil.post("${baseUrl}${path}team/quit"){
            header 'X-Requested-With', 'XMLHttpRequest'
        }
    }
    public static void checkQuitTeam(RsqRestResponse resp){
        assert resp.status == 200
    }

    /**
     * 直接邀请
     * @param inviteUserParam
     * @return
     */
    public static final RsqRestResponse directInvite(Map inviteUserParam){
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
    public static final Map checkDirectInvite(RsqRestResponse resp, Map expect = [:]){
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
     * 批量邀请
     * @param inviteUserParam
     * @return
     */
    public static final RsqRestResponse batchInvite(List inviteUserList){
        List accounts = []
        inviteUserList.each {it ->
            accounts.add(it.username?:it.phone)
        }
        Map inviteParams = [
                accounts: accounts,
                deptId: 'unDept'
        ]
        RsqRestUtil.post("${baseUrl}${path}v2/invite/batchInvite"){
            header 'X-Requested-With', 'XMLHttpRequest'
            fields inviteParams
        }
    }
    public static final List checkBatchInvite(RsqRestResponse resp, Map expect = [:]){
        assert resp.status == 200
        assert resp.json.success == true
        assert resp.json.inviteSuccess == expect.inviteSuccess
        List result = []
        resp.jsonMap.inviteResult.each {it->
            result.add([t: it.t])
        }
        result
    }

    /**
     * 链接邀请
     * @return
     */
    public static final RsqRestResponse linkInvite(){
        Map inviteParams = [
                deptId: 'unDept'
        ]
        RsqRestResponse resp = RsqRestUtil.post("${baseUrl}${path}v2/invite/linkInvite"){
            header 'X-Requested-With', 'XMLHttpRequest'
            fields inviteParams
        }
        //  link "http://www.rishiqing.com/i?t=xxxxxxxx"类型
        String link = resp.json.link
        String token = link.split("\\?")[1].split("=")[1]
        resp.json["token"] = token
        resp.jsonMap.put("token", token)
        resp
    }

    /**
     * 登录后加入团队
     * @param invitedUserPam
     * @return
     */
    public static final RsqRestResponse joinInTeam(Map invitedUserPam){
        Map params = [
                t: invitedUserPam.t
        ]
        RsqRestUtil.post("${baseUrl}${path}v2/invite/inviteJoinInTeam"){
            header 'content-type', 'application/x-www-form-urlencoded;charset=UTF-8'
            header 'X-Requested-With', 'XMLHttpRequest'
            fields params
        }
    }
    public static final void checkJoinInTeam(RsqRestResponse resp, Map expect = [:]){
        assert resp.status == 200
        if(expect.team){
            assert resp.json.team != null
            assert resp.json.team.name == expect.team.name
        }
    }

    /**
     * 在系统外通过连接的方式加入团队
     * @param invitedUserPam
     * @return
     */
    public static final RsqRestResponse joinInTeamOutside(Map teamParams, Map invitedUserParams){
        Map params
        if(invitedUserParams.username){
            params = [
                    u: invitedUserParams.username,
                    p: invitedUserParams.password,
                    t: teamParams.t
            ]
        }else if(invitedUserParams.phone){
            params = [
                    phone: invitedUserParams.phone,
                    p: invitedUserParams.password,
                    t: teamParams.t
            ]
        }
        RsqRestUtil.post("${baseUrl}${path}v2/invite/inviteJoinTeam"){
            header 'content-type', 'application/x-www-form-urlencoded;charset=UTF-8'
            header 'X-Requested-With', 'XMLHttpRequest'
            fields params
        }
    }
    public static final void checkJoinInTeamOutside(RsqRestResponse resp){
        assert resp.status == 200
    }

    public static final RsqRestResponse deleteTeam(Map teamUserMap){
        Map params = [
                password: teamUserMap.password
        ]
        RsqRestUtil.post("${baseUrl}${path}team/deleteTeam"){
            header 'content-type', 'application/x-www-form-urlencoded;charset=UTF-8'
            header 'X-Requested-With', 'XMLHttpRequest'
            fields params
        }
    }
    public static final void checkDeleteTeam(RsqRestResponse resp){
        assert resp.status == 200
    }

    /**
     * 用来做通用的http 200 返回检查
     * @param resp
     */
    public static final void checkSuccess(RsqRestResponse resp){
        assert resp.status == 200
    }

    /**
     * teamMember是团队中的人，邀请别人加入团队
     * inviteParams是被邀请人的参数
     * @param teamMember
     * @param inviteParams
     * @return
     */
    public static final RsqRestResponse loginAndDirectInviteAndJoinInTeam(Map team, Map teamMember, Map inviteParams){
        //  邀请人登录
        RsqRestResponse resp = AccountApi.login(teamMember)
        AccountApi.checkLogin(resp)
        //  邀请人邀请别人
        resp = directInvite(inviteParams)
        Map result = checkDirectInvite(resp)
        //  保存邀请token
        inviteParams.t = result.t
        //  邀请人退出
        resp = AccountApi.logout()
        AccountApi.checkLogout(resp)

        //  受邀请人登录
        resp = AccountApi.login(inviteParams)
        AccountApi.checkLogin(resp)
        //  受邀请人接受邀请加入团队
        resp = joinInTeam(inviteParams)
        checkJoinInTeam(resp)
        //  受邀请人退出
        RsqRestResponse logoutResp = AccountApi.logout()
        AccountApi.checkLogout(logoutResp)

        resp
    }

    public static final RsqRestResponse loginAndBatchInviteAndJoinInTeam(Map team, Map teamMember, List invitedMembers){
        //  邀请人登录
        RsqRestResponse resp = AccountApi.login(teamMember)
        AccountApi.checkLogin(resp)
        //  邀请人邀请别人
        resp = batchInvite(invitedMembers)
        List inviteResultList = checkBatchInvite(resp, [inviteSuccess: invitedMembers.size()])
        //  保存邀请token
        invitedMembers.eachWithIndex {it, index ->
            it.t = inviteResultList[index].t
        }
        //  邀请人退出
        resp = AccountApi.logout()
        AccountApi.checkLogout(resp)

        invitedMembers.each {it ->
            //  受邀请人登录
            resp = AccountApi.login(it as Map)
            AccountApi.checkLogin(resp)
            //  受邀请人接受邀请加入团队
            resp = joinInTeam(it as Map)
            checkJoinInTeam(resp, team)
            //  受邀请人退出
            RsqRestResponse logoutResp = AccountApi.logout()
            AccountApi.checkLogout(logoutResp)
        }
        resp
    }

    public static final RsqRestResponse loginAndCreateTeam(Map loginUser, Map teamParams){
        //  登录
        RsqRestResponse resp = AccountApi.login(loginUser)
        AccountApi.checkLogin(resp)
        //  创建团队
        resp = createTeam(loginUser, teamParams)
        //  注销
        def logoutResp = AccountApi.logout()
        AccountApi.checkLogout(logoutResp)

        resp
    }

    public static final RsqRestResponse loginAndQuitTeam(Map loginUser){
        //  登录
        RsqRestResponse resp = AccountApi.login(loginUser)
        AccountApi.checkLogin(resp)
        //  获取兄弟用户数
        resp = quitTeam()
        //  注销
        def logoutResp = AccountApi.logout()
        AccountApi.checkLogout(logoutResp)

        resp
    }
}
