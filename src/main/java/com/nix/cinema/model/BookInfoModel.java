package com.nix.cinema.model;

import com.nix.cinema.model.base.BaseModel;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Kiss
 * @date 2018/05/25 19:55
 * 图书信息
 */
public class BookInfoModel extends BaseModel<BookInfoModel> {
    //编号
    private String sn;
    //名称
    private String name;
    //作者
    private String author;
    //译者
    private String translator;
    //价格
    private BigDecimal price;
    //ISBN编码
    private String ISBNCodel;
    //出版日期
    private Date comeUpTime;
    //图书状态(true 在库，false 借出)
    private Boolean status;
    //入库用户
    private MemberModel member;
    //入库日期
    private Date enteringDate;

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTranslator() {
        return translator;
    }

    public void setTranslator(String translator) {
        this.translator = translator;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getISBNCodel() {
        return ISBNCodel;
    }

    public void setISBNCodel(String ISBNCodel) {
        this.ISBNCodel = ISBNCodel;
    }

    public Date getComeUpTime() {
        return comeUpTime;
    }

    public void setComeUpTime(Date comeUpTime) {
        this.comeUpTime = comeUpTime;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public MemberModel getMember() {
        return member;
    }

    public void setMember(MemberModel member) {
        this.member = member;
    }

    public Date getEnteringDate() {
        return enteringDate;
    }

    public void setEnteringDate(Date enteringDate) {
        this.enteringDate = enteringDate;
    }
}
