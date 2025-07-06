package com.lm.lawmaster.entity;

import java.util.Date;
import java.io.Serializable;

/**
 * (Follows)实体类
 *
 * @author makejava
 */
public class Follows implements Serializable {
    private static final long serialVersionUID = -38671307845624140L;

    private Integer id;

    private Integer followerId;

    private Integer followedId;

    private Date createdAt;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFollowerId() {
        return followerId;
    }

    public void setFollowerId(Integer followerId) {
        this.followerId = followerId;
    }

    public Integer getFollowedId() {
        return followedId;
    }

    public void setFollowedId(Integer followedId) {
        this.followedId = followedId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

}

