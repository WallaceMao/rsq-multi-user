package com.demo

import com.rishiqing.domain.user.User
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(UserInfoController)
class UserInfoControllerSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "test something"() {
    }

    void "test DemoUser concurrent save"(){
        String username1 = "user1",
            password1 = "user1password",
            realName1 = "用户1",
            username2 = "user2",
            password2 = "user2password",
            realName2 = "用户2";

        User u = new User(username1, password1, realName1)
        if(!u.save(flush: true)){
            println u.errors
        }
    }
}
