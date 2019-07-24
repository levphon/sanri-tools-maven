define(['util','contextMenu'],function(util){
  /**
   * scrolltabs 初始化方法
   */
  function init(jq,opts){
    var $tabs = $('<ul class="page-tabs-content"></ul>'),width = opts.width;
    jq.addClass('content-tabs');
    if(opts.fit){
      width = jq.parent().width();
    }else {
      if(opts.width && opts.width.endWidth('%')){
        width = jq.parent().width() * opts.width.substring(0,(opts.width.length() -2));
      }
    }
    opts.width = width;    //这个宽度为显示区宽度
    jq.css({
      width:width
    });
    jq.append('<button class="tabs-nav nav-left"><i class="fa icon-backward"></i></button>');
    var $pageTabs = $('<div class="page-tabs clearfix"></div>').appendTo(jq);
    jq.append('<button class="tabs-nav nav-right"><i class="fa icon-forward"></i></button>');
    $pageTabs.append($tabs);
    
    //计算标签页显示区宽度
    var btnSumWidth = elsWidth($('.tabs-nav',jq));
    
    //添加右键菜单
    var selector = '#'+jq.attr('id')+' .page-tabs-content>li'; //TODO 选择器必须有 id
    $.contextMenu({
      selector:selector,
      items:{
        'closeTab':{name:'关闭标签页',icon:'delete',callback:function(key,opts){
          var tabId = opts.$trigger.attr('tabid');
          closeTab(jq, tabId);
        }},
        'closeOther':{name:'关闭其它标签页',icon:'delete',callback:function(key,opts){
          var tabId = opts.$trigger.attr('tabid');
          closeOther(jq, tabId);
        }},
        'closeAll':{name:'关闭所有标签页',icon:'delete',callback:function(key,opts){
          closeAll(jq);
        }},
        'sep1':'------------------',
        'newTab':{name:'新建标签页',icon:'edit',callback:function(){
          
        }}
      }
    });
    
    return {
      tabs:$tabs,
      showWidth:(width - btnSumWidth)
    }
  }
  
  /**
   * 初始化数据 
   */
  function initData(jq){
    var state = jq.data('scrolltabs'), //$.data(jq[0],'scrolltabs'),
        $tabs = state.tabs,
        opts = state.opts;
    //初始化标签页数据 
    if(opts.tabs && opts.tabs.length > 0){
      for(var i=0;i<opts.tabs.length;i++){
        var currentTab = opts.tabs[i];
        var $li = $('<li title="'+currentTab.title+'" tabId="'+currentTab.id+'" tabIndex="'+i+'"><a href="#" >'+currentTab.title+'<i class="fa icon-remove-circle"></i></a></li>').appendTo($tabs);
        $li.data('original',currentTab);
      }
      //初始化 tabsMap
      for(var i=0;i<opts.tabs.length;i++){
        var currTab = opts.tabs[i];
        opts.tabsMap[currTab['id']] = currentTab;
      }
      //首个标签页选中
      $tabs.find('li:first').addClass('selected');
    }
  }
  
  /**
   * 新增标签页
   * 支持以对象方式增加和 以id,标题方式增加
   * 建议以对象方式增加
   */
  function addTab(jq,tab,title){
    var state = jq.data('scrolltabs'), //$.data(jq[0],'scrolltabs'),
        $tabs = state.tabs,
        opts = state.opts;
    
    var currentTab = tab;
    if(typeof tab == 'object'){
      opts.tabs.push(tab);
      opts.tabsMap[tab.id] = tab;
    }else{
      var _tab = {id:tab,title:title};
      opts.tabs.push(_tab);
      opts.tabsMap[tab] = _tab;
      currentTab = _tab;
    }
    var $li = $('<li title="'+currentTab.title+'" tabId="'+currentTab.id+'" tabIndex="'+currentTab.index+'"><a href="#" >'+currentTab.title+'<i class="fa icon-remove-circle"></i></a></li>').appendTo($tabs);
    $li.data('original',currentTab);
    
    //添加之后选中当前,并定位
    selectTab(jq, currentTab.id);
    showActive(jq);
  }
  
  /*
   * private method 获取选中的标签页
   * 先根据 id 获取,获取不到就根据标题来获取 
   */
  function findTab(jq,which){
    var state = jq.data('scrolltabs'),
        $tabs = state.tabs;
    var selectTab = $tabs.find('li[tabid='+which+']');
    if(selectTab.size() == 0){
      selectTab = $tabs.find('li[title='+which+']');
    }
    return selectTab;
  }
  /*
   * private method
   * 传入一个 jquery 元素集合,计算总宽度
   */
  function elsWidth($els) {
    var sumWidth = 0;
    $els.each(function() {
      sumWidth += $(this).outerWidth(true);
    });
    return sumWidth;
  }
  /**
   * 存在返回真,不存在返回假
   */
  function existTab(jq,which){
    return findTab(jq, which).size() != 0;
  }
  /**
   * 标签页选择
   * which id or title 
   * 首先使用 id 来选择
   */
  function selectTab(jq,which){
    var state = jq.data('scrolltabs'),
        opts = state.opts;
    var $selectTab = findTab(jq, which),
        tabId = $selectTab.attr('tabid');
    
    if($selectTab.hasClass('selected')){
      //选中的标签页再次选中相当于双击,调用双击回调函数
      if(opts.onDblClickTab && $.isFunction(opts.onDblClickTab)){
        opts.onDblClickTab.call(jq,opts.tabsMap[tabId]);
      }
      return ;
    }
    $selectTab.addClass('selected').siblings().removeClass('selected');
    if(opts.onSelect && $.isFunction(opts.onSelect)){
      opts.onSelect.call(jq,opts.tabsMap[tabId]);
    }
  }
  /**
   * 显示选中的标签页
   * 逻辑暂时没看懂 //TODO 
   */
  function showActive(jq){
    var state = jq.data('scrolltabs'),
        $tabs = state.tabs,
        opts = state.opts;
    //标签页的所有宽度之合不足显示区宽度,则不需要滚动
    if($tabs.width() < state.showWidth){
      //复位只显示第一页
      $tabs.animate({marginLeft:0},opts.scrollDuration);
      return ;
    }
    
    var $selectTab = $tabs.find('li.selected'),
        prevWidth = elsWidth($selectTab.prevAll()), 
        nextWidth = elsWidth($selectTab.nextAll()),
        opts = state.opts,
        position = 0;
    
    if (nextWidth <= (state.showWidth - $selectTab.outerWidth(true) - $selectTab.next().outerWidth(true))) {
      if ((state.showWidth - $selectTab.next().outerWidth(true)) > nextWidth) {
        position = prevWidth;
        var $currentTab = $selectTab;
        while ((position - $currentTab.outerWidth()) > ($tabs.outerWidth() - opts.showWidth)) {
          position -= $currentTab.prev().outerWidth();
          $currentTab = $currentTab.prev()
        }
      }
    } else {
      if (prevWidth > (state.showWidth - $selectTab.outerWidth(true) - $selectTab.prev().outerWidth(true))) {
        position = prevWidth - $selectTab.prev().outerWidth(true)
      }
    }
    $tabs.animate({
      marginLeft : (0 - position) + 'px'
    }, opts.scrollDuration)
  }
  /**
   * 标签页关闭<br/>
   * 可能存在的情况<br/>
   * <ul>
   *  <li>关闭未选中的标签页</li>
   *  <li>关闭选中的标签页
   *    <ol>
   *      <li>关闭的标签页在<strong>显示区</strong>最后一个标签页,后面还有标签页</li>
   *      <li>关闭的标签页在<strong>显示区</strong>第一个标签页,前面还有标签页</li>
   *      <li>整个就只有一个当前一个可以关闭的标签页,并且被选中</li>
   *    </ol>
   *  </li>
   * </ul>
   */
  function closeTab(jq,which){
    var state = jq.data('scrolltabs'),
        $tabs = state.tabs,
        opts = state.opts,
        $currentTab = findTab(jq, which),
        tabId = $currentTab.attr('tabid');
    if(opts.onBeforeClose && $.isFunction(opts.onBeforeClose)){
      if(!opts.onBeforeClose.call(jq,opts.tabsMap[tabId])){//返回 flase 阻止关闭
        return ;
      }
    }
    //下面开始真正关闭标签页
    if(!$currentTab.hasClass('selected')){
      //关闭未选中的,则关闭,直接定位当前选中的标签页
      $currentTab.remove();
      showActive(jq);
      if(opts.onClose && $.isFunction(opts.onClose)){
        opts.onClose.call(jq,opts.tabsMap[tabId]);
      }
      //删除数组和 map 中的元素 TODO 数组中的暂时不删除,删除也没用,只在初始化的时候用到
      delete opts.tabsMap[tabId];
      return ;
    }
    var $nextTab = undefined;
    if($currentTab.next('li').size()){
      $nextTab = $currentTab.next('li');
    }else if($currentTab.prev('li').size()){
      //如果前面还有标签页,但后面没有标签页了,则一定是最后一个标签页,不会存在任何问题
      $nextTab = $currentTab.prev('li');
    }
    if($nextTab){ //如果有接下来可被选中的标签页,则选中
      selectTab(jq, $nextTab.attr('tabid'));  //选中标签页
      $currentTab.remove(); //移除当前标签页,先移除再定位,不然会出现定位不准的情况
      showActive(jq);
    }else{
      $currentTab.remove(); //移除当前标签页
    }
    if(opts.onClose && $.isFunction(opts.onClose)){
      opts.onClose.call(jq,opts.tabsMap[tabId]);
    }
    delete opts.tabsMap[tabId];
  }
  /**
   * 关闭所有标签页
   */
  function closeAll(jq){
    var state = jq.data('scrolltabs'),
        $tabs = state.tabs,
        opts = state.opts,
        $lis = $tabs.find('li');
    $lis.remove();
    if(opts.onClose && $.isFunction(opts.onClose)){
      $lis.each(function(){
        var tabId = $(this).attr('tabid');
        opts.onClose.call(jq,opts.tabsMap[tabId]);
        delete opts.tabsMap[tabId];
      });
    }
    //最后需要把偏移归位
    $tabs.css({marginLeft:0});
  }
  /**
   * 关闭其它标签页
   */
  function closeOther(jq,which){
    var state = jq.data('scrolltabs'),
        $tabs = state.tabs,
        $currentTab = findTab(jq, which),
        opts = state.opts,
        $lis = $tabs.find('li').not($currentTab);
    $lis.remove();
    selectTab(jq, which);
    //最后需要把偏移归位,因为只有一个标签了
    $tabs.css({marginLeft:0});

    if(opts.onClose && $.isFunction(opts.onClose)){
      $lis.each(function(){
        var tabId = $(this).attr('tabid');
        opts.onClose.call(jq,opts.tabsMap[tabId]);
        delete opts.tabsMap[tabId];
      });
    }
  }
  /**
   * 向左滚动
   */
  function leftScroll(jq){
    var state = jq.data('scrolltabs'),
        $tabs = state.tabs,
        opts = state.opts;
    //标签页总宽度不大于显示区宽度,不需要移动
    if($tabs.width() < state.showWidth){   //TODO $tabs.width() ,好像 $tabs.outerWidth() 不行,等下查查
      return ;
    }
    var currentOffset = Math.abs(parseInt($tabs.css("margin-left"))); //当前的偏移值
    /*如果包含有所有标签页的宽度大于了 nav 宽度,则这样处理
     * 1.先看所有标签已经超出父元素多少了,即 currentOffset,此时是个负值 Math.abs 转为正值
     * 2.从第一个标签开始统计宽度,直到所有的宽度和能够在显示区显示出来,即宽度和大于  tabsContentMarginLeft
     * 3.所有标签有可能有几页,即前面所有的标签不在显示区一屏显示得下,如果显示得下,直接 marginLeft 设置为 0 就好了
     * 如果显示不下,则 marginLeft 还是一个负值,下面则是计算要还要负多少
     * 3.1,又从当前的 tab 住前推,直到刚好一屏的内容,然后 marginLeft 则为这个 元素前面标签的宽度总和
    */
    var $currentTab = $tabs.find('li:first'),sumWidth = 0;
    while ((sumWidth + $currentTab.outerWidth(true)) <= currentOffset) {
      sumWidth += $currentTab.outerWidth(true);
      $currentTab = $currentTab.next()
    }
    sumWidth = 0;
    var prevWidth = 0;
    if (elsWidth($currentTab.prevAll()) > state.showWidth) {
      //如果前面的标签在一个显示区显示不下,即还有好几页的情况
      while ((sumWidth + $currentTab.outerWidth(true)) < (state.showWidth) && $currentTab.length > 0) {
        sumWidth += $currentTab.outerWidth(true);
        $currentTab = $currentTab.prev();
      }
      prevWidth = elsWidth($currentTab.prevAll());
    }
    $tabs.animate({
      marginLeft: 0 - prevWidth + 'px'
    }, opts.scrollDuration);
  }
  /**
   * 向右滚动
   * .content-tabs 是导航区的宽度
   * 它的实现方式 是用一个固定宽度的 div 包裹一个很宽的 div  .page-tabs,
   * 然后所有的标签页在此 div 中显示,用一个 .page-tabs-content 来包裹不设置宽度,由标签页来充大,所有 page-tabs-content 代表了当前所有标签页的宽度
   */
  function rightScroll(jq){
    var state = jq.data('scrolltabs'),
        $tabs = state.tabs,
        opts = state.opts;
    if($tabs.width(true) < state.showWidth){   //标签页总宽度不大于显示区宽度,不需要移动
      return ;
    }
    var currentOffset = Math.abs(parseInt($tabs.css("margin-left"))); //当前的偏移值
    
    //先找到第一个能显示的标签页
    var $currentTab = $tabs.find('li:first'),sumWidth = 0;
    while ((sumWidth + $currentTab.outerWidth(true)) <= currentOffset) {
      sumWidth += $currentTab.outerWidth(true);
      $currentTab = $currentTab.next()
    }
    
    //然后从第一个能显示的标签页向推,直到最后一个显示的标签页的后一个标签页,然后设置 偏移为当前标签页的前面标签页的宽度
    sumWidth = 0;
    while ((sumWidth + $currentTab.outerWidth(true)) < (state.showWidth) && $currentTab.length > 0) {
      sumWidth += $currentTab.outerWidth(true);
      $currentTab = $currentTab.next()
    }
    var prevWidth = elsWidth($currentTab.prevAll());
    if (prevWidth > 0) {
      $tabs.animate({
        marginLeft: 0 - prevWidth + 'px'
      }, opts.scrollDuration)
    }
  }
  /**
   * 事件绑定
   */
  function bindEvents(jq){
    var state = jq.data('scrolltabs'),
        $navLeft = jq.find('.tabs-nav.nav-left'),
        $navRight = jq.find('.tabs-nav.nav-right'),
        $tabs = state.tabs,
        opts = state.opts;
    
    var EVENTS=[{selector:$navLeft,types:['click'],handler:function(e){
      leftScroll(jq);
    }},
    {selector:$navRight,types:['click'],handler:function(e){
      rightScroll(jq);
    }},
    {parent:$tabs,selector:'li>a>i',types:['click'],handler:function(e){
      var $tab = $(e.target).closest('li'),
          id = $tab.attr('tabid');
      closeTab(jq,id);
    }},
    {parent:$tabs,selector:'li',types:['click'],handler:function(e){
      var $tab = $(e.target).closest('li'),
          id = $tab.attr('tabid');
      selectTab(jq, id);
    }},
    {parent:$tabs,selector:'li',types:['dblclick'],handler:opts.onDblClickTab}];
    util.regPageEvents(EVENTS);
  }
  
  /**
   * 插件定义
   */
  $.fn.scrolltabs = function(options,params){
    if(typeof options == 'string'){
      var method = $.fn.scrolltabs.methods[options],
        args = [this];
      if(method){
//        return method(this,params);
        return method.apply(this,args.concat(Array.prototype.slice.call(arguments, 1)));
      }
      return ;
    }
    options = options || {};
    return this.each(function(){
      var state = $.data(this,'scrolltabs'),$this= $(this);
      if(state){
          $.extend(state.opts,options);
      }else{
          state = $.extend({}, $.fn.scrolltabs.defaults,options);
          state = $.data(this,'scrolltabs', $.extend({opts:state},init($(this),state)));
      }
      bindEvents($this);
      initData($this);
   });
  }
  
  $.fn.scrolltabs.methods={
      options:function(jq){
        var state = jq.data('scrolltabs');
        return state.opts;
      },
      add:function(jq,tab,title){
        addTab(jq, tab,title);
      },
      close:function(jq,which){
        closeTab(jq, which);
      },
      select:function(jq,which){
        selectTab(jq,which);
      },
      exist:function(jq,which){
        return existTab(jq,which);
      },
      showActive:showActive
  };
  $.fn.scrolltabs.defaults={
    fit:true,
    width:'100%',
    scrollDuration:400,
    tabs:[],              //所有的标签页配置
    tabsMap:{},
    onSelect:function(id,tab){}, //选中后的回调
    onBeforeClose:function(id,tab){return true;},
    onClose:function(id,tab){},
    onDblClickTab:function(id,tab){}
  }
});