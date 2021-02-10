package io.riskscanner.base.mapper.ext;

import io.riskscanner.base.domain.RuleTag;
import io.riskscanner.controller.request.rule.RuleTagRequest;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ExtRuleTagMapper {

    List<RuleTag> list(@Param("request") RuleTagRequest request);

}