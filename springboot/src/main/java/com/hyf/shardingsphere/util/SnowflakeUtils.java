package com.hyf.shardingsphere.util;

import org.apache.shardingsphere.sharding.algorithm.keygen.SnowflakeKeyGenerateAlgorithm;

/**
 * @author baB_hyf
 * @date 2022/09/14
 */
public class SnowflakeUtils {

    private static final SnowflakeKeyGenerateAlgorithm snowflakeKeyGenerateAlgorithm = new SnowflakeKeyGenerateAlgorithm();

    private static final long NEW_SEQUENCE_BITS = 8L;
    private static final long NEW_SEQUENCE_MASK = (1 << NEW_SEQUENCE_BITS) - 1;

    public static Long generate() {
        return snowflakeKeyGenerateAlgorithm.generateKey();
    }

    public static Long generate(long gene) {
        Long id = snowflakeKeyGenerateAlgorithm.generateKey(); // origin id
        long sequence = id & NEW_SEQUENCE_MASK; // origin sequence

        long geneSize = geneSize(gene);
        id = id | (sequence << geneSize) | gene; // new id

        return id;
    }


    private static long geneSize(long gene) {
        long term = 64;
        while (term > 0) {
            if ((gene & (1L << term)) == 1) {
                return term;
            }
            term--;
        }
        return 0;
    }
}
