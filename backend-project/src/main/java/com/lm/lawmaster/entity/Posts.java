package com.lm.lawmaster.entity;

import java.util.Date;
import java.io.Serializable;

/**
 * (Posts)实体类
 *
 * @author makejava
 */
public class Posts implements Serializable {
    private static final long serialVersionUID = 275758306807553298L;

    private Integer id;

    private Integer userId;

    private String title;

    private String content;

    private String images;

    private String tags;

    private Integer views;

    private Integer likes;

    private Date createdAt;

    private Date updatedAt;

    private Integer collected;
    private Integer istop;


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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
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
    public Integer getCollected() {
        return collected;
    }
    public void setCollected(Integer collected) {
        this.collected = collected;
    }

    public Integer getIstop() {
        return istop;
    }
    public void setIstop(Integer istop) {
        this.istop = istop;
    }
}

