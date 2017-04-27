var stwHtml = "";
var stwHtml1 ="";
var product="";
$(document).ready(function(){
	stw.renderTabsWidget();
	//stWidget('#stw_widget','');
	//stWidget('#stw_widget');
	//stwidgetBlp('#stw_widget_Blp');
	
});

var stw = {
	renderFlatWidget: function(divElement){
		  var category = window.location.pathname.split("/")[1];
		  console.log("category"+category);
		  var json = stwService.call(category);
		  var header = stwRender.header(json);
		  console.log("widgetTab"+header);
		  var html = stwRender.flatWidget(json);
		  console.log("html"+html);
		  var stw_block= "<div class='best_seller stw-list'>" +header+"</div>"+carousel;
		  $("#stw_widget_Blp").append(stw_block);
	
	},
	renderTabsWidget: function(divElement){
		//1. 
		var json = stwService.call();
		//2.
		var header = stwRender.header(json);
		//3.
		var tabs = stwRender.tabs(json);
		//4.
		var carousel = stwRender.carousel(json);
		var stw_block = "<div class='best_seller stw-list'>" +header + tabs +"</div>" + carousel;
		$("#stw_widget").append(stw_block);
		
	},
}
var callbackVar;
var stwService = {
	
		call: function(category){
			$.ajax({
	  		  url: '/getStwrecomendations',
	  		  dataType: "json",
	  		  type:"GET",
	  		  async: !1,
	  		  data:{pageType:'Home',widgetType:'STW',siteType:'Marketplace',sendOnlyListingId:'false',category:category},
	  		  success: function(callback){
	  			  alert("callback" + callback);
	  			callbackVar = callback;
	  			  
	  		  },
	  	//	  console.log('service'+data);
	  		  fail:function(){
	  			  console.log('STW failed to load --'+error);
	  		  },
	  		complete:function(){
	  		  $(divElement).html(stw_block);
				  $(".stw-widget-owl").owlCarousel({
						items:5,
						loop: true,
						nav:true,
						dots:false,
						navText:[],
						lazyLoad: true,
						responsive: {
			                0: {
			                    items: 1,
			    				stagePadding: 50
			                },
			                480: {
			                    items: 2,
			    				stagePadding: 75
			                },
			                700: {
			                    items: 3
			                },
			                1000: {
			                    items: 5
			                }
			            }
					});
				 // $('#stw_widget .loaderDiv').remove();
				  $(".best_seller .best_seller_section:first-child").after("<div class='Menu'><div class='mobile selectmenu'></div><ul></ul></div>");
  				$(".best_seller .best_seller_section").each(function(){
  				var li_text= $(this).children("h1").text();
  				if(li_text != ""){
  				$(".best_seller .Menu ul").append("<li>"+li_text+"</li>");
  				}
  				});
  				$(".best_seller .Menu ul li:nth-child(1)").addClass("active");
  				$(".best_seller .best_seller_section:nth-of-type(5)").addClass("show_clplist");
  				$(".best_seller .Menu .mobile.selectmenu").text($(".best_seller .Menu ul li.active").text());
  				$(".best_seller .Menu ul li").off("click").on("click", function(){
  					$(".best_seller .Menu ul li").removeClass("active");
  					$(this).addClass("active");
  					var active_text = $(this).text();
  				$(".best_seller .best_seller_section").each(function(){
  				var li_text= $(this).children("h1").text();
  				if(li_text == active_text){
  				$(".best_seller .best_seller_section").removeClass("show_clplist").addClass("hide_clplist");
  				$(this).removeClass("hide_clplist").addClass("show_clplist");
  				}
  				});
  				$(".best_seller .Menu .mobile.selectmenu").text(active_text);
  				$('.best_seller .Menu ul').slideUp();
  				});
  				$(".best_seller .Menu .selectmenu").off("click").on("click", function() {
  		            $(this).next().slideToggle();
  		        });
	  		}
	  		});
			return callbackVar;
		}	
}



 
function getVisiterIp(ip){
	var lastIpPart = ip.split(".")[3];
	if(typeof(lastIpPart) != 'undefined'){
		if(lastIpPart%2 == 0){
			delete productWidget[4];
		}else{
			return false;
		}
	}
}

$(document).on('click',".best_seller .Menu ul li",function(){
	var category = $(this).text() == 'ALL' ? '':$(this).text();
	$.ajax({
		  url: '/getStwrecomendations',
		  dataType: "json",
		  type:"GET",
		  data:{pageType:'Home',widgetType:'STW',siteType:'Marketplace',sendOnlyListingId:'false',category:category},
		  beforeSend:function(){
			  var staticHost = $('#staticHost').val();
			  $(".stw-widget-owl").css(
                      "margin-bottom", "120px");
                  $("#stw_widget").append(
                      "<div class='loaderDiv' style='background: transparent;z-index: 100000;position: absolute; top: 135px;left: 50%;margin-left: -50px;display:inline-block;width:100px;height:100px;'><img src='"+staticHost+"/_ui/desktop/theme-blue/images/loading.gif' style='width:100%;'/></div>"
                  );
		  },
		  success: formStwWidgetProducts,
		  fail:function(error){
			  console.log('STW failed to load --'+error);
		  },
		  complete:function(){
			  $(".stw-widget-owl").owlCarousel({
					items:5,
					loop: true,
					nav:true,
					dots:false,
					navText:[],
					lazyLoad: true,
					responsive: {
		                0: {
		                    items: 1,
		    				stagePadding: 50
		                },
		                480: {
		                    items: 2,
		    				stagePadding: 75
		                },
		                700: {
		                    items: 3
		                },
		                1000: {
		                    items: 5
		                }
		            }
				});
			  $('#stw_widget .loaderDiv').remove();
		  }
		});
}); 


var stwRender = {
		header:function(STWJObject){
		var stwWidgetHeading = "";
		   //  stwWidgetHeading+= '<div class="best_seller stw-list">'
			stwWidgetHeading+= '<div class="best_seller_section hide_clplist">';
		     stwWidgetHeading+= '<div class="content">'+STWJObject.STWHeading+'</div>';
		     stwWidgetHeading+='</div>';
		     console.log("stwWidgetHeading"+stwWidgetHeading);
		     return stwWidgetHeading;
		},
		tabs: function(STWJObject){
			var tabsFormationHtml ="";
			tabsFormationHtml+= '<div class="best_seller_section hide_clplist">';
			tabsFormationHtml+= '<h1>ALL</h1>';
			tabsFormationHtml+= '</div>';
			 var allCatCodeNameArray = STWJObject.STWCategories.split(",");
			 
			 $.each(allCatCodeNameArray,function(index,value){
				 	var categoryCodeNameArray = value.split("=");
				 	tabsFormationHtml+= '<div class="best_seller_section hide_clplist">';
				 	tabsFormationHtml+='<h1>'+categoryCodeNameArray[1]+'</h1>';
				 	tabsFormationHtml+= '</div>';
			 }); 
			 return tabsFormationHtml;
		},
		carousel: function(STWJObject){
			var stwWidgetProducts = "";
			stwWidgetProducts+='<div class="carousel-component">';
			stwWidgetProducts +='<div class="carousel js-owl-carousel js-owl-lazy-reference js-owl-carousel-reference stw-widget-owl">';
			    $.each(STWJObject.STWElements, function( index, value ){
			    	var productId=value.listingId;
			    	var product=productId.toUpperCase();
			    	if(null!=value.mrp && null!=value.mop){
			    	var	savingPriceCal=(value.mrp-value.mop);
					var savingPriceCalPer=(savingPriceCal/value.mrp)*100;
					var	savingsOnProduct=Math.round((savingPriceCalPer*100)/100);
					}
			    	stwWidgetProducts += '<div class="item slide">';
			    	stwWidgetProducts +='<div class="product-tile"><li onmouseover="showQuickview(this)" onmouseout="hideQuickView(this)" class="look slide product-tile stw_widget_list_elements productParentList" style="display: inline-block;position: relative;"><div class="image"><a href="'+value.productUrl+'" class="product-tile"> <img src="'+value.imageUrl+'"></a>';
			    	stwWidgetProducts += '<div onclick=popupwindow("'+product+'") class="IAQuickView" style="position: absolute; text-transform: uppercase;cursor: pointer; bottom: 0; z-index: -1; visibility: hidden; color: #00cbe9;display: block; width: 100%; text-align: center;background: #f8f9fb;background-color: rgba(248, 249, 251,0.77);-webkit-font-smoothing: antialiased;height: 70px;font-size:12px;"><span>Quick View</span></div></div>';
			    	stwWidgetProducts +=' <div class="short-info"><ul class="color-swatch"><li><span  style="background-color: '+value.availableColor+';border: 1px solid rgb(204, 211, 217);" title="'+value.availableColor+'"></span></li></ul>';
			    	stwWidgetProducts += '<div class="brand">'+value.productBrand+'</div>';
			    	stwWidgetProducts += ' <a href="'+value.productUrl+'" class="item"><h3 class="product-name">'+value.productName+'</h3>';
			    	stwWidgetProducts += '<div class="price"><span class="stw-mrp">&#8377;'+value.mrp+' </span><span class="stw-mop"> &#8377;'+value.mop+'</span>';
			    	stwWidgetProducts += '<p class="savings pdp-savings"> <span>(-'+savingsOnProduct+'%)</span></p>';
			    	stwWidgetProducts += '</div></a></div></li></div></div>'; 
			    });
			stwWidgetProducts+='</div></div>';
			return stwWidgetProducts;
		}
		
}
