package com.sanri.deginmodel.proxy;

import com.sanri.deginmodel.proxy.static_.Dog;
import com.sanri.deginmodel.proxy.static_.Moveable;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * 作者: sanri
 * 时间: 2017/08/10 21:20
 * 功能: 动态编译类文件例子
 */
public class DynamicCompileExample {

   @Test
   public void testMainRun() throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
       String code = "package com.sanri.deginmodel.proxy.static_; \n public class $Proxy1 implements  Moveable {\n" +
               "    private Moveable target;\n" +
               "\n" +
               "    public $Proxy1(Moveable target){\n" +
               "        this.target = target;\n" +
               "    }\n" +
               "\n" +
               "    @Override\n" +
               "    public void move() {\n" +
               "        long startTime = System.currentTimeMillis();\n" +
               "        System.out.println(\"移动开始时间为:\"+startTime);\n" +
               "        target.move();\n" +
               "        System.out.println(\"移动花费时间:\"+(System.currentTimeMillis() - startTime));\n" +
               "    }\n" +
               "}\n";
       //把字符串写入文件
       File dir = new File("E:\\doc\\project\\20170422\\newsanritools\\src\\com\\sanri\\deginmodel\\proxy\\static_");
       File filename = new File(dir,"$Proxy1.java");
       FileUtils.writeStringToFile(filename, code);
       //获取一个编译器
       JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
       //获取一个文件管理器
       StandardJavaFileManager standardFileManager = javaCompiler.getStandardFileManager(null, null, null);
       //获取要编译的文件
       Iterable<? extends JavaFileObject> javaFileObjects = standardFileManager.getJavaFileObjects(filename);
       //获取编译任务
       JavaCompiler.CompilationTask task = javaCompiler.getTask(null, standardFileManager, null, null, null, javaFileObjects);
       task.call();
       standardFileManager.close();

       //加载到 ClassLoder, 必须保证 class 在 classpath 下面的
       URL url = new URL("file:/"+"E:\\doc\\project\\20170422\\newsanritools/src");
       ClassLoader classLoader = new URLClassLoader(new URL[]{url});
       Class clazz = classLoader.loadClass("com.sanri.deginmodel.proxy.static_.$Proxy1");
       System.out.println(clazz);
       Method move = clazz.getMethod("move");
       Constructor constructor = clazz.getConstructor(Moveable.class);
       //创建目标类
       Moveable dog = new Dog();
       //实例化代理类
       Object obj = constructor.newInstance(dog);
       move.invoke(obj);
   }
}
