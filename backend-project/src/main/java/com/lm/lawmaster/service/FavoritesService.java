package com.lm.lawmaster.service;

import com.lm.lawmaster.entity.Favorites;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

/**
 * (Favorites)表服务接口
 *
 * @author makejava
 */
public interface FavoritesService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Favorites queryById(Integer id);

    /**
     * 分页查询
     *
     * @param favorites   筛选条件
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    Page<Favorites> queryByPage(Favorites favorites, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param favorites 实例对象
     * @return 实例对象
     */
    Favorites insert(Favorites favorites);

    /**
     * 修改数据
     *
     * @param favorites 实例对象
     * @return 实例对象
     */
    Favorites update(Favorites favorites);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

    Boolean toggleFavorites(Favorites favorites);

    Favorites queryByUserIdAndPostId(Integer userId, Integer postId);

    //    queryAllByUserId method
    List<Favorites> queryAllByUserId(Integer userId);
}
