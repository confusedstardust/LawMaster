package com.lm.lawmaster.service.impl;

import com.lm.lawmaster.entity.Categories;
import com.lm.lawmaster.dao.CategoriesDao;
import com.lm.lawmaster.service.CategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;


/**
 * (Categories)表服务实现类
 *
 * @author makejava
 * @since 2025-02-26 23:50:49
 */
@Service("categoriesService")
public class CategoriesServiceImpl implements CategoriesService {
    @Autowired
    private CategoriesDao categoriesDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public Categories queryById(Integer id) {
        return this.categoriesDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param categories  筛选条件
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @Override
    public Page<Categories> queryByPage(Categories categories, PageRequest pageRequest) {
        long total = this.categoriesDao.count(categories);
        return new PageImpl<>(this.categoriesDao.queryAllByLimit(categories, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param categories 实例对象
     * @return 实例对象
     */
    @Override
    public Categories insert(Categories categories) {
        this.categoriesDao.insert(categories);
        return categories;
    }

    /**
     * 修改数据
     *
     * @param categories 实例对象
     * @return 实例对象
     */
    @Override
    public Categories update(Categories categories) {
        this.categoriesDao.update(categories);
        return this.queryById(categories.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.categoriesDao.deleteById(id) > 0;
    }

    /**
     * 通过实体作为筛选条件查询
     *
     * @param categories 实例对象
     * @return 对象列表
     */
    @Override
    public List<Categories> queryAll() {
        return this.categoriesDao.queryAll();
    }

//    upsert
    @Override
    public Categories upsert(Categories categories) {
        if (categories.getId() == null) {
            return this.insert(categories);
        }
        else {
            return this.update(categories);

        }
    }
}
