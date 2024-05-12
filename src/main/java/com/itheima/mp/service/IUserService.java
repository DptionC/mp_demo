package com.itheima.mp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.mp.domain.dto.PageDTO;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.vo.AddressVO;
import com.itheima.mp.domain.vo.UserVO;
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

    /**
     * 根据用户id查询，并获取相应的地址信息
     * @param id
     * @return
     */
    UserVO queryUserAndAddressesBy(Long id);

    /**
     * 根据用户id查询收获地址功能
     * @param id
     * @return
     */
    List<AddressVO> queryAddressesById(Long id);

    /**
     * 根据id批量查询用户信息,及返回地址信息
     * @param ids
     * @return
     */
    List<UserVO> queryUserAndAddressByIds(List<Long> ids);

    /**
     * 分页查询
     * @param query
     * @return
     */
    PageDTO<UserVO> queryUsersPage(UserQuery query);
}
