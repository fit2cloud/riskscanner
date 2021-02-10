package io.riskscanner.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.riskscanner.base.domain.AccountWithBLOBs;
import io.riskscanner.base.domain.RuleAccountParameter;
import io.riskscanner.commons.constants.CloudAccountConstants;
import io.riskscanner.commons.exception.PluginException;
import io.riskscanner.commons.exception.RSException;
import io.riskscanner.commons.utils.PageUtils;
import io.riskscanner.commons.utils.Pager;
import io.riskscanner.controller.request.account.CloudAccountRequest;
import io.riskscanner.controller.request.account.CreateCloudAccountRequest;
import io.riskscanner.controller.request.account.UpdateCloudAccountRequest;
import io.riskscanner.dto.AccountDTO;
import io.riskscanner.dto.QuartzTaskDTO;
import io.riskscanner.dto.RuleDTO;
import io.riskscanner.service.AccountService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "account", headers = "Accept=application/json")
public class AccountController {
    @Resource
    private AccountService accountService;

    @PostMapping("list/{goPage}/{pageSize}")
    public Pager<List<AccountDTO>> getCloudAccountList(
            @PathVariable int goPage, @PathVariable int pageSize, @RequestBody CloudAccountRequest request) {
        Page page = PageHelper.startPage(goPage, pageSize, true);
        return PageUtils.setPageInfo(page, accountService.getCloudAccountList(request));
    }

    @GetMapping("allList")
    public List<AccountDTO> getCloudAccountList() {
        CloudAccountRequest request = new CloudAccountRequest();
        request.setStatus(CloudAccountConstants.Status.VALID.name());
        return accountService.getCloudAccountList(request);
    }

    @GetMapping("getAccount/{id}")
    public Object getAccount(@PathVariable String id) {
        return accountService.getAccount(id);
    }

    @RequestMapping(value = "all")
    public List<AccountWithBLOBs> getAllCloudAccount(@RequestBody CloudAccountRequest request) {
        return accountService.getAllCloudAccount(request);
    }

    @GetMapping(value = "syncAll")
    public Object syncAllCloudAccount() throws PluginException {
        try {
            accountService.syncRegions();
        }catch (Exception e){
            return false;
        }
        return true;
    }

    @PostMapping("validate")
    public void validate(@RequestPart(value = "selectIds") List<String> ids) throws Exception {
        accountService.validate(ids);
    }

    @PostMapping("validate/{id}")
    public Boolean validate(@PathVariable String id) throws Exception {
        return accountService.validate(id);
    }

    @PostMapping("add")
    public AccountWithBLOBs addAccount(@RequestBody CreateCloudAccountRequest request) throws Exception {
        return accountService.addAccount(request);
    }

    @PostMapping("update")
    public AccountWithBLOBs editAccount(@RequestBody UpdateCloudAccountRequest request) throws Exception {
        return accountService.editAccount(request);
    }

    @PostMapping(value = "delete/{accountId}")
    public void deleteAccount(@PathVariable String accountId) {
        accountService.delete(accountId);
    }

    @GetMapping("getRegions/{id}")
    public Object getRegions(@PathVariable String id) throws RSException {
        return accountService.getRegions(id);
    }

    @PostMapping("string2PrettyFormat")
    public String string2PrettyFormat(@RequestBody AccountWithBLOBs accountWithBLOBs) throws Exception {
        return accountService.string2PrettyFormat(accountWithBLOBs.getRegions());
    }

    @PostMapping("clean/parameter")
    public Object cleanParameter(@RequestBody List<RuleAccountParameter> list) throws Exception {
        return accountService.cleanParameter(list);
    }

    @PostMapping("save/parameter")
    public Object saveParameter(@RequestBody List<QuartzTaskDTO> list) throws Exception {
        return accountService.saveParameter(list);
    }

    @PostMapping(value = "rule/list/{goPage}/{pageSize}")
    public Pager<List<RuleDTO>> ruleList(@PathVariable int goPage, @PathVariable int pageSize, @RequestBody QuartzTaskDTO dto) {
        Page page = PageHelper.startPage(goPage, pageSize, true);
        return PageUtils.setPageInfo(page, accountService.getRules(dto));
    }

    @PostMapping("group/list/{goPage}/{pageSize}")
    public Object groupList(@PathVariable int goPage, @PathVariable int pageSize, @RequestBody Map<String, Object> params) {
        Page page = PageHelper.startPage(goPage, pageSize, true);
        return PageUtils.setPageInfo(page, accountService.groupList(params));
    }

    @PostMapping("report/list/{goPage}/{pageSize}")
    public Object reportList(@PathVariable int goPage, @PathVariable int pageSize, @RequestBody Map<String, Object> params) {
        Page page = PageHelper.startPage(goPage, pageSize, true);
        return PageUtils.setPageInfo(page, accountService.reportList(params));
    }

    @PostMapping("tag/list/{goPage}/{pageSize}")
    public Object tagList(@PathVariable int goPage, @PathVariable int pageSize, @RequestBody Map<String, Object> params) {
        Page page = PageHelper.startPage(goPage, pageSize, true);
        return PageUtils.setPageInfo(page, accountService.tagList(params));
    }

    @PostMapping("regions/list/{goPage}/{pageSize}")
    public Object regionsList(@PathVariable int goPage, @PathVariable int pageSize, @RequestBody Map<String, Object> params) {
        Page page = PageHelper.startPage(goPage, pageSize, true);
        return PageUtils.setPageInfo(page, accountService.regionsList(params));
    }

    @PostMapping("resource/list/{goPage}/{pageSize}")
    public Object resourceList(@PathVariable int goPage, @PathVariable int pageSize, @RequestBody Map<String, Object> params) {
        Page page = PageHelper.startPage(goPage, pageSize, true);
        return PageUtils.setPageInfo(page, accountService.resourceList(params));
    }

    @GetMapping("iam/strategy/{type}")
    public Object strategy(@PathVariable String type) throws Exception {
        return accountService.strategy(type);
    }


}
