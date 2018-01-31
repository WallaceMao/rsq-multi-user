package com.rishiqing.test.functional.util.db

/**
 * Created by  on 2017/8/30.Wallace
 */
class DomainUtil {
    /**
     * 随机生成一个用户的日程，用于功能测试
     * @param userId
     * @return
     */
    static Map genRandomTodo(long userId){
        Date now = new Date()
        [
                "createTaskDate":"${now.format('yyyyMMdd')}",
                "pTitle":"test todo ${now.getTime()}",
                "pDisplayOrder":65535,
                "pContainer":"IE",
                "startDate":"${now.format('yyyy/MM/dd')}",
                "endDate":"${now.format('yyyy/MM/dd')}",
                "clock":{},
                "receiverIds":"${userId}",
                "checkAuthority":"public",
                "todoLabelIds":"",
                "pNote":"",
                "pIsDone":false,
                "pFinishedTime":null,
                "pPlanedTime":"${now.format('yyyy-MM-dd HH:mm:ss')}",
                "senderId":null,
                "hasAvatar":null,
                "systemAvatar":null,
                "isDeleted":false,
                "kanbanItem":null,
                "todoDeployId":null,
                "noteFile":[],
                "allDoneSubTodosCount":0,
                "allSubTodosCount":0,
                "receiverUser":[],
                "KSLList":[],
                "TSLList":[],
                "allKList":[],
                "allTList":[],
                "subTodos":[],
                "comments":[],
                "noteFiles":[]
        ]
    }
    /**
     * 随机生成一个计划
     * @param params
     * @return
     */
    static Map genRandomPlan(Map params = null){
        Date now = new Date()
        Map resultMap = [
                "name":"test plan ${now.getTime()}",
                "attribute":"company",
                "backgroundImage":"",
                "isCreate":true,
                "isArchived":false,
                "isTeam":true,
                "isStarMark":false,
                "templateId":1,
                "isDefault":false,
                "starMark":false,
                "isLoaded":false,
                "editAuthority":"all",
                "isKanban":true,
                "processLoad":false,
                "userIdAry":[],
                "childKanbanList":[],
                "kanbanCardList":[],
                "kanbanAllKList":[],
                "kanbanAllTList":[],
                "userRoles":[]
                ]
        params?.each {k, v ->
            resultMap[k] = v
        }
        resultMap
    }
    static Map genRandomCorpus(Map params = null){
        Date now = new Date()
        Map resultMap = [
            "name": "test corpus ${now.getTime()}",
            "type": "essays",
            "cover": "https://images.timetask.cn/cover/default/corpus_v1/cover-default-8.png",
            "displayOrder": 65535,
            "isStar": false,
            "attribute": "person",
            "memberIds": 50,
            "position": "bottom",
            "userCorpus": [],
            "isDefault": false,
            "editControl": [:],
            "lastSummaryAuthority": [:],
            "allowEditBeforeDoc": true,
            "allowAddBeforeDoc": true,
            "mineSummarys": [],
            "commonSummarys": [],
            "atMeSummarys": [],
            "userRoles": [],
            "templateSetting": [
                "summaryTemplate": []
            ],
            "_viewType_block": [
                "sortType": null,
                "allowEditBeforeDoc": true,
                "allowAddBeforeDoc": true,
                "selectedDocItem": null,
                "date": null,
                "creator": null,
                "dept": null,
                "parentId": null
            ],
            "_viewType_list": [
                "sortType": null,
                "allowEditBeforeDoc": true,
                "allowAddBeforeDoc": true,
                "selectedDocItem": null,
                "date": null,
                "creator": null,
                "dept": null,
                "parentId": null
            ],
            "_viewType_sidebar": [
                "sortType": null,
                "allowEditBeforeDoc": true,
                "allowAddBeforeDoc": true,
                "selectedDocItem": null,
                "date": null,
                "creator": null,
                "dept": null,
                "parentId": null
            ]
        ]
        params?.each {k, v ->
            resultMap[k] = v
        }
        resultMap
    }
}
