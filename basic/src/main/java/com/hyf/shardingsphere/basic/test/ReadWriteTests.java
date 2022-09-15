package com.hyf.shardingsphere.basic.test;

import com.hyf.shardingsphere.basic.utils.DataSourceUtils;
import org.apache.shardingsphere.infra.config.algorithm.ShardingSphereAlgorithmConfiguration;
import org.apache.shardingsphere.readwritesplitting.api.ReadwriteSplittingRuleConfiguration;
import org.apache.shardingsphere.readwritesplitting.api.rule.ReadwriteSplittingDataSourceRuleConfiguration;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author baB_hyf
 * @date 2022/08/06
 */
public class ReadWriteTests {

    public static void main(String[] args) {

        // 读写分离
        Properties properties = new Properties();
        properties.put("write-data-source-name", "ds_0");
        properties.put("read-data-source-names", "ds_1, ds_2");
        ReadwriteSplittingDataSourceRuleConfiguration readwriteSplittingDataSourceRuleConfiguration =
                new ReadwriteSplittingDataSourceRuleConfiguration("readwrite_ds", "STATIC", properties, "round_robin");

        // 读从库的负载均衡
        Map<String, ShardingSphereAlgorithmConfiguration> loadBalancers = new HashMap<>();
        loadBalancers.put("round_robin", new ShardingSphereAlgorithmConfiguration("ROUND_ROBIN", new Properties()));

        ReadwriteSplittingRuleConfiguration readwriteSplittingRuleConfiguration =
                new ReadwriteSplittingRuleConfiguration(Collections.singleton(readwriteSplittingDataSourceRuleConfiguration), loadBalancers);

        DataSource dataSource = DataSourceUtils.createShardingSphereDataSource(
                readwriteSplittingRuleConfiguration, "shardingjdbc", "shardingjdbc1", "shardingjdbc2");

        String sql = "insert into t_order (order_id, order_name) values (2, '测试读写分离')";
        DataSourceUtils.execute(dataSource, sql);
        sql = "select * from t_order";
        DataSourceUtils.executeQueryOrder(dataSource, sql);
        sql = "select * from t_order";
        DataSourceUtils.executeQueryOrder(dataSource, sql);
    }
}
