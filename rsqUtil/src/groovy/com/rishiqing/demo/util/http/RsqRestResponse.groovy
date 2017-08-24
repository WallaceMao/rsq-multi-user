package com.rishiqing.demo.util.http

import com.mashape.unirest.http.HttpResponse
import org.codehaus.groovy.grails.web.json.JSONException
import org.codehaus.groovy.grails.web.json.JSONObject

/**
 * Created by  on 2017/8/15.Wallace
 */
class RsqRestResponse {
    HttpResponse unirestResponse
    int status
    String statusText
    String body
    Map headers
    JSONObject json

    RsqRestResponse(HttpResponse unirestResponse) {
        this.unirestResponse = unirestResponse
        this.status = unirestResponse.status
        this.statusText = unirestResponse.statusText
        this.body = unirestResponse.body
        this.headers = unirestResponse.headers
    }

    JSONObject getJson(){
        if(!json){
            try {
                this.json = new JSONObject(this.body)
            } catch (JSONException e){
            }
        }

        return this.json
    }
}
