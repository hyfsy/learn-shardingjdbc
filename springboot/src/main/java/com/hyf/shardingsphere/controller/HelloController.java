package com.hyf.shardingsphere.controller;

import com.hyf.shardingsphere.entity.User;
import com.hyf.shardingsphere.mapper.UserMapper;
import com.hyf.shardingsphere.util.GeneUtils;
import com.hyf.shardingsphere.util.SnowflakeUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.List;

/**
 * @author baB_hyf
 * @date 2022/09/14
 */
@RestController
@RequestMapping("user")
public class HelloController {

    @Resource
    private DataSource dataSource;

    @Resource
    private UserMapper userMapper;

    // http://localhost:8082/user/1

    @RequestMapping("{id}")
    public User user(@PathVariable Integer id) {
        return userMapper.getById(id);
    }

    @RequestMapping("add")
    public User addUser(@RequestParam("name") String name) {
        User user = new User();
        user.setName(name);
        userMapper.insert(user);
        return user;
    }

    @RequestMapping("add_gene")
    public User addUserGene(@RequestParam("name") String name) {
        User user = new User();
        // long gene = GeneUtils.gene(name, 4);
        // Long id = SnowflakeUtils.generate(gene);
        // System.out.println(id);
        // user.setUserId(id);
        user.setName(name);
        userMapper.insertGene(user);
        return user;
    }

    @RequestMapping("list")
    public List<User> list() {
        return userMapper.list();
    }
}
