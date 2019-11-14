package com.yinhai.cloud.module.repertory.app.controller;

import com.yinhai.cloud.core.api.constant.ConfigPropKey;
import com.yinhai.cloud.core.api.exception.SSHConnectionException;
import com.yinhai.cloud.core.api.exception.SSHExecuteException;
import com.yinhai.cloud.core.api.ssh.SlashPath;
import com.yinhai.cloud.core.api.ssh.command.ConsoleCommand;
import com.yinhai.cloud.core.api.ssh.command.UploadStreamCommand;
import com.yinhai.cloud.core.api.util.CommonSshExecUtil;
import com.yinhai.cloud.core.api.util.CommonUtil;
import com.yinhai.cloud.core.api.util.SystemConfigUtil;
import com.yinhai.cloud.core.api.vo.CommonResultVo;
import com.yinhai.cloud.core.api.vo.ConnVo;
import com.yinhai.cloud.module.repertory.api.bpo.IAppImageBpo;
import com.yinhai.cloud.module.repertory.api.bpo.IAppRepertoryBpo;
import com.yinhai.cloud.module.repertory.api.constant.RepositoryConstant;
import com.yinhai.cloud.module.repertory.api.util.BuildJavaApplicationDockerImageTool;
import com.yinhai.cloud.module.repertory.api.vo.AppImageVo;
import com.yinhai.cloud.module.repertory.api.vo.AppRepertoryVo;
import com.yinhai.cloud.module.resource.util.NodeUtils;
import com.yinhai.core.app.ta3.web.controller.BaseController;
import com.yinhai.core.common.api.util.ValidateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.util.WebUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhaokai on 2018/5/17.
 * 自定义Docker镜像
 */
@Controller
@RequestMapping({"/diyImage"})
public class DiyAppImageController extends BaseController {
    private static String DIY_IMAGE_OS = "DIY_IMAGE_OS";
    private static String DIY_JDK_VERSION = "DIY_JDK_VERSION";
    private static String DIY_WEB_CONTAINER = "DIY_WEB_CONTAINER";
    private static final Logger logger = LoggerFactory.getLogger(DiyAppImageController.class);
    @Resource
    private IAppImageBpo appImageBpo;

    @Resource
    private IAppRepertoryBpo appRepertoryBpo;

    @RequestMapping("save")
    @ResponseBody
    public CommonResultVo save(@RequestBody AppImageVo image) {
        CommonResultVo result = new CommonResultVo();
        if (ValidateUtil.isEmpty(image.getRepertoryId())) {
            result.setSuccess(false);
            result.setErrorMsg("应用仓库ID不能为空，" + removeFile(image.getUploadTmpFileName()));
            return result;
        }

        if (ValidateUtil.isEmpty(image.getVersion())) {
            result.setSuccess(false);
            result.setErrorMsg("版本号不能为空，" + removeFile(image.getUploadTmpFileName()));
            return result;
        }
        if (ValidateUtil.isEmpty(image.getDiyType())) {
            result.setSuccess(false);
            result.setErrorMsg("自定义镜像类型不能为空，" + removeFile(image.getUploadTmpFileName()));
            return result;
        }
        if (appImageBpo.checkVersionExist(image.getRepertoryId(), image.getVersion())) {
            result.setSuccess(false);
            result.setErrorMsg("该版本号 " + image.getVersion() + " 已存在，" + removeFile(image.getUploadTmpFileName()));
            return result;
        }
        if (ValidateUtil.isEmpty(image.getDiyJdkVersion())) {
            result.setSuccess(false);
            result.setErrorMsg("JDK版本不能为空，" + removeFile(image.getUploadTmpFileName()));
            return result;
        }

        if (ValidateUtil.isEmpty(image.getDiyOperateSystem())) {
            result.setSuccess(false);
            result.setErrorMsg("操作系统不能为空，" + removeFile(image.getUploadTmpFileName()));
            return result;
        }

        if (ValidateUtil.isEmpty(image.getUploadTmpFileName())) {
            result.setSuccess(false);
            result.setErrorMsg("已上传的临时文件名称不能为空，" + removeFile(image.getUploadTmpFileName()));
            return result;
        }

        boolean needWebContainer = RepositoryConstant.WAR_FILE_SUFFIX.equals(image.getDiyType()) || RepositoryConstant.EAR_FILE_SUFFIX.equals(image.getDiyType());
        if (needWebContainer && ValidateUtil.isEmpty(image.getDiyWebContainer())) {
            result.setSuccess(false);
            result.setErrorMsg("自定义镜像应用服务器不能为空，" + removeFile(image.getUploadTmpFileName()));
            return result;
        }
        AppRepertoryVo repo = appRepertoryBpo.getAppRepertory(image.getRepertoryId());
        image.setImageBuildDirName(RepositoryConstant.IMAGE_BUILD_DIR_PREFIX + "-" + repo.getIdentify() + "-" + image.getVersion());
        image.setLocalFileName(repo.getIdentify() + "." + image.getDiyType());
        try {
            BuildJavaApplicationDockerImageTool.build(image);
            appImageBpo.saveDiyImage(image);

        } catch (SSHExecuteException | SSHConnectionException e) {
            result.setSuccess(false);
            result.setErrorMsg(e.getMessage());
            return result;
        }finally {
            removeFile(image.getUploadTmpFileName());
        }
        result.setSuccess(true);
        return result;

    }

    @RequestMapping("createDiyImage")
    @ResponseBody
    public CommonResultVo createDiyImagePageInitData(@RequestBody AppRepertoryVo repertory) {
        CommonResultVo result = new CommonResultVo();
        if (ValidateUtil.isEmpty(repertory.getId())) {
            result.setErrorMsg("仓库ID不能为空");
            result.setSuccess(false);
            return result;
        }
        AppRepertoryVo repository = appRepertoryBpo.getAppRepertory(repertory.getId());
        List<Map<String, String>> osList = CommonUtil.getCodeDescListForSelect(DIY_IMAGE_OS);
        List<Map<String, String>> jdkList = CommonUtil.getCodeDescListForSelect(DIY_JDK_VERSION);
        List<Map<String, String>> webContainerList = CommonUtil.getCodeDescListForSelect(DIY_WEB_CONTAINER);
        HashMap<String, Object> data = new HashMap<>();
        data.put("repository", repository);
        data.put("osList", osList);
        data.put("jdkList", jdkList);
        data.put("webContainerList", webContainerList);
        result.setData(data);
        result.setSuccess(true);
        return result;
    }

//
//    @RequestMapping("upload")
//    @ResponseBody
//    public CommonResultVo upload(HttpServletRequest request) {
//        CommonResultVo result = new CommonResultVo();
//        String contentType = request.getContentType();
//        if (contentType == null) {
//            result.setSuccess(false);
//            result.setErrorMsg("上传文件内容不能为空");
//            return result;
//        }
//        if (!contentType.toLowerCase().startsWith("multipart/")) {
//            result.setSuccess(false);
//            result.setErrorMsg("上传文件数据格式不符，期望：multipart/ ");
//            return result;
//        }
//        MultipartHttpServletRequest multipartRequest =
//                WebUtils.getNativeRequest(request, MultipartHttpServletRequest.class);
//        multipartRequest.getMultiFileMap();
//        MultipartFile file = multipartRequest.getFile("file");
//        if (ValidateUtil.isEmpty(file)) {
//            result.setSuccess(false);
//            result.setErrorMsg("上传文件为空!");
//            return result;
//        }
//        String uploadDir = SystemConfigUtil.getSystemConfigValue(ConfigPropKey.DOCKER_IMAGE_BUILD_MAIN_DIR_PATH) + "/"
//                + RepositoryConstant.DOCKER_APPLICATION_UPLOAD_SERVER_DIR_NAME;
//        final String makeUploadDir = "mkdir -p " + uploadDir;
//        ConsoleCommand command = new ConsoleCommand();
//        command.appendCommand("mkdir -p " + uploadDir);
//        ConnVo dockerRepoServerConn = NodeUtils.createDockerRepoServerConn();
//        try {
//            CommonSshExecUtil.exec(dockerRepoServerConn, command);
//        } catch (Exception e) {
//            result.setSuccess(false);
//            result.setErrorMsg("文件创建失败");
//            return result;
//        }
//        String fileName = CommonUtil.getTime(RepositoryConstant.UPLOAD_FILE_NAME_TIME_FORMAT);
//        String remoteFilePath = uploadDir + ServerCmdConstant.FILE_SEPARATOR + fileName;
//        try (InputStream is = ((CommonsMultipartFile) file).getFileItem().getInputStream();OutputStream outputStream=new FileOutputStream(new File(remoteFilePath))){
////            Runtime.getRuntime().exec(makeUploadDir);
//            //上传
//            int bytesWritten = 0;
//            int byteCount = 0;
//            byte[] bytes = new byte[1024];
//            while ((byteCount = is.read(bytes)) != -1)
//            {
//                outputStream.write(bytes, bytesWritten, byteCount);
//                bytesWritten += byteCount;
//            }
//            AppImageVo data = new AppImageVo();
//            data.setUploadTmpFileName(fileName);
//            result.setData(data);
//            result.setSuccess(true);
//        } catch (Exception e) {
//            logger.error(logger.getName() + "context",e);
//            result.setSuccess(false);
//            result.setErrorMsg(e.getMessage());
//            return  result;
//        }
//        return result;
//    }

    @RequestMapping("upload")
    @ResponseBody
    public CommonResultVo upload(HttpServletRequest request) {
        CommonResultVo result = new CommonResultVo();
        String contentType = request.getContentType();
        if (contentType == null) {
            result.setSuccess(false);
            result.setErrorMsg("上传文件内容不能为空");
            return result;
        }
        if (!contentType.toLowerCase().startsWith("multipart/")) {
            result.setSuccess(false);
            result.setErrorMsg("上传文件数据格式不符，期望：multipart/ ");
            return result;
        }
        MultipartHttpServletRequest multipartRequest =
                WebUtils.getNativeRequest(request, MultipartHttpServletRequest.class);
        multipartRequest.getMultiFileMap();
        MultipartFile file = multipartRequest.getFile("file");
        if (ValidateUtil.isEmpty(file)) {
            result.setSuccess(false);
            result.setErrorMsg("上传文件为空!");
            return result;
        }
        ConnVo dockerRepoServerConn = NodeUtils.createDockerRepoServerConn();


        String uploadDir = SystemConfigUtil.getSystemConfigValue(ConfigPropKey.DOCKER_IMAGE_BUILD_MAIN_DIR_PATH) + "/"
                + RepositoryConstant.DOCKER_APPLICATION_UPLOAD_SERVER_DIR_NAME;

        final ConsoleCommand makeUploadDir = new ConsoleCommand();
        makeUploadDir.appendCommand("mkdir -p " + uploadDir);
        String fileName = CommonUtil.getTime(RepositoryConstant.UPLOAD_FILE_NAME_TIME_FORMAT);
        try (InputStream is = ((CommonsMultipartFile) file).getFileItem().getInputStream()){
            SlashPath remoteFilePath = new SlashPath(uploadDir, fileName);
            UploadStreamCommand uploadCommand = new UploadStreamCommand(remoteFilePath.getFullPath(), is);
            CommonSshExecUtil.exec(dockerRepoServerConn, makeUploadDir, uploadCommand);
            AppImageVo data = new AppImageVo();
            data.setUploadTmpFileName(fileName);
            result.setData(data);
            result.setSuccess(true);
        } catch (Exception e) {
            logger.error(logger.getName() + "context",e);
            result.setSuccess(false);
            result.setErrorMsg(e.getMessage());
            return  result;
        }
        return result;
    }

    /**
     * 删除对应镜像文件
     * @param imageFileName
     * @return String返回的日志
     */
    public String removeFile(String imageFileName){
        try {
            //保存成功后删除上传的war文件
            String uploadDir = SystemConfigUtil.getSystemConfigValue(ConfigPropKey.DOCKER_IMAGE_BUILD_MAIN_DIR_PATH) + "/"
                    + RepositoryConstant.DOCKER_APPLICATION_UPLOAD_SERVER_DIR_NAME;

            ConsoleCommand stopCommand = new ConsoleCommand();
            stopCommand.appendCommand("rm " + uploadDir + "/" + imageFileName + " -f");
            ConnVo dockerRepoServerConn = NodeUtils.createDockerRepoServerConn();
            CommonSshExecUtil.exec(dockerRepoServerConn, stopCommand);
        } catch (SSHExecuteException | SSHConnectionException e) {
            return "未删除已上传的war/jar文件";
        }
        return  "删除已上传war/jar文件";
    }
}
