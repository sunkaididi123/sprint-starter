package com.xwhking.yuapi.model.enums;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum SentenceType {
    Origin("原创","origin"),
    Others("其他","others"),
    Carton("动画","carton"),
    Philosophy("哲学","philosophy"),
    Movie("影视","movie"),
    Clever("抖机灵","clever"),
    Literary("文学","literary"),
    Game("游戏","game"),
    Comic("漫画","comic"),
    NetEaseCloud("网易云","netEaseCloud"),
    Internet("网络","internet"),
    Poem("诗词","poem"),
    CheckSoup("鸡汤","checkSoup");
    private String type;
    private String value;
    SentenceType(String type,String value){
        this.type = type;
        this.value = value;
    }
    /**
     * 获取值列表
     *
     * @return
     */
    public static List<String> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static SentenceType getEnumByValue(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (SentenceType anEnum : SentenceType.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }

    public String getType() {
        return type;
    }

}
