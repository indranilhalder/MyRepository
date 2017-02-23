<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>	



<li id="shopMicrositeSeller"> 
</li>

<script>
	
$(document).ready(function () {
	//TPR-4471
	var requestParam = window.location.href;
	var siteName = '${cmsSite.uid}';
	var sellerName = $('#mSeller_name').val();
	var lastSegment = "";
	if(sellerName != undefined) {
		lastSegment = sellerName;
	}
	else
	{
		lastSegment = requestParam.split('/').pop();
	}
	    var url='/m/fetchSellerSalesHierarchyCategories/'+lastSegment;
	      if(lastSegment != ''){  
	        $.ajax({
	        	type: 'GET',
	        	url: url,
	        	success : function(secondLevelCategoryData) {
	        		
	        		if(secondLevelCategoryData != ''){
	        			/* Specified seller is available in SSH1 category hierarchy */
	        			jQuery('<div/>', {
	            		    id: secondLevelCategoryData.code,
	            			class: 'toggle',
	            		    text: 'Shop '+lastSegment
	            		}).appendTo('#shopMicrositeSeller');
	        			jQuery('<span/>', {
	            		    id: 'mobile-menu-toggle',
	            			class: 'mainli',
	            		}).appendTo('#shopMicrositeSeller');
	            		jQuery('<ul/>', {
	            		    id: 'topul'
	            		}).appendTo('#shopMicrositeSeller');
	            		$container = $('#topul');
	            			if(secondLevelCategoryData.subCategories!=null){
	            				$.each(secondLevelCategoryData.subCategories, function(i, v) {
	            					$container.append('<li class="level1"><div class="toggle" ><a href= "'+siteName+'' + v.url + '">'+v.name+'</a></div><span id="mobile-menu-toggle" class="mainli"></span><ul class="words words'+v.code+'"><li><a href="'+siteName+'' + v.url + '" class="view_dept">View '+v.name+' </a></li></ul>');
	            					if(v.subCategories!=null){
	    	        		 			$.each(v.subCategories, function(j, v1) {	
	    	        		 				$( "ul.words"+v.code).append('<li class="short words"><div class="toggle"><a href="'+siteName+''+v1.url+'" style="">'+v1.name+'</a></div><span id="mobile-menu-toggle" class="mainli"></span></li>');
	    	        			 			if(v1.subCategories!=null){
	    	        			 				$.each(v1.subCategories, function(q, v2) {	
	    	        			 					$( "ul.words"+v.code ).append('<li class="long words"><div class="toggle" style="text-transform: capitalize !important;"><a href="'+siteName+''+v2.url+'" style="font-weight:normal;">'+v2.name+'</a></div></li>');
	    	        			 				});	  
	    	        			 			}  
	            						});		
	    	        		 
	            					}
	            					
	            				});		
	            			}
	        		}
	        		else{
	        			/* empty , Seller undefined */
	        			/* Specified seller is not available in SSH1 category hierarchy */
	        		}	
        		    
			 	},
				 error : function(secondLevelCategoryData){
					 
				 }
			 
			 });
	      }
	        
	       /*  $("#shopMicrositeSeller").on("click",".toggle", function(e){
	        	  var p = $(e.currentTarget).parent();
	        			    if(p.hasClass('active')) {
	        			      p.removeClass('active');
	        			    } else {
	        			      p.addClass('active');
	        			    }
	        	}); */
	    	if ('ontouchstart' in window) {
		 		$("body").on("click","header.brand-header .content .bottom nav>ul>li>ul>li.level1>div.toggle>a", function(){
		 			$(this).attr("href","#");
		 		});
				}
	        	
	    	/*TPR-682*/
				/*For menu*/
				 $(document).on('click','header .content nav > ul > li#shopMicrositeSeller > ul#topul > li.level1',function(){
					var brand_name = lastSegment.replace(/[^a-z0-9\s]/gi, '').replace(/[\s]+/g, '_').toLowerCase();
					var top_menu_name = $(this).children('div.toggle').children('a').text().replace(/[^a-z0-9\s]/gi, '').replace(/[\s]+/g, '_').toLowerCase();
			    	var linkText = "shop_" + brand_name + "_" + top_menu_name;
					utag.link({
						link_text: linkText, 
						event_type : 'brandstore_navigation_click'
					});
			    });
			
				/*For Sub menu*/
			   $(document).on('click','header .content nav > ul > li#shopMicrositeSeller > ul#topul > li.level1 > ul.words > li.short.words',function(){
					var brand_name = lastSegment.replace(/[^a-z0-9\s]/gi, '').replace(/[\s]+/g, '_').toLowerCase();
					var top_menu_name = $(this).parents('li.level1').children('div.toggle').children('a').text().replace(/[^a-z0-9\s]/gi, '').replace(/[\s]+/g, '_').toLowerCase();
			    	var sub_menu_name = $(this).text().replace(/[^a-z0-9\s]/gi, '').replace(/[\s]+/g, '_').toLowerCase();
					var linkText = "shop_" + brand_name + "_" + top_menu_name + "_" + sub_menu_name;
					utag.link({
						link_text: linkText, 
						event_type : 'brandstore_navigation_click'
					});
			    });
				/*For Sub Sub menu*/
				$(document).on('click','header .content nav > ul > li#shopMicrositeSeller > ul#topul > li.level1 > ul.words > li.long.words',function(){
					var brand_name = lastSegment.replace(/[^a-z0-9\s]/gi, '').replace(/[\s]+/g, '_').toLowerCase();
					var top_menu_name = $(this).parents('li.level1').children('div.toggle').children('a').text().replace(/[^a-z0-9\s]/gi, '').replace(/[\s]+/g, '_').toLowerCase();
			    	var sub_menu_name = $(this).prevAll('li.short.words:first').children('div.toggle').children('a').text().replace(/[^a-z0-9\s]/gi, '').replace(/[\s]+/g, '_').toLowerCase();
					var sub_sub_menu_name = $(this).text().replace(/[^a-z0-9\s]/gi, '').replace(/[\s]+/g, '_').toLowerCase();
					var linkText = "shop_" + brand_name + "_" + top_menu_name + "_" + sub_menu_name + "_" + sub_sub_menu_name;
					utag.link({
						link_text: linkText, 
						event_type : 'brandstore_navigation_click'
					});
			    });
		   /*TPR-682 ends*/
	   
	});
	</script>	
