package com.lm.lawmaster.service;

import com.lm.lawmaster.entity.Categories;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

/**
 * (Categories)表服务接口
 *
 * @author makejava
 * @since 2025-02-26 23:50:49
 */
public interface CategoriesService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Categories queryById(Integer id);

    /**
     * 分页查询
     *
     * @param categories  筛选条件
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    Page<Categories> queryByPage(Categories categories, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param categories 实例对象
     * @return 实例对象
     */
    Categories insert(Categories categories);

    /**
     * 修改数据
     *
     * @param categories 实例对象
     * @return 实例对象
     */
    Categories update(Categories categories);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

    List<Categories> queryAll();

//    upsert
    Categories upsert(Categories categories);
}
