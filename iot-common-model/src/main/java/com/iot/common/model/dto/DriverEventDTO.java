package com.iot.common.model.dto;

import com.iot.common.enums.DriverEventTypeEnum;
import com.iot.common.enums.DriverStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author Orchid
 * @Create 2024/4/3
 * @Remark 驱动事件
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverEventDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 事件类型
     */
    private DriverEventTypeEnum type;

    /**
     * 事件内容
     */
    private String content;

    /**
     * 创建时间
     */
    private Date createTime;

    public DriverEventDTO( DriverEventTypeEnum type, String content ) {
        this.type = type;
        this.content = content;
        this.createTime = new Date();
    }

    /**
     * @Author Orchid
     * @Create 2024/4/3
     * @Remark 驱动事件
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DriverStatus implements Serializable {

        private static final long serialVersionUID = 1L;

        /**
         * 驱动ID
         */
        private String driverId;

        /**
         * 驱动状态
         */
        private DriverStatusEnum status;

        /**
         * 创建时间
         */
        private Date createTime;

        public DriverStatus( String driverId, DriverStatusEnum status ) {
            this.driverId = driverId;
            this.status = status;
            this.createTime = new Date();
        }
    }

}
