package com.nix.cinema.model;

/**
 * @author Kiss
 * @date 2018/05/30 22:08
 */

public class BorrowTime{
    private Integer bookInfoId;
    private Long sumTime;

    public Integer getBookInfoId() {
        return bookInfoId;
    }

    public void setBookInfoId(Integer bookInfoId) {
        this.bookInfoId = bookInfoId;
    }

    public Long getSumTime() {
        return sumTime;
    }

    public void setSumTime(Long sumTime) {
        this.sumTime = sumTime;
    }
}