<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>	



<li id="micrositeSellerLogo" class="simple-banner-component">
</li>

<script>
	
$(document).ready(function () {
	var sellerName = $('#mSeller_name').val();
	var lastSegment = "";
	if(sellerName != undefined) {
		lastSegment = sellerName;
	}
	 var url='/m/fetchSellerLogo/'+lastSegment;
	      if(lastSegment != ''){  
	        $.ajax({
	        	type: 'GET',
	        	dataType : "json",
	        	cache: false,
	        	url: url,
	        	success : function(data) {
                    $("#micrositeSellerLogo").append("<img src="+data.url+">");
			 	},
				 error : function(data){
					//console.log("error " +data); 
				 }
			 
			 });
	      }
	        
	});
	</script>