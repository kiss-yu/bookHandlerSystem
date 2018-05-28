package com.nix.cinema.controller.admin;

import com.nix.cinema.common.Pageable;
import com.nix.cinema.common.ReturnObject;
import com.nix.cinema.common.annotation.AdminController;
import com.nix.cinema.model.BookInfoModel;
import com.nix.cinema.model.MemberModel;
import com.nix.cinema.service.impl.BookInfoService;
import com.nix.cinema.service.impl.BorrowRecordService;
import com.nix.cinema.service.impl.MemberService;
import com.nix.cinema.util.ReturnUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.util.Assert;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Kiss
 * @date 2018/05/25 23:22
 */
@AdminController
@RequestMapping("/admin/bookInfo")
public class AdminBookInfoController {

    @Autowired
    private BookInfoService bookInfoService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private BorrowRecordService borrowRecordService;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    @PostMapping("/add")
    public ReturnObject add(@ModelAttribute BookInfoModel model,
            @RequestParam(value = "portraitImg",required = false) MultipartFile portraitImg) throws Exception {
        MemberModel member = memberService.findByUsername(model.getMember().getUsername());
        if (member == null) {
            return ReturnUtil.fail(100,"入库用户不存在",null);
        }
        model.setMember(member);
        return ReturnUtil.success(bookInfoService.add(model,portraitImg));
    }
    @PostMapping("/delete")
    public ReturnObject delete(@RequestParam("ids") Integer[] ids) throws Exception {
        bookInfoService.delete(ids);
        return ReturnUtil.success();
    }
    @PostMapping("/update")
    public ReturnObject update(@ModelAttribute BookInfoModel model,
                               @RequestParam(value = "portraitImg",required = false) MultipartFile portraitImg) throws Exception {
        MemberModel member = memberService.findByUsername(model.getMember().getUsername());
        if (member == null) {
            return ReturnUtil.fail(100,"入库用户不存在",null);
        }
        model.setMember(member);
        return ReturnUtil.success(bookInfoService.update(model,portraitImg));
    }
    @GetMapping("/view")
    public ReturnObject select(@RequestParam("id") Integer id) {
        return ReturnUtil.success(bookInfoService.findById(id));
    }
    @GetMapping("/checkSn")
    public Boolean checkSn(String sn,@RequestParam(value = "id",required = false) Integer id) {
        if (id != null) {
            if (bookInfoService.findById(id).getSn().equals(sn)) {
                return true;
            }
        }
        return bookInfoService.findByOneField("sn",sn).size() == 0;
    }
    @PostMapping("/borrow")
    public ReturnObject borrow(@RequestParam("bookInfoId") Integer bookInfoId,
                               @RequestParam("username") String username) throws Exception {
        BookInfoModel bookInfo = bookInfoService.findById(bookInfoId);
        Assert.notNull(bookInfo,"图书不存在");
        if (!bookInfo.getStatus()) {
            Assert.notNull(bookInfo,"图书已借出");
        }
        MemberModel member = memberService.findByUsername(username);
        Assert.notNull(member,"用户不存在");
        return ReturnUtil.success(borrowRecordService.create(bookInfo,member));
    }
    @PostMapping("/returnBack")
    public ReturnObject returnBack(@RequestParam("bookInfoId") Integer bookInfoId) throws Exception {
        BookInfoModel bookInfo = bookInfoService.findById(bookInfoId);
        Assert.notNull(bookInfo,"图书不存在");
        return ReturnUtil.success(borrowRecordService.returnBack(bookInfo));
    }

    @PostMapping("/list")
    public ReturnObject list(@ModelAttribute Pageable<BookInfoModel> pageable) throws Exception {
        Map additionalData = new HashMap();
        List list = pageable.getList(bookInfoService);
        additionalData.put("total",pageable.getCount());
        return ReturnUtil.success(null,list,additionalData);
    }
}
