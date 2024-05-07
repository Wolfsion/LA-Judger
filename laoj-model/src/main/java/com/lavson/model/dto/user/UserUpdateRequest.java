package com.lavson.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户更新请求（包括id、角色）
 *
 * @author LA
 * @version 1.0
 * 2024/5/7 - 20:13
 */
@Data
public class UserUpdateRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 简介
     */
    private String userProfile;

    /**
     * 用户角色：user/admin/ban
     */
    private String userRole;

    private static final long serialVersionUID = 1L;
}
