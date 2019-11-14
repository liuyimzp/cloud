package com.yinhai.cloud.module.application.app.controller;

import com.yinhai.cloud.core.api.vo.CommonResultVo;
import com.yinhai.cloud.module.application.api.vo.AppServiceVo;
import com.yinhai.cloud.module.application.api.vo.AppVo;
import com.yinhai.cloud.module.application.api.vo.NamespaceVo;
import com.yinhai.core.common.api.util.ValidateUtil;

import java.util.HashMap;
import java.util.Map;

public class ApplicationDuplicationCode {

    /**
     * 判断成功返回true失败返回false
     * @param vo
     * @return Map
     */
    public static Map<String,CommonResultVo> getBolServiceOne(AppServiceVo vo){
        CommonResultVo result = new CommonResultVo();
        Map map = new HashMap();
        if (ValidateUtil.isEmpty(vo.getAppId())) {
            result.setSuccess(false);
            result.setErrorMsg("应用ID不能未空!");
            map.put("result",result);
            return map;
        }
        if (ValidateUtil.isEmpty(vo.getServiceType())) {
            result.setSuccess(false);
            result.setErrorMsg("服务类型不能未空!");
            map.put("result",result);
            return map;
        }
        if (ValidateUtil.isEmpty(vo.getInnerPort())) {
            result.setSuccess(false);
            result.setErrorMsg("内部端口不能未空!");
            map.put("result",result);
            return map;
        }
        result.setSuccess(true);
        map.put("result",result);
        return map;
    }

    public static Map<String,CommonResultVo> getBolServiceTwo(AppServiceVo vo){
        CommonResultVo result = new CommonResultVo();
        Map map = new HashMap();
        if (ValidateUtil.isEmpty(vo.getMappingPort())) {
            result.setSuccess(false);
            result.setErrorMsg("外部端口不能未空!");
            map.put("result",result);
            return map;
        }
        if (vo.getMappingPort() <= 30000) {
            result.setSuccess(false);
            result.setErrorMsg("外部端口必须大于30000!");
            map.put("result",result);
            return map;
        }
        result.setSuccess(true);
        map.put("result",result);
        return map;
    }

    public static Map<String,CommonResultVo> getBolNameSpace(NamespaceVo vo) {
        CommonResultVo result = new CommonResultVo();
        Map map = new HashMap();
        if (ValidateUtil.isEmpty(vo.getNamespaceName())) {
            result.setSuccess(false);
            result.setErrorMsg("命名空间名称不能未空!");
            map.put("result",result);
            return map;
        }
        if(ValidateUtil.isEmpty(vo.getMaxMemoryLimit())){
            result.setSuccess(false);
            result.setErrorMsg("命名空间最大内存限制不能为空!");
            map.put("result",result);
            return map;
        }
        if(ValidateUtil.isEmpty(vo.getMaxCPULimit())){
            result.setSuccess(false);
            result.setErrorMsg("命名空间最大CPU限制不能为空!");
            map.put("result",result);
            return map;
        }
        result.setSuccess(true);
        map.put("result",result);
        return map;
    }
}
