package com.lm.lawmaster.dao;

import com.lm.lawmaster.entity.Categories;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * (Categories)表数据库访问层
 *
 * @author makejava
 * @since 2025-02-26 23:50:49
 */
public interface CategoriesDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Categories queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param categories 查询条件
     * @param pageable   分页对象
     * @return 对象列表
     */
    List<Categories> queryAllByLimit(Categories categories, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param categories 查询条件
     * @return 总行数
     */
    long count(Categories categories);

    /**
     * 新增数据
     *
     * @param categories 实例对象
     * @return 影响行数
     */
    int insert(Categories categories);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<Categories> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<Categories> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<Categories> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<Categories> entities);

    /**
     * 修改数据
     *
     * @param categories 实例对象
     * @return 影响行数
     */
    int update(Categories categories);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param categories 实例对象
     * @return 对象列表
     */
    List<Categories> queryAll();

}

