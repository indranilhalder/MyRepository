var stwHtml = "";
var product="";
$(document).ready(function(){
	stWidget('#stw_widget','');
	
});
 function stWidget(divElement,category) {  
        	$.ajax({
        		  url: '/getStwrecomendations',
        		  dataType: "json",
        		  type:"GET",
        		  data:{pageType:'Home',widgetType:'STW',siteType:'Marketplace',sendOnlyListingId:'false',category:category},
        		  success: jsonCallback,
        		  fail:function(){
        			  console.log('STW failed to load --'+error);
        		  },
        		  complete:function(){
        			  $(divElement).html(stwHtml);
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
        		})
        }
 
 function jsonCallback(STWJObject) {
	var tabsFormationHtml ="";
	tabsFormationHtml+= '<div class="best_seller stw-list">'
	tabsFormationHtml+= '<div class="best_seller_section hide_clplist">';
	tabsFormationHtml+= '<div class="content">'+STWJObject.STWHeading+'</div>';
	tabsFormationHtml+= '</div>';
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
	 tabsFormationHtml+= '</div>';
	 tabsFormationHtml+= '<div id="widget">'+formStwWidgetProducts(STWJObject)+'</div>';
     stwHtml = tabsFormationHtml;
}

function formStwWidgetProducts(STWJObject){
	var stwWidgetProducts = "";
	stwWidgetProducts='<div class="carousel-component">';
	stwWidgetProducts +='<div class="carousel js-owl-carousel js-owl-lazy-reference js-owl-carousel-reference stw-widget-owl">';
	    $.each(STWJObject.STWElements, function( index, value ) {
	    	var productId=value.listingId;
	    	var product=productId.toUpperCase();
	    	//console.log("productID"+product);
	    //	var productQVUrl=value.productUrl+"/quickView";
	    	if(null!=value.mrp && null!=value.mop){
	    	var	savingPriceCal=(value.mrp-value.mop);
			var savingPriceCalPer=(savingPriceCal/value.mrp)*100;
			var	savingsOnProduct=Math.round((savingPriceCalPer*100)/100);
			}
	    	stwWidgetProducts += '<div class="item slide">';
	    	stwWidgetProducts +='<div class="product-tile"><li onmouseover="showQuickview(this)" onmouseout="hideQuickView(this)" class="look slide product-tile stw_widget_list_elements productParentList" style="display: inline-block;position: relative;"><div class="image"><a href="'+value.productUrl+'" class="product-tile"> <img src="'+value.imageUrl+'"></a>';
	    	stwWidgetProducts += '<div onclick=popupwindow("'+product+'") class="IAQuickView" style="position: absolute; text-transform: uppercase;cursor: pointer; bottom: 118px; z-index: -1; visibility: hidden; color: #00cbe9;display: block; width: 100%; text-align: center;background: #f8f9fb;background-color: rgba(248, 249, 251,0.77);-webkit-font-smoothing: antialiased;height: 70px;font-size:12px;"><span>Quick View</span></div>';
	    	stwWidgetProducts +=' <div class="short-info"><ul class="color-swatch"><li><span  style="background-color: '+value.availableColor+';border: 1px solid rgb(204, 211, 217);" title="'+value.availableColor+'"></span></li></ul>';
	    	stwWidgetProducts += '<div class="brand">'+value.productBrand+'</div>';
	    	stwWidgetProducts += ' <a href="'+value.productUrl+'" class="item"><h3 class="product-name">'+value.productName+'</h3>';
	    	stwWidgetProducts += '<div class="price"><span class="stw-mrp">&#8377;'+value.mrp+' </span><span class="stw-mop"> &#8377;'+value.mop+'</span>';
	    	stwWidgetProducts += '<p class="savings pdp-savings"> <span>(-'+savingsOnProduct+'%)</span></p>';
	    	stwWidgetProducts += '</div></a></div></div></li></div></div>'; 
	    });
	stwWidgetProducts+='</div></div>'; 
	$("#widget").html(stwWidgetProducts);  
	return stwWidgetProducts;
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
