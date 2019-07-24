/**
 * 依赖
 * @link util 工具类
 * @link tabletree 连接表格树
 * @link code codeMirror sql 代码输入
 * @link output 结果输出
 * @link quicksql 快速生成 sql 
 */
define(['util','dialog','sqlclient/meta','sqlclient/code','sqlclient/output','sqlclient/quicksql','sqlclient/rightmenu','chosen'],function(util,dialogutil,metatree,code,output,quicksql,rightmenu){
	var sqlclient = {dialog:{}};
	
	/**
	 * 打开服务器上的文件列表
	 */
	sqlclient.filelist = function(){
		//打开对话框
		var build=dialogutil.create('文件列表')
		.setContent($('#files'))
		.setWidthHeight('40%','90%')
		.build();
		//保存对话框信息
		sqlclient.dialog['filelist']=build;
		
		//加载第一页的文件列表
		var page = {currentPage:0,pageSize:9};
		loadData();
		//事件绑定
		$('#prevpage').off().bind('click',function(){
			page.currentPage--;
			if(page.currentPage<0){
				page.currentPage = 0;
			}
			loadData();
		});
		$('#nextpage').off().bind('click',function(){
			page.currentPage++;			//上限不管
			loadData();
		});
		
		//加载数据
		function loadData (){
			$('#listfiles').empty();
			util.requestData('/sqlclient/sqlList',page,function(result){
				total = result.total;
				//添加数据
				for(var i=0;i<result.files.length;i++){
					var file = result.files[i];
					$('<li class="list-group-item" filename="'+file.name+'"><p>'+file.name+' : '+file.lastModified+'</p><button class="btn btn-success btn-sm"><i class="icon-download"></i>下载</button></li>').appendTo($('#listfiles'));
				}
			});
		}
	}
	
	/**
	 * 关于
	 */
	sqlclient.about = function(){
//		util.dialogHtml($('#about'),'关于','400px','200px');
		dialogutil.create('关于')
			.setContent($('#about'))
			.setWidthHeight('400px','200px')
			.addBtn({type:'yes',text:'捐赠',handler:function(){
				window.open('https://user.qzone.qq.com/2441719087/infocenter?ptsig=LzK-iq2Q6CvpVV2Zvgx7zEYXl*AxIUxwXKPV5Wu0PAQ_','_blank');
			}})
			.build();
	}
	/**
	 * 执行 sql 
	 */
	sqlclient.executeSql = function(){
		code.executeSqls(metatree.current.conn,metatree.current.db);
	}

	/**
	 * sqlclient 重新布置
	 */
	sqlclient.relayout = function(){
		var clientWidth = window.document.documentElement.clientWidth;
		var clientHeight = window.document.documentElement.clientHeight;

		metatree.relayout(clientWidth,clientHeight);
		output.relayout(clientWidth,clientHeight);
		
		$('body.full-window').css({
			width:clientWidth,
			height:clientHeight
		});
		$('body.full-window').find('.fit').each(function(){
			var $parent = $(this).parent();
			$(this).css({
				width:$parent.width(),
				height:$parent.height()
			});
		});
	}
	
	/**
	 * sqlclient 初始化
	 */
	sqlclient.init = function(){
		bindEvents();
		
		sqlclient.relayout();
		metatree.init();
		code.init();
		output.init();
		rightmenu.init();			//最后初始化右键菜单
	}

	function bindEvents(){
		var EVENTS = [{parent:'#functions',selector:'li',types:['click'],handler:callInterface},
		              {parent:'#listfiles',selector:'li',types:['click'],handler:loadSqls}];
		util.regPageEvents(EVENTS);
		/**
		 * 调用相应功能接口
		 */
		function callInterface(){
			var fun = $(this).attr('event');
			switch(fun){
			case 'save':
				var filename = $(this).data('filename');	// 有可能为空
				var sqls = code.getAllString();
				util.requestData('/sqlclient/saveSql',{fileName:filename,sqls:sqls},function(){
					layer.msg('保存成功');
				});
				
				break;
			case 'open':
				sqlclient.filelist();
				break;
			case 'execute':
				sqlclient.executeSql();
				break;
			case 'newconn':
				var build=dialogutil.create('新连接')
						.setContent($('#newconn'))
						.setWidthHeight('400px','500px')
						.addBtn({type:'yes',text:'确定',handler:function(index, layero){
							var data = util.serialize2Json($('#newconn>form').serialize());
							util.requestData('/sqlclient/createConnection',{connectionInfo:data},function(ret){
								layer.close(index);
								if(ret == -1){
									layer.msg('新建连接失败,已存在连接名称');
									return ;
								}
								layer.msg('新建连接成功');
								//TODO 树添加连接结点
							});
						}})
						.build();
				//初始化单选框
				require(['icheck'],function(){
					$(':radio','#newconn').iCheck({
						checkboxClass: 'icheckbox_square-green',
						radioClass: 'iradio_square-green'
					});
				});
				break;
			case 'help':
				break;
			case 'about':
				sqlclient.about();
				break;
			}
		}
		
		/**
		 * 加载 sql 语句
		 */
		function loadSqls(){
			var fileName = $(this).attr('filename');
			//保存现在编辑的文件信息
			$('#functions>li[event=save]').data('filename',fileName);
			$('#functions>li[event=save]>button').html('<i class="icon-save"></i>&nbsp;保存('+fileName+')');
			util.requestData('/sqlclient/readSqls',{fileName:fileName},function(sqls){
				code.setValue(sqls);
				//关闭层信息
				layer.close(sqlclient.dialog['filelist'].index);
			});
		}
	}
	
	sqlclient.init();
	$(window).resize(sqlclient.relayout);
});