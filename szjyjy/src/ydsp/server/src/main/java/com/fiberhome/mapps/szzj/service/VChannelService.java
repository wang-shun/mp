package com.fiberhome.mapps.szzj.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.szzj.dao.VChannelMapper;
import com.fiberhome.mapps.szzj.entity.ChannelUser;
import com.fiberhome.mapps.szzj.entity.VChannel;
import com.fiberhome.mapps.szzj.entity.VChannelDetail;
import com.fiberhome.mapps.szzj.request.AddVChannelRequest;
import com.fiberhome.mapps.szzj.request.DeleteVChannelRequest;
import com.fiberhome.mapps.szzj.request.EndMeetingRequest;
import com.fiberhome.mapps.szzj.request.QueryVChannelListRequest;
import com.fiberhome.mapps.szzj.response.AddVChannelResponse;
import com.fiberhome.mapps.szzj.response.DetailVChannelResponse;
import com.fiberhome.mapps.szzj.response.QueryChannelResponse;
import com.fiberhome.mapps.szzj.utils.DateUtil;
import com.fiberhome.mapps.szzj.utils.ErrorCode;
import com.fiberhome.mapps.szzj.utils.JsonUtil;
import com.fiberhome.mapps.szzj.utils.LogUtil;
import com.fiberhome.mos.core.openapi.rop.client.ClientException;
import com.fiberhome.mos.core.openapi.rop.client.RestClient;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rop.annotation.IgnoreSignType;
import com.rop.annotation.NeedInSessionType;
import com.rop.annotation.ServiceMethod;
import com.rop.annotation.ServiceMethodBean;



@ServiceMethodBean(version = "1.0")
public class VChannelService {
	
	@Autowired
	VChannelMapper channelMapper;
	
    protected final Logger    LOGGER            = LoggerFactory.getLogger(getClass());
    
    @Value("${api.key}")
    private String api_key;
    @Value("${api.secret}")
    private String api_secret;
    @Value("${api.getaccountreport}")
    private String getAccountReportUrl;
    @Value("${api.getmeetinginfo}")
    private String getMeetingInfoUrl;
    @Value("${api.endmeeting}")
    public String endMeetingUrl;
    
    /**
     * 分页列表查询
     * @param req
     * @return
     */
    @ServiceMethod(method = "mapps.channel.query", group = "member", groupTitle = "视频通道列表", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public QueryChannelResponse queryVChannelList(QueryVChannelListRequest req){
        LOGGER.info("查询视频接口(mapps.channel.query)入口,请求参数==" + LogUtil.getObjectInfo(req));
        QueryChannelResponse res = new QueryChannelResponse();
        try {
        	Map<String, Object> map = new HashMap<String, Object>();
        	if(!StringUtils.isEmpty(req.getChannel())){
        		map.put("channel", req.getChannel());
        	}
        	if(!StringUtils.isEmpty(req.getSort())){
        		map.put("sort", req.getSort());
        	}
        	PageHelper.startPage(req.getOffset(), req.getLimit());
    		List<VChannelDetail> channelList = channelMapper.queryChannelList(map);
    		PageInfo<VChannelDetail> page = new PageInfo<VChannelDetail>(channelList);
    		
    		Map<String, Object> reqmap = new HashMap<String, Object>();
    		reqmap.put("api_key", api_key);
    		reqmap.put("api_secret", api_secret);
    		reqmap.put("from", "2016-01-01");
    		reqmap.put("to", DateUtil.sdf.format(new Date()));
    		RestClient client = new RestClient(getAccountReportUrl, "", Locale.SIMPLIFIED_CHINESE);
    		String result = client.requestForString(reqmap);
    		System.out.println(result);
    		JSONObject resultJson = JSONObject.parseObject(result);
    		String userString = resultJson.getString("users");
			List<ChannelUser>  ChannelUserList = (List<ChannelUser>) JsonUtil.jsonToObject(userString, ChannelUser.class);
			String meetingResult = "";
			if(channelList.size()>0){
    			for (VChannelDetail channelDetail : channelList) {
					if(ChannelUserList.size()>0){
						for (ChannelUser channelUser : ChannelUserList) {
							if(channelDetail.getHostId().equals(channelUser.getUser_id())){
								channelDetail.setMeetings(channelUser.getMeetings());
								channelDetail.setMeetingMinutes(channelUser.getMeeting_minutes());
							}
						}
					}
					if(!StringUtils.isEmpty(channelDetail.getMeetingId())){
						Map<String, Object> getMeetingInfomap = new HashMap<String, Object>();
						getMeetingInfomap.put("api_key", api_key);
						getMeetingInfomap.put("api_secret", api_secret);
						getMeetingInfomap.put("id", channelDetail.getMeetingId());
						getMeetingInfomap.put("host_id",channelDetail.getHostId());
			    		RestClient meetClient = new RestClient(getMeetingInfoUrl, "", Locale.SIMPLIFIED_CHINESE);
			    		meetingResult = meetClient.requestForString(getMeetingInfomap);
			    		resultJson = JSONObject.parseObject(meetingResult);
			    		channelDetail.setStatus(resultJson.getString("status"));
					}
					
					
				}
    		}
    		
            res.setTotal(page.getTotal());
            res.setChannelList(channelList);
            res.setTimestamp(req.getTimestamp());
            if (page.isIsLastPage())
            {
                res.setEndflag(1);
            }
            else
            { 
                res.setEndflag(0);
            }
            res.success();
            LOGGER.info("分页查询成功");
		} catch (Exception e) {
			e.printStackTrace();
            LOGGER.error("分页查询信息异常：{}", e);
            ErrorCode.fail(res, ErrorCode.CODE_100001);
		}
		
		
		return res;
		
	}
    
    /**
     * 新增
     * @param req
     * @return
     */
    @ServiceMethod(method = "mapps.channel.add", group = "member", groupTitle = "新增视频通道", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public AddVChannelResponse addChannel(AddVChannelRequest req){
        LOGGER.info("新增视频接口(mapps.channel.add)入口,请求参数==" + LogUtil.getObjectInfo(req));
    	AddVChannelResponse res = new AddVChannelResponse();
    	try {
    		Map<String, Object> map = new HashMap<String, Object>();
    		map.put("hostId", req.getHostId());
        	List<VChannelDetail> channelList  = channelMapper.queryChannelList(map);
        	if(channelList.size()>0){
        		res.setCode("102");
        		res.setError_message("帐号已存在");
        		return res;
        	}
        	VChannel channel = new VChannel();
        	channel.setHostId(req.getHostId());
        	channel.setChannel(req.getChannel());
        	channel.setHostEmail(req.getHostEmail());
        	channel.setHostPass(req.getHostPass());
        	channel.setCapacity(req.getCapacity());
        	channel.setCreateTime(new Date());
        	channelMapper.insert(channel);
        	res.success();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("新增视频通道异常：{}", e);
            ErrorCode.fail(res, ErrorCode.CODE_100001);
		}
    	return res;
    }
    
    /**
     * 删除
     * @param req
     * @return
     */
    @ServiceMethod(method = "mapps.channel.delete", group = "member", groupTitle = "删除视频通道", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.NO)
    public BaseResponse deleteChannle(DeleteVChannelRequest req){
        LOGGER.info("删除视频接口(mapps.channel.delete)入口,请求参数==" + LogUtil.getObjectInfo(req));
    	BaseResponse res = new BaseResponse();
    	try {
    		VChannel channel = new VChannel();
        	channel.setHostId(req.getHostId());
        	channelMapper.delete(channel);
        	res.success();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("删除视频通道异常：{}", e);
            ErrorCode.fail(res, ErrorCode.CODE_100001);
		}
    	return res;
    }
    /**
     * 
     * @param req
     * @return
     */
    @ServiceMethod(method = "mapps.channel.edit", group = "member", groupTitle = "编辑视频通道", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.NO)
    public AddVChannelResponse editChannel(AddVChannelRequest req){
        LOGGER.info("编辑视频接口(mapps.channel.edit)入口,请求参数==" + LogUtil.getObjectInfo(req));
    	AddVChannelResponse res = new AddVChannelResponse();
    	try {
    		VChannel channel = new VChannel();
    		channel.setHostId(req.getHostId());
        	channel.setChannel(req.getChannel());
        	channel.setHostEmail(req.getHostEmail());
        	channel.setHostPass(req.getHostPass());
        	channel.setCapacity(req.getCapacity());
        	channelMapper.updateByPrimaryKeySelective(channel);
        	res.success();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("新增视频通道异常：{}", e);
            ErrorCode.fail(res, ErrorCode.CODE_100001);
		}
    	return res;
    }
    /**
     * 视频通道详情接口
     * @param req
     * @return
     */
    @ServiceMethod(method = "mapps.channel.detail", group = "member", groupTitle = "新增视频通道", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public DetailVChannelResponse getChannelDetail(DeleteVChannelRequest req){
        LOGGER.info("视频通道详情接口(mapps.channel.detail)入口,请求参数==" + LogUtil.getObjectInfo(req));
    	DetailVChannelResponse res = new DetailVChannelResponse();
    	try {
    		VChannel channel = new VChannel();
    		channel.setHostId(req.getHostId());
    		channel = channelMapper.selectOne(channel);
    		res.setCapacity(channel.getCapacity());
    		res.setChannel(channel.getChannel());
    		res.setHostId(channel.getHostId());
    		res.setHostEmail(channel.getHostEmail());
    		res.setHostPass(channel.getHostPass());
    		res.success();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("获取视频通道详情异常：{}", e);
            ErrorCode.fail(res, ErrorCode.CODE_100001);

		}
    	return res;
    }
    /**
     * 结束会议
     * @param req
     * @return
     */
    @ServiceMethod(method = "mapps.channel.endMeeting", group = "member", groupTitle = "结束会议", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public BaseResponse endMeeting(EndMeetingRequest req){
        LOGGER.info("结束会议接口(mapps.channel.endMeeting)入口,请求参数==" + LogUtil.getObjectInfo(req));
    	BaseResponse res = new BaseResponse();
    	Map<String, Object> reqmap = new HashMap<String, Object>();
		reqmap.put("api_key", api_key);
		reqmap.put("api_secret", api_secret);
		reqmap.put("id",req.getMeetingId());
		reqmap.put("host_id",req.getHostId());
		RestClient client = new RestClient(endMeetingUrl, "", Locale.SIMPLIFIED_CHINESE);
		String result = "";
		try {
			result = client.requestForString(reqmap);
			JSONObject resultJson = JSONObject.parseObject(result);
			if(resultJson.getJSONObject("error")==null){
				res.success();
			}else{
				res.setCode(resultJson.getJSONObject("error").getString("code"));
				res.setError_message(resultJson.getJSONObject("error").getString("message"));
			}
			
		} catch (ClientException e) {
			e.printStackTrace();
			LOGGER.error("结束会议异常：{}", e);
            ErrorCode.fail(res, ErrorCode.CODE_100001);
		}
    	
    	return res;
    	
    }
}
