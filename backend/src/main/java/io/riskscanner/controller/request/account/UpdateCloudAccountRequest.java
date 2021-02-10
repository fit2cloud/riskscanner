package io.riskscanner.controller.request.account;

import io.swagger.annotations.ApiModelProperty;

/**
 * maguohao
 */
public class UpdateCloudAccountRequest {

    @ApiModelProperty(value = "云账号ID", required = true)
    private String id;

    @ApiModelProperty(value = "名称", required = true)
    private String name;

    @ApiModelProperty(value = "插件ID,不允许改变")
    private String pluginId;

    @ApiModelProperty(value = "凭据", required = true)
    private String credential;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPluginId() {
        return pluginId;
    }

    public void setPluginId(String pluginId) {
        this.pluginId = pluginId;
    }

    public String getCredential() {
        return credential;
    }

    public void setCredential(String credential) {
        this.credential = credential;
    }
}
