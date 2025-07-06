package com.lm.lawmaster.entity;

import java.io.Serializable;

/**
 * (Categories)实体类
 *
 * @author makejava
 * @since 2025-02-26 23:50:49
 */
public class Categories implements Serializable {
    private static final long serialVersionUID = -59485056894477694L;

    private Integer id;

    private String name;

    private String description;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}

