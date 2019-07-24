package com.sanri.app.servlet;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sanri.app.BaseServlet;
import com.sanri.frame.RequestMapping;
import com.sun.management.OperatingSystemMXBean;

/**
 * 
 * 作者:sanri <br/>
 * 时间:2017-6-26下午2:30:11<br/>
 * 功能:jvm 参数处理 <br/>
 */
@RequestMapping("/jvmargs")
public class JvmArgsServlet extends BaseServlet{
	
	/**
	 * 
	 * 作者:sanri <br/>
	 * 时间:2017-6-26下午2:45:56<br/>
	 * 功能:内存使用情况查询 <br/>
	 * @return
	 */
	public Map<String,MemoryUsage> memoryUseQuery(){
		Map<String,MemoryUsage> memoryUseMap = new HashMap<String, MemoryUsage>();
		//获取堆区和非堆区内存(非堆内存指的是 jvm 管理的非堆内存,而不是除了堆之外的内存)
		MemoryMXBean memorymbean = ManagementFactory.getMemoryMXBean(); 
		MemoryUsage heapUsage = memorymbean.getHeapMemoryUsage();
		MemoryUsage nonHeapMemoryUsage = memorymbean.getNonHeapMemoryUsage();
		memoryUseMap.put("heap", heapUsage);
		memoryUseMap.put("nonHeap", nonHeapMemoryUsage);
		
		//获取堆中各内存使用情况
		List<MemoryPoolMXBean> memoryPoolMXBeans = ManagementFactory.getMemoryPoolMXBeans();
		for (MemoryPoolMXBean memoryPoolMXBean : memoryPoolMXBeans) {
			String name = memoryPoolMXBean.getName();
			MemoryUsage usage = memoryPoolMXBean.getUsage();
//			MemoryUsage collectionUsage = memoryPoolMXBean.getCollectionUsage();			// jvm 最近回收了内存之后的内存使用量
			if(name.indexOf("Code") != -1){
				//TODO 这里应该有更好的办法获取名称
				name = "codecache";
			}else if(name.indexOf("Eden") != -1){
				name = "eden";
			}else if(name.indexOf("Perm") != -1){
				name = "perm";
			}else if(name.indexOf("Old") != -1){
				name = "old";
			}else if(name.indexOf("Survivor") != -1){
				name="survivor";
			}
			memoryUseMap.put(name, usage);
		}
		//获取 jvm 中的内存使用情况
		Runtime runtime = Runtime.getRuntime();
		long jvmMaxMemory = runtime.maxMemory();			//返回虚拟机试图使用的最大内存量,如操作系统没有限制,则返回 Long.MAX_VALUE
		long jvmFreeMemory = runtime.freeMemory();
		long jvmTotalMemory = runtime.totalMemory();
		long jvmUsedMemory = jvmTotalMemory - jvmFreeMemory;
		MemoryUsage jvmMemoryUsage = new MemoryUsage(0, jvmUsedMemory, jvmTotalMemory, jvmMaxMemory);
		memoryUseMap.put("jvmMem", jvmMemoryUsage);

		//获取操作系统的内存使用量以及虚拟内存使用量
		OperatingSystemMXBean operatingSystemMXBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
		long freePhysicalMemorySize = operatingSystemMXBean.getFreePhysicalMemorySize();
		long totalPhysicalMemorySize = operatingSystemMXBean.getTotalPhysicalMemorySize();
		long usedPhysicalMemorySize = totalPhysicalMemorySize - freePhysicalMemorySize;
		long freeSwapSpaceSize = operatingSystemMXBean.getFreeSwapSpaceSize();
		long totalSwapSpaceSize = operatingSystemMXBean.getTotalSwapSpaceSize();
		long usedSwapSize = totalSwapSpaceSize - freeSwapSpaceSize;
		
		MemoryUsage physicalMemoryUsage = new MemoryUsage(0, usedPhysicalMemorySize, usedPhysicalMemorySize, totalPhysicalMemorySize);
		MemoryUsage swapMemoryUsage = new MemoryUsage(0, usedSwapSize, usedSwapSize, totalSwapSpaceSize);
		memoryUseMap.put("physical", physicalMemoryUsage);
		memoryUseMap.put("swap", swapMemoryUsage);
		return memoryUseMap;
	}
	
}
