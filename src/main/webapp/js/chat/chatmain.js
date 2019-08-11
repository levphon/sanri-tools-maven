define(['util','dialog','template'],function (util,dialog,template) {
    var baseAddress = util.root.replace('http:','ws:')
    var chat = {
        websocket:undefined,
        address:baseAddress+"/chat/",
        userName: undefined
    };

    chat.init = function () {
        loadHistoryMessages();
        bindEvent();

        window.onbeforeunload = function () {
            if(chat.websocket){
                chat.websocket.close();
            }
        };
        //这个需要延时打开，防止样式还未加载
        setTimeout(openLoginDialog,100);
        return this;
    };

    /**
     * 加载历史消息
     */
    function loadHistoryMessages() {
        let localMessagesItem = sessionStorage.getItem('localMessages') || '[]';
        let localMessages = JSON.parse(localMessagesItem);
        template.helper('dateFormat',util.FormatUtil.dateFormat);
        for (var i=0;i<localMessages.length;i++){
            var htmlCode = template('messageTemplate',localMessages[i]);
            $('#messagebox').append(htmlCode);
        }
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

        $('#usernamedialog').find('input').bind('keydown',function (event) {
            var event = event || window.event;
            if(event.keyCode == 13){
                let dialog = $('#usernamedialog').data('dialog');
                login(dialog);
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
        //每来一句消息，将消息保存到本地，使用 sessionStorage ,不是和用户关联，用户名只是一个昵称而已，是和当前 session 关联
        if(message.from != 'system'){       //不存储 system 信息
            let localMessagesItem = sessionStorage.getItem('localMessages') || '[]';
            let localMessages = JSON.parse(localMessagesItem);
            localMessages.push(message);
            sessionStorage.setItem('localMessages',JSON.stringify(localMessages));
        }

        template.helper('dateFormat',util.FormatUtil.dateFormat);
        var htmlCode = template('messageTemplate',message);
        $('#messagebox').append(htmlCode);

        adjustScroll();
    }

    /**
     * 调整滚动条
     */
    function adjustScroll() {
        $('#messagebox')[0].scrollTop = $('#messagebox')[0].scrollHeight;
    }

    function openLoginDialog() {
        if(chat.websocket){return ;}
        let userName = sessionStorage.getItem('userName');
        if(userName){
            $('#usernamedialog').find('input').val(userName);
            login();

            //刷新还需要调整滚动条到最下面
            adjustScroll();
        }else{
         var buildDialog = dialog.create('登录')
            .setContent($('#usernamedialog'))
            .setWidthHeight('400px', '140px')
            .onClose(function () {      //阻止关闭
                return false;
            })
            .onOpen(function () {
                $('#usernamedialog').find('input').focus()
            })
            .addBtn({type:'yes',text:'确定',handler:function () {
                    login(buildDialog);
                }})
            .build();
        }
    }

    /**
     * 登录
     */
    function login(buildDialog) {
        var userName = $('#usernamedialog').find('input').val().trim();
        util.requestData('/chat/login',{userName:userName},function (result) {
            if(result == 0){
                chat.userName = userName;
                if(!('WebSocket' in window)){
                    layer.alert('浏览器不支持 WebSocket')
                    return ;
                }
                //存储当前用户名,刷新不再弹框
                sessionStorage.setItem('userName',userName);

                //创建 websocket ,并绑定事件
                chat.websocket = new WebSocket(chat.address+userName);
                chat.websocket.onopen = function () {
                    if(buildDialog){
                        layer.close(buildDialog.index);
                    }
                    initChat(userName);
                };
                chat.websocket.onerror = function () {
                    layer.alert('连接建立失败 '+chat.address);
                };
                chat.websocket.onclose = function () {
                    layer.alert('远程关闭了 socket 连接 ');
                };
                chat.websocket.onmessage = listenMessage;
            }else{
                layer.msg('用户名['+userName+']已经存在');
            }
        })
    }

    return chat.init();
});