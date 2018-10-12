package com.fiberhome.mapps.dbr.utils;

public class VersionUtil
{
    /**
     * 格式化 应用版本分解 的版本号
     * 
     * @param version 版本号
     * @param y 分几组
     * @param z 每组几位
     * @return String 应用版本分解
     */
    public static String getFmtVersion(String version, int y, int z)
    {
        StringBuffer rs = new StringBuffer();
        String[] pas = version.split("\\.");
        int n = pas.length;
        // 按.分割 多余的组忽略
        for (int i = 0; i < y; i++)
        {
            if (i != 0)
            {
                rs.append(".");
            }
            if (i < n)
            {
                String member = pas[i];
                // 非数字成员替换
                member = member.replaceAll("\\D", "");
                // System.out.println(member);
                if (z - member.length() > 0)
                {
                    for (int j = 0; j < (z - member.length()); j++)
                    {
                        rs.append("0");
                    }
                    rs.append(member);
                }
                else
                {
                    rs.append(member.substring(0, z));
                }
            }
            else
            {
                for (int j = 0; j < z; j++)
                {
                    rs.append("0");
                }
            }
        }
        return rs.toString();
    }

    /**
     * 格式化 应用版本分解 的版本号
     * 
     * @param version 版本号
     * @return String 应用版本分解
     */
    public static String getFmtVersion(String version)
    {
        return getFmtVersion(version, 8, 8);
    }
}
