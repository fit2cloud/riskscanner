package io.riskscanner.dto;

import io.riskscanner.base.domain.Schedule;

public class ScheduleDao extends Schedule {

    private String resourceName;
    private String userName;

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
