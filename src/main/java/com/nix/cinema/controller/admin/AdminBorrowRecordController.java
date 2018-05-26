package com.nix.cinema.controller.admin;

import com.nix.cinema.common.Pageable;
import com.nix.cinema.common.ReturnObject;
import com.nix.cinema.common.annotation.AdminController;
import com.nix.cinema.model.BookInfoModel;
import com.nix.cinema.model.BorrowRecordModel;
import com.nix.cinema.model.CollegeModel;
import com.nix.cinema.service.impl.BookInfoService;
import com.nix.cinema.service.impl.BorrowRecordService;
import com.nix.cinema.util.ReturnUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Kiss
 * @date 2018/05/25 23:23
 */
@AdminController
@RequestMapping("/admin/borrowRecord")
public class AdminBorrowRecordController {
    @Autowired
    private BorrowRecordService borrowRecordService;
    @PostMapping("/add")
    public ReturnObject add(@ModelAttribute BorrowRecordModel model) throws Exception {
        return ReturnUtil.success(borrowRecordService.add(model));
    }
    @PostMapping("/delete")
    public ReturnObject delete(@RequestParam("ids") Integer[] ids) throws Exception {
        borrowRecordService.delete(ids);
        return ReturnUtil.success();
    }
    @PostMapping("/update")
    public ReturnObject update(@ModelAttribute BorrowRecordModel model) throws Exception {
        return ReturnUtil.success(borrowRecordService.update(model));
    }
    @GetMapping("/view")
    public ReturnObject select(@RequestParam("id") Integer id) {
        return ReturnUtil.success(borrowRecordService.findById(id));
    }

    @PostMapping("/list")
    public ReturnObject list(@ModelAttribute Pageable<BorrowRecordModel> pageable) throws Exception {
        Map additionalData = new HashMap();
        List list = pageable.getList(borrowRecordService);
        additionalData.put("total",pageable.getCount());
        return ReturnUtil.success(null,list,additionalData);
    }
}
