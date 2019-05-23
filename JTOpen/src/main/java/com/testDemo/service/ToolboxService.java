package com.testDemo.service;

import com.ibm.as400.access.*;
import com.testDemo.config.AS400Config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Enumeration;

import static jdk.nashorn.internal.runtime.regexp.joni.Syntax.Java;

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
     *
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
    public void jobListInfo() {
        try {
            AS400 as400 = getAS400();
            JobList jobList = new JobList(as400);
            jobList.addJobSelectionCriteria(JobList.SELECTION_USER_NAME, as400Config.getJobuser());
            Enumeration list = jobList.getJobs();
            while (list.hasMoreElements()) {
                Job job = (Job) list.nextElement();
                log.info("name={},number={}", job.getName(), job.getNumber());
            }
            log.info("Total: {}", jobList.getLength());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * JobLog 类可以用来获得系统中一个作业的日志
     */
    public void jobLog() {
        try {
            AS400 as400 = getAS400();
            // 初初初 JobLog 初初初初初初初初初
            JobLog jlog = new JobLog(as400, as400Config.getJobName(), as400Config.getJobName(), as400Config.getJobNumber());
            Enumeration messageList = jlog.getMessages();
            while (messageList.hasMoreElements()) {
                AS400Message message =
                        (AS400Message) messageList.nextElement();
                System.out.println(message.getText());
                log.info(message.getText());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void command() throws Exception {
        AS400 as400 = getAS400();
        CommandCall cmd = new CommandCall(as400);
        cmd.run("CRTLIB TBXSAMPLE");
        AS400Message[] messageList = cmd.getMessageList();
        for (int i = 0; i < messageList.length; i++) {
            log.info(messageList[i].getText());
        }
    }

    public void JPing() throws Exception {
        AS400 as400 = getAS400();
        AS400JPing jPing = new AS400JPing(as400Config.getSystemName(), AS400.COMMAND, false);
        if (jPing.ping()) {
            log.info("SUCCESS");
        } else {
            log.info("FAILED");
        }
    }

    /**
     * 下面的示例在名为chtsvr01的集群哈希表服务器上操作。它假设一个集群
     * 群集哈希表服务器已处于活动状态。它打开一个连接，生成一个键，放入一个条目
     * 使用集群哈希表中的新键，从集群哈希表中获取一个条目，然后关闭连接。
     */
    public void ClusteredHashTable() throws Exception {

        ClusteredHashTableEntry myEntry = null;
        String myData = new String("This is my data.");
        log.info("Data to be stored: " + myData);
        AS400 system = getAS400();
        ClusteredHashTable cht = new ClusteredHashTable(system, "CHTSVR01");
// Open a connection.
        cht.open();
// Get a key to the hash table.
        byte[] key = null;
        key = cht.generateKey();
// Prepare some data that you want to store into the hash table.
// ENTRY_AUTHORITY_ANY_USER means that any user can access the
// entry in the clustered hash table.
// DUPLICATE_KEY_FAIL means that if the specified key already exists,
// the ClusteredHashTable.put() request will not succeed.
        int timeToLive = 500;
        myEntry = new ClusteredHashTableEntry(key, myData.getBytes(), timeToLive,
                ClusteredHashTableEntry.ENTRY_AUTHORITY_ANY_USER,
                ClusteredHashTableEntry.DUPLICATE_KEY_FAIL);
// Store (or put) the entry into the hash table.
        cht.put(myEntry);
// Get an entry from the hash table.
        ClusteredHashTableEntry output = cht.get(key);
// Close the connection.
        cht.close();
    }

    /**
     * 使用连接池共享连接并管理到IBMi系统的连接集（池）。
     * 例如，应用程序可以从池中检索连接，使用它，然后将其返回到池中重新使用。
     */
    public void pool() {
        try {
// Create an AS400ConnectionPool.
            AS400ConnectionPool testPool = new AS400ConnectionPool();
// Set a maximum of 128 connections to this pool.
            testPool.setMaxConnections(128);
// Set a maximum lifetime for 30 minutes for connections.
            testPool.setMaxLifetime(1000 * 60 * 30); // 30 min Max lifetime since created.
// Preconnect 5 connections to the AS400.COMMAND service.
            testPool.fill(as400Config.getSystemName(), as400Config.getUsername(), as400Config.getPassword(), AS400.COMMAND, 1);
            System.out.println();
            System.out.println("Preconnected 1 connection to the AS400.COMMAND service");
// Call getActiveConnectionCount and getAvailableConnectionCount to see how many
// connections are in use and available for a particular system.
            System.out.println("Number of active connections: "
                    + testPool.getActiveConnectionCount(as400Config.getSystemName(), as400Config.getUsername()));
            System.out.println("Number of available connections for use: "
                    + testPool.getAvailableConnectionCount(as400Config.getSystemName(), as400Config.getUsername()));
// Create a connection to the AS400.COMMAND service. (Use the service number
// constants defined in the AS400 class (FILE, PRINT, COMMAND, DATAQUEUE, and so on.))
// Since connections have already been filled, the usual time spent connecting
// to the command service is avoided.
            AS400 newConn1 = testPool.getConnection(as400Config.getSystemName(), as400Config.getUsername(), as400Config.getPassword(), AS400.COMMAND);
            System.out.println();
            System.out.println("getConnection gives out an existing connection to user");
            System.out.println("Number of active connections: "
                    + testPool.getActiveConnectionCount(as400Config.getSystemName(), as400Config.getUsername()));
            System.out.println("Number of available connections for use: "
                    + testPool.getAvailableConnectionCount(as400Config.getSystemName(), as400Config.getUsername()));
// Create a new command call object and run a command.
            CommandCall cmd1 = new CommandCall(newConn1);
            cmd1.run("CRTLIB FRED");
// Return the connection to the pool.
            testPool.returnConnectionToPool(newConn1);
            System.out.println();
            System.out.println("Returned a connection to pool");
            System.out.println("Number of active connections: "
                    + testPool.getActiveConnectionCount(as400Config.getSystemName(), as400Config.getUsername()));
            System.out.println("Number of available connections for reuse: "
                    + testPool.getAvailableConnectionCount(as400Config.getSystemName(), as400Config.getUsername()));
// Create a connection to the AS400.COMMAND service. This will return the same
// object as above for reuse.
            AS400 newConn2 = testPool.getConnection(as400Config.getSystemName(), as400Config.getUsername(), AS400.COMMAND);
            System.out.println();
            System.out.println("getConnection gives out an existing connection to user");
            System.out.println("Number of active connections: "
                    + testPool.getActiveConnectionCount(as400Config.getSystemName(), as400Config.getUsername()));
            System.out.println("Number of available connections for reuse: "
                    + testPool.getAvailableConnectionCount(as400Config.getSystemName(), as400Config.getUsername()));
// Create a connection to the AS400.COMMAND service. This will create a new
// connection as there are not any connections in the pool to reuse.
            AS400 newConn3 = testPool.getConnection(as400Config.getSystemName(), as400Config.getUsername(), AS400.COMMAND);
            System.out.println();
            System.out.println("getConnection creates a new connection because there are no connections available");
            System.out.println("Number of active connections: "
                    + testPool.getActiveConnectionCount(as400Config.getSystemName(), as400Config.getUsername()));
            System.out.println("Number of available connections for reuse: "
                    + testPool.getAvailableConnectionCount(as400Config.getSystemName(), as400Config.getUsername()));
// Close the test pool.
            testPool.close();
        } catch (Exception e) {
// If any of the above operations failed say the pool operations failed
// and output the exception.
            System.out.println("Pool operations failed");
            System.out.println(e);
            e.printStackTrace();
        }
    }

    /**
     * IBM I数据区域的抽象基类
     * 这个基类有四个子类，它们支持：字符数据、十进制数据、逻辑数据，以及包含字符数据的本地数据区域。
     * CharacterDataArea、DecimalDataArea、LocalDataArea、LogicalDataArea
     * 还有 DataAreaEvent和DataAreaListener功能
     */
    public void dataArea() {
        try {
            AS400 system = getAS400();
// Create a DecimalDataArea object.
            QSYSObjectPathName path = new QSYSObjectPathName("MYLIB", "MYDATA", "DTAARA");
            //CharacterDataArea
            DecimalDataArea dataArea = new DecimalDataArea(system, path.getPath());
// Create the decimal data area on the server using default values.
            dataArea.create();
// Clear the data area.
            dataArea.clear();
// Write to the data area.
            dataArea.write(new BigDecimal("1.2"));
            // Read from the data area.
            BigDecimal data = dataArea.read();
// Delete the data area from the server.
            dataArea.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * AS400DataType是一个定义数据转换所需方法的接口。java当需要转换单个数据块时，程序使用数据类型。存在转换类对于以下类型的数据
     * Numeric、Text (character)、Composite (numeric and text)
     */
    public void DataTypes() {
        As400ToJavaInt();
        JavaIntTos400();
        textdata();
    }

    private void As400ToJavaInt() {
        // Create a buffer to hold the system data type. Assume the buffer is
// filled with numeric data in the system format by data queues,
// program call, and so on.
        byte[] data = new byte[100];
// Create a converter for this system data type.
        AS400Bin4 bin4Converter = new AS400Bin4();
// Convert from system type to Java object. The number starts at the
// beginning of the buffer.
        Integer intObject = (Integer) bin4Converter.toObject(data, 0);
// Extract the simple Java type from the Java object.
        int i = intObject.intValue();
    }

    private void JavaIntTos400() {

        // Create a Java object that contains the value to convert.
        Integer intObject = new Integer(22);
// Create a converter for the system data type.
        AS400Bin4 bin4Converter = new AS400Bin4();
// Convert from Java object to system data type.
        byte[] data = bin4Converter.toBytes(intObject);
// Find out how many bytes of the buffer were filled with the
// system value.
        int length = bin4Converter.getByteLength();
    }

    private void textdata() {
// Assume the data queue work has already been done to
// retrieve the text from the system and the data has been
// put in the following buffer.
        int textLength = 100;
        byte[] data = new byte[textLength];
// Create a converter for the system data type. Note a default
// converter is being built. This converter assumes the IBM i
// EBCDIC code page matches the client’s locale. If this is not
// true the Java program can explicitly specify the EBCDIC
// CCSID to use. However, it is recommended that you specify a
// CCSID whenever possible (see the Notes: below).
        AS400Text textConverter = new AS400Text(textLength);
// Note: Optionally, you can create a converter for a specific
// CCSID. Use an AS400 object in case the program is running
// as an IBM Toolbox for Java proxy client.
        int ccsid = 37;
//        AS400 system =getAS400();; // AS400 object
//        AS400Text textConverter = new AS400Text(textLength, ccsid, system);
// Note: You can also create a converter with just the AS400 object.
// This converter assumes the IBM i code page matches
// the CCSID returned by the AS400 object.
//        AS400Text textConverter = new AS400Text(textLength, system);
// Convert the data from EBCDIC to Unicode. If the length of
// the AS400Text object is longer than the number of
// converted characters, the resulting String will be
// blank-padded out to the specified length.
        String javaText = (String) textConverter.toObject(data);
    }

    /**
     * 下面的示例显示从Java结构到字节数组的转换并再次返回。这个
     * 示例假设发送和接收数据使用相同的数据格式。
     */
    private void bytes() {
// Create a structure of data types that corresponds to a structure
// that contains: - a four-byte number
// - four bytes of pad
// - an eight-byte number
// - 40 characters
        AS400DataType[] myStruct =
                {
                        new AS400Bin4(),
                        new AS400ByteArray(4),
                        new AS400Float8(),
                        new AS400Text(40)
                };
// Create a conversion object using the structure.
        AS400Structure myConverter = new AS400Structure(myStruct);
// Create the Java object that holds the data to send to the server.
        Object[] myData =
                {
                        new Integer(88), // the four-byte number
                        new byte[0], // the pad (let the conversion object 0 pad)
                        new Double(23.45), // the eight-byte floating point number
                        "This is my structure" // the character string
                };
        // Convert from Java object to byte array.
        byte[] myAS400Data = myConverter.toBytes(myData);
// ... send the byte array to the server. Get data back from the
// server. The returned data will also be a byte array.
// Convert the returned data from IBM i to Java format.
        Object[] myRoundTripData = (Object[]) myConverter.toObject(myAS400Data, 0);
// Pull the third object out of the structure. This is the double.
        Double doubleObject = (Double) myRoundTripData[2];
// Extract the simple Java type from the Java object.
        double d = doubleObject.doubleValue();
    }

}
