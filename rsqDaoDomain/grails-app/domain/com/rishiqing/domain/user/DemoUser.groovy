package com.rishiqing.domain.user

class DemoUser {

//    long id
    String name
    int age
    String address
    long number = 0L

    Date dateCreated
    Date lastUpdated

    DemoUser(String name, int age) {
        this.name = name
        this.age = age
    }
//    static mapping = {
//        dateCreated defaultValue: new Date()
//        lastUpdated defaultValue: new Date()
//    }
    static constraints = {
        name nullable: true
        age nullable: true
        address nullable: true
    }
}
