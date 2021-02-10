package io.riskscanner.security;

import org.apache.shiro.authc.UsernamePasswordToken;

public class RsUserToken extends UsernamePasswordToken {
    private String loginType;

    public RsUserToken() {
    }

    public RsUserToken(final String username, final String password) {
        super(username, password);
    }

    public RsUserToken(final String username, final String password, final String loginType) {
        super(username, password);
        this.loginType = loginType;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

}
