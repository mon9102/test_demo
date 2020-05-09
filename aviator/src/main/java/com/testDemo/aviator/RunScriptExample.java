package com.testDemo.aviator;

import com.googlecode.aviator.*;
import com.googlecode.aviator.script.AviatorScriptEngine;
import com.testDemo.App;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

import javax.script.*;

/**
 * @author : zouren
 * @date : 2020/5/9 09:57
 */
//@RunWith(App.class)
//@SpringBootTest
public class RunScriptExample {
    /**
     * 执行脚本文件
     */
    @Test
    public void compileScriptTest() throws Exception{
        // Compile the script into a Expression instance.
        Expression exp = AviatorEvaluator.getInstance().compileScript("examples/hello.av");
        // Run the exprssion.
        exp.execute();
    }

    /**
     * 通过 ScriptEngineManager 可以获得 AviatorScript 的执行引擎
     * @throws Exception
     */
    @Test
    public void scriptEngineExample() throws Exception{
        final ScriptEngineManager sem = new ScriptEngineManager();
        ScriptEngine engine = sem.getEngineByName("AviatorScript");

    }

    /**
     * 可以从 ScriptEngine 里获取底层的 AviatorEvaluatorInstance 引用，进行引擎的相关配置：
     * @throws Exception
     */
    @Test
    public void ConfigureEngine() throws Exception{
        final ScriptEngineManager sem = new ScriptEngineManager();
        ScriptEngine engine = sem.getEngineByName("AviatorScript");
        AviatorEvaluatorInstance instance = ((AviatorScriptEngine) engine).getEngine();
        // Use compatible feature set
        instance.setOption(Options.FEATURE_SET, Feature.getCompatibleFeatures());
        // Doesn't support if in compatible feature set mode.
        engine.eval("if(true) { println('support if'); }");

    }

    /**
     * 最简单的，你可以直接执行一段 AviatorScript 脚本，调用 eval(script) 方法即可
     * @throws Exception
     */
    @Test
    public void EvalScriptExample() throws Exception{
        final ScriptEngineManager sem = new ScriptEngineManager();
        ScriptEngine engine = sem.getEngineByName("AviatorScript");
        engine.eval("print('Hello, World')");
        //如果你的脚本是文件，也可以用 eval(reader) 方法
        String filePath="";
        engine.eval(new java.io.FileReader(filePath));

    }

    /**
     * AviatorScript 也支持了 Scripting API 的预编译模式：
     * @throws Exception
     */
    @Test
    public void CompileScript() throws Exception{
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("AviatorScript");

        Compilable compilable = (Compilable) engine;
        CompiledScript script = compilable.compile("a + b");
        CompiledScript script2 = compilable.compile("a + b>c");

        final Bindings bindings = engine.createBindings();
        bindings.put("a", 99);
        bindings.put("b", 1);
        bindings.put("c", 1);
        System.out.println(script.eval(bindings));
        System.out.println(script2.eval(bindings));

    }
    /**
     * 在 java 中调用 script 函数也同样支持：
     */
    @Test
    public void InvokeScriptFunction() throws Exception{
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("AviatorScript");

        // AviatorScript code in a String
        String script = "fn hello(name) { print('Hello, ' + name); }";
        // evaluate script
        engine.eval(script);

        // javax.script.Invocable is an optional interface.
        // Check whether your script engine implements or not!
        // Note that the AviatorScript engine implements Invocable interface.
        Invocable inv = (Invocable) engine;

        // invoke the global function named "hello"
        inv.invokeFunction("hello", "Scripting!!" );

    }

    /**
     * 在 AviatorScript 中可以使用 map 和闭包来模拟面向对象编程，
     * 同样，我们可以在 java 代码里调用 AviatorScript 中“对象”的方法：
     * @throws Exception
     */
    @Test
    public void InvokeScriptMethod() throws Exception{
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("AviatorScript");

        // AviatorScript code in a String. This code defines a script object 'obj'
        // with one method called 'hello'.
        String script =
                "let obj = seq.map(); obj.hello = lambda(name) -> print('Hello, ' + name); end;";
        // evaluate script
        engine.eval(script);

        // javax.script.Invocable is an optional interface.
        // Check whether your script engine implements or not!
        // Note that the AviatorScript engine implements Invocable interface.
        Invocable inv = (Invocable) engine;

        // get script object on which we want to call the method
        Object obj = engine.get("obj");

        // invoke the method named "hello" on the script object "obj"
        inv.invokeMethod(obj, "hello", "Script Method !!");

    }
}
