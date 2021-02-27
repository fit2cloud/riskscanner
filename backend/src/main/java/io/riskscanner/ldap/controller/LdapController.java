package io.riskscanner.ldap.controller;

import io.riskscanner.base.domain.User;
import io.riskscanner.commons.constants.ParamConstants;
import io.riskscanner.commons.constants.UserSource;
import io.riskscanner.commons.exception.RSException;
import io.riskscanner.controller.ResultHolder;
import io.riskscanner.controller.request.LoginRequest;
import io.riskscanner.i18n.Translator;
import io.riskscanner.ldap.service.LdapService;
import io.riskscanner.service.SystemParameterService;
import io.riskscanner.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;

@ApiIgnore
@RestController
@RequestMapping("/ldap")
public class LdapController {

    @Resource
    private UserService userService;
    @Resource
    private LdapService ldapService;
    @Resource
    private SystemParameterService systemParameterService;

    @PostMapping(value = "/signin")
    public ResultHolder login(@RequestBody LoginRequest request) {

        String isOpen = systemParameterService.getValue(ParamConstants.LDAP.OPEN.getValue());
        if (StringUtils.isBlank(isOpen) || StringUtils.equals(Boolean.FALSE.toString(), isOpen)) {
            RSException.throwException(Translator.get("ldap_authentication_not_enabled"));
        }

        DirContextOperations dirContext = ldapService.authenticate(request);
        String email = ldapService.getMappingAttr("email", dirContext);
        String userId = ldapService.getMappingAttr("username", dirContext);

        SecurityUtils.getSubject().getSession().setAttribute("authenticate", UserSource.LDAP.name());
        SecurityUtils.getSubject().getSession().setAttribute("email", email);


        if (StringUtils.isBlank(email)) {
            RSException.throwException(Translator.get("login_fail_email_null"));
        }

        // userId 或 email 有一个相同即为存在本地用户
        User u = userService.selectUser(userId, email);
        if (u == null) {

            // 新建用户 获取LDAP映射属性
            String name = ldapService.getMappingAttr("name", dirContext);
            String phone = ldapService.getNotRequiredMappingAttr("phone", dirContext);

            User user = new User();
            user.setId(userId);
            user.setName(name);
            user.setEmail(email);

            if (StringUtils.isNotBlank(phone)) {
                user.setPhone(phone);
            }

            user.setSource(UserSource.LDAP.name());
            userService.addLdapUser(user);
        }

        // 执行 ShiroDBRealm 中 LDAP 登录逻辑
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(userId);
        return userService.login(loginRequest);
    }

    @PostMapping("/test/connect")
    public void testConnect() {
        ldapService.testConnect();
    }

    @PostMapping("/test/login")
    public void testLogin(@RequestBody LoginRequest request) {
        ldapService.authenticate(request);
    }

    @GetMapping("/open")
    public boolean isOpen() {
        return ldapService.isOpen();
    }

}
