package com.lm.lawmaster.service;

import com.lm.lawmaster.entity.Collects;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

/**
 * (Collects)表服务接口
 *
 * @author makejava
 * @since 2025-03-01 16:23:25
 */
public interface CollectsService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Collects queryById(Integer id);

    /**
     * 分页查询
     *
     * @param collects    筛选条件
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    Page<Collects> queryByPage(Collects collects, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param collects 实例对象
     * @return 实例对象
     */
    Collects insert(Collects collects);

    /**
     * 修改数据
     *
     * @param collects 实例对象
     * @return 实例对象
     */
    Collects update(Collects collects);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

/**
 * 切换收藏状态的方法。
 * 该方法用于在用户收藏列表中添加或移除某个项目。
 *
 * @param id 项目的唯一标识符。
 * @param userId 用户的唯一标识符。
 */
Boolean toggleCollect(Collects collects);

Collects queryByUserIdAndPostId(Integer userId, Integer postId);
    //    queryAllByUserId method
    List<Collects> queryAllByUserId(Integer userId);

}
