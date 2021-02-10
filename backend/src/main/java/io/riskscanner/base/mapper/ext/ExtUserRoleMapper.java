package io.riskscanner.base.mapper.ext;

import io.riskscanner.base.domain.Role;
import io.riskscanner.base.domain.User;
import io.riskscanner.controller.request.member.QueryMemberRequest;
import io.riskscanner.controller.request.organization.QueryOrgMemberRequest;
import io.riskscanner.dto.OrganizationMemberDTO;
import io.riskscanner.dto.UserRoleHelpDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ExtUserRoleMapper {

    List<UserRoleHelpDTO> getUserRoleHelpList(@Param("userId") String userId);

    List<User> getMemberList(@Param("member") QueryMemberRequest request);

    List<User> getOrgMemberList(@Param("orgMember") QueryOrgMemberRequest request);

    List<OrganizationMemberDTO> getOrganizationMemberDTO(@Param("orgMember") QueryOrgMemberRequest request);

    List<Role> getOrganizationMemberRoles(@Param("orgId") String orgId, @Param("userId") String userId);

    List<Role> getWorkspaceMemberRoles(@Param("workspaceId") String workspaceId, @Param("userId") String userId);

    List<User> getBesideOrgMemberList(@Param("orgId") String orgId);


    List<User> getWorkspaceAdminAndUserList(@Param("request") QueryMemberRequest request);
}
