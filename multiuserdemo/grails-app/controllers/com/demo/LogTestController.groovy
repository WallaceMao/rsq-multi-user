package com.demo

class LogTestController {

    def logError() {
        log.error("error test in logError in LogTestController...")
        render "logError success"
    }

    def logWarn(){
        log.warn("warn test in LogWarn in LogTestController...")
        render "logWarn success"
    }

    def logException(){
        log.error("rsq exception:", new RuntimeException("hello exception"))
        render "hello exception success"
    }
}
