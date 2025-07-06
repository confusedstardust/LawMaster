package com.lm.lawmaster.entity;

import java.util.Date;
import java.io.Serializable;

/**
 * (Articles)实体类
 *
 * @author makejava
 * @since 2025-03-09 19:22:29
 */
public class Articles implements Serializable {
    private static final long serialVersionUID = -27170415233859384L;
    /**
     * 新闻ID
     */
    private Integer id;
    /**
     * 标题
     */
    private String title;
    /**
     * 新闻内容
     */
    private String content;
    /**
     * 分类ID
     */
    private Integer categoryId;
    /**
     * 新闻来源
     */
    private String source;
    /**
     * 浏览次数
     */
    private Integer views;
    /**
     * 点赞数
     */
    private Integer likes;
    /**
     * 发布时间
     */
    private Date createdAt;
    /**
     * 用户是否可见
     */
    private Integer visible;
    /**
     * 类型 知识/新闻
     */
    private String type;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
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

    public Integer getVisible() {
        return visible;
    }

    public void setVisible(Integer visible) {
        this.visible = visible;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}

