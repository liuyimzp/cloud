package com.yinhai.cloud.module.application.app.controller;

import com.yinhai.cloud.core.api.vo.CommonResultVo;
import com.yinhai.cloud.module.application.api.bpo.IAppBpo;
import com.yinhai.cloud.module.application.api.bpo.IConfigMapBpo;
import com.yinhai.cloud.module.application.api.util.AppUtil;
import com.yinhai.cloud.module.application.api.util.ConfigMapUtil;
import com.yinhai.cloud.module.application.api.util.sm4.SM4Utils;
import com.yinhai.cloud.module.application.api.vo.AppVo;
import com.yinhai.cloud.module.application.api.vo.ConfigMapVo;
import com.yinhai.core.app.ta3.web.controller.BaseController;
import com.yinhai.core.common.api.util.ValidateUtil;
import com.yinhai.sysframework.util.IConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.util.WebUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by tianhy on 2019/2/20.
 */
@Controller
@RequestMapping({"/configMap"})
public class ConfigMapController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(ConfigMapController.class);
    @Resource
    private IConfigMapBpo configMapBpo;
    @Resource
    private IAppBpo appBpo;

    @RequestMapping("/get.do")
    @ResponseBody
    public CommonResultVo getConfigMap(@RequestBody ConfigMapVo vo) {
        CommonResultVo result = new CommonResultVo();
        if(ValidateUtil.isEmpty(vo.getAppId())){
            result.setSuccess(false);
            result.setErrorMsg("应用ID不能为空!");
            return result;
        }
        List<ConfigMapVo> list = configMapBpo.getConfigMapByAppId(vo.getAppId());
        result.setSuccess(true);
        result.setData(list);
        return result;
    }

    @RequestMapping("/save.do")
    @ResponseBody
    public CommonResultVo saveConfigMap(@RequestBody ConfigMapVo vo){
        CommonResultVo result = new CommonResultVo();
        if(ValidateUtil.isEmpty(vo.getAppId())){
            result.setSuccess(false);
            result.setErrorMsg("应用ID不能为空!");
            return result;
        }
        if(ValidateUtil.isEmpty(vo.getPropertyName())){
            result.setSuccess(false);
            result.setErrorMsg("属性名称不能为空!");
            return result;
        }
        if(ValidateUtil.isEmpty(vo.getPropertyValue())){
            result.setSuccess(false);
            result.setErrorMsg("属性值不能为空!");
            return result;
        }
        ConfigMapVo configMapVo = configMapBpo.saveConfigMap(vo);
        result.setSuccess(true);
        result.setData(configMapVo);
        return result;
    }

    @RequestMapping("/edit.do")
    @ResponseBody
    public CommonResultVo editConfigMap(@RequestBody ConfigMapVo vo){
        CommonResultVo result = new CommonResultVo();
        if(ValidateUtil.isEmpty(vo.getId())){
            result.setSuccess(false);
            result.setErrorMsg("配置ID不能为空!");
            return result;
        }
        if(ValidateUtil.isEmpty(vo.getPropertyValue())){
            result.setSuccess(false);
            result.setErrorMsg("属性值不能为空!");
            return result;
        }
        configMapBpo.editConfigMap(vo);
        result.setSuccess(true);
        return result;
    }

    @RequestMapping("/remove.do")
    @ResponseBody
    public CommonResultVo removeConfigMap(@RequestBody ConfigMapVo vo){
        CommonResultVo result = new CommonResultVo();
        if(ValidateUtil.isEmpty(vo.getId())){
            result.setSuccess(false);
            result.setErrorMsg("配置ID不能为空!");
            return result;
        }
        configMapBpo.removeConfigMap(vo.getId());
        result.setSuccess(true);
        return result;
    }

    @RequestMapping("/show.do")
    @ResponseBody
    public CommonResultVo showConfigMap(@RequestBody ConfigMapVo vo){
        CommonResultVo result = new CommonResultVo();
        if(ValidateUtil.isEmpty(vo.getAppId())){
            result.setSuccess(false);
            result.setErrorMsg("配置ID不能为空!");
            return result;
        }
        AppVo appVo = appBpo.getApp(vo.getAppId());
        List<ConfigMapVo> list = configMapBpo.getConfigMapByAppId(vo.getAppId());
        result.setData(ConfigMapUtil.showConfigMap(appVo,list));
        result.setSuccess(true);
        return result;
    }

    @RequestMapping("/create.do")
    @ResponseBody
    public CommonResultVo createConfigMap(@RequestBody ConfigMapVo vo){
        CommonResultVo result = new CommonResultVo();
        if(ValidateUtil.isEmpty(vo.getAppId())){
            result.setSuccess(false);
            result.setErrorMsg("配置ID不能为空!");
            return result;
        }
        AppVo appVo = appBpo.getApp(vo.getAppId());
        List<ConfigMapVo> list = configMapBpo.getConfigMapByAppId(vo.getAppId());
        try{
            ConfigMapUtil.applyConfigMap(appVo,list);
        }catch (Exception e){
            result.setSuccess(false);
            result.setErrorMsg("创建失败:"+e.getMessage());
            return result;
        }
        result.setSuccess(true);
        return result;
    }

    /**
     * 下载全局变量
     * @param id
     * @param response
     * @return
     */
    @RequestMapping("/downloadConfigMap.do")
    @ResponseBody
    public String downloadConfigMap(Long id, HttpServletResponse response){
        AppVo appVo = appBpo.getApp(id);
        response.setCharacterEncoding("utf-8");
        response.setContentType("APPLICATION/OCTET-STREAM");
        response.setHeader("Content-Disposition", "attachment; filename=" + appVo.getAppName() + "ConfigMap.properties");
        List<ConfigMapVo> list = configMapBpo.getConfigMapByAppId(id);
        String str = AppUtil.getStringToList(list);
        try(OutputStream os = response.getOutputStream(); InputStream is = new ByteArrayInputStream(str.getBytes())){
            byte[] b = new byte[2048];
            int length;
            while ((length = is.read(b)) > 0){
                os.write(b,0,length);
            }
            os.flush();
        } catch (Exception e) {
            logger.error(logger.getName() + "context",e);
        }
        return null;
    }

    @RequestMapping("/appConfigUpload.do")
    @ResponseBody
    public CommonResultVo appConfigUpload(HttpServletRequest req) {
        CommonResultVo result = new CommonResultVo();
        Map<String,String[]> map = req.getParameterMap();
        String[] aa = map.get("id");
        if (ValidateUtil.isEmpty(map)) {
            result.setSuccess(false);
            result.setErrorMsg("参数为空!");
            return result;
        }
        String contentType = req.getContentType();
        if (contentType != null && contentType.toLowerCase().startsWith("multipart/")) {
            MultipartHttpServletRequest multipartRequest =
                    WebUtils.getNativeRequest(req, MultipartHttpServletRequest.class);
            multipartRequest.getMultiFileMap();
            MultipartFile file = multipartRequest.getFile("file");
            if (ValidateUtil.isEmpty(file)) {
                result.setSuccess(false);
                result.setErrorMsg("上传文件为空!");
                return result;
            }
            try {
                String reqStr = new String(file.getBytes(), "UTF-8");
                String errStr = AppUtil.setAppConfig(reqStr,Long.valueOf(aa[0]));
                if (ValidateUtil.isNotEmpty(errStr)){
                    result.setErrorMsg(errStr);
                }
            } catch (Exception e) {
                result.setSuccess(false);
                result.setErrorMsg(e.getMessage());
                return result;
            }
        }
        result.setSuccess(true);
        return result;
    }
}
