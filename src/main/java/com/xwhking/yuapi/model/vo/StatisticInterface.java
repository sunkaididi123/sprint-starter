package com.xwhking.yuapi.model.vo;

import lombok.Data;
import java.util.List;

@Data
public class StatisticInterface {
    /**
     * 所有接口总的调用次数
     */
    private Integer totalNum;
    /**
     * 某个接口调用次数统计
     */
    private List<InterfaceInfoStatistic> perInterfacesStatistics;
}
