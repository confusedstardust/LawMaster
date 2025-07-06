package com.lm.lawmaster.service;

import com.lm.lawmaster.entity.UserAnswers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.relational.core.sql.In;

import java.util.List;

/**
 * (UserAnswers)表服务接口
 *
 * @author makejava
 * @since 2025-03-08 00:49:12
 */
public interface UserAnswersService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    UserAnswers queryById(Integer id);

    /**
     * 分页查询
     *
     * @param userAnswers 筛选条件
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    Page<UserAnswers> queryByPage(UserAnswers userAnswers, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param userAnswers 实例对象
     * @return 实例对象
     */
    UserAnswers insert(UserAnswers userAnswers);

    /**
     * 修改数据
     *
     * @param userAnswers 实例对象
     * @return 实例对象
     */
    UserAnswers update(UserAnswers userAnswers);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

//    save list of user answers
    Boolean saveUserAnswers(List<UserAnswers> userAnswers);

//    queryIncorrectAnswers
    List<UserAnswers> queryIncorrectAnswers(Integer userId);

//    queryDistinctAnswers
    List<UserAnswers> queryDistinctAnswers(Integer userId);
}
