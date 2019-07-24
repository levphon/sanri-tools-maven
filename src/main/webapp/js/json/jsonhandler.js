define(['util','json/log'],function(util,hlog){
	var jsonhandler = {
			standardJson:{},
      original:{},
      mode:'likeJson',//现在的处理模式
	};
	
  /*
   * json 构建 
   */
  jsonhandler.buildJson = function(){
     if(!this.original.jsonString){
       hlog.error('没有传入 json 字符串');
     }
     try{
       if(this.original.type == 'noStandard'){
//      	 logger.debug(this.mode,'现在的处理模式');
         hlog.info('保存原始类 json 串')
         this.original.pre = this.original.jsonString;
         hlog.info('处理成标准 json 串');
         switch(this.mode){
         case 'mapJson':
        	 //TODO 暂时先把 = 替换成 : 后继续
        	 this.original.jsonString = this.original.jsonString.replace(/=/g,':');
         case 'likeJson':
        	 //不标准的先转换成标准的 
           //空数据暂时转换成 $nbsp$ ,后面再转回来 解决空数据 bug at 2016/11/20 by sanri
           this.original.process = this.original.jsonString.replace(/(\'\')|(\"\")/g,'"$nbsp$"');
           this.original.process = this.original.process.replace(/[\r\n\s\'\"]/g,''); 
           //TODO 当数据中有 [] {} : , 等特殊字符时,无法处理
           this.original.jsonString = this.original.process.replace(/([^:\[\]\{\},]+)/g,'"$1"');
           //去掉之前专门为 "" 数据加的标记
           this.original.jsonString = this.original.jsonString.replace('$nbsp$','');
        	 break;
         case 'keyvalueJson':
        	 var jsonObj = util.serialize2Json(this.original.jsonString);
        	 this.original.jsonString = JSON.stringify(jsonObj);
        	 break;
         }
        
       }
       hlog.info('构建 json 对象');
       this.standardJson = JSON.parse(this.original.jsonString);
       this.original.isHandle = true;
       hlog.success('构建成功');
     }catch(e){
//       logger.error(e);
       hlog.error(e);
       //可能是没有加 {} ,两边加上 {} 再试一次
       hlog.warning('构建失败,将在两边加上 {} 再试一次');
       try{
         this.original.jsonString = '{'+this.original.jsonString+'}';
         this.standardJson = JSON.parse(this.original.jsonString);
         this.original.isHandle = true;
         hlog.success('构建成功');
       }catch(e){
//         logger.error(e);
      	 console.error(e);
         hlog.error(e);
       }
     }
  }
  
  return $.extend(jsonhandler,{
  	 setNoStandardJson:function(_nostandardJson){
       this.original.jsonString = _nostandardJson;
       this.original.isHandle = false;
       this.original.type='noStandard';
     },
     setStandardJson:function(_standardJson){
       this.original.jsonString = _standardJson;
       this.original.isHandle = false;
       this.original.type='standard';
     },
     /**
      * json 对象和字符串之间的转换方法
      * @param json_ 如果提供 json 对象,则转成字符串;如果提供字符串,则转换成 json 对象
      * @returns
      */
     convert:function(json_){
       if(typeof json_ == 'object'){
         return JSON.stringify(json_);
       }
       return JSON.parse(json_);
     }
  });
});