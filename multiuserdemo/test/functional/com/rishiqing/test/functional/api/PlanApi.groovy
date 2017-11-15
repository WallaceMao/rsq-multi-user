package com.rishiqing.test.functional.api

import com.rishiqing.demo.util.http.RsqRestResponse
import com.rishiqing.demo.util.http.RsqRestUtil
import com.rishiqing.test.functional.ConfigUtil;

public class PlanApi {
    private static String baseUrl = ConfigUtil.config.baseUrl
    private static String path = ConfigUtil.config.path

    public static RsqRestResponse createPlan(Map planParams){
        RsqRestUtil.post("${baseUrl}${path}v2/kanbans/"){
            header 'X-Requested-With', 'XMLHttpRequest'
            fields planParams
        }
    }
}
