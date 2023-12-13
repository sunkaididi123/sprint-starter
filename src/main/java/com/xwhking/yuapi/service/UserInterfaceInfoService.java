package com.xwhking.yuapi.service;

import com.xwhking.yuapi.model.entity.UserInterfaceInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 28374
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service
* @createDate 2023-12-03 16:04:14
*/
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {

    /**
     * 验证是否合法
     * @param userInterfaceInfo
     */
    void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo);

    /**
     * 记录接口调用的次数
     * @param userId
     * @param interfaceInfoId
     * @return
     */
    boolean recordInvokeOne(Long userId, Long interfaceInfoId);


}
