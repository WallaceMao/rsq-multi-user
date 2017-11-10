package com.rishiqing.test.functional.browser.page
/**
 * 登录之后的主页面
 * Created by  on 2017/8/16.Wallace
 */
class MainPage extends BasePage {
    static url = "/app/todo"
    static at = {
        title == "日事清"
        mainBody
        !loadingMask.displayed
    }
    static content = {
        //  body
        mainBody { $('body#mainBody') }
        //  加载页面
        loadingMask { $('#entrance-loading-animate') }
        //  用户引导页面
        userGuideMask { $('.userGuideMask') }
        //  顶部导航栏
        navBar(wait: true, required: true) { $('#navigator') }
        //  导航栏以下的显示区
        mainContent(wait: true, required: true) { $('#mainContent') }
        //  用户个人信息
        userSettingButton(wait: true, required: true) { $('.user-profile .dropdown-toggle') }
        //  退出按钮
        logoutButton(wait: true, required: true) { $('.userSettings li:last-child a.dropdown-item') }
    }

    def logout(){
        userSettingButton.click()
        waitFor{
            logoutButton.displayed
        }
        logoutButton.click()
    }
}
