package com.rishiqing.demo.util.http

/**
 * Created by  on 2017/8/15.Wallace
 */
class RsqRestRequest {
    def headerMap = [:]
    def bodyMap = [:]
    def cookies = []

    def header(p1, p2){
        headerMap[p1] = p2
    }

    def fields(Map params){
        bodyMap << params
//        if(params instanceof Map){
//            body = params.collect {
//                "$it.key=$it.value"
//            }.join("&")
//        }
    }

    def field(p1, p2){
        bodyMap[p1] = p2
    }

    def cookies(cookieList){
        cookies += cookieList
    }
}
