package com.lm.lawmaster.dao;

import com.lm.lawmaster.entity.Collects;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * (Collects)表数据库访问层
 *
 * @author makejava
 * @since 2025-03-01 16:23:25
 */
public interface CollectsDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Collects queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param collects 查询条件
     * @param pageable 分页对象
     * @return 对象列表
     */
    List<Collects> queryAllByLimit(Collects collects, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param collects 查询条件
     * @return 总行数
     */
    long count(Collects collects);

    /**
     * 新增数据
     *
     * @param collects 实例对象
     * @return 影响行数
     */
    int insert(Collects collects);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<Collects> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<Collects> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<Collects> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<Collects> entities);

    /**
     * 修改数据
     *
     * @param collects 实例对象
     * @return 影响行数
     */
    int update(Collects collects);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

    Collects queryByUserIdAndPostId(Integer userId, Integer postId);


    Collects queryByUserId(Integer userId);

    //    queryAllByUserId method
    List<Collects> queryAllByUserId(Integer userId);

}

