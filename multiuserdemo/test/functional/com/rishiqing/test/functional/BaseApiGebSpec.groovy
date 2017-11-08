package com.rishiqing.test.functional

import com.rishiqing.demo.util.http.RsqRestResponse
import com.rishiqing.demo.util.http.RsqRestUtil
import geb.spock.GebSpec
import spock.lang.Shared

/**
 * Created by  on 2017/8/23.Wallace
 */
class BaseApiGebSpec extends GebSpec {
    @Shared String baseUrl
    @Shared String path

    def setupSpec(){

        baseUrl = ConfigUtil.config.baseUrl
        path = ConfigUtil.config.path

//        RsqRestUtil.config([proxy: ['127.0.0.1': 5555]])

    }

    RsqRestResponse login(Map userParams){
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
    void checkLogin(RsqRestResponse resp){
        assert resp.status == 200
        assert resp.json.success == true
    }

    RsqRestResponse loginAndCheck(Map userParams){
        RsqRestResponse resp = login(userParams)
        checkLogin(resp)
        userParams.id = resp.jsonMap.id
        resp
    }

    RsqRestResponse logout(){
        RsqRestUtil.get("${baseUrl}${path}logout/index"){
            header 'X-Requested-With', 'XMLHttpRequest'
        }
    }
    void checkLogout(RsqRestResponse resp){
        assert resp.status == 200
    }
    RsqRestResponse logoutAndCheck(){
        RsqRestResponse resp = logout()
        checkLogout(resp)
        resp
    }

    /**
     * 注册用户，忽略验证
     * @param params
     * @return
     */
    RsqRestResponse register(Map registerUser){
        Map params
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
        RsqRestResponse resp = RsqRestUtil.post("${baseUrl}${path}v2/register"){
            header 'content-type', 'application/x-www-form-urlencoded;charset=UTF-8'
            header 'X-Requested-With', 'XMLHttpRequest'
            fields params
        }
        resp
    }
    void checkRegister(RsqRestResponse resp){
        assert resp.status == 200
    }
}
