package io.riskscanner.service;

import io.riskscanner.base.domain.Role;
import io.riskscanner.base.mapper.RoleMapper;
import io.riskscanner.base.mapper.ext.ExtRoleMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author maguohao
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class RoleService {

    @Resource
    private ExtRoleMapper extRoleMapper;
    @Resource
    private RoleMapper roleMapper;

    public List<Role> getRoleList(String sign) {
        return extRoleMapper.getRoleList(sign);
    }

    public List<Role> getAllRole() {
        return roleMapper.selectByExample(null);
    }
}
