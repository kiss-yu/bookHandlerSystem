package com.nix.cinema.service.impl;

import com.nix.cinema.Exception.WebException;
import com.nix.cinema.common.cache.UserCache;
import com.nix.cinema.dao.MemberInfoMapper;
import com.nix.cinema.dao.MemberMapper;
import com.nix.cinema.model.MemberInfoModel;
import com.nix.cinema.model.MemberModel;
import com.nix.cinema.service.BaseService;
import com.nix.cinema.util.ServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

/**
 * @author Kiss
 * @date 2018/05/01 20:13
 * 用户service
 */
@Service
public class MemberService extends BaseService<MemberModel> {
    public final static String ADMIN_USERNAME = "admin";
    public final static String MEMBER_IMG_PATH = "/images/member/";
    public final static String MEMBER_DEFAULT_IMG = "default.jpg";

    @Autowired
    private RoleService roleService;

    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private MemberInfoService memberInfoService;

    public MemberModel login(String username, String password, HttpServletRequest request) {
        MemberModel user = UserCache.currentUser();
        if (user == null) {
            user = memberMapper.login(username,password);
            request.getSession(true).setAttribute(UserCache.USER_SESSION_KEY,user);
        }
        return user;
    }

    public MemberModel registered(MemberModel user, HttpServletRequest request) throws Exception {
        user.setRole(roleService.findByOneField("value","student").get(0));
        add(user,null);
        user = findByUsername(user.getUsername());
        if (user != null) {
            request.getSession(true).setAttribute(UserCache.USER_SESSION_KEY,user);
        }
        return user;
    }

    public MemberModel findByUsername(String username) {
        return memberMapper.findByUsername(username);
    }

    @Override
    public MemberModel add(MemberModel model) throws Exception {
        if (ADMIN_USERNAME.equals(model.getUsername())) {
            throw new WebException(401,"不能使用admin做完用户名");
        }
        if (model.getMemberInfo() == null) {
            model.setMemberInfo(new MemberInfoModel());
        }
        memberInfoService.add(model.getMemberInfo());
        return super.add(model);
    }

    public MemberModel add(MemberModel model,MultipartFile portraitImg) throws Exception {
        if (portraitImg != null && !portraitImg.isEmpty()) {
            model.setImg(MEMBER_IMG_PATH + portraitImg.getOriginalFilename());
        } else {
            model.setImg(MEMBER_IMG_PATH + MEMBER_DEFAULT_IMG);
        }
        add(model);
        if (portraitImg != null && !portraitImg.isEmpty()) {
            File file = new File(MemberService.class.getResource("/").getFile() + MEMBER_IMG_PATH + portraitImg.getOriginalFilename());
            portraitImg.transferTo(file);
        }
        return model;
    }

    public MemberModel update(MemberModel model,MultipartFile log) throws Exception {
        if (log != null && !log.isEmpty()) {
            MemberModel before = findById(model.getId());
            model.setImg(MEMBER_IMG_PATH + log.getOriginalFilename());
            ServiceUtil.updateImage(before.getImg(),model.getImg(),MEMBER_IMG_PATH + MEMBER_DEFAULT_IMG,log);
        }
        if (model.getMemberInfo() != null) {
            memberInfoService.update(model.getMemberInfo());
        }
        return super.update(model);
    }
}
