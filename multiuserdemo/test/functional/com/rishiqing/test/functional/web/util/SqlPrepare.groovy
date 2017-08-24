package com.rishiqing.test.functional.web.util

import com.rishiqing.test.functional.ConfigUtil
import sun.security.krb5.Config

/**
 * 生成用于清理测试用户的SQL
 * 如果功能测试环境可以连接到被测试的库，那么在测试前可以直接连接并执行SQL语句
 * 如果功能测试环境不能连接到被测试的库，那么在测试前需要手动执行SQL语句
 * 为了降低对数据库更新造成的风险，严禁直接使用update语句！！！
 * 清理用户时每次限值最多一次修改5个用户
 * Created by  on 2017/8/24.Wallace
 */
class SqlPrepare {

    static String genCleanEmailUsers(Map userMap){
        Date now = new Date()
        String username = userMap.email
        if(!username){
            return null
        }
        return """\
update `user` u set u.username = CONCAT(u.username,'.test${now.getTime()}.test')
where `id` in
(
  select uid as 'id' from
    (
      select i_u.id as uid from `user` i_u
      where i_u.username like '${username}'
      order by i_u.id desc limit 5
    ) tmp
);
"""
    }
    static String genCleanPhoneUsers(Map userMap){
        Date now = new Date()
        String phoneNumber = userMap.phoneNumber
        if(!phoneNumber){
            return null
        }
        return """\
update `user` u set u.username = CONCAT(u.username,'.test${now.getTime()}.test'),
u.phone_number = NULL
where `id` in
(
  select uid as 'id' from
    (
      select i_u.id as uid from `user` i_u
      where i_u.phone_number like '${phoneNumber}'
      order by i_u.id desc limit 5
    ) tmp
);
"""
    }
    static String genCleanQqUsers(Map userMap){
        Date now = new Date()
        String realName = userMap.realName
        if(!realName){
            return null
        }
        return """\
update `user` u set u.username = CONCAT(u.username,'.test${now.getTime()}.test'),
u.qq_open_id = NULL
where `id` in
(
  select uid as 'id' from
    (
      select i_u.id as uid from `user` i_u
      where i_u.real_name like '${realName}' and i_u.qq_open_id is not null
      order by i_u.id desc limit 5
    ) tmp
);
"""
    }
    static String genCleanWeixinUsers(Map userMap){
        Date now = new Date()
        String realName = userMap.realName
        if(!realName){
            return null
        }
        return """\
update `user` u set u.username = CONCAT(u.username,'.test${now.getTime()}.test'),
u.wx_open_id = NULL, u.public_wx_open_id = NULL, u.union_id = NULL
where `id` in
(
  select uid as 'id' from
    (
      select i_u.id as uid from `user` i_u
      where i_u.real_name like '${realName}' and i_u.wx_open_id is not null
      order by i_u.id desc limit 5
    ) tmp
);
"""
    }
    static String genCleanWeiboUsers(Map userMap){
        Date now = new Date()
        String realName = userMap.realName
        if(!realName){
            return null
        }
        return """\
update `user` u set u.username = CONCAT(u.username,'.test${now.getTime()}.test'),
u.sina_open_id = NULL
where `id` in
(
  select uid as 'id' from
    (
      select i_u.id as uid from `user` i_u
      where i_u.real_name like '${realName}' and i_u.sina_open_id is not null
      order by i_u.id desc limit 5
    ) tmp
);
"""
    }
    static List genCleanDingUsers(Map userMap){
        Date now = new Date()
        String realName = userMap.realName
        if(!realName){
            return null
        }
        return ["""\
update `zz_client_user_data` u set u.dd_union_id = NULL
where `id` in
(
  select uid as 'id' from
    (
      select i_u.id as uid from `user` i_u
      where i_u.real_name like '${realName}' and i_u.from_client like 'dingtalk'
      order by i_u.id desc limit 1
    ) tmp
);
""", """\
update `user` u set u.username = CONCAT(u.username,'.test${now.getTime()}.test'),
u.from_client = 'dingtalk_test',
u.outer_id = NULL
where `id` in
(
  select uid as 'id' from
    (
      select i_u.id as uid from `user` i_u
      where i_u.real_name like '${realName}' and i_u.from_client like 'dingtalk'
      order by i_u.id desc limit 1
    ) tmp
);
"""]
    }


    public static void main(String[] args) {
        System.setProperty("geb.env", "rsqbeta")
        println "# --------清理邮箱注册用户--------"
        println genCleanEmailUsers(ConfigUtil.config.users.emailUsers[0])
        println "# --------清理手机号用户--------"
        println genCleanPhoneUsers(ConfigUtil.config.users.phoneUsers[0])
        println "# --------清理QQ用户--------"
        println genCleanQqUsers(ConfigUtil.config.users.qqUsers[0])
        println "# --------清理微信用户--------"
        println genCleanWeixinUsers(ConfigUtil.config.users.weixinUsers[0])
        println "# --------清理微博用户--------"
        println genCleanWeiboUsers(ConfigUtil.config.users.weiboUsers[0])
        println "# --------清理钉钉用户--------"
        println genCleanDingUsers(ConfigUtil.config.users.dingUsers[0])[0]
        println genCleanDingUsers(ConfigUtil.config.users.dingUsers[0])[1]
        println "# ----------------"
    }
}
