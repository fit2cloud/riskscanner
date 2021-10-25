package io.riskscanner.base.mapper.ext;

import io.riskscanner.controller.request.rule.RuleGroupRequest;
import io.riskscanner.dto.RuleDTO;
import io.riskscanner.dto.RuleGroupDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ExtRuleGroupMapper {

    List<RuleGroupDTO> list(@Param("request") RuleGroupRequest request);

    List<RuleDTO> getRules(@Param("accountId") String accountId, @Param("groupId") String groupId);

    List<String> getRuleGroup(@Param("accountId") String accountId);

}