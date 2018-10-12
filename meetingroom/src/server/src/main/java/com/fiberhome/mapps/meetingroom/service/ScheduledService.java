package com.fiberhome.mapps.meetingroom.service;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;

import com.fiberhome.mapps.contact.pojo.MyDepartment;
import com.fiberhome.mapps.contact.service.MplusAccessService;
import com.fiberhome.mapps.meetingroom.dao.MrPrivilegeMapper;
import com.fiberhome.mapps.meetingroom.dao.MrReservedMapper;
import com.fiberhome.mapps.meetingroom.entity.MrPrivilege;
import com.rop.annotation.ServiceMethodBean;

import tk.mybatis.mapper.util.StringUtil;

@ServiceMethodBean(version = "1.0")
public class ScheduledService
{
    @Autowired
    MrPrivilegeMapper          mrPrivilegeMapper;
    @Autowired
    MrReservedMapper           mrReservedMapper;
    @Autowired
    MplusAccessService         accessService;
    protected final Logger     LOGGER              = LoggerFactory.getLogger(getClass());

    public static final String PRIVILEGE_TYPE_DEPT = "dept";
    @Value("${meetingroom.clearDataByDay}")
    Integer                    days;

    /**
     * 同步权限表中部门的dept_order
     * 
     * @throws Exception
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void syncPrivilegeDeptOrder() throws Exception
    {
        clearData();
        LOGGER.info("开始执行权限表dept_order的同步");
        MrPrivilege mf = new MrPrivilege();
        // 获取涉及到的机构ecid
        List<String> ecidList = mrPrivilegeMapper.getAllEcid(null);
        // 获取mplus 的机构列表
        Map<String, String> map = accessService.getOrg();
        if (map != null)
        {
            for (String ecid : ecidList)
            {
                LOGGER.info("开始执行的机构ecid:{}", ecid);
                String orgUuid = map.get(ecid);
                // 获取mplus 机构下的部门列表
                List<MyDepartment> deptList = accessService.getDepartments(orgUuid);
                // 数据转存map
                Map<String, MyDepartment> deptMap = new HashMap<String, MyDepartment>();
                for (MyDepartment deptInfo : deptList)
                {
                    deptMap.put(deptInfo.getDepUuid(), deptInfo);
                }
                // 查询单机构数据
                mf.setType(PRIVILEGE_TYPE_DEPT);
                mf.setEcid(ecid);
                List<MrPrivilege> list = mrPrivilegeMapper.select(mf);
                // deptorder发生变化的修改记录
                for (MrPrivilege privInfo : list)
                {
                    MyDepartment mdInfo = deptMap.get(privInfo.getEntityId());
                    String newDeptName = mdInfo.getDepName();
                    String newDeptOrder = mdInfo.getDepOrder();
                    MrPrivilege record = new MrPrivilege();
                    if (!privInfo.getDeptOrder().equals(newDeptOrder))
                    {
                        LOGGER.info("deptorder=数据库:{},同步源:{}", privInfo.getDeptOrder(), newDeptOrder);
                        record.setId(privInfo.getId());
                        record.setDeptOrder(newDeptOrder);
                    }
                    if (!privInfo.getEntityName().equals(newDeptName))
                    {
                        LOGGER.info("deptname=数据库:{},同步源:{}", privInfo.getEntityName(), newDeptName);
                        record.setId(privInfo.getId());
                        record.setEntityName(newDeptName);
                    }
                    if (StringUtil.isNotEmpty(record.getId()))
                    {
                        LOGGER.info("修改权限记录,id:{}", record.getId());
                        mrPrivilegeMapper.updateByPrimaryKeySelective(record);
                    }
                }
            }
        }
        LOGGER.info("权限表dept_order的同步完成");
    }

    @Async
    public void clearData()
    {
        if (days != null)
        {
            LOGGER.info("开始数据清理,days:{}", days);
            Map<String, Object> map = new HashMap<String, Object>();
            Calendar nowTime = Calendar.getInstance();
            nowTime.add(Calendar.DATE, -days);
            map.put("now", nowTime.getTime());
            mrReservedMapper.clearData(map);
        }
        else
        {
            LOGGER.info("未配置数据清理");
        }
    }
}
