package com.testDemo.service;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.data.PcmlException;
import com.ibm.as400.data.ProgramCallDocument;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Auther: zouren
 * @Date: 2019/5/23 10:47
 * @Description: 下面的示例使用程序调用
 * API pcml示例为QSYRUSRI.pcml
 */
@Slf4j
@Service
public class DemoPCML {
    @Autowired
    private ToolboxService toolboxService;

    public void QSYRUSRI() {
        {
            AS400 as400System; // com.ibm.as400.access.AS400
            ProgramCallDocument pcml; // com.ibm.as400.data.ProgramCallDocument
            boolean rc = false; // Return code from ProgramCallDocument.callProgram()
            String msgId, msgText; // Messages returned from the server
            Object value; // Return value from ProgramCallDocument.getValue()
            try {
                // Construct AS400 without parameters, user will be prompted
                as400System = toolboxService.getAS400();
// Uncomment the following to get debugging information
//com.ibm.as400.data.PcmlMessageLog.setTraceEnabled(true);
                System.out.println("Beginning PCML Example..");
                System.out.println(" Constructing ProgramCallDocument for QSYRUSRI API...");
// Construct ProgramCallDocument
// First parameter is system to connect to
// Second parameter is pcml resource name. In this example,
// serialized PCML file "qsyrusri.pcml.ser" or
// PCML source file "qsyrusri.pcml" must be found in the classpath.
                pcml = new ProgramCallDocument(as400System, "qsyrusri");
// Set input parameters. Several parameters have default values
// specified in the PCML source. Do not need to set them using Java code.
                System.out.println(" Setting input parameters...");
                pcml.setValue("qsyrusri.receiverLength",
                        new Integer((pcml.getOutputsize("qsyrusri.receiver"))));
// Request to call the API
// User will be prompted to sign on to the system
                System.out.println(" Calling QSYRUSRI API requesting information for the sign-on user.");
                rc = pcml.callProgram("qsyrusri");
// If return code is false, we received messages from the server
                if (rc == false) {
// Retrieve list of server messages
                    AS400Message[] msgs = pcml.getMessageList("qsyrusri");
// Iterate through messages and write them to standard output
                    for (int m = 0; m < msgs.length; m++) {
                        msgId = msgs[m].getID();
                        msgText = msgs[m].getText();
                        System.out.println(" " + msgId + " - " + msgText);
                    }
                    System.out.println("** Call to QSYRUSRI failed. See messages above **");
                    System.exit(0);
                }
// Return code was true, call to QSYRUSRI succeeded
// Write some of the results to standard output
                else {
                    value = pcml.getValue("qsyrusri.receiver.bytesReturned");
                    System.out.println(" Bytes returned: " + value);
                    value = pcml.getValue("qsyrusri.receiver.bytesAvailable");
                    System.out.println(" Bytes available: " + value);
                    value = pcml.getValue("qsyrusri.receiver.userProfile");
                    System.out.println(" Profile name: " + value);
                    value = pcml.getValue("qsyrusri.receiver.previousSignonDate");
                    System.out.println(" Previous signon date:" + value);
                    value = pcml.getValue("qsyrusri.receiver.previousSignonTime");
                    System.out.println(" Previous signon time:" + value);
                }
            } catch (Exception e) {
                System.out.println(e.getLocalizedMessage());
                e.printStackTrace();
                System.out.println("*** Call to QSYRUSRI failed. ***");
                System.exit(0);
            }
            System.exit(0);
        }
    }


}
