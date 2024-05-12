package com.itheima.mp.mapper;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.po.UserInfo;
import com.itheima.mp.service.IUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private IUserService userService;

    @Test
    void testInsert() {
        User user = new User();
        user.setId(5L);
        user.setUsername("Lucy");
        user.setPassword("123");
        user.setPhone("18688990011");
        user.setBalance(200);
        user.setInfo(UserInfo.of(24, "英语老师", "female"));
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        userMapper.insert(user);
    }

    @Test
    void testSelectById() {
        User user = userMapper.selectById(5L);
        System.out.println("user = " + user);
    }


    @Test
    void testQueryByIds() {
        List<User> users = userMapper.selectBatchIds(List.of(1L, 2L, 3L, 4L));
        users.forEach(System.out::println);
    }

    @Test
    void testUpdateById() {
        User user = new User();
        user.setId(5L);
        user.setBalance(20000);
        userMapper.updateById(user);
    }

    @Test
    void testDeleteUser() {
        userMapper.deleteById(5L);
    }

    @Test
    void testQueryWrapper() {
        //1.构建查询条件
        //select id,username,info,balance where username like o% and balance >= 1000
        QueryWrapper<User> wrapper = new QueryWrapper<User>()
                .select("id", "username", "info", "balance")
                .like("username", "o")
                .ge("balance", 1000);
        //2.查询
        List<User> users = userMapper.selectList(wrapper);
        users.forEach(System.out::println);
    }

    @Test
    void testUpdateQueryWrapper() {
        //1.要更新的数据 update user set balance = 2000 where (username = jack)
        User user = new User();
        user.setBalance(2000);

        //2.更新条件
        UpdateWrapper<User> wrapper = new UpdateWrapper<User>()
                .eq("username", "jack"); // username = jack

        //3.执行更新
        userMapper.update(user, wrapper);
    }

    @Test
    void testUpdateWrapper() {
        //update user set balance = balance -200 where id in (1,2,4)
        List<Long> ids = List.of(1L, 2L, 4L);
        UpdateWrapper<User> wrapper = new UpdateWrapper<User>()
                .setSql("balance = balance - 200")
                .in("id", ids);
        userMapper.update(null, wrapper);
    }

    @Test
    void testUpdateBalance() {
        List<Long> ids = List.of(1L, 2L, 4L);
        int amount = 200;
        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<User>().in(User::getId, ids);
        //2.自定义SQL方法调用
        userMapper.updateBalanceById(wrapper, amount);
    }

    @Test
    void testPageQuery() {
        //模拟前端传参,当前页,和每次查询2条
        int pageNo = 1, pageSize = 2;
        //1.准备分页条件
        //1.1 分页条件
        Page<User> page = Page.of(pageNo, pageSize);
        //1.2 分页条件 默认是升序,true是降序,可以写多个
        page.addOrder(new OrderItem("balance", true));
        //如果balance相同,则按id排序
        page.addOrder(new OrderItem("id", true));
        //2.分页查询
        Page<User> userPage = userService.page(page);

        //3.解析
        //3.1 获取分页总记录数
        long total = userPage.getTotal();
        System.out.println("total = " + total);
        //3.2 获取总页数
        long pages = userPage.getPages();
        System.out.println("pages" + pages);
        //3.3 获取记录数据列表
        List<User> records = userPage.getRecords();
        records.forEach(System.out::println);

    }
}