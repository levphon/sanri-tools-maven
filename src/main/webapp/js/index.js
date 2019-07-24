define(['util','template','dialog','chosen'],function (util, template,dialog) {

    util.requestData('/index/toolCount',function (count) {
        $('#toolCount').text(count);
    });

    util.requestData('/index/listTools',function (toolModels) {
        var $toolUl = $('#tools').empty();
        template.helper('dateFormat',util.FormatUtil.dateFormat);
        for(var i=0;i<toolModels.length;i++){
            var toolModel = toolModels[i];
            var toolHtml = template('toolTemplate',toolModel);
            $toolUl.append($(toolHtml));
        }
    });

    $('#tools').on('click','a:not(#removetool)',function () {
        var $li = $(this).closest('li');
        var url = $li.attr('url');
        //当前元素移动到最上层,修改调用次数和最后调用时间
        $('#tools').prepend($li);
        var totalCalls = parseInt($li.find('b.totalCalls').text());
        $li.find('b.totalCalls').text(++totalCalls);
        $li.find('span.lastCallTime').text(util.FormatUtil.dateFormat(new Date().getTime(),'yyyy-MM-dd HH:mm:ss'));

        util.requestData('/index/visited',{url:url},function () {});
    });

    $('#reloadConfig').bind('click',function () {
        util.requestData('/index/reloadConfig',function () {});
    });
    $('#fun').find('select').bind('change',function () {
       var toolUrl = $(this).val();
       util.requestData('/index/toolInfo',{url:toolUrl},function (toolModel) {
           $('#fun').find('input[name=url]').val(toolModel.url);
           $('#desc').text(toolModel.desc);
       });
    });

    $('#tools').on('click','a#removetool',function () {
        var url = $(this).closest('li').attr('url');
        util.requestData('/index/removeTool',{url:url},function () {
           location.reload();
        });
    });

    $('#addfunction').bind('click',function () {
        var $select = $('#fun').find('select').empty();
        util.requestData('/index/toolNames',function (toolMap) {
            var htmlCode =  [];
            for(var key in toolMap){
                htmlCode.push('<option value="'+toolMap[key]+'">'+key+'</option>');
            }
            $select.append(htmlCode.join('')).change();
            $('#toolselect').chosen({
                disable_search_threshold : 10,
                no_results_text : "没有数据",
                width:'100%'
            });
        });

        dialog.create('添加功能')
            .setContent($('#fun'))
            .setWidthHeight('500px', '40%')
            .addBtn({type:'yes',text:'确定',handler:addFunction})
            .build();
    });

    /**
     * 添加工具
     */
    function addFunction(index) {
        var url = $('#fun').find('input[name=url]').val().trim();
        util.requestData('/index/addTool',{url:url},function () {
            layer.close(index);
            location.reload();
        });
    }
});