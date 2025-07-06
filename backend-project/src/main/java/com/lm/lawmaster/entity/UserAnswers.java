package com.lm.lawmaster.entity;

import java.util.Date;
import java.io.Serializable;

/**
 * (UserAnswers)实体类
 *
 * @author makejava
 * @since 2025-03-08 00:49:12
 */
public class UserAnswers implements Serializable {
    private static final long serialVersionUID = 760930504727784656L;

    private Integer id;

    private Integer userId;

    private Integer questionId;

    private String selectedOption;

    private Integer isCorrect;

    private Date answeredAt;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public String getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(String selectedOption) {
        this.selectedOption = selectedOption;
    }

    public Integer getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(Integer isCorrect) {
        this.isCorrect = isCorrect;
    }

    public Date getAnsweredAt() {
        return answeredAt;
    }

    public void setAnsweredAt(Date answeredAt) {
        this.answeredAt = answeredAt;
    }

}

