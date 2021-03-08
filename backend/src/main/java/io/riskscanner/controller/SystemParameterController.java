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

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Api(tags = "系统设置")
@RestController
@RequestMapping(value = "/system")
public class SystemParameterController {
    @Resource
    private SystemParameterService SystemParameterService;

    @ApiOperation(value = "编辑邮箱设置")
    @PostMapping("/edit/email")
    @RequiresRoles(value = {RoleConstants.ADMIN})
    public void editMail(@RequestBody List<SystemParameter> systemParameter) {
        SystemParameterService.editMail(systemParameter);
    }

    @ApiOperation(value = "测试连接")
    @PostMapping("/testConnection")
    @RequiresRoles(value = {RoleConstants.ADMIN})
    public void testConnection(@RequestBody Map<String, String> hashMap) {
        SystemParameterService.testConnection(hashMap);
    }

    @ApiOperation(value = "版本信息")
    @GetMapping("/version")
    public String getVersion() {
        return SystemParameterService.getVersion();
    }

    @ApiOperation(value = "邮件设置")
    @GetMapping("/mail/info")
    @RequiresRoles(value = {RoleConstants.ADMIN})
    public List<SystemParameter> mailInfo() {
        return SystemParameterService.info(ParamConstants.Classify.MAIL.getValue());
    }

    @ApiOperation(value = "保存LADP设置")
    @PostMapping("/save/ldap")
    @RequiresRoles(value = {RoleConstants.ADMIN})
    public void saveLdap(@RequestBody List<SystemParameter> systemParameter) {
        SystemParameterService.saveLdap(systemParameter);
    }

    @ApiOperation(value = "LDAP设置")
    @GetMapping("/ldap/info")
    @RequiresRoles(value = {RoleConstants.ADMIN})
    public LdapInfo getLdapInfo() {
        return SystemParameterService.getLdapInfo(ParamConstants.Classify.LDAP.getValue());
    }

    @ApiOperation(value = "消息通知")
    @GetMapping("/message/info")
    @RequiresRoles(value = {RoleConstants.ADMIN})
    public List<SystemParameter> messageInfo() {
        return SystemParameterService.info(ParamConstants.Classify.MESSAGE.getValue());
    }

    @ApiOperation(value = "编辑消息通知")
    @PostMapping("/edit/message")
    @RequiresRoles(value = {RoleConstants.ADMIN})
    public void editMessage(@RequestBody List<SystemParameter> systemParameter) {
        SystemParameterService.editMessage(systemParameter);
    }

}
