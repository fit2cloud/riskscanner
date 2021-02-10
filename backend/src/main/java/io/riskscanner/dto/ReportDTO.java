package io.riskscanner.dto;

import io.riskscanner.base.domain.RuleInspectionReport;
import io.riskscanner.base.domain.Task;

import java.util.List;


public class ReportDTO extends RuleInspectionReport {

    private String Status;

    private List<Task> taskList;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }
}
