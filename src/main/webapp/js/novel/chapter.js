define(['util'],function (util) {
    var chapter = {};

    chapter.init = function () {
      //获取请求参数
      var parseUrl = util.parseUrl();
      chapter.params = parseUrl.params;

      //标记标题
        $('#bookname').text(parseUrl.params.bookname);

        var index = layer.load(1, {
            shade: [0.1,'#fff']
        });
        try{
            //加载所有章节
            chapter.reqParams = {netSource:parseUrl.params.netSource,novel:{bookId:parseUrl.params.bookId,chapterUrl:parseUrl.params.chapterUrl,name:parseUrl.params.bookname,netSource:parseUrl.params.netSource}};
            util.requestData('/novel/listChapters',chapter.reqParams,function (chapters) {
                $('#allchapter').empty();
                for(var i=0;i<chapters.length;i++){
                    $('#allchapter').append('<dd url="'+chapters[i].url+'"><a href="javascript:;"  target="_blank">'+chapters[i].sequence+'-'+chapters[i].title+'</a></dd>')
                }
                layer.close(index);

                //显示最新 10 章
                $('#newerchapter').empty();
                for(var i = chapters.length - 1;i>chapters.length - 11 ;i--){
                    $('#newerchapter').append('<dd url="'+chapters[i].url+'"><a href="javascript:;" target="_blank">'+chapters[i].sequence+'-'+chapters[i].title+'</a></dd>')
                }
            });
        }catch (e){
            layer.close(index);
        }
        
        $('.listmain').on('click','dd>a',function () {
           var url = $(this).parent().attr('url');
           var params = $.extend({},chapter.params,{
               chapterUrl:url,title:chapter.params.bookname+'-'+$(this).text()
           });

           util.tab('/app/novel/content.html',params);
        });
    };

    return chapter.init();
});