package com.studio.settlement.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class MathUtils {

    public static BigDecimal toBigDecimal(String str) {
        if (StringUtils.isBlank(str)) {
            return BigDecimal.ZERO;
        }
        return new BigDecimal(str);
    }

    /**
     * 计算累计值
     * @param list
     * @return
     */
    public static String calculateTotal(List<String> list){
        if (CollectionUtils.isEmpty(list)){
            return "0";
        }

        BigDecimal total = new BigDecimal(0);
        for (String data : list) {
            if (StringUtils.isNotBlank(data)){
                total = total.add(new BigDecimal(data));
            }
        }

        return total.toString();
    }

    /**
     * 计算平均值
     * @param size
     * @param total
     */
    public static String calculateAvg(int size, BigDecimal total){
        if (0 == size || null == total){
            return "0";
        }

        return total.divide(new BigDecimal(size), 2, BigDecimal.ROUND_HALF_UP).toString();
    }

    /**
     * 计算最大值
     * @param list
     * @return
     */
    public static String calculateMax(List<String> list){
        if (CollectionUtils.isEmpty(list)){
            return "0";
        }

        List<BigDecimal> bigDecimalList = new ArrayList<>();

        for (String data : list) {
            if (StringUtils.isNotBlank(data)){
                bigDecimalList.add(new BigDecimal(data));
            }
        }

        Optional<BigDecimal> max = bigDecimalList.stream().max(Comparator.naturalOrder());

        return max.isPresent() ? max.get().toString() : "0";
    }

    /**
     * 计算最小值
     * @param list
     * @return
     */
    public static String calculateMin(List<String> list){
        if (CollectionUtils.isEmpty(list)){
            return "0";
        }

        List<BigDecimal> bigDecimalList = new ArrayList<>();

        for (String data : list) {
            if (StringUtils.isNotBlank(data)){
                bigDecimalList.add(new BigDecimal(data));
            }
        }

        Optional<BigDecimal> min = bigDecimalList.stream().min(Comparator.naturalOrder());

        return min.isPresent() ? min.get().toString() : "0";
    }

    /**
     * 替换字符
     * @param value
     * @return
     */
    public static String format(String value){
        if (StringUtils.isEmpty(value)){
            return value;
        }

        value = value.replaceAll("\n", "").replaceAll("\r", "").
                replaceAll("/", "").replaceAll("#DIV/0!", "").
                replaceAll(" ", "").replaceAll("-", "").
                replaceAll("#VALUE!", "");

        return value;
    }

    /**
     * 如果非空不包含大于小于才添加
     * @param value
     * @return ＜0.8
     */
    public static void addIfNotContains(List<String> list, String value){
        if (StringUtils.isNotBlank(value) && !StringUtils.contains(value, ">") && !StringUtils.contains(value, "<") &&
                !StringUtils.contains(value, "＜") && !StringUtils.contains(value, "＞")){
            list.add(value);
        }
    }

    public static boolean containsSymbol(String value) {
        return StringUtils.contains(value, ">") || StringUtils.contains(value, "<")
                || StringUtils.contains(value, "＜") || StringUtils.contains(value, "＞");
    }

    public static boolean checkRealNumber(String value) {
        try {
            BigDecimal bigDecimal = new BigDecimal(value);
        } catch (NumberFormatException e) {
            log.error("数据转换异常：", e);
            return false;
        }

        return true;
    }

    public static boolean ge(String value1, String value2) {
        value1 = format(value1).replaceAll("<", "").replaceAll(">", "")
                .replaceAll("＞", "").replaceAll("＜", "");
        value2 = format(value2).replaceAll("<", "").replaceAll(">", "")
                .replaceAll("＞", "").replaceAll("＜", "");

        return new BigDecimal(value1).compareTo(new BigDecimal(value2)) >= 0;
    }

    public static boolean le(String value1, String value2) {
        value1 = format(value1).replaceAll("<", "").replaceAll(">", "")
                .replaceAll("＞", "").replaceAll("＜", "");
        value2 = format(value2).replaceAll("<", "").replaceAll(">", "")
                .replaceAll("＞", "").replaceAll("＜", "");

        return new BigDecimal(value1).compareTo(new BigDecimal(value2)) <= 0;
    }

    public static boolean isNull(String...value) {
        for (String s : value) {
            if (StringUtils.isBlank(s)) {
                return true;
            }
        }

        return false;
    }


}
