<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ attribute name="product" required="true" type="de.hybris.platform.commercefacades.product.data.ProductData" %>
<%@ attribute name="galleryImages" required="true" type="java.util.List" %>
<!-- below js code is responsible for zoom functionality of image on hover -->
<script type="text/javascript">

	 $(function() {
		$('.productImagePrimaryLink').picZoomer();
		/* $('.piclist li').on('click', function(event) {
			var $pic = $(this).find('img');
			$('.picZoomer-pic').attr('src', $pic.attr('src'));
		}); */
	}); 

	function hit() {
		// initialized with no keyboard
		$("#zoomModal").modal({
			backdrop : 'static',
			keyboard : false
		});
		//$("#zoomModal").addClass("active");
		$("div#zoom_gallery img").each(function() {

			if ($(this).attr('data-type') == "video") {
				$(this).hide();
			}
		});
	}

	function closing() {
		$("#zoomModal, #videoModal").modal('hide');
		//$("#zoomModal, #videoModal").removeClass("active");
		 $("#videoFrame")[0].src += "&autoplay=0";
	}
	$(document).ready(function(){
		if ($(window).width() > 789) {
			$(".picZoomer-pic-wp img").attr('data-zoom-image',$(".product-image-container .productImageGallery .imageList .active img").attr("data-zoomimagesrc")); 
			$('.picZoomer-pic-wp .picZoomer-pic').elevateZoom({
		    zoomType: "window",
		    cursor: "crosshair",
		    zoomWindowFadeIn: 500,
		    zoomWindowFadeOut: 750
		       });	
			}
	});
	
</script>


<!-- the below code is responsible for displaying the main image  -->

<div class="main-image span-14 productImage"> 

<div style="z-index:1;display:none"  class="online-exclusive" >
	<img class="brush-strokes-sprite sprite-Vector_Smart_Object" src="/store/_ui/responsive/common/images/transparent.png">
	<span>online exclusive</span>
</div>
	<div class="productImagePrimary" id="primary_image">	

		 <%-- <span id="codId" style="display:none;"> <img  alt="cod" src="${commonResourcePath}/images/cod.png" style="width:60px;">  </span> --%> 
		<img  id ="newProduct" class="new brush-strokes-sprite sprite-New" style="z-index:1; display:none;" src="${commonResourcePath}/images/transparent.png">
		<div class="productImagePrimaryLink" id="imageLink" data-href="${productZoomImagesUrl}" target="_blank" data-toggle="modal" title="<spring:theme code="general.zoom"/>">
			<product:productPrimaryImage product="${product}" format="zoom"/>
		</div>			
	</div>	
<!-- 	<iframe name="videoFrame" id="videoFrame" width="400" height="560" frameborder="0"  style="display:none"  allowfullscreen ></iframe>
 -->
 </div>

<!-- Change for Showing ZOOM Box -->
<div class="modal fade" id="zoomModal" tabindex="-1" role="dialog" 
   aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="overlay" data-dismiss="modal"></div>
      <div class="modal-content content">
            <button type="button" class="close pull-right" 
              onclick="closing()" aria-hidden="true" data-dismiss="modal">
            </button>
         <div class="" style="">       
				<product:productZoomImagePanel galleryImages="${galleryImages}"
			product="${product}" />
         </div>
      </div>
</div>

<!-- Change for Showing VIDEO ZOOM Box -->
<div class="modal fade" id="videoModal" tabindex="-1" role="dialog" 
   aria-labelledby="myModalLabel" aria-hidden="true">
   <div class="overlay" data-dismiss="modal" onclick="closing()"></div>
      <div class="modal-content content"  style="width:53%; height:60%; overflow:hidden;">
            <button type="button" class="close pull-right" 
              onclick="closing()" aria-hidden="true" data-dismiss="modal"  style="width: 15px; height: 15px; top:0; right:0px;">
            </button>
			<iframe name="videoFrame" id="videoFrame" width="100%" height="100%" frameborder="0" allowfullscreen ></iframe>
      </div>
</div>
<style>
#videoModal .content > .close:before {
    content: "\00d7";
    color: #fff;
    font-family: "Avenir Next";
    font-size: 17px;
    font-weight: 600;
    -webkit-transition: font-weight 0.15s;
    -moz-transition: font-weight 0.15s;
    transition: font-weight 0.15s;
}
</style>

