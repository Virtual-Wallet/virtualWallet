package org.example.virtual_wallet.repositories.contracts;

import org.example.virtual_wallet.enums.RoleType;
import org.example.virtual_wallet.models.Role;
import org.example.virtual_wallet.models.User;

import java.util.List;

public interface RoleRepository extends BaseCRUDRepository<Role>{
    Role getByName(RoleType name);
    List<Role> findByUserAndRoleType(User user, RoleType roleType);

}
