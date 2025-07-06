package com.lm.lawmaster.service;

import com.lm.lawmaster.entity.Posts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Map;

/**
 * (Posts)表服务接口
 *
 * @author makejava
 */
public interface PostsService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Posts queryById(Integer id);

    /**
     * 分页查询
     *
     * @param posts       筛选条件
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    Page<Posts> queryByPage(Posts posts, PageRequest pageRequest);
    /**
     * 新增数据
     *
     * @param posts 实例对象
     * @return 实例对象
     */
    Posts insert(Posts posts);

    List<Posts> queryAll();

    /**
     * 修改数据
     *
     * @param posts 实例对象
     * @return 实例对象
     */
    Posts update(Posts posts);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

    List<Posts> queryByTag(String tag);
    //    query posts by post id, reveive an array of post id
    List<Posts> queryByPostIds(List<Integer> postId);

//    queryByUserId
    List<Posts> queryByUserId(Integer userId);

//    queryByKeyword
    List<Posts> queryByKeyword(String keyword);
//   query weekly post count sort by date  queryWeekly()
List<Map<String, Object>> queryWeekly();

//addViews
    Posts addViews(Integer id);
//    setTop
    int setTop(Integer id);
//    getTopPosts
List<Posts> getTopPosts();


}
