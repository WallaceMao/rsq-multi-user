package com.rishiqing.test.functional.util

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

    static Map genRandomPlan(){
        Date now = new Date()
        [
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
   }
}
