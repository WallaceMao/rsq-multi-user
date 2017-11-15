package com.rishiqing.test.functional.api;

import com.rishiqing.demo.util.http.RsqRestResponse;
import com.rishiqing.demo.util.http.RsqRestUtil;
import com.rishiqing.test.functional.ConfigUtil;

import java.util.Map;

public class ScheduleApi {
    private static String baseUrl = ConfigUtil.config.baseUrl
    private static String path = ConfigUtil.config.path

    public static RsqRestResponse createTodo(Map todoParams){
        RsqRestUtil.post("${baseUrl}${path}v2/todo/"){
            header 'X-Requested-With', 'XMLHttpRequest'
            fields todoParams
        }
    }
}
