package com.iot.common.model.dto;

import com.iot.common.enums.DeviceCommandTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author Orchid
 * @Create 2024/4/3
 * @Remark 设备指令
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceCommandDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 指令类型
     */
    private DeviceCommandTypeEnum type;

    /**
     * 指令内容
     */
    private String content;

    /**
     * 创建时间
     */
    private Date createTime;

    public DeviceCommandDTO( DeviceCommandTypeEnum type, String content ) {
        this.type = type;
        this.content = content;
        this.createTime = new Date();
    }

    /**
     * @Author Orchid
     * @Create 2024/4/3
     * @Remark 设备读指令
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeviceRead implements Serializable {
        private static final long serialVersionUID = 1L;

        /**
         * 设备ID
         */
        private String deviceId;

        /**
         * 位号ID
         */
        private String pointId;


        /**
         * 创建时间
         */
        private Date createTime;

        public DeviceRead( String deviceId, String pointId ) {
            this.deviceId = deviceId;
            this.pointId = pointId;
            this.createTime = new Date();
        }
    }

    /**
     * @Author Orchid
     * @Create 2024/4/3
     * @Remark 设备写指令
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeviceWrite implements Serializable {
        private static final long serialVersionUID = 1L;

        /**
         * 设备ID
         */
        private String deviceId;

        /**
         * 位号ID
         */
        private String pointId;

        /**
         * 待写入的值
         */
        private String value;

        /**
         * 创建时间
         */
        private Date createTime;

        public DeviceWrite( String deviceId, String pointId, String value ) {
            this.deviceId = deviceId;
            this.pointId = pointId;
            this.value = value;
            this.createTime = new Date();
        }
    }
}
