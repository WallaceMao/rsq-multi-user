package com.demo

class RequestmapController {

    def springSecurityService

    def clearCache() {
        springSecurityService.clearCachedRequestmaps()
        render "cleared--"
    }

    def test(){
        render "test action in requestmap controller"
    }
}
