package com.lm.lawmaster.service.impl;

import com.lm.lawmaster.entity.Users;
import com.lm.lawmaster.dao.UsersDao;
import com.lm.lawmaster.service.UsersService;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import javax.annotation.Resource;

/**
 * (Users)表服务实现类
 *
 * @author makejava
 */
@Service("usersService")
public class UsersServiceImpl implements UsersService {
    @Autowired
    private UsersDao usersDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public Users queryById(Integer id) {
        return this.usersDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param users       筛选条件
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @Override
    public Page<Users> queryByPage(Users users, PageRequest pageRequest) {
        long total = this.usersDao.count(users);
        return new PageImpl<>(this.usersDao.queryAllByLimit(users, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param users 实例对象
     * @return 实例对象
     */
    @Override
    public Users insert(Users users) {
        this.usersDao.insert(users);
        return users;
    }

    /**
     * 修改数据
     *
     * @param users 实例对象
     * @return 实例对象
     */
    @Override
    public Users update(Users users) {
        boolean a=this.usersDao.update(users);
        return this.queryById(users.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.usersDao.deleteById(id) > 0;
    }


    @Override
    public Users findByUsername(String username) {
        return usersDao.findByUsername(username);
    }
//    getAllUsersCount
    @Override
    public int getAllUsersCount() {
        return usersDao.getAllUsersCount();
    }

//    get allUsers
    @Override
    public List<Users> getAllUsers() {
        return usersDao.getAllUsers();
    }

//    queryByRole
    @Override
    public List<Users> queryByRole(String role) {
        return usersDao.queryByRole(role);
    }
//    queryByUsername
    @Override
    public List<Users> queryByUsername(String username) {
        return usersDao.queryByUsername(username);

    }
    //    query weekly user count sort by date  queryWeekly()
    public List<Map<String, Object>> queryWeekly() {
        // 获取当前日期，并计算过去 7 天的日期
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(6); // 7 天前的日期（含今天）

        // 查询数据库获取已存在的发帖统计数据
        List<Map<String, Object>> userCounts = usersDao.findWeeklyUserCount();

        // 创建一个日期 -> 统计数的映射
        Map<LocalDate, Long> userCountMap = new HashMap<>();
        for (Map<String, Object> entry : userCounts) {
            LocalDate date = LocalDate.parse(entry.get("userDate").toString()); // 解析日期
            Long count = ((Number) entry.get("userCount")).longValue(); // 解析发帖数
            userCountMap.put(date, count);
        }

        // 生成完整的 7 天数据
        List<Map<String, Object>> result = new ArrayList<>();
        for (LocalDate date = startDate; !date.isAfter(today); date = date.plusDays(1)) {
            Map<String, Object> data = new HashMap<>();
            data.put("userDate", date.toString());
            data.put("userCount", userCountMap.getOrDefault(date, 0L)); // 如果没有数据，默认 0
            result.add(data);
        }

        return result;
}


    /**
     * sample:
     *OkHttpClient client = new OkHttpClient().newBuilder()
     *   .build();
     * MediaType mediaType = MediaType.parse("application/json");
     * RequestBody body = RequestBody.create(mediaType, "{\n  \"messages\": [\n    {\n      \"content\": \"You are a helpful assistant\",\n      \"role\": \"system\"\n    },\n    {\n      \"content\": \"Hi\",\n      \"role\": \"user\"\n    }\n  ],\n  \"model\": \"deepseek-chat\",\n  \"frequency_penalty\": 0,\n  \"max_tokens\": 2048,\n  \"presence_penalty\": 0,\n  \"response_format\": {\n    \"type\": \"text\"\n  },\n  \"stop\": null,\n  \"stream\": false,\n  \"stream_options\": null,\n  \"temperature\": 1,\n  \"top_p\": 1,\n  \"tools\": null,\n  \"tool_choice\": \"none\",\n  \"logprobs\": false,\n  \"top_logprobs\": null\n}");
     * Request request = new Request.Builder()
     *   .url("https://api.deepseek.com/chat/completions")
     *   .method("POST", body)
     *   .addHeader("Content-Type", "application/json")
     *   .addHeader("Accept", "application/json")
     *   .addHeader("Authorization", "Bearer <TOKEN>")
     *   .build();
     * Response response = client.newCall(request).execute();
     **/
    //        String askAi(String question) use okhttpClient to call the AI model and get the answer
    public  String askAi(String question) {
        // 调用 AI 模型，获取回答
        try {
        String answer = "AI回答：" + question;
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\n  \"messages\": [\n    {\n      \"content\":罚款是什么意思 \"\",\n      \"role\": \"system\"\n    },\n    {\n      \"content\": \"Hi\",\n      \"role\": \"user\"\n    }\n  ],\n  \"model\": \"deepseek-chat\",\n  \"frequency_penalty\": 0,\n  \"max_tokens\": 2048,\n  \"presence_penalty\": 0,\n  \"response_format\": {\n    \"type\": \"text\"\n  },\n  \"stop\": null,\n  \"stream\": false,\n  \"stream_options\": null,\n  \"temperature\": 1,\n  \"top_p\": 1,\n  \"tools\": null,\n  \"tool_choice\": \"none\",\n  \"logprobs\": false,\n  \"top_logprobs\": null\n}");
        Request request = new Request.Builder()
                .url("https://api.deepseek.com/chat/completions")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "Bearer sk-0b4f6ee35d5343e2a6c8a60a9ede3cba")
                .build();

            Response response = client.newCall(request).execute();
            return response.message();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

//    findByPnumber
    @Override
    public Users findByPnumber(String pnumber) {
        return usersDao.findByPnumber(pnumber);
    }

//  boolean  changePassword(request.get("userid"), request.get("oldPassword"), request.get("newPassword"))
    @Override
    public boolean changePassword(String userid, String oldPassword, String newPassword) {
//        convert string to int
        int useridInt = Integer.parseInt(userid);
//        check if userid and password match
        if (usersDao.queryById(useridInt).getPassword().equals(oldPassword)) {
            return usersDao.changePassword(useridInt, oldPassword, newPassword);
        }
        return false;
}
//String getNicknameById(Integer id);
    @Override
    public Map<String,String> getUserInfoById(Integer id) {
        String nickname = usersDao.queryById(id).getNickname();
        String avatar = usersDao.queryById(id).getAvatar();
        Map<String,String> userInfo = new HashMap<>();
        userInfo.put("nickname",nickname);
        userInfo.put("avatar",avatar);
        return userInfo;
    }


}
