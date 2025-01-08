package com.iot.common.log.pojo.dto;

import com.iot.common.log.annotation.Log;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aspectj.lang.reflect.MethodSignature;

import java.time.LocalDateTime;

/**
 * @Author Orchid
 * @Create 2024/4/1
 * @Remark 日志信息DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogInfoDTO {

    /**
     * log注解信息
     */
    private Log logAnnotation;

    /**
     * 目标代理类class
     */
    private Class< ? > targetClass;

    /**
     * 方法签名
     */
    private MethodSignature methodSignature;

    /**
     * 实际参数
     */
    private Object[] args;

    /**
     * 开始时间
     */
    private LocalDateTime start;

    /**
     * 结束时间
     */
    private LocalDateTime end;

    /**
     * 返回结果
     */
    private Object proceed;

    /**
     * 是否抛出异常
     */
    private Boolean whetherException;

    /**
     * 异常信息
     */
    private Exception exception;

}
