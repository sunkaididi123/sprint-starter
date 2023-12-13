package com.xwhking.yuapi.utils;

import cn.hutool.crypto.SecureUtil;

/**
 * 生成签名的类
 */
public class SignUtils {
    public static String getSign(String accessKey, String secretKey, Long userId) {
        return SecureUtil.sha256(accessKey + "xwhking" + secretKey + "api" + userId.toString());
    }

}
