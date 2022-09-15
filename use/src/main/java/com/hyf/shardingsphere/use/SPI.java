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

import org.apache.shardingsphere.encrypt.spi.EncryptAlgorithm;
import org.apache.shardingsphere.mode.persist.PersistRepository;
import org.apache.shardingsphere.readwritesplitting.spi.ReadQueryLoadBalanceAlgorithm;
import org.apache.shardingsphere.shadow.spi.ShadowAlgorithm;
import org.apache.shardingsphere.sharding.spi.KeyGenerateAlgorithm;
import org.apache.shardingsphere.sharding.spi.ShardingAlgorithm;
import org.apache.shardingsphere.sqltranslator.spi.SQLTranslator;

/**
 * @author baB_hyf
 * @date 2022/08/05
 */
public class SPI {

    public static void main(String[] args) {

        // 规则的内置算法
        // https://shardingsphere.apache.org/document/current/cn/user-manual/shardingsphere-jdbc/builtin-algorithm/

        // 完整的SPI
        // https://shardingsphere.apache.org/document/current/cn/dev-manual/

        Class<?>[] classes = new Class[]{
                // File
                // ZooKeeper
                // Etcd
                PersistRepository.class,
                // MOD
                // HASH_MOD
                // VOLUME_RANGE
                // BOUNDARY_RANGE
                // AUTO_INTERVAL
                // INLINE
                // INTERVAL
                // COMPLEX_INLINE
                // HINT_INLINE
                // CLASS_BASED
                ShardingAlgorithm.class,
                // SNOWFLAKE
                // NANOID
                // UUID
                // COSID
                // COSID_SNOWFLAKE
                KeyGenerateAlgorithm.class,
                // ROUND_ROBIN
                // RANDOM
                // WEIGHT
                // TRANSACTION_RANDOM
                // TRANSACTION_ROUND_ROBIN
                // TRANSACTION_WEIGHT
                // FIXED_REPLICA_RANDOM
                // FIXED_REPLICA_ROUND_ROBIN
                // FIXED_REPLICA_WEIGHT
                // FIXED_PRIMARY
                ReadQueryLoadBalanceAlgorithm.class,
                // MD5
                // AES
                // RC4
                // SM3
                // SM4
                EncryptAlgorithm.class,
                // VALUE_MATCH
                // REGEX_MATCH
                // SIMPLE_HINT
                ShadowAlgorithm.class,
                // JOOQ
                SQLTranslator.class,
        };

    }
}
