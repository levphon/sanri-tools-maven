define(['util','dialog','jsonview'],function (util,dialog) {
   var cloudconfig = {};
   var apis = {
       readConfig:'/config/readConfig',
       loadConfigs:'/config/loadConfigs',
       address:'/config/address',
       newConn:'/config/newConn',
       configs:'/config/configs',
       saveConfig:'/config/saveConfig'
   }

   cloudconfig.init = function () {
       //事件绑定
       bindEvents();

       loadConns(function (configs) {
           if(configs){
               //请求第一个连接,并选中
              $('#connect>.dropdown-menu>li:first').click();
               $('#connect>.dropdown-menu').dropdown('toggle');
           }
       });
       return this;
   }

    /**
     * 重新加载所有连接
     * @param callback
     */
   function loadConns(callback) {
        util.requestData(apis.loadConfigs,function (configs) {
           var $menu = $('#connect>ul.dropdown-menu').empty();
           if(configs){
               for (var i = 0; i < configs.length; i++) {
                   var $item = $('<li><a href="javascript:void(0);">'+configs[i]+'</a></li>').appendTo($menu);
                   $item.data('value',configs[i]);
               }
               if(callback){
                   callback(configs);
               }
           }
       });
   }

    /**
     * 加载当前已经有的配置信息
     * @param callback
     */
   function loadConfigs(callback) {
        util.requestData(apis.configs,{conn:cloudconfig.conn},function (configArr) {
               if(configArr){
                   var $latestUse =  $('#latestUse>.list-group').empty();
                   for(var i=0;i<configArr.length;i++){
                       var conf = configArr[i];
                       var $item = $('<li class="list-group-item">'+conf["modul"]+'-'+conf["env"]+'-'+conf["branch"]+'</li>').appendTo($latestUse);
                       $item.data('conf',conf);
                   }
               }
               if(callback){
                   callback(configArr);
               }
           });
   }

   function bindEvents() {
       var events = [{selector:'#newconnbtn',types:['click'],handler:newConn},
                {selector:'#newconfigbtn',types:['click'],handler:newconfig},
           {parent:'#connect>.dropdown-menu',selector:'li',types:['click'],handler:switchConn},
           {parent:'#latestUse>.list-group',selector:'li',types:['click'],handler:readConfig}]

       util.regPageEvents(events);

       /**
        * 读取配置信息
        */
       function readConfig() {
           $(this).siblings().removeClass('active');
           $(this).addClass('active');
           var conf = $(this).data('conf');
           conf.conn = cloudconfig.conn;
           util.requestData(apis.readConfig,conf,function (configjson) {
              // $('#loadconfigdata').text(configjson);
               $('#loadconfigdata').JSONView(configjson);
           });
       }

       /**
        * 切换连接
        */
       function switchConn() {
           var conn = $(this).data('value');
           cloudconfig.conn = conn;

            $('#connect>button>span:eq(0)').text(conn);
           util.requestData(apis.address,{conn:conn},function (address) {
              $('#connect').next('input').val(address);
           });
           $('#connect>.dropdown-menu').dropdown('toggle');

           loadConfigs(function () {
               //加载第一个连接的配置信息
               var $latestUse =  $('#latestUse>.list-group');
              $latestUse.find('li:first').click();
           });

       }



       /**
        * 打开新连接对话框
        */
       function newConn() {
            dialog.create('新连接')
                .setContent($('#newconn'))
                .setWidthHeight('90%','40%')
                .addBtn({type:'yes',text:'确定',handler:function(index, layero){
                    var params = util.serialize2Json($('#newconn>form').serialize());
                    if(!params.conn || !params.address){
                        layer.msg('请将信息填写完整');
                        return ;
                    }
                    util.requestData(apis.newConn,params,function () {
                        layer.close(index);

                          loadConns(function (configs) {
                           if(configs){
                               //请求最后一个连接,并选中
                              $('#connect>.dropdown-menu>li:last').click();
                              $('#connect>.dropdown-menu').dropdown('toggle');
                           }
                       });
                    });
                }})
                .build();
       }

       /**
        * 添加新配置对话框
        */
       function newconfig() {
           dialog.create('新配置')
                .setContent($('#newconfig'))
                .setWidthHeight('500px','40%')
                .addBtn({type:'yes',text:'确定',handler:function(index, layero){
                    var params = util.serialize2Json($('#newconfig>form').serialize());
                    if(!params.modul || !params.env || !params.branch){
                        layer.msg('请将信息填写完整');
                        return ;
                    }
                    params.conn = cloudconfig.conn ;
                    util.requestData(apis.saveConfig,params,function () {
                        layer.close(index);

                        loadConfigs(function () {
                            //点击最后一个
                            var $latestUse =  $('#latestUse>.list-group');
                            $latestUse.find('li:last').click();
                        });
                    });
                }})
                .build();
       }
   }

   return cloudconfig.init();
});