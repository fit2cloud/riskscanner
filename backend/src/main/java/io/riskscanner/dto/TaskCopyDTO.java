package io.riskscanner.dto;


import io.riskscanner.base.domain.Rule;
import io.riskscanner.base.domain.TaskItem;
import io.riskscanner.base.rs.SelectTag;

import java.util.LinkedList;
import java.util.List;

/**
 * @author maguohao
 */
public class TaskCopyDTO {
    private List<TaskItem> taskItemList;
    private Rule rule;
    private List<String> ruleTagMappingList;
    private List<SelectTag> SelectTags = new LinkedList<>();

    public List<TaskItem> getTaskItemList() {
        return taskItemList;
    }

    public void setTaskItemList(List<TaskItem> taskItemList) {
        this.taskItemList = taskItemList;
    }

    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }

    public List<SelectTag> getSelectTags() {
        return SelectTags;
    }

    public void setSelectTags(List<SelectTag> selectTags) {
        SelectTags = selectTags;
    }

    public List<String> getRuleTagMappingList() {
        return ruleTagMappingList;
    }

    public void setRuleTagMappingList(List<String> ruleTagMappingList) {
        this.ruleTagMappingList = ruleTagMappingList;
    }
}
