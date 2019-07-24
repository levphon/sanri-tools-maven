define(['util'],function (util) {
   var netsource = {};
   netsource.init = function () {
       bindEvents();
       return this;
   }

    /**
     * 表格渲染
     */
    var showSeq = ['title','panUrl','fileSize','password']
   function renderTable(result) {
       if(result){
           if(result.hasNext){
               changePage(true);
           }
           var data = result.data;
           var $tableBody = $('#result>tbody').empty();
           var htmlCode = [];
           for(var i=0;i<data.length;i++){
               htmlCode.push('<tr>');
               htmlCode.push('<td>'+data[i]["title"]+'</td>');
               htmlCode.push('<td><a href="'+data[i]["panUrl"]+'" target="_blank">'+data[i]["panUrl"]+'</a></td>');
               htmlCode.push('<td>'+data[i]["fileSize"]+'</td>');
               if(data[i]['panUrl'].indexOf('init') != -1){
                   htmlCode.push('<td class="text-danger">邀请码</td>');
               }else {
                   htmlCode.push('<td class="text-success">直接访问</td>');
               }
               htmlCode.push('</tr>')
           }
           $tableBody.append(htmlCode.join(''));
       }
   }

    /**
     *
     * @param arrow true 下一页,false 上一页
     */
    function changePage(arrow) {
        var currPrev = $('#page>li[name=prev]').data('value') || 0;
        var currNext = $('#page>li[name=next]').data('value') || 1;
       var prev = arrow ? ++currPrev:--currPrev;
       var next = arrow ? ++currNext:--currNext;

        $('#page>li[name=prev]').data('value',prev);
        $('#page>li[name=next]').data('value',next);

        $('#page>li[name=prev]>a>span').text(prev);
        $('#page>li[name=next]>a>span').text(next);
    }

   function bindEvents() {
       var events = [{selector:'#searchBtn',types:['click'],handler:callSearch},
           {selector:'#page>li>a',types:['click'],handler:switchPage}];
       util.regPageEvents(events);

       $('#search>input[name=keyword]').bind('keydown',function (event) {
           var event = event || window.event;
           if(event.keyCode == 13)
               callSearch();//查询函数
       });

       /**
        * 调用后台搜索并展示结果
        */
       function callSearch() {
           search(1);
           $('#page>li[name=prev]').data('value',0);
           $('#page>li[name=next]').data('value',1);
       }

       /**
        * 切换到上一页下一页
        */
       function switchPage() {
           var $li = $(this).parent();

           var page = $li.data('value');
           search(page);
       }

       function search(page) {
           var keyword = $('#search').find('input[name=keyword]').val().trim();
           var index = layer.load(1, {
               shade: [0.1,'#fff']
           });
           try{
               util.requestData('/netsource/search',{keyword:keyword,page:page},function (result) {
                   renderTable(result);
                   layer.close(index);
               });
           }catch (e) {
               layer.close(index);
           }
       }
       

   }

   return netsource.init();
});