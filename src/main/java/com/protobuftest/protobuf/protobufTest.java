package com.protobuftest.protobuf;

import com.google.protobuf.InvalidProtocolBufferException;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class protobufTest {
    @Test
    public void testN() throws IOException {
        PersonModel.Person.Builder builder = PersonModel.Person.newBuilder();
        builder.setId(1);
        builder.setName("maruizhong");
        builder.setEmail("mrz@sinosoft.com");
        PersonModel.Person person = builder.build();
        System.out.println("before:" + person);

        System.out.println("===Person Byte:");
        for (byte b : person.toByteArray()) {
            System.out.print(b);
        }
        System.out.println("================");

        byte[] byteArray = person.toByteArray();
        PersonModel.Person p2 = PersonModel.Person.parseFrom(byteArray);
         System.out.println("after name:" + p2.getName());
        System.out.println("after email:" + p2.getEmail());


        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        person.writeDelimitedTo(byteArrayOutputStream);
        //反序列化，从steam中读取一个或者多个protobuf字节对象
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        PersonModel.Person result = PersonModel.Person.parseDelimitedFrom(byteArrayInputStream);
        System.out.println("after email:"+result.getEmail());


    }
}