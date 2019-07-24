/**
 * 树结节的右键菜单功能,因为右键功能太多,避免造成 meta.js 太过庞大,把右键功能分离
 */
define(['util','dialog','sqlclient/meta','template','generate','base64','sqlclient/relation','sqlclient/projectbuild','steps','contextMenu','icheck'],function(util,dialog,metatree,template,generate,base64,relation,projectbuild){
	var menu = {};
	
	var api = {
			buildJavaBean:'/code/build/javabean',
            buildXmlCode:'/code/build/mybatis',
			mapperClassName:'/code/mapper/className',
			downFile:'/code/downFile',
			addData:'/sqlclient/writeData',
			addMultiData:'/sqlclient/writeMultiData',
			transfer:'/sqlclient/transfer',
			exportPreview:'/sqlclient/exportPreview',
			exportPreviewDataCount:'/sqlclient/exportDataCount',
			exportProcess:'/sqlclient/exportLowMemoryMutiProcess',
			generateExportTicket:'/sqlclient/generateExportTicket',
			exportProcessQuery:'/sqlclient/exportProcessQuery',
			saveExportSql:'/sqlclient/saveExportSql',
			exportSqls:'/sqlclient/exportSqls',
			loadExportSql:'/sqlclient/loadExportSql',
			showCreateTable:'/sqlclient/showCreateTable',
			exportStruct:'/sqlclient/exportStruct',
			sqlDownFile:'/sqlclient/downFile'

	};
	
	/**
	 * 菜单初始化
	 */
	menu.init = function(){
		//初始化右键菜单
    $.contextMenu({
      selector:'#metatree li',
      zIndex:4,
      items:{		//bug 不能每个 item 都加 visible,必须要有一个没有 visible
      	projectBuild:{name:'项目构建...',icon:'copy',visible:dbVisible,callback:buildProject},
        export:{name:'导出向导',icon:'edit',visible:dbVisible,items:{
        	exportExcel:{name:'导出数据...',icon:'cut',callback:exportExcel},
			exportStruct:{name:'结构导出',icon:'copy',callback:exportStruct}
		}},
        buildRelation:{name:'建立表关系',icon:'copy',visible:dbVisible,callback:buildRelation},
		// exportExcel:{name:'导出数据...',icon:'copy',visible:dbVisible,callback:exportExcel},

        insert:{name:'添加数据...',icon:'add',callback:addData,visible:tableVisible},
        insertMulti:{name:'添加大量数据...',icon:'add',callback:addMultiData,visible:tableVisible},
        insertRelation:{name:'添加关联数据...',icon:'add',callback:addRelationData,visible:tableVisible},
        javaBean:{name:'java pojo 生成...',icon:'paste',visible:tableVisible,callback:buildJavaBean},
        mybatisCodeBuild:{name:'mybatis xml 生成...',icon:'paste',visible:tableVisible,callback:buildMybatisCode},
        columns:{name:'获取表字段',icon:'cut',visible:tableVisible,items:{
        	simple:{name:'以逗号拼接获取',icon:'copy',callback:listColumns},
        	diy:{name:'自定义设置...',icon:'copy',callback:diyTableColumns}
        }},
        dataTransf:{name:'数据转移',icon:'paste',visible:tableVisible,callback:dataTranfer},

        sep1: '---------',
		  refresh:{name:'刷新',icon:'copy',visible:excludeColumn,callback:refreshInfo},
		  property:{name:'属性',icon:'copy',callback:showConnInfo}

      }
    });
    
    bindEvents();
	}
    
  /**
   * 只有连接节点可见
   */
  function connVisible(key,opts){
  	var treeNode = findTreeNode(key, opts);
  	if(treeNode && treeNode.nodeType == 'conn'){
			return true;
		}
  	return false;
  }

	/**
	 * 只有列不可见
	 * @returns {boolean}
	 */
	function excludeColumn(key, opts) {
	  var treeNode = findTreeNode(key, opts);
	  if(treeNode && (treeNode.nodeType == 'conn' || treeNode.nodeType == 'db' || treeNode.nodeType == 'table')){
		  return true;
	  }
	  return false;
  }
  
  /**
   * 只有在数据库节点上可见
   */
  function dbVisible(key,opts){
  	var treeNode = findTreeNode(key, opts);
  	if(treeNode && treeNode.nodeType == 'db'){
			return true;
		}
  	return false;
  }
  
 	/**
	 * 只有表节点可见
	 */
	function tableVisible(key,opts){
		var treeNode=findTreeNode(key, opts);
		if(treeNode && treeNode.nodeType == 'table'){
			return true;
		}
		return false;
	}

	/**
	 * 刷新信息
	 */
	function refreshInfo(key,opts) {
		var treeNode=findTreeNode(key, opts);
		if(!treeNode){
			return ;
		}
		metatree.refresh(treeNode);
	}
	
	/**
	 * 构建表之间关系
	 */
	function buildRelation(key,opts){
		var treeNode=findTreeNode(key, opts);
		if(!treeNode){
			return ;
		}
		if(true){
			layer.msg('功能未实现');
			return ;
		}
		metatree.current.db = treeNode.originName;
		metatree.current.conn = treeNode.getParentNode().originName;
		relation.create();
	}

    /**
	 * 导出数据库表结构信息
     */
	function exportStruct() {
		var params = {
			db:metatree.current.db,
			conn:metatree.current.conn
		};

		util.requestData(api.exportStruct,params,function (filename) {
			if(filename){
                util.downFile(api.sqlDownFile,{typeName:'export',fileName:filename})
            }
        });
    }

    /**
	 * 导出数据
     */
	function exportExcel(key,opts){
		var treeNode=findTreeNode(key, opts);
		if(!treeNode){
			return ;
		}
        switchConnAndDb(treeNode);
		var build = dialog.create('导出数据配置['+treeNode.originName+']')
		.setContent($('#exportExcel'))
		.setWidthHeight('97%','90%')
			.onOpen(refreshExportSql)
			.onClose(function () {
				//关闭对话框,清除定时器
				console.log('清除定时器');
				var intervals = $('#exportExcel').data('setInterval') || [];
                for (var i = 0; i <intervals.length ; i++) {
					window.clearInterval(intervals[i]);
                }
            })
			.build();
	}
	
	/**
	 * 显示连接信息
	 */
	function showConnInfo(key,opts){
		var treeNode=findTreeNode(key, opts);
		if(!treeNode){
			return ;
		}
        switchConnAndDb(treeNode);
		if(treeNode.nodeType == 'conn'){
			//显示连接信息
			var connName = treeNode.originName;
			var connMeta = metatree.meta.connMeta[connName].connInfo;
			if(connMeta){
				$('#connInfo').find('.form-group').each(function(){
					var $ptext = $(this).find('p'),
						key = $ptext.attr('name');
					$ptext.text(connMeta[key]);
				});
				var build=dialog.create('连接属性')
				.setContent($('#connInfo'))
				.setWidthHeight('400px','400px')
				.build();
				
			}
		}else if (treeNode.nodeType  == 'table'){
			// 请求建表信息
			var params = {
                conn:metatree.current.conn,
                db:metatree.current.db,
				table:treeNode.originName
			}

			util.requestData(api.showCreateTable,params,function (result) {
                $('#showtableinfo>#tableinfo').html(result.replace(/\n/g,'<br/>'));
                dialog.create('显示表['+treeNode.originName+']信息')
                    .setContent($('#showtableinfo'))
                    .setWidthHeight('600px','80%')
                    .build();
            });
		}
	}

    /**
	 * 解决右键没有及时切换连接和数据库信息
     */
	function switchConnAndDb(treeNode) {
		if(!treeNode){
			return ;
		}
		// 不能调用点击,必须手动切换
		// metatree.ztreeConfig.callback.onClick(e,treeNode.id,treeNode);
		metatree.switchCurrent(treeNode);
    }
	
	/**
	 * 添加数据,会默认生成随机数据
	 */
	function addData(key,opts){
		var tableColumns = privateTableColumns(key,opts);
		if(!tableColumns){
			layer.msg('没有找到表格列');
			return ;
		}
		var treeNode=findTreeNode(key, opts);
		//选中表格,设置 metatree.current.table 
		metatree.current.table = treeNode.originName;
		var columns = [];
		for(var i=0;i<tableColumns.length;i++){
			columns[i] = {};
			//加入初始值,给每一列
			columns[i].name = tableColumns[i];
			columns[i].initValue = generate.num(10,false,true);
		}
		var randomDataHtml = template('randomdata',{columns:columns});
		$('#adddata').find('tbody').html(randomDataHtml);
		var build=dialog.create('添加数据['+treeNode.originName+']')
		.setContent($('#adddata'))
		.setWidthHeight('99%','90%')
		.addBtn({type:'yes',text:'确定',handler:function(index, layero){
			var params = {
					connName:metatree.current.conn,
					database:metatree.current.db,
					tableName:metatree.current.table,
					dataMap:{}
			};
			$('#adddata').find('tbody>tr').each(function(){
				var finallyVal = $(this).find('span.finaly-value').text().trim(),
					columnname = $(this).attr('columnname'),
					checked = $(this).find(':checkbox').is(':checked');
				if(checked){
					params.dataMap[columnname]= finallyVal;
				}
			});
			util.requestData(api.addData,params,function(ret){
				layer.close(index);
				layer.msg(ret);
			});
		}})
		.build();
		
		//美化复选框
		$('#adddata').find(':checkbox').iCheck({
      checkboxClass: 'icheckbox_square-green',
      radioClass: 'iradio_square-green'
		});
	}
	
	/**
	 * 添加大量数据
	 * 1.前端对于数据生成不做处理,后台自动根据字段类型生成
	 * 2.对某些字段有固定值需指定
	 * 3.排除字段需要指定
	 */
	function addMultiData(key,opts){
		var tableColumns = privateTableColumns(key,opts);
		if(!tableColumns){
			layer.msg('没有找到表格列');
			return ;
		}
		var treeNode=findTreeNode(key, opts);
		//选中表格,设置 metatree.current.table 
		metatree.current.table = treeNode.originName;
		var randomDataHtml = template('randommultidata',{columns:tableColumns});
		$('#addmultidata').find('tbody').html(randomDataHtml);
		var build=dialog.create('添加大量数据['+treeNode.originName+']')
		.setContent($('#addmultidata'))
		.setWidthHeight('400px','80%')
		.addBtn({type:'yes',text:'确定',handler:function(index, layero){
			var params = {
					connName:metatree.current.conn,
					database:metatree.current.db,
					tableName:metatree.current.table,
					dataMap:{}
			};
			//查询添加数量 
			params.count = $('#addmultidata').find('input[name=count]').val().trim();
			$('#addmultidata').find('tbody>tr').each(function(){
				var finallyVal = $(this).find('input[name=fixedval]').text().trim(),
					columnname = $(this).attr('columnname'),
					checked = $(this).find(':checkbox').is(':checked');
				if(checked){
					params.dataMap[columnname]= finallyVal;
				}
			});
			util.requestData(api.addMultiData,params);
			layer.close(index);
			layer.msg('正在等待后台生成');
		}}).build();
		
		//美化复选框
		$('#addmultidata').find(':checkbox').iCheck({
      checkboxClass: 'icheckbox_square-green',
      radioClass: 'iradio_square-green'
		});
	}
	
	/**
	 * 列出表字段,
	 * 以逗号拼接获取
	 */
	function listColumns(key,opts){
		var tableColumns = privateTableColumns(key,opts);
		if(tableColumns){
			layer.alert(tableColumns.join(','));
		}
	}
	
	/*
	 * 抽出方法,查找表格列
	 */
	function privateTableColumns(key, opts){
		var treeNode=findTreeNode(key, opts);
		if(!treeNode){
			return undefined;
		}
		if(treeNode.children && treeNode.children.length > 0){
			var tableColumns = [];
			for(var i=0;i<treeNode.children.length;i++){
				tableColumns.push(treeNode.children[i].originName);
			}
			return tableColumns;
		}
		return undefined;
	}
	
	/**
	 * 自定义获取表字段
	 */
	function diyTableColumns(key,opts){
		var tableColumns = privateTableColumns(key,opts);
		if(!tableColumns){
			layer.msg('没有表格列');
			return ;
		}
		var treeNode=findTreeNode(key, opts);
		var buildDialog = dialog.create('自定义表 ['+treeNode.originName+'] 字段获取')
				.setContent($('#diyTableColumns'))
				.setWidthHeight('300px','270px')
				.addBtn({type:'yes',text:'确定',handler:function(index, layero){
					var setting = util.serialize2Json($('#diyTableColumns>form').serialize());
		  		if(treeNode.children && treeNode.children.length > 0){
		  			var result = tableColumns.join(setting.prefix+setting.join+setting.suffix);
		  			//替换特殊字符 
		  			result = result.replace(/\</g,'&lt;').replace(/\>/g,'&gt;');
		  			//替换换行符
		  			result = result.replace(/\\n/g,'<br/>');
		  			layer.alert(result);
		  			layer.close(index);
		  		}
				}})
				.build();
	}
	
	/**
	 * 随机关联添加数据
	 * 暂定思路:
	 * 	1.选取需要添加数据的表
	 * 	2.加载之前已经设定好的关联关系(存文件)
	 * 	3.重新设定关联关系或跳过
	 * 	4.根据关系生成数据
	 *
	 * 	最终思路,确定于 2019-03-06 :
	 * 		1.选取需要建立数据关联的表
	 * 		2.建立关联
	 * 			2.1 自动取好别名
	 * 			2.2 用户手动输入关联关系,如 a.userId = b.userId
	 * 			2.3 后台解析关联
	 * 		3. 数据由 excel 导入
	 * 			3.1每一张 sheet 导入一张表
	 * 			3.2第一张 sheet 页是最底层的表,后面的表依次依赖于第一张表
	 * 		关于数据联系算法 :
	 * 		    暂未确定???
	 */
	function addRelationData(key,opts){
		var treeNode=findTreeNode(key, opts);
		if(!treeNode){
			layer.msg('没有选中节点');
			return ;
		}
		//构建对话框
		var buildDialog = dialog.create('['+treeNode.originName+'] 添加关联数据')
		.setContent($('#relationdata'))
		.setWidthHeight('80%','98%').build();
		
		require(['steps'],function(){
			$('#relationdata').steps({
				labels:{
					finish:'完成',
					previous:'上一步',
					next:'下一步'
				},
				headerTag: "h4",
			  bodyTag: "section",
			  transitionEffect: "slideLeft",
			  autoFocus: true,
			  onStepChanging:changeStep
			});
		});
		
		/*
		 * 添加关联数据下一步 
		 */
		function changeStep(){
			
			return true;
		}
	}
	
	
	
	/**
	 * 构建 java bean 
	 */
	function buildJavaBean(key,opts){
		var treeNode=findTreeNode(key, opts);
		if(!treeNode){
			layer.msg('没有选中节点');
			return ;
		}
		//所有字段追加进排除框中
		$('#generatepojoexclude').empty();
		if(treeNode.children && treeNode.children.length > 0){
			for(var i=0;i<treeNode.children.length;i++){
				var child = treeNode.children[i];
				if(child.originName == 'id'){
				    //如果当前列为 id,默认排除
				    $('#generatepojoexclude').append('<label class="checkbox-inline"><input name="excludecolumn" checked type="checkbox" value="'+child.originName+'">'+child.originName+'</label>');
                }else{
				    $('#generatepojoexclude').append('<label class="checkbox-inline"><input name="excludecolumn" type="checkbox" value="'+child.originName+'">'+child.originName+'</label>');
                }

			}
		}
		
		//构建对话框
		var buildDialog = dialog.create('['+treeNode.originName+'] java pojo 生成')
		.setContent($('#generatepojo'))
		.setWidthHeight('500px','90%')
		.addBtn({type:'yes',text:'生成',handler:function(index){
			var packageName = $('#generatepojo').find('input[name=packageName]').val().trim();
			var baseEntity = $('#generatepojo').find('input[name=baseEntity]').val().trim();
			var model = $('#generatepojo').find(':radio:checked').val();
			var supports = [];
			$('#generatepojo').find(':checkbox:checked').each(function () {
                supports.push($(this).val());
            });
			var interfaces = [],excludeColumns = [];
			$('#generatepojo').find(':checkbox[name=interfaces]:checked').each(function(){
				interfaces.push($(this).val());
			});
			$('#generatepojoexclude').find(':checkbox[name=excludecolumn]:checked').each(function(){
				excludeColumns.push($(this).val());
			});
			var params = {
					connName:metatree.current.conn,
					dbName:metatree.current.db,
					tableName:treeNode.originName,
					packageName:packageName,
					baseEntity:baseEntity,
					model:model,
					interfaces:interfaces,
					excludeColumns:excludeColumns,
                    supports:supports
			}
			util.requestData(api.buildJavaBean,params,function(filename){
				$('<iframe id="pojo_'+filename+'" src="'+util.root+api.downFile+'?typeName=pojo&fileName='+filename+'" style="display:none"></iframe>').appendTo($('body'))
				setTimeout(function(){
					$('iframe#pojo_'+filename).remove();
				},1000);
				layer.close(index);
			});
		}}).build();
		//必须显示之后,才能调用 checkbox 和 radio 美化
		require(['icheck'],function(){
			$(':radio,:checkbox',$('#generatepojo')).iCheck({
  			checkboxClass: 'icheckbox_square-green',
  			radioClass: 'iradio_square-green'
  		});
		});
	}
	
	/**
	 * 数据转移功能
	 */
	function dataTranfer(key,opts){
		var treeNode=findTreeNode(key, opts);
		if(!treeNode){
			layer.msg('没有选中节点');
			return ;
		}
		metatree.current.table = treeNode.originName;
		metatree.current.db = treeNode.getParentNode().originName;
		metatree.current.conn = treeNode.getParentNode().getParentNode().originName;

		layer.prompt({title:'处理类',value:'com.sanri.app.jdbc.datatransfer.impl.TestTransferImpl'},function(value, index, elem){
			if(!value){
				layer.msg('需要提供处理类');
				return ;
			}
			var params = $.extend({},metatree.current,{handlerClazz:value});
			util.requestData(api.transfer,params);
		  layer.close(index);
		  layer.msg('正在进行数据转移');
		});
	}
	
	/**
	 * 构建项目代码
	 */
	function buildProject(key,opts){
		var treeNode=findTreeNode(key, opts);
		if(!treeNode){
			layer.msg('没有选中节点');
			return ;
		}
		//构建项目代码生成数据
		if(!treeNode.children || treeNode.children.length == 0){
			layer.msg('当前库没有打开,无法生成');
			return ;
		}
		metatree.current.db = treeNode.originName;
		metatree.current.conn = treeNode.getParentNode().originName;
		projectbuild.init();		//开始项目构建 
	}

    /**
     * 单表模块代码构建
     * @param key
     * @param opts
     */
	function buildMybatisCode(key,opts){
	    var treeNode=findTreeNode(key, opts);
		if(!treeNode){
			layer.msg('没有选中节点');
			return ;
		}

		//当打开一张表时,请求后台获取类名
		util.requestData(api.mapperClassName,{tableName:treeNode.originName},function (className) {
			$('#generatemybatis').data('className',className);
			$('#generatemybatis').data('tableName',treeNode.originName);
        });

		var buildDialog = dialog.create('['+treeNode.originName+'] java mybatis 代码 生成')
		.setContent($('#generatemybatis'))
		.setWidthHeight('500px','90%')
        .addBtn({type:'yes',text:'生成',handler:function(index){
            var namespace = $('#generatemybatis').find('input[name=namespace]').val().trim();
			var beanType = $('#generatemybatis').find('input[name=beanType]').val().trim();

            var params = {
                connName:metatree.current.conn,
                dbName:metatree.current.db,
                tableName:treeNode.originName,
                namespace:namespace,
                beanType:beanType
            };

            util.requestData(api.buildXmlCode,params,function(filename){
                if(filename) {
                    $('<iframe id="mybatis_' + filename + '" src="' + util.root + api.downFile + '?typeName=mybatis&fileName=' + filename + '" style="display:none"></iframe>').appendTo($('body'))
                    setTimeout(function () {
                        $('iframe#mybatis_' + filename).remove();
                    }, 1000);
                    layer.close(index);
                }else{
                    layer.msg("文件未生成");
                }
            });
        }}).build();
    }
	
	/**
	 * 查找到当前右击的树节点
	 */
	function findTreeNode(key,opts){
		if(opts.$trigger ){
			var treeId = opts.$trigger.attr('id');
	    	return metatree.getTreeNodeByTreeId(treeId);
		}
		return null;
	}

    /**
	 * 刷新导出数据业务列表
     */
	function refreshExportSql(select){
        $('#exportbusiness').empty();

		util.requestData(api.exportSqls,{db:metatree.current.db},function (exports) {
			for(var i=0;i<exports.length;i++){
				$('#exportbusiness').append('<option value="'+exports[i]+'">'+exports[i]+'</option>');
			}

			if(select){
                $('#exportbusiness').val(select);
			}else{
                $('#exportbusiness>option:first').attr('selected',true);
			}

			//加载相应 sql 语句
            $('#exportbusiness').change();

        });
	}
	
	/**
	 * 页面事件绑定
	 */
	function bindEvents(){
		var events = [{parent:'#adddata',selector:'.btn',types:['click'],handler:changeRandomData},
			{selector:'#generatemybatis input[name=basePackage]',types:['keyup'],handler:cascadeName},
			{selector:'#preview',types:['click'],handler:callPreview},
			{selector:'#exportProcess',types:['click'],handler:exportProcess},
			{selector:'#newexport',types:['click'],handler:openNewExportDialog},
            {selector:'#saveexport',types:['click'],handler:saveExport},
            {selector:'#exportbusiness',types:['change'],handler:loadExportSql}];
		util.regPageEvents(events);

        /**
		 * 加载导出 sql
         */
		function loadExportSql(){
			var val = $(this).val();
			util.requestData(api.loadExportSql,{subject:val,db:metatree.current.db},function (result) {
				$('#previewSql').val(result);

				//执行预览查询
				$('#preview').click();
            });
		}

        /**
		 * 打开新业务对话框
         */
		function openNewExportDialog(){
			dialog.create('创建新业务')
			.setContent($('#addbusinesssql'))
			.setWidthHeight('500px','90%')
			.addBtn({type:'yes',text:'添加',handler:function(index, layero){
				var $form  = $('#addbusinesssql').find('form');
				var data = util.serialize2Json($form.serialize());
				if(!data.subject || !data.sql){
					layer.msg('输入 业务 和 sql 保存数据');
					return ;
				}
				data.db = metatree.current.db;
				util.requestData(api.saveExportSql,data,function(){
					layer.close(index);
				});

				// 刷新业务列表,并初始化为当前
				refreshExportSql(data.subject);
            }})
			.build();
		}

        /**
		 * 保存当前业务修改
         */
		function saveExport(){
			var subject = $('#exportbusiness').val();
			var sql = $('#previewSql').val();
			util.requestData(api.saveExportSql,{subject:subject,sql:sql,db:metatree.current.db});
		}

        /**
		 * 导出数据并查看进度
         */
		function exportProcess() {
			//获取入场券
			util.requestData(api.generateExportTicket,function (ticket) {
				//创建遮罩
				var $processMask = $('<div class="progress-mask" id="exportprocess"></div>').appendTo('#exportExcel');

				//可能后台查询总数需要耗时,所以先查到有多少个进度条,这段时间为准备导出中
				// layer.msg('正在为导出做准备');
				var index = layer.load(1, {
				  shade: [0.1,'#fff']
				});

				//首先创建查看进度订时器,每 0.5 秒查看一下进度
				$('#exportExcel').addClass('position-relative');

				//是否有追加过进度条
				var createProcess = false;

				var interval = setInterval(function () {
					util.requestData(api.exportProcessQuery,{ticket:ticket},function (processes) {
						if(processes && !createProcess){
							if(processes.length == 1 && processes[0].id == 0){
								//是多线程导出,需要重新查询
								return ;
							}
							// 如果没有追加过进度条,并且有进度了; 添加进度条;清除加载器
							layer.close(index);

							var $currentProcess = null;
							for(var i=0;i<processes.length;i++){
								$currentProcess = $(template('processtemplate',{id:processes[i].id,index:i,name:processes[i].name})).appendTo($processMask).css({top:((i+1)* 5) +'%'});
							}
							if(processes.length == 1){
								// 如果只有一个进度条,则居中显示
								$currentProcess.css('top','50%');
							}
							createProcess = true;
						}else if(processes){
							//进度的维护
							for(var i=0;i<processes.length;i++){
								var $process = $('#exportprocess').find('.progress[name=process_'+processes[i].id+']')
								$process.find('.progress-bar').css('width',processes[i].percent+'%').text(processes[i].percent+'%');

								//如果有多个进度,总进度为 id=0 的进度
								if(processes[i].id == 0 && processes[i].percent == 100){
									closeMask(interval);
								}
							}
							//单进度条的移除
							if(processes.length == 1 && processes[0].percent == 100){
								closeMask(interval)
							}
						}

					});
				},100);

				//绑定所有定时器到对话框,方便后面清除定时器
				var intervals = $('#exportExcel').data('setInterval') || [];
				intervals.push(interval);
				$('#exportExcel').data('setInterval',intervals);

				var originSql = $('#previewSql').val().trim();
				if(!originSql){
					layer.msg('sql 为空');
					return ;
				}
				var sqlBase64 = base64.encode64(originSql);
				//然后开始下载
				var params = {
					conn:metatree.current.conn,
					db:metatree.current.db,
					sql:sqlBase64,
					ticket:ticket
				}
				util.downFile(api.exportProcess,params,800000,function () {
					// 超时后清除定时器
					window.clearInterval(interval);
				});
            });
        }

        /**
		 * 关闭遮罩层
         * @param interval
         */
        function closeMask(interval) {
			setTimeout(function () {
				$('#exportExcel').removeClass('position-relative');
				$('#exportExcel').find('.progress-mask').remove();
				window.clearInterval(interval);
			},1000);
        }

        /**
		 * 预览 sql 执行结果
         */
		function callPreview(){
			var originSql = $('#previewSql').val().trim();
			if(!originSql){
				return ;
			}
			var sqlBase64 = base64.encode64(originSql);
			var params = {
				conn:metatree.current.conn,
                db:metatree.current.db,
				sql:sqlBase64
			}
			//请求数据量
			var $dataTotal = $('#datainfoshow>span>b[name=total]').empty().data('dataCounted',false);
			$dataTotal.append('<img src="../../images/loading3.gif" width="16" height="16" />')
			util.requestData(api.exportPreviewDataCount,params,function (result) {
				$dataTotal.text(result);
				$dataTotal.data('dataCounted',true);
				$dataTotal.data('dataTotal',result);
            });

			//请求结果集
			util.requestData(api.exportPreview,params,function (result) {
				if(result){
					//拼接头部
					var $header = $('table#datapreview>thead').empty();

					var header = result.header;
					var headerHtml = [];headerHtml.push('<tr>');
					for(var i=0;i<header.length;i++){
						headerHtml.push('<td>'+header[i]+'</td>');
					}
					headerHtml.push('</tr>');

					$header.append(headerHtml.join('\n'));

					//拼接 body
					var $body = $('table#datapreview>tbody').empty();

					var body = result.rows ;var bodyHtml = [];
					for (var i=0;i<body.length;i++){
						bodyHtml.push('<tr>');
						for (var j=0;j<header.length;j++){
							bodyHtml.push('<td>'+body[i][j]+'</td>')
						}
						bodyHtml.push('</tr>');
					}

					$body.append(bodyHtml.join('\n'));
				}
            });
		}

        /**
		 * 级联包名改动
         */
		function cascadeName() {
			var className = $('#generatemybatis').data('className');
			$('#generatemybatis input[name=namespace]').val($(this).val()+'.mapper.'+className+'Mapper');
			$('#generatemybatis input[name=beanType]').val($(this).val()+'.pojo.'+className);
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
			
			//加入选中样式
			$tr.find('.btn').removeClass('selected');
			$(this).addClass('selected');
			
			//生成数据
			var $input = $currTd.find('input');
			var inputVal = undefined;
			if($input && $input.size() > 0){
				inputVal= $input.val().trim();
			}
			if(genMethod == 'fixed'){
				//固定值不用生成
				genValue = inputVal;
			}else{
				var fun = generate[genMethod];
				if(fun){
					genValue = fun.apply(generate,[inputVal]);
				}else{
					//找不到函数使用默认值,不修改
				}
			}
			//填充到 fixed 值和最终值
			$tr.find('span.finaly-value').text(genValue);
		}
	}
	return menu;
});