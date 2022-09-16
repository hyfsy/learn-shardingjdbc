package com.hyf.shardingsphere.util;

/**
 * @author baB_hyf
 * @date 2022/09/14
 */
public class GeneUtils {

    public static void main(String[] args) {
        long id = Math.abs("userId".hashCode());
        long gene = Math.abs("张三".hashCode());
        int genSize = 5;

        System.out.println(Long.toBinaryString(id) + " -> " + id);
        System.out.println(Long.toBinaryString(gene) + " -> " + gene);

        long gene2 = gene2(id, gene, genSize);
        long gene3 = gene3(id, gene, genSize);
        System.out.println(Long.toBinaryString(gene2) + " -> " + gene2);
        System.out.println(Long.toBinaryString(gene3) + " -> " + gene3);
    }

    public static long gene2(long id, long gene, int geneSize) {
        long geneMask = (1L << geneSize) - 1L;
        return Math.abs(id - (id & geneMask) + (gene & geneMask));
    }

    public static long gene3(long id, long gene, int geneSize) {
        return Math.abs((id << geneSize) + (gene & ((1L << geneSize) - 1)));
    }

}
