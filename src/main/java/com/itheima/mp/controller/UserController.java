package com.itheima.mp.controller;

import cn.hutool.core.bean.BeanUtil;
import com.itheima.mp.domain.dto.UserFormDTO;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.vo.UserVO;
import com.itheima.mp.query.UserQuery;
import com.itheima.mp.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @Autor：林建威
 * @DateTime：2024/5/6 15:41
 **/

@RestController
@RequestMapping("/users")
@Slf4j
@Api(tags = "用户管理接口")
@RequiredArgsConstructor  //必备的构造函数，只对需要在一开始初始化的对象进行构造
public class UserController {

    //表示一个常量，必须在初始化过程中完成对对象的初始化。
    private final IUserService userService;

    @PostMapping
    @ApiOperation("新增用户接口")
    public void saveUser(@RequestBody UserFormDTO userFormDTO) {
        //1.数据拷贝
        User user = new User();
        BeanUtils.copyProperties(userFormDTO, user);
        //2.插入数据库
        userService.save(user);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除用户")
    public void deleteUser(@PathVariable Long id) {
        //1.根据id删除用户信息
        userService.removeById(id);
    }

    @GetMapping("/{id}")
    @ApiOperation("根据id查询用户")
    public UserVO queryUserById(@PathVariable Long id) {
        User user = userService.getById(id);
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user,userVO);
        return userVO;
    }

    @GetMapping
    @ApiOperation("根据id批量查询用户")
    public List<UserVO> queryUserByIds(@RequestParam List<Long> ids) {
        List<User> userList = userService.listByIds(ids);
        List<UserVO> userVOList = new ArrayList<>();
        for (User user : userList) {
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO);
            userVOList.add(userVO);
        }
        return userVOList;
    }

    @PutMapping("/user/{id}/deduction/{money}")
    @ApiOperation("根据id扣减余额接口")
    public void deductBalance(@PathVariable Long id, @PathVariable Integer money) {
        userService.deductBalance(id, money);
    }

    @GetMapping("/list")
    @ApiOperation("根据复杂查询用户")
    public List<UserVO> queryUsers(UserQuery userQuery) {
        List<User> users = userService.queryUsers(userQuery);
        //把po拷贝到vo
        return BeanUtil.copyToList(users,UserVO.class);

    }
}
