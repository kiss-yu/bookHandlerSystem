package com.nix.cinema.model;

import com.nix.cinema.model.base.BaseModel;

import java.util.Date;

/**
 * @author Kiss
 * @date 2018/05/25 20:01
 * 借阅记录
 */
public class BorrowRecordModel extends BaseModel<BorrowRecordModel> {
    //借阅用户
    private MemberModel member;
    //借阅图书
    private BookInfoModel bookInfo;
    //借阅时间
    private Date borrowTime;
    //预期还书时间
    private Date shouldTime;
    //实际还书时间
    private Date returnTime;
    //状态（true 已还，false 未还）
    private Boolean status;

    public MemberModel getMember() {
        return member;
    }

    public void setMember(MemberModel member) {
        this.member = member;
    }

    public BookInfoModel getBookInfo() {
        return bookInfo;
    }

    public void setBookInfo(BookInfoModel bookInfo) {
        this.bookInfo = bookInfo;
    }

    public Date getBorrowTime() {
        return borrowTime;
    }

    public void setBorrowTime(Date borrowTime) {
        this.borrowTime = borrowTime;
    }

    public Date getShouldTime() {
        return shouldTime;
    }

    public void setShouldTime(Date shouldTime) {
        this.shouldTime = shouldTime;
    }

    public Date getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(Date returnTime) {
        this.returnTime = returnTime;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
