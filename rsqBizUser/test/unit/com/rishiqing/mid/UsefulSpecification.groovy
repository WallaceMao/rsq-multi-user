package com.rishiqing.mid

import spock.lang.Specification

/**
 * Created by  on 2017/8/3.Wallace
 */
abstract class UsefulSpecification extends Specification {
    def setupSpec(){
        beforeSetup()
    }
    def cleanupSpec(){}
    def setup(){
        beforeSetup()
    }
    def cleanup(){}

    /**
     * 扩展的方法，在setupSpec和setup之前均执行
     * @return
     */
    def beforeSetup(){}
}
