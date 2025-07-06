package com.lm.lawmaster.service;

import com.lm.lawmaster.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Map;

/**
 * (Users)表服务接口
 *
 * @author makejava
 */
public interface UsersService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Users queryById(Integer id);

    /**
     * 分页查询
     *
     * @param users       筛选条件
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    Page<Users> queryByPage(Users users, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param users 实例对象
     * @return 实例对象
     */
    Users insert(Users users);

    /**
     * 修改数据
     *
     * @param users 实例对象
     * @return 实例对象
     */
    Users update(Users users);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);


// 根据用户名查找用户
    Users findByUsername(String username);

//    getAllUsersCount

    int getAllUsersCount();

//    getAllUsers
    List<Users> getAllUsers();

//    queryByRole
    List<Users> queryByRole(String role);
//    queryByUsername
    List<Users> queryByUsername(String username);

//    queryWeekly
    List<Map<String, Object>> queryWeekly();
//    //问ai客服一些问题，客服返回回答
    String askAi(String question);

//    findByPnumber
    Users findByPnumber(String pnumber);

//    changePassword(request.get("userid"), request.get("oldPassword"), request.get("newPassword"))
boolean changePassword(String userid, String oldPassword, String newPassword);

//getNicknameById
Map<String,String> getUserInfoById(Integer id);

}

