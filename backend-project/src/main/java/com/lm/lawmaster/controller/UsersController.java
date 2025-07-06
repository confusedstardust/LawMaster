package com.lm.lawmaster.controller;

import com.lm.lawmaster.entity.Users;
import com.lm.lawmaster.service.UsersService;
import com.lm.lawmaster.util.HttpUtils;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

//import javax.annotation.Resource;

/**
 * (Users)表控制层
 *
 * @author makejava
 */
@RestController
@RequestMapping("users")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UsersController {
  /**
     * 服务对象
     */
    @Autowired
    private UsersService usersService;

    /**
     * 分页查询
     *
     * @param users       筛选条件
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @GetMapping
    public ResponseEntity<Page<Users>> queryByPage(Users users, PageRequest pageRequest) {
        return ResponseEntity.ok(this.usersService.queryByPage(users, pageRequest));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public ResponseEntity<Users> queryById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(this.usersService.queryById(id));
    }



    @PostMapping("/login")
    public ResponseEntity<Users> login(@RequestBody Users user) {
        // 查询数据库中是否存在该用户
        Users existingUser = usersService.findByUsername(user.getUsername());
        if (existingUser != null && existingUser.getPassword().equals(user.getPassword())) {
            // 登录成功，返回用户对象
            return ResponseEntity.ok(existingUser);
        } else {
            // 用户名或密码错误
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

//    login by phone


    /**
     * 新增数据
     *
     * @param users 实体
     * @return 新增结果
     */
    @PostMapping
    public ResponseEntity<Users> add(@RequestBody Users users) {
        return ResponseEntity.ok(this.usersService.insert(users));
    }

    /**
     * 编辑数据
     *
     * @param users 实体
     * @return 编辑结果
     */
    @PutMapping
    public ResponseEntity<Users> edit(@RequestBody Users users) {
        return ResponseEntity.ok(this.usersService.update(users));
    }


//    update user info by id
    @PostMapping("/update")
    public ResponseEntity<Users> update(@RequestBody Users users) {
        return ResponseEntity.ok(this.usersService.update(users));
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @PostMapping("/delete/{id}")
    public ResponseEntity<Boolean> deleteById(@RequestBody Integer id) {
        return ResponseEntity.ok(this.usersService.deleteById(id));
    }
//    Register function
    @PostMapping("/register")
    public ResponseEntity<Users> register(@RequestBody Users user) {
        // 查询数据库中是否存在该用户
        Users existingUser = usersService.findByUsername(user.getUsername());
        if (existingUser != null) {
            // 用户名已存在
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        } else {
            // 注册成功，返回用户对象
            return ResponseEntity.ok(usersService.insert(user));
        }
    }

//    send verification code
    @PostMapping("/sendcode/{pnumber}")
    public ResponseEntity<Boolean> sendCode(@PathVariable String pnumber) {
//        getuserbypnumber
        Users user = usersService.findByPnumber(pnumber);
        if (user != null) {
            // send verification code
            String code = String.valueOf(new Random().nextInt(900000) + 100000);
            HttpUtils.smsService(pnumber, code);
            user.setVerifycode(code);
            usersService.update(user);
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.ok(false);
    }
//    forget password
    @PostMapping("/sendforgetpasswordcode/{pnumber}")
    public ResponseEntity<Boolean> forgetPassword(@PathVariable String pnumber) {
//        get user
        Users user = usersService.findByPnumber(pnumber);
        if (user != null) {
            String code = String.valueOf(new Random().nextInt(900000) + 100000);
            HttpUtils.smsService(pnumber, code);
            user.setVerifycode(code);
            usersService.update(user);
            return ResponseEntity.ok(true);
        }else {
            return ResponseEntity.ok(false);
        }

    }

//    reset password
    @PostMapping("/resetpassword")
    public ResponseEntity<Boolean> resetPassword(@RequestBody Map<String, String> body) {
        String pnumber = body.get("pnumber");
        String password = body.get("password");
        String code = body.get("code");
        Users user = usersService.findByPnumber(pnumber);
        if (user != null&&user.getVerifycode().equals(code)) {
            user.setPassword(password);
            usersService.update(user);
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.ok(false);
    }

//    login with verification code
    @PostMapping("/loginbycode")
    public ResponseEntity<Users> loginByCode(@RequestBody Object body) {
        String pnumber = (String) ((Map<String, String>) body).get("pnumber");
        String verifycode = (String) ((Map<String, String>) body).get("verifycode");
        Users existingUser = usersService.findByPnumber(pnumber);
        if (existingUser != null && existingUser.getVerifycode().equals(verifycode)) {
            return ResponseEntity.ok(existingUser);
        }
        else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

//    query all user accounts
    @GetMapping("/allcount")
    public ResponseEntity<Integer> getAllUsersCount() {
        return ResponseEntity.ok(usersService.getAllUsersCount());

    }

@GetMapping("/allusers")
    public ResponseEntity<List<Users>> getAllUsers() {
        return ResponseEntity.ok(usersService.getAllUsers());
}
//query by role
    @GetMapping("/role/{role}")
    public ResponseEntity<List<Users>> queryByRole(@PathVariable("role") String role) {
        return ResponseEntity.ok(this.usersService.queryByRole(role));
    }

//    query by username
 @GetMapping("/username/{username}")
    public ResponseEntity<List<Users>> queryByUsername(@PathVariable("username") String username) {
        return ResponseEntity.ok(this.usersService.queryByUsername(username));

 }

    //    query weekly user count sort by date  queryWeekly());
    @GetMapping("weekly")
    public ResponseEntity<List<Map<String, Object>>> queryWeekly() {
        List<Map<String, Object>> weeklyPostCounts = usersService.queryWeekly();
        return ResponseEntity.ok(weeklyPostCounts);
}
//问ai客服一些问题，客服返回回答
    @PostMapping("/ask")
    public ResponseEntity<String> ask(@RequestBody String question) {
        return ResponseEntity.ok(usersService.askAi(question));
    }

//    change password
    @PostMapping("/changePassword")
    public ResponseEntity<Boolean> changePassword(@RequestBody Map<String, String> request) {
        return ResponseEntity.ok(usersService.changePassword(request.get("userid"), request.get("oldPassword"), request.get("newPassword")));
    }

//    get user nickname and Avatar by id
    @GetMapping("/info/{id}")
    public ResponseEntity<Map<String,String>> getUserInfoById(@PathVariable("id") String id) {
        return ResponseEntity.ok(usersService.getUserInfoById(Integer.parseInt(id)));
    }

    
}

