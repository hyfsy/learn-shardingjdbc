package com.hyf.shardingsphere.basic.test;

import com.hyf.shardingsphere.basic.utils.DataSourceUtils;
import org.apache.shardingsphere.driver.api.ShardingSphereDataSourceFactory;
import org.apache.shardingsphere.infra.config.RuleConfiguration;
import org.apache.shardingsphere.infra.config.algorithm.ShardingSphereAlgorithmConfiguration;
import org.apache.shardingsphere.sharding.api.config.ShardingRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.rule.ShardingTableRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.strategy.keygen.KeyGenerateStrategyConfiguration;
import org.apache.shardingsphere.sharding.api.config.strategy.sharding.StandardShardingStrategyConfiguration;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;

/**
 * @author baB_hyf
 * @date 2022/08/06
 */
public class ShardingTests {

    public static void main(String[] args) throws SQLException {

        Map<String, DataSource> dataSourceMap = new HashMap<>();
        dataSourceMap.put("ds_0", DataSourceUtils.createDataSource("shardingjdbc"));
        dataSourceMap.put("ds_1", DataSourceUtils.createDataSource("shardingjdbc1"));

        ShardingRuleConfiguration shardingRuleConfiguration = new ShardingRuleConfiguration();
        ShardingTableRuleConfiguration t_order = new ShardingTableRuleConfiguration("t_order", "ds_${0..1}.t_order_$->{[0, 1]}");
        // 分库配置
        t_order.setDatabaseShardingStrategy(new StandardShardingStrategyConfiguration("order_id", "database_t_order_inline"));
        // 分表配置
        t_order.setTableShardingStrategy(new StandardShardingStrategyConfiguration("order_id", "table_t_order_inline"));
        // 主键生成策略
        t_order.setKeyGenerateStrategy(new KeyGenerateStrategyConfiguration("id", "order_id_snowflake"));
        ShardingTableRuleConfiguration t_order_item = new ShardingTableRuleConfiguration("t_order_item", "ds_${0..1}.t_order_item_$->{[0, 1]}");
        t_order.setTableShardingStrategy(new StandardShardingStrategyConfiguration("order_id", "table_t_order_inline"));
        shardingRuleConfiguration.getTables().add(t_order);
        shardingRuleConfiguration.getTables().add(t_order_item);
        Properties shardDatabaseInlineProperties = new Properties();
        shardDatabaseInlineProperties.put("algorithm-expression", "ds_${order_id % 2}");
        Properties shardTableInlineProperties = new Properties();
        shardTableInlineProperties.put("algorithm-expression", "t_order_${order_id % 2}");
        shardingRuleConfiguration.getShardingAlgorithms().put("database_t_order_inline", new ShardingSphereAlgorithmConfiguration("INLINE", shardDatabaseInlineProperties));
        shardingRuleConfiguration.getShardingAlgorithms().put("table_t_order_inline", new ShardingSphereAlgorithmConfiguration("INLINE", shardTableInlineProperties));
        shardingRuleConfiguration.getKeyGenerators().put("order_id_snowflake", new ShardingSphereAlgorithmConfiguration("SNOWFLAKE", new Properties()));
        // 广播表
        shardingRuleConfiguration.getBroadcastTables().add("t_address");
        shardingRuleConfiguration.getBroadcastTables().add("t_user");
        // 绑定表
        shardingRuleConfiguration.getBindingTableGroups().add("t_order");
        shardingRuleConfiguration.getBindingTableGroups().add("t_order_item");
        Collection<RuleConfiguration> ruleConfigurations = Collections.singleton(shardingRuleConfiguration);

        Properties properties = new Properties();
        properties.put("sql-show", true);

        DataSource dataSource = ShardingSphereDataSourceFactory.createDataSource(dataSourceMap, ruleConfigurations, properties);

        String sql = "insert into t_order (order_id, order_name, order_price, user_id, address_id) values (1, '测试订单', 188, 1, 1)";
        DataSourceUtils.execute(dataSource, sql);
        sql = "select * from t_order where order_id = 1";
        DataSourceUtils.executeQueryOrder(dataSource, sql);
        sql = "select * from t_order where order_id = 2";
        DataSourceUtils.executeQueryOrder(dataSource, sql);
        sql = "SELECT i.* FROM t_order o JOIN t_order_item i ON o.order_id = i.order_id " +
                "WHERE o.user_id = 1 AND o.order_id = 1";
        DataSourceUtils.executeQueryOrder(dataSource, sql);

    }
}
