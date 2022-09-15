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

import org.apache.shardingsphere.infra.hint.HintManager;
import org.apache.shardingsphere.transaction.annotation.ShardingSphereTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.apache.shardingsphere.transaction.core.TransactionTypeHolder;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author baB_hyf
 * @date 2022/08/05
 */
public class Specific_API {

    public static void main(String[] args) {

        Specific_API specific_api = new Specific_API();

        useHintManager();
        specific_api.useDistributedTransaction();
    }

    public static void useHintManager() {

        // 1. 数据分片强制路由

        HintManager hintManager = HintManager.getInstance(); // new, AutoClosable can try with resource
        hintManager.addDatabaseShardingValue("t_order", 1);
        hintManager.addTableShardingValue("t_order", 2);
        hintManager.setDatabaseShardingValue(3); // 无分表，直接分库，强制路由
        try {
            // execute sql
        } finally {
            hintManager.close();
        }

        // 2. 读写分离强制路由

        hintManager.setWriteRouteOnly(); // 强制路由主库
        try {
            // execute sql
        } finally {
            hintManager.close();
        }

        // SQL Hint 功能需要用户提前开启解析注释的配置，设置 sqlCommentParseEnabled 为 true
        // 注释格式暂时只支持 /* */，内容需要以 ShardingSphere hint: 开始，属性名为 writeRouteOnly
        // 如：
        // /* ShardingSphere hint: writeRouteOnly=true */
        // SELECT * FROM t_order;

    }

    // 需要给 PlatformTransactionManager 和 JdbcTemplate 注入ShardingSphere的DataSource
    @Transactional
    @ShardingSphereTransactionType(TransactionType.XA) // 整合SpringBoot
    public void useDistributedTransaction() {
        TransactionTypeHolder.set(TransactionType.XA); // 基本方式使用
        // execute sql
    }
}
