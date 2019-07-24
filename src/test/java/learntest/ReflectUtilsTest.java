package learntest;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.junit.Test;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.util.ReflectionUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectUtilsTest {

    @Test
    public void test() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        // MethodUtil 主要是调用方法功能
        Object intValue = MethodUtils.invokeMethod(1, "intValue");
        System.out.println(intValue);

        //ClassUtils 主要用于获取 class
        Class<?> clazz = ClassUtils.getClass("learntest.ReflectUtilsTest");
        System.out.println(clazz);

        //这个能查不需要参数的方法
        Method test = ReflectionUtils.findMethod(clazz, "test");
        System.out.println(test);

        //主要用于获取class 信息
        PropertyDescriptor[] beanGetters = ReflectUtils.getBeanGetters(clazz);

    }
}
