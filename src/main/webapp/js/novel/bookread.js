define(['util'],function (util) {
    var bookcontent = {};
    bookcontent.init = function () {
        var parseUrl = util.parseUrl();
        bookcontent.params = parseUrl.params;

        $('#chaptername').text(parseUrl.params.title);

        //请求参数
        var reqParams = {netSource:parseUrl.params.netSource,novel:{bookId:parseUrl.params.bookId,chapterUrl:parseUrl.params.chapterUrl},
            chapter:{url:parseUrl.params.chapterUrl}
        };
        var index = layer.load(1, {
            shade: [0.1,'#fff']
        });
        try{
            util.requestData('/novel/contentHtml',reqParams,function (contentHtml) {
                $('#contentHtml').html(contentHtml);
            });
            layer.close(index);
        }catch (e){
            layer.close(index);
        }

        return this;
    };

    return bookcontent.init();
});