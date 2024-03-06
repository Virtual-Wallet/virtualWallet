package org.example.virtual_wallet.helpers.mappers;

import org.example.virtual_wallet.enums.RoleType;
import org.example.virtual_wallet.models.Role;
import org.example.virtual_wallet.models.dtos.RoleDto;
import org.example.virtual_wallet.services.contracts.RoleService;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {

private final RoleService roleService;

    public RoleMapper(RoleService roleService) {
        this.roleService = roleService;
    }

    public Role createRegularRole() {
        Role role = new Role();
        RoleType roleType = RoleType.REGULAR;
        role.setRoleType(roleType);
        return role;
    }
    public Role createBanRole() {
        Role role = new Role();
        RoleType roleType = RoleType.BANNED;
        role.setRoleType(roleType);
        return role;
    }
    public Role createAdminRole() {
        Role role = new Role();
        RoleType roleType = RoleType.ADMIN;
        role.setRoleType(roleType);
        return role;
    }
}
