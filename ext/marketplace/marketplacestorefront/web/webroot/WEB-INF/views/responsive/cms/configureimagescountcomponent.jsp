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
$(document).ready(function(){
	var mainImageHeight = $(".main-image").find("img.picZoomer-pic").height();
	var thumbnailImageHeight = (mainImageHeight / 5);
	$(".imageList ul li img").css("height", thumbnailImageHeight);
	$("#previousImage").css("opacity","0.5");
	$("#nextImage").css("opacity","1");
	var listHeight = $(".imageList li").height();
	if($("#previousImage").length){
		$(".imageList").css("height",(listHeight*imagePageLimit)+"px");
		$(".productImageGallery").css("height",(listHeight*imagePageLimit+100)+"px");
	}
	$(".imageListCarousel").show();
});

</script>
<c:set var="increment" value="0"/>
<c:set var="thumbNailImageLength" value="${fn:length(galleryImages)}" />

<div class="productImageGallery pdp-gallery image-list" style="position: relative; /* max-height: 390px; */">
<c:if test="${thumbNailImageLength > count}"> 

<button type="button" class="previous white" style="margin: 0 auto;"
				name="previousImageDevBtn" id="previousImage" onclick="previousImage()">
				<img src="${commonResourcePath}/images/thin_top_arrow_333.png"/><%-- <spring:theme code="product.othersellers.previous" /> --%>
</button>
</c:if>
			
	<div class="imageList" style="overflow: hidden;">
		<ul class="jcarousel-skin imageListCarousel" style="display:none; position: relative; top: 0; width: 100%;"> 
			<c:forEach items="${galleryImages}" var="container" varStatus="varStatus" begin="0" end="${thumbNailImageLength}">
			
				<li id="addiImage${varStatus.index}">
					<span class="thumb ${(varStatus.index==0)? "active":""}">
					<c:if test="${container.thumbnail.mediaType.code eq 'Image'}">
						<img src="${container.thumbnail.url}" data-type="image" data-zoomimagesrc="${container.superZoom.url}"  data-primaryimagesrc="${container.product.url}" data-galleryposition="${varStatus.index}" alt="${container.thumbnail.altText}" title="${container.thumbnail.altText}" />	
					</c:if>
					<c:if test="${container.thumbnail.mediaType.code eq 'Video'}">
					<img src="${commonResourcePath}/images/video-play.png"  data-type="video" data-videosrc="${container.thumbnail.url}?rel=0&enablejsapi=1" />
					<%-- <iframe src="${commonResourcePath}/images/video-play.png"  data-type="video" data-videosrc="${container.thumbnail.url}?rel=0&enablejsapi=1" id="player"></iframe> --%>
					</c:if>
					
					</span>
				</li>
			</c:forEach>
		</ul>
		</div>
		<input type="hidden" id="totalAdditionalImage" name="totalAdditionalImage" value="${thumbNailImageLength}">

<c:if test="${thumbNailImageLength > count}"> 
	<button type="button" class="next white" name="nextImageDevBtn" style="/* position:absolute; */ margin: 0 auto; bottom: 0; /* left: 18px; */" id="nextImage" onclick="nextImage()"> 
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
