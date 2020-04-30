package com.geofuturelab.pswmm.Entity;

import com.alibaba.fastjson.JSONObject;

public class SOMessage {

    private String type;

    private JSONObject userInfo;

    private JSONObject operation;

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public JSONObject getUserInfo() {
        return userInfo;
    }

    public JSONObject getOperation() {
        return operation;
    }

    public void setOperation(JSONObject operation) {
        this.operation = operation;
    }

    public void setUserInfo(JSONObject userInfo) {
        this.userInfo = userInfo;
    }
}
