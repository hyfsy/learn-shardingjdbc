package com.hyf.shardingsphere.mapper;

import com.hyf.shardingsphere.entity.Order;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author baB_hyf
 * @date 2022/09/16
 */
@Mapper
public interface OrderMapper {

    @Insert("insert into t_order (order_id, order_name) values (#{order.orderId}, #{order.orderName})")
    void addOrder(@Param("order") Order order);

}
