define(['util','generate','icheck'], function (util,generate) {
    var idcard = {};

    idcard.init = function () {
        var $place = $('#infoload>.form-group.place');
        var $province = $place.find('select[name=province]');
        var $city = $place.find('select[name=city]');
        var $area = $place.find('select[name=area]');

        $.getJSON('idcard.json', function (data) {
            idcard.data = data;

            for (var key in data) {
                $province.append('<option value="' + data[key].key + '">' + data[key].name + '</option>');
            }

        });

        $province.bind('change', function (e) {
            var province = $(this).val();
            $city.empty();
            if (!province) {
                return;
            }

            var citys = idcard.data[province].child;
            for (var key in citys) {
                $city.append('<option value="' + citys[key].key + '">' + citys[key].name + '</option>');
            }

            //当修改省份的时候,城市发生变化,需要选择第一个,然后改变区域
            $city.change();
        });

        $city.bind('change', function () {
            var province = $province.val();
            $area.empty();
            var city = $(this).val();
            if (!city) {
                return;
            }

            var areas = idcard.data[province].child[city].child;
            for (var key in areas) {
                $area.append('<option value="' + areas[key].key + '">' + areas[key].name + '</option>');
            }
        });

        setTimeout(function () {
            $province.change();
            $city.change();
        }, 200);

        // 加载出生日期
        var $birthday = $('#infoload>.form-group.birthday');
        var $year = $birthday.find('select[name=year]');
        var $month = $birthday.find('select[name=month]');
        var $day = $birthday.find('select[name=day]');

        var currDate = new Date(), currYear = currDate.getFullYear(), currMonth = currDate.getMonth() + 1,
            currDay = currDate.getDate();
        for (var i = currYear; i > currYear - 50; i--) {
            $year.append('<option value="' + i + '">' + i + '</option>');
        }
        for (var i = 1; i <= 12; i++) {
            if((i+'').length == 1){
                $month.append('<option value="0' + i + '">0' + i + '</option>');
            }else{
                $month.append('<option value="' + i + '">' + i + '</option>');
            }

        }

        $month.bind('change', function () {
            var month = $(this).val();
            $day.empty();
            var year = $year.val();
            //getDate() 方法可返回月份的某一天。取值范围是1~31,如果是0的话，就返回最后一天。这样就能取得当月的天数了
            var date = new Date(year, month, 0);
            var dayCount = date.getDate();

            for (var i = 1; i <= dayCount; i++) {
                if((i+'').length == 1){
                    $day.append('<option value="0' + i + '">0' + i + '</option>');
                }else{
                    $day.append('<option value="' + i + '">' + i + '</option>');
                }
            }

        });

        $year.val(1993);
        $month.val(currMonth);
        $month.change();
        $day.val(currDay);

        // 美化复选框
        $('#infoload').find('input:radio').iCheck({
            radioClass: 'iradio_square-green'
        });

        $('#generate').bind('click', function () {
            var selected = {};

            //年月日和省市区
            $('#infoload').find('select').each(function () {
                var key = $(this).attr('name');
                selected[key] = $(this).val();
            });
            //性别
            var gender = $('#infoload').find('input[name=gender]:radio:checked').val();
            var genCount = $('#infoload').find('input[name=size]').val().trim()

            var allGenerate = [];
            for (var i=0;i<genCount;i++){
                var current = generate.idcard(selected.area,selected.year+''+selected.month+selected.day,!!parseInt(gender));
                allGenerate.push(current);
            }

            $('#genresult').val(allGenerate.join('\n'));
        });

        $('#cleanall').bind('click',function () {
             $('#genresult').val('');
        });

        //验证身份证
        $('#checkIdcard').find('button').bind('click',function () {
            var input = $('#checkIdcard').children('input').val().trim();
            if(!input || input.length != 18){
                layer.msg('身份证位数不正确');
                return ;
            }
            var input17 = input.substring(0,17);
            var last = input.substring(17,18);

            if(!/^\d{17}$/.test(input17)){
                layer.msg('前 17 位必须是数字');
                return ;
            }

            var realLast = generate.verifyCode(input17);
            if(last.toLocaleLowerCase() != realLast){
                layer.msg('检验码不匹配');
                return ;
            }

            var province = input.substring(0,2);
            var city = input.substring(0,4);
            var area = input.substring(0,6);
            var year = input.substring(6,10);
            var month = input.substring(10,12);
            var day = input.substring(12,14);
            var seriaCode = input.substring(14,17);
            var gender = seriaCode % 2 == 0;

            $province.val(province).change();
            $city.val(city).change();
            $area.val(area);
            $year.val(year);
            $month.val(month);
            $day.val(day);
            if(gender){
                $('input[name=gender][value="0"]').iCheck('check');
            }else {
                $('input[name=gender][value="1"]').iCheck('check');
            }

        });

        return this;
    };

    return idcard.init();
});