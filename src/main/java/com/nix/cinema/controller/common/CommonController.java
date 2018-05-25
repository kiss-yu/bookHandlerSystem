package com.nix.cinema.controller.common;

import com.nix.cinema.common.ReturnObject;
import com.nix.cinema.common.annotation.Clear;
import com.nix.cinema.common.cache.UserCache;
import com.nix.cinema.model.MemberModel;
import com.nix.cinema.model.RoleModel;
import com.nix.cinema.service.impl.SystemService;
import com.nix.cinema.service.impl.MemberService;
import com.nix.cinema.util.ReturnUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Kiss
 * @date 2018/05/01 23:52
 * 公共核心controller
 */
@Controller
@Clear
public class CommonController {
    @Autowired
    private MemberService memberService;
    @Autowired
    private SystemService systemService;

    /**
     * 登录接口
     * */
    @ResponseBody
    @PostMapping(value = "/login")
    public ReturnObject login(@RequestParam(value = "username",defaultValue = "") String username,
                              @RequestParam(value = "password",defaultValue = "") String password, HttpServletRequest request) {
        return ReturnUtil.success(memberService.login(username,password,request));
    }
    /**
     * 注册
     * */
    @ResponseBody
    @PostMapping(value = "/register")
    public ReturnObject register(@ModelAttribute("/") MemberModel user) throws Exception {
        Assert.notNull(user.getUsername(),"用户名不能为空");
        Assert.notNull(user.getPassword(),"密码不能为空");
        return ReturnUtil.success(memberService.add(user));
    }
    /**
     * 获取当前用户
     * */
    @ResponseBody
    @GetMapping("/currentUser")
    public ReturnObject currentUser() {
        return ReturnUtil.success(UserCache.currentUser());
    }

    /**
     * 注销
     * */
    @ResponseBody
    @GetMapping("/logout")
    public void logout(HttpServletRequest request) {
        request.getSession(true).removeAttribute(UserCache.USER_SESSION_KEY);
    }

    @ResponseBody
    @GetMapping("/system/captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response) {
        systemService.getCaptcha(request,response);
    }
}
