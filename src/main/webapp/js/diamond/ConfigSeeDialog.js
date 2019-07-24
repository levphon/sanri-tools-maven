define(['util','dialog'],function (util,dialog) {
   var ConfigSeeDialog = {};

   ConfigSeeDialog.init = function () {

       return this;
   }

   ConfigSeeDialog.buildDialog = function (group,dataId) {
        var buildDialog = dialog.create('查看配置['+group+']['+dataId+']')
                    .setContent($('#content'))
                    .setWidthHeight('70%','80%')
                    .addBtn({type:'button',text:'查看测试',handler:seeRemoteTest})
                    .addBtn({type:'button',text:'查看生产',handler:seeRemoteRelease})
                    .addBtn({type:'button',text:'数据比对',handler:function () {
                            util.tab('/app/diamond/compare.html',{group:group,dataId:dataId});
                        }})
                    .build();
   }

   function seeRemoteTest() {
        var group = $('#content').data('group');
        var dataId = $('#content').data('dataId');
        util.requestData('/diamond/remoteConfigContent',{group:group,dataId:dataId,env:'test'},function (data) {
            $('#testContent').find('textarea').val(data);
            dialog.create('查看<span style="color: red">测试</span>配置['+group+']['+dataId+']')
                    .setContent($('#testContent'))
                    .setWidthHeight('70%','80%')
                .build();
        });
    }

    function seeRemoteRelease() {
        var group = $('#content').data('group');
        var dataId = $('#content').data('dataId');
        util.requestData('/diamond/remoteConfigContent',{group:group,dataId:dataId,env:'release'},function (data) {
            $('#releaseContent').find('textarea').val(data);
            dialog.create('查看<span style="color: red">生产</span>配置['+group+']['+dataId+']')
                    .setContent($('#releaseContent'))
                    .setWidthHeight('70%','80%')
                .build();
        });
    }


   return ConfigSeeDialog.init();
});