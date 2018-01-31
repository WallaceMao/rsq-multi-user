package com.rishiqing.test.functional.api

import com.rishiqing.demo.util.http.RsqRestResponse
import com.rishiqing.demo.util.http.RsqRestUtil
import com.rishiqing.test.functional.ConfigUtil
import com.rishiqing.test.functional.util.common.MapUtil;

/**
 * 文集相关的api
 */
public class CorpusApi {
    private static String baseUrl = ConfigUtil.config.baseUrl
    private static String path = ConfigUtil.config.path

    public static RsqRestResponse getCorpusList(Map params = [:]){
        Map reqParams = [
                filter: 'commonSummarys',
                max: 20
        ]
        RsqRestUtil.get("${baseUrl}${path}v2/note/getCorpuses"){
            queryParams reqParams
        }
    }

    public static void checkGetCorpusList(RsqRestResponse resp, Map expected = [:]){
        assert resp.status == 200
        if(expected.corpusListLength){
            assert resp.jsonMap.list.size() == expected.corpusListLength
        }
    }

    public static RsqRestResponse createCorpus(Map params){
        RsqRestUtil.post("${baseUrl}${path}v2/note"){
            header 'X-Requested-With', 'XMLHttpRequest'
            fields params
        }
    }

    public static final void checkCreateCorpus(RsqRestResponse resp){
        assert resp.status == 200
        assert resp.json.id != null
    }

    public static final RsqRestResponse getCorpusById(Map params){
        Map reqParams = [id: params.id]
        RsqRestUtil.get("${baseUrl}${path}v2/note/"){
            queryParams reqParams
        }
    }

    public static final checkCorpus(RsqRestResponse resp, Map expected = [:]){
        assert MapUtil.compareMapValue(resp.jsonMap, expected)
    }

    public static final RsqRestResponse addCorpusMember(Map params = [:]){
        Map reqParams = [
                id: params.id,
                deptIds: params.deptIds?:'',
                memberIds: params.memberIds?:''
        ]
        if(params.members){
            reqParams.memberIds = params.members.collect{it -> it.id}.join(',')
        }
        RsqRestUtil.put("${baseUrl}${path}v2/note/${reqParams.id}"){
            fields reqParams
        }
    }

    public static final void checkAddCorpusMember(RsqRestResponse resp, expected = [:]){
        assert resp.status == 200
        if(expected.userRolesLength != null){
            resp.jsonMap.userRoles.length == expected.userRolesLength
        }
    }
}
