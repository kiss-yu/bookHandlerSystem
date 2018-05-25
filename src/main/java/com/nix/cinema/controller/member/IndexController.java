package com.nix.cinema.controller.member;

import com.nix.cinema.common.ReturnObject;
import com.nix.cinema.common.annotation.Clear;
import com.nix.cinema.common.annotation.MemberController;
import com.nix.cinema.common.cache.UserCache;
import com.nix.cinema.model.MemberModel;
import com.nix.cinema.service.impl.MemberService;
import com.nix.cinema.service.impl.RoleService;
import com.nix.cinema.util.ReturnUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author Kiss
 * @date 2018/04/25 11:30
 */
@RestController
@RequestMapping("/member")
@MemberController
public class IndexController {
    @Autowired
    private MemberService userService;
    @Autowired
    private RoleService roleService;

    @Clear
    @PostMapping("/registered")
    public ReturnObject registered(@ModelAttribute MemberModel user, HttpServletRequest request) throws Exception {
        user.setRole(roleService.findByOneField("value","user").get(0));
        MemberModel insertUser = userService.registered(user,request);
        if (insertUser == null) {
            return ReturnUtil.fail(user);
        }
        return ReturnUtil.success(insertUser);
    }
    /**
     * 用户修改自己的资料
     * */
    @PostMapping("/update")
    public ReturnObject updateUser(@ModelAttribute MemberModel member,HttpServletRequest request) throws Exception {
        Assert.isTrue(UserCache.currentUser().getUsername().equals(member.getUsername()),"非法修改");
        member = userService.update(member);
        request.getSession().setAttribute(UserCache.USER_SESSION_KEY,member);
        return ReturnUtil.success(member);
    }

}
