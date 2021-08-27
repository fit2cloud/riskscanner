package io.riskscanner.controller.request.msg;


import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

public class MsgSettingRequest implements Serializable {

    @ApiModelProperty("消息类型ID")
    private Long typeId;

    @ApiModelProperty("消息渠道ID")
    private Long channelId;

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }
}
