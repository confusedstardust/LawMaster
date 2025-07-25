package com.lm.lawmaster.dao;

import com.lm.lawmaster.entity.Favorites;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * (Favorites)表数据库访问层
 *
 * @author makejava
 */
public interface FavoritesDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Favorites queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param favorites 查询条件
     * @param pageable  分页对象
     * @return 对象列表
     */
    List<Favorites> queryAllByLimit(Favorites favorites, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param favorites 查询条件
     * @return 总行数
     */
    long count(Favorites favorites);

    /**
     * 新增数据
     *
     * @param favorites 实例对象
     * @return 影响行数
     */
    int insert(Favorites favorites);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<Favorites> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<Favorites> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<Favorites> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<Favorites> entities);

    /**
     * 修改数据
     *
     * @param favorites 实例对象
     * @return 影响行数
     */
    int update(Favorites favorites);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

    Favorites queryByUserIdAndPostId(Integer userId, Integer postId);

    Favorites queryByUserId(Integer userId);

//    query all favourite by user's id
    List<Favorites> queryAllByUserId(Integer userId);

}

