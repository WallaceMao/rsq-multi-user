//  读取home目录下的配置文件
Properties props = new Properties()
File propertiesFile = new File("${System.getProperty('user.home')}${File.separator}.multiusertest.properties")
propertiesFile.withInputStream {
    props.load(it)
}

//  默认环境为local，需要在启动测试时通过-Dgeb.env=prod指定使用prod环境
environments {
    //  default env is local
    local {
        baseUrl = "http://127.0.0.1:8080/"
        path = "multiuserdemo/"
    }
    rsqprod {
        baseUrl = "https://www.rishiqing.com/"
        path = "task/"
    }
    rsqbeta {
        baseUrl = "http://beta.rishiqing.com/"
        path = "task/"
        dataSource {
            url = "${props.url}"
            username = "${props.username}"
            password = "${props.password}"
            driverClassName = "${props.driverClassName}"
        }
        //  基本用户
        users {
            //  使用邮箱注册的用户
            emailUsers = [
                    [ email: "421503610@qq.com", realName: "webDriver自动化测试", password: "qazXSW" ]
            ]
            //  使用手机号注册的用户
            phoneUsers = [
                    [ phoneNumber: "13810360752", realName: "webDriver自动化测试", password: "wsxCDE"]
            ]
            //  使用QQ注册的用户，由于无法有唯一标识，所以只能根据realName和qq open id 不为null查找
            qqUsers = [
                    [ realName: "文殊圣灵"]
            ]
            //  使用微信注册的用户，由于无法获取到唯一标识，所以只能根据realName和wx open id部位null查找
            weixinUsers = [
                    [ realName: "毛文强的真身-Wallace%"]
            ]
            //  使用微博注册的用户
            weiboUsers = [
                    [ realName: "Wallace_Mao"]
            ]
            dingUsers = [
                    [ realName: "毛文强"]
            ]
        }
    }
}