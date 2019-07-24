define(['util', 'diamond/ConfigSeeDialog'], function (util, ConfigSeeDialog) {
    var dataIdPage = {};
    dataIdPage.init = function () {
        //获取当前组
        var parseUrl = util.parseUrl();
        var params = parseUrl.params;
        dataIdPage.params = params;
        dataIdPage.group = params.group;

        $('#groupBrand').text(params.group);

        //加载 dataIds
        util.requestData('/diamond/dataIds', {group: params.group}, function (dataIds) {
            var $dataIds = $('#dataIds>.list-group').empty();
            for (var i = 0; i < dataIds.length; i++) {
                $dataIds.append('<li dataIdPage="' + dataIds[i] + '" class="list-group-item">' + dataIds[i] + '</li>')
            }
            $('#dataIds>.panel-heading>span').text(dataIds.length);
        });

        $('#dataIds').on('click', '.list-group-item', function () {
            var $item = $(this);
            var dataId = $item.attr('dataIdPage');

            util.requestData('/diamond/content', {group: params.group, dataId: dataId}, function (content) {
                $('#content>textarea').val(content);

                $('#content').data('group',params.group);
                $('#content').data('dataId',dataId);

                ConfigSeeDialog.buildDialog(params.group,dataId);
            });


        });
        return this;
    };

    return dataIdPage.init();
});