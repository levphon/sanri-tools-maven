define(['util','dialog','contextMenu','javabrush','xmlbrush'],function (util,dialog) {
    var tablehelp = {
        connName:undefined,
        schemaName:undefined
    };
    var apis = {
        conns:'/sqlclient/connections',
        schemas:'/sqlclient/schemas',
        search:'/sqlclient/searchTables',
        columns:'/sqlclient/refreshTable',
        codeConvertPreview:'/code/codeConvertPreview',
        templateNames:'/file/manager/simpleConfigNames',
        writeConfig:'/file/manager/writeConfig',
        readConfig:'/file/manager/readConfig',
        templateConvert:'/code/templateConvert',
        downloadPath:'/file/manager/downloadPath'
    };
    var modul = 'tableTemplate';

    tablehelp.init = function () {
      initConns();

      bindEvents();

      createRightMenu();
      return this;
    };

    /**
     *  加载所有连接
     */
    function initConns() {
        util.requestData(apis.conns,function (conns) {
            var htmlCode = [];
            for (var i=0;i<conns.length;i++){
                htmlCode.push('<option value="'+conns[i]+'">'+conns[i]+'</option>');
            }
            $('#conns').empty().append(htmlCode.join(''));

            $('#conns').change();
        });
    }

    /**
     * 创建右键菜单
     */
    function createRightMenu() {
        $.contextMenu({
            selector: '#tables li',
            zIndex: 4,
            items:{
                templateCode:{name:'模板代码...',icon:'copy',callback:templateCode},
                columns:{name:'属性列',icon:'cut',callback:tableColumns}
            }
        });

        /**
         * 查看当前表的所有列
         * @param key
         * @param opts
         */
        function tableColumns(key,opts) {
            var tableName = currentTable(opts);
            util.requestData(apis.columns,{tableName:tableName},function (columns) {
                var columnNames = columns.map(column => {return column.columnName});
                layer.msg(columnNames.join(','));
            });
        }

        /**
         * 使用表来创建模板代码
         */
        function templateCode(key,opts) {
            var tableName = currentTable(opts);
            loadTemplates();
            //记录数据在对话框上
            $('#templatecodeconfig').data('tableName',tableName);

            dialog.create('模板代码['+tableName+']')
                .setContent($('#templatecodeconfig'))
                .setWidthHeight('90%', '90%')
                .addBtn({type:'yes',text:'生成代码',handler:writeCode})
                .addBtn({type:'button',text:'下载代码',handler:downloadCode})
                .build();

            /**
             * 生成本次模板代码
             */
            function writeCode() {
                var connName = $('#conns').val();
                var schemaName = $('#schemas').val();
                var ticket = $('#templatecodeconfig').data('ticket');
                var templateName = $('#templates').val();

                util.requestData(apis.templateConvert,{ticket:ticket,templateName:templateName,connName:connName,schemaName:schemaName,tableName:tableName},function (_ticket) {
                    layer.msg("代码生成成功,入场券是:"+_ticket);
                    $('#templatecodeconfig').data('ticket',_ticket);
                });
            }

            /**
             * 下载生成的所有模板代码
             * @param index
             */
            function downloadCode(index) {
                var ticket = $('#templatecodeconfig').data('ticket');
                //tableTemplateCodePath generate
                util.downFile(apis.downloadPath,{modul:'generate',baseName: 'tableTemplateCodePath/'+ticket},1000,function () {
                    layer.close(index);
                });
            }
        }

        function loadTemplates() {
            util.requestData(apis.templateNames,{modul:modul},function (templateNames) {
               var $template = $('#templates').empty();
               for (var i=0;i<templateNames.length;i++){
                   $template.append('<option value="'+templateNames[i]+'">'+templateNames[i]+'</option>');
               }
                $template.change();
            });
        }

        function currentTable(opts) {
            var tableName = opts.$trigger.attr('tableName');
            return tableName;
        }
    }

    /**
     * 搜索相匹配的结果,有可能数据表未初始化,需要做加载进度条
     * @param keyword
     */
    function search(keyword) {
        $('#columns>tbody').empty();
        var index = layer.load(1, {
            shade: [0.1,'#fff']
        });
        util.requestData(apis.search,{connName:tablehelp.connName,schemaName:tablehelp.schemaName,keyword:keyword},function (tables) {
            var htmlCode = [];
            for(var i=0;i<tables.length;i++){
                if(!tables[i].tableName)continue;
                htmlCode.push('<li class="list-group-item" tableName = "'+tables[i].tableName+'"> <i class="fa fa-table"></i> '+tables[i].tableName+'('+tables[i].comments+')</li>')
            }
            $('#tables').empty().html(htmlCode.join(''));

            $('#tables>li:first').addClass('active').click();
            layer.close(index);
        },function () {
            layer.close(index);
        });
    }

    function bindEvents() {
        var events = [{selector:'#conns',types:['change'],handler:switchConn},
            {selector:'#schemas',types:['change'],handler:switchSchema},
            {selector:'#search',types:['keyup'],handler:keyupSearch},
            {selector:'#btnsearch',types:['click'],handler:clickSearch},
            {selector:'#seevars',types:['click'],handler:seeVars},
            {selector:'#plustemplate',types:['click'],handler:plusTemplate},
            {parent:'#tables',selector:'li',types:['click'],handler:columnsView},
            {selector:'#templates',types:['change'],handler:switchTemplate},
            {selector:'#search',types:['keydown'],handler:function (event) {
                    var event = event || window.event;
                    if(event.keyCode == 13){
                        clickSearch();
                    }
                }
            }];

        /**
         * 模板切换时切换预览
         */
        function switchTemplate() {
            var template = $(this).val();
            var tableName = $('#templatecodeconfig').data('tableName');
            var connName = $('#conns').val();
            var schemaName = $('#schemas').val();

            //获取模板代码
            util.requestData(apis.readConfig,{modul:modul,baseName:template},function (templateConfig) {
                $('#templatePreview').empty();
                $('#templatePreview').append('<pre class="brush:\'java\';"></pre>');
                $('#templatePreview>pre').text(templateConfig);

                //打开代码预览
                util.requestData(apis.codeConvertPreview,{templateName:template,connName:connName,schemaName:schemaName,tableName:tableName},function (formatCode) {
                    $('#codepreview').empty();
                    $('#codepreview').append('<pre class="brush:\'java\';"></pre>');
                    $('#codepreview>pre').text(formatCode);
                    SyntaxHighlighter.highlight();
                });
            })
        }

        /**
         * 添加模板
         */
        function plusTemplate() {
            dialog.create('添加模板')
                .setContent($('#plustemplatedialog'))
                .setWidthHeight('700px', '90%')
                .addBtn({type:'yes',text:'确定',handler:saveTemplate})
                .build();

            /**
             * 保存模板
             */
            function saveTemplate(index) {
                var data = util.serialize2Json($('#plustemplatedialog form').serialize());
                data.modul = modul;
                util.requestData(apis.writeConfig,data,function () {
                    layer.close(index);
                })
            }
        }

        /**
         * 查看可用变量
         */
        function seeVars() {
            dialog.create('可用变量名列表')
                .setContent($('#varslist'))
                .setWidthHeight('500px', '90%')
                .build();
        }

        function columnsView() {
            var tableName = $(this).attr('tableName');
            $('#tables>li').removeClass('active');$(this).addClass('active');
            util.requestData(apis.columns,{connName:tablehelp.connName,schemaName:tablehelp.schemaName,tableName:tableName},function (columns) {
               var htmlCode = [];
               for(var i=0;i<columns.length;i++){
                   htmlCode.push('<tr>');
                   htmlCode.push('<td>'+columns[i].columnName+'</td>');
                   htmlCode.push('<td>'+columns[i].columnType.dataType+'</td>');
                   htmlCode.push('<td>'+columns[i].comments+'</td>');
                   htmlCode.push('</tr>');
               }
               $('#columns>tbody').empty().append(htmlCode.join(''));
            });
        }

        function clickSearch() {
            var keyword = $('#search').val().trim();
            if(keyword.endsWith(':') || !keyword)return ;
            search(keyword);
        }
        function keyupSearch() {
            var keyword = $(this).val().trim();
            if(keyword.length > 10){
                if(keyword.endsWith(':'))return ;
                search(keyword);
            }
        }

        /**
         * 切换数据源
         */
        function switchConn(){
            tablehelp.connName  = $(this).val();

            loadSchemas(tablehelp.connName );
        }

        /**
         * 切换数据库
         */
        function switchSchema(){
            tablehelp.schemaName = $(this).val();

            //重新发起搜索
            $('#btnsearch').click();
        }

        util.regPageEvents(events);
    }


    /**
     * 切换数据源时加载所有的数据库
     * @param connName
     */
    function loadSchemas(connName) {
        util.requestData(apis.schemas,{connName:connName},function (schemas) {
            var htmlCode = [];
            for (var i=0;i<schemas.length;i++){
                htmlCode.push('<option value="'+schemas[i].schemaName+'">'+schemas[i].schemaName+'</option>');
            }
            $('#schemas').empty().append(htmlCode.join(''));

            $('#schemas').change();
        });

    }

    return tablehelp.init();
});