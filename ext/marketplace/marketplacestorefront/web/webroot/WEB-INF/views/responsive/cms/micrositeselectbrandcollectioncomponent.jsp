<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>	



<li id="shopMicrositeSeller"> 
</li>

<script>
	
$(document).ready(function () {
	var pageLabel = '${cmsPage.label}';
		var siteName = '${cmsSite.uid}';
	var lastSegment = pageLabel.split('/').pop();
	    var url='**/m/fetchSellerSalesHierarchyCategories/'+lastSegment;
	        
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
	            		jQuery('<ul/>', {
	            		    id: 'topul'
	            		}).appendTo('#shopMicrositeSeller');
	            		$container = $('#topul');
	            			if(secondLevelCategoryData.subCategories!=null){
	            				$.each(secondLevelCategoryData.subCategories, function(i, v) {
	            					$container.append('<li class="level1"><div class="toggle" ><a href= "'+siteName+'' + v.url + '">'+v.name+'</a></div><ul class="words words'+v.code+'"></ul>');
	            					if(v.subCategories!=null){
	    	        		 			$.each(v.subCategories, function(j, v1) {	
	    	        		 				$( "ul.words"+v.code).append('<li class="short words"><div class="toggle"><a href="'+siteName+''+v1.url+'" style="">'+v1.name+'</a></div></li>');
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
	        
	        $("#shopMicrositeSeller").on("click",".toggle", function(e){
	        	  var p = $(e.currentTarget).parent();
	        			    if(p.hasClass('active')) {
	        			      p.removeClass('active');
	        			    } else {
	        			      p.addClass('active');
	        			    }
	        	});
	
	});
	</script>	
