package com.yinhai.cloud.module.shell.controller;

import com.google.common.base.CaseFormat;
import com.yinhai.cloud.core.api.exec.ShellUid;
import com.yinhai.cloud.core.api.vo.CommonResultVo;
import com.yinhai.cloud.module.shell.api.IShellBpo;
import com.yinhai.cloud.module.shell.vo.KvVo;
import com.yinhai.cloud.module.shell.vo.ShellVo;
import com.yinhai.modules.codetable.api.domain.vo.AppCodeVo;
import com.yinhai.modules.codetable.api.util.CodeTableUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jianglw
 */
@Controller
@RequestMapping("/shell")
public class ShellController {

    private final static Logger logger = LoggerFactory.getLogger(ShellController.class);
    @Resource
    private IShellBpo shellBpo;

    @RequestMapping(value = "/init/uid.do", method = RequestMethod.GET)
    @ResponseBody
    public CommonResultVo getShellUids() {
        final CommonResultVo resultVo = new CommonResultVo();
        final List<AppCodeVo> codeVoList = CodeTableUtil.getCodeList(ShellUid.SHELL_UID, null);
        final List<KvVo> uidList = codeVoList.stream().map(it -> KvVo.getKvVo(CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, it.getCodeDESC()), it.getCodeValue())).collect(Collectors.toList());
        resultVo.setSuccess(true);
        resultVo.setData(uidList);
        return resultVo;
    }

    @RequestMapping(value = "/init/type.do", method = RequestMethod.GET)
    @ResponseBody
    public CommonResultVo getShellTypes() {
        final List<AppCodeVo> codeVoList = CodeTableUtil.getCodeList(ShellUid.SHELL_CODE_TYPE, null);
        final CommonResultVo resultVo = new CommonResultVo();
        resultVo.setData(codeVoList);
        resultVo.setSuccess(true);
        return resultVo;
    }

    @RequestMapping(value = "/get/all.do", method = RequestMethod.GET)
    @ResponseBody
    public CommonResultVo getAllShell() {
        final List<ShellVo> vos = shellBpo.queryAllShell();
        vos.forEach(it -> {
            final String desc = CodeTableUtil.getDesc(ShellUid.SHELL_CODE_TYPE, it.getShellType());
            it.setShellTypeDesc(desc);
            it.setShellUidDesc(CodeTableUtil.getDesc(ShellUid.SHELL_UID, it.getShellUid()));
        });
        final CommonResultVo resultVo = new CommonResultVo();
        resultVo.setSuccess(true);
        resultVo.setData(vos);
        return resultVo;
    }

    @RequestMapping(value = "/get/uid.do", method = RequestMethod.GET)
    @ResponseBody
    public CommonResultVo getShell(@RequestBody final ShellVo vo) {
        final ShellVo shellVo = shellBpo.getOneShell(vo.getShellId());
        final CommonResultVo resultVo = new CommonResultVo();

        resultVo.setSuccess(true);
        resultVo.setData(shellVo);
        return resultVo;
    }

    @RequestMapping(value = "/save.do")
    @ResponseBody
    public CommonResultVo addShell(@RequestBody final ShellVo vo) {
        final boolean saveSuccess = shellBpo.save(vo);
        final CommonResultVo resultVo = new CommonResultVo();

        resultVo.setSuccess(saveSuccess);
        if (!saveSuccess) {
            resultVo.setErrorMsg("保存失败，可能已存在相同类型的脚本。");
            resultVo.setData(vo);
        }
        return resultVo;
    }

    @RequestMapping("/edit.do")
    @ResponseBody
    public CommonResultVo editShell(@RequestBody final ShellVo vo) throws Exception {
        final CommonResultVo resultVo = new CommonResultVo();
        if (vo.getShellId() == null) {
            resultVo.setErrorMsg("Shell ID不能为空！");
            resultVo.setSuccess(false);
            return resultVo;
        }
        final boolean updateSuccess = shellBpo.update(vo);

        resultVo.setSuccess(updateSuccess);
        final String shellContent = vo.getShellContent();
        try (final BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Paths.get("D:\\1.sh").toFile()), "UTF-8"))){
            bufferedWriter.write(shellContent);
        }catch (Exception e){
            logger.error(logger.getName() + "context",e);
        }
        return resultVo;
    }

    @RequestMapping("/delete.do")
    @ResponseBody
    public CommonResultVo deleteShell(@RequestBody final ShellVo vo) {
        final boolean updateSuccess = shellBpo.delete(vo);
        final CommonResultVo resultVo = new CommonResultVo();

        resultVo.setSuccess(updateSuccess);
        return resultVo;
    }

    @RequestMapping(value = "/exe/execShell.do")
    @ResponseBody
    public CommonResultVo execShell(@RequestBody final ShellVo vo) {
        final CommonResultVo resultVo = new CommonResultVo();

        resultVo.setSuccess(true);
        resultVo.setData(LocalTime.now().toString() + "::uid" + vo.getShellUid());
        return resultVo;
    }
}
