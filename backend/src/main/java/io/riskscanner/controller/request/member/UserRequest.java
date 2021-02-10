package io.riskscanner.controller.request.member;

import io.riskscanner.base.domain.User;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class UserRequest extends User {

    private List<Map<String, Object>> roles = new ArrayList<>();

    public List<Map<String, Object>> getRoles() {
        return roles;
    }

    public void setRoles(List<Map<String, Object>> roles) {
        this.roles = roles;
    }
}
