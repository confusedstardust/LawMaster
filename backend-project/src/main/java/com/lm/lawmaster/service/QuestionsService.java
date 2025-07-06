package com.lm.lawmaster.service;

import com.lm.lawmaster.entity.Questions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Map;

/**
 * (Questions)表服务接口
 *
 * @author makejava
 * @since 2025-02-26 23:51:30
 */
public interface QuestionsService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Questions queryById(Integer id);


    /**
     * 分页查询
     *
     * @param questions   筛选条件
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    Page<Questions> queryByPage(Questions questions, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param questions 实例对象
     * @return 实例对象
     */
    Questions insert(Questions questions);

    /**
     * 修改数据
     *
     * @param questions 实例对象
     * @return 实例对象
     */
    Questions update(Questions questions);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

    List<Questions> queryByCategoryId(Integer id);

//    query all questions
    List<Questions> queryAll();

//    query question by id array
    List<Questions> queryByIdArray(List<Integer> ids);

//    searchQuestions
    List<Questions> searchQuestions(String keyword);

//    countByCategory
List<Map<String, Object>> countByCategory();


}
