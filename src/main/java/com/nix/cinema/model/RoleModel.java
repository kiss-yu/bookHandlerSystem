package com.nix.cinema.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nix.cinema.model.base.BaseModel;

import java.util.List;

/**
 * @author Kiss
 * @date 2018/05/01 18:59
 * 角色
 */
public class RoleModel extends BaseModel<RoleModel> {
    public final static String ADMIN_VALUE = "admin";
    public final static String CINEMA_VALUE = "cinema";
    public final static String MOVIE_VALUE = "movie";
    private String name;
    private String value;

    //角色允许的接口列表
    private List<RoleInterfaceModel> roleInterfaces;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @JsonIgnore
    public List<RoleInterfaceModel> getRoleInterfaces() {
        return roleInterfaces;
    }

    public void setRoleInterfaces(List<RoleInterfaceModel> roleInterfaces) {
        this.roleInterfaces = roleInterfaces;
    }
}
