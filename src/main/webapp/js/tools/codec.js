define(['util'],function (util) {
   var codec = {encode:{}};

    /**
     * 编码/解码模块初始化
     */
    codec.encode.init = function () {
        $('#encode').find('button').bind('click',function () {
            var algorithm = $('#encode').find('select').val();
            var op = $(this).attr('name');
            var $origin = $('#encode').find('textarea[name=origin]');
            var $code = $('#encode').find('textarea[name=encode]');
            var origin = $origin.val().trim(),code = $code.val().trim();

            switch (op){
                case 'encode':
                    switch (algorithm){
                        case 'escape':
                            $code.val(escape(origin));
                            break;
                        case 'urlencode':
                            $code.val(encodeURI(origin));
                            break;
                        case 'urlencodeComponent':
                            $code.val(encodeURIComponent(origin));
                        case 'base64':
                            break;

                    }
                    break;
                case 'decode':
                    break;
            }
        });
    };

    codec.init = function () {

        this.encode.init();
    };

   return codec .init();
});