package com.lm.lawmaster.entity;

import java.util.Date;
import java.io.Serializable;

/**
 * (Favorites)实体类
 *
 * @author makejava
 */
public class Favorites implements Serializable {
    private static final long serialVersionUID = 663780283730405383L;

    private Integer id;

    private Integer userId;

    private Integer postId;

    private Date createdAt;


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

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

}

