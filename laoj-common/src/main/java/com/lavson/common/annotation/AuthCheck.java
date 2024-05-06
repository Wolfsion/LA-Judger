package com.lavson.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限校验
 *
 * @author LA
 * @version 1.0
 * 2024/5/6 - 20:54
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthCheck {
    /**
     * 调用方法者必须拥有的角色身份
     * @return
     */
    String needRole() default "";
}
