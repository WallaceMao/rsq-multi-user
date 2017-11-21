package com.rishiqing.test.functional.api

import com.rishiqing.demo.util.http.RsqRestResponse
import com.rishiqing.demo.util.http.RsqRestUtil
import com.rishiqing.test.functional.ConfigUtil

class AccountApi {
    private static String baseUrl = ConfigUtil.config.baseUrl
    private static String path = ConfigUtil.config.path

    public static RsqRestResponse login(Map userParams){
        String account = userParams.username ?: userParams.phone
        Map params = [
                '_spring_security_remember_me': true,
                'j_password': userParams.password,
                'j_username': account
        ]
        RsqRestUtil.post("${baseUrl}${path}j_spring_security_check"){
            header 'content-type', 'application/x-www-form-urlencoded;charset=UTF-8'
            header 'X-Requested-With', 'XMLHttpRequest'
            fields params
        }
    }
    public static void checkLogin(RsqRestResponse resp){
        assert resp.status == 200
        assert resp.json.success == true
    }

    public static RsqRestResponse loginAndCheck(Map userParams){
        RsqRestResponse resp = login(userParams)
        checkLogin(resp)
        userParams.id = resp.jsonMap.id
        resp
    }

    public static RsqRestResponse logout(){
        RsqRestUtil.get("${baseUrl}${path}logout/index"){
            header 'X-Requested-With', 'XMLHttpRequest'
        }
    }
    public static void checkLogout(RsqRestResponse resp){
        assert resp.status == 200
    }
    public static RsqRestResponse logoutAndCheck(){
        RsqRestResponse resp = logout()
        checkLogout(resp)
        resp
    }

    /**
     * 注册用户，忽略验证
     * @param params
     * @return
     */
    public static RsqRestResponse register(Map registerUser, Map team = null){
        Map params = [:]
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
        if(team != null){
            params.t = team.t
        }
        RsqRestResponse resp = RsqRestUtil.post("${baseUrl}${path}v2/register"){
            header 'content-type', 'application/x-www-form-urlencoded;charset=UTF-8'
            header 'X-Requested-With', 'XMLHttpRequest'
            fields params
        }
        resp
    }
    public static void checkRegister(RsqRestResponse resp){
        assert resp.status == 200
    }

    /**
     * 验证是否已经注册
     * @param userParam
     * @return
     */
    public static RsqRestResponse registerVerify(Map userParam){
        Map params
        if(userParam.username){
            params = [u: userParam.username]
        }else if(userParam.phone){
            params = [phone: userParam.phone]
        }
        RsqRestUtil.get("${baseUrl}${path}v2/register/registerVerify"){
            header 'X-Requested-With', 'XMLHttpRequest'
            queryParams params
        }
    }
    public static void checkRegisterVerify(RsqRestResponse resp, Map expect = [:]){
        assert resp.status == 200
        assert resp.json.registed == expect.registered
    }

    /**
     * 获取登录信息
     * @param userParams
     */
    public static RsqRestResponse fetchLoginInfo(){
        RsqRestUtil.post("${baseUrl}${path}login/authAjax"){
            header 'X-Requested-With', 'XMLHttpRequest'
        }
    }

    /**
     * 获取当前用户
     * @return
     */
    public static RsqRestResponse fetchMe(){
        RsqRestUtil.get("${baseUrl}${path}multiuser/fetchMe"){
            header 'X-Requested-With', 'XMLHttpRequest'
        }
    }
    public static void checkFetchMe(RsqRestResponse resp, Map expect = [:]){
        assert resp.status == 200
        if(expect){
            assert resp.json.result.team.name == expect.team.name
        }
    }

    public static final RsqRestResponse switchUser(Map targetUserParams){
        Map params
        if(targetUserParams.id){
            params = [
                    id: targetUserParams.id
            ]
        }else if(targetUserParams.username){
            params = [
                    username: targetUserParams.username
            ]
        }

        RsqRestUtil.post("${baseUrl}${path}multiuser/switchUser"){
            header 'X-Requested-With', 'XMLHttpRequest'
            fields params
        }
    }

    /**
     * 获取团队数量
     * @return
     */
    public static RsqRestResponse fetchUserSiblings(){
        RsqRestUtil.post("${baseUrl}${path}multiuser/fetchUserSiblings"){
            header 'X-Requested-With', 'XMLHttpRequest'
        }
    }
    public static void checkFetchUserSiblings(RsqRestResponse resp, Map expect = [:]){
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

    public static RsqRestResponse setMainUser(Map userMap){
        Map params = [
                userId: userMap.id
        ]
        RsqRestUtil.post("${baseUrl}${path}multiuser/setMainUser"){
            header 'X-Requested-With', 'XMLHttpRequest'
            fields params
        }
    }
    public static void checkSetMainUser(RsqRestResponse resp, Map expect = [:]){
        assert resp.status == 200
        assert resp.json.errcode == 0
        Map mainUser = resp.jsonMap.result
        if(expect){
            expect.userId == mainUser.id
        }
    }

    public static RsqRestResponse getMainUser(){
        RsqRestUtil.get("${baseUrl}${path}multiuser/getMainUser"){
            header 'X-Requested-With', 'XMLHttpRequest'
        }
    }
    public static void checkGetMainUser(RsqRestResponse resp, Map expect = [:]){
        assert resp.status == 200
        assert resp.json.errcode == 0
        Map mainUser = resp.jsonMap.result
        if(expect){
            expect.userId == mainUser.id
        }
    }

    public static RsqRestResponse loginAndFetchUserSiblings(Map loginUser){
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
    public static RsqRestResponse loginAndSetMainUser(Map loginUser, Map userMap){
        //  登录
        RsqRestResponse resp = login(loginUser)
        checkLogin(resp)
        //  获取兄弟用户数
        resp = setMainUser(userMap)
        //  注销
        def logoutResp = logout()
        checkLogout(logoutResp)

        resp
    }
}
