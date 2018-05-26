package com.nix.cinema.controller.admin;

import com.nix.cinema.common.Pageable;
import com.nix.cinema.common.ReturnObject;
import com.nix.cinema.common.annotation.AdminController;
import com.nix.cinema.model.BookInfoModel;
import com.nix.cinema.model.CollegeModel;
import com.nix.cinema.model.MemberModel;
import com.nix.cinema.model.RoleModel;
import com.nix.cinema.service.impl.CollegeService;
import com.nix.cinema.util.ReturnUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Kiss
 * @date 2018/05/25 23:02
 */
@AdminController
@RequestMapping("/admin/college")
public class AdminCollegeController {
    @Autowired
    private CollegeService collegeService;

    @PostMapping("/add")
    public ReturnObject add(@ModelAttribute CollegeModel model) throws Exception {
        return ReturnUtil.success(collegeService.add(model));
    }

    @PostMapping("/delete")
    public ReturnObject delete(@RequestParam("ids") Integer[] ids) throws Exception {
        collegeService.delete(ids);
        return ReturnUtil.success();
    }
    @PostMapping("/update")
    public ReturnObject update(@ModelAttribute CollegeModel model) throws Exception {
        return ReturnUtil.success(collegeService.update(model));
    }

    @GetMapping("/checkSn")
    public Boolean checkSn(String sn) {
        return collegeService.findByOneField("sn",sn).size() == 0;
    }
    @GetMapping("/checkName")
    public Boolean checkName(String name) {
        return collegeService.findByOneField("name",name).size() == 0;
    }


    @PostMapping("/list")
    public ReturnObject list(@ModelAttribute Pageable<CollegeModel> pageable) throws Exception {
        Map additionalData = new HashMap();
        List list = pageable.getList(collegeService);
        additionalData.put("total",pageable.getCount());
        return ReturnUtil.success(null,list,additionalData);
    }
}