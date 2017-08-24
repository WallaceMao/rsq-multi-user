package com.rishiqing.domain.user

class SuperUser {

    long id

    Date dateCreated
    Date lastUpdated

    User mainUser
    User defaultLoginUser

    SuperUser(User mainUser, User defaultLoginUser) {
        this.mainUser = mainUser
        this.defaultLoginUser = defaultLoginUser
    }

    static hasMany = [
            users: User
    ]

    static mapping = {
        id name: "id", column: "id"
    }

    static constraints = {
    }
}
