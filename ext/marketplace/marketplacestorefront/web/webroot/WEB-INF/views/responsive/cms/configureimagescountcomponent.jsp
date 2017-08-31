<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%> 
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<script>
var imagePageLimit = ${count};
$(window).load(function() {	
	var mainImageHeight = $(".main-image").find("img.picZoomer-pic").height();
	var thumbnailImageHeight = (mainImageHeight / 5);
	var buttonHeight = $(".productImageGallery #previousImage").outerHeight();
	/* $(".imageList ul li img").css("height", thumbnailImageHeight); */
	/*start of PRDI-397*/
 	if ($(window).width() > 1025) {
 		$(".imageList ul li img").css("height", thumbnailImageHeight);
 	}
	$("#previousImage").css("opacity","0.5");
	$("#nextImage").css("opacity","1");
	/* var listHeight = $(".imageList li").height(); */ /*commented as part of PRDI-68*/
	 var listHeight = thumbnailImageHeight + 13.6;		/*added as part of PRDI-68*/
	if($("#previousImage").length){
		/* $(".imageList").css("height",(listHeight*imagePageLimit)+"px"); */
		/*start of PRDI-397*/
 		/* if ($(window).width() > 1025) {
 			$(".imageList").css("height",(listHeight*imagePageLimit)+"px");
 		} */
		$(".productImageGallery").css("max-height",(mainImageHeight - buttonHeight)+"px");
	}
	$(".imageListCarousel").show();
});
/*start of PRDI-68*/
$(window).on("load resize", function() {
		var mainImageHeight = $(".main-image").find("img.picZoomer-pic").height();
		var thumbnailImageHeight = (mainImageHeight / 5);
		var buttonHeight = $(".productImageGallery #previousImage").outerHeight();
		var listHeight = thumbnailImageHeight + 13.6;
		if($("#previousImage").length){
			/* $(".imageList").css("height",(listHeight*imagePageLimit)+"px"); */
			/*start of PRDI-397*/
 			if ($(window).width() > 1025) {
 				$(".imageList").css("height",(listHeight*imagePageLimit)+"px");
 			}
			$(".productImageGallery").css("max-height",(mainImageHeight - buttonHeight)+"px");
		}
});
/*end of PRDI-68*/
</script>
<c:set var="increment" value="0"/>
<c:set var="thumbNailImageLength" value="${fn:length(galleryImages)}" />

<div class="productImageGallery pdp-gallery image-list" style="position: relative; /* max-height: 390px; */">
<c:if test="${thumbNailImageLength > count}"> 

<button type="button" class="previous white" style="margin: 0 auto;"
				name="previousImageDevBtn" id="previousImage" onclick="previousImage(this)">
				<img src="${commonResourcePath}/images/thin_top_arrow_333.png"/><%-- <spring:theme code="product.othersellers.previous" /> --%>
</button>
</c:if>
			
	<div class="imageList" style="overflow: hidden;">

		<ul class="jcarousel-skin imageListCarousel" style="display:block; position: relative; top: 0; width: 100%;"> 
			<c:forEach items="${galleryImages}" var="container" varStatus="varStatus" begin="0" end="${thumbNailImageLength}">
			
				<li id="addiImage${varStatus.index}" class="thumbailItem${varStatus.index +1}"> <!-- For TPR-4687 -->
					<span class="thumb ${(varStatus.index==0)? "active":""}">

					<c:if test="${container.thumbnail.mediaType.code eq 'Image'}">
						<img src="${container.thumbnail.url}" data-type="image" data-zoomimagesrc="${container.superZoom.url}"  data-primaryimagesrc="${container.product.url}" data-galleryposition="${varStatus.index}" alt="${container.thumbnail.altText}" title="${container.thumbnail.altText}" style="${(varStatus.index < count)? "":"display:none;"}" />	
					</c:if>
					<c:if test="${container.thumbnail.mediaType.code eq 'Video'}">
					<img src="${commonResourcePath}/images/video-play.png"  data-type="video" data-videosrc="${container.thumbnail.url}?rel=0&enablejsapi=1" style="${(varStatus.index < count)? "":"display:none;"}" />
					<%-- <iframe src="${commonResourcePath}/images/video-play.png"  data-type="video" data-videosrc="${container.thumbnail.url}?rel=0&enablejsapi=1" id="player"></iframe> --%>
					</c:if>
				</span>
				</li>
			</c:forEach>
		</ul>
		</div>
		<input type="hidden" id="totalAdditionalImage" name="totalAdditionalImage" value="${thumbNailImageLength}">

<c:if test="${thumbNailImageLength > count}"> 
	<button type="button" class="next white" name="nextImageDevBtn" style="/* position:absolute; */ margin: 0 auto; bottom: 0; /* left: 18px; */" id="nextImage" onclick="nextImage(this)">
				<%-- <spring:theme code="product.othersellers.next" />  --%><img src="${commonResourcePath}/images/thin_bottom_arrow_333.png"/>
</button>
</c:if>
</div>
<style>
.imageList:after {
	content: "";
	display: block;
	clear: both;
}
</style>
