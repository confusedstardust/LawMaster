package com.lm.lawmaster.service.impl;

import com.lm.lawmaster.entity.Articles;
import com.lm.lawmaster.dao.ArticlesDao;
import com.lm.lawmaster.service.ArticlesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

/**
 * (Articles)表服务实现类
 *
 * @author makejava
 * @since 2025-03-09 19:22:30
 */
@Service("articlesService")
public class ArticlesServiceImpl implements ArticlesService {
    @Autowired
    private ArticlesDao articlesDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public Articles queryById(Integer id) {
        return this.articlesDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param articles    筛选条件
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @Override
    public Page<Articles> queryByPage(Articles articles, PageRequest pageRequest) {
        long total = this.articlesDao.count(articles);
        return new PageImpl<>(this.articlesDao.queryAllByLimit(articles, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param articles 实例对象
     * @return 实例对象
     */
    @Override
    public Articles insert(Articles articles) {
        this.articlesDao.insert(articles);
        return articles;
    }

    /**
     * 修改数据
     *
     * @param articles 实例对象
     * @return 实例对象
     */
    @Override
    public Articles update(Articles articles) {
        this.articlesDao.update(articles);
        return this.queryById(articles.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.articlesDao.deleteById(id) > 0;
    }
//    queryAllArticles
    @Override
    public List<Articles> queryAllArticles() {
        return this.articlesDao.queryAllArticles();
    }
//    searchArticles
    @Override
    public List<Articles> searchArticles(String keyword) {
        return this.articlesDao.searchArticles(keyword);
    }
//    Boolean setVisible(Integer id);
    @Override
    public Boolean setVisible(Integer id, Boolean flag) {
        if (flag){
            return this.articlesDao.setVisible(id,1);
        }else
            return this.articlesDao.setVisible(id,0);
    }

//    queryVisible
    @Override
    public List<Articles> queryVisible() {
        return this.articlesDao.queryVisible();
    }
//    addViews
    @Override
    public Articles addViews(Integer id) {
        Articles articles=this.queryById(id);
        articles.setViews(articles.getViews()+1);
        return this.update(articles);
    }
}

