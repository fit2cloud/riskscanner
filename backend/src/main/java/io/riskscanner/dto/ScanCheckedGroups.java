package io.riskscanner.dto;

import java.util.List;

public class ScanCheckedGroups {

    private String accountId;

    private List<String> checkedGroups;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public List<String> getCheckedGroups() {
        return checkedGroups;
    }

    public void setCheckedGroups(List<String> checkedGroups) {
        this.checkedGroups = checkedGroups;
    }
}
