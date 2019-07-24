(function( factory ) {
	if ( typeof define === "function" && define.amd ) {
		define( ["jquery", "../plugins/validate/jquery.validate.min"], factory );
	} else {
		factory( jQuery );
	}
}(function( $ ) {

/*
 * Translated default messages for the jQuery validation plugin.
 * Locale: ZH (Chinese, 中文 (Zhōngwén), 汉语, 漢語)
 */
$.extend($.validator.messages, {
	required: "该栏必需填写",
	remote: "请修正此栏位",
	email: "请输入有效的电子邮件",
	url: "请输入有效的网址",
	date: "请输入有效的日期",
	dateISO: "请输入有效的日期 (YYYY-MM-DD)",
	number: "请输入正确的数字",
	digits: "只可输入数字",
	creditcard: "请输入有效的信用卡号码",
	equalTo: "你的输入不相同",
	extension: "请输入有效的后缀",
	maxlength: $.validator.format("最多 {0} 个字"),
	minlength: $.validator.format("最少 {0} 个字"),
	rangelength: $.validator.format("请输入长度为 {0} 至 {1} 之間的字串"),
	range: $.validator.format("请输入 {0} 至 {1} 之间的数值"),
	max: $.validator.format("请输入不大于 {0} 的数值"),
	min: $.validator.format("请输入不小于 {0} 的数值")
});

//邮政编码验证   
//$.validator.addMethod("isZipCode", function(value, element) {   
//    var tel = /^[0-9]{6}$/;
//    return this.optional(element) || (tel.test(value));
//}, "请正确填写您的邮政编码");

	$.validator.addMethod("idcard", function(value, element) {
		var idcard = /(^[0-9]{15}$)|(^[0-9]{17}([0-9]|[X,x])$)/;
		return idcard.test(value);
	},"请输入 15 位或 18 位的身份证号码");

	$.validator.addMethod("telphone", function(value, element) {
		var telphone = /^1\d{10}$/g;
		return telphone.test(value);
	},"请输入正确的电话号码");
	
	$.validator.addMethod("platenumber", function(value, element) {
		var platenum = /^[\u4E00-\u9FA5\uF900-\uFA2D0-9a-zA-Z]{7,}$/;
		return platenum.test(value);
	},"请输入正确的车牌号码");
	
	$.validator.addMethod("standno", function(value, element) {
		var standno = /^[A-Za-z0-9]{17}$/;
		return standno.test(value);
	},"输入正确的车架号, 17位数字或者字母");
	
}));