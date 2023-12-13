package com.xwhking.yuapi;
import com.xwhking.yuapi.config.WxOpenConfig;
import com.xwhking.yuapistarter.client.XWHKINGClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

/**
 * 主类测试
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@SpringBootTest
class MainApplicationTests {

    @Resource
    private WxOpenConfig wxOpenConfig;
    @Resource
    RedisTemplate<String , Object> redisTemplate;
    @Resource
    private XWHKINGClient xwhkingClient;

    @Test
    void testStarter(){
        System.out.println(xwhkingClient.invokeGetQrCode("Hello World!"));
    }
}
