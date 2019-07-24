define(['util', 'dialog', 'zclip'], function (util, dialog) {
    var templatecode = {};

    templatecode.init = function () {
        bindEvents();

        loadTemplates(function (templates) {
            if(templates){
                $('#templates').val(templates[0]).change();
            }
        });
    }

    function bindEvents() {
        var EVENTS = [{selector: '#datacols>.value-col-plus', types: ['click'], handler: addDataCol},
            {selector: '#templates', types: ['change'], handler: switchTemplate},
            {selector: '#newTmp', types: ['click'], handler: newTemplate},
            {selector: '#gencode', types: ['click'], handler: generator}];
        util.regPageEvents(EVENTS);
        $('#copy').zclip({
            path: "ZeroClipboard.swf",
            copy: function(){
                return $('#result-show').val();
            },
            afterCopy:function(){/* 复制成功后的操作 */
                layer.msg('复制成功');
            }
        });

         /**
         * 模板切换
         */
        function switchTemplate() {
            var tmp = $(this).val();
            util.requestData('/tmpcode/readTmplate',{baseName:tmp},function (data) {
                $('#template-edit').val(data);
            });
        }

        /**
         * 增加新模板
         */
        function newTemplate() {
            dialog.create('新模板')
                .setContent($('#newconfig'))
                .setWidthHeight('500px','60%')
                .addBtn({type:'yes',text:'确定',handler:submitTemplate})
                .build();

            function submitTemplate(index) {
                var params = util.serialize2Json($('#newconfig>form').serialize())
                if(!params.baseName  || !params.content ){
                    layer.msg('埴入模板名称和内容');
                    return ;
                }
                util.requestData('/tmpcode/writeTemplate',params,function () {
                    loadTemplates(function () {
                        //选中当前模板
                         $('#templates').val(params.baseName).change();
                    })
                    layer.close(index);
                });
            }
        }

       /**
         * 添加数据列
         */
        function addDataCol() {
            //检查数量 ,不能超过 10 列
            var cols = $('#datacols>.value-col').size();
            $(this).before($('<div class="value-col "><textarea class="form-control" ></textarea></div>'));
            if(cols == 3){
                //把自己删除并添加
                $(this).remove();
            }
        }

         /**
         *  生成代码
         */
        function generator() {
            //读取列数据
            var datacols = {};
            $('#datacols').find('textarea').each(function (j) {
                var col =  $(this).val().trim();
                if(col != ''){
                    var colArray = col.split('\n');
                    var trimColArray = [];
                    for(var i = 0;i<colArray.length;i++){
                        if(colArray[i].trim() != ''){
                            trimColArray.push(colArray[i].trim());
                        }
                    }
                    if(trimColArray.length > 0){
                        datacols[j+''] = colArray;
                    }
                }
            });

            //获取模板,开始生成
             var tmpcode = $('#template-edit').val().trim();
            var cols = Object.keys(datacols);
            var colRegx = [];           //列正则
            var codeLength = datacols[0+''].length;
            var codes = [];
            for(var i=0;i<cols.length;i++){
                //先生成每一列的正则
                colRegx.push(new RegExp('(\\{'+i+'\\})','g'));
            }
            for(var i=0;i<codeLength;i++){
                var code = tmpcode;
                for(var j=0;j<colRegx.length;j++){
                    code = code.replace(colRegx[j],datacols[j+''][i]);
                }
                codes.push(code);
            }

            $('#result-show').val(codes.join('\n'));

        }

    }
    
    function loadTemplates(callback) {
        //加载所有模板,并加载第一个
        util.requestData('/tmpcode/listTemplates',function (templates) {
            if(templates.length > 0){
                $('#templates').empty();
                for (var i=0;i<templates.length;i++){
                    $('#templates').append('<option value="'+templates[i]+'">'+templates[i]+'</option>')
                }

                if(callback){
                    callback(templates);
                }

            }
        });
    }

    return templatecode.init();
});