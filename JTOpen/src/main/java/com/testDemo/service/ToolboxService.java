package com.testDemo.service;

import com.ibm.as400.access.*;
import com.testDemo.config.AS400Config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Enumeration;

/**
 * @Auther: zouren
 * @Date: 2019/5/22 11:28
 * @Description:
 */
@Slf4j
@Service
public class ToolboxService {
    @Autowired
    private AS400Config as400Config;

    /**
     * 使用 Toolbox 建立连接
     *
     * @return
     */
    public AS400 getAS400() throws Exception {
        AS400 as400 = new AS400(as400Config.getSystemName(), as400Config.getUsername(), as400Config.getPassword());
        //获取 IBM i 系统信息
        log.info("SystemName={};VRM={};UserId={};Ccsid={};", as400.getSystemName(), as400.getVRM(), as400.getUserId(), as400.getCcsid());
        return as400;
    }

    /**
     * 获取系统运行信息
     *
     * @throws Exception
     */
    public void systemINFO() throws Exception {
        AS400 as400 = getAS400();
        // 获取系统运行信息
        SystemStatus systemStatus = new SystemStatus(as400);
        // 回当前正在运行的批处理作业数量
        log.info("getBatchJobsRunning:{}", systemStatus.getBatchJobsRunning());
    }

    /**
     * Job 类用于表示 IBM i 系统上的一个作业，它的定义中包含了作业所需要的各种参数和信息，我们可
     * 以利用 Job 类提供的各种方法，对作业进行查询、管理，信息收集
     *
     * @return
     */
    public Job getJobInfo() throws Exception {
        AS400 as400 = getAS400();
        Job job = new Job(as400, as400Config.getJobName(), as400Config.getUsername(), as400Config.getPassword());
        job.loadInformation();
        log.info("getStatus={};CurrentLibrary={};CPUUsed={};FunctionName={};", job.getStatus(), job.getCurrentLibrary(), job.getCPUUsed(), job.getFunctionName());
        log.info("JobActiveDate={};JobDescription={};LoggingLevel={};LanguageID={};", job.getJobActiveDate(), job.getJobDescription(), job.getLoggingLevel(), job.getLanguageID());
        log.info("Name={};Number={};Queue={};Subsystem={};User={}", job.getName(), job.getNumber(), job.getQueue(), job.getSubsystem(), job.getUser());
        return job;
    }

    /**
     * 使用 Job 类对作业进行控制、修改和管理
     * @throws Exception
     */
    public void chageJob() throws Exception {
        Job job = getJobInfo();
//设立一个缓存把对作业的修改先保存在缓存当中，等到需要提交修改的时候再提交出去，使修改生效
        job.setCacheChanges(true);
// 作业的优先级
        job.setRunPriority(66);
        // 日期格式
        job.setDateFormat("*YMD");
        job.commitChanges();
        job.setCacheChanges(false);
        //可以立即生效
        // 日志等级
        job.setLoggingLevel(4);
        job.hold(true);
        job.release();
    }

    /**
     * 选出所有用户名的作业
     */
    public  void jobListInfo(){
        try{
            AS400 as400 = getAS400();
            JobList jobList = new JobList(as400);
            jobList.addJobSelectionCriteria(JobList.SELECTION_USER_NAME,as400Config.getJobuser());
            Enumeration list = jobList.getJobs();
            while(list.hasMoreElements()) {
                Job job = (Job) list.nextElement();
                log.info("name={},number={}",job.getName(),job.getNumber());
            }
            log.info("Total: {}",jobList.getLength());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * JobLog 类可以用来获得系统中一个作业的日志
     */
    public void jobLog(){
        try{
            AS400 as400 = getAS400();
            // 初初初 JobLog 初初初初初初初初初
            JobLog jlog = new JobLog(as400, as400Config.getJobName(), as400Config.getJobName(), as400Config.getJobNumber());
            Enumeration messageList = jlog.getMessages();
            while(messageList.hasMoreElements()) {
                AS400Message message =
                        (AS400Message) messageList.nextElement();
                System.out.println(message.getText());
                log.info(message.getText());
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

    }
    public void command()throws Exception{
        AS400 as400 = getAS400();
        CommandCall cmd = new CommandCall(as400);
        cmd.run("CRTLIB TBXSAMPLE");
        AS400Message[] messageList = cmd.getMessageList();
        for (int i = 0; i < messageList.length; i++) {
            log.info(messageList[i].getText());
        }
    }



}
