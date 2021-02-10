package io.riskscanner.dto;

import io.riskscanner.base.domain.AccountWithBLOBs;


public class AccountDTO extends AccountWithBLOBs {

    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
