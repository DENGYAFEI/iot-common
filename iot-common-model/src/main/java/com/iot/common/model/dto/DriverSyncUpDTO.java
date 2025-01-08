package com.iot.common.model.dto;

import com.iot.common.model.model.Driver;
import com.iot.common.model.model.DriverAttribute;
import com.iot.common.model.model.PointAttribute;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Author Orchid
 * @Create 2024/4/3
 * @Remark
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverSyncUpDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String client;

    private Driver driver;

    private List< DriverAttribute > driverAttributes;

    private List< PointAttribute > pointAttributes;

}
