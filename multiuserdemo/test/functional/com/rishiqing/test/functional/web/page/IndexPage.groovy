package com.rishiqing.test.functional.web.page

/**
 * http://www.rishiqing.com首页
 * Created by  on 2017/8/16.Wallace
 */
class IndexPage extends BasePage {
    static url = "/"
    static at = {
        title == "日事清 -- 你理想的工作方式!"
        $("body.index")
    }
    static content = {
        //  右上角的登录按钮
        loginButton(wait: true, required: true) { $('a.login', title: '登录') }
        //  右上角的注册按钮
        registerButton(wait: true, required: true) { $('a.register', title: '注册') }
        //  立即使用按钮
        nowUseButton(wait: true, required: true) { $('a.enter', title: '立即使用') }
    }
}
