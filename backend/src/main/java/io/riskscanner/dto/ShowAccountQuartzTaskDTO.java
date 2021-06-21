package io.riskscanner.dto;


import io.riskscanner.base.domain.CloudAccountQuartzTask;
import io.riskscanner.base.domain.CloudAccountQuartzTaskRelaLog;
import io.riskscanner.base.domain.CloudAccountQuartzTaskRelation;

import java.util.List;

public class ShowAccountQuartzTaskDTO extends CloudAccountQuartzTask{

   private List<ShowAccountQuartzTaskRelationDto> quartzTaskRelationDtos;

    public List<ShowAccountQuartzTaskRelationDto> getQuartzTaskRelationDtos() {
        return quartzTaskRelationDtos;
    }

    public void setQuartzTaskRelationDtos(List<ShowAccountQuartzTaskRelationDto> quartzTaskRelationDtos) {
        this.quartzTaskRelationDtos = quartzTaskRelationDtos;
    }
}
