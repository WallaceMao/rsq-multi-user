package com.rishiqing.test.functional.api

import com.rishiqing.demo.util.http.RsqRestResponse
import com.rishiqing.demo.util.http.RsqRestUtil
import com.rishiqing.test.functional.ConfigUtil
import com.rishiqing.test.functional.util.common.MapUtil;

/**
 * 计划的api
 */
public class PlanApi {
    private static String baseUrl = ConfigUtil.config.baseUrl
    private static String path = ConfigUtil.config.path

    public static RsqRestResponse getPlanGroupList(Map params = [:]){
        Map reqParams = [
                selectGroupId: params.selectGroupId?:'all'
        ]
        RsqRestUtil.get("${baseUrl}${path}v2/kanbanGroup/details"){
            queryParams reqParams
        }
    }

    public static void checkGetPlanGroupList(RsqRestResponse resp, Map expected = [:]){
        assert resp.status == 200
        if(expected.planListLength){
            assert resp.jsonMap.list.size() == expected.planListLength
        }
    }

    public static RsqRestResponse createPlan(Map planParams){
        RsqRestUtil.post("${baseUrl}${path}v2/kanbans/"){
            header 'X-Requested-With', 'XMLHttpRequest'
            fields planParams
        }
    }

    public static final void checkCreatePlan(RsqRestResponse resp){
        assert resp.status == 200
        assert resp.json.id != null
    }

    public static final RsqRestResponse getPlanById(Map params){
        Map reqParams = [id: params.id]
        RsqRestUtil.get("${baseUrl}${path}v2/kanbans/"){
            queryParams reqParams
        }
    }

    public static final void checkPlan(RsqRestResponse resp, Map expectedPlan = [:]){
        assert MapUtil.compareMapValue(resp.jsonMap, expectedPlan)
    }

    public static final RsqRestResponse addPlanMember(Map params = [:]){
        Map reqParams = [
                id: params.id,
                deptIds: params.deptIds?:'',
                accessIds: params.accessIds?:''
        ]
        if(params.members){
            reqParams.accessIds = params.members.collect{it -> it.id}.join(',')
        }
        RsqRestUtil.put("${baseUrl}${path}v2/kanbans/${reqParams.id}"){
            fields reqParams
        }
    }

    public static final void checkAddPlanMember(RsqRestResponse resp, expected = [:]){
        assert resp.status == 200
        if(expected.userRolesLength != null){
            resp.jsonMap.userRoles.length == expected.userRolesLength
        }
    }
}
