package com.wusihao.monkeyojbackendmodel.model.dto.user;


import lombok.Data;
import lombok.EqualsAndHashCode;
import wusihao.monkeyojbackendcommon.common.PageRequest;

import java.io.Serializable;

/**
 * 用户查询请求
 *
 * @author <a href="https://github.com/wsh1931">吴思豪</a>
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserQueryRequest extends PageRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 开放平台id
     */
    private String unionId;

    /**
     * 公众号openId
     */
    private String mpOpenId;

    /**
     * 用户昵称
     */
    private String userName;

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