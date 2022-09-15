package com.hyf.shardingsphere.basic.test;

import com.hyf.shardingsphere.basic.utils.DataSourceUtils;
import org.apache.shardingsphere.driver.api.yaml.YamlShardingSphereDataSourceFactory;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;

/**
 * @author baB_hyf
 * @date 2022/08/06
 */
public class ShadowYamlTests {

    public static void main(String[] args) throws IOException, SQLException {

        URL resource = ClassLoader.getSystemClassLoader().getResource("shadow.yaml");
        assert resource != null;
        DataSource dataSource = YamlShardingSphereDataSourceFactory.createDataSource(new File(resource.getFile()));

        String sql = "/* shadow:true */\ninsert into t_user (name, age, password) values ('李四3', 17, 'kAPBEO0+YAsV8Vs+KYso0w==')";
        DataSourceUtils.execute(dataSource, sql);
        sql = "select * from t_user";
        DataSourceUtils.executeQueryUser(dataSource, sql);
    }
}
