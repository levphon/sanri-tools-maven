define(['util','dialog','domready'],function(util,dialog){
	console.log('fetchfile page ');
	var fetchfile = {};
	
	/**
	 * 初始化工作
	 */
	fetchfile.init = function(){
		var EVENTS = [{selector:'#seefiles',types:['click'],handler:fetchfile.seefiles},
		              {selector:'#handlePaths',types:['click'],handler:fetchfile.handlePaths},
		              {selector:'#downfiles',types:['click'],handler:fetchfile.downfiles},
		              {selector:'#files',types:['blur'],handler:fetchfile.handlePaths},
		              {selector:'#cutpath',types:['blur','keyup'],handler:fetchfile.handlePaths},
		              {selector:'#mergeVersion',types:['click'],handler:mergeVersion},
		              {parent:'#errorfiles',selector:'.badge',types:['click'],handler:fetchfile.downhistory},
		              {selector:'#deployPreview',types:['click'],handler:fetchfile.deployPreview},
		              {selector:'#deployOnline',types:['click'],handler:fetchfile.deployOnline}];
		util.regPageEvents(EVENTS);
		
		/**
		 * 版本合并
		 */
		function mergeVersion(){
			var version = $('#output').val().trim();
			if(version == ''){
				return ;
			}
			util.requestData('/filefetch/mergeVersion',{version:version},function(filename){
				downfile(filename);
			});
		}
	}
	/**
	 * 路径预处理
	 */
	fetchfile.handlePaths = function(){
		var pathArray=getpathArray();
		if(pathArray != null){
			var cutPath = $('#cutpath').val().trim();
			var leavePaths = [];
			for(var i=0;i<pathArray.length;i++){
				var currentPath = $.trim(pathArray[i]);
				if(currentPath != ''){
					currentPath = currentPath.replace(cutPath,'');
					if(currentPath != ''){
						leavePaths.push(currentPath);
					}
				}
			}
//			fetchfile.pathArray = leavePaths;
			//去除重复路径 add by sanri at 2017/07/20
			fetchfile.pathArray = util.CollectionUtils.uniqueSimpleArray(leavePaths);
			//得到最终路径
			$('#files').val(fetchfile.pathArray.join('\n'));
			return ;
		}
		$('#files').val('');
	}
	
	fetchfile.downhistory = function(){
		var $li = $(this).closest('li'),
			filename=$li.attr('filename');
		downfile(filename);
	}
	/**
	 * 查看服务器当前所有文件
	 */
	fetchfile.seefiles = function(){
		util.requestData('/filefetch/listAllFiles',function(files){
			if(files && files.length> 0){
				$('#errorfiles').empty();
				for(var i=0;i<files.length;i++){
					$('#errorfiles').append('<li filename="'+files[i]+'" class="list-group-item">'+files[i]+'<span class="badge">下载</span></li>');
				}
				util.dialogHtml($('#errorfiles'),'文件列表','80%','90%');
			}
		});
	}
	/**
	 * 获取当前文件列表中的文件
	 */
	fetchfile.downfiles = function(){
		if(fetchfile.pathArray){
			var files = $('#files').val().trim();
			var connName = $('#connName').val().trim();
			var version = $('#output').val().trim();
			util.requestData('/fetch/findfiles',{files:files,connName:connName,version:version},function(ret){
				if(ret.filename){
					downfile(ret.filename);
				}
				if(ret.errorFiles && ret.errorFiles.length > 0){
					$('#errorfiles').empty();
					for(var i=0;i<ret.errorFiles.length;i++){
						$('#errorfiles').append('<li class="list-group-item">'+ret.errorFiles[i]+'</li>');
					}
					util.dialogHtml($('#errorfiles'),'错误文件','80%','90%');
				}
			}); 
			return ;
		}
		util.layer.msg('没有文件列表可供下载');
	}
	
	/**
	 * 一键部署预览
	 */
	fetchfile.deployPreview = function(){
		if(fetchfile.pathArray){
			var files = $('#files').val().trim();
			var connName = $('#connName').val().trim();
			var version = $('#output').val().trim();
			util.requestData('/fetch/findfiles',{files:files,connName:connName,version:version},function(ret){
				//先展示异常文件列表
				if(ret.errorFiles && ret.errorFiles.length > 0){
					$('#errorfiles').empty();
					for(var i=0;i<ret.errorFiles.length;i++){
						$('#errorfiles').append('<li class="list-group-item">'+ret.errorFiles[i]+'</li>');
					}
//					util.dialogHtml($('#errorfiles'),'错误文件','80%','90%');
					var buildDialog = dialog.create('错误文件')
  				.setContent($('#errorfiles'))
  				.setWidthHeight('70%','80%');
					
  				buildDialog.zIndex = 19891024;
					
					buildDialog.build();
				}
				
				if(ret.filename){
					//展示会上传的文件列表
					util.requestData('/version/listUploadFiles',{filename:ret.filename},function(uploadFiles){
						if(uploadFiles.errorFiles && uploadFiles.errorFiles.length > 0){
							$('#uploadfiles').empty();
							for(var i=0;i<uploadFiles.errorFiles.length;i++){
								$('#uploadfiles').append('<li class="list-group-item">'+uploadFiles.errorFiles[i]+'</li>');
							}
//							util.dialogHtml($('#uploadfiles'),'待上传文件','80%','90%');
							var buildDialog = dialog.create('待上传文件')
		  				.setContent($('#uploadfiles'))
		  				.setWidthHeight('70%','80%')
		  				.addBtn({type:'yes',text:'上传并部署',handler:function(index, layero){
								util.requestData('/version/upload',{connName:connName,filename:uploadFiles.filename,version:version});
								
								layer.close(buildDialog.index);
								layer.msg('上传成功');
							}})
		  				.build();
						}
						
					});
				}
				
				
			}); 
			return ;
		}
		util.layer.msg('没有文件列表可供下载');
	}
	
	/**
	 * 版本上线,打最终版本
	 */
	fetchfile.deployOnline = function(){
		var connName = $('#connName').val().trim();
		var version = $('#output').val().trim();
		util.requestData('/version/merge',{connName:connName,version:version},function(filename){
			$('<form action="'+util.root+'/version/release"><input name="t" value="'+new Date().getTime()+'" /><input name="filename" value="'+filename+'" /></form>').appendTo('body').submit().remove();
		});
	}
	
	function downfile(filename){
		$('<form action="'+util.root+'/filefetch/downFile"><input name="t" value="'+new Date().getTime()+'" /><input name="filename" value="'+filename+'" /></form>').appendTo('body').submit().remove();
	}
	
	function getpathArray(){
		var $files = $('#files').val().trim();
		if($files != ''){
			return $files.split('\n');
		}
		return null;
	}
	
	fetchfile.init();
});