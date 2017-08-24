package com.rishiqing.demo.util.http

import com.mashape.unirest.http.HttpResponse
import com.mashape.unirest.http.Unirest
import com.mashape.unirest.request.HttpRequest
import com.mashape.unirest.request.HttpRequestWithBody
import groovy.json.JsonOutput
import org.apache.http.HttpHost
import org.apache.http.client.CookieStore
import org.apache.http.cookie.Cookie
import org.apache.http.impl.client.BasicCookieStore
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.impl.client.HttpClients
import org.apache.http.impl.client.LaxRedirectStrategy
import org.apache.http.impl.cookie.BasicClientCookie

/**
 * Created by  on 2017/8/22.Wallace
 */
class RsqRestUtil {
    static final String DEFAULT_CONTENT_TYPE = 'application/json'

    static CookieStore cookieStore

    static {
        config()
//        config([proxy: ['127.0.0.1': 5555]])
    }

    static void config(Map settings = [:]) {
        cookieStore = new BasicCookieStore()
        HttpClientBuilder builder = HttpClients.custom()
                .setRedirectStrategy(new LaxRedirectStrategy())
                .setDefaultCookieStore(cookieStore)

        if(settings.proxy){
            Map.Entry entry = settings.proxy.find()
            builder.setProxy(new HttpHost(String.valueOf(entry.key), (int)entry.value))
        }

        Unirest.setHttpClient(builder.build())
    }

    static RsqRestResponse get(String url, @DelegatesTo(RsqRestRequest) Closure closure){
        RsqRestRequest delegateRequest = new RsqRestRequest()
        def code = closure.rehydrate(delegateRequest, this, this)
        code.resolveStrategy = Closure.DELEGATE_ONLY
        code()

        doGet(url, delegateRequest)
    }


    static RsqRestResponse post(String url, @DelegatesTo(RsqRestRequest) Closure closure){
        RsqRestRequest delegateRequest = new RsqRestRequest()
        def code = closure.rehydrate(delegateRequest, this, this)
        code.resolveStrategy = Closure.DELEGATE_ONLY
        code()

        doPost(url, delegateRequest)
    }

    static RsqRestResponse postJSON(String url, @DelegatesTo(RsqRestRequest) Closure closure){
        RsqRestRequest delegateRequest = new RsqRestRequest()
        def code = closure.rehydrate(delegateRequest, this, this)
        code.resolveStrategy = Closure.DELEGATE_ONLY
        code()

        if(!delegateRequest.headerMap['content-type'] && !delegateRequest.headerMap['Content-Type']){
            delegateRequest.header('content-type', DEFAULT_CONTENT_TYPE)
        }

        return doPost(url, delegateRequest)
    }

    static private RsqRestResponse doGet(String url, RsqRestRequest delegateRequest){
        HttpRequest unirestReq = Unirest.get(url)

        handleHeader(unirestReq, delegateRequest)
        handleCookie(delegateRequest)

        HttpResponse<String> response = unirestReq.asString()
        println response.body

        new RsqRestResponse(response)
    }

    static private RsqRestResponse doPost(String url, RsqRestRequest delegateRequest){
        //  use Unirest to send request
        HttpRequestWithBody unirestReq = Unirest.post(url)

        handleHeader(unirestReq, delegateRequest)
        handleBody(unirestReq, delegateRequest)
        handleCookie(delegateRequest)

        HttpResponse<String> response = unirestReq.asString()

        new RsqRestResponse(response)
    }

    static private HttpRequest handleHeader(HttpRequest request, RsqRestRequest delegateRequest){
        delegateRequest.headerMap.each {key, value ->
            request = request.header(String.valueOf(key), String.valueOf(value))
        }
        return request
    }

    static private HttpRequestWithBody handleBody(HttpRequestWithBody request, RsqRestRequest delegateRequest){
        // 根据header判断body的格式，目前支持"application/x-www-form-urlencoded"和"application/json"两种，默认为"application/json"
        def map = delegateRequest.headerMap
        String headerValue = map['content-type'] ?: map['Content-Type'] ?: DEFAULT_CONTENT_TYPE

        if(headerValue.indexOf('application/x-www-form-urlencode') != -1){
            request.fields(delegateRequest.bodyMap)
        }else if(headerValue.indexOf('application/json') != -1){
            String str = JsonOutput.toJson(delegateRequest.bodyMap)
            request.body(str)
        }else{
            throw new RuntimeException("unsupported http Content-Type header")
        }
        return request
    }

    static private void handleCookie(RsqRestRequest delegateRequest){
        delegateRequest.cookies.each {it ->
            if(it instanceof Cookie){
                addCookie(it)
            }else if(it instanceof Map) {
                Cookie cookie = new BasicClientCookie("$it.name", "$it.value")
                cookie.domain = it.domain
                cookie.path = it.path
                addCookie(cookie)
            }else {
                throw new RuntimeException("cookie type not supported")
            }
        }
    }

    /**
     * 使用cookieStore来管理cookie，转发cookie相关的方法到cookieStore
     * @param cookie
     */
    static void addCookie(Cookie cookie) {
        cookieStore.addCookie(cookie)
    }

    /**
     * 使用cookieStore来管理cookie，转发cookie相关的方法到cookieStore
     * @return
     */
    static List<Cookie> getCookies() {
        return cookieStore.getCookies()
    }

    /**
     * 根据名称获取cookie
     * @param name
     * @return
     */
    static Cookie getCookieByName(String name) {
        return cookieStore.getCookies()?.find {it ->
            return it.name == name
        }
    }

    /**
     * 使用cookieStore来管理cookie，转发cookie相关的方法到cookieStore
     * @param date
     * @return
     */
    static boolean clearExpiredCookies(Date date) {
        return cookieStore.clearExpired(date)
    }

    /**
     * 使用cookieStore来管理cookie，转发cookie相关的方法到cookieStore
     */
    static void clearCookies() {
        cookieStore.clear()
    }
}
