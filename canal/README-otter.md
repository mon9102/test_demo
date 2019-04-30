##说明
    暂时卡在zookeeper与otter交互
####环境
    1.otter为纯java编写，windows/linux均可支持
    2.jdk建议使用1.6.25以上的版本
    3.整个otter同步由几部分组成，需要预先进行安装(manager和node)
    4.otter node依赖于zookeeper进行分布式调度，需要安装一个zookeeper节点或者集群.
        重要：考虑异地机房的地域性，node机器会优先选择就近的zookeeper节点进行访问，比如国际站会在杭州和美国各部署node，针对美国的node会选择美国的zookeeper进行访问，提升读效率. 
        ps. 不同机房的zookeeper集群组成一个物理大集群，只不过是根据地域不同划分为不同逻辑集群，所有地域的node机器对zookeeper进行写操作都会发到一个地域的zookeeper进行paoxs算法仲裁.
        所以，manager启动完成后，需要首先定义不同机房的zookeeper机器集群。
    5.aria2下载：https://github.com/aria2/aria2/releases/tag/release-1.34.0
####注意
    1.otter依赖于canal提供数据库日志，针对mysql数据有一些要求，具体请查看README.md
    2.目前canal支持mixed,row,statement多种日志协议的解析，但配合otter进行数据库同步，目前仅支持row协议的同步，使用时需要注意.
        
##manager安装
####环境
    1.载入 otter-manager-schema.sql
    2. 整个otter架构依赖了zookeeper进行多节点调度，所以需要预先安装zookeeper，不需要初始化节点，otter程序启动后会自检.
    manager需要在otter.properties中指定一个就近的zookeeper集群机器
    ps.我在windows实验时并没有自检，需要手动配置
####启动
    1.修改conf/otter.properties下相关参数
    2.在bin下运行对应的startup文件
####验证
    logs/manager.log下出现
    2019-04-29 17:27:43.219 [] INFO  com.alibaba.otter.manager.deployer.JettyEmbedServer - ##Jetty Embed Server is startup!
    2019-04-29 17:27:43.220 [] INFO  com.alibaba.otter.manager.deployer.OtterManagerLauncher - ## the manager server is running now ......
    或访问访问： http://127.0.0.1:8080/，出现otter的页面（地址由otter.domainName和otter.port拼成）
##aria2安装
    win10 下建议直接在path配置目录地址即可，如：D:\aria2-1.34.0-win-64bit-build1
    其他版本可以考虑配置AEIA2_HOME的方式
    linux（网上找的）
        ./configure
        Make
        Make install
        aria2c -v #可以查看版本
##node安装（还没成功）
    先在manage管理页面进行相关配置
    机器管理-node管理-添加
    之后在node文件夹下的conf下运行
    echo 1 > nid
    然后启动，manage页面变为运行中即可
##相关介绍
####用户向导
    https://github.com/alibaba/otter/wiki/Adminguide
####docker一键部署
    https://github.com/alibaba/otter/wiki/Docker_QuickStart