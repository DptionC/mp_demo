package com.itheima.mp.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.itheima.mp.domain.po.Address;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.vo.AddressVO;
import com.itheima.mp.domain.vo.UserVO;
import com.itheima.mp.mapper.UserMapper;
import com.itheima.mp.query.UserQuery;
import com.itheima.mp.service.IUserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    /**
     * 根据用户id查询，并查询相应的地址信息
     * @param id
     * @return
     */
    @Override
    public UserVO queryUserAndAddressesBy(Long id) {
        //1.根据id查询用户信息
        User user = getById(id);
        if (user == null) {
            throw new RuntimeException("用户不存在或用户异常！");
        }
        //2.并查询相应地址信息,告诉lambda需要查找的是address的实体类
        List<Address> addresses = Db.lambdaQuery(Address.class)
                .eq(Address::getUserId, id).list();
        //3.封装VO
        //3.1 转User的PO为VO
        UserVO userVO = BeanUtil.copyProperties(user, UserVO.class);
        //3.2 转Address的PO为VO
        if (CollUtil.isNotEmpty(addresses)) {
            //如果列表为空，就不需要转换了
            List<AddressVO> addressVOS = BeanUtil.copyToList(addresses, AddressVO.class);
            userVO.setAddresses(addressVOS);
        }
        return userVO;
    }

    /**
     * 根据id批量查询用户,并返回地址信息
     * @param ids
     * @return
     */
    @Override
    public List<UserVO> queryUserAndAddressByIds(List<Long> ids) {
        //1.查询用户
        List<User> userList = listByIds(ids);
        if (CollUtil.isEmpty(userList)) {
            throw new RuntimeException("部分用户不存在或用户异常!");
        }
        //这样写性能较差
        /*List<UserVO> userVOList = new ArrayList<>();
        for (User user : userList) {

            //2.查询地址
            List<Address> addresses = Db.lambdaQuery(Address.class)
                    .eq(Address::getUserId, user.getId())
                    .list();
            //3.封装结果
            UserVO userVO = BeanUtil.copyProperties(user, UserVO.class);
            List<AddressVO> addressVOS = BeanUtil.copyToList(addresses, AddressVO.class);
            userVO.setAddresses(addressVOS);
            userVOList.add(userVO);
        }*/
        //性能较好写法
        //2.查询地址
        List<Long> userIds = userList.stream().map(User::getId).collect(Collectors.toList());
        //2.2 根据用户id查询地址
        List<Address> addresses = Db.lambdaQuery(Address.class)
                .in(Address::getUserId, userIds).list();
        //2.3 转换地址VO
        List<AddressVO> addressVOList = BeanUtil.copyToList(addresses, AddressVO.class);
        //2.4 手动分组,Long为用户id,List<AddressVO>用户地址
        Map<Long, List<AddressVO>> addressMap = new HashMap<>(0);
        if (CollUtil.isNotEmpty(addresses)) {
            //一次性获取所有地址信息,并根据id进行分组
             addressMap = addressVOList.stream().collect(Collectors.groupingBy(AddressVO::getUserId));
        }

        //3.封装VO返回
        List<UserVO> userVOS = new ArrayList<>(userIds.size());
        for (User user : userList) {
            UserVO userVO = BeanUtil.copyProperties(user, UserVO.class);
            userVOS.add(userVO);
            //3.2 转换地址vo
            userVO.setAddresses(addressMap.get(user.getId()));
        }


        return userVOS;
    }

    /**
     * 根据用户id查询收获地址功能
     * @param id
     * @return
     */
    @Override
    public List<AddressVO> queryAddressesById(Long id) {
        //1.查询用户
        User user = getById(id);
        if (user == null || user.getStatus().equals(2)) {
            throw new RuntimeException("用户不存在或用户状态异常！");
        }
        //2.查询地址
        List<Address> addresses = Db.lambdaQuery(Address.class)
                .eq(Address::getUserId, id)
                .list();
        //3.封装成VO
        List<AddressVO> addressVOS = BeanUtil.copyToList(addresses, AddressVO.class);
        return addressVOS;
    }
}
