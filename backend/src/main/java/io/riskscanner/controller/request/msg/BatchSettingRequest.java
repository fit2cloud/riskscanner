package io.riskscanner.controller.request.msg;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

public class BatchSettingRequest implements Serializable {
    @ApiModelProperty("消息类型ID集合")
    private List<Long> typeIds;
    @ApiModelProperty("消息类型ID")
    private Long channelId;
    @ApiModelProperty("订阅状态")
    private Boolean enable;

    public List<Long> getTypeIds() {
        return typeIds;
    }

    public void setTypeIds(List<Long> typeIds) {
        this.typeIds = typeIds;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }
}
