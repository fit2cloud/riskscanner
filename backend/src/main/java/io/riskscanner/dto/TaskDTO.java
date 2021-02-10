package io.riskscanner.dto;


import io.riskscanner.base.domain.Task;
import io.riskscanner.base.domain.TaskItem;

import java.util.List;

/**
 * @author maguohao
 */
public class TaskDTO extends Task {
    private String customData;
    private String details;
    private Integer count;
    private String ruleId;
    private String taskItemCreateTime;

    public String getCustomData() {
        return customData;
    }

    public void setCustomData(String customData) {
        this.customData = customData;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public String getTaskItemCreateTime() {
        return taskItemCreateTime;
    }

    public void setTaskItemCreateTime(String taskItemCreateTime) {
        this.taskItemCreateTime = taskItemCreateTime;
    }
}
