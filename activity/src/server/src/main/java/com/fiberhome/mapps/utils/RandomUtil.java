package com.fiberhome.mapps.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author Administrator
 */
public class RandomUtil
{
    @SuppressWarnings("serial")
    public final static Map<Integer, String> imageMap = new HashMap<Integer, String>()
    {
        {
            put(1, "1.jpg");
            put(2, "2.jpg");
            put(3, "3.jpg");
            put(4, "4.jpg");
            put(5, "5.jpg");
            put(6, "6.jpg");
            put(7, "7.jpg");
            put(8, "8.jpg");
            put(9, "9.jpg");
        }
    };

    public static String getDefaultImage()
    {
        Random random = new Random();
        return imageMap.get(random.nextInt(9) + 1);
    }
}
