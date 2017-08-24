package com.rishiqing.mid.user

import com.rishiqing.domain.user.CommonUser
import com.rishiqing.domain.user.DemoUser
import com.rishiqing.domain.user.Role
import com.rishiqing.domain.user.SuperUser
import com.rishiqing.domain.user.Team
import com.rishiqing.domain.user.User
import com.rishiqing.mid.UsefulSpecification
import com.rishiqing.mid.dao.DaoUserService
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import grails.test.runtime.FreshRuntime
import spock.lang.Ignore
import spock.lang.IgnoreRest
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@FreshRuntime
@TestFor(MidUserService)
@Mock([User, CommonUser, SuperUser, Team, Role, DemoUser])
class MidUserServiceSpec extends UsefulSpecification {

    //  for getters
    @Shared CommonUser userWithoutSuperUser
    @Shared SuperUser superUser1
    @Shared CommonUser user1WithSuperUser1
    @Shared CommonUser user2WithSuperUser1

    @Shared def daoUserService



    /**
     * 由于这个原因：
     * The GORM implementation in DomainClassUnitTestMixin is using a simple in-memory ConcurrentHashMap implementation
     * @see <a href="http://docs.grails.org/2.5.4/guide/single.html#unitTestingDomains">http://docs.grails.org/2.5.4/guide/single.html#unitTestingDomains</a>
     */
//    def addCommonUserDefaultValue(CommonUser commonUser){
//        commonUser.loginCount = 0
//        commonUser.enabled = true
//    }

    def setupSpec(){

    }

    def cleanupSpec(){}

    def setup() {}

    def cleanup() {}

    def beforeSetup(){
        //  添加一些辅助的方法，用来判断Object是否相等
//        CommonUser.metaClass.equals = { obj->
//            //  添加equals方法，简单做，先只判断id
//            return obj.id == delegate.id
//        }

        //  初始化环境
        userWithoutSuperUser = new CommonUser('user1@qq.com', 'user1', 'ffffff')

        user1WithSuperUser1 = new CommonUser('user2@qq.com', 'user2', '用户2')
        superUser1 = new SuperUser(user1WithSuperUser1, user1WithSuperUser1)
        user1WithSuperUser1.superUser = superUser1

        user2WithSuperUser1 = new CommonUser('user3@qq.com', 'user3', '用户3')
        user2WithSuperUser1.superUser = superUser1

        mockDomain(CommonUser, [
                userWithoutSuperUser,
                user1WithSuperUser1,
                user2WithSuperUser1
        ])
        mockDomain(SuperUser, [
                superUser1
        ])

//        daoUserServiceMock = mockFor(DaoUserService)
//        daoUserServiceMock.demand.userCheckSuperUser { user -> return "hhhhhhh"}
//        daoUserService = daoUserServiceMock.createMock()
    }

    void "getSiblingUsers should get users with same superUser"() {

//        given:
//        def userSimulationMock = mockFor(UserSimulationService)
//        userSimulationMock.demand.getScene {-> return "hhhhhhh"}
//        def userSimulationService = userSimulationMock.createMock()
//
//        println "scene: ${userSimulationService.getScene()}"

//        println "-----userWithoutSuperUser:${userWithoutSuperUser}"

        when:
        def userList = service.getSiblingUsers(null)

        then:
        userList == null

        when:
        userList = service.getSiblingUsers(userWithoutSuperUser)

        then:
        userList.size() == 1
        userList[0].id == userWithoutSuperUser.id

        when:
        userList = service.getSiblingUsers(user1WithSuperUser1)

        then:
        userList.size() == 2
        userList[0].id == user1WithSuperUser1.id
        userList[1].id == user2WithSuperUser1.id
    }

    void "getParentSuperUser should get superUser"() {
        when:
        def parent = service.getParentSuperUser(null)

        then:
        parent == null

        when:
        parent = service.getParentSuperUser(userWithoutSuperUser)

        then:
        parent == null

        when:
        parent = service.getParentSuperUser(user1WithSuperUser1)

        then:
        parent.id == superUser1.id
    }

    void "getChildrenUsers should get children users of the superUser"() {
        when:
        def userList = service.getChildrenUsers(null)

        then:
        userList == null

        when:
        userList = service.getChildrenUsers(superUser1)

        then:
        userList.size() == 2
        userList[0].id == user1WithSuperUser1.id
        userList[1].id == user2WithSuperUser1.id
    }

    //  只做集成测试，暂时不做单元测试
    @Ignore
    void "createTeam should create a new Team"(){}

    @Ignore
    void "quitTeam should quit Team"(){}

    @Ignore
    void "dismissTeam should dismiss the Team"(){}

    @Ignore
    void "setMain should set the mainUser "(){}

}
