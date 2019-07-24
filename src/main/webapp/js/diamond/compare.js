define(['util','textdiff'],function (util) {
    var compare = {};

    compare.init = function () {
        var parseUrl = util.parseUrl();
        compare.params = parseUrl.params;
        compare.group = parseUrl.params.group;
        compare.dataId = parseUrl.params.dataId;

         util.requestData('/diamond/content', {group: compare.group, dataId: compare.dataId}, function (content) {
             $('#dev').find('.content').html(content.replace(/\r/g,'</br>'));
         });
         util.requestData('/diamond/remoteConfigContent',{group:compare.group,dataId:compare.dataId,env:'test'},function (data) {
             $('#test').find('.content').html(data.replace(/\n/g,'</br>'));
         });
         util.requestData('/diamond/remoteConfigContent',{group:compare.group,dataId:compare.dataId,env:'release'},function (data) {
             $('#release').find('.content').html(data.replace(/\n/g,'</br>'));
         });

         setTimeout(function () {
             var dev = $('#dev').find('.content').text();
             var test = $('#test').find('.content').text();
             var release = $('#release').find('.content').text();

             $('#compareArea').find('.col-xs-4:first').prettyTextDiff({
                 originalContent:dev,
                 changedContent:test,
                 diffContainer:'.diff-dev-test'
             });
              $('#compareArea').find('.col-xs-4:eq(1)').prettyTextDiff({
                 originalContent:test,
                 changedContent:release,
                 diffContainer:'.diff-test-release'
             });
               $('#compareArea').find('.col-xs-4:eq(2)').prettyTextDiff({
                 originalContent:dev,
                 changedContent:release,
                 diffContainer:'.diff-dev-release'
             });
         },200)
    }

    return compare.init();
});