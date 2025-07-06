package com.lm.lawmaster.controller;

import com.lm.lawmaster.entity.Questions;
import com.lm.lawmaster.service.QuestionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * (Questions)表控制层
 *
 * @author makejava
 * @since 2025-02-26 23:51:29
 */
@RestController
@RequestMapping("questions")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class QuestionsController {
    /**
     * 服务对象
     */
    @Autowired
    private QuestionsService questionsService;

    /**
     * 分页查询
     *
     * @param questions   筛选条件
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @GetMapping
    public ResponseEntity<Page<Questions>> queryByPage(Questions questions, PageRequest pageRequest) {
        return ResponseEntity.ok(this.questionsService.queryByPage(questions, pageRequest));
    }

//    get question by categoryid
    @GetMapping("/category/{id}")
    public ResponseEntity<List<Questions>> queryByCategoryId(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(this.questionsService.queryByCategoryId(id));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public ResponseEntity<Questions> queryById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(this.questionsService.queryById(id));
    }

//    query question by id array
    @GetMapping("/array")
    public ResponseEntity<List<Questions>> QueryByIdArray(@RequestParam("ids") List<Integer> ids) {
        return ResponseEntity.ok(this.questionsService.queryByIdArray(ids));
    }

    /**
     * 新增数据
     *
     * @param questions 实体
     * @return 新增结果
     */
    @PostMapping
    public ResponseEntity<Questions> add(@RequestBody Questions questions) {
        return ResponseEntity.ok(this.questionsService.insert(questions));
    }

    /**
     * 编辑数据
     *
     * @param questions 实体
     * @return 编辑结果
     */
    @PostMapping("edit")
    public ResponseEntity<Questions> edit(@RequestBody Questions questions) {
        return ResponseEntity.ok(this.questionsService.update(questions));
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @PostMapping("delete/{id}")
    public ResponseEntity<Boolean> deleteById(@PathVariable Integer id) {
        return ResponseEntity.ok(this.questionsService.deleteById(id));
    }

//  query all questions
    @GetMapping("all")
    public ResponseEntity<List<Questions>> queryAll() {
        List<Questions>a=this.questionsService.queryAll();
        System.out.println("query all questions");
        return ResponseEntity.ok(a);
    }
// query by content keyword
    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<Questions>> searchQuestions(@PathVariable("keyword") String keyword) {
        return ResponseEntity.ok(this.questionsService.searchQuestions(keyword));
    }

//    query question count by category
    @GetMapping("/countByCategory")
    public ResponseEntity<List<Map<String, Object>>> countByCategory() {
        return ResponseEntity.ok(this.questionsService.countByCategory());

    }

}

