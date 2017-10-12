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
	if($("#pageType").val() == 'homepage' || 
	   $("input[name=newBrandLandingPage]").length || 
	   $('#pageType').val() == "product" || 
	   $("input[name=productGrid]").length ||
	   $("input[name=searchPanel]").length ||
	   $("input[name=apparelCategoryLandingPage]").length ||
	   $("input[name=BrandLayoutPage]").length){
		
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

