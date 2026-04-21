package com.xf;

import com.stec.utils.ListUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IntervalProcessor {

    public static class InputInterval {
        private final double start;
        private final double end;

        public InputInterval(double start, double end) {
            if (start > end) throw new IllegalArgumentException("Invalid interval range");
            this.start = start;
            this.end = end;
        }
    }

    public static class IntervalData {
        private final double start;
        private final double end;
        private int count;

        public IntervalData(double start, double end) {
            this.start = start;
            this.end = end;
            this.count = 0;
        }

        // Getters
        public double getStart() { return start; }
        public double getEnd() { return end; }
        public int getCount() { return count; }
        
        void increment() { count++; }
    }

    public static List<IntervalData> processIntervals(List<Double> thresholds, List<InputInterval> inputs) {
        // 1. 参数校验
        validateThresholds(thresholds);
        
        // 2. 初始化区间集合
        List<IntervalData> intervals = createIntervalList(thresholds);
        
        // 3. 处理输入区间
        processInputIntervals(intervals, inputs);
        
        return Collections.unmodifiableList(intervals);
    }

    private static void validateThresholds(List<Double> thresholds) {
        if (thresholds.size() < 2) {
            throw new IllegalArgumentException("至少需要两个阈值点");
        }
        
        Double prev = null;
        for (Double current : thresholds) {
            if (prev != null && current < prev) {
                throw new IllegalArgumentException("阈值必须为递增序列");
            }
            prev = current;
        }
    }

    private static List<IntervalData> createIntervalList(List<Double> thresholds) {
        List<IntervalData> list = new ArrayList<>();
        for (int i = 0; i < thresholds.size() - 1; i++) {
            double start = thresholds.get(i);
            double end = thresholds.get(i + 1);
            list.add(new IntervalData(start, end));
        }
        return list;
    }

    private static void processInputIntervals(List<IntervalData> intervals, List<InputInterval> inputs) {
        for (InputInterval input : inputs) {
            for (IntervalData interval : intervals) {
                if (hasOverlap(input, interval)) {
                    interval.increment();
                }
            }
        }
    }

    private static boolean hasOverlap(InputInterval input, IntervalData target) {
        return input.start <= target.end && input.end >= target.start;
    }

    // 示例用法
    public static void main(String[] args) {
        // 阈值集合（注意最后的异常值1844.73已被排除）
        List<Double> thresholds = ListUtils.newArrayList(
            5200.0, 6630.00, 11600.00, 14870.00, 14870.00,
            19892.25, 24594.91, 29855.96, 34488.451, 37250.277
        );

        // 输入区间集合
        List<InputInterval> inputs = new ArrayList<>();
        inputs.add(new InputInterval(0, 3000));       // 完全在左侧
        inputs.add(new InputInterval(5000, 7000));    // 覆盖第一个区间
        inputs.add(new InputInterval(14870, 14870));  // 零长度区间
        inputs.add(new InputInterval(30000, 40000));  // 覆盖最后两个区间

        List<IntervalData> results = processIntervals(thresholds, inputs);

        // 输出结果
        System.out.println("区间统计结果：");
        results.forEach(data -> 
            System.out.printf("[%.2f, %.2f) -> %d次覆盖\n", 
                data.getStart(), data.getEnd(), data.getCount()));
    }
}