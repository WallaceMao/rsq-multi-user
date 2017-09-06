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

    RsqRestResponse logout(){
        RsqRestUtil.get("${baseUrl}${path}logout/index"){
            header 'X-Requested-With', 'XMLHttpRequest'
        }
    }
}
