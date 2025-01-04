package com.wusihao.monkeyojcodesandbox.security;

import java.security.Permission;

/**
 * @author: wusihao
 * @date: 2025/1/4 17:21
 * @version: 1.0
 * @description: 限制所有权限
 */
public class DenySecurityManager extends SecurityManager{

    @Override
    public void checkPermission(Permission perm) {
        throw new SecurityException("权限异常");
//        super.checkPermission(perm);
    }
}
