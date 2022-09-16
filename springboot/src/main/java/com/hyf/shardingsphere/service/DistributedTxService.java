package com.hyf.shardingsphere.service;

import com.hyf.shardingsphere.entity.Order;
import com.hyf.shardingsphere.entity.User;
import com.hyf.shardingsphere.mapper.OrderMapper;
import com.hyf.shardingsphere.mapper.UserMapper;
import org.apache.shardingsphere.transaction.annotation.ShardingSphereTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.UUID;

/**
 * @author baB_hyf
 * @date 2022/09/16
 */
@Service
public class DistributedTxService {

    @Resource
    private DataSource dataSource;

    @Resource
    private UserMapper userMapper;

    @Resource
    private OrderMapper orderMapper;

    @Transactional
    @ShardingSphereTransactionType(TransactionType.BASE)
    public void invoke() {
        User user = new User();
        user.setName("张三");
        userMapper.insert(user);

        int i = 1 / 0;

        Order order = new Order();
        order.setOrderId((long) UUID.randomUUID().toString().hashCode());
        order.setOrderName(UUID.randomUUID().toString());
        orderMapper.addOrder(order); // 按名称顺序取第一个库
    }
}
