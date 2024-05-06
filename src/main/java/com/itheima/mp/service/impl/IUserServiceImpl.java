package com.itheima.mp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.mapper.UserMapper;
import com.itheima.mp.query.UserQuery;
import com.itheima.mp.service.IUserService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Autor：林建威
 * @DateTime：2024/5/6 15:20
 **/

@Service
public class IUserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    /**
     * 根据id扣减用户余额,及复杂更新
     * @param id
     * @param money
     */
    @Override
    public void deductBalance(Long id, Integer money) {
        //1.查询用户
        User user = getById(id);
        //2.校验用户状态是否正常
        if (user == null || user.getStatus() == 2) {
            throw new RuntimeException("用户状态异常");
        }

        //3.判断余额是否盈余
        if (user.getBalance() < money) {
            throw new RuntimeException("用户余额不足!");
        }
        //4.扣除余额 update user set balance = balance - #{money} where id = #{id}
        // baseMapper.deductBalance(id, money);
        int reminder = user.getBalance() - money;
        lambdaUpdate()
                .set(User::getBalance, reminder) //set balance = balance - #{money},
                .set(reminder == 0, User::getStatus, 2) // <if test = "reminder == 0"> status = 2</if>
                .set(User::getId, id) // where id = #{id}
                .eq(User::getBalance,user.getBalance()) //乐观锁
                .update(); //执行更新语句
    }

    /**
     * 根据复杂条件查询用户
     * @param userQuery
     * @return
     */
    @Override
    public List<User> queryUsers(UserQuery userQuery) {
        return lambdaQuery()
                .like(userQuery.getName() != null, User::getUsername, userQuery.getName())
                .eq(userQuery.getStatus() != null, User::getStatus, userQuery.getStatus())
                .ge(userQuery.getMinBalance() != null, User::getBalance, userQuery.getMinBalance())
                .lt(userQuery.getMaxBalance() != null, User::getBalance, userQuery.getMaxBalance())
                .list();
    }
}
