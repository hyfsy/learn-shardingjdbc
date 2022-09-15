package com.hyf.shardingsphere.basic.test;

import com.hyf.shardingsphere.basic.utils.DataSourceUtils;
import org.apache.shardingsphere.infra.config.algorithm.ShardingSphereAlgorithmConfiguration;
import org.apache.shardingsphere.parser.config.SQLParserRuleConfiguration;
import org.apache.shardingsphere.shadow.api.config.ShadowRuleConfiguration;
import org.apache.shardingsphere.shadow.api.config.datasource.ShadowDataSourceConfiguration;
import org.apache.shardingsphere.shadow.api.config.table.ShadowTableConfiguration;
import org.apache.shardingsphere.sql.parser.api.CacheOption;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;

/**
 * @author baB_hyf
 * @date 2022/08/06
 */
public class ShadowTests {
    public static void main(String[] args) {

        // 启用hint
        SQLParserRuleConfiguration sqlParserRuleConfiguration = new SQLParserRuleConfiguration();
        sqlParserRuleConfiguration.setSqlCommentParseEnabled(true);
        sqlParserRuleConfiguration.setParseTreeCache(new CacheOption(128, 1024));
        sqlParserRuleConfiguration.setSqlStatementCache(new CacheOption(2000, 65535));

        ShadowDataSourceConfiguration shadowDataSourceConfiguration =
                new ShadowDataSourceConfiguration("ds_0", "ds_shadow");
        ShadowTableConfiguration shadowTableConfiguration =
                new ShadowTableConfiguration(Collections.singleton("shadow_datasource"),
                        Arrays.asList("user-id-insert-match-algorithm"));

        // simple hint
        Properties properties = new Properties();
        properties.put("shadow", "true");
        ShardingSphereAlgorithmConfiguration shardingSphereAlgorithmConfiguration =
                new ShardingSphereAlgorithmConfiguration("SIMPLE_HINT", properties);

        // value match
        Properties properties2 = new Properties();
        properties2.put("operation", "insert");
        properties2.put("column", "name");
        properties2.put("value", "李四2");
        ShardingSphereAlgorithmConfiguration shardingSphereAlgorithmConfiguration2 =
                new ShardingSphereAlgorithmConfiguration("VALUE_MATCH", properties2);

        // regex match
        Properties properties3 = new Properties();
        properties3.put("operation", "insert");
        properties3.put("column", "age");
        properties3.put("regex", "18");
        ShardingSphereAlgorithmConfiguration shardingSphereAlgorithmConfiguration3 =
                new ShardingSphereAlgorithmConfiguration("REGEX_MATCH", properties3);

        ShadowRuleConfiguration shadowRuleConfiguration = new ShadowRuleConfiguration();
        shadowRuleConfiguration.getDataSources().put("shadow_datasource", shadowDataSourceConfiguration);
        shadowRuleConfiguration.getTables().put("t_user", shadowTableConfiguration);
        shadowRuleConfiguration.getShadowAlgorithms().put("user-id-insert-match-algorithm", shardingSphereAlgorithmConfiguration);
        // shadowRuleConfiguration.getShadowAlgorithms().put("user-id-insert-match-algorithm", shardingSphereAlgorithmConfiguration2);
        // shadowRuleConfiguration.getShadowAlgorithms().put("user-id-insert-match-algorithm", shardingSphereAlgorithmConfiguration3);

        DataSource dataSource = DataSourceUtils.create(Arrays.asList(sqlParserRuleConfiguration, shadowRuleConfiguration));

        String sql = "/* shadow:true */\ninsert into t_user (name, age, password) values ('李四3', 17, 'kAPBEO0+YAsV8Vs+KYso0w==')";
        DataSourceUtils.execute(dataSource, sql);
        sql = "select * from t_user";
        DataSourceUtils.executeQueryUser(dataSource, sql);
    }
}
