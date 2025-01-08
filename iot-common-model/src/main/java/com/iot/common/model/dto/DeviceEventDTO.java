package com.iot.common.model.dto;

import com.iot.common.enums.DeviceEventTypeEnum;
import com.iot.common.enums.DriverStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author Orchid
 * @Create 2024/4/3
 * @Remark 设备事件
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceEventDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 事件类型
     */
    private DeviceEventTypeEnum type;

    /**
     * 事件内容
     */
    private String content;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * @Author Orchid
     * @Create 2024/4/3
     * @Remark 设备事件
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeviceStatus implements Serializable {
        private static final long serialVersionUID = 1L;

        /**
         * 驱动服务名称
         */
        private String serviceName;

        /**
         * 驱动状态
         */
        private DriverStatusEnum status;

        /**
         * 创建时间
         */
        private Date createTime;

        public DeviceStatus( String serviceName, DriverStatusEnum status ) {
            this.serviceName = serviceName;
            this.status = status;
            this.createTime = new Date();
        }
    }

}
