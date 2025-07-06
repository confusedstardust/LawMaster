package com.lm.lawmaster.controller;

import com.lm.lawmaster.entity.Follows;
import com.lm.lawmaster.service.FollowsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//import javax.annotation.Resource;

/**
 * (Follows)表控制层
 *
 * @author makejava

 */
@RestController
@RequestMapping("follows")
public class FollowsController {
    /**
     * 服务对象
     */
    @Autowired
    private FollowsService followsService;

    /**
     * 分页查询
     *
     * @param follows     筛选条件
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @GetMapping
    public ResponseEntity<Page<Follows>> queryByPage(Follows follows, PageRequest pageRequest) {
        return ResponseEntity.ok(this.followsService.queryByPage(follows, pageRequest));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public ResponseEntity<Follows> queryById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(this.followsService.queryById(id));
    }

    /**
     * 新增数据
     *
     * @param follows 实体
     * @return 新增结果
     */
    @PostMapping
    public ResponseEntity<Follows> add(@RequestBody Follows follows) {
        return ResponseEntity.ok(this.followsService.insert(follows));
    }

    /**
     * 编辑数据
     *
     * @param follows 实体
     * @return 编辑结果
     */
    @PutMapping
    public ResponseEntity<Follows> edit(@RequestBody Follows follows) {
        return ResponseEntity.ok(this.followsService.update(follows));
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @DeleteMapping
    public ResponseEntity<Boolean> deleteById(Integer id) {
        return ResponseEntity.ok(this.followsService.deleteById(id));
    }

}

