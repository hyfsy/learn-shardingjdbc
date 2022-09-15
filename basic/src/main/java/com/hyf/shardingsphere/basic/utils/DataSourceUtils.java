/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hyf.shardingsphere.basic.utils;

import com.hyf.shardingsphere.basic.pojo.Order;
import com.hyf.shardingsphere.basic.pojo.User;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.shardingsphere.driver.api.ShardingSphereDataSourceFactory;
import org.apache.shardingsphere.infra.config.RuleConfiguration;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * @author baB_hyf
 * @date 2022/08/05
 */
public class DataSourceUtils {

    public static DataSource create(RuleConfiguration ruleConfiguration) {
        return create(Collections.singleton(ruleConfiguration));
    }

    public static DataSource create(Collection<RuleConfiguration> ruleConfiguration) {
        try {
            return ShardingSphereDataSourceFactory.createDataSource(createDefaultDataSourceMap(), ruleConfiguration, createProperties());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static DataSource createShardingSphereDataSource(RuleConfiguration ruleConfiguration, String... databaseNames) {
        return createShardingSphereDataSource(Collections.singleton(ruleConfiguration), databaseNames);
    }

    public static DataSource createShardingSphereDataSource(Collection<RuleConfiguration> ruleConfigurations, String... databaseNames) {
        try {
            return ShardingSphereDataSourceFactory.createDataSource(createDataSourceMap(databaseNames), ruleConfigurations, createProperties());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<String, DataSource> createDataSourceMap(String... databaseNames) {
        Map<String, DataSource> dataSourceMap = new HashMap<>();
        int i = 0;
        for (String databaseName : databaseNames) {
            dataSourceMap.put("ds_" + i++, createDataSource(databaseName));
        }
        return dataSourceMap;
    }

    public static Map<String, DataSource> createDefaultDataSourceMap() {
        Map<String, DataSource> dataSourceMap = new HashMap<>();
        dataSourceMap.put("ds_0", createDataSource("shardingjdbc")); // master
        dataSourceMap.put("ds_1", createDataSource("shardingjdbc1")); // slave
        dataSourceMap.put("ds_2", createDataSource("shardingjdbc2")); // readwrite
        dataSourceMap.put("ds_3", createDataSource("shardingjdbc3")); // backup
        dataSourceMap.put("ds_shadow", createDataSource("shardingjdbc_shadow")); // shadow
        return dataSourceMap;
    }

    public static DataSource createDataSource(String databaseName) {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/" + databaseName + "?useSSL=false");
        dataSource.setUsername("root");
        dataSource.setPassword("hyflearn");
        return dataSource;
    }

    public static Properties createProperties() {
        Properties properties = new Properties();
        properties.put("sql-show", true);
        return properties;
    }

    public static void execute(DataSource dataSource, String sql) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void executeQuery(DataSource dataSource, String sql, ConsumerWithException<ResultSet> consumer) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    consumer.accept(rs);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void executeQueryOrder(DataSource dataSource, String sql) {
        executeQuery(dataSource, sql, rs -> {
            int i = 1;
            // ...
            Long id = rs.getLong(i++); // snowflake auto generate
            Long orderId = rs.getLong(i++);
            String orderName = rs.getString(i++);
            Long orderPrice = rs.getLong(i++);
            Long addressId = rs.getLong(i++);
            Long userId = rs.getLong(i++);

            Order order = new Order(id, orderId, orderName, orderPrice, addressId, userId);
            System.out.println(order);
        });
    }

    public static void executeQueryUser(DataSource dataSource, String sql) {
        executeQuery(dataSource, sql, rs -> {
            int i = 1;
            Long id = rs.getLong(i++);
            String name = rs.getString(i++);
            String password = rs.getString(i++);
            // String password_plain = rs.getString(i++);
            String password_assisted = rs.getString(i++);

            User user = new User(id, name, password, null, password_assisted);
            System.out.println(user);
        });
    }

    public interface ConsumerWithException<T> {
        void accept(T t) throws Exception;
    }
}
