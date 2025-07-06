package com.lm.lawmaster.dao;

import com.lm.lawmaster.entity.Questions;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * (Questions)表数据库访问层
 *
 * @author makejava
 * @since 2025-02-26 23:51:29
 */
public interface QuestionsDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Questions queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param questions 查询条件
     * @param pageable  分页对象
     * @return 对象列表
     */
    List<Questions> queryAllByLimit(Questions questions, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param questions 查询条件
     * @return 总行数
     */
    long count(Questions questions);

    /**
     * 新增数据
     *
     * @param questions 实例对象
     * @return 影响行数
     */
    int insert(Questions questions);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<Questions> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<Questions> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<Questions> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<Questions> entities);

    /**
     * 修改数据
     *
     * @param questions 实例对象
     * @return 影响行数
     */
    int update(Questions questions);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

// 定义一个方法，用于根据分类ID查询问题列表
// 方法的返回类型是List<Questions>，表示返回一个包含Questions对象的列表
// 方法的名称是queryByCategoryId，表示根据分类ID进行查询
// 方法的参数是Integer类型的id，表示要查询的分类ID
    List<Questions> queryByCategoryId(Integer id);

    //query all questions
    List<Questions> queryAll();

    //query question by id array
    List<Questions> queryByIdArray(List<Integer> ids);

//    searchQuestions(keyword)
    List<Questions> searchQuestions(String keyword);

//    Map<String, Objects> countByCategory()
    List<Map<String, Object>> countByCategory();
}

