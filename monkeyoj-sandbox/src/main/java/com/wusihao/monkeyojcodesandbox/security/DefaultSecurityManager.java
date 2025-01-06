package com.wusihao.monkeyojcodesandbox.security;

import java.security.Permission;

/**
 * @author: wusihao
 * @date: 2025/1/4 17:10
 * @version: 1.0
 * @description: 放开所有权限
 */
public class DefaultSecurityManager extends SecurityManager{
    // 检查所有权限
    @Override
    public void checkPermission(Permission perm) {
        System.out.println("默认不做任何权限限制");
        System.out.println(perm);
        // 使用super.checkPermission(perm);默认禁用所有权限
//        super.checkPermission(perm);
    }
}
