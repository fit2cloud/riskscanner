package io.riskscanner.controller;

import io.riskscanner.commons.constants.ResourceOperation;
import io.riskscanner.commons.constants.ResourceTypeConstants;
import io.riskscanner.commons.constants.SsoMode;
import io.riskscanner.commons.constants.UserSource;
import io.riskscanner.commons.user.SessionUser;
import io.riskscanner.commons.utils.SessionUtils;
import io.riskscanner.controller.request.LoginRequest;
import io.riskscanner.service.OperationLogService;
import io.riskscanner.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.util.Objects;

@ApiIgnore
@RestController
@RequestMapping
public class LoginController {

    @Resource
    private UserService userService;
    @Resource
    private Environment env;

    @GetMapping(value = "/isLogin")
    public ResultHolder isLogin() {
        if (SecurityUtils.getSubject().isAuthenticated()) {
            SessionUser user = SessionUtils.getUser();
            if ((user!= null) && StringUtils.isBlank(user.getLanguage()))
                    user.setLanguage(LocaleContextHolder.getLocale().toString());
            return ResultHolder.success(user);
        }
        String ssoMode = env.getProperty("sso.mode");
        if (ssoMode != null && StringUtils.equalsIgnoreCase(SsoMode.CAS.name(), ssoMode)) {
            return ResultHolder.error("sso");
        }
        return ResultHolder.error("");
    }

    @PostMapping(value = "/signin")
    public ResultHolder login(@RequestBody LoginRequest request) {
        SecurityUtils.getSubject().getSession().setAttribute("authenticate", UserSource.LOCAL.name());
        return userService.login(request);
    }

    @GetMapping(value = "/currentUser")
    public ResultHolder currentUser() {
        return ResultHolder.success(SecurityUtils.getSubject().getSession().getAttribute("user"));
    }

    @GetMapping(value = "/signout")
    public ResultHolder logout() throws Exception {
        String ssoMode = env.getProperty("sso.mode");
        if ((ssoMode != null) && StringUtils.equalsIgnoreCase(SsoMode.CAS.name(), ssoMode)) {
            return ResultHolder.error("sso");
        } else {
            userService.logout();
            SecurityUtils.getSubject().logout();
            OperationLogService.log(SessionUtils.getUser(), Objects.requireNonNull(SessionUtils.getUser()).getId(), SessionUtils.getUser().getName(), ResourceTypeConstants.USER.name(), ResourceOperation.LOGOUT, "用户登出");
        }
        return ResultHolder.success("");
    }

    /*Get default language*/
    @GetMapping(value = "/language")
    public String getDefaultLanguage() {
        return userService.getDefaultLanguage();
    }

}
