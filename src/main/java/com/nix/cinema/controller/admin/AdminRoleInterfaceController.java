package com.nix.cinema.controller.admin;

import com.nix.cinema.common.Pageable;
import com.nix.cinema.common.ReturnObject;
import com.nix.cinema.common.annotation.AdminController;
import com.nix.cinema.model.RoleInterfaceModel;
import com.nix.cinema.model.RoleModel;
import com.nix.cinema.service.impl.RoleInterfaceService;
import com.nix.cinema.util.ReturnUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Kiss
 * @date 2018/05/03 11:46
 */
@RestController
@AdminController
@RequestMapping("/admin/roleInterface")
public class AdminRoleInterfaceController {

    @Autowired
    private RoleInterfaceService roleInterfaceService;
    @PostMapping("/add")
    public ReturnObject add(@ModelAttribute RoleInterfaceModel roleInterfaceModel) throws Exception {
        roleInterfaceService.add(roleInterfaceModel);
        return ReturnUtil.success(roleInterfaceModel);
    }

    @GetMapping("/all")
    public ReturnObject all() {
        return ReturnUtil.success(roleInterfaceService.list(null,null,"`group`","asc",null));
    }
    @PostMapping("/delete")
    public ReturnObject delete(@RequestParam("ids") Integer[] ids) throws Exception {
        roleInterfaceService.delete(ids);
        return ReturnUtil.success();
    }
    @GetMapping("/view")
    public ReturnObject select(@RequestParam("id") Integer id) {
        return ReturnUtil.success(roleInterfaceService.findById(id));
    }
    @PostMapping("/update")
    public ReturnObject update(@ModelAttribute RoleInterfaceModel roleInterfaceModel) throws Exception {
        roleInterfaceModel.setEnabled(roleInterfaceModel.getEnabled() == null ? false : true);
        return ReturnUtil.success(roleInterfaceService.update(roleInterfaceModel));
    }

    @PostMapping("/list")
    public ReturnObject list(@ModelAttribute Pageable<RoleInterfaceModel> pageable) throws Exception {
        Map additionalData = new HashMap();

        List list = pageable.getList(roleInterfaceService);
        additionalData.put("total",pageable.getCount());
        return ReturnUtil.success(null,list,additionalData);
    }
}
