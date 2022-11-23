package com.xf;

import org.springframework.util.CollectionUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author wangshuning
 *
 */
public class PlaceholdersUtils {
    /**
     * Prefix for system property placeholders: "${"
     */
    public static final String PLACEHOLDER_PREFIX = "${";
    /**
     * Suffix for system property placeholders: "}"
     */
    public static final String PLACEHOLDER_SUFFIX = "}";
    /**
     * Prefix for system property placeholders: "{"
     */
    public static final String PLACEHOLDER_PREFIXS = "{";

    /**
     * 将字符串text中由openToken和closeToken组成的占位符依次替换为args数组中的值
     * @param openToken
     * @param closeToken
     * @param text
     * @param args
     * @return
     */
    public static String parse(String openToken, String closeToken, String text, Object... args) {
        if (args == null || args.length <= 0) {
            return text;
        }
        int argsIndex = 0;
        if (text == null || text.isEmpty()) {
            return "";
        }
        char[] src = text.toCharArray();
        int offset = 0;
        // search open token
        int start = text.indexOf(openToken, offset);
        if (start == -1) {
            return text;
        }
        final StringBuilder builder = new StringBuilder();
        StringBuilder expression = null;
        while (start > -1) {
            if (start > 0 && src[start - 1] == '\\') {
                // this open token is escaped. remove the backslash and continue.
                builder.append(src, offset, start - offset - 1).append(openToken);
                offset = start + openToken.length();
            } else {
                // found open token. let's search close token.
                if (expression == null) {
                    expression = new StringBuilder();
                } else {
                    expression.setLength(0);
                }
                builder.append(src, offset, start - offset);
                offset = start + openToken.length();
                int end = text.indexOf(closeToken, offset);
                while (end > -1) {
                    if (end > offset && src[end - 1] == '\\') {
                        // this close token is escaped. remove the backslash and continue.
                        expression.append(src, offset, end - offset - 1).append(closeToken);
                        offset = end + closeToken.length();
                        end = text.indexOf(closeToken, offset);
                    } else {
                        expression.append(src, offset, end - offset);
                        offset = end + closeToken.length();
                        break;
                    }
                }
                if (end == -1) {
                    // close token was not found.
                    builder.append(src, start, src.length - start);
                    offset = src.length;
                } else {

                    String value = (argsIndex <= args.length - 1) ?
                            (args[argsIndex] == null ? "" : args[argsIndex].toString()) : expression.toString();
                    builder.append(value);
                    offset = end + closeToken.length();
                    argsIndex++;

                }
            }
            start = text.indexOf(openToken, offset);
        }
        if (offset < src.length) {
            builder.append(src, offset, src.length - offset);
        }
        return builder.toString();
    }
    public static String parse0(String text, Object... args) {
        return PlaceholdersUtils.parse(PLACEHOLDER_PREFIX, PLACEHOLDER_SUFFIX, text, args);
    }
    public static String parse1(String text, Object... args) {
        return PlaceholdersUtils.parse(PLACEHOLDER_PREFIXS, PLACEHOLDER_SUFFIX, text, args);
    }


    public static String parse2(String text, List<String> list) {
        return PlaceholdersUtils.parse(PLACEHOLDER_PREFIX, PLACEHOLDER_SUFFIX, text, list);
    }
    public static String parse3(String text, List<String> list) {
        return PlaceholdersUtils.parse(PLACEHOLDER_PREFIXS, PLACEHOLDER_SUFFIX, text, list);
    }
    /**
     * 将字符串text中由openToken和closeToken组成的占位符依次替换为list中的值
     * @param openToken
     * @param closeToken
     * @param text
     * @param list
     * @return
     */
    public static String parse(String openToken, String closeToken, String text, List<String> list) {
        if (CollectionUtils.isEmpty(list) || list.size() <= 0) {
            return text;
        }
        int argsIndex = 0;
        if (text == null || text.isEmpty()) {
            return "";
        }
        char[] src = text.toCharArray();
        int offset = 0;
        // search open token
        int start = text.indexOf(openToken, offset);
        if (start == -1) {
            return text;
        }
        final StringBuilder builder = new StringBuilder();
        StringBuilder expression = null;
        while (start > -1) {
            if (start > 0 && src[start - 1] == '\\') {
                // this open token is escaped. remove the backslash and continue.
                builder.append(src, offset, start - offset - 1).append(openToken);
                offset = start + openToken.length();
            } else {
                // found open token. let's search close token.
                if (expression == null) {
                    expression = new StringBuilder();
                } else {
                    expression.setLength(0);
                }
                builder.append(src, offset, start - offset);
                offset = start + openToken.length();
                int end = text.indexOf(closeToken, offset);
                while (end > -1) {
                    if (end > offset && src[end - 1] == '\\') {
                        // this close token is escaped. remove the backslash and continue.
                        expression.append(src, offset, end - offset - 1).append(closeToken);
                        offset = end + closeToken.length();
                        end = text.indexOf(closeToken, offset);
                    } else {
                        expression.append(src, offset, end - offset);
                        offset = end + closeToken.length();
                        break;
                    }
                }
                if (end == -1) {
                    // close token was not found.
                    builder.append(src, start, src.length - start);
                    offset = src.length;
                } else {

                    String value = (argsIndex <= list.size() - 1) ?
                            (list.get(argsIndex) == null ? "" : list.get(argsIndex).toString()) : expression.toString();
                    builder.append(value);
                    offset = end + closeToken.length();
                    argsIndex++;

                }
            }
            start = text.indexOf(openToken, offset);
        }
        if (offset < src.length) {
            builder.append(src, offset, src.length - offset);
        }
        return builder.toString();
    }
    /**
     * 解析字符串占位符
     * @param text
     * @param parameter
     * @return
     */
    public static String resolvePlaceholders(String text, Map<String, String> parameter) {
        if (parameter == null || parameter.isEmpty()) {
            return text;
        }
        StringBuffer buf = new StringBuffer(text);
        int startIndex = buf.indexOf(PLACEHOLDER_PREFIX);
        while (startIndex != -1) {
            int endIndex = buf.indexOf(PLACEHOLDER_SUFFIX, startIndex + PLACEHOLDER_PREFIX.length());
            if (endIndex != -1) {
                String placeholder = buf.substring(startIndex + PLACEHOLDER_PREFIX.length(), endIndex);
                int nextIndex = endIndex + PLACEHOLDER_SUFFIX.length();
                try {
                    String propVal = parameter.get(placeholder);
                    if (propVal != null) {
                        buf.replace(startIndex, endIndex + PLACEHOLDER_SUFFIX.length(), propVal);
                        nextIndex = startIndex + propVal.length();
                    } else {
                        System.out.println("Could not resolve placeholder '" + placeholder + "' in [" + text + "] ");
                    }
                } catch (Exception ex) {
                    System.out.println("Could not resolve placeholder '" + placeholder + "' in [" + text + "]: " + ex);
                }
                startIndex = buf.indexOf(PLACEHOLDER_PREFIX, nextIndex);
            } else {
                startIndex = -1;
            }
        }
        return buf.toString();
    }

    /**
     * date2比date1多的天数
     * @param date1
     * @param date2
     * @return
     */
    public static int differentDays(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        int day1= cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);

        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        //不同年
        if(year1 != year2) {
            int timeDistance = 0 ;
            for(int i = year1 ; i < year2 ; i ++)
            {
                //闰年
                if(i%4==0 && i%100!=0 || i%400==0)
                {
                    timeDistance += 366;
                }
                else    //不是闰年
                {
                    timeDistance += 365;
                }
            }

            return timeDistance + (day2-day1) ;
            // 同一年
        } else {
//            System.out.println("判断day2 - day1 : " + (day2-day1));
            return day2-day1;
        }
    }

//    public static void main(String[] args) {
//        String[] strings = {"1111","22222","正常"};
//        List<String> list = Arrays.asList("1111","dsfdsfsdfsd","正常");
//        String str = "${name}（${idNumber}）核酸检测结果${value}";
//        System.out.println(PlaceholdersUtils.parse0(str,strings));

//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//        try {
//            Date date = df.parse("2022-06-28 23:59:59");
//            Date date1 = df.parse("2022-07-1 00:00:01");
//            System.out.println(PlaceholdersUtils.differentDays(date,date1));
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
}
