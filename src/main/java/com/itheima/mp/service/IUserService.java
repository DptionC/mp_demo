package com.itheima.mp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.query.UserQuery;

import java.util.List;

/**
 * @Autor：林建威
 * @DateTime：2024/5/6 15:19
 **/
public interface IUserService extends IService<User> {

    /**
     * 根据id扣减用户余额
     * @param id
     * @param money
     */
    void deductBalance(Long id, Integer money);

    /**
     * 根据复杂条件查询用户
     * @param userQuery
     * @return
     */
    List<User> queryUsers(UserQuery userQuery);
}
