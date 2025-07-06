package com.lm.lawmaster.controller;

import com.lm.lawmaster.entity.Collects;
import com.lm.lawmaster.entity.Favorites;
import com.lm.lawmaster.service.FavoritesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//import javax.annotation.Resource;

/**
 * (Favorites)表控制层
 *
 * @author makejava
 */
@RestController
@RequestMapping("favorites")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class FavoritesController {
    /**
     * 服务对象
     */
    @Autowired
    private FavoritesService favoritesService;

    /**
     * 分页查询
     *
     * @param favorites   筛选条件
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @GetMapping
    public ResponseEntity<Page<Favorites>> queryByPage(Favorites favorites, PageRequest pageRequest) {
        return ResponseEntity.ok(this.favoritesService.queryByPage(favorites, pageRequest));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public ResponseEntity<Favorites> queryById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(this.favoritesService.queryById(id));
    }

    /**
     * 新增数据
     *
     * @param favorites 实体
     * @return 新增结果
     */
    @PostMapping
    public ResponseEntity<Favorites> add(@RequestBody Favorites favorites) {
        return ResponseEntity.ok(this.favoritesService.insert(favorites));
    }

    /**
     * 编辑数据
     *
     * @param favorites 实体
     * @return 编辑结果
     */
    @PutMapping
    public ResponseEntity<Favorites> edit(@RequestBody Favorites favorites) {
        return ResponseEntity.ok(this.favoritesService.update(favorites));
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @DeleteMapping
    public ResponseEntity<Boolean> deleteById(Integer id) {
        return ResponseEntity.ok(this.favoritesService.deleteById(id));
    }

    //    query if userid exists in collects.if exists delete it, else add it
    @PostMapping("/togglefavorites")
    public ResponseEntity<Boolean> toggleFavorites(@RequestBody Favorites favorites) {
        return ResponseEntity.ok(this.favoritesService.toggleFavorites(favorites));
    }

    //    query by userid and postid
    @GetMapping("/queryByUserIdAndPostId")
    public ResponseEntity<Favorites> queryByUserIdAndPostId(@RequestParam Integer userId, @RequestParam Integer postId) {
    return ResponseEntity.ok(this.favoritesService.queryByUserIdAndPostId(userId, postId));
    }

    //    queryAllByUserId method
 @GetMapping("/queryAllByUserId")
    public ResponseEntity<List<Integer>> queryAllByUserId(@RequestParam Integer userId) {

     //        get all postid from lists
        List<Favorites> lists = this.favoritesService.queryAllByUserId(userId);
     List<Integer> postIds = lists.stream().map(Favorites::getPostId).toList();
     return ResponseEntity.ok(postIds);
 }

}

