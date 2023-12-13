package com.xwhking.yuapi.model.entity;

import lombok.Data;

import java.util.List;
@Data
public class Expression {
    private Integer code;
    private List<List<String>> result;
    private String msg;
}
