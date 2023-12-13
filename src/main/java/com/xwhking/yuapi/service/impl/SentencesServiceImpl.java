package com.xwhking.yuapi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xwhking.yuapi.model.entity.Sentences;
import com.xwhking.yuapi.service.SentencesService;
import com.xwhking.yuapi.mapper.SentencesMapper;
import org.springframework.stereotype.Service;

/**
* @author 28374
* @description 针对表【sentences(句子)】的数据库操作Service实现
* @createDate 2023-12-04 19:16:57
*/
@Service
public class SentencesServiceImpl extends ServiceImpl<SentencesMapper, Sentences>
    implements SentencesService{

}




