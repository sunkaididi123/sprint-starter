package com.xwhking.yuapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xwhking.yuapi.common.ErrorCode;
import com.xwhking.yuapi.exception.ThrowUtils;
import com.xwhking.yuapi.model.entity.InterfaceInfo;
import com.xwhking.yuapi.model.entity.User;
import com.xwhking.yuapi.model.entity.UserInterfaceInfo;
import com.xwhking.yuapi.service.InterfaceInfoService;
import com.xwhking.yuapi.service.UserInterfaceInfoService;
import com.xwhking.yuapi.mapper.UserInterfaceInfoMapper;
import com.xwhking.yuapi.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author 28374
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service实现
* @createDate 2023-12-03 16:04:14
*/
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
    implements UserInterfaceInfoService{

    @Resource
    private UserService userService;
    @Resource
    private InterfaceInfoService interfaceInfoService;

    @Resource
    private UserInterfaceInfoMapper userInterfaceInfoMapper;

    /**
     * 实现方法秩检验用户 id 和 接口 id 是否存在，并且 totalNum 是否为负数， 以及leftNum是否为负数
     * @param userInterfaceInfo
     */
    @Override
    public void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo) {
         Long userId = userInterfaceInfo.getUserId();
         Long interfaceInfoId = userInterfaceInfo.getInterfaceInfoId();
         Integer totalNum = userInterfaceInfo.getTotalNum();
         Integer leftNum = userInterfaceInfo.getLeftNum();
         Integer status = userInterfaceInfo.getStatus();
         User user = userService.getById(userId);
         InterfaceInfo interfaceInfo = interfaceInfoService.getById(interfaceInfoId);
         ThrowUtils.throwIf(user == null || interfaceInfo == null, ErrorCode.PARAMS_ERROR,"用户或接口不存在");
         ThrowUtils.throwIf(totalNum < 0 || leftNum < 0 , ErrorCode.PARAMS_ERROR,"用户接口信息调用数据不正确");
         ThrowUtils.throwIf(status<0 || status > 1,ErrorCode.PARAMS_ERROR,"用户接口信息状态不正确");
    }

    @Override
    public boolean recordInvokeOne(Long userId, Long interfaceInfoId) {
        ThrowUtils.throwIf(userId == null || interfaceInfoId== null,ErrorCode.PARAMS_ERROR,"用户 或 接口 id 为空");
        LambdaQueryWrapper<UserInterfaceInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UserInterfaceInfo::getUserId,userId);
        lambdaQueryWrapper.eq(UserInterfaceInfo::getInterfaceInfoId,interfaceInfoId);
        UserInterfaceInfo userInterfaceInfo = userInterfaceInfoMapper.selectOne(lambdaQueryWrapper) ;
        ThrowUtils.throwIf(userInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);
        int totalNum = userInterfaceInfo.getTotalNum();
        int leftNum = userInterfaceInfo.getLeftNum();
        ThrowUtils.throwIf(leftNum <= 0 , ErrorCode.OPERATION_ERROR,"暂无调用次数");
        userInterfaceInfo.setLeftNum(leftNum - 1);
        userInterfaceInfo.setTotalNum(totalNum + 1);
        return this.updateById(userInterfaceInfo);
    }
}




