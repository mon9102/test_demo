package com.testDemo.service;

import com.testDemo.JTOpenApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Auther: zouren
 * @Date: 2019/5/22 14:55
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JTOpenApplication.class)
public class ToolboxServiceTest {
    @Autowired
    ToolboxService toolboxService;
    @Test
    public void getAS400(){
        try {
            toolboxService.getAS400();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void getJobInfo(){
        try {
            toolboxService.getJobInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void chageJob(){
        try {
            toolboxService.chageJob();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void command(){
        try {
            toolboxService.command();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void jobListInfo(){
        try {
            toolboxService.jobListInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void jobLog(){
        try {
            toolboxService.jobLog();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void systemINFO(){
        try {
            toolboxService.systemINFO();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
