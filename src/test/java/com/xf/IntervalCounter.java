package com.xf;

import java.util.ArrayList;
import java.util.List;

public class IntervalCounter {

    public static class Interval {
        double start;
        double end;

        public Interval(double start, double end) {
            this.start = start;
            this.end = end;
        }
    }

    public static int[] countIntervals(double[] a, List<Interval> intervals) {
        int[] counts = new int[9]; // 共有9个区间：(a1,a2)到(a9,a10)
        for (Interval interval : intervals) {
            double s = interval.start;
            double e = interval.end;
            for (int i = 0; i < 9; i++) {
                double aStart = a[i];
                double aEnd = a[i + 1];
                // 判断当前输入的区间是否与当前a的区间有交集
                if (s <= aEnd && e >= aStart) {
                    counts[i]++;
                }
            }
        }
        return counts;
    }

    public static void main(String[] args) {
        // 示例数据：假设a数组为a1到a10的值，这里以1到10为例
        double[] a = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        List<Interval> inputIntervals = new ArrayList<>();
        inputIntervals.add(new Interval(0, 3));
        inputIntervals.add(new Interval(3, 4));
        inputIntervals.add(new Interval(5, 10));

        int[] result = countIntervals(a, inputIntervals);

        // 输出结果
        for (int i = 0; i < result.length; i++) {
            System.out.printf("区间 (a%d,a%d) 的计数：%d\n", i + 1, i + 2, result[i]);
        }
    }
}