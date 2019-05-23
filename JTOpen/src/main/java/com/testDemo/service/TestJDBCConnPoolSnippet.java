package com.testDemo.service;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.ibm.as400.access.AS400JDBCManagedConnectionPoolDataSource;
import com.ibm.as400.access.AS400JDBCManagedDataSource;

import java.sql.Connection;

/**
 * @Auther: zouren
 * @Date: 2019/5/23 14:57
 * @Description:
 */
public class TestJDBCConnPoolSnippet {
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
    void test() throws Exception
    {
        AS400JDBCManagedConnectionPoolDataSource cpds0 = new AS400JDBCManagedConnectionPoolDataSource();

        // Set general datasource properties.  Note that both connection pool datasource (CPDS) and managed
        // datasource (MDS) have these properties, and they might have different values.

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
        //cpds0.setReuseConnections(false);  // do not re-use connections

        // Set the initial context factory to use.
        System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.fscontext.RefFSContextFactory");


        // Get the JNDI Initial Context.
        Context ctx = new InitialContext();

        // Note: The following is an alternative way to set context properties locally:
        //   Properties env = new Properties();
        //   env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.fscontext.RefFSContextFactory");
        //   Context ctx = new InitialContext(env);

        ctx.rebind("mydatasource", cpds0);  // We can now do lookups on cpds, by the name "mydatasource".

        // Create a standard DataSource object that references it.

        AS400JDBCManagedDataSource mds0 = new AS400JDBCManagedDataSource();
        mds0.setDescription("DataSource supporting connection pooling");
        mds0.setDataSourceName("mydatasource");
        ctx.rebind("ConnectionPoolingDataSource", mds0);

        DataSource dataSource_ = (DataSource)ctx.lookup("ConnectionPoolingDataSource");

        AS400JDBCManagedDataSource mds_ = (AS400JDBCManagedDataSource)dataSource_;

        boolean isHealthy = mds_.checkPoolHealth(false);  //check pool health

        Connection c = dataSource_.getConnection();

    }
}
