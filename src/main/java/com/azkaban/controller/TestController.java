package com.azkaban.controller;

/**
 * @program: taskDemo
 * @description: hello,world
 * @author: yhj
 * @create: 2021-01-11 15:06
 **/

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.azkaban.api.AzkabanApi;
import com.azkaban.azkabanResponse.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController{

    @Autowired
    private AzkabanApi azkabanApi;

    private static final Logger LOG = LoggerFactory.getLogger(TestController.class);

    //创建项目
    @RequestMapping(value = "/createProject",method = RequestMethod.POST)
    public JSONObject createProject(@RequestBody JSONObject createParam){
        JSONObject result = new JSONObject();
        String name = createParam.getString("name");
        String desc = createParam.getString("desc");
        BaseResponse response = azkabanApi.createProject(name,desc);
        result.put("status",response.getStatus());
        result.put("message",response.getMessage());
        return result;
    }

    //删除项目
    @RequestMapping(value = "/deleteProject",method = RequestMethod.POST)
    public JSONObject deleteProject(@RequestBody JSONObject deleteParam){
        JSONObject result = new JSONObject();
        String name = deleteParam.getString("name");
        BaseResponse response = azkabanApi.deleteProject(name);
        result.put("status","success");
        return result;
    }

    //上传zip包
    @RequestMapping(value = "/uploadProjectZip",method = RequestMethod.POST)
    public JSONObject uploadProjectZip(@RequestBody JSONObject uploadParam){
        JSONObject result = new JSONObject();
        String projectName = uploadParam.getString("projectName");
        String filePath = uploadParam.getString("filePath");
        ProjectZipResponse response = azkabanApi.uploadProjectZip(filePath,projectName);
        result.put("projectId",response.getProjectId());
        result.put("version",response.getVersion());
        return result;
    }

    //获取项目流
    @RequestMapping(value = "/fetchProjectFlows",method = RequestMethod.POST)
    public JSONObject fetchProjectFlows(@RequestBody JSONObject uploadParam){
        JSONObject result = new JSONObject();
        String projectName = uploadParam.getString("projectName");
        FetchFlowsResponse response = azkabanApi.fetchProjectFlows(projectName);
        result.put("project",response.getProject());
        result.put("projectId",response.getProjectId());
        result.put("flows",response.getFlows());
        return result;
    }

    //fixme 自由添加参数
    //单次执行项目
    @RequestMapping(value = "/executeFlow",method = RequestMethod.POST)
    public JSONObject executeFlow(@RequestBody JSONObject executeParam){
        JSONObject result = new JSONObject();
        String projectName = executeParam.getString("projectName");
        String flowName = executeParam.getString("flowName");
        ExecuteFlowResponse response = azkabanApi.executeFlow(projectName,flowName);
        result.put("project",response.getProject());
        result.put("flow",response.getFlow());
        result.put("execid",response.getExecid());
        return result;
    }

    //获取全部项目
    @RequestMapping(value = "/fetchAllProjects",method = RequestMethod.GET)
    public JSONObject fetchAllProjects(){
        JSONObject result = new JSONObject();
        FetchAllProjectsResponse response = azkabanApi.fetchAllProjects();
        JSONArray projectList = JSONArray.parseArray(JSON.toJSONString(response.getProjects()));
        result.put("project",projectList);
        return result;
    }

    //设置定时任务
    @RequestMapping(value = "/scheduleCronFlow",method = RequestMethod.POST)
    public JSONObject scheduleCronFlow(@RequestBody JSONObject scheduleParam){
        JSONObject result = new JSONObject();
        String projectName = scheduleParam.getString("projectName");
        String flowName = scheduleParam.getString("flowName");
        String cronExpression = scheduleParam.getString("cronExpression");
        ScheduleCronFlowResponse response = azkabanApi.scheduleCronFlow(projectName,flowName,cronExpression);
        String scheduleId = response.getScheduleId();
        result.put("scheduleId",scheduleId);
        return result;
    }

    //查询单项目定时任务
    @RequestMapping(value = "/fetchSchedule",method = RequestMethod.POST)
    public JSONObject fetchSchedule(@RequestBody JSONObject fetchScheduleParam){
        String projectId = fetchScheduleParam.getString("projectId");
        String flowId = fetchScheduleParam.getString("flowId");
        FetchScheduleResponse response = azkabanApi.fetchSchedule(projectId,flowId);
        Schedule schedule = response.getSchedule();
        return JSONObject.parseObject(JSONObject.toJSONString(schedule));
    }

    //查询Flow的执行记录
    @RequestMapping(value = "/fetchFlowExecutions",method = RequestMethod.POST)
    public JSONObject fetchFlowExecutions(@RequestBody JSONObject fetchFlowExecutionsParam){
        String projectName = fetchFlowExecutionsParam.getString("projectName");
        String flowName = fetchFlowExecutionsParam.getString("flowName");
        int start = Integer.parseInt(fetchFlowExecutionsParam.getString("start"));
        int length = Integer.parseInt(fetchFlowExecutionsParam.getString("length"));
        FetchFlowExecutionsResponse response = azkabanApi.fetchFlowExecutions(projectName,flowName,start,length);
        return JSONObject.parseObject(JSONObject.toJSONString(response));
    }

    //查询具体Flow的执行详情
    @RequestMapping(value = "/fetchExecFlow",method = RequestMethod.POST)
    public JSONObject fetchExecFlow(@RequestBody JSONObject fetchExecFlowParam){
        String execId = fetchExecFlowParam.getString("execId");
        FetchExecFlowResponse response = azkabanApi.fetchExecFlow(execId);
        return JSONObject.parseObject(JSONObject.toJSONString(response));
    }

    //取消Flow执行,未测试
    @RequestMapping(value = "/cancelFlow",method = RequestMethod.POST)
    public JSONObject cancelFlow(@RequestBody JSONObject cancelFlowParam){
        String execId = cancelFlowParam.getString("execId");
        BaseResponse response = azkabanApi.cancelFlow(execId);
        JSONObject result = new JSONObject();
        result.put("status",response.getStatus());
        result.put("message",response.getMessage());
        return result;
    }

    //移除定时
    @RequestMapping(value = "/removeSchedule",method = RequestMethod.POST)
    public JSONObject removeSchedule(@RequestBody JSONObject removeScheduleParam){
        String scheduleId = removeScheduleParam.getString("scheduleId");
        BaseResponse response = azkabanApi.removeSchedule(scheduleId);
        JSONObject result = new JSONObject();
        result.put("status",response.getStatus());
        result.put("message",response.getMessage());
        return result;
    }
}
