define(['util','chosen'],function(util){
	var log = {
			timeFormat:'yyyy-MM-dd HH:mm:ss',
			threshold:'warning',
			levelMap:{debug:1,info:2,warning:3,error:4,success:5},
			logList:[]
	};
	var $logDom = $('ul#log');
	/**
	 * 日志区初始化
	 */
	log.init = function(){
		$('#levelFilter').val(this.threshold);     //初始时选中默认等级 
    $('#levelFilter').chosen({
      disable_search_threshold : 10,
      no_results_text : "没有数据",
      width:'120px'
    });
    this.addLog('success', '欢迎使用三日日志器');
    bindEvents();
	}
	
	/**
	 * 添加日志
	 */
	log.addLog = function(level,message){
		 var logJson = {},timestamp = new Date().getTime();;
     logJson.level = level;
     logJson.time = util.FormatUtil.dateFormat(timestamp,this.timeFormat);
     logJson.index = this.logList.length;
     logJson.message = message.toString();
     
     this.logList.push(logJson);
     domAddLog(logJson);
	}
	
	log.error = function(message){
		this.addLog('error',message);
	}
	log.info = function(message){
		this.addLog('info',message);
	}
	log.success = function(message){
		this.addLog('success',message);
	}
	log.warning = function(message){
		this.addLog('warning',message);
	}
	log.debug = function(message){
		this.addLog('debug',message);
	}
	/**
	 * dom 节点添加日志信息
	 */
	function domAddLog(logJson){
		 if(log.levelMap[logJson.level] < log.levelMap[log.threshold]){return ;}
     var elements = [];
     elements.push('<span class="linenum">'+logJson.index+'</span>');
     elements.push('<span class="time">'+logJson.time+'</span>');
     elements.push('<span class="level">'+logJson.level+'</span>');
     elements.push('<span class="message" title="'+logJson.message+'">'+logJson.message+'</span>')
     $logDom.append('<li class="'+logJson.level+'" level="'+logJson.level+'" index="'+logJson.index+'">'+elements.join('')+'</li>');
     
     //让消息滚动到最后面
     $logDom[0].scrollTop = $logDom[0].scrollHeight; 
	}
	/**
	 * 清空日志信息
	 */
	log.clean = function(){
		this.logList.length = 0;
    $logDom.empty();
	}
	
	/**
	 * 设置为指定日志信息
	 * 内部用作过滤日志信息,其实真实内容是没有改变的
	 */
	function setLogs(logs){
		 if(logs && logs.length > 0){
       $logDom.empty();
       for(var i=0;i<logs.length;i++){
         domAddLog(logs[i]);
       }
     }
	}
	
	/**
	 * log 的事件
	 */
	function bindEvents(){
		 var EVENTS = [{selector:'#logSearch input',types:['keyup','blur'],handler:searchLog},
                   {selector:'#logSearch .search',types:['click'],handler:searchLog},
                   {selector:'#levelFilter',types:['change'],handler:filterLog}];
     
     util.regPageEvents(EVENTS);
     
     /**
      * 日志搜索
      */
     function searchLog(e){
       var $target = $(e.target);
       if(!$target.is('input')){
         $target = $target.closest('.search-box').find('input');
       }
       var value = $target.val(),selectLog = [];
       if(log.logList && log.logList.length > 0){
         for(var i=0;i<log.logList.length;i++){
           if(log.logList[i].message.indexOf(value) != -1){
             selectLog.push(log.logList[i]);
           }
         }
         setLogs(selectLog);
       }
     }
     
     /**
      * 日志过滤
      */
     function filterLog(evt, params){
       threshold = params.selected;
       if(log.logList && log.logList.length > 0){
         var selectLog = [];
         for(var i=0;i<log.logList.length;i++){
           if(log.levelMap[log.logList[i].level] >= log.levelMap[log.threshold]){
             selectLog.push(log.logList[i]);
           }
         }
         setLogs(selectLog);
       }
     }
   }
	
	return log;
});