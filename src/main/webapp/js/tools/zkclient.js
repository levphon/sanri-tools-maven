define(['util','dialog','template','ztree','contextMenu'],function (util,dialog,template) {
    var zkclient = {},$ztree = $('#zooconntree');
    var rightmenu = {};
    var ztreeConfig = {
        //nameIsHTML 名称支持 html 格式
        view:{dblClickExpand : false,showLine:true,selectedMulti:false,nameIsHTML:true},
        data:{
            key:{name:'name'},		//名称的键
            simpleData:{enable:true,idKey:'id',pIdKey:'pid',rootPId:-1}
        },
        callback:{
            onClick:fetchNodeData,
            onDblClick:fetchChildNodes
        }
    }

    var apis = {
        serializes:'/zk/serializes',
        createConn:'/file/manager/writeConfig',
        connNames:'/file/manager/configNames',
        detail:'/file/manager/readConfig',
        childrens:'/zk/childrens',
        readData:'/zk/readData',
        meta:'/zk/meta',
        acls:'/zk/acls',
        deleteNode:'/zk/deleteNode'
    }

    // 右键菜单部分
    /**
     * 查找到当前右击的树节点
     */
    rightmenu.findTreeNode = function(key,opts){
        if(opts.$trigger ){
            var treeId = opts.$trigger.attr('id');
            return zkclient.ztree.getNodeByTId(treeId);
        }
        return null;
    }
    /**
     * 创建子节点
     * @param key
     * @param opts
     */
    rightmenu.createNode = function(key,opts){
        var treeNode = rightmenu.findTreeNode(key,opts);
        if(!treeNode){return ;}
        layer.msg('创建子节点'+treeNode.name + '的子节点');
    }
    /**
     * 删除节点,包含子节点
     * @param key
     * @param opts
     */
    rightmenu.dropNode = function(key,opts){
        var treeNode = rightmenu.findTreeNode(key,opts);
        if(!treeNode){return ;}
        layer.confirm('是否删除节点:'+treeNode.name,function (r) {
            if(r){
                var path = getXpath(treeNode);
                util.requestData(apis.deleteNode,{path:path,name:zkclient.conn},function () {
                   layer.msg('删除节点成功');
                   //重新加载子树 TODO ,暂时加载整颗树
                    loadZtree();
                });
            }
        })
        // util.requestData()
    }
    rightmenu.xpath= function(key,opts){
        var treeNode = rightmenu.findTreeNode(key,opts);
        if(!treeNode){return ;}

        var xpath = getXpath(treeNode);
        layer.alert(xpath);
    }

    zkclient.init = function () {
        //加载所有序列化工具
        util.requestData(apis.serializes,function (serializes) {
            var $serializes =  $('#nodedata').find('select[name=deserialize]').empty();
            for(var i=0;i<serializes.length;i++){
                $serializes.append('<option value="'+serializes[i]+'">'+serializes[i]+'</option>')
            }
            //选中第一个
            $serializes.val(serializes[0]);
        });

        bindEvents();

        loadConns(function () {
            $('#connect>.dropdown-menu>li:first').click();
            $('#connect>.dropdown-menu').dropdown('toggle');
        });

        //初始化右键菜单
        $.contextMenu({
            selector:'#zooconntree li',
            zIndex:4,
            items:{
                createNode:{name:'创建子节点...',icon:'edit',callback:rightmenu.createNode},
                dropNode:{name:'删除节点',icon:'delete',callback:rightmenu.dropNode},
                xpath:{name:'xpath',icon:'delete',callback:rightmenu.xpath}
            }
        });

        return this;
    }

    function nodeZkData(xpath,deserialize){
        util.requestData(apis.readData, {name: zkclient.conn, path: xpath,deserialize:deserialize}, function (nodeData) {
            if(typeof nodeData != 'string'){
                nodeData = JSON.stringify(nodeData);
            }
            $('#nodedata textarea').val(nodeData);
        });
    }

    function bindEvents() {
        var events = [{parent:'#connect>.dropdown-menu',selector:'li',types:['click'],handler:switchConn},
            {selector:'#nodedata select[name=deserialize]',types:['change'],handler:switchDeserialize},
            {selector:'#newconnbtn',types:['click'],handler:newconn}];
        util.regPageEvents(events);

        /**
         * 新连接
         */
        function newconn() {
            dialog.create('新连接')
                .setContent($('#newconn'))
                .setWidthHeight('90%','40%')
                .addBtn({type:'yes',text:'确定',handler:function(index, layero){
                        var params = util.serialize2Json($('#newconn>form').serialize());
                        if(!params.name || !params.connectStrings){
                            layer.msg('请将信息填写完整');
                            return ;
                        }
                        params.modul = 'zookeeper';
                        params.baseName = params.name;
                        params.content = params.connectStrings;
                        util.requestData(apis.createConn,params,function () {
                            layer.close(index);

                            loadConns(function (conns) {
                                if(conns){
                                    //请求最后一个连接,并选中
                                    $('#connect>.dropdown-menu>li[name='+params.name+']').click();
                                    $('#connect>.dropdown-menu').dropdown('toggle');
                                }
                            });
                        });
                    }})
                .build();
        }

        /**
         * 切换序列化工具
         */
        function switchDeserialize() {
            //获取选中树节点的 xpath
            var nodes = zkclient.ztree.getSelectedNodes();
            var xpath = getXpath(nodes[0]);

            if(!xpath){
                return ;
            }
            var deserialize = $(this).val();
            nodeZkData(xpath,deserialize);
        }

        function switchConn() {
            var conn = $(this).data('value');
            zkclient.conn = conn;

            $('#connect>button>span:eq(0)').text(conn);
            util.requestData(apis.detail,{modul:'zookeeper',baseName:conn},function (address) {
                $('#connect').next('input').val(address);
            });
            $('#connect>.dropdown-menu').dropdown('toggle');

            loadConns(function () {
                loadZtree();
            });
        }

    }
    
    function loadZtree() {
        var index = layer.load(1, {
          shade: [0.1,'#fff']
        });
        if(zkclient.ztree){
            zkclient.ztree.destroy();
        }
        util.requestData(apis.childrens,{name:zkclient.conn,path:'/'},function (roots) {
            var rootNodes = [];
            for(var i=0;i<roots.length;i++){
                rootNodes.push({name:roots[i],id:roots[i]+'_0_',pid:'-1',deep:0,nodeType:'dataNode'});
            }
            zkclient.ztree = $.fn.zTree.init($ztree, ztreeConfig, rootNodes);
            layer.close(index);
        },function () {
            layer.close(index);
        });
    }

    function loadConns(callback) {
        util.requestData(apis.connNames,{modul:'zookeeper'},function (paths) {
            var conns = paths.map(function (path) {
                return path.pathName;
            });
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

    /**
     * 双击时抓取子节点
     */
    function fetchChildNodes(event, treeId, treeNode){
        if(!treeNode){
            return ;
        }
        //如果有子节点，无需重复加载，后面再考虑强制刷新 TODO
        if(treeNode.children && treeNode.children.length > 0){
            ztree.expandNode(treeNode);
            return ;
        }
        if(treeNode.nodeType == 'conn'){
            //连接节点，直接抓取根数据
            loadNode(treeNode.name);
        }else{
            var connName = treeNode.connName;
            loadNode(connName, treeNode.id);
        }
    }

    /**
     * 双击节点加载第一级子节点
     * connName: 连接名称
     * nodeId: 节点id
     * 当nodeId 为空时表示双击根节点
     */
    function loadNode(connName,nodeId){
        var nodePath = undefined;
        if(!nodeId){
            nodePath = '/';
        }else{
            var currTreeNode = zkclient.ztree.getNodeByParam('id',nodeId);
            if(currTreeNode){
                nodePath = getXpath(currTreeNode);
            }
        }

        if(nodePath){
            var parentNode = undefined;
            if(nodeId){
                parentNode = zkclient.ztree.getNodeByParam('id',nodeId);
            }else{
                parentNode = zkclient.ztree.getNodeByParam('id',connName+'_0_');
            }
            //加载子节点数据
            util.requestData(apis.childrens,{name:zkclient.conn,path:nodePath},function(childs){
                if(!childs || childs.length == 0 ){
                    return ;
                }
                renderNode(connName, parentNode, childs);
            });
        }
    }

    /**
     * 渲染节点
     * connName: 连接名称
     * parentId : 父级节点编号
     * childeNodes : 子节点序列 Array,没有时传空数组
     */
    function renderNode(connName,parentTreeNode,childs){
        var childNodes = [];
        var childDeep = parentTreeNode.deep + 1;
        for(var i=0;i<childs.length;i++){
            var currNodeId = connName+'_'+childDeep+'_'+childs[i] + Math.round(Math.random() * 100);
            var nodeName = decodeURIComponent(childs[i]);
            childNodes.push({id:currNodeId ,name:nodeName,deep:childDeep,nodeType:'dataNode',connName:connName});
        }
        zkclient.ztree.addNodes(parentTreeNode,childNodes);
    }

    /**
     * 单击时抓取节点数据
     * 需要区分是单击还是双击
     */
    function fetchNodeData(event, treeId, treeNode) {
        if (zkclient.clickTimeout) {
//		console.log('取消单击事件:'+metatree.clickTimeout);
            window.clearTimeout(zkclient.clickTimeout);					//取消单击延时未执行的方法
            zkclient.clickTimeout = null;
        } else {
            zkclient.clickTimeout = window.setTimeout(function () {
                //执行单击事件
                zkclient.clickTimeout = null;
                fetchNodeDataSingleClick(treeNode);
            }, 250);
        }
    }

    /**
     * 获取节点路径,使用 / 做为分隔,以 / 开头
     */
    function getXpath(treeNode){
        var nodes = treeNode.getPath();
        if(nodes){
            var xPath = [];
            for(var i=0;i<nodes.length;i++){
                if(nodes[i].nodeType == 'conn'){
                    continue;
                }
                xPath.push(nodes[i].name);
            }
            return '/'+xPath.join('/');
        }
        return '/';		//如果当前节点是根节点,返回 /
    }

    function fetchNodeDataSingleClick(treeNode) {
        $('#nodenameshow').val(treeNode.name);

        //节点路径
        var nodePath = getXpath(treeNode);
        //序列化工具
        var deserialize = $('#nodedata').find('select[name=deserialize]').val();

        //获取节点数据
        nodeZkData(nodePath,deserialize);

        //获取节点 ACL 权限信息
        util.requestData(apis.acls, {name:  zkclient.conn, path: nodePath}, function (nodeACLs) {
            var htmlCode = template('nodeacls', {acls: nodeACLs});
            $('#nodeACL').find('tbody').html(htmlCode);
        });
        //获取节点元数据信息
        util.requestData(apis.meta, {name:  zkclient.conn, path: nodePath}, function (stat) {
            var $tbody = $('#nodeattr tbody').empty();
            if (stat) {
                var index = 0;
                var htmlCodes = [];
                for (var key in stat) {
                    var currAttr = {index: ++index, key: key, value: stat[key]};
                    switch (key) {
                        case 'version':
                            currAttr['remark'] = '数据版本';
                            break;
                        case 'cversion':
                            currAttr['remark'] = '子节点版本';
                            break;
                        case 'aversion':
                            currAttr['remark'] = 'ACL 版本';
                            break;
                        case 'dataLength':
                            currAttr['remark'] = '数据长度';
                            break;
                        case 'ctime':
                            currAttr['remark'] = '节点创建时间';
                            currAttr['value'] = stat[key] + '(' + util.FormatUtil.dateFormat(stat[key], 'yyyy-MM-dd HH:mm:ss') + ')';
                            break;
                        case 'mtime':
                            currAttr['remark'] = '节点最后一次被修改的时间';
                            currAttr['value'] = stat[key] + '(' + util.FormatUtil.dateFormat(stat[key], 'yyyy-MM-dd HH:mm:ss') + ')';
                            break;
                        case 'numChildren':
                            currAttr['remark'] = '子节点个数';
                            break;
                        case 'emphemeralOwner':
                            currAttr['remark'] = '节点拥有者会话ID';
                            break;
                    }
                    var htmlCode = template('attrTemplate', currAttr);
                    htmlCodes.push(htmlCode);
                }
                $tbody.append(htmlCodes.join(''));
            }
        });
    }
    return zkclient.init();
});