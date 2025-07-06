package com.lm.lawmaster.controller;

import com.lm.lawmaster.entity.Comments;
import com.lm.lawmaster.entity.Favorites;
import com.lm.lawmaster.service.CommentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * (Comments)表控制层
 *
 * @author makejava
 * @since 2025-02-26 23:51:03
 */
@RestController
@RequestMapping("comments")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CommentsController {
    /**
     * 服务对象
     */
    @Autowired
    private CommentsService commentsService;
    @Autowired
    private PostsController postsController;

    /**
     * 分页查询
     *
     * @param comments    筛选条件
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @GetMapping
    public ResponseEntity<Page<Comments>> queryByPage(Comments comments, PageRequest pageRequest) {
        return ResponseEntity.ok(this.commentsService.queryByPage(comments, pageRequest));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public ResponseEntity<Comments> queryById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(this.commentsService.queryById(id));
    }

    /**
     * 新增数据
     *
     * @param comments 实体
     * @return 新增结果
     */
    @PostMapping
    public ResponseEntity<Comments> add(@RequestBody Comments comments) {
        return ResponseEntity.ok(this.commentsService.insert(comments));
    }

    /**
     * 编辑数据
     *
     * @param comments 实体
     * @return 编辑结果
     */
    @PutMapping
    public ResponseEntity<Comments> edit(Comments comments) {
        return ResponseEntity.ok(this.commentsService.update(comments));
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @DeleteMapping
    public ResponseEntity<Boolean> deleteById(Integer id) {
        return ResponseEntity.ok(this.commentsService.deleteById(id));
    }
//check useid and delete comment by id
    @PostMapping("delete/{userid}/{id}")
    public ResponseEntity<Boolean> deleteByCommentsId(@PathVariable Integer userid,@PathVariable Integer id) {
        if (this.commentsService.queryById(id).getUserId() == userid) {
            return ResponseEntity.ok(this.commentsService.deleteById(id));
        }
        return ResponseEntity.ok(false);
    }

//    query comments by post id
    @GetMapping("post/{postId}")
    public ResponseEntity<List<Comments>> queryByPostId(@PathVariable("postId") Integer postId) {
        return ResponseEntity.ok(this.commentsService.queryByPostId(postId));
    }

    //    queryAllByUserId method
    @GetMapping("/queryAllByUserId")
    public ResponseEntity<List<Integer>> queryAllByUserId(@RequestParam Integer userId) {

        //        get all postid from lists
        List<Comments> lists = this.commentsService.queryByUserId(userId);
        List<Integer> postIds = lists.stream().map(Comments::getPostId).toList();
        return ResponseEntity.ok(postIds);
    }

}

