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
    public RoleServiceImpl(org.example.virtual_wallet.repositories.contracts.RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role getByName(RoleType roleType) {
        return roleRepository.getByName(roleType);
    }
}
