package com.xwhking.yuapi.controller;

import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.gson.Gson;
import com.xwhking.yuapi.annotation.InvokeInterface;
import com.xwhking.yuapi.common.BaseResponse;
import com.xwhking.yuapi.common.ErrorCode;
import com.xwhking.yuapi.common.ResultUtils;
import com.xwhking.yuapi.exception.BusinessException;
import com.xwhking.yuapi.exception.ThrowUtils;
import com.xwhking.yuapi.model.entity.Daily;
import com.xwhking.yuapi.model.entity.Expression;
import com.xwhking.yuapi.model.entity.Sentences;
import com.xwhking.yuapi.model.enums.SentenceType;
import com.xwhking.yuapi.service.SentencesService;
import com.xwhking.yuapistarter.config.ClientConfig;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 专门来写接口调用的 Controller
 */
@RestController
@RequestMapping("/invoke")
@InvokeInterface
public class InvokeController {
    @Resource
    private SentencesService sentencesService;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    public static final String SENTENCE_REDIS_PREFIX = "com:xwhking:sentence:";

    /**
     * 调用一句话的接口
     * todo 限制调用次数，并且通过切面编程把用户的调用次数进行统计
     * todo 修改传入参数，因为调用的时候要进行用户的鉴权，所以用户的鉴权要放入切面编程中。基本功能是实现了的就行暂时
     *
     * @param type
     * @return
     */
    @GetMapping("/getOneSentence")
    public BaseResponse<Sentences> getOneSentence(String type) {
        SentenceType sentenceType = SentenceType.getEnumByValue(type);
        if (sentenceType == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "不存在该类型的一句话");
        }
        List<Sentences> resultList = (List<Sentences>) redisTemplate.opsForValue().get(SENTENCE_REDIS_PREFIX + sentenceType.getValue());
        Random random = new Random(new Date().getTime());
        if (resultList != null && resultList.size() > 0) {
            return ResultUtils.success(resultList.get(random.nextInt(resultList.size())));
        }
        LambdaQueryWrapper<Sentences> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Sentences::getType, sentenceType.getType());
        resultList = sentencesService.list(lambdaQueryWrapper);
        redisTemplate.opsForValue().set(SENTENCE_REDIS_PREFIX + sentenceType.getValue(), resultList, 1, TimeUnit.DAYS);
        return ResultUtils.success(resultList.get(random.nextInt(resultList.size())));
    }

    /**
     * 获取金山词霸的每日一句鼓励以及分享的图片
     *
     * @return
     */
    @GetMapping("/daily")
    public BaseResponse<Daily> getDaily() {
        HttpResponse body = HttpUtil.createGet("https://open.iciba.com/dsapi/").execute();
        String result = body.body();
        Gson gson = new Gson();
        Daily daily = gson.fromJson(result, Daily.class);
        return ResultUtils.success(daily);
    }

    /**
     * 获取相应内容的二维码 API
     * @param content
     * @return
     */
    @GetMapping("/getQrCode")
    public BaseResponse<String> getQrCode(String content) {
        ThrowUtils.throwIf(content == null || "".equals(content),ErrorCode.PARAMS_ERROR,"内容为空");
        QrConfig qrConfig = QrConfig.create();
        qrConfig.setBackColor(Color.WHITE);
        qrConfig.setForeColor(Color.BLACK);
        return ResultUtils.success(QrCodeUtil.generateAsBase64(content, qrConfig, QrCodeUtil.QR_TYPE_SVG));
    }

    /**
     * 获取相应表情包的 API
     * @param keyword
     * @return
     */
    @GetMapping("/getExpression")
    public BaseResponse<List<String>> getExpression(String keyword){
        Map<String,Object> map = new HashMap<>();
        map.put("keyword",keyword);
        String s = HttpUtil.get("https://api.oioweb.cn/api/picture/emoticon", map);
        Gson gson = new Gson();
        Expression result = gson.fromJson(s,Expression.class);
        List<List<String>> resultList = result.getResult();
        List<String> returnResult = new ArrayList<>();
        for(List<String> ls: resultList){
            returnResult.add(ls.get(0));
        }
        return ResultUtils.success(returnResult );
    }
}
