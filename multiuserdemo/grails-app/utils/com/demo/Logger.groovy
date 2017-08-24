package com.demo

/**
 * Created by  on 2017/7/1.Wallace
 */
class Logger {
    public static void log(String msg, Date start = null){
        Date now = new Date()
        String cost = (start == null ? "" : "cost: ${now.getTime() - start.getTime()} ms")
        println "----${now.format('yyyy-MM-dd HH:mm:ss')}--${cost}--${msg}----"
    }
}
