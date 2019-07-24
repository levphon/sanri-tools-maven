/**
 * 依赖 layer 的对话框
 * 依赖后使用 new Dialog().set*().create();
 */
define(['util'],function(util){
	var Dialog = function(title){
		this.title = title;
	}
	
	//内部方法,用来获取按扭的索引值的
	function getBtnIndex(btnIndex,_this){
		var findBtnIndex = -1;
		//如果 index 没有设置,则取最大 index 
		if(!btnIndex){
			if(!_this.btnMaxIndex){		//最开始是没有最大按扭序号的,取 0 
				_this.btnMaxIndex = 0;
				findBtnIndex =  _this.btnMaxIndex;
			}else{
				findBtnIndex = ++_this.btnMaxIndex;
			}
		}else{
			if(_this.btnIndexs){
				//如果已经有 index 设置过,则不能再设置,不用抛出异常,把 index 往后加 1 ,直到不冲突为止
				while($.inArray(btnIndex,_this.btnIndexs) != -1){
					//有冲突 加到不冲突为止
					btnIndex ++;
				}
			}
			findBtnIndex = btnIndex;
		}
		
		//放入已经存在的按扭索引库
		if(_this.btnIndexs){
			_this.btnIndexs.push(findBtnIndex);
		}else{
			_this.btnIndexs = [];
		}
		return findBtnIndex;
	}
	
	//扩展 dialog 的原型方法
	$.extend(Dialog.prototype,{
		config:{
			shade: 0.8,
			shadeClose: false			//点击遮罩关闭
		},
		onOpen:function(callback){
			this.success = callback;
			return this;
		},
		onClose:function(callback){
			this.cancel = callback;
			return this;
		},
		/**
		 * 添加按扭
		 * 入参格式 Object {type:[yes/cancel/button],index:int,text:String,handler:function(){}}
		 * cancel 按扭有默认动作,关闭层
		 * index 可以不设置,自动往后排; 如果设置有可能造成 index 冲突,想排后面请尽量设置大点
		 */
		addBtn:function(options){
			if(!options.type){
				throw new Error('请先设置按扭类型[yes/cancel/button]');
			}
			if(!this.btnconfig){
				this.btnconfig = [];
			}
			options.btnIndex = getBtnIndex(options.btnIndex,this);
			this.btnconfig.push(options);
			return this;
		},
		/**
		 * 
		 * @param width String 数字或百分比
		 * @param height String 数字或百分比
		 */
		setWidthHeight:function(width,height){
			$.extend(this,{area: [width, height]});
			return this;
		},
		/**
		 * 设置内容
		 * @param content String/jquery 对象/Dom 对象
		 */
		setContent:function(content){
			this.type = 1;
			this.content = content;
			return this;
		},
		/**
		 * 以 url 的形式的加载层
		 * url 的优先级会高于 content 
		 */
		setUrl:function(url){
			
			return this;
		},
		/**
		 * 最终构建对话框
		 */
		build:function(){
			var options = $.extend({},this.config,this);		//使用覆盖默认配置
			//处理按扭选项
			if(options.btnconfig && options.btnconfig.length > 0){
				//先按照 index 位置排序
				options.btnconfig.sort(function(before,after){
					return before.btnIndex - after.btnIndex;
				});
				options.btn = [];
				for(var i=0;i<options.btnconfig.length;i++){
					var currBtnConfig = options.btnconfig[i];
					options.btn.push(currBtnConfig['text']);
					if(currBtnConfig.type == 'yes'){
						options.yes = currBtnConfig.handler;
					}else if(currBtnConfig.type == 'cancel'){
						options.cancel = currBtnConfig.handler;
					}else{
						//普通的按扭
						var btnKey = 'btn'+(i+1);
						options[btnKey] = currBtnConfig.handler;
					}
				}
			}
			//创建对话框,得到层位置
			this.index = layer.open(options);
			return this;
		}
	});
	
	return {
		create:function(title){
			return new Dialog(title);
		}
	};
});