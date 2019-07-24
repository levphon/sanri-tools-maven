define(['util','fancybox'],function (util) {
    $('.fancybox').fancybox({
        hideOnOverlayClick:false,
        afterClose:function () {
            this.element.find('img').show();
        }
    });

   $(document).bind('paste',function (e) {
        let clipboardData = e.originalEvent.clipboardData;
        let items = clipboardData.items;
        if(items){
            var blob = undefined;
            var file = undefined;

            for(var i=0;i<items.length;i++){
                var itemType = items[i].type;
                if(itemType.indexOf('image') != -1){
                     blob =  items[i].getAsFile();
                     file = clipboardData.files[i];
                     break;
                }
            }

            if(blob){
                //只解析第一张图,应该也只会有一张图吧
                 var blobUrl=URL.createObjectURL(blob);
                 document.getElementById("imgNode").src=blobUrl;

                 //上传图片解析
                var formData = new  FormData();
                formData.append('image',file);
                var index = layer.load(1, {
				  shade: [0.1,'#fff']
				});
                util.postFile('/ocr/resolve',formData,function (data) {
                    $('#result').html(data.join('<br/>'));
                    layer.close(index);
                });

            }else{
                layer.msg('没有在粘贴板找到图片信息,请截图后再试')
            }
        }
    });
});