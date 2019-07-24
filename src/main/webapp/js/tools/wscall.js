define(['util','dialog','chosen','jsonview'],function(util,dialog){
	var wsCall = {
			current:{}
	};
	/**
	 * webservice 调用初始化工作
	 */
	wsCall.init = function(){
		var chonseOptions = {
				 disable_search_threshold : 10,
		      no_results_text : "没有数据",
		      width:'120px'
		};
		//读取所有的已经存在的 webservice 地址
		util.requestData('/wscall/listAllAddress',null,function(addressMap){
			wsCall.addressMap = addressMap;
			var $existsws =  $('#existsws');
			if(addressMap){
				for(var wsdlKey in addressMap){
					$('<option wsdlKey="'+wsdlKey+'" value="'+addressMap[wsdlKey]+'">'+wsdlKey+'</option>').appendTo($existsws);
				}
			}
			
			$existsws.chosen($.extend({},chonseOptions,{width:'210px'}));
			$('#ports').chosen($.extend({},chonseOptions,{width:'270px'}));
			$('#operations').chosen($.extend({},chonseOptions,{width:'210px'}));
		});
		
		this.bindEvents();
		return this;
	}
	
	/**
	 * 构建入参模板
	 * html 模板
	 */
	wsCall.buildInputParam = function(){
		var $parent = $('#inputparam').empty();
		if(!wsCall.current.input){
			//如果没有入参,则不需要构建 
			return ;
		}
		buildInputParamHtml(wsCall.current.input,$parent);
	}
	
	/**
	 * 递归构建消息模板
	 */
	function buildInputParamHtml(inputParam,$parent){
		var paramName = inputParam.paramName;
		var paramType = inputParam.paramType;
		
		var $childLiNode = $('<li></li>').appendTo($parent);
		$childLiNode.append('<label>'+paramName+':</label>');
		
		if(paramType.simple){
			$childLiNode.append('<input name="'+paramName+'" />');
		}else{
			if(paramType.childParams){
				var $childUl = $('<ul name="'+paramName+'"></ul>').appendTo($childLiNode);
				for(var i=0;i<paramType.childParams.length;i++){
					var childParam = paramType.childParams[i];
					buildInputParamHtml(childParam, $childUl);
				}
			}
		}
	}
	
	/**
	 * 事件绑定
	 */
	wsCall.bindEvents = function(){
		var events = [{selector:'#existsws',types:['change'],handler:changeService},
		              {selector:'#wsdladdress',types:['change','blur'],handler:loadService},
		              {selector:'#ports',types:['change'],handler:changePorts},
		              {selector:'#operations',types:['change'],handler:changeOperations},
		              {parent:'#operationcmd',selector:'.btn',types:['click'],handler:doRequest},
		              {parent:'#outputjsoncmd,#inputjsoncmd',selector:'.btn',types:['click'],handler:doJsonOperation}];
		
		/**
		 * 做 json 操作
		 */
		function doJsonOperation(){
			var field = $(this).attr('field'),
					event = $(this).attr('event'),
					current = $(this).attr('current');
			switch(event){
			case 'view':
				dialog.create('json 树视图查看 ')
				.setContent($('#jsontreeview').show())
				.setWidthHeight('500px','90%')
				.build();
				if(field == 'input'){
					$('#jsontreeview').JSONView(JSON.stringify(wsCall.current.input));
				}else{
					$('#jsontreeview').JSONView(JSON.stringify(wsCall.current.output));
				}
				break;
			case 'toggle':
				if(current == 'expand'){
					$(this).attr('current','compress');
					$(this).find('i').removeClass('fa-compress').addClass('fa-expand');
				}else {
					$(this).attr('current','expand');
					$(this).find('i').removeClass('fa-expand').addClass('fa-compress');
				}
				if(field == 'input'){
					$('#inputjsonview').JSONView('toggle');
				}else{
					$('#outputjsonview').JSONView('toggle');
				}
				break;
			}
		}
		
		/**
		 * 向后台发送请求处理数据
		 */
		function doRequest(){
			var callMethod = $(this).attr('event');
			switch(callMethod){
			case 'buildSoapMessage':
				util.requestData('/wscall/'+callMethod,wsCall.current,function(soapMessage){
					$('#inputsoapmessage').val(soapMessage);
				});
				break;
			case 'invokeMethod':
				//发送 soap xml 消息
				var soapXml = $('#inputsoapmessage').val();
				var params = $.extend({},wsCall.current,{soapMessage:soapXml});
				util.requestData('/wscall/'+callMethod,params,function(retSoapXml){
					$('#outputsoapmessage').val(retSoapXml);
				});
				break;
			case 'callMethod':
				break;
			}
		}
		
		/**
		 * 改变 webservice 地址
		 */
		function changeService(){
			var wsdlAddress = $(this).val();
			var wsdlKey = $(this).find(':selected').attr('wsdlKey');
			wsCall.current.wsdlKey = wsdlKey;
			$('#wsdladdress').val(wsdlAddress).change();
		}
		
		/**
		 * 加载 webservice 的所有 port
		 */
		function loadService(){
			var wsdlAddress = $(this).val().trim();
			if(!wsdlAddress){
				// 空的 wsdl 地址不做解析
				return ;
			}
			wsCall.current.wsdlAddress  = wsdlAddress;
			util.requestData('/wscall/loadServiceInfo',{wsdlAddress:wsdlAddress},function(wsdlKey){
				wsCall.current.wsdlKey = wsdlKey;		//重新赋值 wsdlKey 因为有可能是需要临时解析 ws
				util.requestData('/wscall/listAllPort',{wsdlKey:wsdlKey},function(ports){
					var $ports = $('#ports').empty().append('<option value="">请选择</option>');
					if(ports){
						for(var i=0;i<ports.length;i++){
							$('<option value="'+ports[i]+'">'+ports[i]+'</option>').appendTo($ports);
						}
					}
					$ports.trigger("chosen:updated"); 			//重新渲染 chosen
				});
			});
		}
		
		/**
		 * 改变 ports 加载所有的方法
		 */
		function changePorts(){
			var portName = $(this).val().trim();
			if(!portName){
				return ;
			}
			wsCall.current.portName = portName;
			util.requestData('/wscall/listAllMethods',{wsdlKey:wsCall.current.wsdlKey,portName:portName},function(operations){
				if(operations){
					var $operations = $('#operations').empty().append('<option value="">请选择</option>');
					for(var i=0;i<operations.length;i++){
						$('<option value="'+operations[i]+'">'+operations[i]+'</option>').appendTo($operations);
					}
					$operations.trigger("chosen:updated"); 	//重新渲染 chosen
				}
			});
		}
		
		/**
		 * 改变 Operations 加载出入参数
		 */
		function changeOperations(){
			var operationName = $(this).val().trim();
			if(!operationName){
				return ;
			}
			wsCall.current.operationName = operationName;
			var data = {wsdlKey:wsCall.current.wsdlKey,portName:wsCall.current.portName,operationName:operationName};
			util.requestData('/wscall/methodInputParams',data,function(wsdlParam){
				if(wsdlParam){
					wsCall.current.input=wsdlParam;
					wsCall.buildInputParam();
					$('#inputjsonview').JSONView(JSON.stringify(wsdlParam));
				}
			});
			util.requestData('/wscall/methodOutputParams',data,function(wsdlParam){
				if(wsdlParam){
					wsCall.current.output=wsdlParam;
					$('#outputjsonview').JSONView(JSON.stringify(wsdlParam));
				}
			});
		}
		
		util.regPageEvents(events);
	}
	return wsCall.init();
});