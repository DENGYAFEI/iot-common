package com.iot.common.log.factory;

import com.iot.common.log.pojo.dto.LogInfoDTO;

/**
 * @Author Orchid
 * @Create 2024/4/1
 * @Remark Log日志工厂接口
 */
public interface LogFactory {

    void createLog( LogInfoDTO dto );

}
