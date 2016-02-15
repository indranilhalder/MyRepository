<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


<%@ attribute name="product" required="true" type="de.hybris.platform.commercefacades.product.data.ProductData" %>
<%@ attribute name="galleryImages" required="true" type="java.util.List" %>

   <script type="text/javascript">
   
 
       $(function() {
        	
            $('.productImagePrimaryLink').picZoomer();

            $('.piclist li').on('click',function(event){
                var $pic = $(this).find('img');
                $('.picZoomer-pic').attr('src',$pic.attr('src'));
            });
        }); 
        
        
    </script>




<div class="span-14 productImage">

    

	<div class="productImagePrimary " id="primary_image">

		
		
		<a class="productImagePrimaryLink" id="imageLink" href="${productZoomImagesUrl}" data-href="${productZoomImagesUrl}" target="_blank" title="<spring:theme code="general.zoom"/>">
			<product:productPrimaryImage product="${product}" format="zoom"/>
		</a>
		
	</div>

	
	
</div>