package com.lm.lawmaster.dao;

import com.lm.lawmaster.entity.Users;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * (Users)表数据库访问层
 *
 * @author makejava
 */
public interface UsersDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Users queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param users    查询条件
     * @param pageable 分页对象
     * @return 对象列表
     */
    List<Users> queryAllByLimit(Users users, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param users 查询条件
     * @return 总行数
     */
    long count(Users users);

    /**
     * 新增数据
     *
     * @param users 实例对象
     * @return 影响行数
     */
    int insert(Users users);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<Users> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<Users> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<Users> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<Users> entities);

    /**
     * 修改数据
     *
     * @param users 实例对象
     * @return 影响行数
     */
    boolean update(Users users);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

    /**
     * 通过用户名查询单条数据
     *
     * @param username 用户名
     * @return 单条数据
     */
    Users findByUsername(String username);

//    getAllUsersCount

    int getAllUsersCount();

//    getAllUsers
    List<Users> getAllUsers();

//    queryByRole
    List<Users> queryByRole(String role);
//    queryByUsername
    List<Users> queryByUsername(String username);
//    findWeeklyPostCount
List<Map<String, Object>> findWeeklyUserCount();
//findByPnumber
Users findByPnumber(String pnumber);

//boolean  changePassword(request.get("userid"), request.get("oldPassword"), request.get("newPassword"))
    boolean changePassword(Integer userid, String oldPassword, String newPassword);

}


