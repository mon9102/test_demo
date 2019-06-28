import com.testDemo.Instrumentation.Business;

/**
 * 打成包把之后与javassist-3.24.1-GA.jar放到同级，
 * 把两个包放到项目中的lib中。
 * 配置MANIFEST.MF，再运行参数增加下面参数
 * -javaagent: 打成包的物理路径
 */
public class MyClassFileTransformerTest {
	 public static void main(String[] args){  
		 
	        System.out.println("main MyClassFileTransformerTest of proxy!");
		 Business business = new Business();
		 business.doSomeThing2();
		 business.aa("哈哈");
	    }  
}
