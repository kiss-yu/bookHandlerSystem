package com.nix.cinema.model;

import com.nix.cinema.model.base.BaseModel;

/**
 * @author Kiss
 * @date 2018/05/25 19:49
 * 专业
 */
public class ProfessionalModel extends BaseModel<ProfessionalModel> {
    //专业编号
    private String sn;
    //专业名称
    private String name;
    //所属学院
    private CollegeModel college;

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

    public CollegeModel getCollege() {
        return college;
    }

    public void setCollege(CollegeModel college) {
        this.college = college;
    }
}
