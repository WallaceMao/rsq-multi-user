grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"

grails.project.fork = [
    // configure settings for compilation JVM, note that if you alter the Groovy version forked compilation is required
//    compile: [maxMemory: 256, minMemory: 64, debug: false, maxPerm: 256, daemon:true],

    // configure settings for the test-app JVM, uses the daemon by default
//    test: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, daemon:true],
    test: false,
    // configure settings for the run-app JVM
    run: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, forkReserve:false],
    // configure settings for the run-war JVM
    war: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, forkReserve:false],
    // configure settings for the Console UI JVM
    console: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256]
]

grails.project.dependency.resolver = "maven" // or ivy
grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // uncomment to disable ehcache
        // excludes 'ehcache'
    }
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    repositories {
        grailsCentral()
        mavenLocal()
        mavenCentral()
        // uncomment the below to enable remote dependency resolution
        // from public Maven repositories
        //mavenRepo "http://repository.codehaus.org"
        //mavenRepo "http://download.java.net/maven/2/"
        //mavenRepo "http://repository.jboss.com/maven2/"
    }
    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.
        // runtime 'mysql:mysql-connector-java:5.1.27'
        test "mysql:mysql-connector-java:5.1.39"
    }

    plugins {
//        compile ":rsq-domain:0.1"

//        compile ":spring-security-core:2.0.0"

        //  我靠，不知道为啥还需要用到tomcat这个插件？不用的话domain的autoTimestamp居然会失效，报这个错：
        /**
         * | Failure:  test demoUser create(com.rishiqing.test.it.user.MidUserServiceIntegrationSpec)
         |  org.springframework.dao.DataIntegrityViolationException: not-null property references a null or transient value : com.rishiqing.domain.user.DemoUser.address; nested exception is org.hibernate.PropertyValueException: not-null property references a null or transient value : com.rishiqing.domain.user.DemoUser.address
         at com.rishiqing.mid.user.UserBizService$$EQRbHaTP.$tt__createDemoUser(UserBizService.groovy:24)
         at com.rishiqing.test.it.user.MidUserServiceIntegrationSpec.test demoUser create(MidUserServiceIntegrationSpec.groovy:55)
         Caused by: org.hibernate.PropertyValueException: not-null property references a null or transient value : com.rishiqing.domain.user.DemoUser.address
         ... 2 more
         */
        test ":tomcat:8.0.22" // or ":tomcat:8.0.22"

        build(":release:3.1.2",
              ":rest-client-builder:2.1.1") {
            export = false
        }
    }
}

//  开发环境下加载rsq-mid-user
grails.plugin.location.'rsq-dao-domain' = "..\\rsqDaoDomain"
