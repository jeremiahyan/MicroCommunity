package com.java110.api.listener.activities;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.activities.IActivitiesBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.community.IActivitiesInnerServiceSMO;
import com.java110.intf.common.IFileInnerServiceSMO;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.dto.file.FileDto;
import com.java110.dto.file.FileRelDto;
import com.java110.po.activities.ActivitiesPo;
import com.java110.po.file.FileRelPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.ServiceCodeActivitiesConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

import java.util.List;

/**
 * 保存活动侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("updateActivitiesListener")
public class UpdateActivitiesListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IActivitiesInnerServiceSMO activitiesInnerServiceSMOImpl;

    @Autowired
    private IActivitiesBMO activitiesBMOImpl;

    @Autowired
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "activitiesId", "活动ID不能为空");
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");
        Assert.hasKeyAndValue(reqJson, "title", "必填，请填写业活动标题");
        Assert.hasKeyAndValue(reqJson, "typeCd", "必填，请选择活动类型");
        Assert.hasKeyAndValue(reqJson, "headerImg", "必填，请选择头部照片");
        Assert.hasKeyAndValue(reqJson, "context", "必填，请填写活动内容");
        Assert.hasKeyAndValue(reqJson, "startTime", "必填，请选择开始时间");
        Assert.hasKeyAndValue(reqJson, "endTime", "必填，请选择结束时间");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {


        if (reqJson.containsKey("headerImg") && !StringUtils.isEmpty(reqJson.getString("headerImg"))) {
            FileDto fileDto = new FileDto();
            fileDto.setFileId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_file_id));
            fileDto.setFileName(fileDto.getFileId());
            fileDto.setContext(reqJson.getString("headerImg"));
            fileDto.setSuffix("jpeg");
            fileDto.setCommunityId(reqJson.getString("communityId"));
            String fileName = fileInnerServiceSMOImpl.saveFile(fileDto);

            reqJson.put("headerImg", fileDto.getFileId());
            reqJson.put("fileSaveName", fileName);

            FileRelDto fileRelDto = new FileRelDto();
            fileRelDto.setRelTypeCd("70000");
            fileRelDto.setObjId(reqJson.getString("activitiesId"));
            List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);

            if (fileRelDtos == null || fileRelDtos.size() == 0) {
                FileRelPo fileRelPo = new FileRelPo();
                fileRelPo.setFileRelId("-1");
                fileRelPo.setFileRealName(reqJson.getString("headerImg"));
                fileRelPo.setFileSaveName(reqJson.getString("fileSaveName"));
                fileRelPo.setObjId(reqJson.getString("activitiesId"));
                fileRelPo.setSaveWay("table");
                fileRelPo.setRelTypeCd("70000");
                super.insert(context, fileRelPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_FILE_REL);
            } else {
                FileRelPo fileRelPo = new FileRelPo();
                fileRelPo.setFileRelId(fileRelDtos.get(0).getFileRelId());
                fileRelPo.setFileRealName(reqJson.getString("headerImg"));
                fileRelPo.setFileSaveName(reqJson.getString("fileSaveName"));
                fileRelPo.setObjId(reqJson.getString("activitiesId"));
                super.update(context, fileRelPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_FILE_REL);
            }

        }

        ActivitiesPo activitiesPo = BeanConvertUtil.covertBean(reqJson, ActivitiesPo.class);

        super.update(context, activitiesPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_ACTIVITIES);

    }


    @Override
    public String getServiceCode() {
        return ServiceCodeActivitiesConstant.UPDATE_ACTIVITIES;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


}
