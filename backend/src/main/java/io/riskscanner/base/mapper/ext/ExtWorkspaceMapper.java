package io.riskscanner.base.mapper.ext;

import io.riskscanner.controller.request.WorkspaceRequest;
import io.riskscanner.dto.WorkspaceDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ExtWorkspaceMapper {

    List<WorkspaceDTO> getWorkspaceWithOrg(@Param("request") WorkspaceRequest request);
}
