package io.riskscanner.controller;

import io.riskscanner.commons.utils.SessionUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@Controller
@RequestMapping
public class IndexController {

    @GetMapping(value = "/")
    public String index() {
        return "index.html";
    }

    @GetMapping(value = "/login")
    public String login() {
        if (SessionUtils.getUser() == null) {
            return "login.html";
        } else {
            return "redirect:/";
        }
    }

    @GetMapping(value = "/sso/login")
    public String ossLogin() {
        return "redirect:/";
    }

    @GetMapping(value = "/sso/logout")
    public void ossLogout() {
        SecurityUtils.getSubject().logout();
    }

}
