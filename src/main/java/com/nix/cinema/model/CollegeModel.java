package com.nix.cinema.model;

import com.nix.cinema.model.base.BaseModel;

import java.util.List;

/**
 * @author Kiss
 * @date 2018/05/25 19:49
 * 学院
 */
public class CollegeModel extends BaseModel<CollegeModel> {
    //学院编号
    private String sn;
    //学院名称
    private String name;
    //所有专业
    private List<ProfessionalModel> professionals;

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

    public List<ProfessionalModel> getProfessionals() {
        return professionals;
    }

    public void setProfessionals(List<ProfessionalModel> professionals) {
        this.professionals = professionals;
    }
}
