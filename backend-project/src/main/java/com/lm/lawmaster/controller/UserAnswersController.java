package com.lm.lawmaster.controller;

import com.lm.lawmaster.entity.UserAnswers;
import com.lm.lawmaster.service.UserAnswersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * (UserAnswers)表控制层
 *
 * @author makejava
 * @since 2025-03-08 00:49:12
 */
@RestController
@RequestMapping("userAnswers")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserAnswersController {
    /**
     * 服务对象
     */
    @Autowired
    private UserAnswersService userAnswersService;

    /**
     * 分页查询
     *
     * @param userAnswers 筛选条件
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @GetMapping
    public ResponseEntity<Page<UserAnswers>> queryByPage(UserAnswers userAnswers, PageRequest pageRequest) {
        return ResponseEntity.ok(this.userAnswersService.queryByPage(userAnswers, pageRequest));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public ResponseEntity<UserAnswers> queryById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(this.userAnswersService.queryById(id));
    }

    /**
     * 新增数据
     *
     * @param userAnswers 实体
     * @return 新增结果
     */
    @PostMapping
    public ResponseEntity<UserAnswers> add(UserAnswers userAnswers) {
        return ResponseEntity.ok(this.userAnswersService.insert(userAnswers));
    }

    /**
     * 编辑数据
     *
     * @param userAnswers 实体
     * @return 编辑结果
     */
    @PutMapping
    public ResponseEntity<UserAnswers> edit(UserAnswers userAnswers) {
        return ResponseEntity.ok(this.userAnswersService.update(userAnswers));
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @DeleteMapping
    public ResponseEntity<Boolean> deleteById(Integer id) {
        return ResponseEntity.ok(this.userAnswersService.deleteById(id));
    }

//    save list of user answers
    @PostMapping("list")
    public ResponseEntity<Boolean> saveList(@RequestBody List<UserAnswers> userAnswers) {
        return ResponseEntity.ok(this.userAnswersService.saveUserAnswers(userAnswers));
    }

//    query all incorrect answers
    @GetMapping("incorrect/{userId}")
    public ResponseEntity<List<UserAnswers>> queryIncorrectAnswers(@PathVariable("userId") Integer userId) {
        return ResponseEntity.ok(this.userAnswersService.queryIncorrectAnswers(userId));
    }

//    queryDistinctAnswers
    @GetMapping("distinct/{userId}")
    public ResponseEntity<List<UserAnswers>> queryDistinctAnswers(@PathVariable("userId") Integer userId) {
        return ResponseEntity.ok(this.userAnswersService.queryDistinctAnswers(userId));
    }
}

