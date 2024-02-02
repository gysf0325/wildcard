package com.wildcard.gysf0325.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 根据spring的AntPathMatcher模仿而来的文件路径通配符匹配
 *      *代表一个或多个字符
 *      **代表一层或多层路径
 */
public class PathFileWildcard {

    /**
     *根据统配符路径检查对应文件路径是否匹配
     * @param pattern   传入通配符路径 (*代表一个或多个字符     **代表一层或多层路径)
     * @param path      传入需要匹配的文件路径（带后缀名的文件绝对路径）
     * @return
     */
    public boolean match(String pattern, String path) {
        //匹配路径为空时直接返回false
        if (path == null ) {
            return false;
        }
        //通配符参数为空时，按目前初定规则，直接返回true
        if (pattern == null | pattern == "") {
            return true;
        }
        //按路径分隔符划分为模式数组
        String[] pattDirs = pattern.split("/");
        //按路径分隔符划分为路径数组
        String[] pathDirs = path.split("/");
        //模式数组的头尾指针
        int pattIdxStart = 0;
        int pattIdxEnd = pattDirs.length - 1;
        //路径数组的头尾指针
        int pathIdxStart = 0;
        int pathIdxEnd = pathDirs.length - 1;

        // 从前往后匹配**
        while (pattIdxStart <= pattIdxEnd && pathIdxStart <= pathIdxEnd) {
            String pattDir = pattDirs[pattIdxStart];
            if ("**".equals(pattDir)) {
                break;
            }
            if (!matchStrings(pattDir, pathDirs[pathIdxStart])) {
                return false;
            }
            pattIdxStart++;
            pathIdxStart++;
        }
        /**
         *程序能运行到现在，有三种情况：
         * 1、在模式与路径的共同范围内出现了 ** 匹配（从前往后）
         * 2、路径全部匹配完毕
         * 3、模式全部匹配完毕
         */
        //路径全部匹配完毕
        if (pathIdxStart > pathIdxEnd) {
            // 模式全部匹配完毕
            if (pattIdxStart > pattIdxEnd) {
                //两串匹配完成返回true
                return true;
            }
            //由于路径为带后缀名的绝对路径，若路径全部匹配完毕但模式有剩余则匹配失败，返回false
            return false;
        }else if (pattIdxStart > pattIdxEnd) {
           //模式全部匹配完毕,路径还没分析完.属于前缀匹配成功，返回true
            return true;
        }


        /**
         *程序能运行到现在，从前往后模式匹配中一定出现了**，并且两串路径匹配均有剩余未匹配的
         */
        // 从后往前匹配**
        while (pattIdxStart <= pattIdxEnd && pathIdxStart <= pathIdxEnd) {
            String pattDir = pattDirs[pattIdxEnd];
            if (pattDir.equals("**")) {
                break;
            }
            if (!matchStrings(pattDir, pathDirs[pathIdxEnd])) {
                return false;
            }
            pattIdxEnd--;
            pathIdxEnd--;
        }
        /**
         *程序能运行到现在，有三种情况：
         * 1、在模式与路径的共同范围内出现了 ** 匹配（从后往前）
         * 2、路径全部匹配完毕
         * 3、模式全部匹配完毕
         */
        //路径全部匹配完毕
        if (pathIdxStart > pathIdxEnd) {
            // 模式全部匹配完毕
            if (pattIdxStart > pattIdxEnd) {
                //两串匹配完成返回true
                return true;
            }
            //当路径全部匹配完毕，模式还有剩余时。剩余模式中存在不为**则为匹配失败，返回false
            //作为一种错误矫正优化，用户输入通配符中**多于实际路径层数
            for (int i = pattIdxStart; i <= pattIdxEnd; i++) {
                if (!pattDirs[i].equals("**")) {
                    return false;
                }
            }
            //当路径全部匹配完毕，模式还有剩余时。剩余模式若全部为**则算匹配成功，返回true
            return true;
        }else if (pattIdxStart > pattIdxEnd) {
            //模式全部匹配完毕,路径还没分析完.属于后缀匹配成功，返回true
            return true;
        }

        //最复杂的情况
        //前后**都进行匹配，但中间剩余模式串可能还存在**
        while (pattIdxStart != pattIdxEnd && pathIdxStart <= pathIdxEnd) {
            int patIdxTmp = -1;
            //当前的pattIdxStart即为从前到后找到的第一个**的位置
            //从前往后，找到中间串的下一个**的位置即patIdxTmp
            for (int i = pattIdxStart + 1; i <= pattIdxEnd; i++) {
                if (pattDirs[i].equals("**")) {
                    patIdxTmp = i;
                    break;
                }
            }
            //当出现**/**时，将中间串的**起始位置加一。进入下一轮处理两个**之间串的循环
            if (patIdxTmp == pattIdxStart + 1) {
                pattIdxStart++;
                continue;
            }

            // 对两个 ** 之间的路径进行匹配
            //patLength为两个**之间字符串个数
            int patLength = (patIdxTmp - pattIdxStart - 1);
            //strLength为剩余需要匹配文件路径字符串个数
            int strLength = (pathIdxEnd - pathIdxStart + 1);
            //匹配标志(路径串中匹配到中间串的结束位置)
            int foundIdx = -1;

            strLoop:
            //外循环是定长串，串首递增变化的循环
            for (int i = 0; i <= strLength - patLength; i++) {
                //内循环是对串是否匹配的循环检查
                for (int j = 0; j < patLength; j++) {
                    String subPat = pattDirs[pattIdxStart + j + 1];
                    String subStr = pathDirs[pathIdxStart + i + j];
                    if (!matchStrings(subPat, subStr)) {
                        continue strLoop;
                    }
                }
                //一次中间串的全匹配立即给匹配标志赋值并跳出检查循环
                //循环了i次匹配成功，foundIdx就是在路径串中匹配成功时，中间串串首匹配成功的位置
                foundIdx = pathIdxStart + i;
                break;
            }
            //匹配标志未被赋值时则代表匹配失败，直接返回false
            if (foundIdx == -1) {
                return false;
            }
            //将当前第二个**的位置赋予pattIdxStart，作为下一次循环中第一个**的位置
            pattIdxStart = patIdxTmp;
            //串首匹配成功的位置加中间串的长度，也就是本次路径串匹配完成的串尾，作为下一次循环中需要匹配串的初始位置
            pathIdxStart = foundIdx + patLength;
        }

        //最后可能存在当路径全部匹配完毕，模式还有剩余时。剩余模式中存在不为**则为匹配失败，返回false
        //作为一种错误矫正优化，用户输入通配符中**多于实际路径层数
        for (int i = pattIdxStart; i <= pattIdxEnd; i++) {
            if (!pattDirs[i].equals("**")) {
                return false;
            }
        }
        //当路径全部匹配完毕，模式还有剩余时。剩余模式若全部为**则算匹配成功，返回true
        return true;
    }

    /**
     * 单个字符串（包含通配符）匹配函数
     * @param pattern   单个字符串（包含通配符）
     * @param str       要匹配的字符串
     * @return
     */
    private boolean matchStrings(String pattern, String str) {
        //包含通配符*时需要正则处理再匹配
        if(pattern.contains("*")){
            StringBuilder sb = new StringBuilder();
            for(int i=0;i<pattern.length();i++){
                /**
                 *      在模式字符串出现通配符时，在其前面加上.
                 *      A*A处理后为A.*A
                 *      正则表达式中.代表任意单个字符，除换行符外
                 *      *代表零次或多次匹配前面的字符
                 *      所以.*就表示零个或多个任意字符
                 */
                if("*".equals(String.valueOf(pattern.charAt(i)))){
                    sb.append(".");
                }
                sb.append(pattern.charAt(i));
            }
            //模式字符串处理好之后，就可以作为正则规则去匹配字符串
            Matcher matcher = Pattern.compile(sb.toString()).matcher(str);
            return  matcher.matches();
        }
        //未包含通配符时直接按照String匹配是否相等
        return pattern.equals(str);
    }
}
