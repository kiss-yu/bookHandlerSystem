package com.nix.cinema.controller.member;

import com.nix.cinema.common.Pageable;
import com.nix.cinema.common.ReturnObject;
import com.nix.cinema.common.annotation.AdminController;
import com.nix.cinema.common.annotation.MemberController;
import com.nix.cinema.common.cache.UserCache;
import com.nix.cinema.model.BorrowRecordModel;
import com.nix.cinema.model.MemberModel;
import com.nix.cinema.service.impl.BorrowRecordService;
import com.nix.cinema.util.ReturnUtil;
import com.nix.cinema.util.SQLUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Kiss
 * @date 2018/05/25 23:23
 */
@MemberController
@RequestMapping("/member/borrowRecord")
public class MemberBorrowRecordController {
    @Autowired
    private BorrowRecordService borrowRecordService;

    private MemberModel currentMember;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
        currentMember = UserCache.currentUser();
    }

    @GetMapping("/view")
    public ReturnObject select(@RequestParam("id") Integer id) {
        BorrowRecordModel borrowRecordModel = borrowRecordService.findById(id);
        if (currentMember.getId().equals(borrowRecordModel.getMember().getId())) {
            return ReturnUtil.success(borrowRecordModel);
        }
        return ReturnUtil.fail();
    }
    @GetMapping("/infoByBookId")
    public ReturnObject infoByBookId(@RequestParam("bookInfoId") Integer bookInfoId) {
        Pageable pageable = new Pageable();
        pageable.setConditionsSql("`status` = 0 and bookInfo = " + bookInfoId);
        return ReturnUtil.success(pageable.getList(borrowRecordService).get(0));
    }

    @PostMapping("/list")
    public ReturnObject list(@ModelAttribute Pageable<BorrowRecordModel> pageable) throws Exception {
        pageable.setConditionsSql("member = ?",currentMember.getId());
        return ReturnUtil.list(pageable,borrowRecordService);
    }

    @PostMapping("/search")
    public ReturnObject search(@ModelAttribute Pageable pageable) throws Exception {
        pageable.setTables("`bookInfo`,`borrowRecord`,`member`");
        pageable.setConditionsSql(SQLUtil.sqlFormat("? like '%?%' and bookInfo.id = borrowRecord.bookInfo and member.id = borrowRecord.member and member.id = ?",
                pageable.getField(),pageable.getValue(),currentMember.getId()));
        return ReturnUtil.list(pageable,borrowRecordService);
    }
}
