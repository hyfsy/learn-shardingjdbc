package com.hyf.shardingsphere.basic.test;

import com.hyf.shardingsphere.basic.utils.DataSourceUtils;
import org.apache.shardingsphere.encrypt.api.config.EncryptRuleConfiguration;
import org.apache.shardingsphere.encrypt.api.config.rule.EncryptColumnRuleConfiguration;
import org.apache.shardingsphere.encrypt.api.config.rule.EncryptTableRuleConfiguration;
import org.apache.shardingsphere.infra.config.algorithm.ShardingSphereAlgorithmConfiguration;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author baB_hyf
 * @date 2022/08/06
 */
public class EncryptTests {
    public static void main(String[] args) {

        // 加密列配置
        EncryptColumnRuleConfiguration encryptColumnRuleConfiguration =
                new EncryptColumnRuleConfiguration("password", "password",
                        "" /* password_assisted */, "password_plain", "aes_encryptor", true);

        // 加密表绑定
        EncryptTableRuleConfiguration aes_encryptor =
                new EncryptTableRuleConfiguration("t_user",
                        Collections.singleton(encryptColumnRuleConfiguration), false);

        // 加密算法配置
        Properties properties = new Properties();
        properties.setProperty("aes-key-value", "123456");
        ShardingSphereAlgorithmConfiguration aes =
                new ShardingSphereAlgorithmConfiguration("AES", properties);
        Map<String, ShardingSphereAlgorithmConfiguration> algorithmConfigurations = new HashMap<>();
        algorithmConfigurations.put("aes_encryptor", aes);

        EncryptRuleConfiguration encryptRuleConfiguration =
                new EncryptRuleConfiguration(Collections.singleton(aes_encryptor),
                        algorithmConfigurations);

        DataSource dataSource = DataSourceUtils.create(encryptRuleConfiguration);

        // 插入最后一个库？
        String sql = "insert into t_user (name, password) values ('张三', '11111')";
        DataSourceUtils.execute(dataSource, sql);
        sql = "select * from t_user"; // please change the queryWithCipherColumn
        DataSourceUtils.executeQueryUser(dataSource, sql);
        sql = "select * from t_user where password = '11111'";
        DataSourceUtils.executeQueryUser(dataSource, sql);
    }
}
