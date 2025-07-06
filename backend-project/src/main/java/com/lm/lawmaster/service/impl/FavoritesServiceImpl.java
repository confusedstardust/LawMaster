package com.lm.lawmaster.service.impl;

import com.lm.lawmaster.entity.Collects;
import com.lm.lawmaster.entity.Favorites;
import com.lm.lawmaster.dao.FavoritesDao;
import com.lm.lawmaster.service.FavoritesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

//import javax.annotation.Resource;

/**
 * (Favorites)表服务实现类
 *
 * @author makejava
 */
@Service("favoritesService")
public class FavoritesServiceImpl implements FavoritesService {
    @Autowired
    private FavoritesDao favoritesDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public Favorites queryById(Integer id) {
        return this.favoritesDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param favorites   筛选条件
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @Override
    public Page<Favorites> queryByPage(Favorites favorites, PageRequest pageRequest) {
        long total = this.favoritesDao.count(favorites);
        return new PageImpl<>(this.favoritesDao.queryAllByLimit(favorites, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param favorites 实例对象
     * @return 实例对象
     */
    @Override
    public Favorites insert(Favorites favorites) {
        this.favoritesDao.insert(favorites);
        return favorites;
    }

    /**
     * 修改数据
     *
     * @param favorites 实例对象
     * @return 实例对象
     */
    @Override
    public Favorites update(Favorites favorites) {
        this.favoritesDao.update(favorites);
        return this.queryById(favorites.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.favoritesDao.deleteById(id) > 0;
    }


    @Override
    public Boolean toggleFavorites(Favorites favorites) {
        //    query if userid exists in collects table,if exists delete it, else add it
        Favorites singalfavorites = this.favoritesDao.queryByUserIdAndPostId(favorites.getUserId(), favorites.getPostId());
        if (singalfavorites != null) {
            //        delete it
            return this.favoritesDao.deleteById(singalfavorites.getId()) > 0;
        }
        //        add it
        return this.favoritesDao.insert(favorites) > 0;

    }

    @Override
    public Favorites queryByUserIdAndPostId(Integer userId, Integer postId) {
        return this.favoritesDao.queryByUserIdAndPostId(userId ,  postId);
    }

//    queryAllByUserId method
    @Override
    public List<Favorites> queryAllByUserId(Integer userId) {
        return this.favoritesDao.queryAllByUserId(userId);
    }
}
