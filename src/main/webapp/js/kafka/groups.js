define(['util','dialog'],function (util,dialog) {
    var groupsPage = {};

    var apis = {
        groups:'/kafka/groups',
        detail:'/kafka/detail',
        connNames:'/kafka/connNames',
        setThirdpartTool:'/kafka/setThirdpartTool',
        createConn:'/kafka/createConn',
        groupSubscribeTopics:'/kafka/groupSubscribeTopics',
        zkConns:'/file/manager/simpleConfigNames',
        stopConsumer:'/kafka/stopConsumer'
    }

    groups.init = function () {
        bindEvents();
        loadConns(function (conns) {
            $('#connect>.dropdown-menu>li:first').click();
            $('#connect>.dropdown-menu').dropdown('toggle');
        });
        return this;
    }

    function loadConns(callback) {
        util.requestData(apis.connNames,function (conns) {
            var $menu = $('#connect>ul.dropdown-menu').empty();
            if(conns){
                for(var i=0;i<conns.length;i++){
                    var $item = $('<li name="'+conns[i]+'"><a href="javascript:;">'+conns[i]+'</a></li>').appendTo($menu);
                    $item.data('value',conns[i]);
                }
                if(callback){
                    callback(conns);
                }
            }
        });
    }

    function loadGroups() {
        var index = layer.load(1, {
          shade: [0.1,'#fff']
        });
        util.requestData(apis.groups,{name:groupsPage.conn},function (groups) {
            var $groups = $('#groups>.list-group').empty();
            for (var i=0;i<groups.length;i++){
                var group = groups[i];
                util.ajax({url:apis.groupSubscribeTopics,data:{group:group,name:groupsPage.conn},async:false},function(topics){
                    $('<a class="list-group-item group" group='+group+'> <span>'+group+'</span> <span class="badge list-group-item-success"> '+topics.length+' </span> </a>').appendTo($groups);
                });
            }
            layer.close(index);
        });
    }

    function bindEvents(){
        var events = [{selector:'#newconnbtn',types:['click'],handler:newconn},
            {selector:'#thirdpart',types:['click'],handler:thirdpart},
            {selector:'#stopConsumer',types:['click'],handler:stopConsumer},
            {parent:'#connect>.dropdown-menu',selector:'li',types:['click'],handler:switchConn},
            {parent:'#groups>.list-group',selector:'a',types:['click'],handler:subscribeTopicsPage},
            {selector:'#admin',types:['click'],handler:adminPage}];
        util.regPageEvents(events);

        function stopConsumer() {
            util.requestData(apis.stopConsumer,{name:groupsPage.conn},function () {
                $('#stopConsumer>i').removeClass('text-red');
            });
        }

        function subscribeTopicsPage() {
            var group = $(this).attr('group');
            util.tab('/app/kafka/subscribeTopics.html',{group:group,name:groupsPage.conn});
        }
        function adminPage() {
            util.tab('/app/kafka/admin.html',{conn:groupsPage.conn});
        }

        function switchConn() {
            var conn = $(this).data('value');
            groupsPage.conn = conn;

            $('#connect>button>span:eq(0)').text(conn);
            util.requestData(apis.detail,{name:conn},function (connInfo) {
                $('#connect').next('input').val(JSON.stringify(connInfo));
                $('#brokers').text(connInfo.broker);
            });
            $('#connect>.dropdown-menu').dropdown('toggle');

            //加载当前连接分组信息
            loadGroups();
        }

        function newconn() {
            dialog.create('创建新连接')
                .setContent($('#newconn'))
                .setWidthHeight('500px','45%')
                .addBtn({type:'yes',text:'添加',handler:createConn})
                .build();
            //加载所有 zk 连接
            util.requestData(apis.zkConns,{modul:'zookeeper'},function (conns) {
                $('#conns').empty();
                for(var i=0;i<conns.length;i++){
                    $('#conns').append('<option value="'+conns[i]+'">'+conns[i]+'</option>');
                }
            });

            function createConn(index) {
                var params = {};
                params.name = $('#conns').val();
                params.version = $('#version').val();
                params.rootPath = $('#newconn').find('input[name=rootPath]').val().trim();

                util.requestData(apis.createConn,params,function () {
                    layer.close(index);
                    loadConns(function () {
                        $('#connect>.dropdown-menu>li[name='+params.name+']').click();
                        $('#connect>.dropdown-menu').dropdown('toggle');
                    })
                });
            }

        }

        function thirdpart() {
            dialog.create('设置第三方监控')
                .setContent($('#setthirdpart'))
                .setWidthHeight('60%','25%')
                .addBtn({type:'yes',text:'设置',handler:setThirdpart})
                .build();

            //加载详情,如果有第三方监控设置,则加载
            util.requestData(apis.detail,{name:groupsPage.conn},function (connInfo) {
                if(connInfo.thirdpartTool){
                    $('#setthirdpart').find('input').val(connInfo.thirdpartTool);
                }
            });

            function setThirdpart(index) {
                var thirdpart = $('#setthirdpart').find('input').val().trim();
                util.requestData(apis.setThirdpartTool,{name:groupsPage.conn,address:thirdpart},function () {
                    layer.close(index);
                })
            }
        }
    }

    return groups.init();
});