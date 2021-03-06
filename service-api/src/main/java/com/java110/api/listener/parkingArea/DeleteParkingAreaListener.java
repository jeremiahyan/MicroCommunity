package com.java110.api.listener.parkingArea;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.parkingArea.IParkingAreaBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeParkingAreaConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

/**
 * 保存小区侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("deleteParkingAreaListener")
public class DeleteParkingAreaListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IParkingAreaBMO parkingAreaBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");
        Assert.hasKeyAndValue(reqJson, "communityId", "必填，请填写小区信息");
        Assert.hasKeyAndValue(reqJson, "paId", "停车场ID不能为空");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        parkingAreaBMOImpl.deleteParkingArea(reqJson, context);

    }

    @Override
    public String getServiceCode() {
        return ServiceCodeParkingAreaConstant.DELETE_PARKINGAREA;
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
