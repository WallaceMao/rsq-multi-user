package com.rishiqing.test.functional.browser.page
/**
 * 登录页面
 * Created by  on 2017/8/16.Wallace
 */
class LoginAndRegisterPage extends BasePage {
    static url = "/i"
    static at = {
        title == "日事清"
        $('div.mainContainer')
    }
    static content = {
        regPort { $('div#regPort') }  //注册统一入口
        regPhone { $('div#regPhone') }  //完善手机注册信息的入口
        regEmail { $('div#regEmail') }  //完善邮箱注册信息的入口
        loginView { $('div#loginView') }  //登录框
        avatarLogin { $('div#avatarLogin') }
        invitePort { $('div#invitePort') }
        inviteLogin { $('div#inviteLogin') }
        inviteRegPhone { $('div#inviteRegPhone') }
        inviteRegEmail { $('div#inviteRegEmail') }
        resetPort { $('div#resetPort') }
        emailSend { $('div#emailSend') }
        emailReset { $('div#emailReset') }
        phoneReset { $('div#phoneReset') }
        resetSuccess { $('div#resetSuccess') }
        resetFail { $('div#resetFail') }
        emailSuccess { $('div#emailSuccess') }
        emailFail { $('div#emailFail') }
        canNotInGroup { $('div#canNotInGroup') }
        errorView { $('div#errorView') }


        weixinRegButton { $('div#regPort i.iconWX') }
        qqRegButton { $('div#regPort i.iconQQ') }
        weiboRegButton { $('div#regPort i.iconWeiBo') }
        dingRegButton { $('div#regPort i.iconDingDing') }
        weixinLoginButton { $('div#loginView i.iconWX') }
        qqLoginButton { $('div#loginView i.iconQQ') }
        weiboLoginButton { $('div#loginView i.iconWeiBo') }
        dingLoginButton { $('div#loginView i.iconDingDing') }
    }
}
