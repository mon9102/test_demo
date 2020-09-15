package com.testDemo.Instrumentation;


import javassist.*;

import java.io.IOException;
import java.io.PrintStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

/**
 * @Auther: zouren
 * @Date: 2019/6/27 11:07
 * @Description:
 */
public class MyClassFileTransformer implements ClassFileTransformer {


    public static void premain(String args, Instrumentation inst) {
        //注册我自己的字节码转换器

        System.out.println("MyClassFileTransformer.premain");
        PrintStream myStream = new PrintStream(System.out) {
            @Override
            public void println(String x) {
                super.println(System.currentTimeMillis() + ": " + x);
            }
        };
        System.setOut(myStream);
        inst.addTransformer(new MyClassFileTransformer());
    }


    /**
     * 字节码加载到虚拟机前会进入这个方法
     */
    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {

        if(!(className.startsWith("com/sinosoft/")||className.startsWith("com/testDemo/"))||className.startsWith("java/io")){
            return null;
        }

        System.out.println("MyClassFileTransformer.transform=="+className+"==start===");

        //javassist的包名是用点分割的，需要转换下
        if (className != null && className.indexOf("/") != -1) {
            className = className.replaceAll("/", ".");
        }
        try {
            //通过包名获取类文件
            ClassPool pool = ClassPool.getDefault();
            CtClass cc = null;
            String classPath =  pool.getClassLoader().getResource("").getPath();
            try {

                System.out.println(classPath);
                //java 类加载
                cc = pool.get(className);
            } catch (NotFoundException e) {
                //tomcat 加载
                try {
//                    Class<?> rClazz = Class.forName("org.apache.catalina.connector.Request");
//                    pool.insertClassPath(new ClassClassPath(rClazz));
//                    System.out.println(rClazz.getResource("").getPath());
                    pool.insertClassPath(classPath);

                    cc = pool.get(className);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }

            CtMethod[] ms = cc.getDeclaredMethods();
            CtMethod m = null;
            if(ms!=null){
                int len = ms.length;
                for(int i=0;i<len;i++){
                    m = ms[i];
                    System.out.println("MyClassFileTransformer.Method=="+m.getLongName());

                    m.insertBefore("{ System.out.println(\""+m.getLongName()+"===记录日志start\"); }");
                    m.insertAfter("{ System.out.println(\""+m.getLongName()+"===记录日志end\"); }");
                }
            }
            //获得指定方法名的方法
//            CtMethod m = cc.getDeclaredMethod("doSomeThing2");
            //在方法执行前插入代码
//            m.insertBefore("{ System.out.println(\"记录日志\"); }");
            System.out.println("MyClassFileTransformer.transform=="+className+"==end===");

            return cc.toBytecode();
        } catch (CannotCompileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            //忽略异常处理
            e.printStackTrace();
        }
        return null;
    }

    private  void test(){
//        ClassPool pool = ClassPool.getDefault();
//        // sun.misc.Launcher$AppClassLoader
//        Class<?> rClazz = Class.forName("org.apache.catalina.connector.Request");
//        pool.insertClassPath(new ClassClassPath(rClazz));
//        CtClass ctc = pool.get(className.replace("/", "."));
//        CtMethod targetMethod = ctc.getDeclaredMethod("list");
//        targetMethod.insertBefore("LOG.info(\"-Fuck Before\");System.out.println(\"Fuck stdin before ....\");");
//        targetMethod.insertAfter("LOG.info(\"-Fuck After\");System.out.println(\"Fuck stdin after ....\");");
//        return ctc.toBytecode();
    }
}
