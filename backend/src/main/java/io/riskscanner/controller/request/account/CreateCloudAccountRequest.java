package io.riskscanner.controller.request.account;

import io.swagger.annotations.ApiModelProperty;

/**
 * maguohao
 */
public class CreateCloudAccountRequest {

    @ApiModelProperty(value = "名称", required = true)
    private String name;
    @ApiModelProperty(value = "插件ID", required = true)
    private String pluginId;
    @ApiModelProperty(value = "插件图标", required = true)
    private String pluginIcon;
    @ApiModelProperty(value = "凭据", required = true)
    private String credential;

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

    public String getPluginIcon() {
        return pluginIcon;
    }

    public void setPluginIcon(String pluginIcon) {
        this.pluginIcon = pluginIcon;
    }

    public String getCredential() {
        return credential;
    }

    public void setCredential(String credential) {
        this.credential = credential;
    }

}
