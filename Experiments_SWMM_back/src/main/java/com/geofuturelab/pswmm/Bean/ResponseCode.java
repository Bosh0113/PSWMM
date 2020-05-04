package com.geofuturelab.pswmm.Bean;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.ResponseMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: David.Xiao
 * @Date: 2019/11/21 16:28
 * @Description:
 */
@NoArgsConstructor
@AllArgsConstructor
public enum ResponseCode {
    // 相当于public static final ResponseCode OK = new ResponseCode(1,"操作成功");
    OK(1,"操作成功"),
    SIGN_IN_OK(2,"登录成功"),
    LOGOUT_OK(3,"注销登录成功"),
    SIGN_IN_INPUT_FAIL(-4,"账号或密码错误"),
    SIGN_IN_FAIL(-3,"登录失败"),
    FAIL(-1,"操作失败"),
    LOGOUT_FAIL(-2,"注销登录失败"),
    SING_IN_INPUT_EMPTY(-5,"账户和密码均不能为空"),
    NOT_SING_IN(-6,"用户未登录或身份异常"),
    SIGN_UP_FAIL_EMAIL_EXIST(-7,"注册邮箱已存在"),
    SIGN_UP_FAIL(-8,"注册失败");
//    LOGIN_FAIL_EMAIL_NOT_EXIST(-9,"添加实例失败");
//    LOGIN_FAIL_PASSWORD_ERROR(-10,"密码错误");

    public Integer code;

    public String msg;

    public static List<ResponseMessage> getArrayMessage(){
        ArrayList<ResponseMessage> responseMessages = new ArrayList<>();
        for (ResponseCode statusEnum : ResponseCode.values()) {
            responseMessages.add(new ResponseMessageBuilder()
                    .code(statusEnum.code)
                    .message(statusEnum.msg)
                    .build());
        }
        return responseMessages;
    }
}
