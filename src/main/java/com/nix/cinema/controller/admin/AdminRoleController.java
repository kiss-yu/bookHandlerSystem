package com.nix.cinema.controller.admin;

import com.nix.cinema.common.Pageable;
import com.nix.cinema.common.ReturnObject;
import com.nix.cinema.common.annotation.AdminController;
import com.nix.cinema.dto.RoleRoleInterfaceDto;
import com.nix.cinema.model.RoleInterfaceModel;
import com.nix.cinema.model.RoleModel;
import com.nix.cinema.service.impl.RoleInterfaceService;
import com.nix.cinema.service.impl.RoleService;
import com.nix.cinema.util.ReturnUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Kiss
 * @date 2018/05/03 11:42
 */
@RestController
@AdminController
@RequestMapping("/admin/role")
public class AdminRoleController {
    @Autowired
    private RoleService roleService;
    @Autowired
    private RoleInterfaceService roleInterfaceService;

    @GetMapping("/checkName")
    public boolean checkName(@RequestParam("name") String name) {
        return roleService.findByName(name) == null;
    }

    @PostMapping("/add")
    public ReturnObject add(
            @ModelAttribute RoleModel roleModel,
            @RequestParam(value = "roleInterfaceId",required = false) Integer[] roleInterfaceId) throws Exception {
        roleService.createRoleModelByInterfacesId(roleModel,roleInterfaceId);
        return ReturnUtil.success(roleService.add(roleModel));
    }

    @PostMapping("/delete")
    public ReturnObject delete(@RequestParam("ids") Integer[] ids) throws Exception {
        roleService.delete(ids);
        return ReturnUtil.success();
    }

    /**
     * 角色获取他所有授权的接口
     * */
    @GetMapping("/interfaces")
    public ReturnObject getRoleInterfacesInAll(@RequestParam("id") Integer id) {
        List<RoleInterfaceModel> roleInterfaceModels = roleInterfaceService.list(null,null,"`group`","asc",null);
        RoleModel roleModel = roleService.findById(id);
        List<RoleRoleInterfaceDto> list = new ArrayList<>();
        for (RoleInterfaceModel roleInterfaceModel:roleInterfaceModels) {
            RoleRoleInterfaceDto dto = new RoleRoleInterfaceDto();
            dto.setRoleInterface(roleInterfaceModel);
            dto.setHave(roleService.roleHaveTheInterface(roleModel,roleInterfaceModel));
            list.add(dto);
        }
        return ReturnUtil.success(list);
    }

    @GetMapping("/view")
    public ReturnObject select(@RequestParam("id") Integer id) {
        return ReturnUtil.success(roleService.findById(id));
    }
    @PostMapping("/update")
    public ReturnObject update(
            @ModelAttribute RoleModel roleModel,
            @RequestParam(value = "roleInterfaceId",required = false) Integer[] roleInterfaceId) throws Exception {
        roleService.createRoleModelByInterfacesId(roleModel,roleInterfaceId);
        return ReturnUtil.success(roleService.update(roleModel));
    }


    @PostMapping("/list")
    public ReturnObject list(@ModelAttribute Pageable<RoleModel> pageable) throws Exception {
        Map additionalData = new HashMap();
        List list = pageable.getList(roleService);
        additionalData.put("total",pageable.getCount());
        return ReturnUtil.success(null,list,additionalData);
    }
}
