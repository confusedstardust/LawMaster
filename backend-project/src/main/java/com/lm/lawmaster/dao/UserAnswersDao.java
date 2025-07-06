package com.lm.lawmaster.dao;

import com.lm.lawmaster.entity.UserAnswers;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * (UserAnswers)表数据库访问层
 *
 * @author makejava
 * @since 2025-03-08 00:49:12
 */
public interface UserAnswersDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    UserAnswers queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param userAnswers 查询条件
     * @param pageable    分页对象
     * @return 对象列表
     */
    List<UserAnswers> queryAllByLimit(UserAnswers userAnswers, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param userAnswers 查询条件
     * @return 总行数
     */
    long count(UserAnswers userAnswers);

    /**
     * 新增数据
     *
     * @param userAnswers 实例对象
     * @return 影响行数
     */
    int insert(UserAnswers userAnswers);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<UserAnswers> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<UserAnswers> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<UserAnswers> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<UserAnswers> entities);

    /**
     * 修改数据
     *
     * @param userAnswers 实例对象
     * @return 影响行数
     */
    int update(UserAnswers userAnswers);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

//    queryIncorrectAnswers
    List<UserAnswers> queryIncorrectAnswers(Integer userId);

//    queryDistinctAnswers
    List<UserAnswers> queryDistinctAnswers(Integer userId);

}

