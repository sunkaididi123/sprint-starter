package com.xwhking.yuapi.model.dto.interfaceInfo;

import lombok.Data;

import java.io.Serializable;

@Data
public class InterfaceInvokeRequest implements Serializable {
    private Long id;
    private String userRequestParams;
    private String url;
    private static final long serialVersionUID = -8779117448808761484L;
}
