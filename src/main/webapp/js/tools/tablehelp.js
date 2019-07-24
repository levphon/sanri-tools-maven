define(['util','dialog','contextMenu'],function (util,dialog) {
    var tablehelp = {
        connName:undefined,
        schemaName:undefined
    };
    var apis = {
        conns:'/sqlclient/connections',
        schemas:'/sqlclient/schemas',
        search:'/sqlclient/searchTables',
        columns:'/sqlclient/refreshTable'
    };

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
                templateCode:{name:'模板代码...',icon:'copy',callback:templateCode}
            }
        });
        $('#testing').click(templateCode);

        /**
         * 使用表来创建模板代码
         */
        function templateCode() {
            dialog.create('模板代码')
                .setContent($('#templatecodeconfig'))
                .setWidthHeight('90%', '90%')
                .addBtn({type:'yes',text:'下载代码',handler:downloadCode})
                .build();

            function downloadCode(index) {

            }
        }
    }

    /**
     * 搜索相匹配的结果
     * @param keyword
     */
    function search(keyword) {
        $('#columns>tbody').empty();
        util.requestData(apis.search,{connName:tablehelp.connName,schemaName:tablehelp.schemaName,keyword:keyword},function (tables) {
            var htmlCode = [];
            for(var i=0;i<tables.length;i++){
                if(!tables[i].tableName)continue;
                htmlCode.push('<li class="list-group-item" tableName = "'+tables[i].tableName+'"> <i class="fa fa-table"></i> '+tables[i].tableName+'('+tables[i].comments+')</li>')
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
            {selector:'#seevars',types:['click'],handler:seeVars},
            {selector:'#plustemplate',types:['click'],handler:plusTemplate},
            {parent:'#tables',selector:'li',types:['click'],handler:columnsView},
            {selector:'#search',types:['keydown'],handler:function (event) {
                    var event = event || window.event;
                    if(event.keyCode == 13){
                        clickSearch();
                    }
                }
            }];

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
            function saveTemplate() {
                var data = util.serialize2Json($('#plustemplatedialog form').serialize());
                console.log(data);
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