###netty入门
### LinBasedFrameDecoder和StringDecoder
1.LinBasedFrameDecoder的工作原理是它依次遍历ByteBuf中的可读字节，判断是不"\n"or"\r\n",如有
    就以此这结束位置，从可读索引到结束位置区间的字节就组成一行。它是以换行符为结束标志。
2.StringDecoder就是把接收的对象转换为字符串。    



