/**
 * Created by sanri on 2017/8/2.
 */
define(['util','generate','template','dialog','chosen','icheck','jsonview','autocomplete','storage'],function(util,generate,template,dialog){
	var reqbuild = {
      connNames:['default'],
      conns:{'default':{host:'127.0.0.1',port:8080,virtualdir:'/hhx-gps-web',login:'/user/login',verifyCode:'/dync_captcha',name:'default',
          loginConfs:[{userName:'admin',passWord:'admin',checkCode:'11'}],
          requests:[]}},
      columnsConfig:{
          preColumns:[]               // 原始列列表,为将来需要做列的下划线转驼峰准备
      }
  };

   /**
		 * 内部使用的请求方法 api
		 */
   var api = {
       login:'/postman/login',
       verifyCode:'/postman/verifyCode',
       request:'/postman/request'
   };
  /**
	 * 初始化页面元素,获取保存的连接信息
	 * 
	 * @returns {reqbuild}
	 */
  reqbuild.init = function (){
      if(window.sessionStorage){
          var sessionConns = window.sessionStorage.getItem('conns');
          if(sessionConns){
              for(var key in sessionConns){
                  this.connNames.push(key);
              }
              $.extend(this.conns,sessionConns);
          }
      }
      // 渲染连接列表
      var $loginconns =  $('#loginconns');
      $('<option value="new">新增连接...</option>').appendTo($loginconns);
      for(var i=0;i<this.connNames.length;i++){
          var connName = this.connNames[i];
          var connItem = this.conns[connName];
          var connShowName = connName+'&nbsp;<span >'+connItem.host+':'+connItem.port+connItem.virtualdir+'</span>';
          var connTem = JSON.stringify(connItem);
          var $option = $('<option value="'+connName+'" title="'+connTem.replace(/"/g,'\'')+'">'+connShowName+'</option>').appendTo($loginconns);
          if(i == 0){   // 选中第一个
              $option.attr('selected','selected');
          }
      }
      $loginconns.chosen({
          width:'300px',
          disable_search_threshold : 10,
          no_results_text : "没有数据"
      });

      bindEvents();
      // 点击图片获取验证码
      $('#verifyCode').click();
      
      //获取所有的连接信息,初始化请求列表
      if(window.localStorage){
    		//储存请求信息
    		var requests = $.localStorage.get('requests') || {};
    		var currentRequests = requests['default'] || {};			//当前连接的所有请求
    		
    		var requestArray = [];		//将所有请求转换为一个数组 
    		for(var key in currentRequests){
    			requestArray.push(currentRequests[key]);
    		}
    		
    		$('#url').autocomplete(requestArray,{
          minChars: 0,
          matchContains: "word",
          autoFill: false,
          formatItem: function(row, i, max) {
            return i+'/'+max+' ['+row.method+']'+row.url;
          },
          formatMatch: function(row, i, max) {
            return row.url;
          },
          formatResult: function(row) {
            return row.url;
          }
          
        }).result(selectResult);
    		
    		/**
    		 * 选择一条结果的时候触发 
    		 * @param event
    		 * @param row
    		 * @param formatted
    		 */
    		function selectResult(event, row, formatted){
    			$('#reqmethod').val(row.method);
    			$('#url').val(row.url);
    			$('#columns').val(row.columns ? row.columns.join('\n') : '');
    		}
    	}

      return this;
  }
  
  /**
   * 获取当前选中的连接信息
   * 公开
   */
  reqbuild.getCurrent = function(){
  	var connName = $('#loginconns').val();
  	return reqbuild.conns[connName];
  }
  
  /**
	 * 页面事件绑定
	 */
  function bindEvents(){
  	 var events = [{selector:'#columns',types:['blur','keyup','change'],handler:randomColumnGenerate},			//填写请求列时触发生成列生成表格
            {parent:'#generatecolumns',selector:'.btn',types:['click'],handler:changeRandomData},	//点击列生成表格中改变随机数据
            {parent:'#generatecolumns',selector:'input[type=text]',types:['blur','keyup'],handler:changeRandomData},//修改随机数据
            {selector:'#databuild',types:['click'],handler:databuild},														//构建三种格式的数据
            {parent:'#reqaction',selector:'[event]',types:['click'],handler:sendRequest},					//发送请求
            {parent:'#cleanaction',selector:'[event]',types:['click'],handler:cleanData},					//清除数据
            {selector:'#verifyCode',types:['click'],handler:changeVerifyCode},										//更改验证码
            {selector:'#verifyCode',types:['load'],handler:removeLoading},												//验证码加载中样式
            {selector:'#login',types:['click'],handler:login},																		//登录请求
            {selector:'#loginsuccess>a[name=exit]',types:['click'],handler:logout},								//退出登录
            {parent:'#loginform',selector:'label[contenteditable]',types:['change','keyup'],handler:updateKey}];	//修改登录时的键设置																		
    util.regPageEvents(events);
    
    /**
		 * 修改登录所需要的键信息
		 */
		function updateKey(){
			var $group = $(this).closest('.form-group');
			var key = $(this).text().trim();
			$group.find('input').attr('name',key);
		}
    
    /**
		 * 获取当前连接的基础路径,因为后台 http 请求需要全路径
		 */
    function basePath(){
    	var currentConn = reqbuild.getCurrent();
    	if(currentConn){
    		return 'http://'+currentConn.host+':'+currentConn.port+currentConn.virtualdir;
    	}
    	return undefined;
    }
    
    /**
		 * 随机列生成
		 */
    function randomColumnGenerate(){
        // 获取列,并且排除重复和空列
        var columnsString = $(this).val().trim();
        if(columnsString){
            var columnsArray = columnsString.split('\n');
            if(columnsArray){
                reqbuild.columnsConfig.preColumns.length = 0;       // 清空原来的列
                for(var i=0;i<columnsArray.length;i++){
                    if(!columnsArray[i] || $.trim(columnsArray[i]) == ''){
                        continue;
                    }
                    if($.inArray(columnsArray[i],reqbuild.columnsConfig.preColumns) != -1){
                        // 重复列
                        continue;
                    }
                    reqbuild.columnsConfig.preColumns.push(columnsArray[i]);
                }
            }
        }
        // TODO 字段转换规则
        var generateList = [];
        if(reqbuild.columnsConfig.preColumns.length > 0){
            for(var i=0;i<reqbuild.columnsConfig.preColumns.length;i++){
                var columnName = reqbuild.columnsConfig.preColumns[i];
                // 获取初始值
                var column = {};
                column.initValue = generate.num(10,false,true);
                column.name = columnName;
                generateList.push(column);
            }
        }
        // 生成列表
        var randomDataHtml = template('randomdatatpl',{columns:generateList});
        $('#generatecolumns').find('tbody').html(randomDataHtml);
        // 美化复选框
        $('#generatecolumns').find('input:checkbox').iCheck({
            checkboxClass: 'icheckbox_square-green'
        });
    }

    /*
		 * 修改随机生成的数据
		 */
    function changeRandomData(){
        var $tr = $(this).closest('tr'),
            $fixedTd = $tr.find('td:last'),
            $finallyspan = $tr.find('span.finaly-value'),
            $currTd = $(this).closest('td');
        var genMethod = $currTd.attr('generate');
        var genValue = 0;

        // 加入选中样式
        $tr.find('.btn').removeClass('selected');
        $(this).addClass('selected');

        // 生成数据
        var $input = $currTd.find('input');
        var inputVal = undefined;
        if($input && $input.size() > 0){
            inputVal= $input.val().trim();
        }
        if(genMethod == 'fixed'){
            // 固定值不用生成
            genValue = inputVal;
        }else{
            var fun = generate[genMethod];
            if(fun){
                genValue = fun.apply(generate,[inputVal]);
            }else{
                // 找不到函数使用默认值,不修改
            }
        }
        // 填充到 fixed 值和最终值
        $tr.find('span.finaly-value').text(genValue);
    }
    
    /**
		 * 登录系统
		 */
    function login(e){
    	var currentConn=reqbuild.getCurrent();
    	var path = basePath();
    	var data = util.serialize2Json($('#loginform').serialize());
    	var msgEntity={body:JSON.stringify(data),contentType:'application/json'};
    	util.requestData(api.login,{url:path + currentConn.login,msgEntity:msgEntity},function(retEntity){
    		//目前只支持 响应为 json 数据
    		var body = retEntity.body;
    		if(body){
    			var sysRet=JSON.parse(body);
    			//目前只支持 errorCode == 0
    			if(sysRet.result != '0'){
    				layer.msg(body);
    				return ;
    			}
    			//请求成功
    			$('#loginform').hide();
    			$('#loginsuccess').find('[name=userconn]').text(currentConn.name);
    			$('#loginsuccess').find('[name=conninfo]').text(JSON.stringify(data));
  				$('#loginsuccess').show();
    		}
    	});
    }
    
    /**
     * 退出登录,并不会真正的退出,重新登录后替换原来的登录
     */
    function logout(){
    	$('#loginsuccess').hide();
    	$('#loginform').show();
    }

    /**
		 * 以不同的数据格式发送请求
		 */
    function sendRequest(){
        var event = $(this).attr('event'),
        		contentType = $(this).attr('contentType');
        var method = $('#reqmethod').val().trim();
        var dataBody = $('#reqdata'+event).val().trim();
        var url = $('#url').val().trim();
        var requestHead = {};
        
        if(url == ''){
        	layer.msg('需填写请求地址');
        	return ;
        }
        var path=basePath();													//当前连接的基础路径
        var currentConn = reqbuild.getCurrent();			//当前连接信息
        util.requestData(api.request,{method:method,url:path+url,msgEntity:{contentType:contentType,body:dataBody}},function(retEntity){
        	if(!retEntity){
        		layer.msg('没有响应消息');
        		return ;
        	}
        	if(retEntity.statusCode != 200){
        		layer.msg(JSON.stringify(retEntity));
        		return ;
        	}
        	//记录成功的请求数据
        	var reqData = {method:method,url:url,columns:reqbuild.columnsConfig.preColumns};
        	if(window.localStorage){
        		//储存请求信息
        		var requests = $.localStorage.get('requests') || {};
        		var currentRequests = requests[currentConn.name] || {};			//当前连接的所有请求
        		requests[currentConn.name] = currentRequests;
        		var key = reqData.method+'_'+reqData.url;
        		//如果有就覆盖,没有就新增
        		currentRequests[key] = reqData;
        		
        		$.localStorage.set('requests',requests);
        	}
        	
        	var buildDialog=dialog.create('响应消息')
        	.setWidthHeight('500px','90%');
        	//请求成功,打开对话框显示数据
        	if(retEntity.contentType == 'text/xml'){
        		//普通对话框展示消息
        		$('#normaldatadialog').text(retEntity.body);
        		buildDialog.setContent($('#normaldatadialog')).build();
        	}else{
        		//使用 json 对话框
        		$('#jsonviewdialog').JSONView(retEntity.body);
        		buildDialog.setContent($('#jsonviewdialog')).build();
        		
        	}
        });
    }
    
    /**
		 * 清除请求数据
		 */
    function cleanData(){
    	var event = $(this).attr('event');
    	if(event == 'all'){
    		$('#reqdatakeyvalue').val('');
    		$('#reqdatajson').val('');
    		$('#reqdataxml').val('');
    		return ;
    	}
    	$('#reqdata'+event).val('');
    }

    /**
		 * 构建 json,keyValue,xml 数据
		 */
    function databuild(){
        // 获取数据
        var data = {};
        $('#generatecolumns>tbody').find('tr').each(function(i){
          var $checkbox = $(this).find('input:checkbox'),
              $finalValNode = $(this).find('td:eq(3)>span'),
              $columnNode = $(this).find('td:eq(2)');
          var checked = $checkbox.is(':checked');
          var finalVal = $finalValNode.text().trim();
          var column = $columnNode.text();
          if(checked){
              data[column] = finalVal;
          }
        });

        // 数据组装
        var keyValueData = '',jsonData = JSON.stringify(data),xmlData = [];
        // 组装 keyvalue 数据
        for(var key in data){
            keyValueData+=(key+'='+data[key]);
            keyValueData+='&';
        }
        // keyvalue 加入随机时间串,防止浏览器阻止提交,也防止最一个字符问题
        keyValueData+=('t='+new Date().getTime());
        // 组装 xml 数据
        xmlData = ['<?xml version="1.0" encoding="UTF-8"?>'];
        xmlData.push('<datain>');
        for(var key in data){
            xmlData.push('<'+key+'>'+data[key]+'</'+key+'>');
        }
        xmlData.push('</datain>');

        // 写入数据
        $('#reqdatakeyvalue').val(keyValueData);
        $('#reqdatajson').val(jsonData);
        $('#reqdataxml').val(xmlData.join('\n'));
    }
    
    /**
		 * 修改验证码
		 */
    function changeVerifyCode(){
    	var currentConn = reqbuild.getCurrent();
    	if(currentConn){
    		$('#verifyCode').hide();
    		$('#verifyCode').parent().append('<span>验证码加载中</span>');
        $('#verifyCode').attr('src',util.root+api.verifyCode+'?t='+new Date().getTime()+'&url='+basePath()+currentConn.verifyCode);
    		return ;
    	}
      layer.msg('没有找到验证码接口');
    }
    
    /**
		 * 移除加载中样式
		 */
    function removeLoading(){
    	$('#verifyCode').parent().find('span').remove();
    	$('#verifyCode').show();
    }
  }
  
  return reqbuild.init();
});