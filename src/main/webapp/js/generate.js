/**
 * 各种字符生成
 */
define(['util'],function(util){
	var generate = {
			ALLCHAR:'0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ',
			LETTERCHAR:'abcdefghijkllmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ',
			NUMBERCHAR:'0123456789',
			FIRST_NAME:'赵钱孙李周吴郑王冯陈褚卫蒋沈韩杨朱秦尤许何吕施张孔曹严华金魏陶姜戚谢邹喻柏水窦章云苏潘葛奚范彭郎鲁韦昌马苗凤花方俞任袁柳酆鲍史唐费廉岑薛雷贺倪汤滕殷罗毕郝邬安常乐于时傅皮卞齐康伍余元卜顾孟平黄和穆萧尹姚邵湛汪祁毛禹狄米贝明臧计伏成戴谈宋茅庞熊纪舒屈项祝董梁杜阮蓝闵席季麻强贾路娄危江童颜郭梅盛林刁钟徐邱骆高夏蔡田樊胡凌霍虞万支柯昝管卢莫经房裘缪干解应宗丁宣贲邓郁单杭洪包诸左石崔吉钮龚程嵇邢滑裴陆荣翁荀羊於惠甄麴家封芮羿储靳汲邴糜松井段富巫乌焦巴弓牧隗山谷车侯宓蓬全郗班仰秋仲伊宫宁仇栾暴甘钭厉戎祖武符刘景詹束龙叶幸司韶郜黎蓟薄印宿白怀蒲邰从鄂索咸籍赖卓蔺屠蒙池乔阴郁胥能苍双闻莘党翟谭贡劳逄姬申扶堵冉宰郦雍舄璩桑桂濮牛寿通边扈燕冀郏浦尚农温别庄晏柴瞿阎充慕连茹习宦艾鱼容向古易慎戈廖庾终暨居衡步都耿满弘匡国文寇广禄阙东殴殳沃利蔚越夔隆师巩厍聂晁勾敖融冷訾辛阚那简饶空曾毋沙乜养鞠须丰巢关蒯相查後荆红游竺权逯盖益桓公晋楚闫法汝鄢涂钦仉督岳帅缑亢况后有琴商牟佘佴伯赏墨哈谯笪年爱阳佟',
			GIRL:'秀娟英华慧巧美娜静淑惠珠翠雅芝玉萍红娥玲芬芳燕彩春菊兰凤洁梅琳素云莲真环雪荣爱妹霞香月莺媛艳瑞凡佳嘉琼勤珍贞莉桂娣叶璧璐娅琦晶妍茜秋珊莎锦黛青倩婷姣婉娴瑾颖露瑶怡婵雁蓓纨仪荷丹蓉眉君琴蕊薇菁梦岚苑婕馨瑗琰韵融园艺咏卿聪澜纯毓悦昭冰爽琬茗羽希宁欣飘育滢馥筠柔竹霭凝晓欢霄枫芸菲寒伊亚宜可姬舒影荔枝思丽',
			BOY:'伟刚勇毅俊峰强军平保东文辉力明永健鸿世广万志义兴良海山仁波宁贵福生龙元全国胜学祥才发武新利清飞彬富顺信子杰涛昌成康星光天达安岩中茂进林有坚和彪博诚先敬震振壮会思群豪心邦承乐绍功松善厚庆磊民友裕河哲江超浩亮政谦亨奇固之轮翰朗伯宏言若鸣朋斌梁栋维启克伦翔旭鹏泽晨辰士以建家致树炎德行时泰盛雄琛钧冠策腾楠榕风航弘正日',
			EMAIL_SUFFIX:'@gmail.com,@yahoo.com,@msn.com,@hotmail.com,@aol.com,@ask.com,@live.com,@qq.com,@0355.net,@163.com,@163.net,@263.net,@3721.net,@yeah.net,@googlemail.com,@126.com,@sina.com,@sohu.com,@yahoo.com.cn'.split(',')
	};
	
	/**
	 * 随机数生成
	 * @param limit 限制最大大小
	 * 生成包含 0 到 limit 的任何数
	 */
  function random(limit){
    return Math.round(Math.random() * limit);
  }
  
  /**
   * 由日期字符串形式生成时间戳
   */
  function unFormatTime(time, format){
    format = format || 'yyyyMMdd';
    time = time || formatTime(new Date().getTime(),'yyyyMMdd');
    format = format.replace(/yyyy|MM|dd/g,function(a){
      switch(a){
      case 'yyyy':
        return '(\\d{4})';
      case 'MM':
        return '(\\d{2})';
      case 'dd':
        return '(\\d{2})';
      }
    });
    var regex = new RegExp(format,'g');
    var formatTimeStr = time.replace(regex,'$1/$2/$3');
    return new Date(formatTimeStr).getTime();
  } 
	
	/**
	 * 随机生成一个数字
	 * @param limit 限制最大大小
	 * @param head 是否包含 0
	 * @param foot 是否包含最大值
	 */
	generate.num = function(limit,head,foot){
		if(head === undefined){head=true;}
		if(foot === undefined){foot=true;}
		
		//根据是否需要包含头尾返回结果
		if(head && foot){return random(limit);}
    if(head){return random(limit - 1);}
    if(foot){return random(limit -1) + 1;}
	}
	
	/**
	 * 根据源字符串,生成指定长度的字符串
	 * @param length 长度
	 * @param src 源字符串,默认取所有字符
	 */
	generate.words = function(length,src){
		length = length || 0;
    src = src || this.ALLCHAR;
    
    var res = '';
    while(length-- != 0){
      res += src.charAt(random(src.length - 1));
    }
    return res;
	}
	
	/**
	 * 生成指定长度的中文
	 * @param length
	 */
	generate.chinese = function(length){
		length = length || 0;
    var res = ''
    while(length-- != 0){
      eval( "var word=" +  '"\\u' + (this.num(20901,false,true) + 19968).toString(16)+'"');
      res += word;
    }
    return res;
	}
	
	/**
	 * 生成指定长度的字母 
	 * @param length
	 * @returns
	 */
	generate.letters = function(length){
		return this.words(length, this.LETTERCHAR);
	}
	
	/**
	 * 生成指定长度的字符串
	 * @param length
	 * @returns
	 */
	generate.strings = function(length){
		return this.words(length, this.ALLCHAR);
	}
	
	/**
	 * 生成指定长度的数字,前面可能是 0 
	 * @param length
	 * @returns
	 */
	generate.nums = function(length){
		return this.words(length, this.NUMBERCHAR);
	}
	
	/**
	 * 生成指定长度的数字,前面没有 0 
	 * @param length
	 */
	generate.numsNoZero = function(length){
		var beginNumStr = this.NUMBERCHAR.substring(1);
    var beginNum = beginNumStr.charAt(random(beginNumStr.length - 1));
    var afterNum = this.nums(length - 1);
    return beginNum + afterNum;
	}
	
	/**
	 * 生成用户名
	 * @param sex true:男,false:女
	 */
	generate.username = function(sex){
		if(sex === undefined){
			sex = (random(100) % 2 == 0 );
		}
		var secondNameLength = this.num(2, false, true);
		var firstName = this.words(1, this.FIRST_NAME);
		var srcChars = sex ? this.BOY : this.GIRL;
		var secondName = this.words(secondNameLength,srcChars );
		
		return firstName+secondName;
	}
	
	/**
	 * 随机生成一个时间戳
	 * @param begin 开始时间戳
	 * @param end 结束时间戳
	 */
	generate.timestamp = function(begin,end){
		begin = begin || new Date(0).getTime();
		end = end || new Date().getTime();
		
		var timeLength = end - begin;
		var timestamp = begin + random(timeLength);
		
		return timestamp;
	}
	
	/**
	 * 生成随机一个日期
	 * @param pattern 日期格式
	 * @param begin 开始日期
	 * @param end 结束日期
	 */
	generate.date = function(pattern,begin,end){
		pattern = pattern || 'yyyy-MM-dd';
		begin = begin || util.FormatUtil.dateFormat(new Date(0).getTime(),pattern);
		end = end || util.FormatUtil.dateFormat(new Date().getTime(),pattern);
		
		var timestamp = this.timestamp(unFormatTime(begin,pattern),unFormatTime(end,pattern));
		return util.FormatUtil.dateFormat(timestamp,pattern);
	}
	
	/**
	 * 随机生成一个时间字符串
	 * @param 时间格式  HH:mm:ss
	 */
	generate.time = function(pattern){
		//未实现 
	}
	
	/**
	 * 随机生成一个邮件地址
	 * @param suffix
	 */
	generate.email = function(length,suffix){
		length = length || this.num(50, false, true);
		suffix  = suffix || this.EMAIL_SUFFIX[this.num(this.EMAIL_SUFFIX.length, true, false)];
		var username = this.words(length);
		return username+suffix;
	}

	/*
	 * 获取身份证校验码
	 */
	function getVerify(id17){
	  var power = [7,9,10,5,8,4,2,1,6,3,7,9,10,5,8,4,2];
	  for (i = 0,sum=0; i < id17.length; i++) {
		sum += id17[i] * power[i];
	  }
	  var t = sum % 11;
	  var r = (12 - t) % 11;
	  return r == 10?'x':r;
	}
	
	/**
	 * 随机生成身份证号
	 * @param area 区域编码
	 * @param yyyyMMdd 年月日
	 * @param sex true 男,false 女
	 */
	generate.idcard = function(area, yyyyMMdd, sex) {
		area = area || '430124';
		yyyyMMdd = yyyyMMdd || this.date('yyyyMMdd');
		if (sex === undefined) {
			sex = (random(100) % 2 == 0);
		}
		//根据男/女 随机生成3 位数字
		var sno = this.nums(3);
		if(sex){
			while(sno % 2 == 0){
				sno = this.nums(3);
			}
		}else{
			while(sno % 2 != 0){
				sno = this.nums(3);
			}
		}
		//获得 17 位身份证号码
		var id17 = area + yyyyMMdd + sno;
    	return id17 + getVerify(id17);
	}

	generate.verifyCode = getVerify;
	
	return generate;
});