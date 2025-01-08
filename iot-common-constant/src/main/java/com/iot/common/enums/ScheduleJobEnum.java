package com.iot.common.enums;

public enum ScheduleJobEnum {

    ENABLE(1, "启用"),
    DISABLE(0, "禁用");

    private final Integer code;

    private final String desc;

    ScheduleJobEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static String getDesc(Integer code) {
        for (ScheduleJobEnum item : ScheduleJobEnum.values()) {
            if (item.getCode().equals(code)) {
                return item.getDesc();
            }
        }
        return null;
    }

    public static Integer getCode(String desc) {
        for (ScheduleJobEnum item : ScheduleJobEnum.values()) {
            if (item.getDesc().equals(desc)) {
                return item.getCode();
            }
        }
        return null;
    }

    public static ScheduleJobEnum getByCode(Integer code) {
        for (ScheduleJobEnum item : ScheduleJobEnum.values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        throw new RuntimeException("未找到对应的枚举");
    }
}
