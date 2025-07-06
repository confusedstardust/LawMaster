package com.lm.lawmaster.service.impl;

import com.lm.lawmaster.entity.UserAnswers;
import com.lm.lawmaster.dao.UserAnswersDao;
import com.lm.lawmaster.service.UserAnswersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.In;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import java.util.List;
/**
 * (UserAnswers)表服务实现类
 *
 * @author makejava
 * @since 2025-03-08 00:49:12
 */
@Service("userAnswersService")
public class UserAnswersServiceImpl implements UserAnswersService {
    @Autowired
    private UserAnswersDao userAnswersDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public UserAnswers queryById(Integer id) {
        return this.userAnswersDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param userAnswers 筛选条件
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @Override
    public Page<UserAnswers> queryByPage(UserAnswers userAnswers, PageRequest pageRequest) {
        long total = this.userAnswersDao.count(userAnswers);
        return new PageImpl<>(this.userAnswersDao.queryAllByLimit(userAnswers, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param userAnswers 实例对象
     * @return 实例对象
     */
    @Override
    public UserAnswers insert(UserAnswers userAnswers) {
        this.userAnswersDao.insert(userAnswers);
        return userAnswers;
    }

    /**
     * 修改数据
     *
     * @param userAnswers 实例对象
     * @return 实例对象
     */
    @Override
    public UserAnswers update(UserAnswers userAnswers) {
        this.userAnswersDao.update(userAnswers);
        return this.queryById(userAnswers.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.userAnswersDao.deleteById(id) > 0;
    }

    @Override
    public Boolean saveUserAnswers(List<UserAnswers> userAnswers) {
        for (UserAnswers userAnswer : userAnswers) {
            this.userAnswersDao.insert(userAnswer);
        }
        return true;
    }
//    queryIncorrectAnswers
    @Override
    public List<UserAnswers> queryIncorrectAnswers(Integer userId) {
        return this.userAnswersDao.queryIncorrectAnswers(userId);
    }

//        List<UserAnswers> queryDistinctAnswers(Integer userId);
    @Override
    public List<UserAnswers> queryDistinctAnswers(Integer userId) {
        return this.userAnswersDao.queryDistinctAnswers(userId);
    }
}
