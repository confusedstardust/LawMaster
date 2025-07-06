package com.lm.lawmaster.service.impl;

import com.lm.lawmaster.entity.Posts;
import com.lm.lawmaster.dao.PostsDao;
import com.lm.lawmaster.service.PostsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.*;

//import javax.annotation.Resource;

/**
 * (Posts)表服务实现类
 *
 * @author makejava
 */
@Service("postsService")
public class PostsServiceImpl implements PostsService {
    @Autowired
    private PostsDao postsDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public Posts queryById(Integer id) {
        return this.postsDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param posts       筛选条件
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @Override
    public Page<Posts> queryByPage(Posts posts, PageRequest pageRequest) {
        long total = this.postsDao.count(posts);
        return new PageImpl<>(this.postsDao.queryAllByLimit(posts, pageRequest), pageRequest, total);
    }



    /**
     * 新增数据
     *
     * @param posts 实例对象
     * @return 实例对象
     */
    @Override
    public Posts insert(Posts posts) {
        posts.setLikes(0);
        posts.setCollected(0);
        posts.setViews(0);
        posts.setCreatedAt(new Date());
        posts.setUpdatedAt(new Date());
        posts.setIstop(0);

        System.out.println(posts.getImages());
        this.postsDao.insert(posts);
        return posts;
    }

    /**
     * 修改数据
     *
     * @param posts 实例对象
     * @return 实例对象
     */
    @Override
    public Posts update(Posts posts) {
        this.postsDao.update(posts);
        return this.queryById(posts.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.postsDao.deleteById(id) > 0;
    }


    @Override
    public List<Posts> queryAll() {
        return this.postsDao.queryAll();
    }

    public List<Posts> queryByTag(String tag) {
        return this.postsDao.queryByTag(tag);
    }

    //    query posts by post id, reveive an array of post id
    public List<Posts> queryByPostIds(List<Integer> postId) {
        return this.postsDao.queryByPostIds(postId);
    }

//    queryByUserId
    public List<Posts> queryByUserId(Integer userId) {
        return this.postsDao.queryByUserId(userId);
    }

//    queryByKeyword
    public List<Posts> queryByKeyword(String keyword) {
        return this.postsDao.queryByKeyword(keyword);
    }

//    query weekly post count sort by date  queryWeekly()
public List<Map<String, Object>> queryWeekly() {
    // 获取当前日期，并计算过去 7 天的日期
    LocalDate today = LocalDate.now();
    LocalDate startDate = today.minusDays(6); // 7 天前的日期（含今天）

    // 查询数据库获取已存在的发帖统计数据
    List<Map<String, Object>> postCounts = postsDao.findWeeklyPostCount();

    // 创建一个日期 -> 统计数的映射
    Map<LocalDate, Long> postCountMap = new HashMap<>();
    for (Map<String, Object> entry : postCounts) {
        LocalDate date = LocalDate.parse(entry.get("postDate").toString()); // 解析日期
        Long count = ((Number) entry.get("postCount")).longValue(); // 解析发帖数
        postCountMap.put(date, count);
    }

    // 生成完整的 7 天数据
    List<Map<String, Object>> result = new ArrayList<>();
    for (LocalDate date = startDate; !date.isAfter(today); date = date.plusDays(1)) {
        Map<String, Object> data = new HashMap<>();
        data.put("postDate", date.toString());
        data.put("postCount", postCountMap.getOrDefault(date, 0L)); // 如果没有数据，默认 0
        result.add(data);
    }

    return result;
}
//addViews
    @Override
    public Posts addViews(Integer id) {
        Posts posts=this.queryById(id);
        posts.setViews(posts.getViews()+1);
        return this.update(posts);
    }
//set top
    @Override
    public int setTop(Integer id) {
        Posts posts=this.queryById(id);
        Integer istop=posts.getIstop();
        if (istop==null){
            istop=0;
        }
        if (istop==1){
            posts.setIstop(0);
        }
        else {
            posts.setIstop(1);
        }
        return this.postsDao.update(posts);
    }
//    getTopPosts
    @Override
    public List<Posts> getTopPosts() {
        return this.postsDao.getTopPosts();
    }
}
