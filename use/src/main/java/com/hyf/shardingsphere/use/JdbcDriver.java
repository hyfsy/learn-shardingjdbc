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

package com.hyf.shardingsphere.use;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * @author baB_hyf
 * @date 2022/08/05
 */
public class JdbcDriver {

    public static void main(String[] args) throws Exception {
        useOrigin();
        useDataSource();
    }

    public static void useOrigin() throws Exception {
        Class.forName("org.apache.shardingsphere.driver.ShardingSphereDriver");
        String jdbcUrl = "jdbc:shardingsphere:classpath:config.yaml";

        String sql = "SELECT i.* FROM t_order o JOIN t_order_item i ON o.order_id=i.order_id WHERE o.user_id=? AND o.order_id=?";
        try (
                Connection conn = DriverManager.getConnection(jdbcUrl);
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, 10);
            ps.setInt(2, 1000);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // ...
                }
            }
        }
    }

    public static void useDataSource() throws Exception {
        String driverClassName = "org.apache.shardingsphere.driver.ShardingSphereDriver";
        String jdbcUrl = "jdbc:shardingsphere:/absolute-path/to/config.yaml";

        // 以 HikariCP 为例
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setJdbcUrl(jdbcUrl);

        String sql = "SELECT i.* FROM t_order o JOIN t_order_item i ON o.order_id=i.order_id WHERE o.user_id=? AND o.order_id=?";
        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, 10);
            ps.setInt(2, 1000);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // ...
                }
            }
        }

    }
}
