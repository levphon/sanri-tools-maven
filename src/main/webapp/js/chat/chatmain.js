define(['util','dialog','template'],function (util,dialog,template) {
    var baseAddress = util.root.replace('http:','ws:')
    var chat = {
        websocket:undefined,
        address:baseAddress+"/chat/",
        userName: undefined
    };

    chat.init = function () {
        openLoginDialog();

        bindEvent();

        window.onbeforeunload = function () {
            if(chat.websocket){
                chat.websocket.close();
            }
        }
        return this;
    }

    function bindEvent() {
        $('#messageSend').find('button').click(function () {
            var value = $('#messageSend').find('input').val();
            send(value);
        });
        $('#messageSend').find('input').keydown(function () {
            var event = event || window.event;
            if(event.keyCode == 13){
                var value = $('#messageSend').find('input').val();
                send(value);
            }

        });

        function send(value) {
            var data = {userName:chat.userName,content:value,time:new Date().getTime(),owner:'message-self',from:chat.userName};
            if(chat.websocket && chat.websocket.readyState == 1){
                appendMessage(data);
                chat.websocket.send(JSON.stringify(data));
                //清空消息
                $('#messageSend').find('input').val('')
            }else{
                layer.msg('连接已断开')
            }
        }
    }

    /**
     * 初始化聊天
     * @param userName
     */
    function initChat(userName) {
        //修改当前用户名
        $('#username').text(userName);
        //获取当前在线用户列表
        util.requestData('/chat/friends',function (friends) {
            var $friends = $('#friends').empty();
            for (var i=0;i<friends.length;i++){
                $friends.append('<li class="list-group-item" value="'+friends[i]+'">'+friends[i]+'</li>');
            }
        });
    }

    /**
     * 监听到来的消息
     */
    function listenMessage(event) {
        var data = JSON.parse(event.data);
        data.owner = 'message-other';
        data.userName = data.from;
        appendMessage(data);

        if(data.messageType != 'message'){
            switch (data.messageType) {
                case 'up':
                    $('#friends').append('<li class="list-group-item" value="'+data.to+'">'+data.to+'</li>');
                    break;
                case 'down':
                    $('#friends>li[value='+data.to+']').remove();
                    break;
            }
        }
    }

    function appendMessage(message) {
        template.helper('dateFormat',util.FormatUtil.dateFormat);
        var htmlCode = template('messageTemplate',message);
        $('#messagebox').append(htmlCode);
    }

    function openLoginDialog() {
        if(chat.websocket){return ;}
        $('#usernamedialog').find('input').val('');
        var buildDialog = dialog.create('登录')
            .setContent($('#usernamedialog'))
            .setWidthHeight('400px', '200px')
            .onClose(function () {      //阻止关闭
                return false;
            })
            .addBtn({type:'yes',text:'确定',handler:login})
            .build();

        /**
         * 登录
         */
        function login() {
            var userName = $('#usernamedialog').find('input').val().trim();
            util.requestData('/chat/login',{userName:userName},function (result) {
                if(result == 0){
                    chat.userName = userName;
                    if(!('WebSocket' in window)){
                        layer.alert('浏览器不支持 WebSocket')
                        return ;
                    }
                    //创建 websocket ,并绑定事件
                    chat.websocket = new WebSocket(chat.address+userName);
                    chat.websocket.onopen = function () {
                        layer.close(buildDialog.index);
                        initChat(userName);
                    }
                    chat.websocket.onerror = function () {
                        layer.alert('连接建立失败 '+chat.address);
                    }
                    chat.websocket.onclose = function () {
                        layer.alert('远程关闭了 socket 连接 ');
                    }
                    chat.websocket.onmessage = listenMessage;
                }else{
                    layer.msg('用户名['+userName+']已经存在');
                }
            })
        }
    }

    return chat.init();
});