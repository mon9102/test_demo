# test_demo
https单双向认证：

1.单向认证

 1.1.生成https证书命令：

     keytool -genkey -keyalg RSA -dname "cn=localhost,ou=none,o=none,l=shanghai,st=shanghai,c=cn" -alias servers -keypass 123456 -keystore servers.keystore -storepass 123456 -validity 3650    

 1.2.生成CER文件，为是在浏览器中使用

     keytool -export -alias server -keystore servers.keystore -file ca.cer -storepass 123456
     
 1.3.配置文件

   1.3.1.服务端生成的keystore

    server.ssl.key-store: src/main/resources/servers.keystore
    server.ssl.key-store-password: 123456     

 
 
2.双向认证

 2.1.生成Server端证书：

     keytool -genkey -keyalg RSA -dname "cn=localhost,ou=none,o=none,l=shanghai,st=shanghai,c=cn" -alias server -keypass 123456 -keystore servers.keystore -storepass 123456 -validity 3650

 2.2.生成客户端证书:

     keytool -genkey -v -alias client -keyalg RSA -storetype PKCS12 -dname "cn=localhost,ou=none,o=none,l=shanghai,st=shanghai,c=cn"  -keypass 123456 -storepass 123456 -keystore client.p12 -validity 3650

 2.3.让服务端信任客户端的证书:

     keytool -export -alias client -keystore client.p12 -storetype PKCS12 -storepass 123456 -rfc -file client.cer

 2.4.将客户端生的CER文件导入服务端的证书库: 网上一般使用同一个Server端证书

     keytool -import -v -file client.cer -keystore clientxx.keystore

 2.5.查看server.keystore 里面的证书列表:

     keytool -list -keystore server.keystore

 2.6.客户端安装client.p12

 2.7.配置文件

  2.7.1.服务端生成的keystore

    server.ssl.key-store: src/main/resources/servers.keystore
    server.ssl.key-store-password: 123456

  2.7.2.客户端导入到服务端生成的Keystore

    server.ssl.trust-store=src/main/resources/clientxx.keystore
    server.ssl.trust-store-password=123456
   2.7.3.证书校验
   
    server.ssl.client-auth=need
3.启动项目后在页面访问https://ip:8443