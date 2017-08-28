package com.rishiqing.test.it.user

import com.rishiqing.domain.user.CommonUser
import com.rishiqing.domain.user.DemoUser
import com.rishiqing.domain.user.SuperUser
import grails.test.spock.IntegrationSpec
import spock.lang.IgnoreRest
import spock.lang.Shared

class UserBizServiceIntegrationSpec extends IntegrationSpec {

//    static transactional = false
    def userBizService

    @Shared CommonUser userWithoutSuperUser
    @Shared SuperUser superUser1
    @Shared CommonUser user1WithSuperUser1
    @Shared CommonUser user2WithSuperUser1

    def setup() {
        //  初始化环境
        userWithoutSuperUser = new CommonUser('user1@qq.com', 'user1', '用户1')
        userWithoutSuperUser.dateCreated = new Date()
        userWithoutSuperUser.save(flush: true)

        user1WithSuperUser1 = new CommonUser('user2@qq.com', 'user2', '用户2')
        user1WithSuperUser1.dateCreated = new Date()
        user1WithSuperUser1.save(flush: true)
        superUser1 = new SuperUser(user1WithSuperUser1, user1WithSuperUser1)
        superUser1.dateCreated = new Date()
        superUser1.lastUpdated = new Date()
        superUser1.save(flush: true)
        user1WithSuperUser1.superUser = superUser1
        superUser1.save(flush: true)

        user2WithSuperUser1 = new CommonUser('user3@qq.com', 'user3', '用户3')
        user2WithSuperUser1.dateCreated = new Date()
        user2WithSuperUser1.superUser = superUser1
        user2WithSuperUser1.save(flush: true)

    }

    def cleanup() {
    }

    @IgnoreRest
    void "test demoUser create"(){
        given:
//        DemoUser.metaClass.'static'.mapping = {
//            dateCreated defaultValue: new Date()
//            lastUpdated defaultValue: new Date()
//        }

        when:
        DemoUser user = userBizService.createDemoUser()

        then:
        user != null
    }

    void "test getSiblingUsers"() {
//        given:
//        setupData()

        when:
        def siblings = userBizService.getSiblingUsers(null)

        then:
        siblings == null

        when:
        siblings = userBizService.getSiblingUsers(user1WithSuperUser1)

        then:
        siblings.size() == 2
        siblings[0] == user1WithSuperUser1
        siblings[1] == user2WithSuperUser1
    }
}
