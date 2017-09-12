//  Groovy配置文件时需要注意文件中的变量会与配置中的变量发生冲突，为避免冲突文件中的变量统一使用_前缀
//  读取home目录下的配置文件
Properties _props = new Properties()
File _propertiesFile = new File("${System.getProperty('user.home')}${File.separator}.multiuser_beta.properties")
_propertiesFile.withInputStream {
    _props.load(it)
}

Properties _localProps = new Properties()
File _localPropertiesFile = new File("${System.getProperty('user.home')}${File.separator}.multiuser_local.properties")
_localPropertiesFile.withInputStream {
    _localProps.load(it)
}

String _emailDomain = 'rsqtest.com'
String _emailDomainForTestTeamJoin = 'rsqtestteamjoin.com'
String _emailDomainForTestTeamCreate = 'rsqtestteamcreate.com'
Long _phoneBaseForTestTeamJoin = 13810000000
Long _phoneBaseForTestTeamCreate = 13810001000

global {
    testEmailDomain = _emailDomain
}

//  默认环境为local，需要在启动测试时通过-Dgeb.env=prod指定使用prod环境
environments {
    //  default env is local
    local {
        baseUrl = "http://localhost:8080/"
        path = "task/"
        dataSource {
            url = "${_localProps.url}"
            username = "${_localProps.username}"
            password = "${_localProps.password}"
            driverClassName = "${_localProps.driverClassName}"
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
        suite {
            teamCreate {
                emailDomain = _emailDomainForTestTeamCreate
                phoneBase = _phoneBaseForTestTeamCreate
                teamUser1 = [username: "team_user_1@${_emailDomainForTestTeamCreate}", realName: 'createTeam自动化测试1', password: 'qazXSW']
                teamUser2 = [username: "team_user_2@${_emailDomainForTestTeamCreate}", realName: 'createTeam自动化测试2', password: 'wsxCDE']
                teamUser3 = [username: "team_user_3@${_emailDomainForTestTeamCreate}", realName: 'createTeam自动化测试3', password: 'edcVFR']
                teamUser4 = [username: "team_user_4@${_emailDomainForTestTeamCreate}", realName: 'createTeam自动化测试4', password: 'rfvBGT']
                teamUser5 = [username: "team_user_5@${_emailDomainForTestTeamCreate}", realName: 'createTeam自动化测试5', password: 'tgbNHY']
                teamUser6 = [username: "team_user_6@${_emailDomainForTestTeamCreate}", realName: 'createTeam自动化测试6', password: 'yhnMJU']
                team1ForCreate = [name: '自动化测试团队teamCreate-A', phoneNumber: "${_phoneBaseForTestTeamCreate + 1}"]
                team2ForCreate = [name: '自动化测试团队teamCreate-B', phoneNumber: "${_phoneBaseForTestTeamCreate + 2}"]
                team3ForCreate = [name: '自动化测试团队teamCreate-C', phoneNumber: "${_phoneBaseForTestTeamCreate + 3}"]
                team4ForCreate = [name: '自动化测试团队teamCreate-D', phoneNumber: "${_phoneBaseForTestTeamCreate + 4}"]
                team5ForCreate = [name: '自动化测试团队teamCreate-E', phoneNumber: "${_phoneBaseForTestTeamCreate + 5}"]
                team6ForCreate = [name: '自动化测试团队teamCreate-F', phoneNumber: "${_phoneBaseForTestTeamCreate + 6}"]
                team7ForCreate = [name: '自动化测试团队teamCreate-G', phoneNumber: "${_phoneBaseForTestTeamCreate + 7}"]
                team8ForCreate = [name: '自动化测试团队teamCreate-H', phoneNumber: "${_phoneBaseForTestTeamCreate + 8}"]
            }
            //  加入团队相关的测试数据
            teamJoin {
                emailDomain = _emailDomainForTestTeamJoin
                phoneBase = _phoneBaseForTestTeamJoin
                //  团队管理员，用于创建团队的用户
                teamManager = [username: "personal_user_0@${_emailDomainForTestTeamJoin}", realName: 'webDriver自动化测试0', password: '123321']
                //  直接邮箱邀请尚未注册的用户
                userWithEmailForDirectInvite = [username: "personal_user_a@${_emailDomainForTestTeamJoin}", realName: 'webDriver自动化测试1', password: 'edcVFR']
                //  直接手机号邀请尚未注册的用户
                userWithPhoneForDirectInvite = [phone: "${_phoneBaseForTestTeamJoin + 1}", realName: 'webDriver自动化测试2', password: 'rfvBGT']
                //  直接邮箱邀请已经注册的用户
                userWithEmailForDirectInviteRegistered = [username: "personal_user_c@${_emailDomainForTestTeamJoin}", realName: 'webDriver自动化测试3', password: 'qazXSW']
                //  直接手机号邀请已经注册的用户
                userWithPhoneForDirectInviteRegistered = [phone: "${_phoneBaseForTestTeamJoin + 2}", realName: 'webDriver自动化测试4', password: 'wsxCDE']
                //  由于批量邀请未注册的用户不能由用户设置密码，只能走邀请链接-》忘记密码的流程，所以这里不做测试
                //  批量邮箱邀请已经注册的用户
                userWithEmailForBatchInviteRegistered = [username: "personal_user_e@${_emailDomainForTestTeamJoin}", realName: 'webDriver自动化测试5', password: 'rfvBGT']
                //  批量手机号邀请已经注册的用户
                userWithPhoneForBatchInviteRegistered = [phone: "${_phoneBaseForTestTeamJoin + 3}", realName: 'webDriver自动化测试6', password: 'tgbNHY']
                //  邮箱注册通过连接加入团队的尚未注册的用户
                userWithEmailForJoinLink = [username: "personal_user_g@${_emailDomainForTestTeamJoin}", realName: 'webDriver自动化测试7', password: 'yhnMJU']
                //  手机注册通过连接加入团队的尚未注册的用户
                userWithPhoneForJoinLink = [phone: "${_phoneBaseForTestTeamJoin + 4}", realName: 'webDriver自动化测试8', password: 'mjuYHN']
                //  邮箱注册的通过链接加入团队的已经注册的用户
                userWithEmailForJoinLinkRegistered = [username: "personal_user_i@${_emailDomainForTestTeamJoin}", realName: 'webDriver自动化测试9', password: 'nhyTGB']
                //  手机号注册的通过链接加入团队的已经注册的用户
                userWithPhoneForJoinLinkRegistered = [phone: "${_phoneBaseForTestTeamJoin + 5}", realName: 'webDriver自动化测试10', password: 'vfrEDC']
                //  测试团队
                teamForJoin = [name: '自动化测试团队teamJoin-A']
            }
            multiuser {
                //
                //  纯个人用户，不会创建团队
                userForPersonal = [username: "personal_user_a@${_emailDomain}", realName: 'webDriver自动化测试1', password: 'edcVFR']
                //  未来会创建核心测试团队Team A
                userForTeamCreate = [username: "team_user_a@${_emailDomain}", realName: 'webDriver自动化测试2', password: 'rfvBGT']
                //  以个人用户的名义注册，未来会被邀请加入到teamForCreate
                userForInvitePersonal = [username: "team_user_b@${_emailDomain}", realName: 'webDriver自动化测试3', password: 'tgbNHY']
                //  以个人用户的名义注册，并已经创建了团队teamForAnother，未来会被加入到teamForCreate
                userForInviteTeam = [username: "team_user_c@${_emailDomain}", realName: 'webDriver自动化测试4', password: 'yhnMJU']
                //  尚未注册的用户，直接通过邮箱邀请
                userForInviteNotRegistered = [username: "team_user_d@${_emailDomain}", realName: 'webDriver自动化测试5', password: 'ujm<KI']
                //  由userForTeamCreate创建的核心测试团队
                teamForCreate = [name: "自动化测试团队A"]
                //  userForInviteTeam所创建的团队
                teamForAnother = [name: "自动化测试团队C"]
            }
        }
    }
    rsqprod {
        baseUrl = "https://www.rishiqing.com/"
        path = "task/"
    }
    rsqbeta {
        baseUrl = "http://beta.rishiqing.com/"
        path = "task/"
        dataSource {
            url = "${_props.url}"
            username = "${_props.username}"
            password = "${_props.password}"
            driverClassName = "${_props.driverClassName}"
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