package com.nix.cinema.model;

import com.nix.cinema.model.base.BaseModel;

/**
 * @author Kiss
 * @date 2018/04/29 20:44
 * 电影院
 */
public class CinemaModel extends BaseModel<CinemaModel> {
    private String cinemaSn;
    //电影院名称
    private String name;
    //影院所在地址（模拟）
    private String address;
    //文字介绍
    private String introduce;
    //简介(html文本)
    private String introduction;
    //热度
    private Integer hotNumber;
    //所属用户
    private MemberModel member;
    //log
    private String log;

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
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

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public String getCinemaSn() {
        return cinemaSn;
    }

    public void setCinemaSn(String cinemaSn) {
        this.cinemaSn = cinemaSn;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public Integer getHotNumber() {
        return hotNumber;
    }

    public void setHotNumber(Integer hotNumber) {
        this.hotNumber = hotNumber;
    }
}
