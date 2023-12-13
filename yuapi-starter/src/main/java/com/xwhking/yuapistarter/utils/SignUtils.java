package com.xwhking.yuapistarter.utils;
import cn.hutool.crypto.SecureUtil;
public class SignUtils {
    public static String getSign(String accessKey,String secretKey,Long userId){
        return SecureUtil.sha256(accessKey + "xwhking" + secretKey + "api" + userId.toString());
    }
}
