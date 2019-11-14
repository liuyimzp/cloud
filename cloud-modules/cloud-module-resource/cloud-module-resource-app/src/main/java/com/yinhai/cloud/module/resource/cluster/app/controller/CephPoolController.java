package com.yinhai.cloud.module.resource.cluster.app.controller;

import com.yinhai.cloud.core.api.util.UserUtils;
import com.yinhai.cloud.core.api.vo.CommonResultVo;
import com.yinhai.cloud.module.resource.cluster.api.bpo.ICephPoolBpo;
import com.yinhai.cloud.module.resource.cluster.api.vo.CephPoolVo;
import com.yinhai.cloud.module.resource.nodes.api.bpo.ICephNodeBpo;
import com.yinhai.cloud.module.resource.util.ServerUtils;
import com.yinhai.core.app.ta3.web.controller.BaseController;
import com.yinhai.core.common.api.util.ValidateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author liuyi02
 */
@Controller
@RequestMapping("/cephPool")
public class CephPoolController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(CephPoolController.class);

    @Resource
    private ICephPoolBpo cephPoolBpo;

    @Resource
    private ICephNodeBpo cephNodeBpo;

    /**
     * 获得集群所有的存储池
     * @param vo
     * @return
     */
    @RequestMapping("getAll.do")
    @ResponseBody
    public CommonResultVo getAllPool(@RequestBody CephPoolVo vo){
        CommonResultVo result = new CommonResultVo();
        List<CephPoolVo> pools = cephPoolBpo.getAllPools(vo.getClusterId());
        List<Map> list = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (CephPoolVo cvo : pools){
            try {
                Map map = cvo.toMap();
                map.put("createDate",sdf.format(cvo.getCreateDate()));
                list.add(map);
            }catch (Exception e) {
                logger.error(logger.getName() + "context",e);
            }
        }
        result.setSuccess(true);
        result.setData(list);
        return result;
    }

    @RequestMapping("saveCephPool")
    @ResponseBody
    public CommonResultVo addCephPool(@RequestBody CephPoolVo vo,ServletRequest request){
        CommonResultVo result = new CommonResultVo();
        if (ValidateUtil.isEmpty(vo.getPgNum())){
            result.setSuccess(false);
            result.setErrorMsg("pgNum为空！");
            return result;
        }
        if (ValidateUtil.isEmpty(vo.getPoolName())){
            result.setSuccess(false);
            result.setErrorMsg("pool名称为空！");
            return result;
        }
        if (cephPoolBpo.checkPoolName(vo.getPoolName())){
            result.setSuccess(false);
            result.setErrorMsg(vo.getPoolName() + "已经存在!");
            return result;
        }
        final String userId = UserUtils.getUserId(request);
        vo.setCreateUser(userId);
        vo.setCreateDate(new Date());
        vo.setPoolIsUse(0);
        try {
            ServerUtils.createPool(vo);
        } catch (Exception e) {
            result.setErrorMsg("创建pool失败");
            result.setSuccess(false);
            return result;
        }
        cephPoolBpo.savePool(vo);
        if (ValidateUtil.isEmpty(vo)){
            result.setSuccess(false);
            result.setErrorMsg("写入数据库失败");
        }
        result.setSuccess(true);
        return result;
    }

    /**
     * 修改pool
     * @return
     */
    @RequestMapping("updatePool")
    @ResponseBody
    public CommonResultVo updatePool(@RequestBody CephPoolVo vo){
        CommonResultVo result = new CommonResultVo();
        if (ValidateUtil.isEmpty(vo.getPgNum())){
            result.setSuccess(false);
            result.setErrorMsg("pgNum为空！");
            return result;
        }
        if (ValidateUtil.isEmpty(vo.getPoolName())){
            result.setSuccess(false);
            result.setErrorMsg("pool名称为空！");
            return result;
        }
        CephPoolVo nvo = cephPoolBpo.getPoolById(vo.getPoolId());
        if (!nvo.getPoolName().equals(vo.getPoolName()) ){
            try {
                ServerUtils.setPoolName(nvo,vo);
                nvo.setPoolName(vo.getPoolName());
            } catch (Exception e) {
                result.setSuccess(false);
                result.setErrorMsg("修改poolName报错");
                return result;
            }
        }
        int osdNum = cephNodeBpo.getOsdNodeNum(vo.getClusterId());
        int oldPgNum = nvo.getPgNum();
        int nvoPgNum = vo.getPgNum();
        if (oldPgNum > nvoPgNum){
            result.setErrorMsg("pgNum不可变小");
            result.setSuccess(false);
            return result;
        }
        if (oldPgNum < nvoPgNum){
            if ((nvoPgNum - oldPgNum) > osdNum*32){
                result.setErrorMsg("增加上限不可超过osd节点数量*32");
                result.setSuccess(false);
                return result;
            }else {
                try {
                    ServerUtils.setPgNum(vo);
                    nvo.setPgNum(vo.getPgNum());
                } catch (Exception e) {
                    result.setSuccess(false);
                    result.setErrorMsg("修改pgNum时报错");
                    return result;
                }
            }
        }
        cephPoolBpo.updatePool(nvo);
        result.setSuccess(true);
        return result;
    }
    @RequestMapping("cephPoolDelete")
    @ResponseBody
    public CommonResultVo deletePool(@RequestBody CephPoolVo vo){
        CommonResultVo result = new CommonResultVo();
        if (vo.getPoolIsUse() != 0){
            result.setSuccess(false);
            result.setErrorMsg("改存储池已经被使用不可删除");
            return result;
        }
        try {
            ServerUtils.deletePool(vo);
        } catch (Exception e) {
            result.setErrorMsg(e.getMessage());
            result.setSuccess(false);
            return result;
        }
        cephPoolBpo.deleteByPoolId(vo.getPoolId());
        result.setSuccess(true);
        return result;
    }
}
