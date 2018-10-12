package com.fiberhome.mapps.survey.utils;

import java.net.HttpURLConnection;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

public class HttpRequestSupport
{

    public final static int    TIME_OUT   = 1000 * 60;
    public final static String ROP_ENCODE = "UTF-8";

    public static class TrustAnyTrustManager implements X509TrustManager
    {

        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException
        {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException
        {
        }

        public X509Certificate[] getAcceptedIssuers()
        {
            return new X509Certificate[]
            {};
        }
    }

    public static class TrustAnyHostnameVerifier implements HostnameVerifier
    {
        public boolean verify(String hostname, SSLSession session)
        {
            return true;
        }
    }

    /**
     * @brief 功能:设置请求参数
     * @param conn HttpURLConnection
     * @return
     */
    public void setRequestParameters(HttpURLConnection conn)
    {
        conn.setConnectTimeout(TIME_OUT);
        conn.setReadTimeout(TIME_OUT);
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setRequestProperty("Charset", ROP_ENCODE);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        // conn.setRequestProperty("user-agent",
        // "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
        // 发送POST请求必须设置如下两行
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setUseCaches(false);
    }
}
