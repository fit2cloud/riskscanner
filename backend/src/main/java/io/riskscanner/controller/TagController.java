package io.riskscanner.controller;

import io.riskscanner.base.domain.RuleTag;
import io.riskscanner.service.RuleService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("tag")
public class TagController {

    @Resource
    private RuleService ruleService;

    @GetMapping(value = "rule/list")
    public Object listRuleTags() {
        return ruleService.getRuleTags();
    }

    @RequestMapping(value = "rule/save")
    public Object saveRuleTag(@RequestBody RuleTag ruleTag) {
        return ruleService.saveRuleTag(ruleTag);
    }

    @RequestMapping(value = "rule/update")
    public Object updateRuleTag(@RequestBody RuleTag ruleTag) {
        return ruleService.updateRuleTag(ruleTag);
    }

    @GetMapping(value = "rule/delete/{tagkey}")
    public Object deleteRuleTags(@PathVariable String tagkey) throws Exception {
        return ruleService.deleteRuleTagByTagKey(tagkey);
    }
}
