package io.riskscanner.controller;

import io.riskscanner.base.domain.SystemParameter;
import io.riskscanner.commons.constants.ParamConstants;
import io.riskscanner.commons.constants.RoleConstants;
import io.riskscanner.ldap.domain.LdapInfo;
import io.riskscanner.service.SystemParameterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

@Api(tags = "系统设置")
@RestController
@RequestMapping(value = "/system")
public class SystemParameterController {
    @Resource
    private SystemParameterService SystemParameterService;

    @ApiOperation(value = "编辑邮箱")
    @PostMapping("/edit/email")
    @RequiresRoles(value = {RoleConstants.ADMIN})
    public void editMail(@RequestBody List<SystemParameter> systemParameter) {
        SystemParameterService.editMail(systemParameter);
    }

    @ApiOperation(value = "测试连接")
    @PostMapping("/testConnection")
    @RequiresRoles(value = {RoleConstants.ADMIN})
    public void testConnection(@RequestBody HashMap<String, String> hashMap) {
        SystemParameterService.testConnection(hashMap);
    }

    @ApiIgnore
    @GetMapping("/version")
    public String getVersion() {
        return SystemParameterService.getVersion();
    }

    @ApiIgnore
    @GetMapping("/mail/info")
    @RequiresRoles(value = {RoleConstants.ADMIN})
    public Object mailInfo() {
        return SystemParameterService.mailInfo(ParamConstants.Classify.MAIL.getValue());
    }

    @ApiIgnore
    @PostMapping("/save/ldap")
    @RequiresRoles(value = {RoleConstants.ADMIN})
    public void saveLdap(@RequestBody List<SystemParameter> systemParameter) {
        SystemParameterService.saveLdap(systemParameter);
    }

    @ApiIgnore
    @GetMapping("/ldap/info")
    @RequiresRoles(value = {RoleConstants.ADMIN})
    public LdapInfo getLdapInfo() {
        return SystemParameterService.getLdapInfo(ParamConstants.Classify.LDAP.getValue());
    }

}
