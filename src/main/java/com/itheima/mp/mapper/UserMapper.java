package com.itheima.mp.mapper;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.mp.domain.po.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

//继承的泛型就是你要对哪个实体类操作，就写哪个类作为泛型
public interface UserMapper extends BaseMapper<User> {
    /**
     * 自定义SQL语句查询
     * @param wrapper
     * @param amount
     */
    void updateBalanceById(@Param("ew") LambdaUpdateWrapper<User> wrapper, @Param("amount") int amount);

    /**
     * 根据id扣除用户余额
     * @param id
     * @param money
     */
    void deductBalance(@Param("id") Long id,@Param("money") Integer money);

    // void saveUser(User user);
    //
    // void deleteUser(Long id);
    //
    // void updateUser(User user);
    //
    // User queryUserById(@Param("id") Long id);
    //
    // List<User> queryUserByIds(@Param("ids") List<Long> ids);
}
