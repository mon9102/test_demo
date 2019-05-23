package com.testDemo.service;

import com.testDemo.JTOpenApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Auther: zouren
 * @Date: 2019/5/23 11:15
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JTOpenApplication.class)
public class DemoPCMLTest {
    @Autowired
    private DemoPCML demoPCML;
    @Test
    private void QSYRUSRI(){
        demoPCML.QSYRUSRI();
    }
}
