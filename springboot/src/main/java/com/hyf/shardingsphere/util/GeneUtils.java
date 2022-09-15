package com.hyf.shardingsphere.util;

/**
 * @author baB_hyf
 * @date 2022/09/14
 */
public class GeneUtils {

    public static void main(String[] args) {
        // long gene = gene("扫东方红", 4);
        // Long id = SnowflakeUtils.generate(gene);
        // System.out.println(id);

    }

    public static long gene(String str, long length) {
        int sum = str.chars().sum();
        return sum & (1L << length) - 1L;
    }

}
