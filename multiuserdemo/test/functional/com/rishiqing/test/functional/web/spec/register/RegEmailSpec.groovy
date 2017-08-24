package com.rishiqing.test.functional.web.spec.register

import com.rishiqing.test.functional.ConfigUtil
import com.rishiqing.test.functional.web.page.IndexPage
import com.rishiqing.test.functional.web.page.LoginAndRegisterPage
import com.rishiqing.test.functional.web.page.MainPage
import com.rishiqing.test.functional.web.util.SqlPrepare
import com.rishiqing.test.functional.web.util.SqlUtil
import geb.spock.GebSpec
import spock.lang.Ignore
import spock.lang.IgnoreRest
import spock.lang.Shared
import spock.lang.Stepwise


/**
 * 邮箱注册和登录
 * Created by  on 2017/8/16.Wallace
 */
//@Stepwise
@Ignore
class RegEmailSpec extends GebSpec {

    @Shared Map emailUser = ConfigUtil.config.users.emailUsers[0]

    def setupSpec(){
        //  使用SqlPrepare中的genCleanEmailUsers生成SQL语句，做账号清理
        if(ConfigUtil.config?.dataSource){
            SqlUtil.execute(SqlPrepare.genCleanEmailUsers(emailUser))
        }
    }
    def cleanupSpec(){}
    def setup(){
        browser.clearCookies()
    }
    def cleanup(){}

    /**
     * 使用邮箱注册新用户
     */
    def "test register new account with email"() {
        when: '首页点击注册'
        to IndexPage
        registerButton.click()

        then: '跳转到登录注册页面'
        at LoginAndRegisterPage
        waitFor {
            regPort.displayed == true
        }

        when: '输入邮箱并点击注册'
        regPort.$('input.popInput') << emailUser.email
        regPort.$('.btn').click()

        then: '显示完善信息'
        waitFor {
            regEmail.displayed == true
        }

        when:
        regEmail.$('.user .popInput') << emailUser.realName
        regEmail.$('.psw .popInput') << emailUser.password
        String info = "--------请在20秒内完成拼图验证--------"
        webJs.showTestInfo(info)
//        webJs.addAlert('#emailRegYzmContainer')
        println info

        then: '等待完成拼图验证码'
        waitFor(20, 0.5){
            regEmail.$('.errorPlace').classes().contains('blue')
        }

        when: '点击完成按钮'
        regEmail.$('.btn').click()

        then: '进入主页面，并等待引导mask出现'
        at MainPage
        waitFor(10, 0.5){
            userGuideMask.$('.guideButton').displayed == true
        }

        when: '退出登录'
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

    /**
     * 注册时邮箱已存在
     */
    def "test register new account which already exists"() {

        when: '首页点击注册'
        to IndexPage
        registerButton.click()

        then: '跳转到登录注册页面'
        at LoginAndRegisterPage
        waitFor {
            regPort.displayed == true
        }

        when: '输入邮箱并点击注册'
        regPort.$('input.popInput') << emailUser.email
        regPort.$('.btn').click()

        then: '显示账号已存在'
        waitFor {
            regPort.$('.errorPlace').text() == '账号已存在'
        }
    }
    /**
     * 使用新注册的邮箱登陆
     */
    def "test login with new registered account"(){
        when: '首页点击登录'
        to IndexPage
        loginButton.click()

        then: '显示登录框'
        at LoginAndRegisterPage
        waitFor {
            loginView.displayed == true
        }

        when: '输入用户名和密码'
        loginView.$('.user .popInput') << emailUser.email
        loginView.$('.psw .popInput') << emailUser.password
        loginView.$('.btn').click()

        then: '进入主页面'
        waitFor {
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
