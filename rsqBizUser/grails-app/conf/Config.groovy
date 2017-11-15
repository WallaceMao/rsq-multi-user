// configuration for plugin testing - will not be included in the plugin zip

rsq.plugin.mid.user.dismissMaxNumber = 200

grails.gorm.failOnError = true

log4j = {
    // Example of changing the log pattern for the default console
    // appender:
    //
    //appenders {
    //    console name:'stdout', layout:pattern(conversionPattern: '%c{2} %m%n')
    //}
    debug  'grails.app'

    error  'org.codehaus.groovy.grails.browser.servlet',  //  controllers
           'org.codehaus.groovy.grails.browser.pages', //  GSP
           'org.codehaus.groovy.grails.browser.sitemesh', //  layouts
           'org.codehaus.groovy.grails.browser.mapping.filter', // URL mapping
           'org.codehaus.groovy.grails.browser.mapping', // URL mapping
           'org.codehaus.groovy.grails.commons', // core / classloading
           'org.codehaus.groovy.grails.plugins', // plugins
           'org.codehaus.groovy.grails.orm.hibernate', // hibernate integration
           'org.springframework',
           'org.hibernate',
           'net.sf.ehcache.hibernate'
}
