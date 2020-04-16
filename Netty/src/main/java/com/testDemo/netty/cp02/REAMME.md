###JDK的写法
AIO是NIO2 对NIO的增强
#### 对比
| |同步阻塞I/O(BIO)|伪异步I/O|非阻塞I/O(NIO)|异步I/O(AIO)|
|:----|:----|:----|:----|:----|
|客户端个数:I/O线程|1:1|M:N(其中M可以大于N)|M:1(1个I/O线程处理多个客户端连接)|M:0(不需要启动额外的I/O线程，被动回调)|
|I/O类型(阻塞)|阻塞I/O|阻塞I/O|非阻塞I/O|非阻塞I/O|
|I/O类型(I/O)|同步I/O|同步I/O|同步I/O(I/O多路复用)|异步I/O|
|API使用难度|简单|简单|非常复杂|复杂|
|调试难度|简单|简单|复杂|复杂|
|可靠性|非常差|差|高|高|
|吞吐量|低|中|高|高|

###NIO开发的步骤
1.创建ServerSocketChannel,配置它为非阻塞模式
2.绑定监听，配置tcp参数，例如port和backlog大小, 参考com.testDemo.netty.cp02.nio.MultiplexerTimeServer
3.创建一个独立的I/O线程，用于轮询多路复用器Selector
4.创建Selector,装之前创建ServerSocketChannel注册到 Selector上，监听SelectionKey.OP_ACCEPT
5.启动I/O线程，在循环体中执行Selector.select()方法，轮询就绪的Channel
6.当轮询到处于就绪状态的Channel时，需要对其判断，如果是OP_ACCEPT状态，说明是新的客户端接入，则调用ServerSocketChannel.accept()
方法接受新的客户端
7.设置新接入的客户端链路SocketChannel为非阻塞模式，配置其它的一些tcp参数
8.将SocketChannel注册到Selector，监听OP_READ操作位
9.如果轮询的Channel为OP_READ，则说明SocketChannel中有新的就绪的数据包需要读取，则构造ByteBuffer,读取数据包
10.如果轮询的Channel为OOP_WRITE，说明还有数据没有发送完成，需要继续发送。
