package com.rishiqing.test.functional.rest.account

import com.rishiqing.demo.util.http.RsqRestResponse
import com.rishiqing.test.functional.BaseApi
import com.rishiqing.test.functional.ConfigUtil
import com.rishiqing.test.functional.api.AccountApi
import spock.lang.Shared
import spock.lang.Unroll

class LoginSpec extends BaseApi {
    @Shared def suiteEnv = ConfigUtil.config.suite.login

    def setupSpec(){
//        RsqRestUtil.config([proxy: ['127.0.0.1': 5555]])
    }

    def setup(){
    }

    def cleanup(){}

    @Unroll
    def "test use wrong username and password should fail, user is #loginUser"(){
        given:
        RsqRestResponse resp

        when:
        resp = AccountApi.login(loginUser as Map)

        then:
        resp.status == 200
        resp.jsonMap.success == loginResult
        AccountApi.logoutAndCheck()

        where:
        loginUser << [
//                suiteEnv.realAdmin,
                suiteEnv.fakeAdmin,
                suiteEnv.fakeAdmin2,
                suiteEnv.fakeCommonUser
        ]
        loginResult << [
//                true,
                false, false, false
        ]
    }
}
