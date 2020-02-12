package com.testDemo.serialize;

/**
 * @Date: 2019/4/30 14:59
 * @Description:
 */

import java.io.*;

/**
 * <p>ClassName: TestObjSerializeAndDeserialize<p>
 * <p>Description: 测试对象的序列化和反序列<p>
 */
public class TestObjSerializeAndDeserialize {

    public static void main(String[] args) throws Exception {



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

    /**
     * 对象转数组
     * @param obj
     * @return
     */
    public static byte[] toByteArray (Object obj) {
        byte[] bytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            bytes = bos.toByteArray ();
            oos.close();
            bos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return bytes;
    }

    /**
     * 数组转对象
     * @param bytes
     * @return
     */
    public static Object toObject (byte[] bytes) {
        Object obj = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream (bytes);
            ObjectInputStream ois = new ObjectInputStream (bis);
            obj = ois.readObject();
            ois.close();
            bis.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return obj;
    }
}
