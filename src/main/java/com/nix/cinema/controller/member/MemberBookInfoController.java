package com.nix.cinema.controller.member;

import com.nix.cinema.Exception.WebException;
import com.nix.cinema.common.Pageable;
import com.nix.cinema.common.ReturnObject;
import com.nix.cinema.common.annotation.AdminController;
import com.nix.cinema.common.annotation.MemberController;
import com.nix.cinema.common.cache.UserCache;
import com.nix.cinema.model.BookInfoModel;
import com.nix.cinema.model.MemberInfoModel;
import com.nix.cinema.model.MemberModel;
import com.nix.cinema.service.impl.BookInfoService;
import com.nix.cinema.service.impl.BorrowRecordService;
import com.nix.cinema.service.impl.MemberService;
import com.nix.cinema.util.ReturnUtil;
import com.nix.cinema.util.SQLUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.util.Assert;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author Kiss
 * @date 2018/05/25 23:22
 */
@MemberController
@RequestMapping("/member/bookInfo")
public class MemberBookInfoController {

    @Autowired
    private BookInfoService bookInfoService;
    @Autowired
    private MemberService memberService;
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
        return ReturnUtil.success(bookInfoService.findById(id));
    }
    @PostMapping("/borrow")
    public ReturnObject borrow(@RequestParam("bookInfoId") Integer bookInfoId) throws Exception {
        BookInfoModel bookInfo = bookInfoService.findById(bookInfoId);
        Assert.notNull(bookInfo,"图书不存在");
        if (!bookInfo.getStatus()) {
            Assert.notNull(bookInfo,"图书已借出");
        }
        MemberInfoModel memberInfo = currentMember.getMemberInfo();
        memberInfo.setBorrowedNum(memberInfo.getBorrowedNum() + 1);
        memberService.update(currentMember,null);
        return ReturnUtil.success(borrowRecordService.create(bookInfo,currentMember));
    }
    @PostMapping("/returnBack")
    public ReturnObject returnBack(@RequestParam("bookInfoId") Integer bookInfoId) throws Exception {
        BookInfoModel bookInfo = bookInfoService.findById(bookInfoId);
        Assert.notNull(bookInfo,"图书不存在");
        try {
            MemberModel borrowMember = borrowRecordService.list(null,null,null,null,
                    SQLUtil.sqlFormat("status = 0 and bookInfo = ?",bookInfoId)).get(0).getMember();
            if (!currentMember.getId().equals(borrowMember.getId())) {
                return ReturnUtil.fail(401,"非法归还",null);
            }
        } catch (Exception e) {
            throw new WebException(401,"非法归还");
        }
        MemberInfoModel memberInfo = currentMember.getMemberInfo();
        memberInfo.setBorrowedNum(memberInfo.getBorrowedNum() + 1);
        memberService.update(currentMember,null);
        return ReturnUtil.success(borrowRecordService.returnBack(bookInfo));
    }
    @GetMapping("/borrowInfo")
    public ReturnObject getBorrowMember(@RequestParam("bookInfoId") Integer bookInfoId) {
        return ReturnUtil.success(borrowRecordService.list(null,null,null,null,
                "status = 0 and bookInfo = " + bookInfoId).get(0) + " and member = " + currentMember.getId());
    }

    @PostMapping("/list")
    public ReturnObject list(@ModelAttribute Pageable<BookInfoModel> pageable) throws Exception {
        return ReturnUtil.list(pageable,bookInfoService);
    }

    @PostMapping("/search")
    public ReturnObject search(@ModelAttribute Pageable pageable) throws Exception {
        pageable.setConditionsSql(SQLUtil.sqlFormat("? like '%?%'",pageable.getField(),pageable.getValue()));
        return ReturnUtil.list(pageable,bookInfoService);
    }
}
