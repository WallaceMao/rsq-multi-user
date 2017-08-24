import com.rishiqing.domain.user.Requestmap
import com.rishiqing.domain.user.CommonUser
import com.rishiqing.domain.user.SystemUser
import com.rishiqing.domain.user.User
import com.rishiqing.domain.user.Role
import com.rishiqing.domain.user.UserRole

class BootStrap {

    def init = { servletContext ->
//        Role adminRole = new Role('ROLE_ADMIN')
//        adminRole.save(flush: true)
//
//        Role userRole = new Role('ROLE_USER')
//        userRole.save(flush: true)
//
//        SystemUser adminUser = new SystemUser('admin', 'admin')
//        adminUser.save(flush: true)
//        CommonUser commonUser = new CommonUser('user@qq.com', 'user', "用户1")
//        if(!commonUser.save(flush: true)){
//            println commonUser.errors
//        }
//        CommonUser commonUser2 = new CommonUser('user2@qq.com', 'user2', "用户2")
//        commonUser2.save(flush: true)
//        CommonUser commonUser3 = new CommonUser('user3@qq.com', 'user3', "用户3")
//        commonUser3.save(flush: true)
//
//        UserRole.create (adminUser, adminRole, true)
//        UserRole.create(commonUser, userRole, true)
//        UserRole.create(commonUser2, userRole, true)
//        UserRole.create(commonUser3, userRole, true)
//
//        assert User.count() == 4
//        assert Role.count() == 2
//        assert UserRole.count() == 4

//        for (String url in [
//                '/', '/index', '/index.gsp', '/**/favicon.ico',
//                '/assets/**', '/**/js/**', '/**/css/**', '/**/images/**',
//                '/login', '/login.*', '/login/*',
//                '/logout', '/logout.*', '/logout/*']) {
//            new Requestmap(url: url, configAttribute: 'permitAll').save()
//        }
//        new Requestmap(url: '/requestmap/**',    configAttribute: 'permitAll').save()
//
//        new Requestmap(url: '/userInfo/**',    configAttribute: 'ROLE_USER').save()
//        new Requestmap(url: '/profile/**',    configAttribute: 'ROLE_USER').save()
//        new Requestmap(url: '/admin/**',      configAttribute: 'ROLE_ADMIN').save()
//        new Requestmap(url: '/admin/role/**', configAttribute: 'ROLE_SUPERVISOR').save()
//        new Requestmap(url: '/admin/user/**', configAttribute: 'ROLE_ADMIN,ROLE_SUPERVISOR').save()
//        new Requestmap(url: '/j_spring_security_switch_user',
//                configAttribute: 'ROLE_SWITCH_USER,isFullyAuthenticated()').save()
    }
    def destroy = {
    }
}
