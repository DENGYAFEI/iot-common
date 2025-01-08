package com.iot.common.excel.model.pojo.dto;

import com.iot.common.mysql.model.pojo.dto.PageQueryDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Orchid
 * @Create 2024/6/1
 * @Remark
 */
@EqualsAndHashCode( callSuper = true )
@Data
public class ExportPageDTO extends PageQueryDTO {

    List< Header > headers = new ArrayList<>();

}
