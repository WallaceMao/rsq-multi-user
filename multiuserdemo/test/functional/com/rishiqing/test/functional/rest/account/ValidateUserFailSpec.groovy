package com.rishiqing.test.functional.rest.account

import com.rishiqing.demo.util.http.RsqRestResponse
import com.rishiqing.demo.util.http.RsqRestUtil
import com.rishiqing.test.functional.BaseApi
import spock.lang.Ignore
import spock.lang.Unroll

/**
 * Created by on 2017/8/15.Wallace
 */
@Ignore
@Deprecated
class ValidateUserFailSpec extends BaseApi {

    def setupSpec(){
//        RsqRestUtil.config([proxy: ['127.0.0.1': 5555]])
    }

    def setup(){
    }

    def cleanup(){}

    @Unroll
    def "test use wrong username and password should fail, username is #username, password is #password"(){
        when:
        Map params = [
                '_spring_security_remember_me': true,
                'j_password': password,
                'j_username': username
        ]
        RsqRestResponse resp = RsqRestUtil.post("${baseUrl}${path}j_spring_security_check"){
            header 'content-type', 'application/x-www-form-urlencoded;charset=UTF-8'
            header 'X-Requested-With', 'XMLHttpRequest'
            fields params
        }

        then:
        resp.status == 200
        resp.json.success == false

        where:
        username << ['zhaoyun@rishiqing.com', '13141234123']
        password << ['qqqqqq', '000000']
    }

    @Unroll
    def "auto login without or with wrong cookie:rishiqingRem should fail, cookie rishiqingRem is #cookieList"(){
        when:
        RsqRestResponse resp = RsqRestUtil.post("${baseUrl}${path}login/authAjax"){
            field 'a', 'b'  //这里不加field无法被postman proxy截获？很奇怪
            cookies cookieList
        }

        then:
        resp.status == 200
        resp.json.success == false
        resp.json.authenticate == false
        resp.json.loginStatus == 'fail'

        where:
        cookieList << [
                [],
                [[name: 'rishiqingRem', value: 'xxxxxxx_random_cookie', domain: 'beta.rishiqing.com', path: '/task']]
        ]
    }
}
