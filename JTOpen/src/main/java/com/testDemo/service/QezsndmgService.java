package com.testDemo.service;

import com.ibm.as400.access.AS400;
import com.ibm.as400.data.ProgramCallDocument;
import com.testDemo.config.AS400Config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Auther: zouren
 * @Date: 2019/5/22 14:36
 * @Description:
 */
@Slf4j
@Service
public class QezsndmgService {
    @Autowired
    private AS400Config as400Config;

    public boolean call() throws Exception {
        AS400 as400 = new AS400(as400Config.getSystemName(), as400Config.getUsername(), as400Config.getPassword());

        //QEZSNDMG API
        String programName = "qezsndmg";
        // 构建 ProgramCallDocument 对象
        ProgramCallDocument pcml = new ProgramCallDocument(as400, programName);
        String inputMsgText = "Hello world!";
        String[] userList = new String[]{
                "*SYSOPR",
        };
        int nbrUser = userList.length;
// 设置输入参数
        pcml.setValue("qezsndmg.msgType", "*INFO");
        pcml.setValue("qezsndmg.deliveryMode", "*NORMAL");
        pcml.setValue("qezsndmg.msgText", inputMsgText);
        pcml.setIntValue("qezsndmg.msgLen", inputMsgText.length());
        pcml.setIntValue("qezsndmg.userNum", nbrUser);

        String tempUser = null;
        int[] fieldIndex = new int[1];
        for (int i = 0; i < nbrUser; i++) {
            tempUser = userList[i];
            fieldIndex[0] = i;

            pcml.setValue("qezsndmg.userList", fieldIndex, tempUser);
        }


        pcml.setValue("qezsndmg.showSndMsgDsp", "N");
        // 调用 IBM i 程序
        boolean rc = pcml.callProgram(programName);//与API中的program中的name对应,
        return rc;
    }
}
