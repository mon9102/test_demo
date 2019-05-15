package com.testDemo.netty.cp06.serializable;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class TestUserInfo {


    @Test
    public void toByteArray() throws IOException {
        System.out.println("--------------toByteArray---------------------");
        UserInfo info = new UserInfo();
        info.buildUserID(100).buildUserName("Welcome to Netty");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(bos);
        os.writeObject(info);
        os.flush();
        os.close();
        byte[] b = bos.toByteArray();
        System.out.println("The jdk serializable length is : " + b.length);
        bos.close();
        System.out.println("-------------------------------------------------");
        System.out.println("The byte array serializable length is : "
                + info.codeC().length);

        System.out.println("---------------------------------------------------");
    }

}
