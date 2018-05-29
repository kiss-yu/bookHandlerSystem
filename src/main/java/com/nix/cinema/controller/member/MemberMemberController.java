package com.nix.cinema.controller.member;

import com.nix.cinema.common.Pageable;
import com.nix.cinema.common.ReturnObject;
import com.nix.cinema.common.annotation.AdminController;
import com.nix.cinema.common.annotation.MemberController;
import com.nix.cinema.common.cache.UserCache;
import com.nix.cinema.model.MemberModel;
import com.nix.cinema.model.RoleModel;
import com.nix.cinema.service.impl.MemberService;
import com.nix.cinema.service.impl.RoleService;
import com.nix.cinema.util.ReturnUtil;
import com.nix.cinema.util.SQLUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Kiss
 * @date 2018/05/01 23:53
 */
@RequestMapping("/member/member")
@MemberController
public class MemberMemberController {
    @Autowired
    private MemberService memberService;
    @Autowired
    private RoleService roleService;

    /**
     * 用户修改自己的资料
     * */
    @PostMapping("/update")
    public ReturnObject update(@ModelAttribute MemberModel user,
                               @RequestParam(value = "portraitImg",required = false) MultipartFile portraitImg,
            HttpServletRequest request) throws Exception {
        //如果密码未修改
        if (user.getPassword() != null && user.getPassword().isEmpty()) {
            user.setPassword(null);
        }
        user.setId(UserCache.currentUser().getId());
        //任何情况下将余额设置为空不进行修改
        user.setBalance(null);
        //username不进行修改
        user.setUsername(null);
        memberService.update(user,portraitImg);
        request.getSession().setAttribute(UserCache.USER_SESSION_KEY,user);
        return ReturnUtil.success(user);
    }
    @GetMapping("/view")
    public ReturnObject select(@RequestParam("id") Integer id) {
        return ReturnUtil.success(memberService.findById(id));
    }

    @GetMapping("/current")
    public ReturnObject current() {
        return ReturnUtil.success(UserCache.currentUser());
    }

}
