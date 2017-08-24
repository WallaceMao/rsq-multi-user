package com.demo

class UserController {
    def myUserService

    def index() {
        String str = myUserService.testText()
        render str
    }
}
