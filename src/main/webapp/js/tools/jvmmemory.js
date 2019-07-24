define(['util','ionRangeSlider','icheck'],function (util) {
   var jvmmemory = {};
   
   jvmmemory.init = function () {
       bindEvents();
       $('#heapFreeRatio').ionRangeSlider( {min:0,max:100,from:40,to:70,type:"double",postfix:" %",prettify:!1,hasGrid:!0});
       $('#newRatio').ionRangeSlider( {min:0,max:100,from:11,type:"single",step:1,postfix:" %",prettify:!1,hasGrid:!0});
       $('#cmdStartThreshold').ionRangeSlider( {min:0,max:100,from:92,type:"single",step:1,postfix:" %",prettify:!1,hasGrid:!0});
       $('#survivorRatio').ionRangeSlider( {min:0,max:100,from:10,type:"single",step:1,postfix:" %",prettify:!1,hasGrid:!0});
       $('input[type=radio],input[type=checkbox]').iCheck({
           checkboxClass: 'icheckbox_square-green',
           radioClass: 'iradio_square-green'
       });

       return this;
   }
   
   function bindEvents() {
       
   }
   return jvmmemory.init();
});