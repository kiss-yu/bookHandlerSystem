package com.nix.cinema.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nix.cinema.model.base.BaseModel;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author Kiss
 * @date 2018/04/29 20:48
 * 电影
 */
public class MovieModel extends BaseModel<MovieModel> {
    private String movieSn;
    //上映时间
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date releaseTime;
    //电影名
    private String name;
    //上映的电影院
    private List<CinemaModel> cinemas;
    //出售价格
    private BigDecimal price;
    //点击量
    private Integer hitCount;
    //简介(html文本)
    private String introduction;
    //缩略图路径
    private String img;
    //文字介绍
    private String introduce;
    //电影版权所属用户
    private MemberModel member;

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public MemberModel getMember() {
        return member;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMember(MemberModel member) {
        this.member = member;
    }

    public String getMovieSn() {
        return movieSn;
    }

    public void setMovieSn(String movieSn) {
        this.movieSn = movieSn;
    }

    public Date getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(Date releaseTime) {
        this.releaseTime = releaseTime;
    }

    public Integer getHitCount() {
        return hitCount;
    }

    public void setHitCount(Integer hitCount) {
        this.hitCount = hitCount;
    }

    public List<CinemaModel> getCinemas() {
        return cinemas;
    }

    public void setCinemas(List<CinemaModel> cinemas) {
        this.cinemas = cinemas;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
