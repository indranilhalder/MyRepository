var html="";
$(document).ready(function(){
	stWidget('stw_widget');
	
});
 function stWidget(divElement) {  
        	$.ajax({
        		  url: '/getStwrecomendations',
        		  dataType: "json",
        		//  data:{pageType:ghg,}
        		  success: jsonCallback,
        		  fail:function(){
        			  
        		  },
        		  complete:function(){
        			  console.log(html);
        			  $("#"+divElement).html(html);
        			  $(".stw-widget-owl").owlCarousel({
        					items:5,
        					loop: true,
        					nav:true,
        					dots:false,
        					navText:[]
        					/*navigation:true,
        					rewindNav: false,
        					navigationText :[],
        					pagination:false,
        					items:2,
        					itemsDesktop : false, 
        					itemsDesktopSmall : false, 
        					itemsTablet: false, 
        					itemsMobile : false*/
        				});
        		  }
        		})
        }
 function jsonCallback(STWJObject) {
	 html='<div class="carousel-component">';
     html +='<div class="carousel js-owl-carousel js-owl-lazy-reference js-owl-carousel-reference stw-widget-owl">';
	    $.each(STWJObject.STWElements, function( index, value ) {
	        	 	 html += '<div class="item slide">';
	        		 html +='<div class="product-tile"><div class="image"><a href="'+value.productUrl+'" class="product-tile"> <img src="'+value.imageUrl+'"></a></div>';
	        		 html +=' <div class="short-info"><ul class="color-swatch"><li><span  style="background-color: '+value.availableColor+';border: 1px solid rgb(204, 211, 217);" title="'+value.availableColor+'"></span></li></ul>';
	        		 html += '<div class="brand">'+value.productBrand+'</div>';
	        		 html += ' <a href="'+value.productUrl+'" class="item"><h3 class="product-name">'+value.productName+'</h3>';
	        		 html += '<div class="price"><span class="stw-mrp">&#8377;'+value.mrp+' </span><span class="stw-mop"> &#8377;'+value.mop+'</span>';
	        		 html += '</div></a></div></div></div>'; 
	    });
	    html+='</div></div>'; 
	}