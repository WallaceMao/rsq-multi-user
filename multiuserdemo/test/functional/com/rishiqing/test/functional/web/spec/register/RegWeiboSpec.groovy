package com.rishiqing.test.functional.web.spec.register

import com.rishiqing.test.functional.ConfigUtil
import com.rishiqing.test.functional.web.page.IndexPage
import com.rishiqing.test.functional.web.page.LoginAndRegisterPage
import com.rishiqing.test.functional.web.page.MainPage
import com.rishiqing.test.functional.web.util.WebJs
import geb.spock.GebSpec
import spock.lang.Shared

/**
 * 微博注册和登录
 * Created by  on 2017/8/16.Wallace
 */
//@Stepwise
class RegWeiboSpec extends GebSpec {

    @Shared Map weiboUser = ConfigUtil.config.users.weiboUsers[0] as Map

    def setupSpec(){
        //  使用SqlPrepare中的genCleanEmailUsers生成SQL语句，做账号清理
//        if(ConfigUtil.config?.dataSource){
//            SqlUtil.execute(SqlPrepare.genCleanWeiboUsers(weiboUser))
//        }
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
        withNewWindow({ weiboRegButton.click() }){
            String info = "--------请在30秒内使用用户-${weiboUser.realName}-登录微博--------"
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
     * 使用微博登录
     */
    def "test login with weibo"() {
        when: '首页点击注册'
        to IndexPage
        loginButton.click()

        then: '跳转到登录注册页面'
        at LoginAndRegisterPage
        waitFor {
            loginView.displayed == true
        }

        expect:
        withNewWindow({ weiboLoginButton.click() }){
            String info = "--------请在30秒内使用用户-${weiboUser.realName}-登录微博--------"
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
