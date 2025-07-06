package com.lm.lawmaster.service.impl;

import com.lm.lawmaster.entity.Questions;
import com.lm.lawmaster.dao.QuestionsDao;
import com.lm.lawmaster.service.QuestionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Map;


/**
 * (Questions)表服务实现类
 *
 * @author makejava
 * @since 2025-02-26 23:51:30
 */
@Service("questionsService")
public class QuestionsServiceImpl implements QuestionsService {
    @Autowired
    private QuestionsDao questionsDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public Questions queryById(Integer id) {
        return this.questionsDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param questions   筛选条件
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @Override
    public Page<Questions> queryByPage(Questions questions, PageRequest pageRequest) {
        long total = this.questionsDao.count(questions);
        return new PageImpl<>(this.questionsDao.queryAllByLimit(questions, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param questions 实例对象
     * @return 实例对象
     */
    @Override
    public Questions insert(Questions questions) {
        this.questionsDao.insert(questions);
        return questions;
    }

    /**
     * 修改数据
     *
     * @param questions 实例对象
     * @return 实例对象
     */
    @Override
    public Questions update(Questions questions) {
        this.questionsDao.update(questions);
        return this.queryById(questions.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.questionsDao.deleteById(id) > 0;
    }

    @Override
    public List<Questions> queryByCategoryId(Integer id) {
        return this.questionsDao.queryByCategoryId(id);
    }

    @Override
    public List<Questions> queryAll() {
        return this.questionsDao.queryAll();
    }

    @Override
    public List<Questions> queryByIdArray(List<Integer> ids) {
        return this.questionsDao.queryByIdArray(ids);
    }

    public List<Questions> searchQuestions(String keyword){
        return this.questionsDao.searchQuestions(keyword);
    }

//    countByCategory
    @Override
    public List<Map<String, Object>> countByCategory() {
        return questionsDao.countByCategory();
    }
}
