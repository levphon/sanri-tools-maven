define([ 'util','sqlclient/output', 'codemirror/lib/codemirror','codemirror/lib/hint/show-hint','codemirror/lib/hint/sql-hint'], function(util,output,CodeMirror) {
	var code = {};

	code.init = function() {
		code.config = {
			value : '--在些处输入你的脚本\n',
			mode : 'text/x-plsql',
			tabSize : 2,
			tabMode : 'indent',
			lineNumbers : true,
			autoMatchParens : true,
			textWrapping : true,
			autofocus : true,
			matchBrackets : true,
			styleActiveLine : true,
			extraKeys : {
				"Alt-/" : "autocomplete"
			}
		}
		//初始化 codeMirror
		code.$codeMirro = $('#sqlInput');
		code.codeMirror = CodeMirror.fromTextArea(code.$codeMirro[0],code.config);
		code.codeMirror.setSize('100%','100%');
	}

	/**
	 * 获取选中的语句
	 * String 当没有选中时返回 null
	 */
	code.getSelect = function(){
		return code.codeMirror.getSelection().trim();
	}
	/**
	 * 获取全部语句
	 * @returns String[]
	 */
	code.getAll = function(){
		var allSql = code.codeMirror.getValue().trim();
		if(allSql){
			var sqlArray=allSql.split('\n');
			var sqlArrayFilter = [];
			//过滤掉空语句后返回
			for(var i=0;i<sqlArray.length;i++){
				var sql = $.trim(sqlArray[i]);
				if(sql){
					sqlArrayFilter.push(sql);
				}
			}
			return sqlArrayFilter;
		}
		return [];
	}

	code.getAllString = function(){
		return code.codeMirror.getValue().trim();
	}

	/**
	 * 追加 sql 进代码器
	 */
	code.appendSql = function(sql) {
		if (sql && $.trim(sql) != '') {
			var nowValue = code.codeMirror.getValue();
			if (nowValue.indexOf(sql) == -1) { // 没有这句话才写,不然会写一堆
				code.codeMirror.setValue(nowValue + sql + '\n');
			}else{
				//如果代码编辑器中有这一行 sql ,则选中,并定位到可视区域
//				code.codeMirror.setSelection();
				// 不知道怎么用
			}
		}
	}
	/**
	 * 加载文件中的值
	 * @param sqls
	 */
	code.setValue = function(sqls){
		code.codeMirror.setValue(sqls);
	}

	/**
	 * 执行 sql
	 */
//	code.executeSql = function(sql){
//		if(!sql){
//			sql = code.getSelect();
//		}
//		var executeSqls = [];
//		if(!sql){
//			executeSqls = code.getAll();
//		}else{
//			executeSqls.push(sql);
//		}
//		util.requestData('/sqlclient/executeSql',{connName:metatree.current.conn,database:metatree.current.db,executorSqlArray:executeSqls},function(resultSets){
//			if(resultSets){
//        for(var sqlStatemt in resultSets){
//          output.addTab(sqlStatemt,resultSets[sqlStatemt]);
//        }
//      }
//		});
//	}

	/**
	 * @param connName 连接名称
	 * @param dbName 库名称
	 * @param sqls sql 语句列表 string/Array
	 */
	code.executeSqls = function(connName,dbName,sqls){
		if(!sqls){
			sqls = code.getSelect();
			if(!sqls){
				sqls = code.getAll();
			}
		}
		if(typeof sqls == 'string'){
			sqls = [sqls];
		}
		code.executeCount = code.executeCount || 1 ;
		//加载遮罩层
        var index = layer.load(1, {
          shade: [0.1,'#fff']
        });
		util.requestData('/sqlclient/executeSql',{connName:connName,database:dbName,executorSqlArray:sqls},function(resultSets){
			try{
				if(resultSets){
				  for(var sqlStatemt in resultSets){
					output.addTab(connName,dbName,code.executeCount++,sqlStatemt,resultSets[sqlStatemt]);
				  }
				}
			}finally {
				layer.close(index);
			}
		},function () {
			layer.close(index);
		});
	}

	return code;
});