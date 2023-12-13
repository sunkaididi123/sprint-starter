package com.xwhking.yuapi.aop;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xwhking.yuapi.annotation.InvokeInterface;
import com.xwhking.yuapi.common.ErrorCode;
import com.xwhking.yuapi.exception.BusinessException;
import com.xwhking.yuapi.exception.ThrowUtils;
import com.xwhking.yuapi.model.entity.InterfaceInfo;
import com.xwhking.yuapi.model.entity.User;
import com.xwhking.yuapi.service.InterfaceInfoService;
import com.xwhking.yuapi.service.UserInterfaceInfoService;
import com.xwhking.yuapi.service.UserService;
import com.xwhking.yuapi.utils.SignUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class InvokeInterceptor {
    @Resource
    UserInterfaceInfoService userInterfaceInfoService;
    @Resource
    InterfaceInfoService interfaceInfoService;
    @Resource
    UserService userService;
    @Around("@within(com.xwhking.yuapi.annotation.InvokeInterface)")
    @Transactional
    public Object doInterceptor(ProceedingJoinPoint joinPoint){
        System.out.println("Before");
        // 用户调用之前进行用户的权限进行验证，验证的流程
        // 1. 从请求头中拿到用户的 信息 并且拿到用用户的 id 、 accessKey 和 secretKey 一起生成的签名。
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        String sign = request.getHeader("sign");
        ThrowUtils.throwIf(sign == null || "".equals(sign), ErrorCode.PARAMS_ERROR,"请输入正确的签名");
        Long requestUserId = Long.parseLong(request.getHeader("userId"));
        // 2. 然后通过拿到的用户名去数据库请求用户的 accessKey 和 secretKey 以及用户的 Id 生成签名
        User databaseUser = userService.getById(requestUserId);
        ThrowUtils.throwIf(databaseUser == null , ErrorCode.PARAMS_ERROR,"用户ID ERROR");
        String rightAccessKey = databaseUser.getAccessKey();
        String rightSecretKey = databaseUser.getSecretKey();
        Long rightUserId = databaseUser.getId();
        String rightSign = SignUtils.getSign(rightAccessKey,rightSecretKey,rightUserId);
        ThrowUtils.throwIf(!sign.equals(rightSign),ErrorCode.NO_AUTH_ERROR,"签名不正确");
        // 3. 两次生成的签名进行比对，如果通过就放行，如果不通过就不放行，抛出异常
        Object obj;
        try {
            obj = joinPoint.proceed();
        } catch (Throwable e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"接口调用失败");
        }
        Long userId = rightUserId;
        LambdaQueryWrapper<InterfaceInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        String uri = request.getRequestURI();
        StringBuffer stringBuffer = request.getRequestURL();
        lambdaQueryWrapper.eq(InterfaceInfo::getUri,request.getRequestURI());
        InterfaceInfo interfaceInfo = interfaceInfoService.getOne(lambdaQueryWrapper);
        ThrowUtils.throwIf(interfaceInfo == null, ErrorCode.SYSTEM_ERROR,"接口Uri不存在");
        userInterfaceInfoService.recordInvokeOne(userId,interfaceInfo.getId());
        return obj;
    }
}
