package com.lm.lawmaster.service;

import com.lm.lawmaster.entity.Articles;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
//import list
import java.util.List;
/**
 * (Articles)表服务接口
 *
 * @author makejava
 * @since 2025-03-09 19:22:30
 */
public interface ArticlesService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Articles queryById(Integer id);

    /**
     * 分页查询
     *
     * @param articles    筛选条件
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    Page<Articles> queryByPage(Articles articles, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param articles 实例对象
     * @return 实例对象
     */
    Articles insert(Articles articles);

    /**
     * 修改数据
     *
     * @param articles 实例对象
     * @return 实例对象
     */
    Articles update(Articles articles);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

//    queryAllArticles
    List<Articles> queryAllArticles();

//    searchArticles
List<Articles> searchArticles(String keyword);
//    setVisible
Boolean setVisible(Integer id, Boolean flag);
//    queryVisible
List<Articles> queryVisible();
//    addViews
Articles addViews(Integer id);
}

