package com.nix.cinema.dto;

import com.nix.cinema.model.RoleInterfaceModel;

import java.util.List;

/**
 * @author Kiss
 * @date 2018/05/04 17:37
 */
public class RoleRoleInterfaceDto {
    private RoleInterfaceModel roleInterface;
    private Boolean have;

    public RoleInterfaceModel getRoleInterface() {
        return roleInterface;
    }

    public void setRoleInterface(RoleInterfaceModel roleInterface) {
        this.roleInterface = roleInterface;
    }

    public Boolean getHave() {
        return have;
    }

    public void setHave(Boolean have) {
        this.have = have;
    }
}
