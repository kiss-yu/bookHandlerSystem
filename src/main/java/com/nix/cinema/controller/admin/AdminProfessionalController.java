package com.nix.cinema.controller.admin;

import com.nix.cinema.common.Pageable;
import com.nix.cinema.common.ReturnObject;
import com.nix.cinema.common.annotation.AdminController;
import com.nix.cinema.model.ProfessionalModel;
import com.nix.cinema.service.impl.ProfessionalService;
import com.nix.cinema.util.ReturnUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Kiss
 * @date 2018/05/25 23:17
 */
@AdminController
@RequestMapping("/admin/professional")
public class AdminProfessionalController {
    @Autowired
    private ProfessionalService professionalService;
    @PostMapping("/add")
    public ReturnObject add(@ModelAttribute ProfessionalModel model) throws Exception {
        return ReturnUtil.success(professionalService.add(model));
    }

    @PostMapping("/delete")
    public ReturnObject delete(@RequestParam("ids") Integer[] ids) throws Exception {
        professionalService.delete(ids);
        return ReturnUtil.success();
    }
    @PostMapping("/update")
    public ReturnObject update(@ModelAttribute ProfessionalModel model) throws Exception {
        return ReturnUtil.success(professionalService.update(model));
    }
    @PostMapping("/list")
    public ReturnObject list(@ModelAttribute Pageable<ProfessionalModel> pageable) throws Exception {
        Map additionalData = new HashMap();
        List list = pageable.getList(professionalService);
        additionalData.put("total",pageable.getCount());
        return ReturnUtil.success(null,list,additionalData);
    }
    @GetMapping("/collegeAll")
    public ReturnObject collegeAll(Integer id) throws Exception {
        Map additionalData = new HashMap();
        Pageable<ProfessionalModel> pageable = new Pageable<>();
        pageable.setConditionsSql("college = " + id);
        List list = pageable.getList(professionalService);
        additionalData.put("total",pageable.getCount());
        return ReturnUtil.success(null,list,additionalData);
    }
}
