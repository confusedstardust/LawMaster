package com.lm.lawmaster.dao;

import com.lm.lawmaster.entity.Articles;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * (Articles)表数据库访问层
 *
 * @author makejava
 * @since 2025-03-09 19:22:29
 */
public interface ArticlesDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Articles queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param articles 查询条件
     * @param pageable 分页对象
     * @return 对象列表
     */
    List<Articles> queryAllByLimit(Articles articles, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param articles 查询条件
     * @return 总行数
     */
    long count(Articles articles);

    /**
     * 新增数据
     *
     * @param articles 实例对象
     * @return 影响行数
     */
    int insert(Articles articles);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<Articles> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<Articles> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<Articles> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<Articles> entities);

    /**
     * 修改数据
     *
     * @param articles 实例对象
     * @return 影响行数
     */
    int update(Articles articles);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);
//    queryAllArticles
    List<Articles> queryAllArticles();

//    searchArticles
    List<Articles> searchArticles(String keyword);

//        public Boolean setVisible(Integer id, Boolean flag)
    Boolean setVisible(Integer id, Integer flag);

//    queryVisible
    List<Articles> queryVisible();
//    addViews
    Articles addViews(Integer id);

}

