package com.sxmd.content.baseuserauthority.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


/**
 * Description:
 *
 * @author sxmd
 * @date Version 1.0
 */
@Data
@ApiModel(value = "用户授权")
@JsonIgnoreProperties({"id"})
public class BaseUserAuthorityAuthModel {

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "权限id集合")
    private List<String> authorityIds;

}