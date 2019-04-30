package com.testDemo.serialize;

/**
 * @Date: 2019/4/30 14:59
 * @Description:
 */
import com.testDemo.object.VData;

import java.io.*;
import java.text.MessageFormat;
import java.util.Date;

/**
 * <p>ClassName: TestObjSerializeAndDeserialize<p>
 * <p>Description: 测试对象的序列化和反序列<p>
 */
public class TestObjSerializeAndDeserialize {

    public static void main(String[] args) throws Exception {
        VData mVData = new VData();
        mVData.add("aaaa");
        mVData.add("bbbb");
        mVData.add(new Date());
        serializeObject(mVData);
        VData m = (VData) deserializeObject(null);
        Date a = (Date) m.getObjectByObjectName("Date",1);
        System.out.println(a);
    }

    /**
     * Description: 序列化对象
     * @throws FileNotFoundException
     * @throws IOException
     */
    private static void serializeObject(Serializable object) throws FileNotFoundException,
            IOException {
        FileOutputStream fileOut =
                new FileOutputStream("E:/test.txt");
        ObjectOutputStream   oo = new ObjectOutputStream(fileOut);
        oo.writeObject(object);
        System.out.println("object对象序列化成功！");
        oo.close();
    }

    /**
     * Description: 反序列对象
     * @return
     * @throws Exception
     * @throws IOException
     */
    private static Serializable deserializeObject(Object object) throws Exception, IOException {
        FileInputStream fileIn = new FileInputStream("E:/test.txt");
        ObjectInputStream ois = new ObjectInputStream(fileIn);

        Serializable object1 = (Serializable) ois.readObject();
        System.out.println("object1对象反序列化成功！");
        return object1;
    }

}
