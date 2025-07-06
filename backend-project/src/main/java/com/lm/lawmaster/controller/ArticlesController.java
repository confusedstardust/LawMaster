package com.lm.lawmaster.controller;

import com.lm.lawmaster.entity.Articles;
import com.lm.lawmaster.service.ArticlesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;


/**
 * (Articles)表控制层
 *
 * @author makejava
 * @since 2025-03-09 19:22:29
 */
@RestController
@RequestMapping("articles")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ArticlesController {
    /**
     * 服务对象
     */
    @Autowired
    private ArticlesService articlesService;

    /**
     * 分页查询
     *
     * @param articles    筛选条件
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @GetMapping
    public ResponseEntity<Page<Articles>> queryByPage(Articles articles, PageRequest pageRequest) {
        return ResponseEntity.ok(this.articlesService.queryByPage(articles, pageRequest));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public ResponseEntity<Articles> queryById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(this.articlesService.queryById(id));
    }

    /**
     * 新增数据
     *
     * @param articles 实体
     * @return 新增结果
     */
    @PostMapping
    public ResponseEntity<Articles> add(@RequestBody Articles articles) {
        articles.setCreatedAt(new Date());
        articles.setVisible(0);
        articles.setViews(0);
        articles.setLikes(0);
        return ResponseEntity.ok(this.articlesService.insert(articles));
    }

    /**
     * 编辑数据
     *
     * @param articles 实体
     * @return 编辑结果
     */
    @PostMapping("/edit")
    public ResponseEntity<Articles> edit(@RequestBody Articles articles) {
        return ResponseEntity.ok(this.articlesService.update(articles));
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @PostMapping("/delete/{id}")
    public ResponseEntity<Boolean> deleteById(@PathVariable Integer id) {
        return ResponseEntity.ok(this.articlesService.deleteById(id));
    }

//    query all articles
    @GetMapping("/all")
    public ResponseEntity<List<Articles>> queryAllArticles() {
        return ResponseEntity.ok(this.articlesService.queryAllArticles());
    }
//    search articles
    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<Articles>> searchArticles(@PathVariable("keyword") String keyword) {
        return ResponseEntity.ok(this.articlesService.searchArticles(keyword));
    }

//    set articles to visible
    @PostMapping("/visible/{id}/{flag}")
    public ResponseEntity<Boolean> setVisible(@PathVariable Integer id,@PathVariable boolean flag) {
        return ResponseEntity.ok(this.articlesService.setVisible(id,flag));
    }
//    query articles visible eq 1
@GetMapping("/visible")
    public ResponseEntity<List<Articles>> queryVisible() {
        return ResponseEntity.ok(this.articlesService.queryVisible());
}
//add article views
    @PostMapping("/addviews/{id}")
    public ResponseEntity<Articles> addViews(@PathVariable Integer id) {
        return ResponseEntity.ok(this.articlesService.addViews(id));
    }

}

