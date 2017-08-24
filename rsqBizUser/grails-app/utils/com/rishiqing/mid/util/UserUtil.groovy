package com.rishiqing.mid.util

import com.rishiqing.domain.user.CommonUser
import com.rishiqing.domain.user.User

/**
 * Created by  on 2017/7/31.Wallace
 */
class UserUtil {
    public static User generateRandomUser(){
        long dateNum = new Date().getTime()
        User randomUser = new CommonUser("randomUser${dateNum}@random.com", "randomUser", "随机用户${dateNum}")
        return randomUser;
    }
}
