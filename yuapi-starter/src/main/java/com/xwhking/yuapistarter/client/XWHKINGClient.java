package com.xwhking.yuapistarter.client;

import cn.hutool.http.HttpRequest;
import com.xwhking.yuapistarter.utils.SignUtils;
import lombok.Data;

import java.util.HashMap;
@Data
public class XWHKINGClient {
    private String accessKey;
    private String secretKey;
    private Long userId;
    private String sign;
    public XWHKINGClient(String accessKey,String secretKey,Long userId){
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.userId = userId;
        this.sign = SignUtils.getSign(accessKey,secretKey,userId);
    }
    public static final String host = "localhost:8080";
    public Object invokeDaily(){
        String uri = "/api/invoke/daily";
        String result = HttpRequest.get(host + uri)
                .header("sign", sign)//头信息，多个头信息多次调用此方法即可
                .header("userId",userId.toString())
                .execute().body();
        return result;
    }
    public Object invokeGetOneSentence(String type){
        String uri = "/api/invoke/getOneSentence";
        HashMap<String,Object> map = new HashMap<String, Object>();
        map.put("type",type);
        String result = HttpRequest.get(host + uri)
                .header("sign", sign)//头信息，多个头信息多次调用此方法即可
                .header("userId",userId.toString())
                .form(map)
                .execute().body();
        return result;
    }
    public Object invokeGetExpression(String keyword){
        String uri = "/api/invoke/getExpression";
        HashMap<String,Object> map = new HashMap<String, Object>();
        map.put("keyword",keyword);
        String result = HttpRequest.get(host + uri)
                .header("sign", sign)//头信息，多个头信息多次调用此方法即可
                .header("userId",userId.toString())
                .form(map)
                .execute().body();
        return result;
    }
    public Object invokeGetQrCode(String content){
        String uri = "/api/invoke/getQrCode";
        HashMap<String,Object> map = new HashMap<String, Object>();
        map.put("content",content);
        String result = HttpRequest.get(host + uri)
                .header("sign", sign)//头信息，多个头信息多次调用此方法即可
                .header("userId",userId.toString())
                .form(map)
                .execute().body();
        return result;
    }

    public static void main(String[] args) {
        XWHKINGClient xwhkingClient = new XWHKINGClient("admin123","admin123",1725120515803697154l);
        System.out.println(xwhkingClient.invokeDaily());
    }
}
