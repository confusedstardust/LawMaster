package com.lm.lawmaster.entity;

import java.util.Date;
import java.io.Serializable;

/**
 * (Comments)实体类
 *
 * @author makejava
 * @since 2025-02-26 23:51:03
 */
public class Comments implements Serializable {
    private static final long serialVersionUID = 756467148372652152L;

    private Integer id;

    private Integer postId;

    private Integer userId;

    private String content;

    private Integer likes;

    private Date createdAt;

    private Date updatedAt;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

}

