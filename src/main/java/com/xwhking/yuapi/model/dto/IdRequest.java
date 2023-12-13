package com.xwhking.yuapi.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 专门用来接收只有一个 Id 参数的请求
 */
@Data
public class IdRequest implements Serializable {
    private Long id;
    private static final long serialVersionUID = 4357640657609543233L;
}
