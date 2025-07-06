package com.lm.lawmaster.dao;

import com.lm.lawmaster.entity.Posts;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * (Posts)表数据库访问层
 *
 * @author makejava
 */
public interface PostsDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Posts queryById(Integer id);


    List<Posts> queryAll();
    /**
     * 查询指定行数据
     *
     * @param posts    查询条件
     * @param pageable 分页对象
     * @return 对象列表
     */
    List<Posts> queryAllByLimit(Posts posts, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param posts 查询条件
     * @return 总行数
     */
    long count(Posts posts);

    /**
     * 新增数据
     *
     * @param posts 实例对象
     * @return 影响行数
     */
    int insert(Posts posts);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<Posts> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<Posts> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<Posts> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<Posts> entities);

    /**
     * 修改数据
     *
     * @param posts 实例对象
     * @return 影响行数
     */
    int update(Posts posts);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);
    List<Posts> queryByTag(String tag);

    List<Posts> queryByPostIds(List<Integer> postIds);

    List<Posts> queryByUserId(Integer userId);

//    queryByKeyword
    List<Posts> queryByKeyword(String keyword);

//    findWeeklyPostCount
    List<Map<String, Object>> findWeeklyPostCount();
//    getTopPosts
    List<Posts> getTopPosts();
}


