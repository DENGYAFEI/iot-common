package com.iot.common.model.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.iot.common.mysql.model.pojo.entity.BaseEntity;
import com.iot.common.model.valid.Insert;
import com.iot.common.model.valid.Update;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;

/**
 * @Author Orchid
 * @Create 2024/4/3
 * @Remark 设备与模版继承关系表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString( callSuper = true )
@EqualsAndHashCode( callSuper = true )
@TableName( "iot_profile_bind" )
@Schema( description = "设备与模版继承关系表实体" )
public class ProfileBind extends BaseEntity {

    /**
     * 模版ID
     */
    @Schema( description = "模版ID", requiredMode = Schema.RequiredMode.REQUIRED )
    @NotBlank(
            message = "Profile id can't be empty",
            groups = { Insert.class, Update.class }
    )
    @TableField( "profile_id" )
    private String profileId;

    /**
     * 设备ID
     */
    @Schema( description = "设备ID", requiredMode = Schema.RequiredMode.REQUIRED )
    @NotBlank(
            message = "Device id can't be empty",
            groups = { Insert.class, Update.class }
    )
    @TableField( "device_id" )
    private String deviceId;

    public ProfileBind( String id, String profileId, String deviceId ) {
        super.setId( id );
        this.profileId = profileId;
        this.deviceId =deviceId;
    }

}
