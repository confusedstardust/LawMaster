package com.lm.lawmaster.service.impl;

import com.lm.lawmaster.entity.Follows;
import com.lm.lawmaster.dao.FollowsDao;
import com.lm.lawmaster.service.FollowsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

//import javax.annotation.Resource;

/**
 * (Follows)表服务实现类
 *
 * @author makejava
 */
@Service("followsService")
public class FollowsServiceImpl implements FollowsService {
    @Autowired
    private FollowsDao followsDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public Follows queryById(Integer id) {
        return this.followsDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param follows     筛选条件
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @Override
    public Page<Follows> queryByPage(Follows follows, PageRequest pageRequest) {
        long total = this.followsDao.count(follows);
        return new PageImpl<>(this.followsDao.queryAllByLimit(follows, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param follows 实例对象
     * @return 实例对象
     */
    @Override
    public Follows insert(Follows follows) {
        this.followsDao.insert(follows);
        return follows;
    }

    /**
     * 修改数据
     *
     * @param follows 实例对象
     * @return 实例对象
     */
    @Override
    public Follows update(Follows follows) {
        this.followsDao.update(follows);
        return this.queryById(follows.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.followsDao.deleteById(id) > 0;
    }
}
