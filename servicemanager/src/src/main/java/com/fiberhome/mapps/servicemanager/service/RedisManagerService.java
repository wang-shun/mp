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
import com.fiberhome.mapps.servicemanager.dao.SmRedisMapper;
import com.fiberhome.mapps.servicemanager.entity.ClientRedisInfo;
import com.fiberhome.mapps.servicemanager.entity.SmRedis;
import com.fiberhome.mapps.servicemanager.request.QueryListRequest;
import com.fiberhome.mapps.servicemanager.request.RedisIdRequest;
import com.fiberhome.mapps.servicemanager.request.RedisSaveRequest;
import com.fiberhome.mapps.servicemanager.response.RedisDetailResponse;
import com.fiberhome.mapps.servicemanager.response.RedisListResponse;
import com.fiberhome.mapps.servicemanager.response.RedisTestResponse;
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

import redis.clients.jedis.Jedis;

@ServiceMethodBean
public class RedisManagerService {
	protected final Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	@Autowired
	SmRedisMapper redisMapper;
	
	@ServiceMethod(method = "mapps.servicemanager.redis.list", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public RedisListResponse getRedisList(QueryListRequest req) {
		LOGGER.debug("===获取redis服务接口(mapps.servicemanager.redis.list)入口===");
		RedisListResponse response = new RedisListResponse();
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
		List<ClientRedisInfo> list = redisMapper.getRedisList(map);
		PageInfo<ClientRedisInfo> page = new PageInfo<ClientRedisInfo>(list);
		response.setTotal(page.getTotal());
		if (list == null) {
			list = new ArrayList<ClientRedisInfo>();
		}
		response.setRedisList(list);
		return response;
	}
	
	@ServiceMethod(method = "mapps.servicemanager.redis.detail", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public RedisDetailResponse getDetail(RedisIdRequest req) {
		LOGGER.debug("===获取redis详情接口(mapps.servicemanager.redis.detail)入口===");
		RedisDetailResponse response = new RedisDetailResponse();
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", req.getRedisId());
			SmRedis redisDetail = redisMapper.getRedisDetailById(map);
			redisDetail.setPassword(CodecUtil.aesDecrypt(redisDetail.getPassword(), DBPasswordEncrypt.encryptKey));
			response.setRedisDetail(redisDetail);
			LOGGER.debug("获取redis详情成功");
		} catch (Exception e) {
			LOGGER.error("获取redis详情异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		}
		return response;
	}
	
	@ServiceMethod(method = "mapps.servicemanager.redis.add", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public RedisDetailResponse addRedisDetail(RedisSaveRequest req) {
		LOGGER.debug("===新增redis接口(mapps.servicemanager.redis.add)入口,请求参数==" + req.getRedisJson());
		RedisDetailResponse response = new RedisDetailResponse();
		try
        {
			RedisSaveRequest redisInfo = (RedisSaveRequest) JsonUtil.jsonToObject(req.getRedisJson(),
					RedisSaveRequest.class);
			
			SmRedis redis = redisInfo.getRedis();
			redis.setId(UUID.randomUUID() + "");
			redis.setCreateTime(new Date());
			redis.setCreator(SessionContext.getUserId());
			redis.setPassword(CodecUtil.aesEncrypt(redis.getPassword(), DBPasswordEncrypt.encryptKey));
			redis.setEnabled("1");
			redis.setRemarks(JsEscape.unescape(redis.getRemarks()));
			redisMapper.insertSelective(redis);
            LOGGER.debug("新增redis成功");
            response.setRedisDetail(redis);
		} catch (Exception e) {
			LOGGER.error("新增redis信息异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		}
		return response;
	}
	
	@ServiceMethod(method = "mapps.servicemanager.redis.edit", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public RedisDetailResponse saveDetail(RedisSaveRequest req) {
		LOGGER.debug("===修改redis接口(mapps.servicemanager.redis.edit)入口,请求参数==" + req.getRedisJson());
		RedisDetailResponse response = new RedisDetailResponse();
		try
        {
			RedisSaveRequest redisInfo = (RedisSaveRequest) JsonUtil.jsonToObject(req.getRedisJson(),
					RedisSaveRequest.class);
			
			SmRedis redis = redisInfo.getRedis();
			redis.setEnabled(null);
			redis.setPassword(CodecUtil.aesEncrypt(redis.getPassword(), DBPasswordEncrypt.encryptKey));
			redis.setRemarks(JsEscape.unescape(redis.getRemarks()));
			redisMapper.updateByPrimaryKeySelective(redis);
            LOGGER.debug("修改redis成功");
            response.setRedisDetail(redis);
        }
        catch (Exception e)
        {
            LOGGER.error("修改redis异常：{}", e);
            ErrorCode.fail(response, ErrorCode.CODE_100001);
        }
		return response;
	}
	
	@ServiceMethod(method = "mapps.servicemanager.redis.disable", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public BaseResponse stopRedis(RedisIdRequest req) {
 		LOGGER.debug("===停止redis服务(mapps.servicemanager.redis.disable)===");
		BaseResponse response = new BaseResponse();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", req.getRedisId());
		try{
			redisMapper.disableRedisById(map);
			LOGGER.debug("停止redis服务成功");
		}catch(Exception e){
			LOGGER.error("停止redis服务异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		}
		return response;
	}
	
	@ServiceMethod(method = "mapps.servicemanager.redis.enable", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public BaseResponse startRedis(RedisIdRequest req) {
		LOGGER.debug("===启动redis服务(mapps.servicemanager.redis.enable)===");
		BaseResponse response = new BaseResponse();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", req.getRedisId());
		try{
			redisMapper.enableRedisById(map);
			LOGGER.debug("启动redis服务成功");
		}catch(Exception e){
			LOGGER.error("启动redis服务异常：{}", e);
			ErrorCode.fail(response, ErrorCode.CODE_100001);
		}
		return response;
	}
	
	@ServiceMethod(method = "mapps.servicemanager.redis.testByList", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public RedisTestResponse testRedisByList(RedisIdRequest req) {
		LOGGER.debug("===测试列表redis服务接口(mapps.servicemanager.redis.testByList)===");
		RedisTestResponse response = new RedisTestResponse();
		String password = "";
		try{
			SmRedis redis = redisMapper.selectByPrimaryKey(req.getRedisId());
			password = CodecUtil.aesDecrypt(redis.getPassword(), DBPasswordEncrypt.encryptKey);
			Jedis jedis = new Jedis(redis.getHost(),redis.getPort().intValue()); 
//			if(!"".equals(password)){
				jedis.auth(password);
//			}
			jedis.connect();
			response.setRedisTestResult("success");
			jedis.close();
			LOGGER.debug("测试列表redis服务成功");
		}catch(Exception e){
			String errMsg = e.getMessage();
			if(errMsg.indexOf("no password is set") > -1 && "".equals(password)){
				LOGGER.debug("测试详情redis服务成功");
				return response;
			}else{
				LOGGER.error("测试详情redis服务异常：{}", e);
				ErrorCode.fail(response, ErrorCode.CODE_300013);
			}
		}
		return response;
	}
	
	@ServiceMethod(method = "mapps.servicemanager.redis.test", group = "servicemanager", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public RedisTestResponse testRedis(RedisSaveRequest req) {
		LOGGER.debug("===测试详情redis服务接口(mapps.servicemanager.redis.test),入口参数==="+req.getRedisJson());
		RedisTestResponse response = new RedisTestResponse();
		String password = "";
		try{
			RedisSaveRequest redisInfo = (RedisSaveRequest) JsonUtil.jsonToObject(req.getRedisJson(),
					RedisSaveRequest.class);
			
			SmRedis redis = redisInfo.getRedis();
			password = redis.getPassword();
			Jedis jedis = new Jedis(redis.getHost(),redis.getPort().intValue()); 
//			if(!"".equals(password)){
				jedis.auth(password);
//			}
			jedis.connect();
			response.setRedisTestResult("success");
			jedis.close();
			LOGGER.debug("测试详情redis服务成功");
		}catch(Exception e){
			String errMsg = e.getMessage();
			if(errMsg.indexOf("no password is set") > -1 && "".equals(password)){
				LOGGER.debug("测试详情redis服务成功");
				return response;
			}else{
				LOGGER.error("测试详情redis服务异常：{}", e);
				ErrorCode.fail(response, ErrorCode.CODE_300013);
			}
		}
		return response;
	}
}
