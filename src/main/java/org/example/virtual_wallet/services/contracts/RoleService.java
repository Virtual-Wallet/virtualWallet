package org.example.virtual_wallet.services.contracts;

import org.example.virtual_wallet.enums.RoleType;
import org.example.virtual_wallet.models.Role;
import org.example.virtual_wallet.models.User;

public interface RoleService {
    Role getByName(RoleType roleType);
    void assignRoleToUser(User user, RoleType roleType);
    void removeRoleFromUser(User user, RoleType roleType);
}
