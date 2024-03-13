package org.example.virtual_wallet.controllers.Rest;

import org.example.virtual_wallet.helpers.mappers.RoleMapper;
import org.example.virtual_wallet.models.Role;
import org.example.virtual_wallet.services.contracts.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/roles")
public class RoleRestController {

    private RoleService roleService;
    private RoleMapper roleMapper;

    @Autowired
    public RoleRestController(RoleService roleService, RoleMapper roleMapper) {
        this.roleService = roleService;
        this.roleMapper = roleMapper;
    }

    @PostMapping("/regular")
    public void createRegular() {
        Role role = roleMapper.createRegularRole();
        roleService.crete(role);
    }

    @PostMapping("/admin")
    public void createAdmin() {
        Role role = roleMapper.createAdminRole();
        roleService.crete(role);
    }

    @PostMapping("/ban")
    public void createBan() {
        Role role = roleMapper.createBanRole();
        roleService.crete(role);
    }

}
