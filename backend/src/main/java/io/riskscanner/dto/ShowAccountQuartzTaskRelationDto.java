package io.riskscanner.dto;

import io.riskscanner.base.domain.CloudAccountQuartzTaskRelation;

import java.util.List;

public class ShowAccountQuartzTaskRelationDto extends CloudAccountQuartzTaskRelation {

    private List<TaskDTO> taskList;

    public List<TaskDTO> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<TaskDTO> taskList) {
        this.taskList = taskList;
    }
}
