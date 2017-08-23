//################################################################
//#### Autoload
//################################################################
// 
// ACC.sample={
// 	_autoload: [
// 		"samplefunction",
// 		["somefunction", "some expression to test"]
// 		["somefunction","some expression to test","elsefunction"]
// 	],

// 	samplefunction:function(){
// 		//... do some suff here, executed every time ...
// 	},

// 	somefunction:function(){
// 		//... do some suff here. if expression match ...
// 	},

// 	elsefunction:function(){
// 		//... do some suff here. if expression NOT match ...
// 	}

// }

//  // sample expression: $(".js-storefinder-map").length != 0


function _autoload(){
	$.each(ACC,function(section,obj){
		if($.isArray(obj._autoload)){
			$.each(obj._autoload,function(key,value){
				if($.isArray(value)){
					if(value[1]){
						ACC[section][value[0]]();
					}else{
						if(value[2]){
							ACC[section][value[2]]()
						}
					}
				}else{
					ACC[section][value]();
				}
			})
		}
	})
}
//UF-439
$(document).ready(function(){
	if($("#pageType").val() == 'homepage'){
		
		$(window).on("load",function(){
			$(function(){
				_autoload();
			});
		});
	}else{
		$(function(){
			_autoload();
		});
	}
});

