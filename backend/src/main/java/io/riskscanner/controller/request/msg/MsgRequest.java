package io.riskscanner.controller.request.msg;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

public class MsgRequest implements Serializable {

    private static final long serialVersionUID = 1920091635946508658L;

    @ApiModelProperty("消息类型ID")
    private Long type;

    @ApiModelProperty("是否订阅")
    private Boolean status;

    @ApiModelProperty("排序描述")
    private List<String> orders;

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<String> getOrders() {
        return orders;
    }

    public void setOrders(List<String> orders) {
        this.orders = orders;
    }
}
