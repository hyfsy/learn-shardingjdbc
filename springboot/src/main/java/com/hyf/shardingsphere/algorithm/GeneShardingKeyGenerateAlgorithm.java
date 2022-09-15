// package com.hyf.shardingsphere.algorithm;
//
// import org.apache.shardingsphere.sharding.algorithm.keygen.SnowflakeKeyGenerateAlgorithm;
// import org.apache.shardingsphere.sharding.spi.KeyGenerateAlgorithm;
//
// import java.util.Properties;
//
// /**
//  * @author baB_hyf
//  * @date 2022/09/14
//  */
// public class GeneShardingKeyGenerateAlgorithm implements KeyGenerateAlgorithm {
//
//     private final SnowflakeKeyGenerateAlgorithm snowflakeKeyGenerateAlgorithm = new SnowflakeKeyGenerateAlgorithm();
//
//     @Override
//     public Comparable<?> generateKey() {
//         Long id = snowflakeKeyGenerateAlgorithm.generateKey();
//         id = id | 1;
//         return id;
//     }
//
//     @Override
//     public Properties getProps() {
//         return snowflakeKeyGenerateAlgorithm.getProps();
//     }
//
//     @Override
//     public void init(Properties props) {
//         snowflakeKeyGenerateAlgorithm.init(props);
//     }
//
//     @Override
//     public String getType() {
//         return "GENE";
//     }
// }
