package com.lavson.common.constant;

/**
 * 用户常量
 *
 * @author LA
 * @version 1.0
 * 2024/5/6 - 21:01
 */
public interface UserConstant {
    /**
     * 用户登录态键
     */
    String USER_LOGIN_STATE = "user_login";

    /**
     * 用户默认密码
     */
    String USER_DEFAULT_PASS = "12345Q1@";

    /**
     * 加盐
     */
    String SALT = "lavson";

    //  region 权限

    /**
     * 默认角色
     */
    String DEFAULT_ROLE = "user";

    /**
     * 管理员角色
     */
    String ADMIN_ROLE = "admin";

    /**
     * 被封号
     */
    String BAN_ROLE = "ban";

    // endregion
}
