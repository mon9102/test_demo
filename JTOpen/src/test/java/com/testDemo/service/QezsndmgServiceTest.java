package com.testDemo.service;

import com.testDemo.JTOpenApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Auther: zouren
 * @Date: 2019/5/22 14:52
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JTOpenApplication.class)
public class QezsndmgServiceTest {
    @Autowired
    QezsndmgService qezsndmgService;
    @Test
    public void call(){
        try {
            qezsndmgService.call();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
