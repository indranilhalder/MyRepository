<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>

<%@ attribute name="product" required="true" type="de.hybris.platform.commercefacades.product.data.ProductData" %>
<%@ attribute name="galleryImages" required="true" type="java.util.List" %>
   <script type="text/javascript">
   
   
   
   /* function changeIt(object){
	var superzoomurl = object.getAttribute("data-zoomimagesrc");
	 $('.super_zoom img').attr('src',superzoomurl);
	  $('#zommMgId').attr('src',superzoomurl);
   }  */     
  
        $( document ).ready(function() {
    	   $("#zoom_gallery iframe").hide();
    	   
    	   var l = document.getElementById('zoom_gallery').getElementsByTagName('img');

    	   for (var i=0; i<l.length; i++)
    	   {	   		  
    	    l[i].addEventListener('click',function(){changeIt(this);},false);
    	   }
    	   
    	  
    	 });
        
      
        
    </script>
    
<style>
#zoomId img {
 width: 70%;
}
#zoomId {
	max-height: 600px;
}
#zoom_gallery .image-list {
    display: inline-block;
    width: 11%;
    float: left;
}
#codId{
display: none !important;
} 
</style>


<!-- Displaying thumbnail images -->
<div id="zoom_gallery">



	 <cms:pageSlot position="ConfigureImagesCount" var="component">
		<cms:component component="${component}" />
	</cms:pageSlot> 
	
</div>	


<div id="zoomId" class="super_zoom">
<product:productSuperZoomImage product="${product}" format="superZoom"/>

</div>
<script>
$(document).ready(function(){
	$("#zoomModal #previousImage").css("opacity","0.5");
	$("#zoomModal #nextImage").css("opacity","1");
});
</script>