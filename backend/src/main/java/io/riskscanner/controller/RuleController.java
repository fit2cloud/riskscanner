package io.riskscanner.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.riskscanner.base.domain.Rule;
import io.riskscanner.base.domain.RuleGroup;
import io.riskscanner.base.domain.RuleTag;
import io.riskscanner.commons.utils.PageUtils;
import io.riskscanner.commons.utils.Pager;
import io.riskscanner.controller.request.rule.CreateRuleRequest;
import io.riskscanner.controller.request.rule.RuleGroupRequest;
import io.riskscanner.controller.request.rule.RuleTagRequest;
import io.riskscanner.dto.RuleDTO;
import io.riskscanner.dto.RuleGroupDTO;
import io.riskscanner.service.RuleService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 规则
 */
@RestController
@RequestMapping(value = "rule", headers = "Accept=application/json")
public class RuleController {
    @Resource
    private RuleService ruleService;

    @PostMapping(value = "list/{goPage}/{pageSize}")
    public Pager<List<RuleDTO>> listAll(@PathVariable int goPage, @PathVariable int pageSize, @RequestBody CreateRuleRequest rule) {
        Page page = PageHelper.startPage(goPage, pageSize, true);
        return PageUtils.setPageInfo(page, ruleService.getRules(rule));
    }

    @PostMapping(value = "ruleTag/list/{goPage}/{pageSize}")
    public Pager<List<RuleTag>> ruleTagList(@PathVariable int goPage, @PathVariable int pageSize, @RequestBody RuleTagRequest request) {
        Page page = PageHelper.startPage(goPage, pageSize, true);
        return PageUtils.setPageInfo(page, ruleService.ruleTagList(request));
    }

    @PostMapping(value = "ruleGroup/list/{goPage}/{pageSize}")
    public Pager<List<RuleGroupDTO>> ruleGroupList(@PathVariable int goPage, @PathVariable int pageSize, @RequestBody RuleGroupRequest request) {
        Page page = PageHelper.startPage(goPage, pageSize, true);
        return PageUtils.setPageInfo(page, ruleService.ruleGroupList(request));
    }

    @GetMapping(value = "ruleTags")
    public Object getRuleTags() {
        return ruleService.getRuleTags();
    }

    @PostMapping(value = "add")
    public Rule addRule(@RequestBody CreateRuleRequest createRuleRequest) throws Exception {
        createRuleRequest.setId(null);
        return ruleService.saveRules(createRuleRequest);
    }

    @PostMapping(value = "update")
    public Rule updateRule(@RequestBody CreateRuleRequest createRuleRequest) throws Exception {
        return ruleService.saveRules(createRuleRequest);
    }

    @PostMapping(value = "copy")
    public Rule copyRule(@RequestBody CreateRuleRequest createRuleRequest) throws Exception {
        return ruleService.copyRule(createRuleRequest);
    }

    @PostMapping(value = "run")
    public Object runRule(@RequestBody RuleDTO ruleDTO) throws Exception {
        return ruleService.runRules(ruleDTO);
    }

    @PostMapping(value = "dryRun")
    public Object dryRun(@RequestBody RuleDTO ruleDTO) throws Exception {
        return ruleService.dryRun(ruleDTO);
    }

    @GetMapping(value = "delete/{id}")
    public void deleteRule(@PathVariable String id) throws Exception {
        ruleService.deleteRule(id);
    }

    @GetMapping(value = "get/{ruleId}")
    public Object getRule(@PathVariable String ruleId) {
        return ruleService.getRuleById(ruleId);
    }

    @GetMapping(value = "getRuleByTaskId/{taskId}")
    public Object getRuleByTaskId(@PathVariable String taskId) {
        return ruleService.getRuleByTaskId(taskId);
    }

    @PostMapping(value = "getRuleByName")
    public Object getRuleByName(@RequestBody CreateRuleRequest request) {
        return ruleService.getRuleByName(request);
    }

    @GetMapping(value = "all/resourceTypes")
    public Object getAllResourceTypes() {
        return ruleService.getAllResourceTypes();
    }

    @GetMapping(value = "all/ruleGroups")
    public Object getRuleGroups() {
        return ruleService.getRuleGroups();
    }

    @GetMapping(value = "all/ruleInspectionReport")
    public Object getRuleInspectionReport() {
        return ruleService.getRuleInspectionReport();
    }

    @GetMapping(value = "getResourceTypesById/{ruleId}")
    public Object getResourceTypesById(@PathVariable String ruleId) {
        return ruleService.getResourceTypesById(ruleId);
    }

    @PostMapping(value = "changeStatus")
    public Object changeStatus(@RequestBody Rule rule) {
        return ruleService.changeStatus(rule);
    }

    @GetMapping("reScans/{accountId}")
    public void reScans(@PathVariable String accountId) throws Exception {
        ruleService.reScans(accountId);
    }

    @GetMapping("reScan/{taskId}/{accountId}")
    public void reScan(@PathVariable String taskId, @PathVariable String accountId) throws Exception {
        ruleService.reScan(taskId, accountId);
    }

    @PostMapping("scan")
    public void scan(@RequestPart(value = "selectIds") List<String> ids) throws Exception {
        ruleService.scan(ids);
    }

    @GetMapping("insertScanHistory/{accountId}")
    public void insertScanHistory(@PathVariable String accountId) throws Exception {
        ruleService.insertScanHistory(accountId);
    }

    @RequestMapping(value = "group/save")
    public Object saveRuleGroup(@RequestBody RuleGroup ruleGroup) {
        return ruleService.saveRuleGroup(ruleGroup);
    }

    @RequestMapping(value = "group/update")
    public Object updateRuleGroup(@RequestBody RuleGroup ruleGroup) {
        return ruleService.updateRuleGroup(ruleGroup);
    }

    @GetMapping(value = "group/delete/{id}")
    public int deleteRuleGroup(@PathVariable Integer id) throws Exception {
        return ruleService.deleteRuleGroupById(id);
    }
}
