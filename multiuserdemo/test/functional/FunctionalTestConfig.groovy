//  Groovy配置文件时需要注意文件中的变量会与配置中的变量发生冲突，为避免冲突文件中的变量统一使用_前缀
//  读取home目录下的配置文件
Properties _localProps = new Properties()
File _localPropertiesFile = new File("${System.getProperty('user.home')}${File.separator}.multiuser_local.properties")
_localPropertiesFile.withInputStream {
    _localProps.load(it)
}

//  dev环境下
Properties _devProps = new Properties()
File _devPropertiesFile = new File("${System.getProperty('user.home')}${File.separator}.multiuser_dev.properties")
_devPropertiesFile.withInputStream {
    _devProps.load(it)
}

//  test环境下
Properties _testProps = new Properties()
File _testPropertiesFile = new File("${System.getProperty('user.home')}${File.separator}.multiuser_test.properties")
_testPropertiesFile.withInputStream {
    _testProps.load(it)
}

Properties _betaProps = new Properties()
File _betaPropertiesFile = new File("${System.getProperty('user.home')}${File.separator}.multiuser_beta.properties")
_betaPropertiesFile.withInputStream {
    _betaProps.load(it)
}

String _emailDomain = 'rsqtest.com'
String _emailDomainForTestTeamJoin = 'rsqtestteamjoin.com'
String _emailDomainForTestTeamCreate = 'rsqtestteamcreate.com'
String _emailDomainForTestTeamJoinMultiuser = 'rsqtestteamjoinmulti.com'
String _emailDomainForTestTeamMainUser = 'rsqtestteammainuser.com'
String _emailDomainForTestTeamSwitchUser = 'rsqtestteamswitchuser.com'
String _emailDomainForTestTeamUserData = 'rsqtestteamuserdata.com'
Long _phoneBaseForTestTeamJoin = 13810000000
Long _phoneBaseForTestTeamCreate = 13810001000
Long _phoneBaseForTestTeamJoinMultiuser = 13810002000
Long _phoneBaseForTestTeamMainUser = 13810003000
Long _phoneBaseForTestTeamSwitchUser = 13810004000
Long _phoneBaseForTestTeamUserData = 13810005000

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
            login {
                realAdmin = [username: "admin", password: "123456"]
                fakeAdmin = [username: "adminxxx", password: "123456"]
                fakeAdmin2 = [username: "admin", password: "fakepassword"]
                fakeCommonUser = [username: "zhaoyun@rishiqing.com", password: "13141234123"]
            }
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
            teamJoinMultiUser {
                emailDomain = _emailDomainForTestTeamJoinMultiuser
                phoneBase = _phoneBaseForTestTeamJoinMultiuser
                teamUser1 = [username: "team_user_1@${_emailDomainForTestTeamJoinMultiuser}", realName: 'joinTemaMultiuser自动化测试1', password: 'qazXSW']
                teamUser2 = [username: "team_user_2@${_emailDomainForTestTeamJoinMultiuser}", realName: 'joinTemaMultiuser自动化测试2', password: 'wsxCDE']
                teamUser3 = [username: "team_user_3@${_emailDomainForTestTeamJoinMultiuser}", realName: 'joinTemaMultiuser自动化测试3', password: 'edcVFR']
                teamUser4 = [username: "team_user_4@${_emailDomainForTestTeamJoinMultiuser}", realName: 'joinTemaMultiuser自动化测试4', password: 'rfvBGT']
                teamUser5 = [username: "team_user_5@${_emailDomainForTestTeamJoinMultiuser}", realName: 'joinTemaMultiuser自动化测试5', password: 'tgbNHY']
                teamUser6 = [username: "team_user_6@${_emailDomainForTestTeamJoinMultiuser}", realName: 'joinTemaMultiuser自动化测试6', password: 'yhnMJU']
                teamUser7 = [username: "team_user_7@${_emailDomainForTestTeamJoinMultiuser}", realName: 'joinTemaMultiuser自动化测试7', password: 'yhnMJU']
                teamUser8 = [username: "team_user_8@${_emailDomainForTestTeamJoinMultiuser}", realName: 'joinTemaMultiuser自动化测试8', password: 'qazXSW']
                teamUser9 = [username: "team_user_9@${_emailDomainForTestTeamJoinMultiuser}", realName: 'joinTemaMultiuser自动化测试9', password: 'wsxCDE']
                teamUser10 = [username: "team_user_10@${_emailDomainForTestTeamJoinMultiuser}", realName: 'joinTemaMultiuser自动化测试10', password: 'edcVFR']
                teamUser11 = [username: "team_user_11@${_emailDomainForTestTeamJoinMultiuser}", realName: 'joinTemaMultiuser自动化测试11', password: 'rfvBGT']
                teamUser12 = [username: "team_user_12@${_emailDomainForTestTeamJoinMultiuser}", realName: 'joinTemaMultiuser自动化测试12', password: 'tgbNHY']
                teamUser13 = [username: "team_user_13@${_emailDomainForTestTeamJoinMultiuser}", realName: 'joinTemaMultiuser自动化测试13', password: 'yhnMJU']
                teamUser14 = [username: "team_user_14@${_emailDomainForTestTeamJoinMultiuser}", realName: 'joinTemaMultiuser自动化测试14', password: 'yhnMJU']
                team1ForCreate = [name: '自动化测试团队teamJoinMultiuser-A', phoneNumber: "${_phoneBaseForTestTeamJoinMultiuser + 1}"]
                team2ForCreate = [name: '自动化测试团队teamJoinMultiuser-B', phoneNumber: "${_phoneBaseForTestTeamJoinMultiuser + 2}"]
                team3ForCreate = [name: '自动化测试团队teamJoinMultiuser-C', phoneNumber: "${_phoneBaseForTestTeamJoinMultiuser + 3}"]
                team4ForCreate = [name: '自动化测试团队teamJoinMultiuser-D', phoneNumber: "${_phoneBaseForTestTeamJoinMultiuser + 4}"]
                team5ForCreate = [name: '自动化测试团队teamJoinMultiuser-E', phoneNumber: "${_phoneBaseForTestTeamJoinMultiuser + 5}"]
                team6ForCreate = [name: '自动化测试团队teamJoinMultiuser-F', phoneNumber: "${_phoneBaseForTestTeamJoinMultiuser + 6}"]
                team7ForCreate = [name: '自动化测试团队teamJoinMultiuser-G', phoneNumber: "${_phoneBaseForTestTeamJoinMultiuser + 7}"]
                team8ForCreate = [name: '自动化测试团队teamJoinMultiuser-H', phoneNumber: "${_phoneBaseForTestTeamJoinMultiuser + 8}"]
                team9ForCreate = [name: '自动化测试团队teamJoinMultiuser-I', phoneNumber: "${_phoneBaseForTestTeamJoinMultiuser + 9}"]
                team10ForCreate = [name: '自动化测试团队teamJoinMultiuser-J', phoneNumber: "${_phoneBaseForTestTeamJoinMultiuser + 10}"]
                team11ForCreate = [name: '自动化测试团队teamJoinMultiuser-K', phoneNumber: "${_phoneBaseForTestTeamJoinMultiuser + 11}"]
                team12ForCreate = [name: '自动化测试团队teamJoinMultiuser-L', phoneNumber: "${_phoneBaseForTestTeamJoinMultiuser + 12}"]
                team13ForCreate = [name: '自动化测试团队teamJoinMultiuser-M', phoneNumber: "${_phoneBaseForTestTeamJoinMultiuser + 13}"]
                team14ForCreate = [name: '自动化测试团队teamJoinMultiuser-N', phoneNumber: "${_phoneBaseForTestTeamJoinMultiuser + 14}"]
            }
            teamMainUser {
                emailDomain = _emailDomainForTestTeamMainUser
                phoneBase = _phoneBaseForTestTeamMainUser
                teamUser1 = [username: "team_user_1@${_emailDomainForTestTeamMainUser}", realName: 'joinTemaMultiuser自动化测试1', password: 'qazXSW']
                teamUser2 = [username: "team_user_2@${_emailDomainForTestTeamMainUser}", realName: 'joinTemaMultiuser自动化测试2', password: 'wsxCDE']
                teamUser3 = [username: "team_user_3@${_emailDomainForTestTeamMainUser}", realName: 'joinTemaMultiuser自动化测试3', password: 'edcVFR']
                teamUser4 = [username: "team_user_4@${_emailDomainForTestTeamMainUser}", realName: 'joinTemaMultiuser自动化测试4', password: 'rfvBGT']
                teamUser5 = [username: "team_user_5@${_emailDomainForTestTeamMainUser}", realName: 'joinTemaMultiuser自动化测试5', password: 'tgbNHY']
                teamUser6 = [username: "team_user_6@${_emailDomainForTestTeamMainUser}", realName: 'joinTemaMultiuser自动化测试6', password: 'yhnMJU']
                teamUser7 = [username: "team_user_7@${_emailDomainForTestTeamMainUser}", realName: 'joinTemaMultiuser自动化测试7', password: 'yhnMJU']
                team1ForCreate = [name: '自动化测试团队teamMainUser-A', phoneNumber: "${_phoneBaseForTestTeamMainUser + 1}"]
                team2ForCreate = [name: '自动化测试团队teamMainUser-B', phoneNumber: "${_phoneBaseForTestTeamMainUser + 2}"]
                team3ForCreate = [name: '自动化测试团队teamMainUser-C', phoneNumber: "${_phoneBaseForTestTeamMainUser + 3}"]
                team4ForCreate = [name: '自动化测试团队teamMainUser-D', phoneNumber: "${_phoneBaseForTestTeamMainUser + 4}"]
                team5ForCreate = [name: '自动化测试团队teamMainUser-E', phoneNumber: "${_phoneBaseForTestTeamMainUser + 5}"]
                team6ForCreate = [name: '自动化测试团队teamMainUser-F', phoneNumber: "${_phoneBaseForTestTeamMainUser + 6}"]
                team7ForCreate = [name: '自动化测试团队teamMainUser-G', phoneNumber: "${_phoneBaseForTestTeamMainUser + 7}"]
                team8ForCreate = [name: '自动化测试团队teamMainUser-H', phoneNumber: "${_phoneBaseForTestTeamMainUser + 8}"]
            }
            teamSwitchUser {
                emailDomain = _emailDomainForTestTeamSwitchUser
                phoneBase = _phoneBaseForTestTeamSwitchUser
                teamUser1 = [username: "team_user_1@${_emailDomainForTestTeamSwitchUser}", realName: 'joinTemaMultiuser自动化测试1', password: 'qazXSW']
                teamUser2 = [username: "team_user_2@${_emailDomainForTestTeamSwitchUser}", realName: 'joinTemaMultiuser自动化测试2', password: 'wsxCDE']
                teamUser3 = [username: "team_user_3@${_emailDomainForTestTeamSwitchUser}", realName: 'joinTemaMultiuser自动化测试3', password: 'edcVFR']
                teamUser4 = [username: "team_user_4@${_emailDomainForTestTeamSwitchUser}", realName: 'joinTemaMultiuser自动化测试4', password: 'rfvBGT']
                teamUser5 = [username: "team_user_5@${_emailDomainForTestTeamSwitchUser}", realName: 'joinTemaMultiuser自动化测试5', password: 'tgbNHY']
                teamUser6 = [username: "team_user_6@${_emailDomainForTestTeamSwitchUser}", realName: 'joinTemaMultiuser自动化测试6', password: 'yhnMJU']
                teamUser7 = [username: "team_user_7@${_emailDomainForTestTeamSwitchUser}", realName: 'joinTemaMultiuser自动化测试7', password: 'yhnMJU']
                team1ForCreate = [name: '自动化测试团队teamSwitchUser-A', phoneNumber: "${_phoneBaseForTestTeamSwitchUser + 1}"]
                team2ForCreate = [name: '自动化测试团队teamSwitchUser-B', phoneNumber: "${_phoneBaseForTestTeamSwitchUser + 2}"]
                team3ForCreate = [name: '自动化测试团队teamSwitchUser-C', phoneNumber: "${_phoneBaseForTestTeamSwitchUser + 3}"]
                team4ForCreate = [name: '自动化测试团队teamSwitchUser-D', phoneNumber: "${_phoneBaseForTestTeamSwitchUser + 4}"]
                team5ForCreate = [name: '自动化测试团队teamSwitchUser-E', phoneNumber: "${_phoneBaseForTestTeamSwitchUser + 5}"]
                team6ForCreate = [name: '自动化测试团队teamSwitchUser-F', phoneNumber: "${_phoneBaseForTestTeamSwitchUser + 6}"]
                team7ForCreate = [name: '自动化测试团队teamSwitchUser-G', phoneNumber: "${_phoneBaseForTestTeamSwitchUser + 7}"]
                team8ForCreate = [name: '自动化测试团队teamSwitchUser-H', phoneNumber: "${_phoneBaseForTestTeamSwitchUser + 8}"]
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
                teamForJoin = [name: '自动化测试团队teamJoin-A', phoneNumber: '13810360752']
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
                teamForCreate = [name: "自动化测试团队A", phoneNumber: '13810360752']
                //  userForInviteTeam所创建的团队
                teamForAnother = [name: "自动化测试团队C", phoneNumber: '13810360753']
            }
            teamUserData {
                emailDomain = _emailDomainForTestTeamUserData
                phoneBase = _phoneBaseForTestTeamUserData
                teamUser1 = [username: "team_user_1@${_emailDomainForTestTeamUserData}", realName: 'teamUserData自动化测试1', password: 'qazXSW']
                teamUser2 = [username: "team_user_2@${_emailDomainForTestTeamUserData}", realName: 'teamUserData自动化测试2', password: 'wsxCDE']
                teamUser3 = [username: "team_user_3@${_emailDomainForTestTeamUserData}", realName: 'teamUserData自动化测试3', password: 'edcVFR']
                teamUser4 = [username: "team_user_4@${_emailDomainForTestTeamUserData}", realName: 'teamUserData自动化测试4', password: 'rfvBGT']
                teamUser5 = [username: "team_user_5@${_emailDomainForTestTeamUserData}", realName: 'teamUserData自动化测试5', password: 'tgbNHY']
                teamUser6 = [username: "team_user_6@${_emailDomainForTestTeamUserData}", realName: 'teamUserData自动化测试6', password: 'yhnMJU']
                teamUser7 = [username: "team_user_7@${_emailDomainForTestTeamUserData}", realName: 'teamUserData自动化测试7', password: 'yhnMJU']
                team1ForCreate = [name: '自动化测试团队teamUserData-A', phoneNumber: "${_phoneBaseForTestTeamUserData + 1}"]
                team2ForCreate = [name: '自动化测试团队teamUserData-B', phoneNumber: "${_phoneBaseForTestTeamUserData + 2}"]
                team3ForCreate = [name: '自动化测试团队teamUserData-C', phoneNumber: "${_phoneBaseForTestTeamUserData + 3}"]
                team4ForCreate = [name: '自动化测试团队teamUserData-D', phoneNumber: "${_phoneBaseForTestTeamUserData + 4}"]
                team5ForCreate = [name: '自动化测试团队teamUserData-E', phoneNumber: "${_phoneBaseForTestTeamUserData + 5}"]
                team6ForCreate = [name: '自动化测试团队teamUserData-F', phoneNumber: "${_phoneBaseForTestTeamUserData + 6}"]
                team7ForCreate = [name: '自动化测试团队teamUserData-G', phoneNumber: "${_phoneBaseForTestTeamUserData + 7}"]
                team8ForCreate = [name: '自动化测试团队teamUserData-H', phoneNumber: "${_phoneBaseForTestTeamUserData + 8}"]
            }
        }
    }

    //  dev test env
    dev {
        baseUrl = "http://dev.beta-test.rishiqing.com/"
        path = "task/"
        dataSource {
            url = "${_devProps.url}"
            username = "${_devProps.username}"
            password = "${_devProps.password}"
            driverClassName = "${_devProps.driverClassName}"
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
            login {
                realAdmin = [username: "admin", password: "123456"]
                fakeAdmin = [username: "adminxxx", password: "123456"]
                fakeAdmin2 = [username: "admin", password: "fakepassword"]
                fakeCommonUser = [username: "zhaoyun@rishiqing.com", password: "13141234123"]
            }
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
            teamJoinMultiUser {
                emailDomain = _emailDomainForTestTeamJoinMultiuser
                phoneBase = _phoneBaseForTestTeamJoinMultiuser
                teamUser1 = [username: "team_user_1@${_emailDomainForTestTeamJoinMultiuser}", realName: 'joinTemaMultiuser自动化测试1', password: 'qazXSW']
                teamUser2 = [username: "team_user_2@${_emailDomainForTestTeamJoinMultiuser}", realName: 'joinTemaMultiuser自动化测试2', password: 'wsxCDE']
                teamUser3 = [username: "team_user_3@${_emailDomainForTestTeamJoinMultiuser}", realName: 'joinTemaMultiuser自动化测试3', password: 'edcVFR']
                teamUser4 = [username: "team_user_4@${_emailDomainForTestTeamJoinMultiuser}", realName: 'joinTemaMultiuser自动化测试4', password: 'rfvBGT']
                teamUser5 = [username: "team_user_5@${_emailDomainForTestTeamJoinMultiuser}", realName: 'joinTemaMultiuser自动化测试5', password: 'tgbNHY']
                teamUser6 = [username: "team_user_6@${_emailDomainForTestTeamJoinMultiuser}", realName: 'joinTemaMultiuser自动化测试6', password: 'yhnMJU']
                teamUser7 = [username: "team_user_7@${_emailDomainForTestTeamJoinMultiuser}", realName: 'joinTemaMultiuser自动化测试7', password: 'yhnMJU']
                teamUser8 = [username: "team_user_8@${_emailDomainForTestTeamJoinMultiuser}", realName: 'joinTemaMultiuser自动化测试8', password: 'qazXSW']
                teamUser9 = [username: "team_user_9@${_emailDomainForTestTeamJoinMultiuser}", realName: 'joinTemaMultiuser自动化测试9', password: 'wsxCDE']
                teamUser10 = [username: "team_user_10@${_emailDomainForTestTeamJoinMultiuser}", realName: 'joinTemaMultiuser自动化测试10', password: 'edcVFR']
                teamUser11 = [username: "team_user_11@${_emailDomainForTestTeamJoinMultiuser}", realName: 'joinTemaMultiuser自动化测试11', password: 'rfvBGT']
                teamUser12 = [username: "team_user_12@${_emailDomainForTestTeamJoinMultiuser}", realName: 'joinTemaMultiuser自动化测试12', password: 'tgbNHY']
                teamUser13 = [username: "team_user_13@${_emailDomainForTestTeamJoinMultiuser}", realName: 'joinTemaMultiuser自动化测试13', password: 'yhnMJU']
                teamUser14 = [username: "team_user_14@${_emailDomainForTestTeamJoinMultiuser}", realName: 'joinTemaMultiuser自动化测试14', password: 'yhnMJU']
                team1ForCreate = [name: '自动化测试团队teamJoinMultiuser-A', phoneNumber: "${_phoneBaseForTestTeamJoinMultiuser + 1}"]
                team2ForCreate = [name: '自动化测试团队teamJoinMultiuser-B', phoneNumber: "${_phoneBaseForTestTeamJoinMultiuser + 2}"]
                team3ForCreate = [name: '自动化测试团队teamJoinMultiuser-C', phoneNumber: "${_phoneBaseForTestTeamJoinMultiuser + 3}"]
                team4ForCreate = [name: '自动化测试团队teamJoinMultiuser-D', phoneNumber: "${_phoneBaseForTestTeamJoinMultiuser + 4}"]
                team5ForCreate = [name: '自动化测试团队teamJoinMultiuser-E', phoneNumber: "${_phoneBaseForTestTeamJoinMultiuser + 5}"]
                team6ForCreate = [name: '自动化测试团队teamJoinMultiuser-F', phoneNumber: "${_phoneBaseForTestTeamJoinMultiuser + 6}"]
                team7ForCreate = [name: '自动化测试团队teamJoinMultiuser-G', phoneNumber: "${_phoneBaseForTestTeamJoinMultiuser + 7}"]
                team8ForCreate = [name: '自动化测试团队teamJoinMultiuser-H', phoneNumber: "${_phoneBaseForTestTeamJoinMultiuser + 8}"]
                team9ForCreate = [name: '自动化测试团队teamJoinMultiuser-I', phoneNumber: "${_phoneBaseForTestTeamJoinMultiuser + 9}"]
                team10ForCreate = [name: '自动化测试团队teamJoinMultiuser-J', phoneNumber: "${_phoneBaseForTestTeamJoinMultiuser + 10}"]
                team11ForCreate = [name: '自动化测试团队teamJoinMultiuser-K', phoneNumber: "${_phoneBaseForTestTeamJoinMultiuser + 11}"]
                team12ForCreate = [name: '自动化测试团队teamJoinMultiuser-L', phoneNumber: "${_phoneBaseForTestTeamJoinMultiuser + 12}"]
                team13ForCreate = [name: '自动化测试团队teamJoinMultiuser-M', phoneNumber: "${_phoneBaseForTestTeamJoinMultiuser + 13}"]
                team14ForCreate = [name: '自动化测试团队teamJoinMultiuser-N', phoneNumber: "${_phoneBaseForTestTeamJoinMultiuser + 14}"]
            }
            teamMainUser {
                emailDomain = _emailDomainForTestTeamMainUser
                phoneBase = _phoneBaseForTestTeamMainUser
                teamUser1 = [username: "team_user_1@${_emailDomainForTestTeamMainUser}", realName: 'joinTemaMultiuser自动化测试1', password: 'qazXSW']
                teamUser2 = [username: "team_user_2@${_emailDomainForTestTeamMainUser}", realName: 'joinTemaMultiuser自动化测试2', password: 'wsxCDE']
                teamUser3 = [username: "team_user_3@${_emailDomainForTestTeamMainUser}", realName: 'joinTemaMultiuser自动化测试3', password: 'edcVFR']
                teamUser4 = [username: "team_user_4@${_emailDomainForTestTeamMainUser}", realName: 'joinTemaMultiuser自动化测试4', password: 'rfvBGT']
                teamUser5 = [username: "team_user_5@${_emailDomainForTestTeamMainUser}", realName: 'joinTemaMultiuser自动化测试5', password: 'tgbNHY']
                teamUser6 = [username: "team_user_6@${_emailDomainForTestTeamMainUser}", realName: 'joinTemaMultiuser自动化测试6', password: 'yhnMJU']
                teamUser7 = [username: "team_user_7@${_emailDomainForTestTeamMainUser}", realName: 'joinTemaMultiuser自动化测试7', password: 'yhnMJU']
                team1ForCreate = [name: '自动化测试团队teamMainUser-A', phoneNumber: "${_phoneBaseForTestTeamMainUser + 1}"]
                team2ForCreate = [name: '自动化测试团队teamMainUser-B', phoneNumber: "${_phoneBaseForTestTeamMainUser + 2}"]
                team3ForCreate = [name: '自动化测试团队teamMainUser-C', phoneNumber: "${_phoneBaseForTestTeamMainUser + 3}"]
                team4ForCreate = [name: '自动化测试团队teamMainUser-D', phoneNumber: "${_phoneBaseForTestTeamMainUser + 4}"]
                team5ForCreate = [name: '自动化测试团队teamMainUser-E', phoneNumber: "${_phoneBaseForTestTeamMainUser + 5}"]
                team6ForCreate = [name: '自动化测试团队teamMainUser-F', phoneNumber: "${_phoneBaseForTestTeamMainUser + 6}"]
                team7ForCreate = [name: '自动化测试团队teamMainUser-G', phoneNumber: "${_phoneBaseForTestTeamMainUser + 7}"]
                team8ForCreate = [name: '自动化测试团队teamMainUser-H', phoneNumber: "${_phoneBaseForTestTeamMainUser + 8}"]
            }
            teamSwitchUser {
                emailDomain = _emailDomainForTestTeamSwitchUser
                phoneBase = _phoneBaseForTestTeamSwitchUser
                teamUser1 = [username: "team_user_1@${_emailDomainForTestTeamSwitchUser}", realName: 'joinTemaMultiuser自动化测试1', password: 'qazXSW']
                teamUser2 = [username: "team_user_2@${_emailDomainForTestTeamSwitchUser}", realName: 'joinTemaMultiuser自动化测试2', password: 'wsxCDE']
                teamUser3 = [username: "team_user_3@${_emailDomainForTestTeamSwitchUser}", realName: 'joinTemaMultiuser自动化测试3', password: 'edcVFR']
                teamUser4 = [username: "team_user_4@${_emailDomainForTestTeamSwitchUser}", realName: 'joinTemaMultiuser自动化测试4', password: 'rfvBGT']
                teamUser5 = [username: "team_user_5@${_emailDomainForTestTeamSwitchUser}", realName: 'joinTemaMultiuser自动化测试5', password: 'tgbNHY']
                teamUser6 = [username: "team_user_6@${_emailDomainForTestTeamSwitchUser}", realName: 'joinTemaMultiuser自动化测试6', password: 'yhnMJU']
                teamUser7 = [username: "team_user_7@${_emailDomainForTestTeamSwitchUser}", realName: 'joinTemaMultiuser自动化测试7', password: 'yhnMJU']
                team1ForCreate = [name: '自动化测试团队teamSwitchUser-A', phoneNumber: "${_phoneBaseForTestTeamSwitchUser + 1}"]
                team2ForCreate = [name: '自动化测试团队teamSwitchUser-B', phoneNumber: "${_phoneBaseForTestTeamSwitchUser + 2}"]
                team3ForCreate = [name: '自动化测试团队teamSwitchUser-C', phoneNumber: "${_phoneBaseForTestTeamSwitchUser + 3}"]
                team4ForCreate = [name: '自动化测试团队teamSwitchUser-D', phoneNumber: "${_phoneBaseForTestTeamSwitchUser + 4}"]
                team5ForCreate = [name: '自动化测试团队teamSwitchUser-E', phoneNumber: "${_phoneBaseForTestTeamSwitchUser + 5}"]
                team6ForCreate = [name: '自动化测试团队teamSwitchUser-F', phoneNumber: "${_phoneBaseForTestTeamSwitchUser + 6}"]
                team7ForCreate = [name: '自动化测试团队teamSwitchUser-G', phoneNumber: "${_phoneBaseForTestTeamSwitchUser + 7}"]
                team8ForCreate = [name: '自动化测试团队teamSwitchUser-H', phoneNumber: "${_phoneBaseForTestTeamSwitchUser + 8}"]
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
                teamForJoin = [name: '自动化测试团队teamJoin-A', phoneNumber: '13810360752']
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
                teamForCreate = [name: "自动化测试团队A", phoneNumber: '13810360752']
                //  userForInviteTeam所创建的团队
                teamForAnother = [name: "自动化测试团队C", phoneNumber: '13810360753']
            }
        }
    }

    beta {
        baseUrl = "http://beta.rishiqing.com/"
        path = "task/"
        dataSource {
            url = "${_betaProps.url}"
            username = "${_betaProps.username}"
            password = "${_betaProps.password}"
            driverClassName = "${_betaProps.driverClassName}"
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
            login {
                realAdmin = [username: "admin@rishiqing.com", password: "123456"]
                fakeAdmin = [username: "adminxxx", password: "123456"]
                fakeAdmin2 = [username: "admin", password: "fakepassword"]
                fakeCommonUser = [username: "zhaoyun@rishiqing.com", password: "13141234123"]
            }
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
            teamJoinMultiUser {
                emailDomain = _emailDomainForTestTeamJoinMultiuser
                phoneBase = _phoneBaseForTestTeamJoinMultiuser
                teamUser1 = [username: "team_user_1@${_emailDomainForTestTeamJoinMultiuser}", realName: 'joinTemaMultiuser自动化测试1', password: 'qazXSW']
                teamUser2 = [username: "team_user_2@${_emailDomainForTestTeamJoinMultiuser}", realName: 'joinTemaMultiuser自动化测试2', password: 'wsxCDE']
                teamUser3 = [username: "team_user_3@${_emailDomainForTestTeamJoinMultiuser}", realName: 'joinTemaMultiuser自动化测试3', password: 'edcVFR']
                teamUser4 = [username: "team_user_4@${_emailDomainForTestTeamJoinMultiuser}", realName: 'joinTemaMultiuser自动化测试4', password: 'rfvBGT']
                teamUser5 = [username: "team_user_5@${_emailDomainForTestTeamJoinMultiuser}", realName: 'joinTemaMultiuser自动化测试5', password: 'tgbNHY']
                teamUser6 = [username: "team_user_6@${_emailDomainForTestTeamJoinMultiuser}", realName: 'joinTemaMultiuser自动化测试6', password: 'yhnMJU']
                teamUser7 = [username: "team_user_7@${_emailDomainForTestTeamJoinMultiuser}", realName: 'joinTemaMultiuser自动化测试7', password: 'yhnMJU']
                teamUser8 = [username: "team_user_8@${_emailDomainForTestTeamJoinMultiuser}", realName: 'joinTemaMultiuser自动化测试8', password: 'qazXSW']
                teamUser9 = [username: "team_user_9@${_emailDomainForTestTeamJoinMultiuser}", realName: 'joinTemaMultiuser自动化测试9', password: 'wsxCDE']
                teamUser10 = [username: "team_user_10@${_emailDomainForTestTeamJoinMultiuser}", realName: 'joinTemaMultiuser自动化测试10', password: 'edcVFR']
                teamUser11 = [username: "team_user_11@${_emailDomainForTestTeamJoinMultiuser}", realName: 'joinTemaMultiuser自动化测试11', password: 'rfvBGT']
                teamUser12 = [username: "team_user_12@${_emailDomainForTestTeamJoinMultiuser}", realName: 'joinTemaMultiuser自动化测试12', password: 'tgbNHY']
                teamUser13 = [username: "team_user_13@${_emailDomainForTestTeamJoinMultiuser}", realName: 'joinTemaMultiuser自动化测试13', password: 'yhnMJU']
                teamUser14 = [username: "team_user_14@${_emailDomainForTestTeamJoinMultiuser}", realName: 'joinTemaMultiuser自动化测试14', password: 'yhnMJU']
                team1ForCreate = [name: '自动化测试团队teamJoinMultiuser-A', phoneNumber: "${_phoneBaseForTestTeamJoinMultiuser + 1}"]
                team2ForCreate = [name: '自动化测试团队teamJoinMultiuser-B', phoneNumber: "${_phoneBaseForTestTeamJoinMultiuser + 2}"]
                team3ForCreate = [name: '自动化测试团队teamJoinMultiuser-C', phoneNumber: "${_phoneBaseForTestTeamJoinMultiuser + 3}"]
                team4ForCreate = [name: '自动化测试团队teamJoinMultiuser-D', phoneNumber: "${_phoneBaseForTestTeamJoinMultiuser + 4}"]
                team5ForCreate = [name: '自动化测试团队teamJoinMultiuser-E', phoneNumber: "${_phoneBaseForTestTeamJoinMultiuser + 5}"]
                team6ForCreate = [name: '自动化测试团队teamJoinMultiuser-F', phoneNumber: "${_phoneBaseForTestTeamJoinMultiuser + 6}"]
                team7ForCreate = [name: '自动化测试团队teamJoinMultiuser-G', phoneNumber: "${_phoneBaseForTestTeamJoinMultiuser + 7}"]
                team8ForCreate = [name: '自动化测试团队teamJoinMultiuser-H', phoneNumber: "${_phoneBaseForTestTeamJoinMultiuser + 8}"]
                team9ForCreate = [name: '自动化测试团队teamJoinMultiuser-I', phoneNumber: "${_phoneBaseForTestTeamJoinMultiuser + 9}"]
                team10ForCreate = [name: '自动化测试团队teamJoinMultiuser-J', phoneNumber: "${_phoneBaseForTestTeamJoinMultiuser + 10}"]
                team11ForCreate = [name: '自动化测试团队teamJoinMultiuser-K', phoneNumber: "${_phoneBaseForTestTeamJoinMultiuser + 11}"]
                team12ForCreate = [name: '自动化测试团队teamJoinMultiuser-L', phoneNumber: "${_phoneBaseForTestTeamJoinMultiuser + 12}"]
                team13ForCreate = [name: '自动化测试团队teamJoinMultiuser-M', phoneNumber: "${_phoneBaseForTestTeamJoinMultiuser + 13}"]
                team14ForCreate = [name: '自动化测试团队teamJoinMultiuser-N', phoneNumber: "${_phoneBaseForTestTeamJoinMultiuser + 14}"]
            }
            teamMainUser {
                emailDomain = _emailDomainForTestTeamMainUser
                phoneBase = _phoneBaseForTestTeamMainUser
                teamUser1 = [username: "team_user_1@${_emailDomainForTestTeamMainUser}", realName: 'joinTemaMultiuser自动化测试1', password: 'qazXSW']
                teamUser2 = [username: "team_user_2@${_emailDomainForTestTeamMainUser}", realName: 'joinTemaMultiuser自动化测试2', password: 'wsxCDE']
                teamUser3 = [username: "team_user_3@${_emailDomainForTestTeamMainUser}", realName: 'joinTemaMultiuser自动化测试3', password: 'edcVFR']
                teamUser4 = [username: "team_user_4@${_emailDomainForTestTeamMainUser}", realName: 'joinTemaMultiuser自动化测试4', password: 'rfvBGT']
                teamUser5 = [username: "team_user_5@${_emailDomainForTestTeamMainUser}", realName: 'joinTemaMultiuser自动化测试5', password: 'tgbNHY']
                teamUser6 = [username: "team_user_6@${_emailDomainForTestTeamMainUser}", realName: 'joinTemaMultiuser自动化测试6', password: 'yhnMJU']
                teamUser7 = [username: "team_user_7@${_emailDomainForTestTeamMainUser}", realName: 'joinTemaMultiuser自动化测试7', password: 'yhnMJU']
                team1ForCreate = [name: '自动化测试团队teamMainUser-A', phoneNumber: "${_phoneBaseForTestTeamMainUser + 1}"]
                team2ForCreate = [name: '自动化测试团队teamMainUser-B', phoneNumber: "${_phoneBaseForTestTeamMainUser + 2}"]
                team3ForCreate = [name: '自动化测试团队teamMainUser-C', phoneNumber: "${_phoneBaseForTestTeamMainUser + 3}"]
                team4ForCreate = [name: '自动化测试团队teamMainUser-D', phoneNumber: "${_phoneBaseForTestTeamMainUser + 4}"]
                team5ForCreate = [name: '自动化测试团队teamMainUser-E', phoneNumber: "${_phoneBaseForTestTeamMainUser + 5}"]
                team6ForCreate = [name: '自动化测试团队teamMainUser-F', phoneNumber: "${_phoneBaseForTestTeamMainUser + 6}"]
                team7ForCreate = [name: '自动化测试团队teamMainUser-G', phoneNumber: "${_phoneBaseForTestTeamMainUser + 7}"]
                team8ForCreate = [name: '自动化测试团队teamMainUser-H', phoneNumber: "${_phoneBaseForTestTeamMainUser + 8}"]
            }
            teamSwitchUser {
                emailDomain = _emailDomainForTestTeamSwitchUser
                phoneBase = _phoneBaseForTestTeamSwitchUser
                teamUser1 = [username: "team_user_1@${_emailDomainForTestTeamSwitchUser}", realName: 'joinTemaMultiuser自动化测试1', password: 'qazXSW']
                teamUser2 = [username: "team_user_2@${_emailDomainForTestTeamSwitchUser}", realName: 'joinTemaMultiuser自动化测试2', password: 'wsxCDE']
                teamUser3 = [username: "team_user_3@${_emailDomainForTestTeamSwitchUser}", realName: 'joinTemaMultiuser自动化测试3', password: 'edcVFR']
                teamUser4 = [username: "team_user_4@${_emailDomainForTestTeamSwitchUser}", realName: 'joinTemaMultiuser自动化测试4', password: 'rfvBGT']
                teamUser5 = [username: "team_user_5@${_emailDomainForTestTeamSwitchUser}", realName: 'joinTemaMultiuser自动化测试5', password: 'tgbNHY']
                teamUser6 = [username: "team_user_6@${_emailDomainForTestTeamSwitchUser}", realName: 'joinTemaMultiuser自动化测试6', password: 'yhnMJU']
                teamUser7 = [username: "team_user_7@${_emailDomainForTestTeamSwitchUser}", realName: 'joinTemaMultiuser自动化测试7', password: 'yhnMJU']
                team1ForCreate = [name: '自动化测试团队teamSwitchUser-A', phoneNumber: "${_phoneBaseForTestTeamSwitchUser + 1}"]
                team2ForCreate = [name: '自动化测试团队teamSwitchUser-B', phoneNumber: "${_phoneBaseForTestTeamSwitchUser + 2}"]
                team3ForCreate = [name: '自动化测试团队teamSwitchUser-C', phoneNumber: "${_phoneBaseForTestTeamSwitchUser + 3}"]
                team4ForCreate = [name: '自动化测试团队teamSwitchUser-D', phoneNumber: "${_phoneBaseForTestTeamSwitchUser + 4}"]
                team5ForCreate = [name: '自动化测试团队teamSwitchUser-E', phoneNumber: "${_phoneBaseForTestTeamSwitchUser + 5}"]
                team6ForCreate = [name: '自动化测试团队teamSwitchUser-F', phoneNumber: "${_phoneBaseForTestTeamSwitchUser + 6}"]
                team7ForCreate = [name: '自动化测试团队teamSwitchUser-G', phoneNumber: "${_phoneBaseForTestTeamSwitchUser + 7}"]
                team8ForCreate = [name: '自动化测试团队teamSwitchUser-H', phoneNumber: "${_phoneBaseForTestTeamSwitchUser + 8}"]
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
                teamForJoin = [name: '自动化测试团队teamJoin-A', phoneNumber: '13810360752']
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
                teamForCreate = [name: "自动化测试团队A", phoneNumber: '13810360752']
                //  userForInviteTeam所创建的团队
                teamForAnother = [name: "自动化测试团队C", phoneNumber: '13810360753']
            }
            teamUserData {
                emailDomain = _emailDomainForTestTeamUserData
                phoneBase = _phoneBaseForTestTeamUserData
                teamUser1 = [username: "team_user_1@${_emailDomainForTestTeamUserData}", realName: 'teamUserData自动化测试1', password: 'qazXSW']
                teamUser2 = [username: "team_user_2@${_emailDomainForTestTeamUserData}", realName: 'teamUserData自动化测试2', password: 'wsxCDE']
                teamUser3 = [username: "team_user_3@${_emailDomainForTestTeamUserData}", realName: 'teamUserData自动化测试3', password: 'edcVFR']
                teamUser4 = [username: "team_user_4@${_emailDomainForTestTeamUserData}", realName: 'teamUserData自动化测试4', password: 'rfvBGT']
                teamUser5 = [username: "team_user_5@${_emailDomainForTestTeamUserData}", realName: 'teamUserData自动化测试5', password: 'tgbNHY']
                teamUser6 = [username: "team_user_6@${_emailDomainForTestTeamUserData}", realName: 'teamUserData自动化测试6', password: 'yhnMJU']
                teamUser7 = [username: "team_user_7@${_emailDomainForTestTeamUserData}", realName: 'teamUserData自动化测试7', password: 'yhnMJU']
                team1ForCreate = [name: '自动化测试团队teamUserData-A', phoneNumber: "${_phoneBaseForTestTeamUserData + 1}"]
                team2ForCreate = [name: '自动化测试团队teamUserData-B', phoneNumber: "${_phoneBaseForTestTeamUserData + 2}"]
                team3ForCreate = [name: '自动化测试团队teamUserData-C', phoneNumber: "${_phoneBaseForTestTeamUserData + 3}"]
                team4ForCreate = [name: '自动化测试团队teamUserData-D', phoneNumber: "${_phoneBaseForTestTeamUserData + 4}"]
                team5ForCreate = [name: '自动化测试团队teamUserData-E', phoneNumber: "${_phoneBaseForTestTeamUserData + 5}"]
                team6ForCreate = [name: '自动化测试团队teamUserData-F', phoneNumber: "${_phoneBaseForTestTeamUserData + 6}"]
                team7ForCreate = [name: '自动化测试团队teamUserData-G', phoneNumber: "${_phoneBaseForTestTeamUserData + 7}"]
                team8ForCreate = [name: '自动化测试团队teamUserData-H', phoneNumber: "${_phoneBaseForTestTeamUserData + 8}"]
            }
        }
    }
}