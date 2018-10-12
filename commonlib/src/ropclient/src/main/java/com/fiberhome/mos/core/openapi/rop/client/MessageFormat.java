package com.fiberhome.mos.core.openapi.rop.client;

public enum MessageFormat
{
    xml, bin, json, jsonp, json_gzip, xml_gzip;
    public static MessageFormat getFormat(String value)
    {
        if ("json".equalsIgnoreCase(value))
        {
            return json;
        } else if ("jsonp".equalsIgnoreCase(value))
        {
            return jsonp;
        }else if ("bin".equalsIgnoreCase(value))
        {
            return bin;
        }else if("json_gzip".equalsIgnoreCase(value)){
        	
        	return json_gzip;
        }else if("xml_gzip".equalsIgnoreCase(value)){
        	
        	return xml_gzip;
        }else
        {
            return xml;
        }
    }

    public static boolean isValidFormat(String value)
    {
        return xml.name().equalsIgnoreCase(value) || bin.name().equalsIgnoreCase(value)|| json_gzip.name().equalsIgnoreCase(value)
        		|| xml_gzip.name().equalsIgnoreCase(value) || json.name().equalsIgnoreCase(value)|| jsonp.name().equalsIgnoreCase(value);
    }
}
