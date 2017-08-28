grails.servlet.version = "3.0" // Change depending on target container compliance (2.5 or 3.0)
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.work.dir = "target/work"
grails.project.target.level = 1.6
grails.project.source.level = 1.6
//grails.project.war.file = "target/${appName}-${appVersion}.war"

//grails.project.fork.run=true
grails.project.fork = [
    // configure settings for compilation JVM, note that if you alter the Groovy version forked compilation is required
    compile: [maxMemory: 256, minMemory: 64, debug: false, maxPerm: 256, daemon:true],
//    compile: false,
    // configure settings for the test-app JVM, uses the daemon by default
    test: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, daemon:true],
//    test: false,
    // configure settings for the run-app JVM
//    run: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, forkReserve:false],
    run: false,
    // configure settings for the run-war JVM
    war: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, forkReserve:false],
//    war: false,
    // configure settings for the Console UI JVM
    console: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256]
//    console: false
]

grails.project.dependency.resolver = "maven" // or ivy
grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // specify dependency exclusions here; for example, uncomment this to disable ehcache:
        // excludes 'ehcache'
    }
    log "error" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    checksums true // Whether to verify checksums on resolve
    legacyResolve false // whether to do a secondary resolve on plugin installation, not advised and here for backwards compatibility

    repositories {
        inherits true // Whether to inherit repository definitions from plugins

        grailsPlugins()
        grailsHome()
        mavenLocal()
        grailsCentral()
        mavenCentral()
//        mavenRepo "http://127.0.0.1:8081/nexus/content/groups/public/"
        mavenRepo "https://raw.github.com/WallaceMao/rsq-user-midware/mvn-repo/"
        // uncomment these (or add new ones) to enable remote dependency resolution from public Maven repositories
        //mavenRepo "http://repository.codehaus.org"
        //mavenRepo "http://download.java.net/maven/2/"
        //mavenRepo "http://repository.jboss.com/maven2/"
    }

    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes e.g.
         runtime 'mysql:mysql-connector-java:5.1.39'
        // runtime 'org.postgresql:postgresql:9.3-1101-jdbc41'
        test "org.grails:grails-datastore-test-support:1.0.2-grails-2.4"

        // geb and spock for functional test
        test "org.gebish:geb-spock:1.0"
        test "org.seleniumhq.selenium:selenium-chrome-driver:2.53.1"
        test "org.seleniumhq.selenium:selenium-support:2.53.1"

//        runtime "com.rishiqing.midware.user:rsq-user-midware:1.0-SNAPSHOT"
    }

    plugins {
        // geb plugin for functional test
        test 'org.grails.plugins:geb:1.0'

        // plugins for the build system only
        build ":tomcat:8.0.22" // or ":tomcat:7.0.55.3"

        // plugins for the compile step
        compile ":scaffolding:2.1.2"
        compile ':cache:1.1.8'
        // asset-pipeline 2.0+ requires Java 7, use version 1.9.x with Java 6
        compile ":asset-pipeline:2.5.7"

        // plugins needed at runtime but not for compilation
        runtime ":hibernate4:4.3.10" // or ":hibernate:3.6.10.18"
        runtime ":database-migration:1.4.0"
        runtime ":jquery:1.11.1"

        compile ":spring-security-core:2.0.0"

//        comile ":rsq-user:0.1"

        // Uncomment these to enable additional asset-pipeline capabilities
        //compile ":sass-asset-pipeline:1.9.0"
        //compile ":less-asset-pipeline:1.10.0"
        //compile ":coffee-asset-pipeline:1.8.0"
        //compile ":handlebars-asset-pipeline:1.3.0.3"
    }
}

//  开发环境下加载rsq-mid-user
grails.plugin.location.'rsq-dao-domain' = "..\\rsqDaoDomain"
grails.plugin.location.'rsq-biz-user' = "..\\rsqBizUser"
grails.plugin.location.'rsq-util' = "..\\rsqUtil"
