package com.rishiqing.test.functional.browser.spec.register

import com.rishiqing.test.functional.ConfigUtil
import com.rishiqing.test.functional.browser.page.IndexPage
import com.rishiqing.test.functional.browser.page.LoginAndRegisterPage
import com.rishiqing.test.functional.browser.page.MainPage
import com.rishiqing.test.functional.util.db.SqlPrepare
import com.rishiqing.test.functional.util.db.SqlUtil
import com.rishiqing.test.functional.browser.util.WebJs
import geb.spock.GebSpec
import spock.lang.Ignore
import spock.lang.Shared

/**
 * 钉钉注册和登录
 * Created by  on 2017/8/16.Wallace
 */
//@Stepwise
@Ignore
//TODO 钉钉扫码登录有问题，回头再调试
class RegDingSpec extends GebSpec {

    @Shared Map dingUser = ConfigUtil.config.users.dingUsers[0] as Map

    def setupSpec(){
        //  使用SqlPrepare中的genCleanEmailUsers生成SQL语句，做账号清理
        if(ConfigUtil.config?.dataSource){
            SqlPrepare.genCleanDingUsers(dingUser).each { it ->
                SqlUtil.execute(it)
            }
        }
    }
    def cleanupSpec(){}
    def setup(){
        browser.clearCookies()
    }
    def cleanup(){}

    /**
     * 使用钉钉注册新用户
     */
    def "test register new account with dingding"() {
        when: '首页点击注册'
        to IndexPage
        registerButton.click()

        then: '跳转到登录注册页面'
        at LoginAndRegisterPage
        waitFor {
            regPort.displayed == true
        }

        expect:
        withNewWindow({ dingRegButton.click() }){
//            String info = "--------请在20秒内使用钉钉号-${dingUser.realName}-扫描二维码--------"
//            new WebJs(js).showTestInfo(info)
//            println info
            waitFor(20, 0.5) {
                $('.login_content').displayed == true
//                at MainPage
//                userGuideMask.$('.guideButton').displayed == true
            }
//
//            when: '点击引导页面的按钮'
//            userGuideMask.$('.guideButton').click()
//
//            then: '引导页面消失'
//            waitFor {
//                userGuideMask.displayed == false
//            }
//
//            when: '退出登录'
//            logout()
//
//            then: '返回首页'
//            waitFor{
//                at IndexPage
//            }
        }
    }

    /**
     * 使用钉钉登录
     */
    def "test login with ding"() {
        when: '首页点击注册'
        to IndexPage
        loginButton.click()

        then: '跳转到登录注册页面'
        at LoginAndRegisterPage
        waitFor {
            loginView.displayed == true
        }

        expect:
        withNewWindow({ dingLoginButton.click() }){
            String info = "--------请在20秒内使用钉钉号-${dingUser.realName}-扫描二维码--------"
            new WebJs(js).showTestInfo(info)
            println info
            waitFor(20, 0.5) {
                at MainPage
            }

            when: '退出登录'
            logout()

            then: '返回首页'
            waitFor{
                at IndexPage
            }
        }
    }
}
