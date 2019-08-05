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
        downloadPath:'/file/manager/downloadPath',
        multiTableSchemaConvert:'/code/multiTableSchemaConvert'
    };
    var modul = 'tableTemplate';
    var codeSchemaModul = 'codeSchema';

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
            var connName = $('#conns').val();
            var schemaName = $('#schemas').val();
            var tableName = currentTable(opts);
            util.requestData(apis.columns,{tableName:tableName,connName:connName,schemaName:schemaName},function (columns) {
                var columnNames = columns.map(column => {return column.columnName});
                layer.alert(columnNames.join(','));
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
     * 从后台请求表格数据
     * @param keyword
     * @param callback
     */
    function searchRequest(keyword, callback) {
        var index = layer.load(1, {
            shade: [0.1,'#fff']
        });
        util.requestData(apis.search,{connName:tablehelp.connName,schemaName:tablehelp.schemaName,keyword:keyword},function (tables) {
            callback(tables);
            layer.close(index);
        },function () {
            layer.close(index);
        });
    }

    /**
     * 搜索相匹配的结果,有可能数据表未初始化,需要做加载进度条
     * @param keyword
     */
    function search(keyword) {
        $('#columns>tbody').empty();
        searchRequest(keyword,function (tables) {
            var htmlCode = [];
            for(var i=0;i<tables.length;i++){
                if(!tables[i].tableName)continue;
                htmlCode.push('<li class="list-group-item" tableName = "'+tables[i].tableName+'"> <i class="fa fa-table"></i> '+tables[i].tableName+'('+(tables[i].comments || '未说明')+')</li>')
            }
            $('#tables').empty().html(htmlCode.join(''));

            $('#tables>li:first').addClass('active').click();
        });
    }

    function bindEvents() {
        var events = [{selector:'#conns',types:['change'],handler:switchConn},
            {selector:'#schemas',types:['change'],handler:switchSchema},
            {selector:'#search',types:['keyup'],handler:keyupSearch},
            {selector:'#btnsearch',types:['click'],handler:clickSearch},
            {selector:'#multisearch',types:['keyup'],handler:multiKeyupSearch},
            {selector:'#multisearchBtn',types:['click'],handler:multiClickSearch},
            {selector:'#seevars',types:['click'],handler:seeVars},
            {selector:'#plustemplate',types:['click'],handler:plusTemplate},
            {parent:'#tables',selector:'li',types:['click'],handler:columnsView},
            {selector:'#templates',types:['change'],handler:switchTemplate},
            {selector:'#codeschema',types:['click'],handler:codeSchemaDialog},
            {parent:'#codeSchemaDialog>ul.list-group',selector:'li',types:['click'],handler:makeCodeFromSchema},
            {selector:'#multiTableSchemaCode',types:['click'],handler:multiTableSchemaCode},
            {parent:'#multitableschemadialog ul.list-group',selector:'li',types:['click'],handler:switchCodeSchema},
            {selector:'#search',types:['keydown'],handler:function (event) {
                    var event = event || window.event;
                    if(event.keyCode == 13){
                        clickSearch();
                    }
                }
            },{selector:'#multisearch',types:['keydown'],handler:function (event) {
                    var event = event || window.event;
                    if(event.keyCode == 13){
                        multiClickSearch();
                    }
            }}];

        function multiSearch(keyword) {
            var $tbody = $('#multitableschemadialog').find('tbody').empty();
            var selectedTables = $('#multitableschemadialog').data('selectedTables');
            searchRequest(keyword,function (tables) {
                //标记表是否被选中
                for(var i=0;i<tables.length;i++){
                    if($.inArray(tables[i].tableName,selectedTables) != -1){
                        tables[i].selected = true;
                    }
                }
               require(['template','icheck'],function (template) {
                   var htmlCode = template('generatetablestemplate',{tables:tables});
                   $tbody.html(htmlCode);
                   $tbody.closest('table').find('input:checkbox').iCheck({
                       checkboxClass: 'icheckbox_square-green'
                   });
               })
            });
        }

        function multiKeyupSearch() {
            var keyword = $(this).val().trim();
            if(keyword.length > 10){
                if(keyword.endsWith(':'))return ;
                multiSearch(keyword);
            }
        }

        function multiClickSearch() {
            var keyword = $('#multisearch').val().trim();
            if(keyword.endsWith(':') )return ;
            multiSearch(keyword);
        }

        function switchCodeSchema() {
            $(this).siblings().removeClass('active');
            $(this).addClass('active');
            $(this).closest('ul').data('value',$(this).attr('value'));
        }


        function multiTableSchemaCode() {
            //加载所有模板信息和表信息
            var $codeSchemaUl = $('#multitableschemadialog').find('ul.list-group').empty();
            util.requestData(apis.templateNames,{modul:codeSchemaModul},function (codeSchemas) {
                for(var i=0;i<codeSchemas.length;i++){
                    var clazz = (i==0 ? 'list-group-item active':'list-group-item');
                    $codeSchemaUl.append('<li class="'+clazz+'" value="'+codeSchemas[i]+'">'+codeSchemas[i]+'</li>')
                }
            });

            //记录所有选中表的信息(初次记录,所有表选中)
            searchRequest('',function (tables) {
                var tableNames = [];
                for (var i=0;i<tables.length;i++){
                    tableNames.push(tables[i].tableName);
                }
                $('#multitableschemadialog').data('selectedTables',tableNames);

                dialog.create('多表使用方案生成代码')
                    .setContent($('#multitableschemadialog'))
                    .setWidthHeight('90%', '90%')
                    .addBtn({type:'yes',text:'确定',handler:requestCodeTicket})
                    .build();

                //触发表搜索
                $('#multisearchBtn').click();
            });

            function requestCodeTicket(index) {
                var selectedTables = $('#multitableschemadialog').data('selectedTables');
                var codeSchemaName = $codeSchemaUl.find('li.list-group-item.active').attr('value');
                util.requestData(apis.multiTableSchemaConvert,{connName:tablehelp.connName,schemaName:tablehelp.schemaName,tableNames:selectedTables,codeSchemaName:codeSchemaName},function (ticket) {
                    util.downFile(apis.downloadPath,{modul:'generate',baseName: 'tableTemplateCodePath/'+ticket},1000,function () {
                        layer.close(index);
                    });
                });

            }
        }

        /**
         * 使用方案生成代码
         */
        function makeCodeFromSchema() {
            var connName = $('#codeSchemaDialog').data('connName');
            var schemaName= $('#codeSchemaDialog').data('schemaName');
            var tableName = $('#codeSchemaDialog').data('tableName');
            var codeSchemas = $(this).data('codeSchemas');

            //打开一相进度框
            $('#codeSchemaProcessDialog').find('ul.list-group').empty();
            dialog.create('代码生成进度')
                .setContent($('#codeSchemaProcessDialog'))
                .setWidthHeight('400px', '400px')
                .build();


            var ticket = '';
            var $process = $('#codeSchemaProcessDialog',$(top.document)).find('ul.list-group');
            for(var i=0;i<codeSchemas.length;i++){
                util.requestData(apis.templateConvert,{ticket:ticket,connName:connName,schemaName:schemaName,tableName:tableName,templateName:codeSchemas[i],sync:true},function (_ticket) {
                    $process.append('<li class="list-group-item list-group-item-success">'+tableName+' -> '+codeSchemas[i]+' completed </li>');
                    ticket = _ticket;
                });
            }

            //开始下载
            util.downFile(apis.downloadPath,{modul:'generate',baseName: 'tableTemplateCodePath/'+ticket},1000,function () {

            });
        }

        /**
         * 打开方案对话框
         */
        function codeSchemaDialog() {
            //加载代码方案
            util.requestData(apis.templateNames,{modul:codeSchemaModul},function (codeSchemas) {
               var $listGroup = $('#codeSchemaDialog>ul.list-group').empty();
               for(var i=0;i<codeSchemas.length;i++){
                   var $li = $('<li class="list-group-item">'+codeSchemas[i]+'</li>');
                   $li.data('codeSchemas',codeSchemas[i].split('+'));
                   $listGroup.append($li);
               }
            });

            //记录数据
            var tableName = $('#templatecodeconfig').data('tableName');
            var connName = $('#conns').val();
            var schemaName = $('#schemas').val();
            $('#codeSchemaDialog').data('connName',connName);
            $('#codeSchemaDialog').data('schemaName',schemaName);
            $('#codeSchemaDialog').data('tableName',tableName);
            dialog.create('使用方案生成')
                .setContent($('#codeSchemaDialog'))
                .setWidthHeight('700px', '90%')
                .build();
        }
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
                //简单判断下文件类型
                var fileType = 'java';
                if(templateConfig.startsWith('<?xml')){
                    fileType = 'xml';
                }
                $('#templatePreview').empty();
                $('#templatePreview').append('<pre class="brush:\''+fileType+'\';"></pre>');
                $('#templatePreview>pre').text(templateConfig);

                //打开代码预览
                util.requestData(apis.codeConvertPreview,{templateName:template,connName:connName,schemaName:schemaName,tableName:tableName},function (formatCode) {
                    $('#codepreview').empty();
                    $('#codepreview').append('<pre class="brush:\''+fileType+'\';"></pre>');
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
            if(keyword.endsWith(':') )return ;
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