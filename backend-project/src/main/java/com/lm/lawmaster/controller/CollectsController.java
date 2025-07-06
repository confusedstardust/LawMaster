package com.lm.lawmaster.controller;

import com.lm.lawmaster.entity.Collects;
import com.lm.lawmaster.entity.Favorites;
import com.lm.lawmaster.service.CollectsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


/**
 * (Collects)表控制层
 *
 * @author makejava
 * @since 2025-03-01 16:23:24
 */
@RestController
@RequestMapping("collects")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CollectsController {
    /**
     * 服务对象
     */
    @Autowired
    private CollectsService collectsService;

    /**
     * 分页查询
     *
     * @param collects    筛选条件
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @GetMapping
    public ResponseEntity<Page<Collects>> queryByPage(Collects collects, PageRequest pageRequest) {
        return ResponseEntity.ok(this.collectsService.queryByPage(collects, pageRequest));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public ResponseEntity<Collects> queryById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(this.collectsService.queryById(id));
    }

    /**
     * 新增数据
     *
     * @param collects 实体
     * @return 新增结果
     */
    @PostMapping
    public ResponseEntity<Collects> add(Collects collects) {
        return ResponseEntity.ok(this.collectsService.insert(collects));
    }


//    query if userid exists in collects.if exists delete it, else add it
    @PostMapping("/togglecollect")
    public ResponseEntity<Boolean> toggleCollect(@RequestBody Collects collects) {
        return ResponseEntity.ok(this.collectsService.toggleCollect(collects));
    }

    /**
     * 编辑数据
     *
     * @param collects 实体
     * @return 编辑结果
     */
    @PutMapping
    public ResponseEntity<Collects> edit(Collects collects) {
        return ResponseEntity.ok(this.collectsService.update(collects));
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @DeleteMapping
    public ResponseEntity<Boolean> deleteById(Integer id) {
        return ResponseEntity.ok(this.collectsService.deleteById(id));
    }


//    query by userid and postid
    @GetMapping("/queryByUserIdAndPostId")
    public ResponseEntity<Collects> queryByUserIdAndPostId(@RequestParam Integer userId,@RequestParam Integer postId) {
        return ResponseEntity.ok(this.collectsService.queryByUserIdAndPostId(userId, postId));
    }

    //    queryAllByUserId method
    @GetMapping("/queryAllByUserId")
    public ResponseEntity<List<Integer>> queryAllByUserId(@RequestParam Integer userId) {
        List<Collects>lists=this.collectsService.queryAllByUserId(userId);
//        get all postid from lists
        List<Integer> postIds = lists.stream().map(Collects::getPostId).collect(Collectors.toList());
        return ResponseEntity.ok(postIds);
    }
}

