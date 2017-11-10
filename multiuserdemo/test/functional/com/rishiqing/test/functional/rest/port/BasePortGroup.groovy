package com.rishiqing.test.functional.rest.port

import com.rishiqing.demo.util.http.RsqRestResponse
import com.rishiqing.demo.util.http.RsqRestUtil
import com.rishiqing.test.functional.ConfigUtil

/**
 * Created by  on 2017/9/13.Wallace
 */
class BasePortGroup {
    static String baseUrl = ConfigUtil.config.baseUrl
    static String path = ConfigUtil.config.path

    static RsqRestResponse login(Map userParams){
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
    static void checkLogin(RsqRestResponse resp){
        assert resp.status == 200
        assert resp.json.success == true
    }

    static RsqRestResponse logout(){
        RsqRestUtil.get("${baseUrl}${path}logout/index"){
            header 'X-Requested-With', 'XMLHttpRequest'
        }
    }
    static void checkLogout(RsqRestResponse resp){
        assert resp.status == 200
    }

    /**
     * 注册用户，忽略验证
     * @param params
     * @return
     */
    static RsqRestResponse register(Map registerUser){
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
    static void checkRegister(RsqRestResponse resp){
        assert resp.status == 200
    }
}
