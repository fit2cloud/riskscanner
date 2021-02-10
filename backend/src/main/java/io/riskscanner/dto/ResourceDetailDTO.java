package io.riskscanner.dto;

import io.riskscanner.base.domain.ResourceItem;
import io.riskscanner.base.domain.ResourceWithBLOBs;

/**
 * @author maguohao
 */
public class ResourceDetailDTO extends ResourceItem {

    private String customData;
    private TaskDTO taskDTO;
    private ResourceWithBLOBs resourceWithBLOBs;

    public String getCustomData() {
        return customData;
    }

    public void setCustomData(String customData) {
        this.customData = customData;
    }

    public TaskDTO getTaskDTO() {
        return taskDTO;
    }

    public void setTaskDTO(TaskDTO taskDTO) {
        this.taskDTO = taskDTO;
    }

    public ResourceWithBLOBs getResourceWithBLOBs() {
        return resourceWithBLOBs;
    }

    public void setResourceWithBLOBs(ResourceWithBLOBs resourceWithBLOBs) {
        this.resourceWithBLOBs = resourceWithBLOBs;
    }
}
