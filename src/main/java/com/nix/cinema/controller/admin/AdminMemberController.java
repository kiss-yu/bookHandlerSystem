package com.nix.cinema.controller.admin;

import com.nix.cinema.common.Pageable;
import com.nix.cinema.common.ReturnObject;
import com.nix.cinema.common.annotation.AdminController;
import com.nix.cinema.common.cache.UserCache;
import com.nix.cinema.model.MemberModel;
import com.nix.cinema.model.RoleModel;
import com.nix.cinema.service.impl.MemberService;
import com.nix.cinema.service.impl.RoleService;
import com.nix.cinema.util.ReturnUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Kiss
 * @date 2018/05/01 23:53
 */
@RestController
@RequestMapping("/admin/member")
@AdminController
public class AdminMemberController {
    @Autowired
    private MemberService memberService;
    @Autowired
    private RoleService roleService;
    /**
     * 管理员添加一个用户
     * */
    @PostMapping("/add")
    public ReturnObject createUser(@ModelAttribute("/") MemberModel user,
                                   @RequestParam("roleId") Integer roleId,
                                   @RequestParam(value = "portraitImg",required = false) MultipartFile portraitImg) throws Exception {
        Assert.notNull(user.getUsername(),"用户名不能为空");
        Assert.notNull(user.getPassword(),"密码不能为空");
        RoleModel roleModel = roleService.findById(roleId);
        if (roleModel == null) {
            return ReturnUtil.fail(404,"错误的角色信息",null);
        }
        user.setRole(roleModel);
        memberService.add(user,portraitImg);
        return ReturnUtil.success(memberService.findByUsername(user.getUsername()));
    }
    @PostMapping("/delete")
    public ReturnObject delete(@RequestParam("ids") Integer[] ids) throws Exception {
        memberService.delete(ids);
        return ReturnUtil.success();
    }
    @PostMapping("/update")
    public ReturnObject update(@ModelAttribute("/") MemberModel user,
                               @RequestParam("roleId") Integer roleId,
                               @RequestParam(value = "portraitImg",required = false) MultipartFile portraitImg) throws Exception {
        Assert.notNull(user.getId(),"id不能为空");
        //如果密码未修改
        if (user.getPassword() != null && user.getPassword().isEmpty()) {
            user.setPassword(null);
        }
        RoleModel roleModel = roleService.findById(roleId);
        if (roleModel == null) {
            return ReturnUtil.fail(404,"错误的角色信息",null);
        }
        user.setRole(roleModel);
        //任何情况下将余额设置为空不进行修改
        user.setBalance(null);
        //usernmae不进行修改
        user.setUsername(null);
        memberService.update(user,portraitImg);
        return ReturnUtil.success(user);
    }
    @GetMapping("/checkUsername")
    public Boolean checkUsername(String username) {
        return memberService.findByUsername(username) == null;
    }

    @GetMapping("/view")
    public ReturnObject select(@RequestParam("id") Integer id) {
        return ReturnUtil.success(memberService.findById(id));
    }
    @PostMapping("/list")
    public ReturnObject list(@ModelAttribute Pageable<MemberModel> pageable) throws Exception {
        Map additionalData = new HashMap();
        List list = pageable.getList(memberService);
        additionalData.put("total",pageable.getCount());
        return ReturnUtil.success(null,list,additionalData);
    }

    @GetMapping("/autoMember")
    public List autoMember(@ModelAttribute Pageable pageable, @RequestParam("q") String username,@RequestParam(value = "sql",required = false)String sql) {
        pageable.setConditionsSql("(username like '%" + username + "%' or name like '%" + username + "') and " + (sql == null ? " 1 = 1" : sql) );
        return pageable.getList(memberService);
    }
    @GetMapping("/current")
    public ReturnObject current() {
        return ReturnUtil.success(UserCache.currentUser());
    }
}
