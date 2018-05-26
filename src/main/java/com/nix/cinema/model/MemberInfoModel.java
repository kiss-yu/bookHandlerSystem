package com.nix.cinema.model;

import com.nix.cinema.model.base.BaseModel;

/**
 * @author Kiss
 * @date 2018/05/25 19:46
 */
public class MemberInfoModel extends BaseModel<MemberInfoModel> {

    //学号
    private String studentId;
    //院系
    private CollegeModel college;
    //专业
    private ProfessionalModel professional;
    //可借阅最大数量
    private Integer max;
    //可借阅期限（小时）
    private Integer timeLimit;
    //已经借阅数量
    private Integer borrowedNum;

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public CollegeModel getCollege() {
        return college;
    }

    public void setCollege(CollegeModel college) {
        this.college = college;
    }

    public ProfessionalModel getProfessional() {
        return professional;
    }

    public void setProfessional(ProfessionalModel professional) {
        this.professional = professional;
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public Integer getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(Integer timeLimit) {
        this.timeLimit = timeLimit;
    }

    public Integer getBorrowedNum() {
        return borrowedNum;
    }

    public void setBorrowedNum(Integer borrowedNum) {
        this.borrowedNum = borrowedNum;
    }
}
