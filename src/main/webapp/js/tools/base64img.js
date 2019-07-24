define(['util','zclip','fancybox'],function (util) {
    var base64 = {};

    base64.init = function () {
        $('.fancybox').fancybox({
            afterClose:function () {
                this.element.find('img').show();
            }
        });

        $('#file').bind('change',function () {
            $('#imagepreview>.image').empty();

            var filelist = $(this)[0].files;

            $('#imagepreview').hide();
            if(filelist && filelist.length > 0){
                $('#imagepreview').show();
                var currentfile = filelist[0];
                var fileSize = currentfile.size;
                var kb = (fileSize / 1024);
                $('#imginfo').text(kb.toFixed(2) +'kb/'+fileSize+'B');

                //读取图片进行预览
                var fileReader = new FileReader(); //创建一个filereader对象
                var img = new Image();  //创建一个图片对象
                fileReader.readAsDataURL(currentfile)  //读取所上传对的文件
                fileReader.onload = function () {
                    img.src = this.result;

                    $('#base64').val(img.src);
                    $('#imagepreview>.image')[0].appendChild(img);
                    $('#imagepreview>.image>img').attr('id','imgNode')
                }
            }

        });

        $('#base642img').bind('click',function () {
            var val = $('#base64').val().trim();
            $('#imagepreview').show();
            $('#imagepreview>.image').html('<img src="'+val+'" />');
        });

        $('#copytext').zclip({
           path: "ZeroClipboard.swf",
           copy: function(){
                var value = $('#base64').val().trim();
                if(value) {
                    return value.split(',')[1];
                }
                return "";
           }
        });

        return this;
    }

    return base64.init();
});