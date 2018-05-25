package com.nix.cinema.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.nix.cinema.model.base.BaseModel;
import org.apache.logging.log4j.core.util.JsonUtils;

import java.math.BigDecimal;
import java.util.Set;

/**
 * @author Kiss
 * @date 2018/04/29 20:31
 * 付款单
 */
public class PaymentModel extends BaseModel<PaymentModel> {
    private String sn;

    private MemberModel member;
    //金额
    private BigDecimal amount;
    //内容{1(电影票id):[1,2,3,](位置编号)}
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public MemberModel getMember() {
        return member;
    }

    public void setMember(MemberModel member) {
        this.member = member;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    private TicketModel ticket;

    public TicketModel getTicket() {
        return ticket;
    }

    public void setTicket(TicketModel ticket) {
        this.ticket = ticket;
    }
}
