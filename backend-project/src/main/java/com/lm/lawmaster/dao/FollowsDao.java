package com.lm.lawmaster.dao;

import com.lm.lawmaster.entity.Follows;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * (Follows)表数据库访问层
 *
 * @author makejava
 */
public interface FollowsDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Follows queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param follows  查询条件
     * @param pageable 分页对象
     * @return 对象列表
     */
    List<Follows> queryAllByLimit(Follows follows, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param follows 查询条件
     * @return 总行数
     */
    long count(Follows follows);

    /**
     * 新增数据
     *
     * @param follows 实例对象
     * @return 影响行数
     */
    int insert(Follows follows);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<Follows> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<Follows> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<Follows> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<Follows> entities);

    /**
     * 修改数据
     *
     * @param follows 实例对象
     * @return 影响行数
     */
    int update(Follows follows);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}

