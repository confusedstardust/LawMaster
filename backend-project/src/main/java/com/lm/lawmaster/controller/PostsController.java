package com.lm.lawmaster.controller;

import com.lm.lawmaster.entity.Posts;
import com.lm.lawmaster.service.PostsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

//import javax.annotation.Resource;

/**
 * (Posts)表控制层
 *
 * @author makejava
 */
@RestController
@RequestMapping("posts")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PostsController {
    /**
     * 服务对象
     */
    @Autowired
    private PostsService postsService;

    /**
     * 分页查询
     *
     * @param posts       筛选条件
     * @param pageRequest 分页对象
     * @return 查询结果
     */
//    @GetMapping
//    public ResponseEntity<Page<Posts>> queryByPage( Posts posts, PageRequest pageRequest) {
//        return ResponseEntity.ok(this.postsService.queryByPage(posts, pageRequest));
//    }




    @GetMapping()
    public ResponseEntity<List<Posts>> queryAll() {
        return ResponseEntity.ok(this.postsService.queryAll());
    }


    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public ResponseEntity<Posts> queryById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(this.postsService.queryById(id));
    }

    /**
     * 新增数据
     *
     * @param posts 实体
     * @return 新增结果
     */
    @PostMapping("add")
    public ResponseEntity<Posts> add(@RequestBody Posts posts) {
        return ResponseEntity.ok(this.postsService.insert(posts));
    }

//    insert a list of posts
    @PostMapping("list")
    public ResponseEntity<String> addList(@RequestBody List<Posts> posts) {
        return ResponseEntity.ok("");
//        this.postsService.insertList(posts)
    }

    /**
     * 编辑数据
     *
     * @param posts 实体
     * @return 编辑结果
     */
    @PostMapping("edit")
    public ResponseEntity<Posts> edit(@RequestBody Posts posts) {
        return ResponseEntity.ok(this.postsService.update(posts));
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @DeleteMapping
    public ResponseEntity<Boolean> deleteById(Integer id) {
        return ResponseEntity.ok(this.postsService.deleteById(id));
    }

//    delete by id
    @PostMapping("delete/{id}")
    public ResponseEntity<Boolean> deleteByPostId(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(this.postsService.deleteById(id));
    }

//    query by tag
    @GetMapping("tag/{tag}")
    public ResponseEntity<List<Posts>> queryByTag(@PathVariable("tag") String tag) {
        return ResponseEntity.ok(this.postsService.queryByTag(tag));
    }

//    query posts by post id, reveive an array of post id
    @GetMapping("postIds")
    public ResponseEntity<List<Posts>> queryByPostIds(@RequestParam("postIds") List<Integer> postIds) {
        return ResponseEntity.ok(this.postsService.queryByPostIds(postIds));
    }

//    query by user id
    @GetMapping("user/{userId}")
    public ResponseEntity<List<Posts>> queryByUserId(@PathVariable("userId") Integer userId) {
        return ResponseEntity.ok(this.postsService.queryByUserId(userId));
    }
//query by keyword
@GetMapping("keyword/{keyword}")
    public ResponseEntity<List<Posts>> queryByKeyword(@PathVariable("keyword") String keyword) {
        return ResponseEntity.ok(this.postsService.queryByKeyword(keyword));
    }

//    query weekly post count sort by date  queryWeekly());
@GetMapping("weekly")
public ResponseEntity<List<Map<String, Object>>> queryWeekly() {
    List<Map<String, Object>> weeklyPostCounts = postsService.queryWeekly();
    return ResponseEntity.ok(weeklyPostCounts);
}

//addViews
@PostMapping("/addviews/{id}")
public ResponseEntity<Posts> addViews(@PathVariable Integer id) {
    return ResponseEntity.ok(this.postsService.addViews(id));
}

//set top
@PostMapping("/settoptoggle/{id}")
public ResponseEntity<Integer> setTop(@PathVariable Integer id) {
    return ResponseEntity.ok(this.postsService.setTop(id));
}
//get all top posts
@GetMapping("/top")
public ResponseEntity<List<Posts>> getTopPosts() {
    return ResponseEntity.ok(this.postsService.getTopPosts());
}
}

