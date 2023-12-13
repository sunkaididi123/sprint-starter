package com.xwhking.yuapi.mapper;

import com.xwhking.yuapi.model.entity.UserInterfaceInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xwhking.yuapi.model.vo.InterfaceInfoStatistic;
import java.util.List;

/**
* @author 28374
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Mapper
* @createDate 2023-12-03 16:04:14
* @Entity com.xwhking.yuapi.model.entity.UserInterfaceInfo
*/
public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {
    Integer getTotalNumInteger();

    List<InterfaceInfoStatistic> getPerInterfaceStatistic();
}




