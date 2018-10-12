/**
 * @file PhotouploadConstants.java
 * @brief 简单描述 - 拍照上传静态变量类 - 版权: Copyright(C) 烽火星空通信发展有限有限公司(Starrysky) - 功能: 本文件归属于【sss】子系统【photoupload】模块 - # 功能描述示例 -
 * 修订历史: - 版本-----月/日/年---修订者----------------------------------------- - v0.9.1.0 02/11/12 zhangenlai - 修改 文件注释规范 -
 * v0.9.0.0 02/03/12 liuqiang 创建
 * @bug TD-xx: 缺陷示例
 */
package com.fiberhome.mapps.trip.utils;

/**
 * @ingroup 拍照上传
 * @author liuqiang
 * @brief 拍照上传静态变量类
 */
public final class PhotouploadConstants
{
	/**
	 * <默认构造函数>
	 */
	private PhotouploadConstants()
	{
	}

	public static final String MODULE_TOUPIAO = "toupiao";//投票
	public static final String MODULE_QIANDAO = "qiandao";//签到
	public static final String MODULE_QINGSHI = "qingshi";//请示
	public static final String MODULE_ZHILING = "zhiling";//指令
	public static final String MODULE_HUODONG = "huodong";//活动
	public static final String MODULE_YONGCHE = "yongche";//用车

	/** 设置分页总数为0 */
	public static final int TOTAL_COUNT = 0;
	/** 地址长度 */
	public static final int ADD_LENGTH = 20;
	/** 空字符串 */
	public static final String EMPTY = "";
	/** 模糊查询 */
	public static final String PERCENT_STR = "%";
	/** 分割符 */
	public static final String TITLE_SPLIT = "-";
	/** 路径分割符 */
	public static final String PATH_SPLIT = "/";
	/** 分割的符号 */
	public static final String INDEX_POINT = ".";
	/** 替换的空字符串 */
	public static final String FILE_EMPTY_STR = " ";
	/** 逗号分割 */
	public static final String INDEX_OF_COMMA = ",";
	/** 拍照对象类型 - 客户 */
	public static final String PHOTO_OBJECT_TYPE_CUSTOMER = "10";
	/** 缓冲区大小 */
	public static final int BUF_SIZE = 1024;
	/** gbk */
	public static final String GBK = "GBK";
	/** 文件名称中间的空格 */
	public static final String FILE_EMPTY = "%20";
	/** 拍照上传类别 */
	public static final String PHOTO_UPLOAD_CODE = "105";
	/** EXCEL sheet */
	public static final String EXCEL_SHEET = "sheet";
	/** 导出失败提示 */
	public static final String EXPORT_FALSE = "false";
	/** 无导出数据提示 */
	public static final String EXPORT_NONE = "none";
	/** 返回toList */
	public static final String RETURN_TO_LIST = "toList";
	/** 后台上传图片路径标识判断 */
	public static final String PHOTO_PATH_ADD_FLAG = "addPath";
	/** 后台读取图片路径标识判断 */
	public static final String PHOTO_PATH_READ_FLAG = "readPath";
	/** 附件名称参数 */
	public static final String FILE_FILE_NAME = "FileName";
	/** WEB-INF */
	public static final String WEB_INF = "WEB-INF";
	/** ISO8859 */
	public static final String ISO8859 = "ISO8859-1";
	/** 图片按日期文件夹存放 */
	public static final String FILE_FORMAT = "yyyyMMdd";
	/** 查询条件 格式时间到时分秒 */
	public static final String FORMAT_HMS = " 00:00:00";
	/** 查询条件 格式时间到时分秒 */
	public static final String FORMAT_HMS_END = " 23:59:59";
	/** 查询条件 上报标题 */
	public static final String PHOTO_NAME = "photoName";
	/** 查询条件 上传人ID */
	public static final String EMPLOYEE_NAME = "employeeName";
	/** 列表排序 */
	public static final String SORT_COLUMNS = "sortColumns";
	/** 错误图片 */
	public static final String ERROR_PHOTO = "no-images.jpg";
	/** 照片存放文件夹 */
	public static final String UPLOAD_FOLDER = "UploadFiles";
	/** 父类机构ID */
	public static final String PARENT_ORG_ID = "parentOrgId";
	/** 附件类型参数 */
	public static final String FILE_CONTENT_TYPE = "ContentType";
	/** 查询条件 上传结束时间 */
	public static final String PHOTO_OBJECT_NAME = "photoObjectName";
	/** 查询条件 上传类型 */
	public static final String PHOTO_OBJECT_TYPE = "photoObjectType";
	/** 格式上传名称防止重复 */
	public static final String DATE_FORMAT_TITLE_SS = "yyyyMMddHHmmss";
	/** 格式上传名称防止重复 */
	public static final String DATE_FORMAT_TITLE_SS_SSS = "yyyyMMddHHmmssSSS";
	/** 父类员工ID */
	public static final String PARENT_EMPLOYEE_ID = "parentEmployeeId";
	/** 时间格式到秒 */
	public static final String DATE_FORMAT_YMDHMS = "yyyy-MM-dd HH:mm:ss";
	/** 查询条件 上传结束时间 */
	public static final String PHOTOUPLOAD_TIME_END = "photoUploadTimeEnd";
	/** 查询条件 上传开始时间 */
	public static final String PHOTOUPLOAD_TIME_BEGIN = "photoUploadTimeBegin";
	/** 读取文件错误后转向路径 */
	public static final String PHOTO_UPLOAD_ERROR = "webfiles/common/skin/images/error/";
	public static final String PHOTO_KIND_NAME = "photoKindName";
	public static final String PHOTO_REMARK = "photoRemark";
	public static final String TEMP_PATH = "photptemp";
}
