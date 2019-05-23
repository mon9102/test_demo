package com.testDemo.service;

import com.ibm.as400.access.AS400;
import com.ibm.as400.data.ProgramCallDocument;
import com.testDemo.JTOpenApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.InputStream;

/**
 * @Auther: zouren
 * @Date: 2019/5/23 11:15
 * @Description:
 */
@Slf4j
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = JTOpenApplication.class)
public class DemoPCMLTest {
    @Autowired
    private DemoPCML demoPCML;
    @Autowired
    private ToolboxService toolboxService;
    @Test
    public void QSYRUSRI(){
        demoPCML.QSYRUSRI();
    }
    @Test
    public void testFile(){
        String a = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        System.out.println(a);
        InputStream s = Thread.currentThread().getContextClassLoader().getResourceAsStream("/qsyrusri.pcml");
        System.out.println(s.toString());
//        try {
//            AS400 as400System = new AS400();
//
//            ProgramCallDocument pcml = new ProgramCallDocument(as400System, "/qsyrusri");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
