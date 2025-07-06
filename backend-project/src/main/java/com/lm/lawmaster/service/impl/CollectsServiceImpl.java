package com.lm.lawmaster.service.impl;

import com.lm.lawmaster.entity.Collects;
import com.lm.lawmaster.dao.CollectsDao;
import com.lm.lawmaster.service.CollectsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

/**
 * (Collects)表服务实现类
 *
 * @author makejava
 * @since 2025-03-01 16:23:25
 */
@Service("collectsService")
public class CollectsServiceImpl implements CollectsService {
    @Autowired
    private CollectsDao collectsDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public Collects queryById(Integer id) {
        return this.collectsDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param collects    筛选条件
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @Override
    public Page<Collects> queryByPage(Collects collects, PageRequest pageRequest) {
        long total = this.collectsDao.count(collects);
        return new PageImpl<>(this.collectsDao.queryAllByLimit(collects, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param collects 实例对象
     * @return 实例对象
     */
    @Override
    public Collects insert(Collects collects) {
        this.collectsDao.insert(collects);
        return collects;
    }

    /**
     * 修改数据
     *
     * @param collects 实例对象
     * @return 实例对象
     */
    @Override
    public Collects update(Collects collects) {
        this.collectsDao.update(collects);
        return this.queryById(collects.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.collectsDao.deleteById(id) > 0;
    }

    /**
     * 切换收藏状态的方法。
     * 该方法用于在用户收藏列表中添加或移除某个项目。
     *
     * @param id 项目的唯一标识符。
     * @param userId 用户的唯一标识符。
     */
    @Override
    public Boolean toggleCollect(Collects collects) {
        //    query if userid exists in collects table,if exists delete it, else add it
        Collects collect = this.collectsDao.queryByUserIdAndPostId(collects.getUserId(),collects.getPostId());
        if (collect != null) {
            //        delete it
            return this.collectsDao.deleteById(collect.getId()) > 0;
        }
        //        add it
        return this.collectsDao.insert(collects) > 0;

    }

    @Override
    public Collects queryByUserIdAndPostId(Integer userId, Integer postId) {
        return this.collectsDao.queryByUserIdAndPostId(userId, postId);
    }

    //    queryAllByUserId method
    @Override
    public List<Collects> queryAllByUserId(Integer userId) {
        return this.collectsDao.queryAllByUserId(userId);
    }
}
