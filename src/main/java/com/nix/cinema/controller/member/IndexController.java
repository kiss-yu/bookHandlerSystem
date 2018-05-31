package com.nix.cinema.controller.member;

import com.nix.cinema.common.Pageable;
import com.nix.cinema.common.ReturnObject;
import com.nix.cinema.common.annotation.MemberController;
import com.nix.cinema.service.impl.BookInfoService;
import com.nix.cinema.util.ReturnUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Kiss
 * @date 2018/04/25 11:30
 */
@RequestMapping("/member/index")
@MemberController
public class IndexController {
    @Autowired
    private BookInfoService bookInfoService;

    @PostMapping("/search")
    public ReturnObject search(@RequestParam("content") String content) {
        Pageable pageable = new Pageable();
        pageable.setConditionsSql("sn like '%%?%%' or name like '%?%' or author like '%?%' or translator like '%?%' or introduce like '%?%'",
                content,content,content,content,content);
        return ReturnUtil.list(pageable,bookInfoService);
    }
}
