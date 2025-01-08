package com.iot.common.model.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.iot.common.mysql.model.pojo.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * @Author Orchid
 * @Create 2024/4/3
 * @Remark 点位与模版绑定关系表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString( callSuper = true )
@EqualsAndHashCode( callSuper = true )
@TableName( "iot_point_bind" )
@Schema( description = "点位与模版绑定关系表实体" )
public class PointBind extends BaseEntity {

    /**
     * 模版ID
     */
    @Schema( description = "模版ID", requiredMode = Schema.RequiredMode.REQUIRED )
    @TableField( "profile_id" )
    private String profileId;

    /**
     * 设备ID
     */
    @Schema( description = "点位ID", requiredMode = Schema.RequiredMode.REQUIRED )
    @TableField( "point_id" )
    private String pointId;

    public PointBind( String id, String profileId, String pointId ) {
        super.setId( id );
        this.profileId = profileId;
        this.pointId =pointId;
    }

}
