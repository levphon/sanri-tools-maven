//package sanri.utils;
//
//import java.beans.BeanInfo;
//import java.beans.IntrospectionException;
//import java.beans.Introspector;
//import java.beans.MethodDescriptor;
//import java.io.File;
//import java.lang.reflect.Method;
//import java.net.URI;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//
//import org.apache.commons.beanutils.BeanUtils;
//import org.apache.commons.io.FileUtils;
//import org.apache.commons.io.filefilter.FileFileFilter;
//import org.apache.commons.io.filefilter.FileFilterUtils;
//import org.apache.commons.io.filefilter.IOFileFilter;
//import org.apache.commons.io.filefilter.SuffixFileFilter;
//import org.apache.commons.io.filefilter.TrueFileFilter;
//
//
///**
// * 
// * 作者:sanri</br>
// * 时间:2016-9-28上午11:46:21</br>
// * 功能:扩展自 apache.BeanUtils 增加获取方法的方法 <br/>
// * 问题:在加载所有的类文件时有点慢主要是为了找到 stopClass 看有没有好的办法优化下<br/>
// */
//public class BeanUtil extends BeanUtils{
//	
//	private final static List<Class<?>> userClasses = new ArrayList<Class<?>>();
//	private static StringBuffer userClassesString = new StringBuffer(",");
//	static{
//		String classPath = PathUtil.classPath();
//		File classDir = new File(classPath);
//		IOFileFilter fileFilter = FileFilterUtils.and(FileFileFilter.FILE,new SuffixFileFilter(".class"));
//		Iterator<File> iterateFiles = FileUtils.iterateFiles(classDir, fileFilter,TrueFileFilter.INSTANCE );
//		try{
//			while(iterateFiles.hasNext()){
//				File nextClassFile = iterateFiles.next();
//				URI relativize = PathUtil.URL.toURI().relativize(nextClassFile.toURI());
//				String loadClassPath = StringUtil.prefix(relativize.toString()).replaceAll("\\/", ".");
//				userClasses.add(Class.forName(loadClassPath));
//			}
////			userClassesString = StringUtil.join(userClasses,",");
//			Iterator<Class<?>> iterator = userClasses.iterator();
//			while(iterator.hasNext()){
//				Class<?> userClass = iterator.next();
//				userClassesString.append(userClass.getSimpleName()).append(",");
//			}
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//	}
//	/**
//	 * 
//	 * 作者:sanri</br>
//	 * 时间:2016-9-28下午3:03:17</br>
//	 * 功能:查找一个类的所有方法(包括继承的方法 )</br>
//	 */
//	public static List<Method> findMethod(Class<?> clazz,MethodFilter me){
//		List<Method> methods = new ArrayList<Method>();
//		Class<?> stopClass = null;
//		stopClass = getStopClass(clazz);
//		try {
//			BeanInfo beanInfo = Introspector.getBeanInfo(clazz, stopClass);
//			MethodDescriptor[] methodDescriptors = beanInfo.getMethodDescriptors();
//			for (MethodDescriptor methodDescriptor : methodDescriptors) {
//				Method method = methodDescriptor.getMethod();
//				if(me.accept(method,methodDescriptor.getTitle())){
//					methods.add(method);
//				}
//			}
//		} catch (IntrospectionException e) {
//			e.printStackTrace();
//		}
//		return methods;
//	}
//	
//	static class GetMethodFilter implements MethodFilter{
//		@Override
//		public boolean accept(Method method, String methodName) {
//			return methodName.indexOf("get") != -1;
//		}
//	}
//	static class SetMethodFilter implements MethodFilter{
//		@Override
//		public boolean accept(Method method, String methodName) {
//			return methodName.indexOf("set") != -1;
//		}
//	}
//	public final static GetMethodFilter GET_METHOD_FILTER = new GetMethodFilter();
//	public final static SetMethodFilter SET_METHOD_FILTER = new SetMethodFilter();
//	
//	/**
//	 * 
//	 * 作者:sanri</br>
//	 * 时间:2016-9-28下午2:46:39</br>
//	 * 功能:获取 clazz 在本应用中的最终父类</br>
//	 */
//	public static Class<?> getStopClass(Class<?> clazz) {
//		if(clazz == null){return null;}
//
//		Class<?> stopClass = Object.class;
//		Class<?> superclass = clazz.getSuperclass();
//		while( superclass != Object.class){
//			String simpleName = superclass.getSimpleName();
//			if(userClassesString.indexOf(","+simpleName+",") == -1){
//				stopClass = superclass;
//				break;
//			}
//			superclass = superclass.getSuperclass();
//		}
//		return stopClass;
//	}
//	
//	public interface MethodFilter{
//		boolean accept(Method method,String methodName);
//	}
//}
