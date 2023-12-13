package com.xwhking.yuapistarter.config;

import com.xwhking.yuapistarter.client.XWHKINGClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@ConfigurationProperties(prefix = "com.xwhking.client")
@Data
@ComponentScan
public class ClientConfig {
    private String accessKey;
    private String secretKey;
    private Long userId;

    @Bean
    public XWHKINGClient getClient(){
        return new XWHKINGClient(accessKey,secretKey,userId);
    }
}
