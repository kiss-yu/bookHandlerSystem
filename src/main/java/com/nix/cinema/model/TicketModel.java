package com.nix.cinema.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nix.cinema.model.base.BaseModel;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

/**
 * @author Kiss
 * @date 2018/04/29 20:16
 * 电影票
 */
public class TicketModel extends BaseModel<TicketModel> {
    private String ticketSn;
    private BigDecimal price;
    //所属电影院
    private CinemaModel cinema;
    //属于哪个电影
    private MovieModel movie;
    //模拟房间
    private String room;
    //一共多少张
    private Integer ticketSum;
    //已经卖出的编号列表[1,2,3]
    private String sellSn;

    //老板
    private MemberModel member;

    //剩余票数
    private Integer remain;

    public MemberModel getMember() {
        return member;
    }

    public void setMember(MemberModel member) {
        this.member = member;
    }

    public String getTicketSn() {
        return ticketSn;
    }

    public void setTicketSn(String ticketSn) {
        this.ticketSn = ticketSn;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public CinemaModel getCinema() {
        return cinema;
    }

    public void setCinema(CinemaModel cinema) {
        this.cinema = cinema;
    }

    public MovieModel getMovie() {
        return movie;
    }

    public void setMovie(MovieModel movie) {
        this.movie = movie;
    }

    public Integer getTicketSum() {
        return ticketSum;
    }

    public void setTicketSum(Integer ticketSum) {
        this.ticketSum = ticketSum;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getSellSn() {
        return sellSn;
    }

    public void setSellSn(String sellSn) {
        this.sellSn = sellSn;
//        this.sellSn = StringUtils.arrayToCommaDelimitedString(sellSn);
    }

    public Integer getRemain() {
        return ticketSum - StringUtils.commaDelimitedListToStringArray(sellSn).length;
    }

    public void setRemain(Integer remain) {
        this.remain = remain;
    }
    public Integer[] getSellTicketIndex() {
        Integer[] index = new Integer[getRemain()];
        String[] sells = StringUtils.commaDelimitedListToStringArray(getSellSn());
        all: for (int i = 0,k = 0;i < ticketSum && k < index.length ;i ++,k ++) {
            if (sells.length == 0) {
                index[k] = i;
            }
            for (int j = 0;j < sells.length;j ++) {
                if (Integer.parseInt(sells[j]) == i) {
                    k--;
                    continue all;
                }
                index[k] = i;
            }
        }
        return index;
    }
}
