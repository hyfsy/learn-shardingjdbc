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

import com.hyf.shardingsphere.utils.DataSourceUtils;
import org.apache.shardingsphere.dbdiscovery.api.config.DatabaseDiscoveryRuleConfiguration;
import org.apache.shardingsphere.dbdiscovery.api.config.rule.DatabaseDiscoveryDataSourceRuleConfiguration;
import org.apache.shardingsphere.dbdiscovery.api.config.rule.DatabaseDiscoveryHeartBeatConfiguration;
import org.apache.shardingsphere.driver.api.ShardingSphereDataSourceFactory;
import org.apache.shardingsphere.encrypt.api.config.EncryptRuleConfiguration;
import org.apache.shardingsphere.encrypt.api.config.rule.EncryptColumnRuleConfiguration;
import org.apache.shardingsphere.encrypt.api.config.rule.EncryptTableRuleConfiguration;
import org.apache.shardingsphere.infra.config.RuleConfiguration;
import org.apache.shardingsphere.infra.config.algorithm.ShardingSphereAlgorithmConfiguration;
import org.apache.shardingsphere.infra.config.mode.ModeConfiguration;
import org.apache.shardingsphere.mode.repository.standalone.StandalonePersistRepositoryConfiguration;
import org.apache.shardingsphere.parser.config.SQLParserRuleConfiguration;
import org.apache.shardingsphere.readwritesplitting.api.ReadwriteSplittingRuleConfiguration;
import org.apache.shardingsphere.readwritesplitting.api.rule.ReadwriteSplittingDataSourceRuleConfiguration;
import org.apache.shardingsphere.shadow.api.config.ShadowRuleConfiguration;
import org.apache.shardingsphere.shadow.api.config.datasource.ShadowDataSourceConfiguration;
import org.apache.shardingsphere.shadow.api.config.table.ShadowTableConfiguration;
import org.apache.shardingsphere.sharding.api.config.ShardingRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.rule.ShardingTableRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.strategy.keygen.KeyGenerateStrategyConfiguration;
import org.apache.shardingsphere.sharding.api.config.strategy.sharding.StandardShardingStrategyConfiguration;
import org.apache.shardingsphere.sql.parser.api.CacheOption;
import org.apache.shardingsphere.sql.parser.api.SQLParserEngine;
import org.apache.shardingsphere.sql.parser.api.SQLVisitorEngine;
import org.apache.shardingsphere.sql.parser.core.ParseASTNode;
import org.apache.shardingsphere.sql.parser.sql.dialect.statement.mysql.MySQLStatement;
import org.apache.shardingsphere.sqltranslator.api.config.SQLTranslatorRuleConfiguration;
import org.apache.shardingsphere.transaction.config.TransactionRuleConfiguration;

import javax.sql.DataSource;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * @author baB_hyf
 * @date 2022/08/02
 */
public class Basic {

    public static void main(String[] args) throws SQLException {

        DataSource dataSource = createShardingSphereDataSource();

        String sql = "SELECT i.* FROM t_order o JOIN t_order_item i ON o.order_id=i.order_id WHERE o.user_id=? AND o.order_id=?";
        try (Connection conn = dataSource.getConnection();
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

    private static DataSource createShardingSphereDataSource() {
        String databaseName = "shardingjdbc"; // Indicate logic database name
        ModeConfiguration modeConfig = createModeConfiguration(); // Build mode configuration
        Map<String, DataSource> dataSourceMap = createDataSourceMap(); // Build actual data sources
        Collection<RuleConfiguration> ruleConfigs = createRuleConfigurations(); // Build concentrate rule configurations
        Properties props = createProperties(); // Build properties
        try {
            // Create ShardingSphereDataSource
            // YamlShardingSphereDataSourceFactory.createDataSource("");
            return ShardingSphereDataSourceFactory.createDataSource(databaseName, modeConfig, dataSourceMap, ruleConfigs, props);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static ModeConfiguration createModeConfiguration() {
        Properties standalonePersistRepositoryConfigurationProperties = new Properties();
        standalonePersistRepositoryConfigurationProperties.put("path", System.getProperty("user.dir") + File.separator + ".shardingsphere");
        StandalonePersistRepositoryConfiguration standalonePersistRepositoryConfiguration =
                new StandalonePersistRepositoryConfiguration("File", standalonePersistRepositoryConfigurationProperties);
        return new ModeConfiguration("Standalone", standalonePersistRepositoryConfiguration, true);
    }

    private static Map<String, DataSource> createDataSourceMap() {
        Map<String, DataSource> dataSourceMap = new HashMap<>();

        // 配置第 1 个数据源
        dataSourceMap.put("ds_1", DataSourceUtils.createDataSource("shardingjdbc"));

        // 配置第 2 个数据源
        dataSourceMap.put("ds_2", DataSourceUtils.createDataSource("shardingjdbc2"));

        // 配置第 3 个数据源
        dataSourceMap.put("ds_3", DataSourceUtils.createDataSource("shardingjdbc3"));

        // 配置第 4 个数据源
        dataSourceMap.put("ds_shadow", DataSourceUtils.createDataSource("shardingjdbc_shadow"));

        return dataSourceMap;
    }

    private static Collection<RuleConfiguration> createRuleConfigurations() {
        List<RuleConfiguration> configurations = new ArrayList<>();

        configurations.addAll(createShardingRuleConfigurations());
        configurations.addAll(createReadWriteSplitRuleConfigurations());
        configurations.addAll(createTransactionRuleConfigurations());
        configurations.addAll(createDatabaseDiscoveryRuleConfigurations());
        configurations.addAll(createEncryptRuleConfigurations());
        configurations.addAll(createShadowRuleConfigurations());
        configurations.addAll(createSQLParserRuleConfigurations());
        configurations.addAll(createSQLTranslatorRuleConfigurations());

        return configurations;
    }

    private static Collection<RuleConfiguration> createShardingRuleConfigurations() {
        ShardingRuleConfiguration shardingRuleConfiguration = new ShardingRuleConfiguration();

        ShardingTableRuleConfiguration shardingTableRuleConfiguration =
                new ShardingTableRuleConfiguration("t_order", "ds_${0..1}.t_order_${[0, 1]}");
        shardingTableRuleConfiguration.setKeyGenerateStrategy(
                new KeyGenerateStrategyConfiguration("order_id", "snowflake"));
        shardingRuleConfiguration.getTables().add(shardingTableRuleConfiguration);
        shardingRuleConfiguration.getBindingTableGroups().add("t_order, t_order_item");
        shardingRuleConfiguration.getBroadcastTables().add("t_address");
        shardingRuleConfiguration.setDefaultDatabaseShardingStrategy(
                new StandardShardingStrategyConfiguration("user_id", "inline"));
        shardingRuleConfiguration.setDefaultTableShardingStrategy(
                new StandardShardingStrategyConfiguration("order_id", "standard_test_tbl"));

        Properties props = new Properties();
        props.setProperty("algorithm-expression", "ds_${user_id % 2}");
        shardingRuleConfiguration.getShardingAlgorithms().put("inline",
                new ShardingSphereAlgorithmConfiguration("INLINE", props));
        shardingRuleConfiguration.getShardingAlgorithms().put("standard_test_tbl",
                new ShardingSphereAlgorithmConfiguration("STANDARD_TEST_TBL", new Properties()));
        shardingRuleConfiguration.getKeyGenerators().put("snowflake",
                new ShardingSphereAlgorithmConfiguration("SNOWFLAKE", new Properties()));

        return Collections.singleton(shardingRuleConfiguration);
    }

    private static Collection<RuleConfiguration> createReadWriteSplitRuleConfigurations() {

        Properties properties = new Properties();
        // properties.put("auto-aware-data-source-name", "ds_1");
        properties.put("write-data-source-name", "ds_1");
        properties.put("read-data-source-names", "ds_2, ds_3");
        ReadwriteSplittingDataSourceRuleConfiguration readwriteSplittingDataSourceRuleConfiguration =
                new ReadwriteSplittingDataSourceRuleConfiguration("read_query_ds", "Static", properties, "demo_weight_lb");

        Properties algorithmProps = new Properties();
        algorithmProps.setProperty("ds_2", "2");
        algorithmProps.setProperty("ds_3", "1");
        Map<String, ShardingSphereAlgorithmConfiguration> algorithmConfigMap = new HashMap<>(1);
        algorithmConfigMap.put("demo_weight_lb", new ShardingSphereAlgorithmConfiguration("WEIGHT", algorithmProps));
        ReadwriteSplittingRuleConfiguration readwriteSplittingRuleConfiguration =
                new ReadwriteSplittingRuleConfiguration(
                        Collections.singleton(readwriteSplittingDataSourceRuleConfiguration),
                        algorithmConfigMap);

        return Collections.singleton(readwriteSplittingRuleConfiguration);
    }

    private static Collection<RuleConfiguration> createTransactionRuleConfigurations() {

        TransactionRuleConfiguration transactionRuleConfiguration =
                new TransactionRuleConfiguration("LOCAL", null, new Properties());

        return Collections.singleton(transactionRuleConfiguration);
    }

    private static Collection<RuleConfiguration> createDatabaseDiscoveryRuleConfigurations() {

        DatabaseDiscoveryDataSourceRuleConfiguration databaseDiscoveryDataSourceRuleConfiguration =
                new DatabaseDiscoveryDataSourceRuleConfiguration("read_query_ds",
                        Arrays.asList("ds_1", "ds_2", "ds_3"), "mgr-heartbeat", "mgr");

        Map<String, DatabaseDiscoveryHeartBeatConfiguration> discoveryHeartBeatConfiguration = new HashMap<>(1, 1);
        Properties heartBeanProps = new Properties();
        heartBeanProps.put("keep-alive-cron", "0/5 * * * * ?");
        discoveryHeartBeatConfiguration.put("mgr-heartbeat", new DatabaseDiscoveryHeartBeatConfiguration(heartBeanProps));

        Map<String, ShardingSphereAlgorithmConfiguration> discoveryTypes = new HashMap<>(1, 1);
        Properties discoveryProps = new Properties();
        // mgr group
        discoveryProps.put("group-name", "558edd3c-02ec-11ea-9bb3-080027e39bd2");
        discoveryTypes.put("mgr", new ShardingSphereAlgorithmConfiguration("MGR", discoveryProps));

        DatabaseDiscoveryRuleConfiguration databaseDiscoveryRuleConfiguration =
                new DatabaseDiscoveryRuleConfiguration(
                        Collections.singleton(databaseDiscoveryDataSourceRuleConfiguration),
                        discoveryHeartBeatConfiguration,
                        discoveryTypes);

        return Collections.singleton(databaseDiscoveryRuleConfiguration);
    }

    private static Collection<RuleConfiguration> createEncryptRuleConfigurations() {

        EncryptColumnRuleConfiguration encryptColumnRuleConfiguration = new EncryptColumnRuleConfiguration(
                "password", "enc_password", null, "password", "password_encryptor", true);
        EncryptTableRuleConfiguration encryptTableRuleConfiguration = new EncryptTableRuleConfiguration(
                "t_order", Collections.singleton(encryptColumnRuleConfiguration), true);
        Map<String, ShardingSphereAlgorithmConfiguration> encryptAlgorithmConfigs = new LinkedHashMap<>(2, 1);
        Properties props = new Properties();
        props.setProperty("aes-key-value", "123456");
        encryptAlgorithmConfigs.put("password_encryptor", new ShardingSphereAlgorithmConfiguration("AES", props));
        EncryptRuleConfiguration encryptRuleConfiguration =
                new EncryptRuleConfiguration(
                        Collections.singleton(encryptTableRuleConfiguration),
                        encryptAlgorithmConfigs,
                        true);

        return Collections.singleton(encryptRuleConfiguration);
    }

    private static Collection<RuleConfiguration> createShadowRuleConfigurations() {

        Map<String, ShardingSphereAlgorithmConfiguration> shardingSphereAlgorithmConfigurationMap = new LinkedHashMap<>();
        Properties userIdInsertProps = new Properties();
        userIdInsertProps.setProperty("operation", "insert");
        userIdInsertProps.setProperty("column", "order_type");
        userIdInsertProps.setProperty("value", "1");
        shardingSphereAlgorithmConfigurationMap.put("user-id-insert-match-algorithm",
                new ShardingSphereAlgorithmConfiguration("VALUE_MATCH", userIdInsertProps));

        Collection<String> algorithmNames = new LinkedList<>();
        algorithmNames.add("user-id-insert-match-algorithm");

        Map<String, ShadowDataSourceConfiguration> shadowDataSourceConfigurationMap = new LinkedHashMap<>();
        shadowDataSourceConfigurationMap.put("datasource_name_shadow", new ShadowDataSourceConfiguration("ds_1", "ds_shadow"));

        Map<String, ShadowTableConfiguration> shadowTableConfigurationMap = new LinkedHashMap<>();
        shadowTableConfigurationMap.put("t_order", new ShadowTableConfiguration(
                Collections.singletonList("datasource_name_shadow"), algorithmNames));

        ShadowRuleConfiguration shadowRuleConfiguration = new ShadowRuleConfiguration();
        shadowRuleConfiguration.setDataSources(shadowDataSourceConfigurationMap);
        shadowRuleConfiguration.setTables(shadowTableConfigurationMap);
        shadowRuleConfiguration.setShadowAlgorithms(shardingSphereAlgorithmConfigurationMap);

        return Collections.singleton(shadowRuleConfiguration);
    }

    private static Collection<RuleConfiguration> createSQLParserRuleConfigurations() {

        CacheOption cacheOption = new CacheOption(128, 1024L);
        SQLParserEngine parserEngine = new SQLParserEngine("MySQL", cacheOption);
        ParseASTNode parseASTNode = parserEngine.parse("SELECT t.id, t.name, t.price FROM t_order AS t ORDER BY t.id DESC;", false);
        SQLVisitorEngine visitorEngine = new SQLVisitorEngine("MySQL", "STATEMENT", false, new Properties());
        MySQLStatement sqlStatement = visitorEngine.visit(parseASTNode);
        System.out.println(sqlStatement.toString());

        SQLParserRuleConfiguration sqlParserRuleConfiguration = new SQLParserRuleConfiguration();
        sqlParserRuleConfiguration.setSqlCommentParseEnabled(true); // 解析sql注释提示
        sqlParserRuleConfiguration.setParseTreeCache(cacheOption);
        sqlParserRuleConfiguration.setSqlStatementCache(cacheOption);

        return Collections.singleton(sqlParserRuleConfiguration);
    }

    private static Collection<RuleConfiguration> createSQLTranslatorRuleConfigurations() {

        SQLTranslatorRuleConfiguration sqlTranslatorRuleConfiguration =
                new SQLTranslatorRuleConfiguration("JOOQ", true);

        return Collections.singleton(sqlTranslatorRuleConfiguration);
    }

    private static Properties createProperties() {
        Properties properties = new Properties();
        properties.setProperty("sql-show", Boolean.TRUE.toString());
        return properties;
    }
}
