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
import com.nix.cinema.util.SQLUtil;
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
    @GetMapping("/infoByBookId")
    public ReturnObject infoByBookId(@RequestParam("bookInfoId") Integer bookInfoId) {
        Pageable pageable = new Pageable();
        pageable.setConditionsSql("`status` = 0 and bookInfo = " + bookInfoId);
        return ReturnUtil.success(pageable.getList(borrowRecordService).get(0));
    }

    @PostMapping("/list")
    public ReturnObject list(@ModelAttribute Pageable<BorrowRecordModel> pageable) throws Exception {
        return ReturnUtil.list(pageable,borrowRecordService);
    }

    @PostMapping("/search")
    public ReturnObject search(@ModelAttribute Pageable pageable) throws Exception {
        pageable.setTables("`bookInfo`,`borrowRecord`,`member`");
        pageable.setConditionsSql(SQLUtil.sqlFormat("? like '%?%' and bookInfo.id = borrowRecord.bookInfo and member.id = borrowRecord.member",pageable.getField(),pageable.getValue()));
        return ReturnUtil.list(pageable,borrowRecordService);
    }
}
