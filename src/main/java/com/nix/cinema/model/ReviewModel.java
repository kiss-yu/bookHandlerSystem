package com.nix.cinema.model;

import com.nix.cinema.model.base.BaseModel;

import java.util.List;

/**
 * @author Kiss
 * @date 2018/05/01 19:06
 * 评论
 */
public class ReviewModel extends BaseModel<ReviewModel> {

    //评论内容
    private String content;
    private MemberModel user;
    //回复评论
    private List<ReviewModel> replies;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MemberModel getUser() {
        return user;
    }

    public void setUser(MemberModel user) {
        this.user = user;
    }

    public List<ReviewModel> getReplies() {
        return replies;
    }

    public void setReplies(List<ReviewModel> replies) {
        this.replies = replies;
    }
}
