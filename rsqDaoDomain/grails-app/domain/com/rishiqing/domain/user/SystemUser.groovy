package com.rishiqing.domain.user

class SystemUser extends User{

    SystemUser(String username, String password) {
        super(username, password)
    }
    static constraints = {
    }
}
