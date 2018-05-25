package com.nix.cinema.dao;

import com.nix.cinema.dao.base.BaseMapper;
import com.nix.cinema.model.RoleModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Kiss
 * @date 2018/05/03 17:51
 */
@Repository
public interface RoleMapper extends BaseMapper<RoleModel> {
    void insertRoleMiddleInterface(@Param("roleId") Integer roleId, @Param("interfaceId") Integer interfaceId);
    void deleteRoleMiddleInterface(@Param("roleId") Integer roleId, @Param("interfaceId") Integer interfaceId);

}
