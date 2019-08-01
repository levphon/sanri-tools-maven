define(['util'],function (util) {
    var refrence = {};

    refrence.init = function () {

        //请求当前连接
        util.requestData('/refrence/current',function (connect) {
            $('#connect>button>span:eq(0)').text(connect);
            refrence.connect = connect;

            //请求当前连接串
            util.requestData('/sqlclient/connectString',{connName:refrence.connect},function (connectString) {
                $('#connect').next('input').val(connectString);
            });
        });

        //请求所有连接
        util.requestData('/sqlclient/connections',function (connections) {
            var $menu = $('#connect>ul.dropdown-menu');
            for(var i=0;i<connections.length;i++){
                $('<li><a href="javascript:void(0);">'+connections[i]+'</a></li>').appendTo($menu);
            }
        });

        //加载表关联
        $('#settable').bind('click',function () {
           var $input = $(this).parent().prev();
           var tablename = $input.val().trim();

           var parentTree = [],childTree = [];

           loadParent(tablename,parentTree);

           loadChild(tablename,childTree);

            renderTree(parentTree,$('#parentTree'));
            renderTree(childTree,$('#childTree'));
        });

        $('#truncate').bind('click',function () {
            var $input = $(this).parent().next();
            var tablename = $input.val().trim();

            var parentTree = [],childTree = [];

           loadParent(tablename,parentTree);

           loadChild(tablename,childTree);

           truncateTree(parentTree);
           truncateTree(childTree);

        });

        return this;
    }

    /**
     * 树 truncate
     * @param tree
     */
    function truncateTree(tree) {
        if(tree){
            for (var i=0;i<tree.length;i++){
                var node = tree[i];
                util.requestData('/refrence/truncate',{tablename:node.name,sync:true},function (update) {
                    console.log('删除表 '+node.name+' 数据量:'+update);
                });

                truncateTree(node.children);
            }
        }
    }

    /**
     * 树渲染
     * @param tree
     * @param container
     */
    function renderTree(tree,$container) {
        $container.empty();
        if(tree){
           for(var i = 0 ;i<tree.length;i++){
               var current = tree[i];
               var $node = $('<li><span class="padding-bottom text-success " href="javacript:;">'+current.name+'</li></span>').appendTo($container);
               var $childContainer = $('<ul></ul>').appendTo($node);
               renderTree(current.children,$childContainer);
           }
        }
    }

    /**
     * 递归加载父级
     * @param tablename
     */
    function loadParent(tablename,parentTree) {
        util.requestData('/refrence/parents',{tablename:tablename,sync:true},function (parents) {
            if(parents){
                for(var i=0;i<parents.length;i++){
                    var parent = parents[i];
                    var node = {name:parent,children:[]};
                    parentTree.push(node);
                    loadParent(parent,node.children);
                }
            }
        });
    }

    /**
     * 递归加载子级
     * @param tablename
     */
    function loadChild(tablename,childTree) {
        util.requestData('/refrence/childs',{tablename:tablename,sync:true},function (parents) {
            if(parents){
                for(var i=0;i<parents.length;i++){
                    var parent = parents[i];
                    var node = {name:parent,children:[]};
                    childTree.push(node);
                    loadParent(parent,node.children);
                }
            }
        });
    }

    return refrence.init();
});