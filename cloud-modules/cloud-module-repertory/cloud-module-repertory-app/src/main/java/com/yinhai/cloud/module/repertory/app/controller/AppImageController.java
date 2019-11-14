package com.yinhai.cloud.module.repertory.app.controller;

import com.yinhai.cloud.core.api.constant.ConfigPropKey;
import com.yinhai.cloud.core.api.entity.MsgVO;
import com.yinhai.cloud.core.api.exec.ShellUid;
import com.yinhai.cloud.core.api.util.SystemConfigUtil;
import com.yinhai.cloud.core.api.vo.CommonResultVo;
import com.yinhai.cloud.core.api.vo.ConnVo;
import com.yinhai.cloud.module.application.api.bpo.IAppConfigBpo;
import com.yinhai.cloud.module.repertory.api.bpo.IAppGroupBpo;
import com.yinhai.cloud.module.repertory.api.bpo.IAppImageBpo;
import com.yinhai.cloud.module.repertory.api.bpo.IAppRepertoryBpo;
import com.yinhai.cloud.module.repertory.api.vo.*;
import com.yinhai.cloud.module.resource.constants.ServerCmdConstant;
import com.yinhai.cloud.module.resource.util.NodeUtils;
import com.yinhai.cloud.module.shell.util.ShellExecUtil;
import com.yinhai.cloud.module.shell.vo.ExecShell;
import com.yinhai.core.app.ta3.web.controller.BaseController;
import com.yinhai.core.common.api.base.IPage;
import com.yinhai.core.common.api.util.ValidateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.util.WebUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by tianhy on 2018/5/17.
 * 应用镜像管理
 */
@Controller
@RequestMapping({"/appImage"})
public class AppImageController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(AppImageController.class);

    @Resource
    private IAppImageBpo appImageBpo;

    @Resource
    private IAppRepertoryBpo appRepertoryBpo;

    @Resource
    private IAppConfigBpo appConfigBpo;

    @Resource
    private IAppGroupBpo appGroupBpo;

    public static final Object SYN_LOCK = "1";

    /**
     * 发送GET请求
     *
     * @param path     请求路径
     * @param params   请求参数
     * @param encoding 编码
     * @return 请求是否成功
     */
    private static boolean sendGETRequest(String path, Map<String, String> params, String encoding) throws Exception {
        StringBuilder url = new StringBuilder(path);
        if (!ValidateUtil.isEmpty(params) && params.size() != 0) {
            url.append("?");
            for (Map.Entry<String, String> entry : params.entrySet()) {
                url.append(entry.getKey()).append("=");
                url.append(URLEncoder.encode(entry.getValue(), encoding));
                url.append("&");
            }
            url.deleteCharAt(url.length() - 1);
        }
        HttpURLConnection conn = (HttpURLConnection) new URL(url.toString()).openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        if (conn.getResponseCode() == 200) {
            return true;
        }
        return false;
    }

    @RequestMapping("/getAll.do")
    @ResponseBody
    public CommonResultVo getAllAppImages(@RequestBody AppImageQueryVo vo) {
        CommonResultVo result = new CommonResultVo();
        if (ValidateUtil.isEmpty(vo.getRepertoryId())) {
            result.setSuccess(false);
            result.setErrorMsg("应用仓库ID为空!");
            return result;
        }
        result.setSuccess(true);
        appImageBpo.syncDockerRepertory(vo);
        IPage<AppImageVo> page = appImageBpo.getQueryAppImages(vo);
        List<Map> list2 = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (AppImageVo appImageVo : page.getList()) {
            try {
                Map map = appImageVo.toMap();
                map.put("createTime", sdf.format(appImageVo.getCreateTime()));
                list2.add(map);
            } catch (Exception e) {
                logger.error(logger.getName() + "context",e);
            }
        }
        Map map = new HashMap();
        map.put("list", list2);
        map.put("total", page.getTotal());
        result.setData(map);
        return result;
    }

    @RequestMapping("/upload.do")
    @ResponseBody
    public CommonResultVo upload(HttpServletRequest request) {
        CommonResultVo result = new CommonResultVo();
        Map map = request.getParameterMap();
        if (ValidateUtil.isEmpty(map)) {
            result.setSuccess(false);
            result.setErrorMsg("参数为空!");
            return result;
        }
        if (ValidateUtil.isEmpty(map.get("repertoryId"))) {
            result.setSuccess(false);
            result.setErrorMsg("应用仓库ID为空!");
            return result;
        }
        Long repertoryId = Long.valueOf(((String[]) map.get("repertoryId"))[0]);
        if (ValidateUtil.isEmpty(map.get("version"))) {
            result.setSuccess(false);
            result.setErrorMsg("版本号为空!");
            return result;
        }
        String version = ((String[]) map.get("version"))[0];
        if (ValidateUtil.isEmpty(map.get("fileType"))) {
            result.setSuccess(false);
            result.setErrorMsg("文件类型为空!");
            return result;
        }
        if (appImageBpo.checkVersionExist(repertoryId, version)) {
            result.setSuccess(false);
            result.setErrorMsg("该版本号已存在!");
            return result;
        }
        AppRepertoryVo appRepertoryVo = appRepertoryBpo.getAppRepertory(repertoryId);
        String contentType = request.getContentType();
        if (contentType != null && contentType.toLowerCase().startsWith("multipart/")) {
            MultipartHttpServletRequest multipartRequest =
                    WebUtils.getNativeRequest(request, MultipartHttpServletRequest.class);
            multipartRequest.getMultiFileMap();
            MultipartFile file = multipartRequest.getFile("file");
            if (ValidateUtil.isEmpty(file)) {
                result.setSuccess(false);
                result.setErrorMsg("上传文件为空!");
                return result;
            }
            Date date = new Date();
            ConnVo dockerRepoConn = NodeUtils.createUploadImageServerConn();
            String privateDockerRepo = SystemConfigUtil.getSystemConfigValue("docker.private.repo.ip") + ":" + SystemConfigUtil.getSystemConfigValue("docker.private.repo.port");
            String tagName = privateDockerRepo + "/" + appRepertoryVo.getGroupIdentify() + "/" + appRepertoryVo.getIdentify().toLowerCase() + ":" + version;
            String imageUploadFilePath = "f:/aa/" + date.getTime();
//            String imageUploadFilePath = SystemConfigUtil.getSystemConfigValue(ConfigPropKey.IMAGE_UPLOAD_DIR_PATH) + ServerCmdConstant.FILE_SEPARATOR + date.getTime();
//            try(InputStream is = ((CommonsMultipartFile) file).getFileItem().getInputStream();OutputStream outputStream=new FileOutputStream(new File(imageUploadFilePath))){
            try(BufferedInputStream bufferedInputStream = new BufferedInputStream(((CommonsMultipartFile) file).getFileItem().getInputStream());BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(new File(imageUploadFilePath)))){
                logger.warn("文件上传开始");
//                int byteCount = 0;
//                byte[] bytes = new byte[8192];
//                while ((byteCount = is.read(bytes)) != -1)
//                {
//                    outputStream.write(bytes, 0, byteCount);
//                }

                byte[] bytes= new byte[8192];
                int byteCount = 0;
                while ((byteCount = bufferedInputStream.read(bytes)) != -1){
                    bufferedOutputStream.write(bytes,0,byteCount);
                }
                bufferedOutputStream.flush();
                logger.warn("文件上传结束");
                MsgVO msgVo = null;
                msgVo = ShellExecUtil.exec(dockerRepoConn, new ExecShell(ShellUid.APP_IMAGE_UPLOAD, false)
                        .appendParam(imageUploadFilePath)
                        .appendParam(tagName));
                if (msgVo.isSuccess()) {
//                    appImageBpo.saveAppImage(repertoryId, version);
                    result.setSuccess(true);
                } else {
                    result.setSuccess(false);
                    result.setErrorMsg(msgVo.getSysoutMsg());
                }
            } catch (Exception e) {
                logger.error(logger.getName() + "context",e);
                result.setSuccess(false);
                result.setErrorMsg(e.getMessage());
            }
        }
        return result;
    }
//    @RequestMapping("/upload.do")
//    @ResponseBody
//    public CommonResultVo upload(HttpServletRequest request) {
//        CommonResultVo result = new CommonResultVo();
//        Map map = request.getParameterMap();
//        if (ValidateUtil.isEmpty(map)) {
//            result.setSuccess(false);
//            result.setErrorMsg("参数为空!");
//            return result;
//        }
//        if (ValidateUtil.isEmpty(map.get("repertoryId"))) {
//            result.setSuccess(false);
//            result.setErrorMsg("应用仓库ID为空!");
//            return result;
//        }
//        Long repertoryId = Long.valueOf(((String[]) map.get("repertoryId"))[0]);
//        if (ValidateUtil.isEmpty(map.get("version"))) {
//            result.setSuccess(false);
//            result.setErrorMsg("版本号为空!");
//            return result;
//        }
//        String version = ((String[]) map.get("version"))[0];
//        if (ValidateUtil.isEmpty(map.get("fileType"))) {
//            result.setSuccess(false);
//            result.setErrorMsg("文件类型为空!");
//            return result;
//        }
//        if (appImageBpo.checkVersionExist(repertoryId, version)) {
//            result.setSuccess(false);
//            result.setErrorMsg("该版本号已存在!");
//            return result;
//        }
//        AppRepertoryVo appRepertoryVo = appRepertoryBpo.getAppRepertory(repertoryId);
//        String contentType = request.getContentType();
//        if (contentType != null && contentType.toLowerCase().startsWith("multipart/")) {
//            MultipartHttpServletRequest multipartRequest =
//                    WebUtils.getNativeRequest(request, MultipartHttpServletRequest.class);
//            multipartRequest.getMultiFileMap();
//            MultipartFile file = multipartRequest.getFile("file");
//            if (ValidateUtil.isEmpty(file)) {
//                result.setSuccess(false);
//                result.setErrorMsg("上传文件为空!");
//                return result;
//            }
//            Date date = new Date();
//            ConnVo dockerRepoConn = NodeUtils.createDockerRepoServerConn();
//            String privateDockerRepo = SystemConfigUtil.getSystemConfigValue("docker.private.repo.ip") + ":" + SystemConfigUtil.getSystemConfigValue("docker.private.repo.port");
//            String tagName = privateDockerRepo + "/" + appRepertoryVo.getGroupIdentify() + "/" + appRepertoryVo.getIdentify().toLowerCase() + ":" + version;
//            String imageUploadFilePath = SystemConfigUtil.getSystemConfigValue(ConfigPropKey.IMAGE_UPLOAD_DIR_PATH) + ServerCmdConstant.FILE_SEPARATOR + date.getTime();
//            try(InputStream is = ((CommonsMultipartFile) file).getFileItem().getInputStream();OutputStream outputStream=new FileOutputStream(new File(imageUploadFilePath))){
//                int byteCount = 0;
//                byte[] bytes = new byte[1024];
//                while ((byteCount = is.read(bytes)) != -1)
//                {
//                    outputStream.write(bytes, 0, byteCount);
//                }
//                MsgVO msgVo = ShellExecUtil.exec(dockerRepoConn, new ExecShell(ShellUid.APP_IMAGE_UPLOAD, false)
//                        .appendParam(imageUploadFilePath)
//                        .appendParam(tagName));
//                if (msgVo.isSuccess()) {
////                    appImageBpo.saveAppImage(repertoryId, version);
//                    result.setSuccess(true);
//                } else {
//                    result.setSuccess(false);
//                    result.setErrorMsg(msgVo.getSysoutMsg());
//                }
//
//            } catch (Exception e) {
//                logger.error(logger.getName() + "context",e);
//                result.setSuccess(false);
//                result.setErrorMsg(e.getMessage());
//            }
//        }
//        return result;
//    }

    @RequestMapping("/remove.do")
    @ResponseBody
    public CommonResultVo removeAppImage(@RequestBody AppImageVo vo) {
        CommonResultVo result = new CommonResultVo();
        if (ValidateUtil.isEmpty(vo.getId())) {
            result.setSuccess(false);
            result.setErrorMsg("应用镜像ID为空!");
            return result;
        }
        if (appConfigBpo.checkImageIdUsed(vo.getId())) {
            result.setSuccess(false);
            result.setErrorMsg("存在使用该镜像的应用，请先删除该应用或修改该应用的镜像!");
            return result;
        }
        AppImageVo appImageVo = appImageBpo.getAppImage(vo.getId());
        AppRepertoryVo appRepertoryVo = appRepertoryBpo.getAppRepertory(appImageVo.getRepertoryId());
        AppGroupVo appGroupVo = appGroupBpo.getAppGroup(appRepertoryVo.getGroupId());
        String deleteUrl = "http://"
                + SystemConfigUtil.getSystemConfigValue("docker.private.repo.ip")
                + ":" + SystemConfigUtil.getSystemConfigValue("docker.private.repo.web.port")
                + "/repo/delete/" + appGroupVo.getGroupIdentify() + "%252F" + appRepertoryVo.getIdentify() + "/" + appImageVo.getVersion();
        try {
            sendGETRequest(deleteUrl, null, "UTF-8");
        } catch (Exception e) {
            logger.error(logger.getName() + "context",e);
        }
        appImageBpo.removeAppImage(vo.getId());
        result.setSuccess(true);
        return result;
    }

    /**
     * 将远程镜像pull到master节点并保存为img文件
     * @param vo
     * @return
     */
    @RequestMapping("/sshDownloadImage.do")
    @ResponseBody
    public CommonResultVo sshDownloadImage(@RequestBody AppImageVo vo,HttpServletRequest request)  {
        CommonResultVo result = new CommonResultVo();
        String imageName = vo.getImagePath() + ":" + vo.getVersion();
        String imageFile = SystemConfigUtil.getSystemConfigValue("DOCKER_IMAGE_FILE");
        Date date = new Date();
        String imagePath =  imageFile + ServerCmdConstant.FILE_SEPARATOR + date.getTime() + ".img";
        ProcessBuilder pb = new ProcessBuilder("/bin/docker","pull",imageName);
        ProcessBuilder pb1 = new ProcessBuilder("mkdir","-p",imageFile);
        ProcessBuilder pb2 = new ProcessBuilder("/bin/docker","save","-o",imagePath,imageName);
        synchronized (this.SYN_LOCK){
            try {
                pb.start();
                pb1.start();
                pb2.start();
            } catch (Exception e) {
                e.printStackTrace();
                result.setErrorMsg("准备文件失败");
                result.setSuccess(false);
                return result;
            }
        }
        result.setData(date.getTime());
        result.setSuccess(true);
        return result;
    }


    @RequestMapping(value = "/downloadImage.do",method= RequestMethod.GET)
    public String downloadImage(String appName,Long id, String version, Long fileName, HttpServletResponse response,HttpServletRequest request) {
        response.setCharacterEncoding("utf-8");
        response.setContentType("APPLICATION/OCTET-STREAM");
        response.setHeader("Content-Disposition", "attachment; filename=" + appName + version + ".img");
        String imageFile = SystemConfigUtil.getSystemConfigValue("DOCKER_IMAGE_FILE");
        String imagePath =  imageFile + ServerCmdConstant.FILE_SEPARATOR + fileName + ".img";
        File file = new File(imagePath);
        AppImageVo nvo = appImageBpo.getAppImage(id);
        if (file.exists()){
            try(OutputStream os = response.getOutputStream();InputStream is = new FileInputStream(file)){
                byte[] b = new byte[2048];
                int length;
                while ((length = is.read(b)) > 0){
                    os.write(b,0,length);
                }
                os.flush();
            }catch (Exception e){
                logger.error(logger.getName() + "context",e);
            }finally {
                if (file.delete()){
                    logger.warn(this.getClass().getName() + "删除文件失败");
                };//删除文件
            }
        }
        return null;
    }

    /**
     * 检测文件是否存在
     * @param vo
     * @return
     */
    @RequestMapping("/checkFile.do")
    @ResponseBody
    public CommonResultVo checkFile(@RequestBody FileVo vo){
        CommonResultVo result = new CommonResultVo();
        System.out.println(vo.getFileName());
        String imageFile = SystemConfigUtil.getSystemConfigValue("DOCKER_IMAGE_FILE");
        String imagePath =  imageFile + ServerCmdConstant.FILE_SEPARATOR + vo.getFileName() + ".img";
        File file = new File(imagePath);
        for (int i = 0; i < 90; i++){
            if (file.exists()){
                result.setSuccess(true);
                return result;
            }else {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    result.setErrorMsg("线程等待时出错");
                    result.setSuccess(false);
                    return result;
                }
            }
        }
        result.setSuccess(false);
        result.setErrorMsg("文件准备失败，需要重新下载");
        return result;
    }
}
