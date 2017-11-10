// locations to search for config files that get merged into the main config;
// config files can be ConfigSlurper scripts, Java properties files, or classes
// in the classpath in ConfigSlurper format

import org.apache.log4j.DailyRollingFileAppender

 grails.config.locations = [ "classpath:${appName}-config.properties"
//                             "classpath:${appName}-config.groovy",
//                             "file:${userHome}/.grails/${appName}-config.properties",
//                             "file:${userHome}/.grails/${appName}-config.groovy"
 ]

// if (System.properties["${appName}.config.location"]) {
//    grails.config.locations << "file:" + System.properties["${appName}.config.location"]
// }

grails.project.groupId = appName // change this to alter the default package name and Maven publishing destination

// The ACCEPT header will not be used for content negotiation for user agents containing the following strings (defaults to the 4 major rendering engines)
grails.mime.disable.accept.header.userAgents = ['Gecko', 'WebKit', 'Presto', 'Trident']
grails.mime.types = [ // the first one is the default format
    all:           '*/*', // 'all' maps to '*' or the first available format in withFormat
    atom:          'application/atom+xml',
    css:           'text/css',
    csv:           'text/csv',
    form:          'application/x-www-form-urlencoded',
    html:          ['text/html','application/xhtml+xml'],
    js:            'text/javascript',
    json:          ['application/json', 'text/json'],
    multipartForm: 'multipart/form-data',
    rss:           'application/rss+xml',
    text:          'text/plain',
    hal:           ['application/hal+json','application/hal+xml'],
    xml:           ['text/xml', 'application/xml']
]

// URL Mapping Cache Max Size, defaults to 5000
//grails.urlmapping.cache.maxsize = 1000

// Legacy setting for codec used to encode data with ${}
grails.views.default.codec = "html"

// The default scope for controllers. May be prototype, session or singleton.
// If unspecified, controllers are prototype scoped.
grails.controllers.defaultScope = 'singleton'

// GSP settings
grails {
    views {
        gsp {
            encoding = 'UTF-8'
            htmlcodec = 'xml' // use xml escaping instead of HTML4 escaping
            codecs {
                expression = 'html' // escapes values inside ${}
                scriptlet = 'html' // escapes output from scriptlets in GSPs
                taglib = 'none' // escapes output from taglibs
                staticparts = 'none' // escapes output from static template parts
            }
        }
        // escapes all not-encoded output at final stage of outputting
        // filteringCodecForContentType.'text/html' = 'html'
    }
}


grails.converters.encoding = "UTF-8"
// scaffolding templates configuration
grails.scaffolding.templates.domainSuffix = 'Instance'

// Set to false to use the new Grails 1.2 JSONBuilder in the render method
grails.json.legacy.builder = false
// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true
// packages to include in Spring bean scanning
grails.spring.bean.packages = []
// whether to disable processing of multi part requests
grails.web.disable.multipart=false

// request parameters to mask when logging exceptions
grails.exceptionresolver.params.exclude = ['password']

// configure auto-caching of queries by default (if false you can cache individual queries with 'cache: true')
grails.hibernate.cache.queries = false

// configure passing transaction's read-only attribute to Hibernate session, queries and criterias
// set "singleSession = false" OSIV mode in hibernate configuration after enabling
grails.hibernate.pass.readonly = false
// configure passing read-only to OSIV session by default, requires "singleSession = false" OSIV mode
grails.hibernate.osiv.readonly = false

grails.gorm.failOnError = true

environments {
    development {
        grails.logging.jul.usebridge = true
    }
    production {
        grails.logging.jul.usebridge = false
        // TODO: grails.serverURL = "http://www.changeme.com"
    }
}

// log4j configuration
log4j = {
    /**
     *  log4j使用说明
     * （1）logger用于日志打印，appender用于指定日志的输出方式（控制台、日志文件、数据库……）
     * （2）对于一个logger，可以指定一个logger的等级（debug/warn/error等），可以指定一个或多个appender
     * （3）log4j库有一个root的logger，如果没有给特定类的logger指定输出等级和appender，则默认使用root logger的等级和输出（error，console）；除非做特殊配置（additivity: false），否则特定类的logger打印输出之后，仍然会调用root logger打印输出日志。
     * （4）stacktrace为特殊的logger，当exception发生时，tomcat会把exception记录到stacktrace.log文件中
     */
    appenders {
        /**
         *  新建了5个appender，名称分别为stacktrace，consoleAppender，rootDailyAppender，rollingAppender，dailyAppender
         *  stacktrace：tomcat中所有的exception会输出到stacktrace.log中
         *  consoleAppender：输出到控制台的appender。开发环境直接输出到开发控制台，tomcat环境输出到catalina.out
         *  rootDailyAppender：每天生成一个文件，用于配置到root logger
         *  rollingAppender：只生成最新的日志文件，旧的日志文件将会被覆盖
         *  dailyAppender：业务程序的输出文件，每天生成一个
         */
        'null' name: "stacktrace"  //  将stacktrace输出到null，不再输出到stacktrace.log中

        console name:'consoleAppender',
                layout: pattern(conversionPattern:'%d [%t] %-5p %c{2} %x - %m%n')

        appender new DailyRollingFileAppender(
                name: 'rootDailyAppender',
                datePattern: "'.'yyyy-MM-dd",  // See the API for all patterns.
                fileName: "../logs/rsq_root_daily_rolling.log",
                layout: pattern(conversionPattern:'%d [%t] %-5p %c{2} %x - %m%n')
        )

        //  只在生产环境使用
        environments {
            production {
                rollingFile name: "rollingAppender",
                        maxFileSize: 10 * 1024 * 1024,  //10M
                        maxBackupIndex: 3,
                        file: "../logs/rsq_rolling.log"

                appender new DailyRollingFileAppender(
                        name: 'dailyAppender',
                        datePattern: "'.'yyyy-MM-dd",  // See the API for all patterns.
                        fileName: "../logs/rsq_daily_rolling.log",
                        layout: pattern(conversionPattern:'%d [%t] %-5p %c{2} %x - %m%n')
                )
            }
        }

    }

    //  root只用于输出没有配置的意外情况，具体的各种环境的配置分环境设置
    root {
        error 'consoleAppender'
    }

    environments {
        //  开发环境的log配置
        development {
            error additivity: true,  //  additivity为false，表示对于该配置中的相关类的logger不再调用root logger打印日志
                    consoleAppender:
                            ['org.codehaus.groovy.grails.browser.servlet',        // controllers
                             'org.codehaus.groovy.grails.browser.pages',          // GSP
                             'org.codehaus.groovy.grails.browser.sitemesh',       // layouts
                             'org.codehaus.groovy.grails.browser.mapping.filter', // URL mapping
                             'org.codehaus.groovy.grails.browser.mapping',        // URL mapping
                             'org.codehaus.groovy.grails.commons',            // core / classloading
                             'org.codehaus.groovy.grails.plugins',            // plugins
                             'org.codehaus.groovy.grails.orm.hibernate',      // hibernate integration
                             'org.springframework',
                             'org.hibernate',
                             'net.sf.ehcache.hibernate',
                             'grails.app.conf',
                             'grails.app.filters',
                             'grails.app.domain',
                             'grails.app.services',
                             'grails.app.controllers']
        }
        //  生产环境的log配置
        production {
            error  additivity: false,  //  additivity为false，表示对于该配置中的相关类的logger不再调用root logger打印日志
                    dailyAppender:
                            ['org.codehaus.groovy.grails.browser.servlet',        // controllers
                             'org.codehaus.groovy.grails.browser.pages',          // GSP
                             'org.codehaus.groovy.grails.browser.sitemesh',       // layouts
                             'org.codehaus.groovy.grails.browser.mapping.filter', // URL mapping
                             'org.codehaus.groovy.grails.browser.mapping',        // URL mapping
                             'org.codehaus.groovy.grails.commons',            // core / classloading
                             'org.codehaus.groovy.grails.plugins',            // plugins
                             'org.codehaus.groovy.grails.orm.hibernate',      // hibernate integration
                             'org.springframework',
                             'org.hibernate',
                             'net.sf.ehcache.hibernate',
                             'grails.app.conf',
                             'grails.app.filters',
                             'grails.app.domain',
                             'grails.app.services',
                             'grails.app.controllers']
        }
    }
}

//grails.plugin.springsecurity.authority.className = 'com.taskmanagement.organization.Role'
//grails.plugin.springsecurity.useHttpSessionEventPublisher = true
//grails.plugin.springsecurity.userLookup.userDomainClassName = 'com.taskmanagement.organization.User'
//grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'com.taskmanagement.organization.UserRole'
//grails.plugin.springsecurity.requestMap.className = 'com.taskmanagement.organization.Requestmap'
//grails.plugin.springsecurity.securityConfigType = 'Requestmap'
//grails.plugin.springsecurity.apf.filterProcessesUrl = '/j_spring_security_check'
//grails.plugin.springsecurity.apf.storeLastUsername = true
//grails.plugin.springsecurity.rejectIfNoRule = true
//grails.plugin.springsecurity.useSwitchUserFilter = true
//grails.plugin.springsecurity.rememberMe.persistent = true
//grails.plugin.springsecurity.rememberMe.persistentToken.domainClassName = 'com.taskmanagement.organization.PersistentToken'
//grails.plugin.springsecurity.rememberMe.tokenValiditySeconds = 1209600
//grails.plugin.springsecurity.rememberMe.cookieName = 'rishiqingRem'
//grails.plugin.springsecurity.rememberMe.key='chanceCreateWallaceMao640321'
//grails.plugin.springsecurity.logout.afterLogoutUrl = '/login/auth'
//grails.plugin.springsecurity.auth.loginFormUrl = '/login/auth'
//grails.plugin.springsecurity.auth.ajaxLoginFormUrl = '/login/authAjax'
//grails.plugin.springsecurity.password.algorithm = 'SHA-512'
//grails.plugin.springsecurity.password.encodeHashAsBase64 = false
//grails.plugin.springsecurity.password.hash.iterations = 1
//grails.plugin.springsecurity.successHandler.targetUrlParameter = 'redirect_url'
//grails.plugin.springsecurity.dao.hideUserNotFoundExceptions = false
//grails.plugin.springsecurity.useSecurityEventListener = true
//grails.plugin.springsecurity.providerNames=[
//        'daoAuthenticationProvider',
//        'anonymousAuthenticationProvider',
//        'rememberMeAuthenticationProvider'
//]


// Added by the Spring Security Core plugin:
grails.plugin.springsecurity.logout.postOnly = false
grails.plugin.springsecurity.userLookup.userDomainClassName = 'com.rishiqing.domain.user.User'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'com.rishiqing.domain.user.UserRole'
grails.plugin.springsecurity.authority.className = 'com.rishiqing.domain.user.Role'
grails.plugin.springsecurity.requestMap.className = 'com.rishiqing.domain.user.Requestmap'
grails.plugin.springsecurity.securityConfigType = 'Requestmap'
grails.plugin.springsecurity.controllerAnnotations.staticRules = [
	'/':                ['permitAll'],
	'/index':           ['permitAll'],
	'/index.gsp':       ['permitAll'],
	'/assets/**':       ['permitAll'],
	'/**/js/**':        ['permitAll'],
	'/**/css/**':       ['permitAll'],
	'/**/images/**':    ['permitAll'],
	'/**/favicon.ico':  ['permitAll']
]

grails.plugin.springsecurity.rememberMe.persistent = true
grails.plugin.springsecurity.rememberMe.persistentToken.domainClassName = 'com.rishiqing.domain.user.PersistentLogin'

grails.plugin.springsecurity.useSwitchUserFilter = true

