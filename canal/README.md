# canal安装说明
#### github上关于支持版本的说明

    目前内部版本已经支持mysql和oracle部分版本的日志解析，当前的canal开源版本支持5.7及以下的版本(阿里内部mysql 5.7.13, 5.6.10, mysql 5.5.18和5.1.40/48)

    虽然说明里有5.5及以下版本，但按我经验5.5版本Binlog缺少字段无法使用至少需要5.6.20以上
    
    github地址：https://github.com/alibaba/canal
    
##主库
#### 开启binglog
    打开服务中mysql服务的属性可以看到"C:\Program Files\MySQL\MySQL Server 5.6\bin\mysqld.exe" --defaults-file="C:\ProgramData\MySQL\MySQL Server 5.6\my.ini" MySQL56
    将my.ini复制到C:\Program Files\MySQL\MySQL Server 5.6
    然后在[mysqld]下加入以下内容
    
    #添加这一行就ok
    log-bin=mysql-bin
    #选择row模式
    binlog-format=ROW
    #指定生成log的数据库（这是指定需要生成log的数据库，如果删除这句，则表示所有数据库都需要生成log）
    binlog_do_db=icc_test
    
#### 验证是否开启

    SHOW VARIABLES LIKE 'log_%';
    查看log_bin 值是否是ON
    
#### 创建账号
    CREATE USER canal IDENTIFIED BY 'canal';    
    GRANT SELECT, REPLICATION SLAVE, REPLICATION CLIENT ON *.* TO 'canal'@'%';  
    -- GRANT ALL PRIVILEGES ON *.* TO 'canal'@'%' ;  
    FLUSH PRIVILEGES; 
    ps:因为不同的版本问题，导致创建用户的赋予权限，密码出现了不同，设置成'%'的时候，会导致 'localhost'无法登录。（就是本机测试会出错（远程无所谓））

#### 配置canal

    canal.deployer-1.1.3\conf\example 下的 instance.properties
    #配置监控的数据库名
    canal.instance.defaultDatabaseName =icc_test
 
 #### instance.properties参数说明
|参数名字|参数说明|
|:----|:----|
|canal.instance.mysql.slaveId|mysql集群配置中的serverId概念，需要保证和当前mysql集群中id唯一 (v1.1.x版本之后canal会自动生成，不需要手工指定)|
|canal.instance.master.address|mysql主库链接地址|
|canal.instance.master.journal.name|mysql主库链接时起始的binlog文件|
|canal.instance.master.position|mysql主库链接时起始的binlog偏移量|
|canal.instance.master.timestamp|mysql主库链接时起始的binlog的时间戳|
|canal.instance.gtidon|是否启用mysql gtid的订阅模式|
|canal.instance.master.gtid|mysql主库链接时对应的gtid位点|
|canal.instance.dbUsername|mysql数据库帐号|
|canal.instance.dbPassword|mysql数据库密码|
|canal.instance.defaultDatabaseName|mysql链接时默认schema|
|canal.instance.connectionCharset|mysql 数据解析编码|
|canal.instance.filter.regex|mysql 数据解析关注的表，Perl正则表达式.多个正则之间以逗号(,)分隔，转义符需要双斜杠(\\)| 
|canal.instance.filter.black.regex|mysql 数据解析表的黑名单，表达式规则见白名单的规则|
|canal.instance.rds.instanceId|aliyun rds对应的实例id信息(如果不需要在本地binlog超过18小时被清理后自动下载oss上的binlog，可以忽略该值)|

##碰到的问题
#### 乱码问题
    1.可能是数据库
        在my.ini里添加以下内容
	    并在[mysqld]下添加character-set-server=utf8
	    在[mysql]下添加default-character-set=utf8
	    在[client]下default-character-set=utf8（这一条并没有看到官方注释，但网上均有提及）
	    重启MySQL登陆后输入show VARIABLES like '%cha%';即可看到都改为utf-8
	
    2.canal中文乱码还有一种可能，当你使用cmd时canal报错含有中文的时候，由于你设定的字符集是utf-8，而cmd默认字符集是GBK
	可以直接去canal.deployer-1.1.3\logs\canal下看日志
	
## 测试方法
    CanalController下的startMonitor，运行main方法后即可监控对应数据库下的操作
    
## 更多参考
    https://github.com/alibaba/canal
    https://github.com/alibaba/canal/wiki/AdminGuide