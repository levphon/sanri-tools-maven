/**
 * 为避免 rightmenu.js 过于庞大,把项目构建单独抽出 
 */
define(['util','dialog','sqlclient/meta','template','steps','icheck','jsonview','javabrush','xmlbrush'],function(util,dialog,metatree,template){
	var projectBuild = {
			configs:{},
			templates:{},
			templateMirror : {'4':'controller','5':'service','6':'serviceImpl','7':'dao','8':'daoImpl','9':'mapper','10':'xml'}
	};
	
	/**
	 * 开始项目构建
	 */
	projectBuild.init = function(){
		loadTables();					//加载所有的表
		renderConfigs();			//渲染配置信息
		openSteps();					//开始执行流程
		bindEvents();				//事件绑定
	}
	
	/**
	 * 加载所有的表格
	 */
	function loadTables(){
		projectBuild.currentDb = metatree.getCurrentDb();
		if(projectBuild.currentDb && projectBuild.currentDb.tables ){
			var tablesHTML = template('generatetablestemplate',projectBuild.currentDb);
			$('#generateprojectcode>section>table[name=generatetables]').find('tbody').html(tablesHTML);
		}
	}
	
	/**
	 * 渲染配置包括
	 * pojo 配置
	 * 	生成模式,基类继承,所有表排除字段,实现接口
	 * action,entity,service,service/impl 包配置
	 * mybatis: mapper 配置
	 * 其它  dao ,dao/impl 配置
	 */
	function renderConfigs(){
		
	}
	
	/**
	 * 绑定页面事件
	 */
	function bindEvents(){
		var events = [{parent:'#generateprojectcode',selector:'table[name=generatetables] input[name=checkall]',types:['click'],handler:selectAll},
		              {parent:'#generateprojectcode',selector:'.list-group-item',types:['click'],handler:selectFramework},
		              {parent:'#generateprojectcode',selector:'input[name=basePackage]',types:['keyup','blur'],handler:changeBasePkg}];
		util.regPageEvents(events);
		
		/**
		 * 表格区的全选,全不选
		 */
		function selectAll(){
			var $table = $(this).closest('table'),
				checked = $(this).is(':checked');
			$table.find('tr input').prop('checked',checked);
		}
		
		/**
		 * 框架选择
		 */
		function selectFramework(){
			$(this).siblings().removeClass('active');
			$(this).addClass('active');
		}
		
		/**
		 * 修改基础包
		 */
		function changeBasePkg(){
			var $panelBody = $(this).closest('.panel-body');
			var basePkg = $(this).val();
			$panelBody.find('input[key]').each(function(){
				var key = $(this).attr('key');
				$(this).val(basePkg+'.'+key);
			});
		}
		
	}
	
	
	/**
	 * 打开对话框,开始流程
	 */
	function openSteps(){
		var nowDb = metatree.current.db;
		var buildDialog = dialog.create('['+nowDb+'] 项目代码生成')
		.setContent($('#generateprojectcode'))
		.setWidthHeight('99%','98%').build();
		
		//美化单复选,美化后无法选择
//		$('#generateprojectcode').find(':checkbox,:radio').iCheck({
//      checkboxClass: 'icheckbox_square-green',
//      radioClass: 'iradio_square-green'
//		});
		//开始流程
		$('#generateprojectcode').steps({
			labels:{
				finish:'完成',
				previous:'上一步',
				next:'下一步'
			},
			headerTag: 'h6',
		  bodyTag: 'section',
		  transitionEffect: 'slideLeft',
		  autoFocus: true,
		  onStepChanging:changingStep,
		  onStepChanged:changedStep,
		  onFinishing:submitSettings
		});
	}
	
	/**
	 * 向后台提交配置,并下载生成的文件 
	 */
	function submitSettings(event, currentIndex){
		var params = $.extend({},projectBuild.configs,{templates:projectBuild.templates});
		//添加连接信息
		$.extend(params,{connName:metatree.current.conn,dbName:metatree.current.db});
		util.requestData('/code/buildProject',{generateConfig:params},function(filename){
			if(filename){
				util.downFile('/code/downFile',{typeName:'projectCode',fileName:filename});
			}
		});
		return true;
	}
	
	/**
	 * 流程改变结束 
	 */
	function changedStep(event, currentIndex, priorIndex){
		//获取配置选项
		var options = $('#generateprojectcode').data('options');
  	var currentStep = $('#generateprojectcode').steps('getCurrentStep');
  	var $currentBody = $('#generateprojectcode').find(options.bodyTag+':eq('+currentIndex+')');
  	$.extend(currentStep,{body:$currentBody});
  	
  	if(currentIndex == 11){
  		//以 json 形式展示配置,排除不存在的模板
			var $configs = currentStep.body.find('.panel-body[name=configs]');
			$configs.JSONView(JSON.stringify(projectBuild.configs)).JSONView('toggle');
			//分模块展示模板信息
			var $templates = currentStep.body.find('.panel-body[name=templates]').empty();
			for(var templateKey in projectBuild.templates){
				var brush = 'java';
				if(templateKey == 'xml'){
					brush = 'xml';
				}
				$templates.append('<div class=".template-code"><label>'+templateKey+'</label><pre class="brush:'+brush+';">'+projectBuild.templates[templateKey]+'</pre></div>');
			}
			//使用插件美化代码
			SyntaxHighlighter.highlight();
  	}
  	
  	//加载模板原始数据
  	switch(currentIndex){
  	case 4:
  	case 5:
  	case 6:
  	case 7:
  	case 8:
  	case 9:
  	case 10:
  		var templateName = projectBuild.templateMirror[currentIndex+''];
  		var params = {frameworkName:projectBuild.configs.framework,templateName:templateName};
			util.requestData('/code/loadTemplate',params,function(ret){
				var $textArea = currentStep.body.find('textarea');
				$textArea.data('result',ret.result);			//存取模板结果
				if(ret.result == '0'){
					$textArea.val(ret.template);
				}else{
					$textArea.val('没有 '+projectBuild.configs.framework+' 的当前模板['+templateName+']配置');
				}
			});
  		break;
  	}
	}
	
	/**
	 * 改变流程
	 */
	function changingStep(event, currentIndex, newIndex){
		var currentStep = $('#generateprojectcode').steps('getCurrentStep'),
			nextStep = $('#generateprojectcode').steps('getStep',newIndex);
		//获取配置选项
		var options = $('#generateprojectcode').data('options');
		
		var $currentBody = $('#generateprojectcode').find(options.bodyTag+':eq('+currentIndex+')'),
			$nextBody = $('#generateprojectcode').find(options.bodyTag+':eq('+newIndex+')');
		$.extend(currentStep,{body:$currentBody});
		$.extend(nextStep,{body:$nextBody});
		if(currentIndex > newIndex){
			//住回走
		}else{
			//往前走
			switch(currentIndex){
			case 1:
				//记住选择的表
				var selectedTables = [];
				currentStep.body.find('tr').each(function(){
					if($(this).attr('name') == 'checkall'){
						return true;
					}
					var tablename = $(this).attr('tablename'),
							checked = $(this).find('td:first>input').is(':checked');
					if(checked){
						selectedTables.push(tablename);
					}
				});
				projectBuild.configs.tables = selectedTables;
				break;
			case 2:
				//选择框架,记住选择的是哪个框架
				var frameworkName = currentStep.body.find('a.active').attr('name');
				projectBuild.configs.framework = frameworkName;
				if(frameworkName == 'ssh'){
					nextStep.body.find('input[name=model][value=hibernate]').prop('checked',true);
				}else if(frameworkName == 'ssm'){
					nextStep.body.find('input[name=model][value=normal]').prop('checked',true);
				}
				break;
			case 3:
				//记住配置信息
				var configs = util.serialize2Json(currentStep.body.find('form').serialize());
				$.extend(projectBuild.configs,configs);
				break;
			case 4:
			case 5:
			case 6:
			case 7:
			case 8:
			case 9:
			case 10:		//配置确认
				var templateKey = projectBuild.templateMirror[currentIndex+''];
				var $textArea = currentStep.body.find('textarea');
				if($textArea.data('result') == '0'){
					projectBuild.templates[templateKey] = $textArea.val();
				}
			}
		}
		
		return true;
	}
	
	return projectBuild;
});