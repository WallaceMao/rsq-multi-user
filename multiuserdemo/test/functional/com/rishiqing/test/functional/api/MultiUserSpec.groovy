package com.rishiqing.test.functional.api

import com.rishiqing.test.functional.ConfigUtil
import com.rishiqing.test.functional.util.SqlPrepare
import com.rishiqing.test.functional.util.SqlUtil
import com.rishiqing.test.functional.web.page.IndexPage
import com.rishiqing.test.functional.web.page.LoginAndRegisterPage
import com.rishiqing.test.functional.web.page.MainPage
import geb.spock.GebSpec
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Stepwise
import spock.lang.Unroll

/**
 * 多用户测试套件
 * Created by  on 2017/8/30.Wallace
 */
@Stepwise
@Ignore
class MultiUserSpec extends GebSpec {

    @Shared def userEnv = ConfigUtil.config.suite.multiuser

    def setupSpec(){
        //  使用SqlPrepare中的genCleanMultiUserUsers生成SQL语句，做账号清理
        if(ConfigUtil.config?.dataSource){
            SqlUtil.execute(SqlPrepare.genCleanMultiUserUsers([
                    domainRegexp: "@${ConfigUtil.config.global.testEmailDomain}\$"
            ]))
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
    @Unroll
    def "test register new account with email user #emailUser"() {
        when: '登录注册页面'
        go '/i?port=1'

        then: '注册框显示'
        at LoginAndRegisterPage
        waitFor {
            regPort.displayed == true
        }

        when: '输入邮箱并点击注册'
        regPort.$('input.popInput') << emailUser.username
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
        println "-------------start sleep---------"
        Thread.sleep(10000)
        println "------------end sleep--------"

        where:
        emailUser << [
                userEnv.userForPersonal,
                userEnv.userForTeamCreate,
                userEnv.userForInvitePersonal,
                userEnv.userForInviteTeam,
                userEnv.userForInviteNotRegistered
        ]
    }
}
