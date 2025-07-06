package com.lm.lawmaster.service;

import com.lm.lawmaster.entity.Follows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * (Follows)表服务接口
 *
 * @author makejava
 */
public interface FollowsService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Follows queryById(Integer id);

    /**
     * 分页查询
     *
     * @param follows     筛选条件
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    Page<Follows> queryByPage(Follows follows, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param follows 实例对象
     * @return 实例对象
     */
    Follows insert(Follows follows);

    /**
     * 修改数据
     *
     * @param follows 实例对象
     * @return 实例对象
     */
    Follows update(Follows follows);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

}
