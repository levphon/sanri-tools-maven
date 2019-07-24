/**
 * 主要用于滚动标签页和表格
 */
define(['util','scrolltabs','datatable'],function(){
	var output = {
			$scrollTabs:$('#tableTabs'),
			$tables:$('#tables'),
			tabsMap:{},
			config:{
				//datatable 配置
				searching:false,
				bLengthChange:false
			}
	};
	/**
	 * 重新布局时设置高度
	 */
	output.relayout = function(clientWidth,clientHeight){
		
	}
	
	/**
	 * 初始化操作
	 */
	output.init = function(){
		var _this = this;
		//初始化表格滚动标签页
		this.$scrollTabs.scrolltabs({
      onSelect:function(tab){
        var tabId = tab.id,
            id = tabId.split('_')[1];
        _this.$tables.find('#div_table_wrap_'+id).show().siblings().hide()   //显示当前选中的表格
      },
      onClose:function(tab){
        var tabId = tab.id,
            id = tabId.split('_')[1];
        _this.$tables.find('table[id=table_'+id+']').parent().remove();
        // delete _this.tabsMap[tab.title];   //删除 map 中数据  ; 之前是用 sql 语句做标题,后面太长改用数字做标题,所以不能用标题来移除
		  delete _this.tabsMap[tab.sqlStatemt];
      }
    });
	}
	
	/**
	 * 追加一个标签页
	 * @param sqlId 提供一个 sql 语句
	 * @param resultSet json 类型的结果集
	 */
//	output.addTab = function(sqlId,resultSet){
//		 if(!sqlId){return ;}
//     if(sqlId in this.tabsMap){
//       var tabId = this.tabsMap[sqlId].id,
//           id = tabId.split('_')[1];
//       this.$scrollTabs.scrolltabs('select',tabId);
//       this.$scrollTabs.scrolltabs('showActive');
//       return ;
//     }
//     //如果不存在,则新加 tab
//     var newTabId = Math.round((Math.random() * 1000)),
//         tab = {id:'tab_'+newTabId,title:sqlId,};
//     this.$scrollTabs.scrolltabs('add',tab);
//     this.tabsMap[sqlId] = tab;
//     appendTable(newTabId,resultSet,this);
//	}
	
	output.addTab = function(connName,dbName,index,sqlStatemt,resultSet){
		var sqlId = connName+'_'+dbName+'_'+index;
		if(sqlStatemt in this.tabsMap){
	    var tabId = this.tabsMap[sqlStatemt].id,
	        id = tabId.split('_')[1];
	    this.$scrollTabs.scrolltabs('select',tabId);
	    this.$scrollTabs.scrolltabs('showActive');
	    //datatable 重新加载  TODO
	    return ;
	  }
		//如果不存在,则新加 tab
	  var newTabId = Math.round((Math.random() * 1000)),
	      tab = {id:'tab_'+newTabId,title:sqlId,sqlStatemt:sqlStatemt}; 
	  this.$scrollTabs.scrolltabs('add',tab);
	  this.tabsMap[sqlStatemt] = tab;
	  appendTable(newTabId,resultSet,this);
	}
	
	 /*
   * 结果集中追加一个表格
   * 写一张表格追加进 tables-result-set,并美化 ;并隐藏其它表格
   */
	 // 限制每列最多 30 个字符 ,多出的显示为 ...
	 var showLimitChars = 30;
	function appendTable(newTabId,resultSet,_this){
    if(resultSet.head && resultSet.head.length > 0){
      var head = [],body = [];
      //拼接 head
      for(var i=0;i<resultSet.head.length;i++){
        var columnName = resultSet.head[i];
        head.push('<th>'+columnName+'</th>');
      }
      //拼接 body
      for(var j=0;j<resultSet.body.length;j++){
        var mapObj = resultSet.body[j];
        body.push('<tr>');
        for(var colName in mapObj){
        	var showValue = mapObj[colName];
        	if(mapObj[colName] && mapObj[colName].length > showLimitChars){
        		showValue = showValue.substring(0,showLimitChars)+'...';
			}
          body.push('<td>'+showValue+'</td>');
        }
        body.push('</tr>');
      }
      var tabHtml = '<div id="div_table_wrap_'+newTabId+'"><table id="table_'+newTabId+'"><thead><tr>'+head.join('')+'</tr></thead><tbody>'+body.join('')+'</tbody></table></div>';
      var $tableWrapDiv = $(tabHtml).appendTo(_this.$tables);//显示当前追加的表格
      //隐藏其它表格
      $tableWrapDiv.siblings().hide();
      $tableWrapDiv.find('table').dataTable(_this.config);   //当前表格加入 datatables 样式 
    }
	}
	
	return output;
});