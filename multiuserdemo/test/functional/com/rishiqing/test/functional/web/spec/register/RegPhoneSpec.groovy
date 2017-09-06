package com.rishiqing.test.functional.web.spec.register

import com.rishiqing.test.functional.ConfigUtil
import com.rishiqing.test.functional.web.page.IndexPage
import com.rishiqing.test.functional.web.page.LoginAndRegisterPage
import com.rishiqing.test.functional.web.page.MainPage
import com.rishiqing.test.functional.util.SqlPrepare
import com.rishiqing.test.functional.util.SqlUtil
import geb.spock.GebSpec
import spock.lang.Ignore
import spock.lang.Shared

/**
 * 手机号注册和登录
 * Created by  on 2017/8/16.Wallace
 */
@Ignore
class RegPhoneSpec extends GebSpec {

    @Shared Map phoneUser = ConfigUtil.config.users.phoneUsers[0] as Map

    def setupSpec(){
        //  使用SqlPrepare中的genCleanphoneUsers生成SQL语句，做账号清理
        if(ConfigUtil.config?.dataSource){
            SqlUtil.execute(SqlPrepare.genCleanPhoneUsers(phoneUser))
        }
    }
    def cleanupSpec(){}
    def setup(){
        browser.clearCookies()
    }
    def cleanup(){}
    /**
     * 使用手机号注册新用户
     */
    def "test register new account with phone number"() {
        when: '首页点击注册'
        to IndexPage
        registerButton.click()

        then: '跳转到登录注册页面'
        at LoginAndRegisterPage
        waitFor {
            regPort.displayed == true
        }

        when: '输入邮箱并点击注册'
        regPort.$('input.popInput') << phoneUser.phoneNumber
        regPort.$('.btn').click()

        then: '显示完善信息'
        waitFor {
            regPhone.displayed == true
        }

        when: '输入用户名和密码，并等待用户输入手机验证码，同时由测试人员点击“完成”按钮'
        regPhone.$('.user .popInput') << phoneUser.realName
        regPhone.$('.psw .popInput') << phoneUser.password
        String info = "--------请在30秒内输入手机验证码，并点击“完成”按钮--------"
        webJs.showTestInfo(info)
//        webJs.addAlert('#emailRegYzmContainer')
        println info

        then: '等待进入主页面，同时显示出引导页面'
        waitFor(30, 0.5){
            at MainPage
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
     * 注册时手机已存在
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
        regPort.$('input.popInput') << phoneUser.phoneNumber
        regPort.$('.btn').click()

        then: '显示账号已存在'
        waitFor {
            regPort.$('.errorPlace').text() == '手机号已存在'
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
        loginView.$('.user .popInput') << phoneUser.phoneNumber
        loginView.$('.psw .popInput') << phoneUser.password
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
