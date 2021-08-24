package io.riskscanner.base.mapper.ext;

import io.riskscanner.base.domain.RuleTag;
import io.riskscanner.controller.request.rule.CreateRuleRequest;
import io.riskscanner.dto.RuleDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ExtRuleMapper {

    List<RuleTag> getTagsOfRule(@Param("ruleId") String ruleId);

    List<RuleDTO> listAllWithTag(CreateRuleRequest Rule);

    RuleDTO selectByPrimaryKey(@Param("ruleId") String ruleId, @Param("accountId") String accountId);
}
