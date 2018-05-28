package com.nix.cinema.service.impl;

import com.nix.cinema.Exception.WebException;
import com.nix.cinema.dao.BorrowRecordMapper;
import com.nix.cinema.model.BookInfoModel;
import com.nix.cinema.model.BorrowRecordModel;
import com.nix.cinema.model.MemberModel;
import com.nix.cinema.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;

/**
 * @author Kiss
 * @date 2018/05/25 23:07
 */
@Service
public class BorrowRecordService extends BaseService<BorrowRecordModel> {
    @Autowired
    private BookInfoService bookInfoService;

    @Transactional(rollbackFor = Exception.class)
    public BorrowRecordModel create(BookInfoModel bookInfo, MemberModel member) throws Exception {
        BorrowRecordModel borrowRecord = new BorrowRecordModel();
        borrowRecord.setBookInfo(bookInfo);
        borrowRecord.setMember(member);
        borrowRecord.setBorrowTime(new Date());
        borrowRecord.setStatus(false);
        borrowRecord.setShouldTime(new Date(System.currentTimeMillis() + member.getMemberInfo().getTimeLimit() * 60 * 60 * 1000));
        //借阅时锁住图书 保证只能被一个用户借阅
        synchronized (BookInfoService.BORROW_CLOCK) {
            BookInfoModel bookInfoModel = bookInfoService.findById(bookInfo.getId());
            if (bookInfoModel.getStatus()) {
                super.add(borrowRecord);
                bookInfoService.borrow(bookInfo);
            }else {
                throw new WebException(100,"图书已经被借阅出");
            }
        }
        return borrowRecord;
    }

    public BorrowRecordModel returnBack(BookInfoModel model) throws Exception {
        List<BorrowRecordModel> list = list(null,null,null,null,
                "status = 0 and bookInfo = " + model.getId());
        if (list.size() != 1) {
            throw new WebException(100,"借阅未归还记录不存在");
        }
        BorrowRecordModel borrowRecordModel = list.get(0);
        borrowRecordModel.setStatus(true);
        super.update(borrowRecordModel);
        model.setStatus(true);
        bookInfoService.update(model);
        return borrowRecordModel;
    }
}
