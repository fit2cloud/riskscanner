package io.riskscanner.dto;


import io.riskscanner.base.domain.Resource;

public class ResourceLogDTO extends TaskItemLogDTO{

    private Resource resource;

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }
}
