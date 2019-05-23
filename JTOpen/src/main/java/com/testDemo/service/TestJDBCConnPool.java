package com.testDemo.service;
import java.awt.TextArea;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Vector;
import java.util.Properties;

import java.sql.Connection;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.naming.*;
import java.util.Date;
import java.util.ArrayList;
import java.util.Random;
import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400JDBCManagedConnectionPoolDataSource;
import com.ibm.as400.access.AS400JDBCManagedDataSource;
import com.ibm.as400.access.Trace;


public class TestJDBCConnPool
{

    private static final boolean DEBUG = false;

    // If you turn this flag on, be sure to also turn on the following flag:
    //   AS400JDBCConnection.TESTING_THREAD_SAFETY.
    private static final boolean TESTING_THREAD_SAFETY = false;

    private static String userid;
    private static String password;
    private static String host;

    // Note: For consistency, all time values are stored units of milliseconds.
    private int initialPoolSize_;  // initial # of connections in pool
    private int minPoolSize_;      // max # of connections in pool
    private int maxPoolSize_;      // max # of connections in pool
    private long maxLifetime_;     // max lifetime (msecs) of connections in pool
    private long maxIdleTime_;     // max idle time (msecs) of available connections in pool
    private long propertyCycle_;   // pool maintenance frequency (msecs)

    private int numDaemons_;       // # of requester daemons to create
    private static long timeToRunDaemons_; // total duration (msecs) to let the daemons run
    private long daemonMaxSleepTime_;  // max time (msecs) for requester daemons to sleep each cycle
    private long daemonMinSleepTime_;  // min time (msecs) for requester daemons to sleep each cycle
    private long poolHealthCheckCycle_;  // # of msecs between calls to checkPoolHealth()

    private boolean keepDaemonsAlive_ = true;  // When this is false, the daemons shut down.

    private DataSource dataSource_;
    private AS400JDBCManagedDataSource mds_;

    private final Object daemonSleepLock_ = new Object();

    private Random random_ = new Random();

    static
    {
        try {
            Class.forName("com.ibm.as400.access.AS400JDBCDriver");
        }
        catch(Exception e){
            System.out.println("Unable to register JDBC driver.");
            System.exit(0);
        }
    }

    public static void main(String[] args)
    {
        host = args[0];
        userid = args[1];
        password = args[2];
        timeToRunDaemons_ = (new Integer(args[3])).intValue() * 1000; //milliseconds
        //args[3]=time to run in seconds
        TestJDBCConnPool cptest = new TestJDBCConnPool();

        cptest.setup();
        cptest.runTest();
    }

    public void setup()
    {
        try
        {
            if (DEBUG)
                System.out.println("TESTING_THREAD_SAFETY flag is "
                        + (TESTING_THREAD_SAFETY ? "true" : "false"));

            if (TESTING_THREAD_SAFETY)
            {
                // Adjust values for performing thread-intensive stress testing.
                // NOTE: This assumes that the AS400JDBCConnection class has also been modified to
                // not make actual connections to an actual server.
                // To do this, edit AS400JDBCConnection.java, changing its TESTING_THREAD_SAFETY
                // flag to 'false', and recompile.
                minPoolSize_ = 100;
                maxPoolSize_ = 190;
                initialPoolSize_ = 150;  // this should get reset to maxPoolSize_
                numDaemons_ = 75;
                if (timeToRunDaemons_ == 0) {
                    timeToRunDaemons_ = 180*1000; // 180 seconds == 3 minutes
                }
            }
            else
            {  // Set more conservative values, as we'll be making actual connections to an
                // actual server, and we don't want to monopolize the server.
                minPoolSize_ = 5;
                maxPoolSize_ = 15;
                initialPoolSize_ = 9;
                numDaemons_ = 4;
                if (timeToRunDaemons_ == 0) {
                    timeToRunDaemons_ = 15*1000; // 15 seconds
                }
            }
            maxLifetime_ = (int)timeToRunDaemons_ / 3;
            maxIdleTime_ = (int)timeToRunDaemons_ / 4;
            propertyCycle_ = timeToRunDaemons_ / 4;
            poolHealthCheckCycle_ = Math.min(timeToRunDaemons_ / 4, 20*60*1000);
            // at least once every 20 minutes (more frequently if shorter run-time)
            daemonMaxSleepTime_ = Math.min(timeToRunDaemons_ / 3, 10*1000);
            // at most 10 seconds (less if shorter run-time)
            daemonMinSleepTime_ = 20;  // milliseconds

            if (DEBUG)
                System.out.println("setup: Constructing "
                        + "AS400JDBCManagedConnectionPoolDataSource (cpds0)");
            AS400JDBCManagedConnectionPoolDataSource cpds0 =
                    new AS400JDBCManagedConnectionPoolDataSource();

            // Set datasource properties.  Note that both CPDS and MDS have these
            // properties, and they might have different values.
            cpds0.setServerName(host);
            cpds0.setDatabaseName(host);//iasp can be here
            cpds0.setUser(userid);
            cpds0.setPassword(password);

            cpds0.setSavePasswordWhenSerialized(true);

            // Set connection pooling-specific properties.
            cpds0.setInitialPoolSize(initialPoolSize_);
            cpds0.setMinPoolSize(minPoolSize_);
            cpds0.setMaxPoolSize(maxPoolSize_);
            cpds0.setMaxLifetime((int)(maxLifetime_/1000));  // convert to seconds
            cpds0.setMaxIdleTime((int)(maxIdleTime_/1000));  // convert to seconds
            cpds0.setPropertyCycle((int)(propertyCycle_/1000));  // convert to seconds
            //cpds0.setReuseConnections(false);  // don't re-use connections

            // Set the initial context factory to use.
            System.setProperty(Context.INITIAL_CONTEXT_FACTORY,
                    "com.sun.jndi.fscontext.RefFSContextFactory");

            // Get the JNDI Initial Context.
            Context ctx = new InitialContext();

            // Note: The following is an alternative way to set context properties locally:
            //   Properties env = new Properties();
            //   env.put(Context.INITIAL_CONTEXT_FACTORY,
            //           "com.sun.jndi.fscontext.RefFSContextFactory");
            //   Context ctx = new InitialContext(env);

            ctx.rebind("mydatasource", cpds0);
            // We can now do lookups on cpds, by the name"mydatasource".

            if (DEBUG)
                System.out.println("setup: lookup(\"mydatasource\"" + ")");
            // AS400JDBCManagedConnectionPoolDataSource cpds1 =
            //   (AS400JDBCManagedConnectionPoolDataSource)ctx.lookup("mydatasource");
            // if (DEBUG) System.out.println("setup: cpds1.getUser() == |" + cpds1.getUser() + "|");

            // Create a standard DataSource object that references it.

            if (DEBUG)
                System.out.println("setup: Constructing AS400JDBCManagedDataSource (mds0)");
            AS400JDBCManagedDataSource mds0 = new AS400JDBCManagedDataSource();
            mds0.setDescription("DataSource supporting connection pooling");
            mds0.setDataSourceName("mydatasource");
            ctx.rebind("ConnectionPoolingDataSource", mds0);

            if (DEBUG)
                System.out.println("setup: lookup(\"ConnectionPoolingDataSource\"" + ")");
            dataSource_ = (DataSource)ctx.lookup("ConnectionPoolingDataSource");
            //dataSource_.setLogWriter(output_);
            if (DEBUG)
                System.out.println("setup: dataSource_.getUser() == |" +
                        ((AS400JDBCManagedDataSource)dataSource_).getUser() + "|");

            mds_ = (AS400JDBCManagedDataSource)dataSource_;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Setup error during Trace file creation.");
        }
    }

    void displayConnectionType(Connection conn, boolean specifiedDefaultId)
    {
        if (conn instanceof com.ibm.as400.access.AS400JDBCConnectionHandle)
            System.out.print("("+ (specifiedDefaultId ? "+" : "-") + "P)");
        else
            System.out.print("("+ (specifiedDefaultId ? "+" : "-") + "NP)");
    }


    /**
     *  Gets and returns connections from and to a connection pool for a while.
     **/
    public void runTest()
    {
        boolean ok = true;
        try
        {
            System.out.println("Started test run at " + new Date());

            if (DEBUG)
                System.out.println("Checking health just after datasource creation "
                        + "(we expect that the pool does not exist yet) ...");
            if (mds_.checkPoolHealth(true)) {
                ok = false;
                System.out.println("\nERROR: Pool exists prior to first getConnection().");
            }

            // Verify some setters/getters for JDBC properties.
            System.out.println("Verifying setters/getters ...");

            mds_.setAccess("read only");
            if (!mds_.getAccess().equals("read only")) {
                ok = false;
                System.out.println("\nERROR: getAccess() returned unexpected value: "
                        + "|" + mds_.getAccess()+"|");
            }

            boolean oldBool = mds_.isBigDecimal();
            boolean newBool = (oldBool ? false : true);
            mds_.setBigDecimal(newBool);
            if (mds_.isBigDecimal() != newBool) {
                ok = false;
                System.out.println("\nERROR: isBigDecimal() returned unexpected value: "
                        + "|"+mds_.isBigDecimal()+"|");
            }
            mds_.setBigDecimal(oldBool);

            int oldInt = mds_.getBlockCriteria();
            int newInt = (oldInt == 2 ? 1 : 2);
            mds_.setBlockCriteria(newInt);
            if (mds_.getBlockCriteria() != newInt) {
                ok = false;
                System.out.println("\nERROR: getBlockCriteria() returned unexpected value: "
                        + "|"+mds_.getBlockCriteria()+"|");
            }
            mds_.setBlockCriteria(oldInt);

            // Verify some setters and getters for socket properties.

            oldBool = mds_.isKeepAlive();
            newBool = (oldBool ? false : true);
            mds_.setKeepAlive(newBool);
            if (mds_.isKeepAlive() != newBool) {
                ok = false;
                System.out.println("\nERROR: isKeepAlive() returned unexpected value: "
                        + "|"+mds_.isKeepAlive()+"|");
            }
            mds_.setKeepAlive(oldBool);

            oldInt = mds_.getReceiveBufferSize();
            newInt = (oldInt == 256 ? 512 : 256);
            mds_.setReceiveBufferSize(newInt);
            if (mds_.getReceiveBufferSize() != newInt) {
                ok = false;
                System.out.println("\nERROR: getReceiveBufferSize() returned unexpected value: "
                        + "|"+mds_.getReceiveBufferSize()+"|");
            }
            mds_.setReceiveBufferSize(oldInt);


            System.out.println("CONNECTION 1");
            Object o = dataSource_.getConnection();
            System.out.println(o.getClass());
            System.out.println("******LOOK ABOVE*******");
            Connection c1 = dataSource_.getConnection();

            if (DEBUG)
                displayConnectionType(c1, true);

            if (DEBUG)
                System.out.println("Checking health after first getConnection() ...");
            if (!mds_.checkPoolHealth(true)) {
                ok = false;
                System.out.println("\nERROR: Pool is not healthy after first getConnection().");
            }

            if (!TESTING_THREAD_SAFETY)
            {
                try
                {
                    c1.setAutoCommit(false);
                    if (DEBUG)
                        System.out.println("SELECT * FROM QIWS.QCUSTCDT");
                    Statement s = c1.createStatement();
                    ResultSet rs = s.executeQuery("SELECT * FROM QIWS.QCUSTCDT");
                    while(rs.next()){
                        if (DEBUG)
                            System.out.println(rs.getString(2));
                    }
                    rs.close();
                    s.close();
                }
                catch (Exception e) {
                    e.printStackTrace();
                    if (DEBUG)
                        System.out.println("Checking health after fatal connection error ...");
                    if (!mds_.checkPoolHealth(true)) {
                        ok = false;
                        System.out.println("\nERROR: Pool is not healthy after fatal connection "
                                + "error.");
                    }
                }
            }

            System.out.println("CONNECTION 2");
            Connection c2 = dataSource_.getConnection(userid, password);
            if (DEBUG)
                displayConnectionType(c2, false);
            System.out.println("CONNECTION 3");
            Connection c3 = dataSource_.getConnection();
            if (DEBUG)
                displayConnectionType(c3, true);
            c1.close();

            if (DEBUG)
                System.out.println("Checking health after first close() ...");
            if (!mds_.checkPoolHealth(true)) {
                ok = false;
                System.out.println("\nERROR: Pool is not healthy after first close().");
            }

            System.out.println("CONNECTION 4");
            Connection c4 = dataSource_.getConnection();
            if (DEBUG) displayConnectionType(c4, true);

            c1.close();  // close this one again
            c2.close();
            c3.close();
            c4.close();

            if (DEBUG) System.out.println("Checking health after last close() ...");
            if (!mds_.checkPoolHealth(true)) {
                ok = false;
                System.out.println("\nERROR: Pool is not healthy after last close().");
            }

            // Start the test daemons.
            System.out.println("Starting test daemons");
            startThreads();

            // Run the test daemons for a while; check pool health periodically.

            long startTime = System.currentTimeMillis();
            long endTime = startTime + timeToRunDaemons_;
            while (System.currentTimeMillis() < endTime)
            {
                System.out.print("h");
                // Let the daemons run for a while, then check pool health.
                try {
                    Thread.sleep(poolHealthCheckCycle_);
                }
                catch (InterruptedException ie) {}
                if (!mds_.checkPoolHealth(true)) {
                    ok = false;
                    System.out.println("\nERROR: Pool is not healthy after test daemons started.");
                }
            }

            // Stop the test daemons.
            System.out.println("\nStopping test daemons");
            stopThreads();

            if (DEBUG)
                System.out.println("Checking health after connectionGetter daemons have run...");
            if (!mds_.checkPoolHealth(true)) {
                ok = false;
                System.out.println("\nERROR: Pool is not healthy after test daemons stopped.");
            }

            if (!TESTING_THREAD_SAFETY)
            {
                System.out.println("CONNECTION 5");
                Connection c = dataSource_.getConnection();
                if (DEBUG) displayConnectionType(c, true);
                c.setAutoCommit(false);
                if (DEBUG) System.out.println("SELECT * FROM QIWS.QCUSTCDT");
                Statement s = c.createStatement();
                ResultSet rs = s.executeQuery("SELECT * FROM QIWS.QCUSTCDT");
                while(rs.next()){
                    if (DEBUG) System.out.println(rs.getString(2));
                }
                rs.close();
                s.close();
                c.close();
            }

            System.out.println("\nClosing the pool...");
            mds_.closePool();

            if (DEBUG)
                System.out.println("Checking health after pool closed ...");
            Trace.setTraceJDBCOn(true);  // make sure the final stats get printed out
            Trace.setTraceOn(true);
            if (!mds_.checkPoolHealth(true)) {
                ok = false;
                System.out.println("\nERROR: Pool is not healthy after pool closed.");
            }

            System.out.println();
            if(ok==true)
                System.out.println("test ran ok");
            else
                System.out.println("test failed");
        }
        catch (Exception e)
        {
            System.out.println(e);
            e.printStackTrace();
        }
        finally {
            System.out.println("Ended test at " + new Date());
        }
    }

    void startThreads()
    {
        // Create a bunch of threads that call getConnection().
        Thread[] threads = new Thread[numDaemons_];
        for (int i=0; i<numDaemons_; i++)
        {
            ConnectionGetter getter;
            // Flip a coin to see if this daemon will specify the default uid, or unique uid.
            if (random_.nextBoolean())
            {
                getter = new ConnectionGetter(userid,password);
                if (TESTING_THREAD_SAFETY) {  // we can use fictional userid
                    getter = new ConnectionGetter("Thread"+i, "Pwd"+i);
                }
                else {  // must use a real userid
                    getter = new ConnectionGetter(userid,password);
                }
            }
            else
                getter = new ConnectionGetter(null, null);

            threads[i] = new Thread(getter, "["+i+"]");
            threads[i].setDaemon(true);
        }

        // Start the threads.
        for (int i=0; i<numDaemons_; i++)
        {
            threads[i].start();
        }
    }

    void stopThreads()
    {
        // Tell the threads to stop.
        keepDaemonsAlive_ = false;
        synchronized (daemonSleepLock_) {
            daemonSleepLock_.notifyAll();
        }

        // Wait for the daemons to stop.
        try {
            Thread.sleep(3000);
        }
        catch (InterruptedException ie) {}
    }


    // ConnectionGetter --------------------------------------------------------------------

    /**
     Helper class. This daemon wakes up at random intervals and either
     gets another connection from the connection pool or returns a
     previously-gotten connection to the pool.
     **/
    private final class ConnectionGetter implements Runnable
    {
        private String uid_;
        private String pwd_;
        private boolean useDefaultUid_;
        private long maxSleepTime_;
        private String threadName_;
        private boolean firstConnection_ = true;
        ArrayList connections_ = new ArrayList();
        // list of connections that this getter currently 'owns'.

        ConnectionGetter(String uid, String pwd) {
            uid_ = uid;
            pwd_ = pwd;
            if (uid_ == null) useDefaultUid_ = true;
            else useDefaultUid_ = false;
            maxSleepTime_ = daemonMaxSleepTime_;  // our own copy that we can adjust
        }

        public void run()
        {
            threadName_ = Thread.currentThread().getName();
            if (DEBUG) System.out.println("ConnectionGetter("+threadName_+") Starting up");

            try
            {
                while (keepDaemonsAlive_)
                {
                    try
                    {
                        // Pick a random sleep-time, between min and max values.
                        long sleepTime = Math.max((long)(maxSleepTime_ * random_.nextFloat()),
                                daemonMinSleepTime_);
                        // Note: Must never call wait(0), because that waits forever.
                        synchronized (daemonSleepLock_) {
                            try {
                                daemonSleepLock_.wait(sleepTime);
                                System.out.print(".");
                            }
                            catch (InterruptedException ie) {}
                        }
                        if (!keepDaemonsAlive_) break;

                        // Decide by chance whether to request another connection or return a
                        // previously-obtained connection.
                        Connection conn;
                        if (random_.nextBoolean())  // Leave the decision to chance.
                        { // Request another connection.
                            if (useDefaultUid_)
                            {
                                if (DEBUG)
                                    System.out.println("ConnectionGetter("+threadName_+") - get()");
                                conn = dataSource_.getConnection();
                            }
                            else
                            {
                                if (DEBUG)
                                    System.out.println("ConnectionGetter("+threadName_+") - "
                                            + "get("+uid_+",***)");
                                conn = dataSource_.getConnection(uid_, pwd_);
                            }

                            if (conn == null) {
                                System.out.println("ConnectionGetter("+threadName_+") ERROR: "
                                        + "getConnection() returned null");
                            }
                            else
                            {
                                // Occasionally "forget" that we own a connection, and neglect to
                                // close it.
                                // Orphaned connections should eventually exceed their maximum
                                // lifetime and get "reaped" by the connection manager.
                                float val = random_.nextFloat();
                                if (firstConnection_ || val < 0.1) {
                                    // 'orphan' a few gotten connections
                                    firstConnection_ = false;
                                }
                                else {
                                    connections_.add(conn);
                                }
                                if (DEBUG) displayConnectionType(conn, useDefaultUid_);
                                if (conn instanceof com.ibm.as400.access.AS400JDBCConnectionHandle)
                                {  // We got a pooled connection.  Try speeding up our cycle time.
                                    if (maxSleepTime_ > 100)
                                        maxSleepTime_--;
                                    else
                                        maxSleepTime_ = 100;
                                }
                                else
                                {  // We didn't get a pooled connection.  That means that the pool
                                    // must be at capacity.  Slow down our cycle time a bit.
                                    maxSleepTime_ = maxSleepTime_ + 50;
                                }
                            }
                        }
                        else { // Close a connection that we currently own.
                            if (connections_.size() != 0) {
                                conn = (Connection)connections_.remove(0);
                                conn.close();
                            }
                        }
                    }  // inner try
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                } // outer while
            }  // outer try
            finally
            {
                if (DEBUG)
                    System.out.println("ConnectionGetter("+threadName_+") Stopping");
                // Return all the connections that I still have in my list.
                for (int i=0; i<connections_.size(); i++) {
                    Connection conn = (Connection)connections_.remove(0);
                    try { conn.close(); } catch (Exception e) { e.printStackTrace(); }
                }

            }
        }

    }  // internal class 'ConnectionGetter'
}
