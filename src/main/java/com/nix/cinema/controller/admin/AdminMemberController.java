package com.nix.cinema.controller.admin;

import com.nix.cinema.common.Pageable;
import com.nix.cinema.common.ReturnObject;
import com.nix.cinema.common.annotation.AdminController;
import com.nix.cinema.common.annotation.Clear;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Kiss
 * @date 2018/05/01 23:53
 */
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
    public ReturnObject update(@ModelAttribute MemberModel user,
                               @RequestParam(value = "roleId",required = false) Integer roleId,
                               @RequestParam(value = "portraitImg",required = false) MultipartFile portraitImg) throws Exception {
        Assert.notNull(user.getId(),"id不能为空");
        //如果密码未修改
        if (user.getPassword() != null && user.getPassword().isEmpty()) {
            user.setPassword(null);
        }

        //任何情况下将余额设置为空不进行修改
        user.setBalance(null);
        //username不进行修改
        user.setUsername(null);RoleModel roleModel = roleService.findById(roleId);
        if (roleModel == null) {
            return ReturnUtil.fail(404,"错误的角色信息",null);
        }
        user.setRole(roleModel);
        memberService.update(user,portraitImg);
        return ReturnUtil.success(user);
    }

    /**
     * 管理员修改自己的信息
     * */
    @PostMapping("/edit")
    public ReturnObject edit(@ModelAttribute MemberModel user,
                               @RequestParam(value = "portraitImg",required = false) MultipartFile portraitImg,
                             HttpServletRequest request) throws Exception {
        Assert.notNull(user.getId(),"id不能为空");
        Assert.isTrue(user.getId().equals(UserCache.currentUser().getId()),"只能修改自己的信息");
        //如果密码未修改
        if (user.getPassword() != null && user.getPassword().isEmpty()) {
            user.setPassword(null);
        }

        //任何情况下将余额设置为空不进行修改
        user.setBalance(null);
        //username不进行修改
        user.setUsername(null);
        user.setRole(null);
        memberService.update(user,portraitImg);
        user = memberService.findById(UserCache.currentUser().getId());
        request.getSession().setAttribute(UserCache.USER_SESSION_KEY,user);
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
        MemberModel current = UserCache.currentUser();
        pageable.setTables("`member`,`role`,`memberInfo`");
        if (RoleModel.ADMIN_VALUE.equals(current.getRole().getValue())) {
            pageable.setConditionsSql("role.id = member.role and member.memberInfo = memberInfo.id");
        } else if (RoleModel.BOOKADMIN_VALUE.equals(current.getRole().getValue())) {
            pageable.setConditionsSql("role.value = '?' and role.id = member.role and member.memberInfo = memberInfo.id",RoleModel.STUDENT_VALUE);
        } else {
            return null;
        }
        return ReturnUtil.list(pageable,memberService);
    }

    @GetMapping("/autoMember")
    public List autoMember(@ModelAttribute Pageable pageable, @RequestParam("q") String username,@RequestParam(value = "sql",required = false)String sql) {
        pageable.setConditionsSql("(username like '%" + username + "%' or name like '%" + username + "') and " + (sql == null ? " 1 = 1" : sql) );
        return pageable.getList(memberService);
    }
    @GetMapping("/current")
    @Clear
    public ReturnObject current() {
        return ReturnUtil.success(UserCache.currentUser());
    }

    @PostMapping("/search")
    public ReturnObject search(@ModelAttribute Pageable pageable) throws Exception {
        MemberModel current = UserCache.currentUser();
        if (RoleModel.ADMIN_VALUE.equals(current.getRole().getValue())) {
            pageable.setTables("`member`,`memberInfo`,`professional`,`college`");
            pageable.setConditionsSql(SQLUtil.sqlFormat("? like '%?%' and memberInfo.id = member.memberInfo and memberInfo.college = college.id and memberInfo.professional = professional.id"
                    ,pageable.getField(),pageable.getValue()));
        } else if (RoleModel.BOOKADMIN_VALUE.equals(current.getRole().getValue())) {
            pageable.setTables("`member`,`role`,`memberInfo`");
            pageable.setConditionsSql("? like '%?%' and role.value = '?' and role.id = member.role and memberInfo.id = member.memberInfo and memberInfo.college = college.id and memberInfo.professional = professional.id",
                    pageable.getField(),pageable.getValue(),RoleModel.STUDENT_VALUE);
        } else {
            return null;
        }
        return ReturnUtil.list(pageable,memberService);
    }

}
