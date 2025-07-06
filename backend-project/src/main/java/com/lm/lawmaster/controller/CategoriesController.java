package com.lm.lawmaster.controller;

import com.lm.lawmaster.entity.Categories;
import com.lm.lawmaster.service.CategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * (Categories)表控制层
 *
 * @author makejava
 * @since 2025-02-26 23:50:49
 */
@RestController
@RequestMapping("categories")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CategoriesController {
    /**
     * 服务对象
     */
    @Autowired
    private CategoriesService categoriesService;

    /**
     * 分页查询
     *
     * @param categories  筛选条件
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @GetMapping
    public ResponseEntity<Page<Categories>> queryByPage(Categories categories, PageRequest pageRequest) {
        return ResponseEntity.ok(this.categoriesService.queryByPage(categories, pageRequest));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public ResponseEntity<Categories> queryById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(this.categoriesService.queryById(id));
    }

    /**
     * 新增数据
     *
     * @param categories 实体
     * @return 新增结果
     */
    @PostMapping("add")
    public ResponseEntity<Categories> add(@RequestBody Categories categories) {
        return ResponseEntity.ok(this.categoriesService.insert(categories));
    }

    /**
     * 编辑数据
     *
     * @param categories 实体
     * @return 编辑结果
     */
    @PutMapping
    public ResponseEntity<Categories> edit(Categories categories) {
        return ResponseEntity.ok(this.categoriesService.update(categories));
    }

//    upsert
    @PostMapping("upsert")
    public ResponseEntity<Categories> upsert(@RequestBody Categories categories) {
        return ResponseEntity.ok(this.categoriesService.upsert(categories));
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @PostMapping("delete/{id}")
    public ResponseEntity<Boolean> deleteById(@PathVariable Integer id) {
        return ResponseEntity.ok(this.categoriesService.deleteById(id));
    }

//    get all categories
    @GetMapping("all")
    public ResponseEntity<List<Categories>> queryAll() {
        return ResponseEntity.ok(this.categoriesService.queryAll());
    }

}

