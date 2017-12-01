package com.rishiqing.demo.util.http

import com.mashape.unirest.http.HttpResponse
import groovy.json.JsonSlurper
import org.codehaus.groovy.grails.web.converters.ConverterUtil
import org.codehaus.groovy.grails.web.json.JSONElement
import org.codehaus.groovy.grails.web.json.JSONException
import org.codehaus.groovy.grails.web.json.JSONObject
import org.codehaus.groovy.grails.web.json.JSONArray

/**
 * Created by  on 2017/8/15.Wallace
 */
class RsqRestResponse {
    HttpResponse unirestResponse
    int status
    String statusText
    String body
    Map headers
    JSONElement json
    Map jsonMap

    RsqRestResponse(HttpResponse unirestResponse) {
        this.unirestResponse = unirestResponse
        this.status = unirestResponse.status
        this.statusText = unirestResponse.statusText
        this.body = unirestResponse.body
        this.headers = unirestResponse.headers
    }

    JSONElement getJson(){
        if(!json){
            try {
                this.json = new JSONObject(this.body)
            } catch (JSONException e){
                try {
                    this.json = new JSONArray(this.body)
                } catch (JSONException ex){
                    this.json = null
                }
            }
        }

        return this.json
    }

    Map getJsonMap(){
        JSONElement ele = this.getJson()
        if(ele == null){
            return null;
        }
        def jsonSlurper = new JsonSlurper()
        Object obj = jsonSlurper.parseText(ele.toString())
        if(obj instanceof List){
            this.jsonMap = [list: (List)obj]
        }else if(obj instanceof Map){
            this.jsonMap = (Map)obj
        }

        return this.jsonMap
    }
}
