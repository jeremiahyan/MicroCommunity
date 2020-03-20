package com.java110.web.smo.activities;

import com.java110.utils.exception.SMOException;
import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * 活动管理服务接口类
 *
 * add by wuxw 2019-06-29
 */
public interface IListActivitiessSMO {

    /**
     * 查询活动信息
     * @param pd 页面数据封装
     * @return ResponseEntity 对象数据
     * @throws SMOException 业务代码层
     */
    ResponseEntity<String> listActivitiess(IPageData pd) throws SMOException;
}