package com.fiberhome.mapps.survey.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtil
{
    /**
     * sdf
     */
    public static final SimpleDateFormat sdf()
    {
        return new SimpleDateFormat("yyyy-MM-dd");
    }

    /**
     * 格式化含 时 分 秒
     */
    public static final SimpleDateFormat sdfHMS()
    {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 格式化含 时 分
     */
    public static final SimpleDateFormat sdfHM()
    {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm");
    }

    /**
     * 格式化时 分 秒
     */
    public static final SimpleDateFormat HMS()
    {
        return new SimpleDateFormat("HH:mm:ss");
    }

    /**
     * 格式化时 分
     */
    public static final SimpleDateFormat HM()
    {
        return new SimpleDateFormat("HH:mm");
    }

    /**
     * 一天当中小时的最大值
     */
    public static final int HOUR_MAX   = 23;
    /**
     * 小时中的分最大值
     */
    public static final int MINUTE_MAX = 59;
    /**
     * 分钟的秒的最大值
     */
    public static final int SECOND_MAX = 59;

    public static Date convertToDate(String dateStr) throws Exception
    {
        return sdf().parse(dateStr);
    }

    public static Date convertToTime(String dateStr) throws Exception
    {
        return sdfHMS().parse(dateStr);
    }

    /**
     * @param date
     *            2016-07-20
     * @return 2016-07-20 23:59:59
     */
    public static Date getLastTime(Date date)
    {
        if (date == null)
        {
            return null;
        }
        Calendar cd = Calendar.getInstance();
        cd.setTime(date);
        // 设置时分秒为各自的最大值
        cd.set(Calendar.HOUR_OF_DAY, HOUR_MAX);
        cd.set(Calendar.MINUTE, MINUTE_MAX);
        cd.set(Calendar.SECOND, SECOND_MAX);
        return cd.getTime();
    }

    public static List<String> convertList(Date beginDate, Date endDate) throws Exception
    {
        if (beginDate == null || endDate == null)
        {
            return null;
        }
        long beginTime = beginDate.getTime();
        long endTime = endDate.getTime();
        List<String> list = new ArrayList<String>();
        if (beginTime > endTime)
        {
            return null;
        }
        else if (beginTime == endTime)
        {
            list.add(sdf().format(beginDate));
        }
        else
        {
            for (int i = 0; i <= daysBetween(beginDate, endDate); i++)
            {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(beginDate);
                calendar.add(Calendar.DATE, i);
                String dateStr = sdf().format(calendar.getTime());
                list.add(dateStr);
            }
        }
        return list;
    }

    /**
     * 计算两个日期之间相差的天数
     * 
     * @param smdate
     *            较小的时间
     * @param bdate
     *            较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static long daysBetween(Date smdate, Date bdate) throws Exception
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        smdate = sdf.parse(sdf.format(smdate));
        bdate = sdf.parse(sdf.format(bdate));
        long time1 = smdate.getTime();
        long time2 = bdate.getTime();
        return (time2 - time1) / (1000 * 3600 * 24);
    }

    /**
     * 秒数转时间
     * 
     * @param second
     * @return
     */
    public static String secToTime(long sec)
    {
        String timeStr = null;
        long hour = 0;
        long minute = 0;
        long second = 0;
        if (sec <= 0)
            return "00分00秒";
        else
        {
            minute = sec / 60;
            if (minute < 60)
            {
                second = sec % 60;
                timeStr = unitFormat(minute) + "分" + unitFormat(second) + "秒";
            }
            else
            {
                hour = minute / 60;
                minute = minute % 60;
                second = sec - hour * 3600 - minute * 60;
                timeStr = hour + "小时" + unitFormat(minute) + "分" + unitFormat(second) + "秒";
            }
        }
        return timeStr;

    }

    /**
     * 秒数转时间
     * 
     * @param second
     * @return
     */
    public static String secToDate(long sec)
    {
        String timeStr = null;
        long day = 0;
        long hour = 0;
        long minute = 0;
        long second = 0;
        if (sec <= 0)
            return "00分00秒";
        else
        {
            minute = sec / 60;
            hour = sec / 3600;
            if (minute < 60)
            {
                second = sec % 60;
                timeStr = unitFormat(minute) + "分" + unitFormat(second) + "秒";
            }
            else if (hour < 24)
            {
                hour = minute / 60;
                minute = minute % 60;
                second = sec - hour * 3600 - minute * 60;
                timeStr = hour + "小时" + unitFormat(minute) + "分" + unitFormat(second) + "秒";
            }
            else
            {
                day = hour / 24;
                hour = hour % 24;
                long s = sec - day * 24 * 3600 - hour * 3600;
                if (s < 60)
                {
                    timeStr = day + "天" + hour + "小时" + unitFormat(minute) + "分" + unitFormat(second) + "秒";
                }
                else
                {
                    minute = s / 60;
                    second = s % 60;
                    timeStr = day + "天" + hour + "小时" + unitFormat(minute) + "分" + unitFormat(second) + "秒";
                }
            }
        }
        return timeStr;
    }

    public static String unitFormat(long i)
    {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + i;
        else retStr = "" + i;
        return retStr;
    }
}
