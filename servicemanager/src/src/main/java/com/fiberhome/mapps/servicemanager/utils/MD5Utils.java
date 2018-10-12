package com.fiberhome.mapps.servicemanager.utils;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MD5Utils
{
    private static Logger          LOG           = LoggerFactory.getLogger(MD5Utils.class);
    protected static char          hexDigits[]   =
    {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
    };
    protected static MessageDigest messagedigest = null;
    static
    {
        try
        {
            messagedigest = MessageDigest.getInstance("MD5");
        }
        catch (NoSuchAlgorithmException nsaex)
        {
            LOG.error(MD5Utils.class.getName() + "初始化失败，MessageDigest不支持MD5Util。", nsaex.getMessage());
        }
    }

    public static String getFileMD5String(File file) throws IOException
    {
        FileInputStream in = new FileInputStream(file);
        FileChannel ch = in.getChannel();
        MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
        messagedigest.update(byteBuffer);
        in.close();
        return bufferToHex(messagedigest.digest());
    }

    public static String getMD5String(String s)
    {
        if (s == null || s.trim().length() == 0)
        {
            return "";
        }
        return getMD5String(s.getBytes());
    }

    public static String getMD5String(byte[] bytes)
    {
        java.security.MessageDigest md5 = null;
        try
        {
            md5 = MessageDigest.getInstance("MD5");
            byte[] result = md5.digest(bytes);
            return byte2String(result);
        }
        catch (Exception e)
        {
            LOG.error(e.getMessage());
            return null;
        }
    }

    private static String bufferToHex(byte bytes[])
    {
        return bufferToHex(bytes, 0, bytes.length);
    }

    private static String bufferToHex(byte bytes[], int m, int n)
    {
        StringBuffer stringbuffer = new StringBuffer(2 * n);
        int k = m + n;
        for (int l = m; l < k; l++)
        {
            appendHexPair(bytes[l], stringbuffer);
        }
        return stringbuffer.toString();
    }

    private static void appendHexPair(byte bt, StringBuffer stringbuffer)
    {
        char c0 = hexDigits[(bt & 0xf0) >> 4];
        char c1 = hexDigits[bt & 0xf];
        stringbuffer.append(c0);
        stringbuffer.append(c1);
    }

    public static boolean checkPassword(String password, String md5PwdStr)
    {
        String s = getMD5String(password);
        return s.equals(md5PwdStr);
    }

    /**
     * byte2String
     * 
     * @param in 字节数组
     * @return string
     */
    private static String byte2String(byte[] in)
    {
        DataInputStream data = new DataInputStream(new ByteArrayInputStream(in));
        String str = "";
        try
        {
            for (int j = 0; j < in.length; j++)
            {
                String tmp = Integer.toHexString(data.readUnsignedByte());
                if (tmp.length() == 1)
                {
                    tmp = "0" + tmp;
                }
                str = str + tmp;
            }
        }
        catch (Exception ex)
        {
            LOG.error(ex.getMessage());
        }
        return str;
    }
    /*
     * public static void main(String[] args) throws IOException{ long begin =System.currentTimeMillis();
     * //2EA3E66AC37DF7610F5BD322EC4FFE48 670M 11s kuri双核1.66G 2G内存 File big = new
     * File("D:/a_workspace/mos_old/pom.xml"); String md5=getFileMD5String(big); long end =System.currentTimeMillis();
     * System.out.println("md5:   "+md5+ "    time:"+((end-begin)/1000)+"s"); }
     */
    
    public static String convertMD5(String inStr)
    {

        char[] a = inStr.toCharArray();
        for (int i = 0; i < a.length; i++)
        {
            a[i] = (char) (a[i] ^ 't');
        }
        String s = new String(a);
        return s;

    }
}