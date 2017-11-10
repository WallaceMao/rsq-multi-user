package com.rishiqing.test.functional.browser.spec.register

import com.rishiqing.test.functional.ConfigUtil
import com.rishiqing.test.functional.browser.page.IndexPage
import com.rishiqing.test.functional.browser.page.LoginAndRegisterPage
import com.rishiqing.test.functional.browser.page.MainPage
import com.rishiqing.test.functional.util.db.SqlPrepare
import com.rishiqing.test.functional.util.db.SqlUtil
import com.rishiqing.test.functional.browser.util.WebJs
import geb.spock.GebSpec
import spock.lang.Shared

/**
 * 微信注册和登录
 * Created by  on 2017/8/16.Wallace
 */
//@Stepwise
class RegWeixinSpec extends GebSpec {

    @Shared Map weixinUser = ConfigUtil.config.users.weixinUsers[0] as Map

    def setupSpec(){
        //  使用SqlPrepare中的genCleanEmailUsers生成SQL语句，做账号清理
        if(ConfigUtil.config?.dataSource){
            SqlUtil.execute(SqlPrepare.genCleanWeixinUsers(weixinUser))
        }
    }
    def cleanupSpec(){}
    def setup(){
        browser.clearCookies()
    }
    def cleanup(){}

    /**
     * 使用微信注册新用户
     */
    def "test register new account with Weixin"() {
        when: '首页点击注册'
        to IndexPage
        registerButton.click()

        then: '跳转到登录注册页面'
        at LoginAndRegisterPage
        waitFor {
            regPort.displayed == true
        }

        expect:
        withNewWindow({ weixinRegButton.click() }){
            String info = "--------请在20秒内使用微信号-${weixinUser.realName}-扫描二维码--------"
            new WebJs(js).showTestInfo(info)
            println info
            waitFor(20, 0.5) {
                at MainPage
                userGuideMask.$('.guideButton').displayed == true
            }

            when: '点击引导页面的按钮'
            userGuideMask.$('.guideButton').click()

            then: '引导页面消失'
            waitFor {
                userGuideMask.displayed == false
            }

            when: '退出登录'
            logout()

            then: '返回首页'
            waitFor{
                at IndexPage
            }
        }
    }

    /**
     * 使用微信登录
     */
    def "test login with weixin"() {
        when: '首页点击注册'
        to IndexPage
        loginButton.click()

        then: '跳转到登录注册页面'
        at LoginAndRegisterPage
        waitFor {
            loginView.displayed == true
        }

        expect:
        withNewWindow({ weixinLoginButton.click() }){
            String info = "--------请在20秒内使用微信号-${weixinUser.realName}-扫描二维码--------"
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
