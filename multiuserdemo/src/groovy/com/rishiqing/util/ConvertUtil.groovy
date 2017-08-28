package com.rishiqing.util

import groovy.json.JsonSlurper
import org.codehaus.groovy.grails.web.json.JSONObject

/**
 * Created by  on 2017/8/26.Wallace
 */
class ConvertUtil {
    static Map JSON2Map(JSONObject json){
        def jsonSlurper = new JsonSlurper()
        (Map)jsonSlurper.parseText(json.toString())
    }
}
