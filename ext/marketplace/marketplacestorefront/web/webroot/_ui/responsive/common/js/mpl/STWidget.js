var product="";

var stw = {
	renderFlatWidget: function(divElement){
		  var brand = window.location.pathname.split("/")[1];
		  var json = stwService.call('',brand);
		  var header = stwRender.blpheader(json);
		  var carousel = stwRender.carousel(json);
		  var stw_block_Blp= "<div class='best_seller stw-list'>" +header+"</div>" + carousel;
		  $("#stw_widget_Blp").html(stw_block_Blp);
		  stwRender.bindCarousel();
		 
	
	},
	renderTabsWidget: function(divElement){
		var category="";
		var json = stwService.call(category,'');
		var header = stwRender.header(json);
		var tabs = stwRender.tabs(json);
		var carousel = stwRender.carousel(json);
		var stw_block = "<div class='best_seller stw-list'>" +header + tabs+"</div>" + carousel;
		$("#stw_widget").html(stw_block);
		stwRender.bindCarousel();
	},
}
var callbackVar;
var stwService = {
	
		call: function(category,brand){
			$.ajax({
	  		  url: '/getStwrecomendations',
	  		  dataType: "json",
	  		  type:"GET",
	  		  async: !1,
	  		  data:{pageType:'Home',widgetType:'STW',siteType:'Marketplace',sendOnlyListingId:'false',category:category,brand:brand},
	  		  beforeSend:function(){
				  var staticHost = $('#staticHost').val();
				  $(".stw-widget-owl").css(
	                      "margin-bottom", "120px");
	                  $("#stw_widget").append(
	                      "<div class='loaderDiv' style='background: transparent;z-index: 100000;position: absolute; top: 135px;left: 50%;margin-left: -50px;display:inline-block;width:100px;height:100px;'><img src='"+staticHost+"/_ui/desktop/theme-blue/images/loading.gif' style='width:100%;'/></div>"
	                  );
			  },
	  		  success: function(callback){
	  			callbackVar = callback;
	  			  
	  		  },
	  	//	  console.log('service'+data);
	  	//	category:category,
	  		  fail:function(){
	  			  console.log('STW failed to load --'+error);
	  		  },
	  		complete:function(){
				 $('#stw_widget .loaderDiv').remove();
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
/*
$(document).on('click',".best_seller .Menu ul li",function(){
	var category = $(this).text() == 'ALL' ? '':$(this).text();
	$.ajax({
		  url: '/getStwrecomendations',
		  dataType: "json",
		  type:"GET",
		  data:{pageType:'Home',widgetType:'STW',siteType:'Marketplace',sendOnlyListingId:'false',category:category},
		 
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
*/

var stwRender = {
		header:function(STWJObject){
		var stwWidgetHeading = "";
			stwWidgetHeading+= '<div class="best_seller_section hide_clplist">';
		     stwWidgetHeading+= '<div class="content">'+STWJObject.STWHeading+'</div>';
		     stwWidgetHeading+='</div>';
		     return stwWidgetHeading;
		},
		blpheader:function(STWJObject){
			var stwWidgetHeading = "";
				stwWidgetHeading+= '<div class="best_seller_section hide_clplist">';
			     stwWidgetHeading+= '<div class="content">'+STWJObject.STWBlpHeading+'</div>';
			     stwWidgetHeading+='</div>';
			     return stwWidgetHeading;
			},
		tabs: function(STWJObject){
			var tabsFormationHtml ="";
			tabsFormationHtml+= '<div class="Menu"><div class="mobile selectmenu">ALL</div>';
			tabsFormationHtml+= '<ul><li class="active">ALL</li>';
			 var allCatCodeNameArray = STWJObject.STWCategories.split(",");
			 
			 $.each(allCatCodeNameArray,function(index,value){
				 	var categoryCodeNameArray = value.split("=");
				 	tabsFormationHtml+='<li>'+categoryCodeNameArray[1]+'</li>';
			 });
			 tabsFormationHtml+= '</ul></div>';
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
		},
		bindCarousel:function(){
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

		}
		
}
$(document).ready(function(){
	
		stw.renderTabsWidget();
		stw.renderFlatWidget();
	//stw.renderTabsWidget();
	//stw.renderFlatWidget();
	
	$(document).off("click",".best_seller.stw-list .Menu ul li").on("click",".best_seller.stw-list .Menu ul li",function() {
		
		var category = $(this).text() == 'ALL' ? '':$(this).text();
	  	var json = stwService.call(category,'');
		var carousel = stwRender.carousel(json);
		$("#stw_widget .carousel-component").html(carousel);
		stwRender.bindCarousel();
		
		$(".best_seller.stw-list .Menu ul li").removeClass("active");
		$(this).addClass("active");
		var active_text = $(this).text();
		$(".best_seller.stw-list .Menu .mobile.selectmenu").text(active_text);
		$('.best_seller.stw-list .Menu ul').slideUp();
		});
		$(".best_seller .Menu .selectmenu").off("click").on("click", function() {
            $(this).next().slideToggle();
        }); 
	});
