package com.rishiqing.test.functional.web.page

import com.rishiqing.test.functional.web.util.WebJs
import geb.Page

/**
 * Created by  on 2017/8/17.Wallace
 */
class BasePage extends Page {
    /**
     * 继承自BasePage的所有page，都可以直接使用webJs对象，来通过js，操作页面上的元素
     * @return
     */
    def getWebJs(){
        new WebJs(js)
    }
}
