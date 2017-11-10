package com.rishiqing.test.functional

import com.rishiqing.demo.util.http.RsqRestResponse
import com.rishiqing.demo.util.http.RsqRestUtil
import geb.spock.GebSpec
import spock.lang.Shared

/**
 * Created by  on 2017/8/23.Wallace
 */
class BaseApi extends GebSpec {
    @Shared String baseUrl
    @Shared String path

    def setupSpec(){

        baseUrl = ConfigUtil.config.baseUrl
        path = ConfigUtil.config.path

//        RsqRestUtil.config([proxy: ['127.0.0.1': 5555]])

    }

}
