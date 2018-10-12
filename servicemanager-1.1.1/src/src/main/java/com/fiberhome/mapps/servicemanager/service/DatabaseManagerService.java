package com.fiberhome.mapps.servicemanager.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.intergration.session.SessionContext;
import com.fiberhome.mapps.intergration.utils.CodecUtil;
import com.fiberhome.mapps.servicemanager.dao.SmDatabaseMapper;
import com.fiberhome.mapps.servicemanager.entity.ClientDatabaseInfo;
import com.fiberhome.mapps.servicemanager.entity.SmDatabase;
import com.fiberhome.mapps.servicemanager.exception.DBUserExistException;
import com.fiberhome.mapps.servicemanager.request.DatabaseCreateRequest;
import com.fiberhome.mapps.servicemanager.request.DatabaseDetailRequest;
import com.fiberhome.mapps.servicemanager.request.DatabaseSaveRequest;
import com.fiberhome.mapps.servicemanager.request.QueryListRequest;
import com.fiberhome.mapps.servicemanager.response.DatabaseDetailResponse;
import com.fiberhome.mapps.servicemanager.response.DatabaseListResponse;
import com.fiberhome.mapps.servicemanager.response.DatabaseTestResponse;
import com.fiberhome.mapps.servicemanager.utils.ErrorCode;
import com.fiberhome.mapps.servicemanager.utils.JsEscape;
import com.fiberhome.mapps.servicemanager.utils.JsonUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.StringUtil;
import com.rop.annotation.IgnoreSignType;
import com.rop.annotation.NeedInSessionType;
import com.rop.annotation.ServiceMethod;
import com.rop.annotation.ServiceMethodBean;

@ServiceMethodBean
public class DatabaseManagerService {
	protected final Logger LOGGER = LoggerFactory.getLogger(getClass());
	@Autowired
	SmDatabaseMapper databaseMapper;
	
	@Autowired
	DatabaseOperationService dbos;
	
	//数据库密码加解密 秘钥
	@Autowired
	DBPasswordEncrypt dbpe;
	
	@ServiceMethod(method = "mapps.servicemanager.database.list", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public DatabaseListResponse getList(QueryListRequest req) {
		LOGGER.debug("===获取数据库服务接口(mapps.servicemanager.database.list)入口===");
		DatabaseListResponse response = new DatabaseListResponse();
		Map<String, Object> map = new HashMap<String, Object>();
		if (req.getIsenabled() != null) {
			map.put("isenabled", req.getIsenabled());
		}
		if (req.getKeyword() != null) {
			map.put("keyword", "%" + req.getKeyword() + "%");
		}
		if (StringUtil.isNotEmpty(req.getSort()))
        {
            String sort = "";
            if (req.getSort().contains("dbType"))
            {
                sort = req.getSort().replaceAll("dbType", "db_type");
            }
            else
            {
                sort = req.getSort();
            }
            map.put("sort", sort);
        }
		PageHelper.startPage(req.getOffset(), req.getLimit());
		List<ClientDatabaseInfo> list = databaseMapper.getDatabaseList(map);
		PageInfo<ClientDatabaseInfo> page = new PageInfo<ClientDatabaseInfo>(list);
		response.setTotal(page.getTotal());
		if (list == null) {
			list = new ArrayList<ClientDatabaseInfo>();
		}
		response.setDatabases(list);
		return response;
	}

	@ServiceMethod(method = "mapps.servicemanager.database.detail", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public DatabaseDetailResponse getDetail(DatabaseDetailRequest req) {
		LOGGER.debug("===获取数据库服务详情接口(mapps.servicemanager.database.detail)入口===");
		DatabaseDetailResponse response = new DatabaseDetailResponse();
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("databaseId", req.getDatabaseId());
			SmDatabase databaseDetail = databaseMapper.getDatabaseDetailById(map);
			databaseDetail.setPassword(CodecUtil.aesDecrypt(databaseDetail.getPassword(), DBPasswordEncrypt.encryptKey));
			response.setDatabaseDetail(databaseDetail);
			LOGGER.debug("获取数据库服务详情成功");
		} catch (Exception e) {
			LOGGER.error("获取数据库服务详情异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		}
		return response;
	}
	
	@ServiceMethod(method = "mapps.servicemanager.database.add", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public DatabaseDetailResponse addDetail(DatabaseSaveRequest req) {
		LOGGER.debug("===新增数据库服务接口(mapps.servicemanager.database.add)入口,请求参数==" + req.getDatabaseJson());
		DatabaseDetailResponse response = new DatabaseDetailResponse();
		try
        {
			DatabaseSaveRequest databaseInfo = (DatabaseSaveRequest) JsonUtil.jsonToObject(req.getDatabaseJson(),
					DatabaseSaveRequest.class);
			
			SmDatabase database = databaseInfo.getDatabase();
			database.setId(UUID.randomUUID() + "");
			database.setCreateTime(new Date());
			database.setCreator(SessionContext.getUserId());
			if(database.getDbType().equals("postgresql")){
				database.setDbName(req.getSidordbname());
			}else if(database.getDbType().equals("oracle")){
				database.setSid(req.getSidordbname());
			}
			database.setPassword(CodecUtil.aesEncrypt(database.getPassword(), DBPasswordEncrypt.encryptKey));
			database.setRemarks(JsEscape.unescape(database.getRemarks()));
			databaseMapper.insertSelective(database);
            LOGGER.debug("新增服务成功");
		} catch (Exception e) {
			LOGGER.error("保存服务信息异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		}
		return response;
	}
	
	@ServiceMethod(method = "mapps.servicemanager.database.save", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public DatabaseDetailResponse saveDetail(DatabaseSaveRequest req) {
		LOGGER.debug("===保存数据库服务接口(mapps.servicemanager.database.save)入口,请求参数==" + req.getDatabaseJson());
		DatabaseDetailResponse response = new DatabaseDetailResponse();
		try
        {
			DatabaseSaveRequest databaseInfo = (DatabaseSaveRequest) JsonUtil.jsonToObject(req.getDatabaseJson(),
					DatabaseSaveRequest.class);
			
			SmDatabase database = databaseInfo.getDatabase();
			if(database.getDbType().equals("postgresql")){
				database.setDbName(req.getSidordbname());
			}else if(database.getDbType().equals("oracle")){
				database.setSid(req.getSidordbname());
			}
			database.setEnabled(null);
			database.setPassword(CodecUtil.aesEncrypt(database.getPassword(), DBPasswordEncrypt.encryptKey));
			database.setRemarks(JsEscape.unescape(database.getRemarks()));
			databaseMapper.updateByPrimaryKeySelective(database);
            LOGGER.debug("保存服务成功");
        }
        catch (Exception e)
        {
            LOGGER.error("保存服务信息异常：{}", e);
            ErrorCode.fail(response, ErrorCode.CODE_100001);
        }
		return response;
	}
	
	@ServiceMethod(method = "mapps.servicemanager.database.create", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public DatabaseDetailResponse createDb(DatabaseCreateRequest req) {
		LOGGER.debug("===创建数据库服务接口(mapps.servicemanager.database.create)入口,请求参数==" + req.getDatabaseJson()+"==="+req.getSidordbname()+"==="+req.getAdminuser()+"==="+req.getAdminpass());
		DatabaseDetailResponse response = new DatabaseDetailResponse();
		try
        {
			DatabaseCreateRequest databaseInfo = (DatabaseCreateRequest) JsonUtil.jsonToObject(req.getDatabaseJson(),
					DatabaseCreateRequest.class);
			
			SmDatabase database = databaseInfo.getDatabase();
			//数据库驱动  
	        String driverClass = "";
	        //数据库连接  
	        String conUrl = "";
			if(database.getDbType().equals("postgresql")){
				database.setDbName(req.getSidordbname());
				driverClass="org.postgresql.Driver";  
		        conUrl="jdbc:postgresql://" + database.getHost() + ":" + database.getPort() + "/postgres";
		        dbos.createPostgresql(driverClass, conUrl, req.getAdminuser(), req.getAdminpass(), database.getUserName(), database.getPassword(),database.getDbName());
			}else if(database.getDbType().equals("oracle")){
				database.setSid(req.getSidordbname());
				driverClass="oracle.jdbc.driver.OracleDriver";  
		        conUrl="jdbc:oracle:thin:@" + database.getHost() + ":" + database.getPort() + ":" + database.getSid();
		        dbos.createOracle(driverClass, conUrl, req.getAdminuser(), req.getAdminpass(), database.getUserName(), database.getPassword());
			}
            LOGGER.debug("创建服务成功");
        }
		catch (DBUserExistException dbe)
        {
            LOGGER.error("创建服务异常：{}", dbe);
            ErrorCode.fail(response, ErrorCode.CODE_180002);
        }
        catch (Exception e)
        {
            LOGGER.error("创建服务异常：{}", e);
            ErrorCode.fail(response, ErrorCode.CODE_180001);
            response.setError_message(response.getError_message()+": <br/>"+e.getMessage());
        }
		return response;
	}
	
	@ServiceMethod(method = "mapps.servicemanager.database.stop", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public BaseResponse stopDatabase(DatabaseDetailRequest req) {
		LOGGER.debug("===停止数据库服务===");
		BaseResponse response = new BaseResponse();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("databaseId", req.getDatabaseId());
		try{
			databaseMapper.stopDatabase(map);
			LOGGER.debug("停止数据库服务成功");
		}catch(Exception e){
			LOGGER.error("停止数据库服务异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_180001);
		}
		return response;
	}
	
	@ServiceMethod(method = "mapps.servicemanager.database.start", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public BaseResponse startDatabase(DatabaseDetailRequest req) {
		LOGGER.debug("===启动数据库服务===");
		BaseResponse response = new BaseResponse();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("databaseId", req.getDatabaseId());
		try{
			databaseMapper.startDatabase(map);
			LOGGER.debug("启动数据库服务成功");
		}catch(Exception e){
			LOGGER.error("启动数据库服务异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_180001);
		}
		return response;
	}
	
	@ServiceMethod(method = "mapps.servicemanager.database.testByList", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public DatabaseDetailResponse testByList(DatabaseDetailRequest req) {
		LOGGER.debug("===数据库列表服务测试接口(mapps.servicemanager.database.testByList)入口===");
		DatabaseDetailResponse response = new DatabaseDetailResponse();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("databaseId", req.getDatabaseId());
		SmDatabase database = databaseMapper.getDatabaseDetailById(map);
		try{
			//数据库驱动  
	        String driverClass = "";
	        //数据库连接  
	        String conUrl = "";
			if(database.getDbType().equals("postgresql")){
				driverClass="org.postgresql.Driver";  
		        conUrl="jdbc:postgresql://" + database.getHost() + ":" + database.getPort() + "/"+ database.getDbName();
		        dbos.testPostgresql(driverClass, conUrl,  database.getUserName(), CodecUtil.aesDecrypt(database.getPassword(), DBPasswordEncrypt.encryptKey));
			}else if(database.getDbType().equals("oracle")){
				driverClass="oracle.jdbc.driver.OracleDriver";  
		        conUrl="jdbc:oracle:thin:@" + database.getHost() + ":" + database.getPort() + ":" + database.getSid();
		        dbos.testOracle(driverClass, conUrl,  database.getUserName(), CodecUtil.aesDecrypt(database.getPassword(), DBPasswordEncrypt.encryptKey));
		        LOGGER.debug("测试数据库成功");
			}       
        }catch (Exception dbtfe){
            LOGGER.warn("无法连接到指定数据库：{}", dbtfe);
            ErrorCode.fail(response, ErrorCode.CODE_180003);
        }
	
		return response;
	}
	
	@ServiceMethod(method = "mapps.servicemanager.database.test", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public DatabaseTestResponse testDatabase(DatabaseSaveRequest req) {
		LOGGER.debug("===数据库服务测试接口(mapps.servicemanager.database.test)入口===");
		DatabaseTestResponse response = new DatabaseTestResponse();
		try
        {
			DatabaseSaveRequest databaseInfo = (DatabaseSaveRequest) JsonUtil.jsonToObject(req.getDatabaseJson(),
					DatabaseSaveRequest.class);
			
			SmDatabase database = databaseInfo.getDatabase();
			//数据库驱动  
	        String driverClass = "";
	        //数据库连接  
	        String conUrl = "";
			if(database.getDbType().equals("postgresql")){
				database.setDbName(req.getSidordbname());
				driverClass="org.postgresql.Driver";  
		        conUrl="jdbc:postgresql://" + database.getHost() + ":" + database.getPort() + "/"+ database.getDbName();
		        dbos.testPostgresql(driverClass, conUrl,  database.getUserName(), database.getPassword());
			}else if(database.getDbType().equals("oracle")){
				database.setSid(req.getSidordbname());
				driverClass="oracle.jdbc.driver.OracleDriver";  
		        conUrl="jdbc:oracle:thin:@" + database.getHost() + ":" + database.getPort() + ":" + database.getSid();
		        dbos.testOracle(driverClass, conUrl,  database.getUserName(), database.getPassword());
		        LOGGER.debug("测试数据库成功");
			}       
        }catch (Exception dbtfe){
            LOGGER.error("无法连接到指定数据库：{}", dbtfe);
            ErrorCode.fail(response, ErrorCode.CODE_180003);
        }

		return response;
		}
}
