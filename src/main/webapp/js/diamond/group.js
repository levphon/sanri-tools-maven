define(['util','diamond/ConfigSeeDialog'],function (util,ConfigSeeDialog) {
    var group = {};

    group.init = function () {
        //请求当前连接
        util.requestData('/diamond/current',function (connect) {
            $('#connect>button>span:eq(0)').text(connect);
            group.connect = connect;

            //请求当前连接串
            util.requestData('/sqlclient/connectString',{connName:group.connect},function (connectString) {
                $('#connect').next('input').val(connectString);
            });
        });

        //加载当前所有分组
        util.requestData('/diamond/groups',function (groups) {
            var $groups = $('#groups').find('.list-group').empty();
            $('#groups>.panel-heading>span').text(groups.length);
            for (var i=0;i<groups.length;i++){
                var group = groups[i];
                $groups.append('<li group="'+group+'" class="list-group-item">'+group+'</li>')
            }

        });

        //请求所有连接
        util.requestData('/sqlclient/connections',function (connections) {
            var $menu = $('#connect>ul.dropdown-menu');
            for(var i=0;i<connections.length;i++){
                $('<li><a href="javascript:;">'+connections[i]+'</a></li>').appendTo($menu);
            }
        });

        //加载最近使用
        util.requestData('/diamond/latestUse',function (latestUses) {
            var $latestUse = $('#latestUse>.list-group').empty();
            for(var i=0;i<latestUses.length;i++){
                $latestUse.append('<li class="list-group-item" groupDataId = "'+latestUses[i]+'">'+latestUses[i]+'</li>')
            }
        });

        $('#groups>.list-group').on('click','.list-group-item',function () {
            var $item = $(this);
            var group = $item.attr('group');
           util.go('dataIds.html',{group:group}) ;
        });

        return this;
    }

    $('#latestUse').on('click','.list-group-item',function () {
       var groupDataId = $(this).attr('groupDataId');
       var group = groupDataId.split('@')[0];
       var dataId = groupDataId.split('@')[1];
        util.requestData('/diamond/content', {group: group, dataId: dataId}, function (content) {
                $('#content>textarea').val(content);

                $('#content').data('group',group);
                $('#content').data('dataId',dataId);

                ConfigSeeDialog.buildDialog(group,dataId);

            });
    });
    


    return group.init();
});