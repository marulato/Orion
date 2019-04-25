package org.orion.common.miscutil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

public class MathUtil {

    public static double round(double value, int scale, boolean willRound) {
        BigDecimal bigDecimal = new BigDecimal(value);
        if (willRound) {
           bigDecimal = bigDecimal.setScale(scale, RoundingMode.HALF_UP);
        } else {
            bigDecimal = bigDecimal.setScale(scale, RoundingMode.DOWN);
        }
        return bigDecimal.doubleValue();
    }

    private static double[] convert(List<String> strings) {
        double[] cache = new double[strings.size()];
        for (int i = 0; i < strings.size(); i++) {
            cache[i] = Double.parseDouble(strings.get(i));
        }
        return cache;
    }

    public static double summation(List<String> numbers) {
        double[] cache = convert(numbers);
        return summation(cache);
    }

    public static double summation(double[] numbers) {
        double sum = 0;
        for (double num : numbers) {
            sum += num;
        }
        return sum;
    }

    public static double summation(int[] numbers) {
        double sum = 0;
        for (double num : numbers) {
            sum += num;
        }
        return sum;
    }

    public static double average(List<String> numbers) {
        return summation(numbers) / numbers.size();
    }

    public static double average(double[] numbers) {
        return summation(numbers) / numbers.length;
    }

    public static double average(int[] numbers) {
        return summation(numbers) / numbers.length;
    }

    public static double max(List<String> numbers) {
        double[] nums = convert(numbers);
        return max(nums);
    }

    public static double max(double[] numbers) {
        Arrays.sort(numbers);
        return numbers[numbers.length - 1];
    }

    public static double max(int[] numbers) {
        Arrays.sort(numbers);
        return numbers[numbers.length - 1];
    }

    public static double min(List<String> numbers) {
        double[] nums = convert(numbers);
        return min(nums);
    }

    public static double min(double[] numbers) {
        Arrays.sort(numbers);
        return numbers[0];
    }

    public static double min(int[] numbers) {
        Arrays.sort(numbers);
        return numbers[0];
    }

    public static double median(List<String> numbers) {
        double[] nums = convert(numbers);
        return median(nums);
    }

    public static double median(double[] numbers) {
        if (numbers.length % 2 == 0)
            return (numbers[numbers.length / 2 - 1] + numbers[numbers.length / 2]) / 2;
        else
            return numbers[numbers.length / 2];
    }

    public static double median(int[] numbers) {
        if (numbers.length % 2 == 0)
            return (numbers[numbers.length / 2 - 1] + numbers[numbers.length / 2]) / 2;
        else
            return numbers[numbers.length / 2];
    }

}
