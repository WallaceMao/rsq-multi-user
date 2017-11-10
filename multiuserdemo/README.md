# grails(2.5.x) test demo
 
## 1 grails unit test（单元测试）
grails单元测试是针对编写的单个类方法进行测试，其所依赖的其他domain/service等需要使用Mock来模拟
grails单元测试一般由开发人员编写，对单个业务逻辑复杂的类进行

## 2 grails integration test（集成测试）
grails集成测试是模拟grails的context环境，使用spock进行测试，测试用例所依赖的domain/service等均可以正常使用，不再需要mock
grails集成测试一般由开发人员编写，对于依赖较多的controller和service，使用单元测试比较麻烦，可以直接进行集成测试

### 基本配置

## 3 grails functional test（功能测试）
grails功能测试是在grails的环境下启动functional test。使用Geb-spock的集成测试框架进行。

### 依赖
    dependencies {
        test "org.gebish:geb-spock:1.0"
        //  连接数据库的话需要使用MySQL驱动
        runtime "mysql:mysql-connector-java:5.1.39"
    }
    plugins {
        test 'org.grails.plugins:geb:1.0'
        //  由于要启动grails application，所以需要加上grails appliaction依赖的插件
        build ":tomcat:7.0.70"
        compile ":scaffolding:2.1.2"
        compile ':cache:1.1.8'
        compile ":asset-pipeline:2.5.7"
        runtime ":hibernate4:4.3.10"
        runtime ":database-migration:1.4.0"
        runtime ":jquery:1.11.1"
    }

### 基本配置
#### FunctionalTestConfig.groovy
用于定义环境变量的配置文件，示例：

    //  默认环境为local，需要在启动测试时通过-Dgeb.env=prod指定使用prod环境
    environments {
        //  default env is local
        local {
            baseUrl = "http://beta.rishiqing.com/"
            path = "task/"
            validateUser {
            }
        }
        prod {
            baseUrl = "https://www.rishiqing.com/"
            path = "task/"
        }
    }

可以通过`ConfigUtil`读取

#### GebConfig.groovy
Geb基本配置，用于配置Geb的option，示例：

    import org.openqa.selenium.chrome.ChromeDriver
    
    def path = "${new File('.').getAbsolutePath()}${File.separator}test${File.separator}functional${File.separator}chromedriver.exe"
    
    driver = {
        System.setProperty("webdriver.chrome.driver",
                path)
        new ChromeDriver()
    }
    
    //  自动清除cookie
    autoClearCookies = true
    
    baseNavigatorWaiting = true
    atCheckWaiting = true
    
    baseUrl = "${ConfigUtil.config.baseUrl}${ConfigUtil.config.path}"

### api test（api测试）
使用grails的functional测试，执行api test
api测试不调用web driver，直接通过封装过的[Unirest库](http://unirest.io/java.html)调用url，并对返回值进行验证

#### 新增依赖：

    dependencies {
        //  Unirest库，用于发送rest请求
        test "com.mashape.unirest:unirest-java:1.4.9"
    }

#### 代码编写
参照`com.rishiqing.test.functional.rest.spec.account.ValidateUserSpec.groovy`

#### 代码运行
运行测试的方式有两种：

##### IDEA环境下
- 由于功能测试可以不依赖grails application context，那么就可以直接使用IDEA集成的jUnit runner来运行api test的测试用例
- 可以直接在Run/Debug Configuration中配置jUnit的Runner，或者使用`Ctrl + Shift + F10`快捷键运行测试用例
- 如果需要配置不同环境，可以在Run/Debug Configuration的VM options中配置环境参数，例如

      -Dgeb.env=prod

##### grails环境下
命令行运行

    grails test-app -Dgeb.env=local --echoOut functional: com.rishiqing.test.functional.rest.**.*

进行api功能测试，参数说明：

- `--echoOut`：输出测试用例中的打印结果
- `-Dgev.env`：指定运行测试时的环境参数，默认为local，需要与FunctionalTestConfig.groovy中的环境参数一致
- `functional:`：运行功能测试
- `com.rishiqing.test.functional.rest.**.*`：运行`com.rishiqing.test.functional.rest`包及其子包中的所有测试用例

### web functional test（web端功能测试）
调用web driver操作浏览器，执行浏览器环境下的功能测试

#### 新增依赖：

    dependencies {
        //  selenium 相应的web driver，用于驱动浏览器
        test "org.seleniumhq.selenium:selenium-chrome-driver:2.53.1"
        test "org.seleniumhq.selenium:selenium-support:2.53.1"
    }
    
#### 代码编写
参照`com.rishiqing.test.functional.browser.spec.WeixinLoginSpec.groovy`

#### 代码运行
运行测试的方式有两种：

##### IDEA环境下
- 由于功能测试可以不依赖grails application context，那么就可以直接使用IDEA集成的jUnit runner来运行web functional test的测试用例
- 可以直接在Run/Debug Configuration中配置jUnit的Runner，或者使用`Ctrl + Shift + F10`快捷键运行测试用例
- 如果需要配置不同环境，可以在Run/Debug Configuration的VM options中配置环境参数，geb.env默认为local例如

      -Dgeb.env=prod

##### grails环境下
命令行运行

    grails test-app -Dgeb.env=local --echoOut functional: com.rishiqing.test.functional.browser.**.*

进行api功能测试，参数说明：

- `--echoOut`：输出测试用例中的打印结果
- `-Dgev.env`：指定运行测试时的环境参数，默认为local，需要与FunctionalTestConfig.groovy中的环境参数一致
- `functional:`：运行功能测试
- `com.rishiqing.test.functional.rest.**.*`：运行`com.rishiqing.test.functional.browser`包及其子包中的所有测试用例

## 常见问题解决

### 1 在windows下的console控制台或者powerShell控制台输出测试打印的中文时出现乱码？

在windows 命令行窗口输入命令`chcp 65001`，切换编码方式，其中65001表示UTF-8编码。
参考链接：[http://blog.csdn.net/u011250882/article/details/48136883](http://blog.csdn.net/u011250882/article/details/48136883)