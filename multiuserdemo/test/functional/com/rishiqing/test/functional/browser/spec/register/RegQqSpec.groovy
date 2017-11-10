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
 * QQ注册和登录
 * Created by  on 2017/8/16.Wallace
 */
//@Stepwise
class RegQqSpec extends GebSpec {

    @Shared Map qqUser = ConfigUtil.config.users.qqUsers[0]

    def setupSpec(){
        //  使用SqlPrepare中的genCleanEmailUsers生成SQL语句，做账号清理
        if(ConfigUtil.config?.dataSource){
            SqlUtil.execute(SqlPrepare.genCleanQqUsers(qqUser))
        }
    }
    def cleanupSpec(){}
    def setup(){
        browser.clearCookies()
    }
    def cleanup(){}

    /**
     * 使用QQ注册新用户
     */
    def "test register new account with QQ"() {
        when: '首页点击注册'
        to IndexPage
        registerButton.click()

        then: '跳转到登录注册页面'
        at LoginAndRegisterPage
        waitFor {
            regPort.displayed == true
        }

        expect:
        withNewWindow({ qqRegButton.click() }){
            String info = "--------请在20秒内使用-${qqUser.realName}-进行QQ授权--------"
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
     * 使用QQ登录新用户
     */
    def "test login with QQ"() {
        when: '首页点击注册'
        to IndexPage
        loginButton.click()

        then: '跳转到登录注册页面'
        at LoginAndRegisterPage
        waitFor {
            loginView.displayed == true
        }

        expect:
        withNewWindow({ qqLoginButton.click() }){
            String info = "--------请在20秒内使用-${qqUser.realName}-进行QQ授权--------"
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
