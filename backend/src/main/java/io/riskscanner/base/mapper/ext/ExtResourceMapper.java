package io.riskscanner.base.mapper.ext;


import io.riskscanner.base.domain.ResourceWithBLOBs;
import io.riskscanner.base.domain.TaskItem;
import io.riskscanner.controller.request.resource.ResourceRequest;
import io.riskscanner.dto.ExportDTO;
import io.riskscanner.dto.ReportDTO;
import io.riskscanner.dto.ResourceDTO;
import io.riskscanner.dto.SourceDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author maguohao
 * @date 2020-04-30 16:17
 */
public interface ExtResourceMapper {

    List<ResourceDTO> getComplianceResult(ResourceRequest resourceRequest);

    List<ReportDTO> reportList(ResourceRequest resourceRequest);

    SourceDTO source(@Param("accountId")String accountId);

    String resultPercent(@Param("accountId") String accountId, @Param("severity") String severity, @Param("taskId") String taskId);

    String sumReturnSum(@Param("accountId") String accountId);

    String sumResourcesSum(@Param("accountId") String accountId);

    Map<String, String> reportIso(@Param("accountId") String accountId, @Param("groupId") String groupId);

    List<Map<String, String>> groups(Map<String, Object> params);

    List<ExportDTO> searchExportData(ResourceRequest resourceRequest, @Param("accountIds") List<String> accountIds);

    ResourceWithBLOBs resource(TaskItem taskItem);

}
