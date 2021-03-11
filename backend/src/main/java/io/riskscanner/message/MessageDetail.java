package io.riskscanner.message;

import io.riskscanner.base.domain.MessageTask;

import java.util.ArrayList;
import java.util.List;

public class MessageDetail extends MessageTask {

    private List<String> userIds = new ArrayList<>();

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }

}
