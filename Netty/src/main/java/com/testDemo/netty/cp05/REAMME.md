#### 分包拆包方式
TCP以流的方式进行数据传输，上层的应用协议为了对消息进行区分，往往采用如下4种方式。
（1）消息长度固定，累计读取到长度总和为定长LEN的报文后，就认为读取到一个完整的消息；
将计数器置位，重新开始读取下一个数据报文；

（2）将回车换行符作为消息结束符，例如FTP协议，这种方式在文本协议中应用比较广泛；

（3）将特殊的分隔符作为消息的结束标志，回车换行符就是一种特殊的结束分隔符；

（4）通过在消息头中定义长度字段来标识消息的长度。
