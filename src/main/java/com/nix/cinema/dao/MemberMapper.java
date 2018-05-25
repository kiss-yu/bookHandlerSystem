package com.nix.cinema.dao;

import com.nix.cinema.dao.base.BaseMapper;
import com.nix.cinema.model.MemberModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author 11723
 */
@Repository
public interface MemberMapper extends BaseMapper<MemberModel> {
    /**
     * 用户登录
     * @param username
     * @param password
     * @return
     * */
    MemberModel login(@Param("username") String username, @Param("password") String password);

    /**
     * 根据用户名查找用户
     * @param username
     * @return
     * */
    MemberModel findByUsername(@Param("username") String username);
}