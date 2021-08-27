package io.riskscanner.dto;

import io.riskscanner.base.domain.Msg;
import io.swagger.annotations.ApiModelProperty;

public class MsgGridDto extends Msg {

    @ApiModelProperty("回调路由")
    private String router;
    @ApiModelProperty("回调函数")
    private String callback;

    public String getRouter() {
        return router;
    }

    public void setRouter(String router) {
        this.router = router;
    }

    public String getCallback() {
        return callback;
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }
}
