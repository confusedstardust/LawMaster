package com.lm.lawmaster.service.impl;

import com.lm.lawmaster.entity.Comments;
import com.lm.lawmaster.dao.CommentsDao;
import com.lm.lawmaster.service.CommentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;


/**
 * (Comments)表服务实现类
 *
 * @author makejava
 * @since 2025-02-26 23:51:04
 */
@Service("commentsService")
public class CommentsServiceImpl implements CommentsService {
    @Autowired
    private CommentsDao commentsDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public Comments queryById(Integer id) {
        return this.commentsDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param comments    筛选条件
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @Override
    public Page<Comments> queryByPage(Comments comments, PageRequest pageRequest) {
        long total = this.commentsDao.count(comments);
        return new PageImpl<>(this.commentsDao.queryAllByLimit(comments, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param comments 实例对象
     * @return 实例对象
     */
    @Override
    public Comments insert(Comments comments) {
        this.commentsDao.insert(comments);
        return comments;
    }

    /**
     * 修改数据
     *
     * @param comments 实例对象
     * @return 实例对象
     */
    @Override
    public Comments update(Comments comments) {
        this.commentsDao.update(comments);
        return this.queryById(comments.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
          return this.commentsDao.deleteById(id) > 0;
    }


    @Override
    public List<Comments> queryByPostId(Integer postId) {
        return this.commentsDao.queryByPostId(postId);
    }

//    queryByUserId
    @Override
    public List<Comments> queryByUserId(Integer userId) {
        return this.commentsDao.queryByUserId(userId);
    }

}
