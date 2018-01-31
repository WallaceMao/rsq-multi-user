package com.rishiqing.test.functional.util.common

class MapUtil {
    /**
     * 对于expectedMap中的所有值，查找是否在orgMap中有相同的值，暂不支持递归进行
     * @param orgMap
     * @param expectedMap
     * @return
     */
    public static Boolean compareMapValue(Map orgMap = [:], Map expectedMap = [:]){
        Boolean isSame = true
        expectedMap.each {k, v ->
            if(!v.equals(orgMap[k])){
                isSame = false
            }
        }
        isSame
    }
}
