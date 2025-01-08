package com.iot.common.web.entity.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 权限信息
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthInfoBO {

    /**
     * 租户 ID
     */
    private String tenantId;

    /**
     * 租户名称
     */
    private String tenantName;

    /**
     * 用户 ID
     */
    private String userId;

    /**
     * 用户别名
     */
    private String nickName;

    /**
     * 用户名称
     */
    private String userName;

}
