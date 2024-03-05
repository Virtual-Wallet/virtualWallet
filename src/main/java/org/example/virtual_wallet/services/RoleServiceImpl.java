package org.example.virtual_wallet.services;

import org.example.virtual_wallet.enums.RoleType;
import org.example.virtual_wallet.models.Role;
import org.example.virtual_wallet.models.User;
import org.example.virtual_wallet.repositories.contracts.RoleRepository;
import org.example.virtual_wallet.services.contracts.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    private RoleRepository roleRepository;
    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role getByName(RoleType roleType) {
        return roleRepository.getByName(roleType);
    }

    @Override
    public void assignRoleToUser(User user, RoleType roleType) {
        List<Role> existingRoles = roleRepository.findByUserAndRoleType(user, roleType);
        if (existingRoles.isEmpty()) {
            Role role = new Role();
            role.setUser(user);
            role.setRoleType(roleType);
            roleRepository.create(role);
        }
    }

    @Override
    public void removeRoleFromUser(User user, RoleType roleType) {
        List<Role> existingRoles = roleRepository.findByUserAndRoleType(user, roleType);
        for (Role role : existingRoles) {
            roleRepository.delete(role.getId());
        }
    }
}
