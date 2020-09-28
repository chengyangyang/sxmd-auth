package com.sxmd.content.baseauthority.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Description: 添加权限实体
 * 字段需要修改，需移至EditModel中
 *
 * @author sxmd
 * @date Version 1.0
 */
@Data
@ApiModel(value = "删除权限")
public class BaseAuthorityDeleteModel {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "是否强制删除")
    private boolean forceDelete;

}