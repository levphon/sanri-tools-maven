define(['util','domready','icheck','zclip'],function(util){
	var column = {},
		origin = {left:[],right:[],ignore:[],fun:[]},
		handler = {left:[],right:[],ignore:[],leftMuti:[],rightMuti:[],equal:[],fun:[],repeat:[]};
	
	column.init = function(){
		var EVENTS = [{selector:'#leftColumns',types:['blur'],handler:dealLeftColumns},
		              {selector:'#rightColumns',types:['blur'],handler:dealRightColumns},
		              {selector:'#ignoreColumns',types:['blur'],handler:dealIgnoreColumns},
		              {selector:'#funColumns',types:['blur'],handler:dealFunColumns},
		              {parent:'.colset',selector:'li',types:['click'],handler:click2Select}];
		util.regPageEvents(EVENTS);
		
		//初始化 radio,checkbox
		$('input:checkbox').iCheck({
	        checkboxClass: 'icheckbox_square-green',
	        radioClass: 'iradio_square-green'
	    });
		
		/**
		 * 点击 li 选中复选框
		 */
		function click2Select(){
			var $checkbox = $(this).find('input:checkbox');
			if($checkbox.is(':checked')){
				$checkbox.iCheck('uncheck');
			}else{
				$checkbox.iCheck('check');
			}
		}
		
		$('.copy').zclip({
	        path: "ZeroClipboard.swf",
	        copy: function(){
	        	var key = $(this).attr('key');
	        	return handler[key].join('\n');
	        },
	        beforeCopy:function(){/* 按住鼠标时的操作 */
	        },
	        afterCopy:function(){/* 复制成功后的操作 */
	        	layer.msg('复制成功');
	        }
	    });
	}
	
	/**
	 * 主比较方法,比较处理后的左边列和右边列
	 */
	column.compare = function(){
		var needCompare = true;
		//重新初始化 leftMuti,rightMuti,equal
		handler.rightMuti = [];
		handler.leftMuti = [];
		handler.equal = [];
		
		if(handler.ignore.length == 0){
			//没有需要忽略的列
			if(handler.right.length > 0 && handler.left.length == 0){
				handler.rightMuti = handler.right;
				needCompare = false;
			}else if(handler.right.length ==0 && handler.left.length > 0){
				handler.leftMuti = handler.left;
				needCompare = false;
			}
		}else{
			//去除需要忽略的列
			if(handler.right.length > 0 && handler.left.length == 0){
				//右边多出全部列
				for(var i=0;i<handler.right.length;i++){
					if($.inArray(handler.right[i],handler.ignore) == -1){
						handler.rightMuti.push(handler.right[i]);
					}
				}
				needCompare = false;
			}else if(handler.right.length ==0 && handler.left.length > 0){
				for(var i=0;i<handler.left.length;i++){
					if($.inArray(handler.left[i],handler.ignore) == -1){
						handler.leftMuti.push(handler.left[i]);
					}
				}
				needCompare = false;
			}
		}
		//其它情况都需要比较
		if(needCompare){
			//统计相同的列
			for(var i=0;i<handler.left.length;i++){
				//首先过滤忽略的列
				if($.inArray(handler.left[i],handler.ignore) != -1){
					continue;
				}
				for(var j=0;j<handler.right.length;j++){
					if(handler.left[i] == handler.right[j]){
						handler.equal.push(handler.left[i]);
					}
				}
			}
			//统计旧列比新列多出的列 注:比较的列中不能有分号
			var eqCols = ';'+handler.equal.join(';')+';';		//相同的列
			for(var i=0;i<handler.left.length;i++){
				//首先过滤忽略的列
				if($.inArray(handler.left[i],handler.ignore) != -1){
					continue;
				}
				if(eqCols.indexOf(';'+handler.left[i]+';') == -1){
					handler.leftMuti.push(handler.left[i]);
				}
			}
			//统计新列比旧列多出的列
			for(var i=0;i<handler.right.length;i++){
				//首先过滤忽略的列
				if($.inArray(handler.right[i],handler.ignore) != -1){
					continue;
				}
				if(eqCols.indexOf(';'+handler.right[i]+';') == -1){
					handler.rightMuti.push(handler.right[i]);
				}
			}
		}
	}
	
	/**
	 * 处理获取到的数据
	 */
	column.handlerData = function(){
		var $colset = $('#colset');
		var isIgnoreAa = $colset.find('input:checkbox[name=ignoreAa]').is(':checked'),
		isIgnoreBlank = $colset.find('input:checkbox[name=ignoreBlank]').is(':checked'),
		isIgnore_ = $colset.find('input:checkbox[name=ignore_]').is(':checked');
		
		/*
		 * 处理输入值
		 */
		function handlerVal(val){
			if(isIgnoreBlank){
				val = val.trim();
			}
			if(isIgnoreAa){
				val = val.toLowerCase();
			}
			if(isIgnore_){
				val = val.replace(/_/g,'');
			}
			return val;
		}
		
		//初始化 handler 值 
		handler.left = [];handler.right = [];
		
		//处理左边值
		if(origin.left && origin.left.length > 0){
			for(var i=0;i<origin.left.length;i++){
				var val = origin.left[i];
				if(!val || val == ''){
					continue;
				}
				handler.left.push(handlerVal(val));
			}
		}
		//处理右边值
		if(origin.right && origin.right.length > 0){
			for(var i=0;i<origin.right.length;i++){
				var val = origin.right[i];
				if(!val || val == ''){
					continue;
				}
				handler.right.push(handlerVal(val));
			}
		}
		
		//写入条数记录 
		$('#leftCount').text(handler.left.length);
		$('#rightCount').text(handler.right.length);
		
		column.compare();	//比较数据
		//写入比较后的数据到指定地方
		$('#leftMuti').html(handler.leftMuti.join('<br/>'));
		$('#rightMuti').html(handler.rightMuti.join('<br/>'));
		$('#equalColumns').html(handler.equal.join('<br/>'));
		
		//写入条数记录 
		$("#leftMutiRightCount").text(handler.leftMuti.length);
		$("#rightMutiLeftCount").text(handler.rightMuti.length);
		$("#equalCount").text(handler.equal.length);
	}
	
	/**
	 * 获取到左边列数据,并触发处理方法
	 */
	function dealLeftColumns(){
		var preData = $(this).val().trim();
		origin.left = preData.split('\n');
		column.handlerData();
	}
	
	/**
	 * 获取右边列数据,并触发处理方法
	 */
	function dealRightColumns(){
		var preData = $(this).val().trim();
		origin.right = preData.split('\n');
		column.handlerData();
	}
	/**
	 * 获取忽略列数据,并触发处理方法
	 */
	function dealIgnoreColumns(){
		var preData = $(this).val().trim();
		origin.ignore = preData.split('\n');
		column.handlerData();
	}
	
	/**
	 * 列功能处理
	 */
	function dealFunColumns(){
		//清空相等列
		handler.equal = [];handler.fun = [];
		$('#equalColumns').html('');
		
		var preData = $(this).val().trim();
		origin.fun = preData.split('\n');
		
		$colset = $('#funBtns');
		//获取功能列配置 
		var erasureBlank = $colset.find('input:checkbox[name=erasureBlank]').is(':checked'),
		erasureRepeat = $colset.find('input:checkbox[name=erasureRepeat]').is(':checked'),
		sort = $colset.find('input:checkbox[name=sort]').is(':checked');

		if(origin.fun && origin.fun.length > 0){
			for(var i=0;i<origin.fun.length;i++){
				var item = origin.fun[i];
				if(!item || item == ''){
					continue;
				}
				
				//去掉空格
				if(erasureBlank){
					item = item.trim();
					if(!item || item == ''){
						continue;
					}
				}
				
				//去掉重复数据
				if(erasureRepeat){
					if($.inArray(item,handler.fun) != -1){
						handler.repeat.push(item);
						continue;
					}
				}
				
				handler.fun.push(item);
			}
			
			//排序,数字在前面； 字符串在后面，如果都是数字，则比较大小（小的在前面）； 如果都是字符串，从头开始比较两个字符串的 ascii 码大小 
			handler.fun.sort(function(x,y){
				var typeX = typeof x;
				var typeY = typeof y;
				
				if(typeX == typeY){
					if(typeX == 'number'){
						return x - y;
					}else{
						//其它在这里只可能是字符串 TODO
						return -1;
					}
				}
				
				if(typeX == 'number'){
					return -1;
				}else{
					return 1;
				}
				
			});
			
			//重新为文本框赋值
			$(this).val(handler.fun.join('\n'));

			// 相等列写入重复数据
			handler.equal = handler.repeat;
			$('#equalColumns').html(handler.repeat.join('<br/>'));
			
			//数据计数
			$('#funCount>b:eq(0)').text(origin.fun.length);
			$('#funCount>b:eq(1)').text(handler.fun.length);
			$('#funCount>b:eq(2)').text(handler.fun.length - origin.fun.length);
		}
	}
	
	column.init();
});