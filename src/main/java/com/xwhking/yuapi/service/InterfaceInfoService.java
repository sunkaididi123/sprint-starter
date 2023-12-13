package com.xwhking.yuapi.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xwhking.yuapi.model.dto.interfaceInfo.InterfaceInfoQueryRequest;
import com.xwhking.yuapi.model.dto.post.PostQueryRequest;
import com.xwhking.yuapi.model.entity.InterfaceInfo;
import com.xwhking.yuapi.model.entity.Post;

/**
* @author 28374
* @description 针对表【interface_info(接口表)】的数据库操作Service
* @createDate 2023-11-16 22:08:25
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {

    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean b);

    Object getQueryWrapper(InterfaceInfoQueryRequest interfaceInfoQueryRequest);

}
