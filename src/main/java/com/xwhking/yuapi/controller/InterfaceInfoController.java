package com.xwhking.yuapi.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.xwhking.yuapi.annotation.AuthCheck;
import com.xwhking.yuapi.common.BaseResponse;
import com.xwhking.yuapi.common.DeleteRequest;
import com.xwhking.yuapi.common.ErrorCode;
import com.xwhking.yuapi.common.ResultUtils;
import com.xwhking.yuapi.constant.CommonConstant;
import com.xwhking.yuapi.constant.UserConstant;
import com.xwhking.yuapi.exception.BusinessException;
import com.xwhking.yuapi.exception.ThrowUtils;
import com.xwhking.yuapi.model.dto.IdRequest;
import com.xwhking.yuapi.model.dto.interfaceInfo.*;
import com.xwhking.yuapi.model.entity.InterfaceInfo;
import com.xwhking.yuapi.model.entity.User;
import com.xwhking.yuapi.service.InterfaceInfoService;
import com.xwhking.yuapi.service.UserInterfaceInfoService;
import com.xwhking.yuapi.service.UserService;
import com.xwhking.yuapistarter.client.XWHKINGClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 帖子接口
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@RestController
@RequestMapping("/interfaceInfo")
@Slf4j
public class InterfaceInfoController {

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @Resource
    private UserService userService;

    @Resource
    private XWHKINGClient xwhkingClient;

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    private final static Gson GSON = new Gson();

    // region 增删改查

    /**
     * 创建
     *
     * @param interfaceInfoAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addInterfaceInfo(@RequestBody InterfaceInfoAddRequest interfaceInfoAddRequest, HttpServletRequest request) {
        if (interfaceInfoAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoAddRequest, interfaceInfo);
        interfaceInfoService.validInterfaceInfo(interfaceInfo, true);
        User loginUser = userService.getLoginUser(request);
        interfaceInfo.setUserId(loginUser.getId());
        boolean result = interfaceInfoService.save(interfaceInfo);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newInterfaceInfoId = interfaceInfo.getId();
        return ResultUtils.success(newInterfaceInfoId);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteInterfaceInfo(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        ThrowUtils.throwIf(oldInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldInterfaceInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = interfaceInfoService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新（仅管理员）
     *
     * @param interfaceInfoUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateInterfaceInfo(@RequestBody InterfaceInfoUpdateRequest interfaceInfoUpdateRequest) {
        if (interfaceInfoUpdateRequest == null || interfaceInfoUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoUpdateRequest, interfaceInfo);
        // 参数校验
        interfaceInfoService.validInterfaceInfo(interfaceInfo, false);
        long id = interfaceInfoUpdateRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        ThrowUtils.throwIf(oldInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = interfaceInfoService.updateById(interfaceInfo);
        return ResultUtils.success(result);
    }
    /**
     * 发布或者下线
     *
     * @param idRequest
     * @return
     */
    @PostMapping("/onlineOrNot")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> onlineOrNotInterface(@RequestBody IdRequest idRequest) {
        long id = idRequest.getId();
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 判断是否存在
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);
        ThrowUtils.throwIf(interfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);
        int status = interfaceInfo.getStatus();
        interfaceInfo.setStatus(status==0?1:0);
        boolean result = interfaceInfoService.updateById(interfaceInfo);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param idRequest
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<InterfaceInfo> getInterfaceInfoById(IdRequest idRequest, HttpServletRequest request) {
        long id = idRequest.getId();
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);
        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(interfaceInfo);
    }

    @GetMapping("/list/page")
    public BaseResponse<Page<InterfaceInfo>> listInterfaceInfoByPage(InterfaceInfoQueryRequest interfaceInfoQueryRequest,HttpServletRequest request){
        if(interfaceInfoQueryRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfoQuery = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoQueryRequest,interfaceInfoQuery);
        long current = interfaceInfoQueryRequest.getCurrent();
        long size = interfaceInfoQueryRequest.getPageSize();
        String sortField = interfaceInfoQueryRequest.getSortField();
        String sortOrder = interfaceInfoQueryRequest.getSortOrder();
        String description = interfaceInfoQueryRequest.getDescription();
        interfaceInfoQuery.setDescription(null);
        // 限制爬虫
        if(size > 50){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"你小子，是不是想爬虫");
        }
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>(interfaceInfoQuery);
        queryWrapper.like(StringUtils.isNotBlank(description),"description",description );
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC),sortField);
        Page<InterfaceInfo> interfaceInfoPage = interfaceInfoService.page(new Page<>(current,size),queryWrapper);
        return ResultUtils.success(interfaceInfoPage    );
    }

    @PostMapping("/invoke")
    public BaseResponse<Object> invoke(@RequestBody InterfaceInvokeRequest interfaceInvokeRequest,HttpServletRequest request){
        // todo  完成调用示例
        long interfaceId = interfaceInvokeRequest.getId();
        String url = interfaceInvokeRequest.getUrl();
        String param = interfaceInvokeRequest.getUserRequestParams();
        Gson gson = new Gson();
        InvokeParam invokeParam = gson.fromJson(param,InvokeParam.class);
        Object result;
        switch (url){
            case "/api/invoke/daily":
                result = xwhkingClient.invokeDaily();
                break;
            case "/api/invoke/getExpression":
                result = xwhkingClient.invokeGetExpression(invokeParam.getKeyword());
                break;
            case "/api/invoke/getOneSentence":
                result = xwhkingClient.invokeGetOneSentence(invokeParam.getType());
                break;
            case "/api/invoke/getQrCode":
                result = xwhkingClient.invokeGetQrCode(invokeParam.getContent());
                break;
            default:
                throw new BusinessException(ErrorCode.PARAMS_ERROR,"url不存在");
        }
        return ResultUtils.success(result);
    }



}
