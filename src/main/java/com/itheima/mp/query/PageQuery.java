package com.itheima.mp.query;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.mp.domain.po.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PageQuery {
    @ApiModelProperty("页面")
    private Integer pageNo = 1;
    @ApiModelProperty("页面显示条数")
    private Integer pageSize = 5;
    @ApiModelProperty("排序字段")
    private String sortBy;
    @ApiModelProperty("是否升序")
    private Boolean isAsc;

    /**
     * mp的分页插件手写工具类
     * @param orderItems 用户传递进来的多个排序参数
     * @param <T> 泛型，后面根据用户实际使用用途自定义
     * @return
     */
    public <T> Page<T> toMpPage(OrderItem... orderItems) {
        //1. 构造分页条件
        Page<T> page = Page.of(pageNo, pageSize);
        //2. 设置排序字段
        if (sortBy != null) {
            //排序字段不为空
            page.addOrder(new OrderItem(sortBy, isAsc));
        }else {
            //为空，则已id为升序
            page.addOrder(orderItems);
        }
        //3.返回分页结果
        return page;
    }

    //同时可以写成根据用户自己定义的排序方法
    public <T> Page<T> toMpPageOrder(String orderByItem, boolean defalutAsc) {
        //返回调用自己的结果
        return toMpPage(new OrderItem(orderByItem, defalutAsc));
    }

    //设置默认由创建时间进行排序
    public <T> Page<T> toMpPageOrderByCreateTime(OrderItem... orderItems) {
        //返回调用自己的结果
        return toMpPage(new OrderItem("create_time", false));
    }
    //设置默认由更新时间进行排序
    public <T> Page<T> toMpPageOrderByUpdateTime(OrderItem... orderItems) {
        //返回调用自己的结果
        return toMpPage(new OrderItem("update_time", false));
    }
}
