package com.nix.cinema.controller.admin;

import com.nix.cinema.common.Pageable;
import com.nix.cinema.common.ReturnObject;
import com.nix.cinema.common.annotation.AdminController;
import com.nix.cinema.model.ProfessionalModel;
import com.nix.cinema.service.impl.ProfessionalService;
import com.nix.cinema.util.ReturnUtil;
import com.nix.cinema.util.SQLUtil;
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
    @GetMapping("/view")
    public ReturnObject select(@RequestParam("id") Integer id) {
        return ReturnUtil.success(professionalService.findById(id));
    }
    @GetMapping("/checkSn")
    public Boolean checkSn(String sn,@RequestParam(value = "id",required = false) Integer id) {
        if (id != null) {
            if (professionalService.findById(id).getSn().equals(sn)) {
                return true;
            }
        }
        return professionalService.findByOneField("sn",sn).size() == 0;
    }
    @GetMapping("/checkName")
    public Boolean checkName(String name,@RequestParam(value = "id",required = false) Integer id) {
        if (id != null) {
            if (professionalService.findById(id).getName().equals(name)) {
                return true;
            }
        }
        return professionalService.findByOneField("name",name).size() == 0;
    }
    @PostMapping("/list")
    public ReturnObject list(@ModelAttribute Pageable<ProfessionalModel> pageable) throws Exception {
        pageable.setTables("`college`,`professional`");
        pageable.setConditionsSql("professional.college = college.id");
        return ReturnUtil.list(pageable,professionalService);
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


    @PostMapping("/search")
    public ReturnObject search(@ModelAttribute Pageable pageable) throws Exception {
        pageable.setTables("`college`,`professional`");
        pageable.setConditionsSql(SQLUtil.sqlFormat("? like '%?%' and professional.college = college.id",pageable.getField(),pageable.getValue()));
        return ReturnUtil.list(pageable,professionalService);
    }
}
