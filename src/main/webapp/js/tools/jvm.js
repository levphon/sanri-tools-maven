define(['util','echarts'],function(util,echarts){
	var chartInfo = {
		optionsDefault : {
			tooltip : {
				trigger : 'axis',
				axisPointer : {
					type : 'cross',
					label : {
						backgroundColor : '#6a7985'
					}
				}
			},
			legend : {
				data : [ 'max', 'init', 'committed', 'used' ]
			},
			toolbox : {
				feature : {
					saveAsImage : {}
				}
			},
			grid : {
				left : '3%',
				right : '4%',
				bottom : '3%',
				containLabel : true
			},
			xAxis : [ {
				type : 'time',
				boundaryGap : false
			} ],
			yAxis : [ {
				type : 'value'
			} ]
		},
		maxData:5
	};
	var jconsole = {timeInterval : 2 * 1000};
	
	/**
	 * 需要处理的 chart 信息
	 */
	chartInfo.dataInit = function(){
		chartInfo.dataInit.items = [];
		chartInfo.dataInit.data = {};				//存放每个表的数据
		chartInfo.charts = {};
		$('.chart-item').each(function(){
			var chartName = $(this).attr('name')
			chartInfo.dataInit.items.push(chartName);
			//图表初始化
			chartInfo.charts[chartName] = echarts.init(this);
		});
		//处理每一个图表的初始化数据
		for(var i=0;i<chartInfo.dataInit.items.length;i++){
			var item = chartInfo.dataInit.items[i];
			chartInfo.dataInit.data[item] = {};
			for(var j=0;j<chartInfo.optionsDefault.legend.data.length;j++){
				var legendItem = chartInfo.optionsDefault.legend.data[j];
				chartInfo.dataInit.data[item][legendItem] = [];
			}
		}
	}
	
	/**
	 * 界面初始化
	 */
	jconsole.init = function(){
		chartInfo.dataInit();				//数据初始化
		console.log(chartInfo);
		
		this.loadMemUsage();
		return this;
	}
	
	/**
	 * 从后加加载实时数据,并加载到表格中
	 */
	jconsole.loadMemUsage = function(){
		util.requestData('/jvmargs/memoryUseQuery',function(memoryUseMap){
			jconsole.loadMemUsageCallback(memoryUseMap);
			window.setTimeout(jconsole.loadMemUsage,jconsole.timeInterval);		//继续加载数据
		});
	}
	
	/**
	 * 渲染视图
	 * @param memoryUseMap
	 */
	jconsole.loadMemUsageCallback = function(memoryUseMap){
		var now = new Date(),
			nowTime = now.getTime();
		//遍历每一张图进行处理
		for(var key in chartInfo.charts){
			var chart = chartInfo.charts[key];
			var chartData = chartInfo.dataInit.data[key];
			var memoryUse = memoryUseMap[key];
			//处理每一项数据
			for(var i=0;i<chartInfo.optionsDefault.legend.data.length;i++){
				var legendItem = chartInfo.optionsDefault.legend.data[i];
				if(chartData[legendItem].length > chartInfo.maxData){
					chartData[legendItem].shift();
				}
				chartData[legendItem].push([nowTime,memoryUse[legendItem]]);
			}
			//加载图表
			chart.setOption($.extend({},chartInfo.optionsDefault,{
				title:{
					text:key
				},
				series:[{
					name:'max',
					type: 'line',
					data:chartData.max,
//					areaStyle: {normal: {}}
//					label: {
//            normal: {
//                show: true,
//                position: 'top'
//            }
//					}
				},{
					name:'init',
					type: 'line',
					data:chartData.init,
//					label: {
//            normal: {
//                show: true,
//                position: 'top'
//            }
//					}
//					areaStyle: {normal: {}}
					
				},{
					name:'committed',
					type: 'line',
					data:chartData.committed,
					label: {
            normal: {
                show: true,
                position: 'top',
                formatter:function(params){
                	var usedMem = params.data[1];
                	return formatBKMG(usedMem, 'h');
                }
            }
					}
//					areaStyle: {normal: {}}
				},{
					name:'used',
					type: 'line',
					data:chartData.used,
					label: {
            normal: {
                show: true,
                position: 'bottom',
                formatter:function(params){
                	var usedMem = params.data[1];
                	return formatBKMG(usedMem, 'h');
                }
            }
					}
//					areaStyle: {normal: {}}
				}]
			}));
		}
	}
	
	
	return jconsole.init();
	/**
	 * 格式化内存使用量 value 需给字节值
	 * 
	 * @returns
	 */
	function formatBKMG(value,pattern){
		if(!value){
			return '0b';
		}
		if('b,B,k,K,m,M,g,G,t,T,h,H'.indexOf(pattern) == -1){
			return 'error';
		}
		var floatVal = parseFloat(value);
		switch(pattern){
		case 'k':
		case 'K':
			return (floatVal / 1024).toFixed(2) + ' k';
		case 'm':
		case 'M':
			return (floatVal / 1048576).toFixed(2) + ' m';
		case 'g':
		case 'G':
			return (floatVal / 1073741824).toFixed(2) + ' g';
		case 't':
		case 'T':
			return (floatVal / 1099511627776).toFixed(2)+' t';
		case 'h':
		case 'H':
			if(floatVal < 1024){
				return formatBKMG(value,'b');
			}else if(floatVal >=1024 && floatVal < 1048576){
				return formatBKMG(value, 'k');
			}else if(floatVal >=1048576 && floatVal < 1073741824){
				return formatBKMG(value, 'm');
			}else if(floatVal >=1073741824 && floatVal < 1099511627776){
				return formatBKMG(value, 'g');
			}else if(floatVal >=1099511627776){
				return formatBKMG(value, 't');
			}
			break;
		}
		return  floatVal.toFixed(2)+' b';
	}
});