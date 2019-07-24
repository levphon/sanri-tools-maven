define(['util','zclip'],function (util) {
   var timestamp = {};

   timestamp.init = function () {
       //每一秒,修改前面三项数据
       setInterval(updateData,1000);

       //设置属性默认值
       var nowDate = new Date();
       var year = nowDate.getFullYear();
       var month = nowDate.getMonth() + 1;
       var day = nowDate.getDate();
       var hour = nowDate.getHours();
       var minute = nowDate.getMinutes();
       var second = nowDate.getSeconds();

       var mirro = {year:year,month:month,day:day,hour:hour,minute:minute,second:second};

       $('#toUnixTime').find('input').each(function (item) {
          var name = $(this).attr('name');
          $(this).val(mirro[name]);
       });

       $('#toBeiJingTime').find('input[name=timestamp]').val(nowDate.getTime());

       //事件绑定
       $('#toUnixTime>button').bind('click',function () {
           $('#toUnixTime').find('input:not([name=millis],[name=timestamp])').each(function (item) {
               var name = $(this).attr('name');
               mirro[name] = $(this).val();
           });
           try{
                var setDate = new Date(mirro.year+'/'+(mirro.month )+'/'+mirro.day+' '+mirro.hour+':'+mirro.minute+':'+mirro.second);
               $('#toUnixTime').find('input[name=millis]').val(setDate.getTime());
               $('#toUnixTime').find('input[name=timestamp]').val(setDate.getTime() / 1000);
           }catch (e){
               layer.msg(e);
           }
       });

       $('#toBeiJingTime>button').bind('click',function () {
           var timestamp = $('#toBeiJingTime').find('input[name=timestamp]').val();
           $('#toBeiJingTime').find('input[name=timeshow]').val(util.FormatUtil.dateFormat(parseInt(timestamp),'yyyy-MM-dd HH:mm:ss'));
       });

       $('#fastTime').find('.copy').zclip({
           path: "ZeroClipboard.swf",
           copy: function(){
              var name = $(this).attr('name');
              switch (name){
                  case 'current':
                      return new Date().getTime();
                  case 'current30':
                      return new Date().getTime()+30000;
                  case 'current60':
                      return new Date().getTime()+60000;
              }
              return new Date().getTime();
           },
           beforeCopy:function(){/* 按住鼠标时的操作 */
           },
           afterCopy:function(){/* 复制成功后的操作 */
               layer.msg('复制成功');
           }
       });

       return this;
   };

   function updateData() {
       var dateNow = new Date();
       var $currentGroup = $('#fastTime>.form-group.current');
       var $current30Group = $('#fastTime>.form-group.current30');
       var $current60Group = $('#fastTime>.form-group.current60');

       $currentGroup.children('input').val(dateNow.getTime());
       $current30Group.children('input').val(dateNow.getTime() + 30000);
       $current60Group.children('input').val(dateNow.getTime() + 60000);

       var format = 'yyyy-MM-dd HH:mm:ss';
       $currentGroup.children('span.timeshow').text(util.FormatUtil.dateFormat(dateNow.getTime(),format));
       $current30Group.children('span.timeshow').text(util.FormatUtil.dateFormat(dateNow.getTime()+ 30000,format));
       $current60Group.children('span.timeshow').text(util.FormatUtil.dateFormat(dateNow.getTime()+ 60000,format));
   }

   return timestamp.init();
});