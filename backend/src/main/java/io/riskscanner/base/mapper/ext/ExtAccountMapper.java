package io.riskscanner.base.mapper.ext;

import io.riskscanner.controller.request.account.CloudAccountRequest;
import io.riskscanner.dto.AccountDTO;
import io.riskscanner.dto.QuartzTaskDTO;
import io.riskscanner.dto.RuleDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ExtAccountMapper {

    List<AccountDTO> getCloudAccountList(@Param("request") CloudAccountRequest request);

    List<RuleDTO> ruleList(QuartzTaskDTO dto);

    List<Map<String, Object>> groupList(Map<String, Object> params);

    List<Map<String, Object>> reportList(Map<String, Object> params);

    List<Map<String, Object>> tagList(Map<String, Object> params);

    List<Map<String, Object>> regionsList(Map<String, Object> params);

    List<Map<String, Object>> resourceList(Map<String, Object> params);

}