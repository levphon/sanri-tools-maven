/**
 * 日期工具类 
 */
define(['util'],function(util){
	var module = {
			calendarFieldMap:{
				YEAR:1,
				MONTH:2,
				DATE:5,
				HOUR:10,
				MINUTE:12,
				SECOND:13,
				MILLISECOND:14
			}
	};
	
	/**
	 * 增加日期
	 */
	function add(date,calendarField,amount){
		if(!date || date.constructor != Date){
			throw new Error('需要提供操作日期 date');
		}
		var mirrorNum = module.calendarFieldMap[calendarField];
		if(!mirrorNum){
			throw new Error('非法的日期字段值 :'+calendarField);
		}
		amount = amount || 1;
		//添加日期
		switch(mirrorNum){
		case 1:
			date.setFullYear(date.getFullYear() + amount);
			break;
		case 2:
			date.setMonth(date.getMonth() + amount);
			break;
		case 5:
			date.setDate(date.getDate() + amount);
			break;
		case 10:
			date.setHours(date.getHours() + amount);
			break;
		case 12:
			date.setMinutes(date.getMinutes() + amount);
			break;
		case 13:
			date.setSeconds(date.getSeconds() + amount);
			break;
		case 14:
			date.setMilliseconds(date.getMilliseconds() + amount);
			break;
		}
		return date;
	}
	
	/**
	 * 增加日期天数
	 * @param date 原来日期
	 * @param amount 增加的天数,减天数设置为负数
	 */
	module.addDays = function(date,amount){
		return add(date,'DATE', amount);
	};
	
	/**
	 * 增加日期月份
	 * @param date
	 * @param amount
	 */
	module.addMonths = function(date,amount){
		return add(date,'MONTH', amount);
	};
	
	/**
	 * 增加日期年份
	 * @param date
	 * @param amount
	 * @returns
	 */
	module.addYears = function(date,amount){
		return add(date,'YEAR', amount);
	};
	/**
	 * 增加日期小时
	 * @param date
	 * @param amount
	 * @returns
	 */
	module.addHours = function(date,amount){
		return add(date,'HOUR', amount);
	};
	/**
	 * 增加日期分钟
	 * @param date
	 * @param amount
	 * @returns
	 */
	module.addMinutes = function(date,amount){
		return add(date,'MINUTE', amount);
	};
	/**
	 * 增加日期秒
	 * @param date
	 * @param amount
	 * @returns
	 */
	module.addSeconds = function(date,amount){
		return add(date,'SECOND', amount);
	};
	/**
	 * 增加日期毫秒
	 * @param date
	 * @param amount
	 * @returns
	 */
	module.addMilliseconds = function(date,amount){
		return add(date,'MILLISECOND', amount);
	};
	
	/**
	 * 判断两个日期是否为同一天
	 */
	module.isSameDay = function(date1,date2){
		if(!date1 || !date2 || date1.constructor != Date || date2.constructor != Date){
			throw new Error('日期值非法');
		}
		return date1.getFullYear() == date2.getFullYear() && date1.getMonth() == date2.getMonth() && date1.getDate() == date2.getDate();
	};
	
	/**
	 * 清空小时,分钟,秒,毫秒
	 * @param date
	 */
	module.cleanHourMinuteSecond = function(date){
		if(!date || date.constructor != Date){
			throw new Error('日期值非法');
		}
		date.setHours(0,0,0,0);
	};
	
	/**
	 * 设置日期为一天中的最后一个时刻,精确表毫秒
	 * @param date
	 */
	module.setLastDayTime = function(date){
		if(!date || date.constructor != Date){
			throw new Error('日期值非法');
		}
		date.setHours(23,59,59,999);
	};
	
	/**
	 * 格式化时间戳
	 */
	module.formatDate = function(time,format){
		return util.FormatUtil.dateFormat(time,format);
	};
	
	return module;
});