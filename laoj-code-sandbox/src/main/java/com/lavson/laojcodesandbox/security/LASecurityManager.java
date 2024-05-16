package com.lavson.laojcodesandbox.security;

import com.lavson.common.constant.SandBoxConstant;
import com.lavson.laojcodesandbox.service.CodeSandbox;

import java.security.Permission;

/**
 * todo
 *
 * @author LA
 * @version 1.0
 * 2024/5/14 - 21:11
 */
public class LASecurityManager extends SecurityManager {


    // 检查所有的权限
    @Override
    public void checkPermission(Permission perm) {
//        super.checkPermission(perm);
    }

    // 禁止执行文件
    @Override
    public void checkExec(String cmd) {
        throw new SecurityException("checkExec 权限异常：" + cmd);
    }

    // 检测程序是否允许读文件
    @Override
    public void checkRead(String file) {
        System.out.println(file);
        if (file.contains(SandBoxConstant.READ_SUB_DIR)) {
            return;
        }
        throw new SecurityException("checkRead 权限异常：" + file);
    }

    // 禁止写文件
    @Override
    public void checkWrite(String file) {
        throw new SecurityException("checkWrite 权限异常：" + file);
    }

    // 禁止删除文件
    @Override
    public void checkDelete(String file) {
        throw new SecurityException("checkDelete 权限异常：" + file);
    }

    // 禁止连接网络
    @Override
    public void checkConnect(String host, int port) {
        throw new SecurityException("checkConnect 权限异常：" + host + ":" + port);
    }
}
