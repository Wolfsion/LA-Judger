package com.lavson.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求
 *
 * @author LA
 * @version 1.0
 * 2024/5/7 - 20:12
 */
@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    private String userAccount;

    private String userPassword;

    private String checkPassword;
}
