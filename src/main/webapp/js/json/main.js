define(['util','json/log','json/jsonhandler','dialog','icheck'],function(util,log,jsonhandler,dialog){
	var jsontree = {};
	
	var $nostandardJson = $('#nostandardjson'),
		$standardJson = $('#standardjson'),
		$treeDom = $('#tree');

	//示例数据
	var example = {
		likeJson:"m:[{ab:'大黄',cd:'李遥'},{sanri:{name:'三日',qq:2441719087}}]",
		mapJson:'{m=[{sanri={name=三日, qq=2441719087}}, {ab=大黄, cd=李遥}]}',
		keyvalueJson:'m=1&ab=大黄&cd=李遥'
	}
	
	/**
	 * 初始化调用
	 */
	jsontree.init = function(){
		log.init();
		/*
		 * 初始化 radio  
		 */
		$('#inputTypeCmd>.radio-item>input[type=radio]').iCheck({
			checkboxClass: 'icheckbox_square-green',
			radioClass: 'iradio_square-green'
		});
		$nostandardJson.val(example.likeJson);
		rebuildNoStandardJson();
		bindEvents();
		this.relayout();
	}
	
	/**
	 * 主页面事件绑定
	 * ,输入框和树进行绑定属于树
	 */
	var modeMirro = {likeJson:'类json',mapJson:'map 数据',keyvalueJson:'keyValue 数据'};
	function bindEvents(){
		var _this = this;
	  var Event = [{selector:$nostandardJson,types:['blur'],handler:nostandardJsonBlur},
                 {selector:$standardJson,types:['blur'],handler:standardJsonBlur},
                 {parent:'#tree',selector:'input:not(:checkbox)',types:['blur','keyup'],handler:jsonRebuild},
                 {selector:'#jsontreeOpts button.btn',types:['click'],handler:eventHandler},
                 {selector:'#expand',types:['click'],handler:function(){
                	 $('#jsonview').JSONView('toggle');
                 }}];
	  util.regPageEvents(Event);
	  
	  $('#tree').on('ifChecked','input:checkbox',jsonRebuild); 
	  $('#tree').on('ifUnchecked','input:checkbox',jsonRebuild); 
	  $('#inputTypeCmd input:radio').on('ifChecked',switchMode);
	  /*
	   * 切换 json 输入模式
	   */
	  function switchMode(e){
	  	var $target = $(e.target),
	  		mode = $target.val();
	  	//写放默认示例
	  	switch(mode){
	  	case 'likeJson':
	  		$nostandardJson.val(example.likeJson);
	  		break;
	  	case 'mapJson':
	  		$nostandardJson.val(example.mapJson);
	  		break;
	  	case 'keyvalueJson':
	  		$nostandardJson.val(example.keyvalueJson);
	  		break;
	  	}
	  	log.success('模式已成功切换成: '+modeMirro[mode]);
	  	jsonhandler.mode = mode;
	  	rebuildNoStandardJson();
	  }
		/*
  	 * 通用按扭事件处理
  	 */
  	function eventHandler(e){
  		var $target = $(e.target),
  			event = $target.attr('event');
  		switch(event){
  		case 'cleanNoStandardJson':
  			$('.no-standard-json textarea').val('');
  			break;
  		case 'cleanStandardJson':
  			$('.standard-json textarea').val('');
  			break;
  		case 'cleanLog':
  			log.clean();
  			break;
  		case 'jsonview':
  			dialog.create('json 树视图查看 ')
  				.setContent($('#jsonviewwrap'))
  				.setWidthHeight('500px','90%')
  				.build();
  			require(['jsonview'],function(){
  				$('#jsonview').JSONView($('.standard-json textarea').val());
  			});
  		}
  	}
	  
	  /**
     * 类 json 失去焦点
     * @param e
     */
    function nostandardJsonBlur(e){
      rebuildNoStandardJson();
    }
    /**
     * 标准 json 失去焦点
     * @param e
     */
    function standardJsonBlur(e){
      rebuildStandardJson();
    }
    
    /**
     * 根据左边树结构更新右边 输入框 json 串,
     * 并重置 自己本身属性 json
     */
    function jsonRebuild(e){
      var $target = $(e.target);
      if($target.is(':checkbox')){
        var select = $target.prop('checked'),$childs = $target.closest('li').find(':checkbox').not($target);
        if(!select){
          $childs.iCheck('uncheck');
        }else{
          $childs.iCheck('check');
        }
      }
      refreshJson();
      $standardJson.val(jsonhandler.convert(_this.json));
    }
	}
	/**
	 * 根据非标准 json 重建 json 树视图
	 */
	function rebuildNoStandardJson(){
	  log.info('开始处理 json 串');
	  var noStandardJsonVal = $nostandardJson.val();
    jsonhandler.setNoStandardJson(noStandardJsonVal);
    buildTreeView();
    $standardJson.val(jsonhandler.original.jsonString);
	}
	/**
	 * 重建标准 json 串
	 */
	function rebuildStandardJson(){
	  var jsonStr = $standardJson.val();
    jsonhandler.setStandardJson(jsonStr);
    buildTreeView();
	}
	
	/*
	 * 构建树视图,前后操作
	 */
	function buildTreeView(){
    jsonhandler.buildJson();
    this.json = jsonhandler.standardJson;
    //构建 json 树视图
    log.info('正在构建 json 树视图');
    $treeDom.empty();
    buildTree(this.json,$treeDom,-1);
    $('#tree :checkbox').iCheck({
      checkboxClass: 'icheckbox_square-green',
      radioClass: 'iradio_square-green'
    });
    log.success('json 树视图构建成功');
	}

	
	/*
	 * 根据 json 数据构建树结构
	 */
	var treeNode = '<li><div class="tree-node"><span class="checkbox"><input type="checkbox" name="" checked class="checkbox" value=""></span> <span><input type="text" id="" class="key" value=""> <b>:</b> <input type="text" id="" class="value" value=""></span></div></li>';
	function buildTree(treeJson,$root,level){
	  if(!treeJson){
	    return ;
	  }
	  level ++;
	  var key = undefined,value = undefined;
	  if($.isArray(treeJson)){
	    //json 数组 
	    for(var i=0;i<treeJson.length;i++){
	      key = i;value = treeJson[i];
	      buildTreeNode(key,value,level,$root);
	    }
	  }else{
	    //json 对象 
	    for(var _key in treeJson){
	      key  = _key;value=treeJson[key];
	      buildTreeNode(key,value,level,$root);
	    }
	  }
	}
	/*
	 * 构建树结点
	 */
	function buildTreeNode(key,value,level,$root){
	  var $currentNode = $(treeNode).appendTo($root);
	  //加入缩进 
    for(var j=0;j<level;j++){
      $currentNode.find('.tree-node').prepend('<span class="indent"></span>');
    }
    $currentNode.find('.key').val(key);
    var $value = $currentNode.find('.value'),val = undefined;
    if($.isArray(value)){
      val = '['+value.length+']';
    }else if(typeof value == 'object'){
      val = '{'+Object.keys(value).length+'}';
    }
    if(val){
      $ul = $('<ul></ul>');
      $ul.appendTo($currentNode);
      $value.attr('readonly',true);
      buildTree(value,$ul,level);
    }
    $value.val(val || value);
	}
	
	/**
	 * 窗口改变大小时调用
	 */
	jsontree.relayout = function(){
		var clientWidth = window.document.documentElement.clientWidth;
		var clientHeight = window.document.documentElement.clientHeight;

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
		
		var pageHeight = $('.page-wrap').height(),  //.height() 方法取的是 innerHeight() 是不包含 padding border 的高度
     pageWidth = $('.page-wrap').width(),
     leftWidth = $('.params-tree').width(),
     rightWidth = pageWidth - leftWidth,
     noStandardJsonHeight = $('.no-standard-json').outerHeight(),
     standardJsonHeight = $('.standard-json').outerHeight(),
     buttonGroupHeight = $('#jsontreeOpts').outerHeight(),
     jsonStatusHeight = pageHeight - noStandardJsonHeight - standardJsonHeight - buttonGroupHeight - $('#inputTypeCmd').outerHeight();
 
		 $('.params-tree').height(pageHeight);
		 $('.json-input-status').width(rightWidth).height(pageHeight);
//		 $('.json-status').height(jsonStatusHeight);
		 $('.json-status .log').height(jsonStatusHeight - $('.json-status .legend').outerHeight());
	}
	
	jsontree.init();
	$(window).resize(jsontree.relayout);
	return jsontree;
});