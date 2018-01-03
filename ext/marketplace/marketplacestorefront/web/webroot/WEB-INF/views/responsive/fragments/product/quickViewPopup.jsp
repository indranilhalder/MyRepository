<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="storepickup" tagdir="/WEB-INF/tags/responsive/storepickup" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<spring:eval expression="T(de.hybris.platform.util.Config).getParameter('marketplace.static.resource.host')" var="staticHost"/>
<script type="text/javascript"
	src="${commonResourcePath}/bootstrap/js/popover.js"></script>
	
<script type="text/javascript" src="https://apis.google.com/js/plusone.js"></script>

 <style type="text/css">
tr.d0 td {
  background-color:#E0E0E0 ;
  color: black;
}
</style>
 <script type="text/javascript">
 var quickview_height;
      (function() {
       var po = document.createElement('script'); po.type = 'text/javascript'; po.async = true;
       po.src = 'https://apis.google.com/js/client:plusone.js';
       var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(po, s);
     })();
    
    </script>
<%-- <script type="text/javascript"
	src="${commonResourcePath}/js/acc.productDetail.js"></script> --%>
 <script>

	function gotoLogin() {
		window.open(ACC.config.encodedContextPath + "/login", "_self");
	}
	

	var wishListList = [];

	// load wishlist of a particular user on opening popup

 
 var imagePageLimit = ${imgCount};

 function nextImage()
 {
	 $(".quick-view-popup .imageListCarousel li img").each(function(){
		$(this).show();
	 });
	var item_height = $(".product-info .imageListCarousel li").height();
 	var item_count = $(".product-info .imageListCarousel li").length;
 	var top = parseInt($(".product-info .imageListCarousel").css("top"));
	if(!(top % item_height)){
 	if(parseInt($(".product-info .imageListCarousel").css("top")) > -item_height* (item_count-imagePageLimit)){
 	$(".product-info .imageListCarousel").animate({top: "-="+item_height+"px"},"250");
 	$("#previousImage,#zoomModal #previousImage").css("opacity","1");
 	}
 	else{
 		$("#nextImage,#zoomModal #nextImage").css("opacity","0.5");
 	}
	}
 	if(parseInt($(".product-info .imageListCarousel").css("top")) == (item_count-imagePageLimit-1)*(-item_height)){
 		$("#nextImage,#zoomModal #nextImage").css("opacity","0.5");
 	}
 	
 }
 /*Display previous list of sellers after clicking previous link*/	
 function previousImage()
 {
 	var item_height = $(".product-info .imageListCarousel li").height();
 	var top = parseInt($(".product-info .imageListCarousel").css("top"));
	if(!(top % item_height)){
 	if(parseInt($(".product-info .imageListCarousel").css("top")) < 0 ){
 		$("#nextImage,#zoomModal #nextImage").css("opacity","1");
 		$(".product-info .imageListCarousel").animate({top: "+="+item_height+"px"},"250");
 	}
 	else{
 		$("#previousImage,#zoomModal #previousImage").css("opacity","0.5");
 	}
	}
 	if(parseInt($(".product-info .imageListCarousel").css("top")) == -item_height){
 		$("#previousImage,#zoomModal #previousImage").css("opacity","0.5");
 	}
 	
 }
 
 
//AKAMAI Fix
 var productSizeQuickVar = '${productSizeQuick}';
 var emiCuttOffAmount = '${emiCuttOffAmount}';
 var productCodeQuickView = '${product.code}';
 var variantCodesPdp = '${allVariantsString}';
 var msiteBuyBoxSeller = '${msiteBuyBoxSellerId}'; //CKD:TPR-250 
 var productCategoryType = '${product.rootCategory}';
 $( document ).ready(function() {
	 //Enable CTA's
	 removedisabled();
	//AKAMAI Fix
	 setSizeforAkamai();
	//AJAX BuyBox call
	
//CKD:TPR-250:Start
	// setBuyBoxDetails();
	 setBuyBoxDetails(msiteBuyBoxSeller);
//CKD:TPR-250:End
	 
	 ACC.quickview.onSizeSelectDropDownQuickView();
		ACC.quickview.onQuantitySelectDropDownQuick();
	 getRating_Qview('${gigyaAPIKey}','${product.code}','${product.rootCategory}');
	 
	 var timer = setInterval(function(){
			if($(document).find("#ussid_quick").length>0){
			isItemInWishList($('#ussid_quick').val());
			clearInterval(timer);
			}
		},1000);
	 
	
	$(document).on('show.bs.modal', "#modalProd", function() {
		$(".zoomContainer").remove();
		$('.picZoomer-pic').removeData('zoom-image');
	});
	$(document).on('hide.bs.modal', "#modalProd", function() {
		$('.quickview .picZoomer-pic').elevateZoom({
			zoomType : "window",
			cursor : "crosshair",
			zoomWindowFadeIn : 500,
			zoomWindowFadeOut : 750
		});
		
	});
	
		/* $('.main-image a img.picZoomer-pic').on('load', function(){
			$("#previousImage").css("opacity","0.5");
		 	$("#nextImage").css("opacity","1");
		 	var listHeight = $(".imageList li").height();
		 	if($("#previousImage").length){
		 		$(".imageList").css("height",(listHeight*imagePageLimit)+"px");
		 		$(".productImageGallery").css("height",(listHeight*imagePageLimit+100)+"px");
		 	}
		 	$(".imageListCarousel").show();
		}).each(function() {
	  	  	if(this.complete) $(this).load();
	  	}); */	
		
			$(".black-arrow").change(function() {
				 
				$("#qty1").val($(".black-arrow :selected").val());
			});
			$(".zoomContainer").remove();
			$('.picZoomer-pic').removeData('zoom-image');
			$("img.picZoomer-pic").attr('data-zoom-image',$(".quickview .product-image-container .productImageGallery .active img").attr("data-zoomimagesrc")); 
			$('.quickview .picZoomer-pic').elevateZoom({
		    	zoomType: "window",
		    	cursor: "crosshair",
		    	zoomWindowFadeIn: 500,
		    	zoomWindowFadeOut: 750
		       });
		
		
	 });
	 $("#cboxClose").click(function(){
		$(".zoomContainer").remove();
		$('.picZoomer-pic').removeData('zoom-image');
		$(".picZoomer-pic-wp img").attr('data-zoom-image',$(".product-image-container .productImageGallery .imageList .active img").attr("data-zoomimagesrc")); 
		$('.picZoomer-pic-wp .picZoomer-pic').elevateZoom({
	    zoomType: "window",
	    cursor: "crosshair",
	    zoomWindowFadeIn: 500,
	    zoomWindowFadeOut: 750
	       }); 
	       //TISEE-882
	       if(window.location.href.toLowerCase().indexOf('cart')>=0)
	       {
	    	   location.reload();
	       }
	       
	  });
 if( $("#variant option:selected").val()=="#")
	  {
		//  alert("Please select size!");
	 //return false;
	  }	 
 
 var selectSizeVal = '${selectedSize}';
 if(selectSizeVal=="")
 {
 	$(".out_of_stock").css("display","none");
 	$(".outOfStock").css("display","none");
 	$(".tempAddToCartQuickView").css("display","block");
 } 
 
/*  $('a.wishlist#wishlist_quick').popover({ 
	    html : true,
	    content: function() {
	      return $(this).parents().find('.add-to-wishlist-container_quick').html();
	    }
	  });*/
 $('input.wishlist#add_to_wishlist_quick').popover({ 
		html : true,
		content: function() {
			return $(this).parents().find('.add-to-wishlist-container_quick').html();
		}
	}); 
 
 function getRating_Qview(key,productCode,category)
 {
 	
 	var url = "https://comments.us1.gigya.com/comments.getStreamInfo?apiKey="+key+"&categoryID="+category+"&streamId="+productCode+"&includeRatingDetails=true&format=jsonp&callback=?";
 	  $.getJSON(url, function(data){
 		  
 		if("undefined" != typeof(data.streamInfo))
 		{
 	  	var totalCount=data.streamInfo.ratingCount;
 		//Reverse the source array
 		var ratingArray = data.streamInfo.ratingDetails._overall.ratings;
 		ratingArray  = ratingArray.reverse();
 		
 		  $(".rate-details .after").each(function(count){			  
 				
 				var countIndiv=ratingArray[count];	
 				$(".rate-bar .rating").eq(count).css({width:countIndiv/totalCount*100+"%"});
 				$(".rate-details .after").eq(count).text(ratingArray[count]);
 				
 			})
 			
 			var avgreview=data.streamInfo.avgRatings._overall;
 			var raingcount=data.streamInfo.ratingCount;
 			$(".product-detail ul.star-review a").empty();
 			$(".product-detail ul.star-review li").attr("class","empty");
 			$('#customer').text("Customer Reviews (" + data.streamInfo.ratingCount + ")");
 			//$("#gig-rating-readReviewsLink_quick").html(raingcount);
 		//	alert("count"+data.streamInfo.ratingCount);
 			//rating(avgreview,raingcount);
 			
 			
 			var rating = Math.floor(avgreview);
	 		var ratingDec = avgreview - rating;
	 		for(var i = 0; i < rating; i++) {
	 			$("#quick_view_rating"+" li").eq(i).removeClass("empty").addClass("full");
	 			}
	 		if(ratingDec!=0)
	 			{
	 			$("#quick_view_rating"+" li").eq(rating).removeClass("empty").addClass("half");
	 			} 
 		
 		
 			//TISUATPII-471 fix
 			var count=data.streamInfo.ratingCount;
 			if(count!= 0 && count == 1){
 				//$('#ratingDiv .gig-rating-readReviewsLink').text(data.streamInfo.ratingCount+" REVIEW");
 				$('.gig-rating-readReviewsLink_quick a').text(count+" REVIEW")			//UF-25
 			}
 			else if(count!= 0){
 				//$('#ratingDiv .gig-rating-readReviewsLink').text(data.streamInfo.ratingCount+" REVIEWS");
 				$('.gig-rating-readReviewsLink_quick a').text(count+" REVIEWS")			 //UF-25 
 			}
 			
 			
 			
 		}
 	  });
 	  
 	// var avgrating = '${product.averageRating}';
 		//alert(":-:"+avgrating);
 	 
 		
 }
 /* $('.main-image a img.picZoomer-pic').on('load', function(){
	 	console.log("jsp image is loaded");
	 	var mainImageHeight = $(".main-image").find("img.picZoomer-pic").height();
		console.log("jsp mainImageHeight is " + mainImageHeight);
		var thumbnailImageHeight = (mainImageHeight / 5);
		console.log("jsp thumbnailImageHeight is " + thumbnailImageHeight);
	if (thumbnailImageHeight > 0) {	
	 $(".imageList ul li img").css("height", thumbnailImageHeight);
 } 
		$("#previousImage").css("opacity","0.5");
	 	$("#nextImage").css("opacity","1");
	 	var listHeight = $(".imageList li").height();
	 	console.log("jsp listHeight is " + listHeight);
	 	console.log("jsp previousImage length is " + $("#previousImage").length);
	 	if($("#previousImage").length && listHeight > 0){
	 		$(".imageList").css("height",(listHeight*imagePageLimit)+"px");
	 		$(".productImageGallery").css("height",(listHeight*imagePageLimit+100)+"px");
	 	}
	 	$(".imageListCarousel").show();
	}).each(function () {
	    if (this.complete) {
	        $(this).trigger("load");
	    }
	}); */
	$(window).load(function() {	
		var mainImageHeight = $(".main-image").find("img.picZoomer-pic").height();
		var thumbnailImageHeight = (mainImageHeight / 5);
		var buttonHeight = $(".productImageGallery #previousImage").outerHeight();
		$(".imageList ul li img").css("height", thumbnailImageHeight);
		$("#previousImage").css("opacity","0.5");
		$("#nextImage").css("opacity","1");
		/* var listHeight = $(".imageList li").height(); */ /*commented as part of PRDI-68*/
		 var listHeight = thumbnailImageHeight + 13.6;		/*added as part of PRDI-68*/
		if($("#previousImage").length){
			$(".imageList").css("height",(listHeight*imagePageLimit)+"px");
			$(".productImageGallery").css("max-height",(mainImageHeight - buttonHeight)+"px");
		}
		$(".imageListCarousel").show();
	});
	
 </script>
 <style type="text/css">
tr.d0 td {
  background-color:#E0E0E0 ;
  color: black;
}
div.emailSend{

display:none;
}
</style>


<c:set var="image"><spring:theme code='quickview.image'/></c:set>
<c:set var="video"><spring:theme code='quickview.video'/></c:set>
<c:set var="cod_y"><spring:theme code='cod.eligible.yes'/></c:set>
<c:set var="stock_y"><spring:theme code='quickview.allOOStock'/></c:set>
<span id="defaultWishId_quick" style="display:none"><spring:theme code="wishlist.defaultname"/></span>
<span id="wishlistSuccess_quick" style="display:none"><spring:theme code="wishlist.success"/></span>
<span id="wishlistnotblank_quick" style="display:none"><spring:theme code="wishlist.notblank"/></span>
<span id="sharepretext" style="display:none"><spring:theme code="share.pretext"/></span>
<span id="shareposttext" style="display:none"><spring:theme code="share.posttext"/></span>
<span id="productUrl" style="display:none">${request.contextPath}${product.url}</span>
<span id="googleClientid" style="display:none">${googleClientid}</span>
<span id="facebookAppid" style="display:none">${facebookAppid}</span>

<span id="inventory" style="display: none"><p class="inventory">
			<font color="#ff1c47"><spring:theme code="Product.outofinventory" /></font>
		</p></span>
<input type="hidden" value="false"  id="isBinded"/>
<input type="hidden" value="true"  id="isQuickView"/>
<input type="hidden" value="${product.rootCategory}" id="categoryType"/>
<input type="hidden" name="showSizeQuickView" id="showSizeQuickView" value="${showSizeGuideForFA}"/>

<!-- CKD:TPR-250:Start -->
<c:choose>
	<c:when test="${not empty msiteBuyBoxSellerId}">
		<c:url value="${product.url}?sellerId=${msiteBuyBoxSellerId}" var="productUrl" />
	</c:when>
	<c:otherwise>
		<c:url value="${product.url}" var="productUrl" />
	</c:otherwise>
</c:choose>
<%-- <c:url value="${product.url}" var="productUrl" /> --%>
<!-- CKD:TPR-250:Start -->

<input type="hidden" value=""  id="ussidInQuickView"/>
<input type="hidden" value="${product.code}"  id="productCode"/>
<%-- <input type="hidden" maxlength="3" size="1" id="stock" name="stock"
		value="${availablestock}" /> --%>
<div class="quickview active">
<div class="content">
<div class="quick-view-popup product-info wrapper">
<!-- TPR-924 -->
<sec:authorize ifAnyGranted="ROLE_ANONYMOUS">
			<input type="hidden" id="loggedIn" value="false"/> 
		</sec:authorize>
		<sec:authorize ifNotGranted="ROLE_ANONYMOUS">
			<input type="hidden" id="loggedIn" value="true"/> 
		</sec:authorize>
		<!-- TPR-924 -->
<div class="product-image-container">
	<a class="wishlist-icon" onclick="openPop_quick()"></a>	
   <c:set var="increment" value="0"/>
<c:set var="thumbNailImageLength" value="${fn:length(galleryImages)}" />

<div class="productImageGallery image-list" style="position: relative; /* max-height: 390px; */">
<c:if test="${thumbNailImageLength > imgCount}"> 
	<button type="button" class="previous white" style="margin: 0 auto;"
				name="previousImageDevBtn" id="previousImage" onclick="previousImage()">
		<img src="${commonResourcePath}/images/thin_top_arrow_333.png"/><%-- <spring:theme code="product.othersellers.previous" /> --%>
	</button>
</c:if>
	<div class="imageList" style="overflow: hidden;${(thumbNailImageLength > imgCount)?"height:480px":""}">
		<ul class="jcarousel-skin imageListCarousel" style="display:block; position: relative; top: 0; width: 100%;"> 
			<c:forEach items="${galleryImages}" var="container" varStatus="varStatus" begin="0" end="${thumbNailImageLength}">
			<li id="addiImage${varStatus.index}" class="thumbailItem${varStatus.index +1}"> <!-- For TPR-4712 -->
					<span class="thumb ${(varStatus.index==0)? "active":""}">
						<c:if test="${container.thumbnail.mediaType.code eq 'Image'}">
							<img src="${container.thumbnail.url}" data-type="image" data-zoomimagesrc="${container.superZoom.url}"  data-primaryimagesrc="${container.product.url}" data-galleryposition="${varStatus.index}" alt="${container.thumbnail.altText}" title="${container.thumbnail.altText}" style="${(varStatus.index < imgCount)? "":"display:none;"}" />	
						</c:if>
						<c:if test="${container.thumbnail.mediaType.code eq 'Video'}">
							<%-- <img src="${commonResourcePath}/images/video-play.png"  data-type="video" data-videosrc="${container.thumbnail.url}" /> --%>
							<img src="${commonResourcePath}/images/video-play.png"  data-type="video" data-videosrc="${container.thumbnail.url}?rel=0&enablejsapi=1" style="${(varStatus.index < imgCount)? "":"display:none;"}" />
					    </c:if>
					</span>
				</li>
			</c:forEach>
		</ul>
	</div>
	<input type="hidden" id="totalAdditionalImage" name="totalAdditionalImage" value="${thumbNailImageLength}">

<c:if test="${thumbNailImageLength > imgCount}"> 
	<button type="button" class="next white" name="nextImageDevBtn" style="/* position:absolute; */ margin: 0 auto; bottom: 0; /* left: 18px; */" id="nextImage" onclick="nextImage()"> 
		<img src="${commonResourcePath}/images/thin_bottom_arrow_333.png"/>
	</button>
</c:if>
</div>

    <div class="main-image">
	<a onClick="openPop_quick();" class="wishlist-icon-qv normal"></a>
	<a onClick="openPop_quick();" class="wishlist-icon-qv zoom-qv" style="display: none;"></a>
	<a href="${productUrl}"> <product:productPrimaryImage lazyLoad="false"
				product="${product}" format="product" />
		</a><!-- 		<div class="zoom" style="z-index:10000;">
		<a onClick="openPop_quick();" id="wishlist_quick" class="wishlist" data-toggle="popover" data-placement='bottom'></a>
		</div> -->
		 <%-- <c:if test="${isCodEligible=='Y'}">
          <div class="cod" id="codId">
		  <span ><spring:theme code="product.cod"/></span> 
		  </div>
	 </c:if> --%>
	 
	<%-- <c:if test="${isNew=='Y'}"> --%>
		 <div id ="newProduct" style="z-index: 1;display:none;" class="new new-product">
					<img class="brush-strokes-sprite sprite-New"
					src="//${staticHost}/_ui/responsive/common/images/transparent.png"><span>New</span>
					</div>
	 	  <%--  </c:if>  --%>
	  <%--  <c:if test="${isOnline=='true'}"> --%>
		 	<div style="z-index: 1;;display:none;" class="online-exclusive">
					<img class="brush-strokes-sprite sprite-Vector_Smart_Object"
						src="//${staticHost}/_ui/responsive/common/images/transparent.png"> <span><spring:theme code="quickview.onlineexclusive"/></span>
				</div>
		<%-- </c:if> --%>
		</div>
		
<%-- 		<div id="emiStickerId" class="emi" style="display:none;">
							<spring:theme code="marketplace.emiavailable" />&nbsp;
							<a type="button" name="yes" id="prodEMI"
		data-target="#modalProd" onclick="openPopForBankEMI_quick()"
		data-toggle="modal"><spring:theme code="marketplace.emiinfo"></spring:theme></a> <input id="prodPrice" type="hidden" />
						</div> --%>
<%-- 		emi		<product:emiDetail product="${product}" /> --%>
	
<%-- 		<c:choose>
		<c:when test="${spPrice ne null}">
			<c:if test="${spPrice.value>emiCuttOffAmount}">
			<div id="emiStickerId" class="emi">
							<spring:theme code="marketplace.emiavailable" />&nbsp;
							<a type="button" name="yes" id="prodEMI"
		data-target="#modalProd" onclick="openPopForBankEMI_quick()"
		data-toggle="modal"><spring:theme code="marketplace.emiinfo"></spring:theme></a> <input id="prodPrice" type="hidden" />
						</div>
				<product:emiDetail product="${product}" />
			</c:if>
		</c:when>
		<c:otherwise>
			<c:choose>
				<c:when test="${mopPrice ne null}">
					<input type="hidden" id="mopId" value="${mopPrice}"/>
					<div>${mopPrice.formattedValue}</div>
					<c:if test="${mopPrice.value>emiCuttOffAmount}">
						<div id="emiStickerId" class="emi">
							<spring:theme code="marketplace.emiavailable" />&nbsp;
							<a type="button" name="yes" id="prodEMI"
							data-target="#modalProd" onclick="openPopForBankEMI_quick()"
							data-toggle="modal"><spring:theme code="marketplace.emiinfo"></spring:theme></a> <input id="prodPrice" type="hidden" />
						</div>
						<product:emiDetail product="${product}" />
					</c:if>
				</c:when>
				<c:otherwise>
					<input type="hidden" id="mrpId" value="${mrp}"/>
					<div>${mrpPrice.formattedValue}</div>
					<c:if test="${mrpPrice.value>emiCuttOffAmount}">
						<div id="emiStickerId" class="emi">
							<spring:theme code="marketplace.emiavailable" />&nbsp;
							<a type="button" name="yes" id="prodEMI"
		data-target="#modalProd" onclick="openPopForBankEMI_quick()"
		data-toggle="modal"><spring:theme code="marketplace.emiinfo"></spring:theme></a> <input id="prodPrice" type="hidden" />
						</div>
					<product:emiDetail product="${product}" />
			
					</c:if>
				</c:otherwise>
			</c:choose>
		</c:otherwise>
	</c:choose> --%>
		
	</div>	
    <div class="product-detail">
   <!-- CKD:TPR-250-Start-->
				<c:set var="clickableBrandname" value="${msiteBrandName}"/>
				<c:set var="clickableBrandCode" value="${msiteBrandCode}"/>
				<c:choose>
					<c:when test="${not empty clickableBrandname && not empty clickableBrandCode}">
						<h2 class="company">
							<a href="/${clickableBrandname}/c-${clickableBrandCode}">${product.brand.brandname}</a>
						</h2>
					</c:when>
					<c:otherwise>
						<h2 class="company">${product.brand.brandname}</a>
						</h2>
					</c:otherwise>
				</c:choose>
				<!-- CKD:TPR-250-End-->
              <%-- &nbsp;<spring:theme code="product.by"/>&nbsp;<span id="sellerNameIdQuick"></span>${sellerName} --%>
            <!-- Convert into AJAX call -->    
    <h3 class="product-name"><a href="${productUrl}">${product.productTitle}</a></h3>
    
    <!-- //TPR-3752 Jewel Heading Added -->
			<%-- <c:choose>
					<c:when test="${product.rootCategory=='FineJewellery'}">
						<div class="product-desc">
							<span class="key-label">
							 <c:forEach var="classification" items="${mapConfigurableAttributes}">
							 <c:if test="${not empty classification.value }">
   						 		<c:forEach var="classValue" items="${classification.value }">
   						 			${classValue.key} &nbsp;&nbsp;${classValue.value}
   						 			 </c:forEach>
   						 			<!--  <a href="" class="more-link">More</a> -->
							  </c:if> 
								</c:forEach>
							</span> 
						</div>
					</c:when>
				</c:choose>
     --%>
    
    <div class="price">
    
    
<%--     <c:choose>
    <c:when test="${not empty spPrice.value && spPrice.value ne 0}">
     <c:choose>
     	<c:when test="${mopPrice.value eq mrpPrice.value}">
     	    <input type="hidden" value="${spPrice.value}"  id="productPrice"/>
     		<span class="old">${mrpPrice.formattedValue}</span>&nbsp;<span class="sale">${spPrice.formattedValue}</span>
     	</c:when>
     	<c:otherwise>
     		<span class="old"> ${mrpPrice.formattedValue}</span>&nbsp;<span class="sale">${spPrice.formattedValue}</span>
     		 <input type="hidden" value="${spPrice.value}"  id="productPrice"/>
     	</c:otherwise>
     </c:choose>
     </c:when>
     <c:otherwise>
      <c:choose>
     	<c:when test="${not empty mopPrice.value && mopPrice.value ne 0}">
     		<c:choose>
     			<c:when test="${mopPrice.value eq mrpPrice.value}">
     			     <input type="hidden" value="${mrpPrice.value}"  id="productPrice"/>
     				<span class="sale">${mrpPrice.formattedValue}</span>
     			</c:when>
     			<c:otherwise>
     			     <input type="hidden" value="${mrpPrice.value}"  id="productPrice"/>
     				<span class="old">${mrpPrice.formattedValue}</span>&nbsp;<span class="sale">${mopPrice.formattedValue}</span>
     			</c:otherwise>
    		</c:choose>
     	</c:when>
     	<c:otherwise>
     	     <input type="hidden" value="${mrpPrice.value}"  id="productPrice"/>
     		<span class="sale">${mrpPrice.formattedValue}</span>
     	</c:otherwise>
     	</c:choose>
     </c:otherwise>
    </c:choose> --%>
    
    <!-- <input type="hidden" id="productPrice" name="productPrice" /> -->
   <p class="old" id="quickMrpPriceId" style="display:none">
	</p>
	<p class="sale" id="quickMopPriceId" style="display:none">
	</p>
	<p class="sale" id="quickSpPriceId" style="display:none">
	</p>
	<p class="savings pdp-savings" id="savingsOnProductIdQV" style="display:none">															
	  <span></span>
	</p>
    
    
    <%--for price breakup(TPR-3752) --%>

	
	<!-- Price Breakup removed From Quickview -->
	<!-- <div id = "showPricequick">
	
	<p id = "showquick" class="pricebreakup-link">Price Breakup</p>
	
	</div>
	<ul id="showPriceBreakupquick" class="price-breakuplist clearfix" style="display:none"></ul>
 -->
	<%-- </c:if> --%>
	
	<%--for price breakup(TPR-3752) --%>
  
  
  </div>  
	<%-- <div id="emiStickerId" class="Emi Emi_wrapper" style="display:none;">		
				<spring:theme code="marketplace.emiavailable" />&nbsp;		
							<a type="button" name="yes" id="prodEMI"		
		data-target="#modalProd" onclick="openPopForBankEMI_quick()"		
		data-toggle="modal"><spring:theme code="marketplace.emiinfo"></spring:theme></a> <input id="prodPrice" type="hidden" />		
	</div>	 --%>	
<%-- <product:emiDetail product="${product}" />  --%>
<a href="#" class="gig--readReviewsLink"></a>
	<span id="gig-rating-readReviewsLink_quick" ></span>	
  <input type="hidden" id="rating_review" value="${product.code}">
		
			
<!-- 			 <script>
				var avgrating = '${product.averageRating}';
				//alert(":-:"+avgrating);
				var rating = Math.floor(${product.averageRating});
				//alert("::"+rating);
				var ratingDec = ${product.averageRating} - rating;
				var star_id = $("#rating_review").val();
				for(var count = 0; count < rating; count++) {
					$("#quick_view_rating"+" li").eq(count).removeClass("empty").addClass("full");
					}
				if(ratingDec!=0)
					{
					$("#quick_view_rating"+" li").eq(rating).removeClass("empty").addClass("half");
					} 
				
			</script>  -->
 
 	
   <div class="product-content" style="margin-top:5px;">
   <product:emiDetail product="${product}" />
	   <div class="">
	<product:viewQuickViewVariant/>
	<spring:eval expression="T(de.hybris.platform.util.Config).getParameter('mpl.cart.maximumConfiguredQuantity.lineItem')" var="maxQuantityCount"/>
	<%-- <div class="qty">
	<!-- TISPRM-131 -->
		<p> <spring:theme code="product.configureproductscount.qty"/></p>
		<select id="quantity">		
		<c:forEach var="qtyCnt" begin="1" end="${maxQuantityCount}">
   		<option value="${qtyCnt}">${qtyCnt}</option>
		</c:forEach>
		</select>
	</div> --%> 

</div>
<%--  <div id="ajax-loader" style="margin: 0 auto; height:20px; width: 20px;"><img src="${commonResourcePath}/images/ajax-loader.gif"></div> --%>     
<!-- add to cart functionality -->
<div id="addToCartFormQuickTitle" class="addToCartTitle">
		</div>
	<ycommerce:testId code="quickview_addToCart_button_${product.code}">
	<div class="Cta">
	
	
	<span id="dListedErrorMsg" style="display: none"  class="dlist_message prod-dlisted-msg">
		<spring:theme code="pdp.delisted.message" />
    </span>
    
   

	
	 <!-- TPR-924 -->
	 
	<div class="buy-btn-holder clearfix"> 
		<div id="buyNowQv"> 
	        <button style="display: block" id="buyNowButton" type="button"  disabled="disabled" class="btn-block js-add-to-cart-qv">
				<spring:theme code="buyNow.button.pdp" />
			</button> 
			<%-- <button id="buyNowButtonQuick-wrong" type="button" class="btn-block" disabled="disabled"> <spring:theme code="buyNow.button.pdp" /></button> --%>
	    </div> 
	    
	    
	    <!-- TPR-924 -->
		<%-- <form:form id="addToCartFormQuick" action="${request.contextPath }/cart/add" method="post" class="add_to_cart_form"> --%>
		
		
		<form:form id="addToCartFormQuick" action="#" method="post" class="add_to_cart_form">
		<span id="addToCartFormQuickTitleSuccess" class="addToCartTitle">
		</span>
		<%-- <form:form method="post" id="addToCartFormQuick" class="add_to_cart_form"
		action="${request.contextPath }/cart/add"> --%>
		<c:if test="${product.purchasable}">
			<input type="hidden" maxlength="3" size="1" name="qty"
				class="qty js-qty-selector-input" value="1">
		</c:if>
		<input type="hidden" name="productCodePost" id="productCodePost" value="${product.code}" />
		<input type="hidden" name="wishlistNamePost" id="wishlistNamePost" value="N" />
		<input type="hidden" name="ussid" id="ussid_quick"	/> <!-- value="${buyboxUssid}" -->
		<input type="hidden" maxlength="3" size="1" id="stock" name="stock"
		 /> <!-- value="${availablestock}" --> <!-- Convert into AJAX call -->
		 <input type="hidden" name="sellerSelId" id="sellerSelId" /> 
		
		 <button id="addToCartButtonQuick" type="${buttonType}" disabled="disabled" class="btn-block js-add-to-cart tempAddToCartQuickView" style="display:none;">
			<spring:theme code="basket.add.to.basket" />
		</button>
		<%-- <span id="dListedErrorMsg" style="display: none"  class="dlist_message">
			<spring:theme code="pdp.delisted.message" />
	    </span> --%>
		
		<button id="addToCartButtonQuick-wrong" type="button" class="btn-block" disabled="disabled" > <spring:theme code="basket.add.to.basket" /></button>
		
											
		<span id="addToCartFormnoInventory" style="display: none" class="no_inventory"><p class="inventory">
			<font color="#ff1c47"><spring:theme code="Product.outofinventory" /></font>
			</p></span>
		 
		<%-- <c:choose>
			<c:when test="${allOOStock==stock_y}">				
			<button id="addToCartButton" type="${buttonType}"
												class="btn-block js-add-to-cart tempAddToCartQuickView" style="display:none;">
												<spring:theme code="basket.add.to.basket" />
											</button>
			</c:when>
			<c:otherwise>
			<span id="addToCartFormnoInventory" style="display: none" class="no_inventory"><p class="inventory">
			<font color="#ff1c47"><spring:theme code="Product.outofinventory" /></font>
			</p></span>			
					 <c:choose>
										<c:when test="${error eq 'true'}">
											<span id="dListedErrorMsg" class="dlist_message" style="color: #FE2E2E;float: right;padding-bottom: 5px;"> <spring:theme
													code="pdp.delisted.message" />
											</span>
											<button id="addToCartButton-wrong" type="button" class="btn-block" disable="true" style="display: block;"><spring:theme code="basket.add.to.basket" /></button>
											<script>
         										$('.wish-share').css('visibility','hidden');
    										</script>
										</c:when>
										<c:otherwise>
											<button id="addToCartButton" type="${buttonType}"
												class="btn-block js-add-to-cart">
												<spring:theme code="basket.add.to.basket" />
											</button>
										</c:otherwise>
									</c:choose>

								</c:otherwise>
							</c:choose> --%>
		
	
	</form:form>
	
	</div>
	
	<%-- <c:if test="${allOOStock==stock_y}"> --%>
			<span id="outOfStockIdQuick" style="display: none"  class="out_of_stock">
				<font color="red"><spring:theme code="product.product.outOfStock" /></font>
				<%-- <input type="button" id="add_to_wishlist_quick" onClick="openPop_quick('${buyboxUssid}');scrollbottom();" class="wishlist" data-toggle="popover" data-placement="bottom" value="<spring:theme code="text.add.to.wishlist"/>"/> --%>
				<input type="button" id="add_to_wishlist_quick" onClick="openPop_quick();" class="wishlist" data-toggle="popover" data-placement="bottom" value="<spring:theme code="text.add.to.wishlist"/>"/>
			</span>				
	<%-- </c:if> --%>
	</div>
	</ycommerce:testId> 
	<div class="SoldWrap">
	<div class="seller">Sold by <span id="sellerNameIdQuick"></span></div>
	<div class="fullfilled-by">
		<spring:theme code="mpl.pdp.fulfillment"></spring:theme>&nbsp;
		<%-- <c:choose>
		<c:when test="${fn:toLowerCase(fullfilmentType) == fn:toLowerCase('sship')}">
			<span id="fullFilledById">${sellerName}</span>
		</c:when>
		<c:otherwise>
			<span id="fullFilledById"><spring:theme code="product.default.fulfillmentType"/></span>
		</c:otherwise>
		</c:choose> --%>
		<span id="fulFilledByTshipQuick" style="display:none;"><spring:theme code="product.default.fulfillmentType"></spring:theme></span>
			<span id="fulFilledBySshipQuick"  style="display:none;"></span>
	</div>
	</div>
	 <ul class="star-review" id="quick_view_rating">
				<li class="empty"></li>
				<li class="empty"></li>
				<li class="empty"></li>
				<li class="empty"></li>
				<li class="empty"></li>
		
		<%-- 	<c:choose>
				<c:when test="${not empty product.ratingCount}">
			
					<span id="gig-rating-readReviewsLink_quick" >  <spring:theme code="rating.reviews"/></span>
				</c:when>
				<c:otherwise> --%>
					<%-- <span class="gig-rating-readReviewsLink_quick"> <spring:theme code="rating.noreviews"/></span> --%>
					<span class="gig-rating-readReviewsLink_quick"><a href="${productUrl}#ReviewSecion"></a></span>	<!-- UF-25 -->
				<%-- </c:otherwise>
			</c:choose> --%>
			</ul>    
<!-- adding to wishlist -->
				<ul class="wish-share">
					  <%--   <li><!-- <span id="addedMessage" style="display:none"></span> -->
						<a onClick="openPop_quick('${buyboxUssid}');scrollbottom();" id="wishlist_quick" class="wishlist" data-toggle="popover" data-placement='bottom'>....<spring:theme code="text.add.to.wishlist"/></a></li>
						<a onClick="openPop_quick();" id="wishlist_quick" class="wishlist" data-toggle="popover" data-placement='bottom'><spring:theme code="text.add.to.wishlist"/></a></li>
				<a onClick="openPop();" id="wishlist" class="wishlist" data-toggle="popover" data-placement='bottom'><spring:theme code="text.add.to.wishlist"/></a></li>  --%>
					<li>
						<div class="share">
							<%-- <span><spring:theme code="product.socialmedia.share"/></span> --%>
							<ul style="width: 100%;">
								<li>
							<!-- TPR-674 -->
							<a class="tw" onclick="return openPopup('https://twitter.com/intent/tweet?text='+ $('#sharepretext').text() + ' ' +window.location.host+ $('#productUrl').text() + ' ' + $('#shareposttext').text(), 'share_twitter')"></a>
							</li>
							<li>
							<a class="fb" onclick="return openPopup('https://www.facebook.com/dialog/feed?link=' + window.location.host+ $('#productUrl').text() + '&amp;app_id=' + $('#facebookAppid').text() + '&amp;description='+$('#sharepretext').text()+' '+$('#shareposttext').text()+' &amp;redirect_uri=http://www.facebook.com/', 'share_facebook')"></a> 
							 <!-- <a class="fb" onclick="return openPopup('https://www.facebook.com/dialog/feed?link=' + window.location + '&amp;app_id=145634995501895&amp;description='+$('#sharepretext').text()+' '+$('#shareposttext').text()+' &amp;redirect_uri=https://developers.facebook.com/tools/explorer')"></a>  -->				
							
							</li>
								<li>
							<button class="g-interactivepost"
                            data-contenturl=""
                            data-clientid='${googleClientid}'
                            data-cookiepolicy="single_host_origin"
                            data-prefilltext="<spring:theme code="share.pretext"/><spring:theme code="share.posttext"/>"
                            data-calltoactionlabel="OPEN"
                            data-calltoactionurl="">
                           <a class="gp"></a>
                           </button>
							<!-- <a class="gp" onclick="return openPopup('https://plusone.google.com/_/+1/confirm?url=https://www.dev.tataunistore.com&amp;clientid=888304528479-qdh1rp8r9o5fvh3dlabr7ebdbr02se6e.apps.googleusercontent.com&amp;prefilltext=helooo&amp;calltoactionurl=https://www.google.com')"></a>  -->
							<!-- <a href="" class="gp" onclick="return openPopup('https://plusone.google.com/_/+1/confirm?url=' + window.location)"></a> -->
							</li>
							
							<li><%-- <a class="mail" data-target="#popUpModal" data-url="${SHARED_PATH}"></a> --%>
							<a class="mail" id="mailQuick" role="button" data-toggle="popover" data-placement="bottom" ></a></li>
							</ul>
						</div>
					</li>
				</ul>
				<div class="full-details">
<a href="${productUrl}" class="quick-view-prod-details-link"><spring:theme code="quickview.productdetails"/></a>
</div>
				
	<script>
			$(".g-interactivepost").attr("data-contenturl",window.location.host+$('#productUrl').text());
			$(".g-interactivepost").attr("data-calltoactionurl",window.location.host+$('#productUrl').text());
			//$(".wish-share .share a.tw").attr("href","https://twitter.com/intent/tweet?text=Wow! I found this amazing product - check it out here"+window.location+". Like or comment to tell me what you guys think. Hit share to spread the love. ");
			
			var popUpWidth=500;
			var popUpHeight=450;
				 var PopUpLeftPosition = screen.width/2 - popUpWidth/2;
				    var PopUpTopPosition= screen.height/2 - popUpHeight/2;
				  //TPR-674
			function openPopup(url, buttontype) {
				utag.link({link_obj: this, link_text: buttontype , event_type : 'share_button_click' });
				    window.open(url, 'popup_id','scrollbars,resizable,height='+popUpHeight+',width='+ popUpWidth +',left='+ PopUpLeftPosition +',top='+ PopUpTopPosition);
			      return false;
			    }
			
			function scrollbottom() {
				//alert();
				 /*  $("#cboxLoadedContent").animate({ scrollTop: $('#cboxLoadedContent')[0].scrollHeight }, "slow");
				 return false; */
				};
				$(document).on('click','#mailQuick',function(){
						utag.link({link_obj: this, link_text: 'share_email' , event_type : 'share_button_click' });
						quickview_height = $("#cboxContent #cboxLoadedContent .quickview.active")[0].offsetHeight;
						if($("#cboxContent #cboxLoadedContent .quickview.active")[0].offsetHeight < $("#cboxContent #cboxLoadedContent .quickview.active")[0].scrollHeight){
							$("#cboxContent #cboxLoadedContent .quickview.active").css("height",$("#cboxContent #cboxLoadedContent .quickview.active")[0].scrollHeight + 10);
							$("#cboxContent").css("max-height",$("#cboxContent #cboxLoadedContent .quickview.active")[0].scrollHeight + 10);
						}
					})
					$(document).on('click','.g-interactivepost',function(){
				utag.link({link_obj: this, link_text: 'share_googleplus' , event_type : 'share_button_click' });
					})
			
  </script>

<div class="add-to-wishlist-container_quick">
<form>
	<input type="hidden" value="${product.code}" id="product_quick" />

				

					<div id="wishListDetailsId_quick" class="other-sellers" style="display: none">
					<h3 class="title-popover"><spring:theme code="wishlist.select"/></h3>
						<table class="other-sellers-table add-to-wishlist-popover-table">
							<%-- <thead>
								<tr>
									<th><spring:theme code="product.wishlist"/></th>
									<th><spring:theme code="product.select"/></th>
								</tr>
							</thead> --%>

							<tbody id="wishlistTbodyId_quick">
							</tbody>
						</table>

						 <input type="hidden" name="hidWishlist" id="hidWishlist_quick">
						 <p id='wishlistErrorId_quick' style="display: none ; color:red ;"> </p>
						<span id="addedMessage_quick" style="display:none;color:#60A119"></span>
						<input type="hidden" name="alreadyAddedWlName_quick" id="alreadyAddedWlName_quick">
						
						<button type='button' onclick="addToWishlist_quick($('#alreadyAddedWlName_quick').val())" name='saveToWishlist' id='saveToWishlist' class="savetowishlistbutton"><spring:theme code="product.wishlistBt"/></button>
					</div>

					<div id="wishListNonLoggedInId_quick" style="display: none"><spring:theme code="product.wishListNonLoggedIn"/></div>

				
				
			
			<!-- /.modal-content -->
		
		<!-- /.modal-dialog -->

	<!-- /.modal -->

</form>
</div>

				<!-- Social sharing -->


				<div id="fb-root"></div>
				<script>
				$("#cboxClose").empty();
				
				// TISPRDT-831 START
				//$("#cboxClose").addClass("close");
				// TISPRDT-831 END
				
					(function(d, s, id) {
						var js, fjs = d.getElementsByTagName(s)[0];
						if (d.getElementById(id))
							return;
						js = d.createElement(s);
						js.id = id;
						js.src = "//connect.facebook.net/en_GB/sdk.js#xfbml=1&version=v2.3";
						fjs.parentNode.insertBefore(js, fjs);
					}(document, 'script', 'facebook-jssdk'));
				</script>

				<script>
					!function(d, s, id) {
						var js, fjs = d.getElementsByTagName(s)[0], p = /^http:/
								.test(d.location) ? 'http' : 'https';
						if (!d.getElementById(id)) {
							js = d.createElement(s);
							js.id = id;
							js.src = p + '://platform.twitter.com/widgets.js';
							fjs.parentNode.insertBefore(js, fjs);
						}
					}(document, 'script', 'twitter-wjs');
				</script>
</div>


   
	</div>

</div></div>

		<sec:authorize ifAnyGranted="ROLE_ANONYMOUS">
			<input type="hidden" id="loggedInQuick" value="false"/> 
		</sec:authorize>
		<sec:authorize ifNotGranted="ROLE_ANONYMOUS">
		<input type="hidden" id="loggedInQuick" value="true"/> 
		</sec:authorize>
 	
<%-- <div class="quick-view-prod-details-container">
<a href="${productUrl}" class="quick-view-prod-details-link"><spring:theme code="quickview.productdetails"/></a>
</div>  --%>
<span id="addtobag" style="display:none"><spring:theme code="product.addtocart.success"/></span>
<span id="addtobagerror" style="display:none"><spring:theme code="product.error"/></span>
<span id="bagtofull" style="display:none"><spring:theme code="product.addtocart.aboutfull"/></span>
<span id="bagfull" style="display:none"><spring:theme code="product.bag"/></span>

</div>

<!-- Change for Showing VIDEO ZOOM Box -->
<div class="modal fade" id="videoModal" tabindex="-1" role="dialog" 
   aria-labelledby="myModalLabel" aria-hidden="true">
   <div class="overlay" data-dismiss="modal" onclick="closing()"></div>
      <div class="modal-content content"  style="width:53%; height:60%; overflow:hidden;">
            <button type="button" class="close pull-right" 
              onclick="closingVideo()" aria-hidden="true" data-dismiss="modal"  style="width: 15px; height: 15px; top:0; right:0px;">     <!-- TISPRO-508 -->
            </button>
			<iframe name="videoFrame" id="player" width="100%" height="100%" frameborder="0" allowfullscreen ></iframe>
      </div>
</div>
<style>
#videoModal .content > .close:before {
    content: "\00d7";
    color: #fff;
    font-family: "Montserrat";
    font-size: 17px;
    font-weight: 600;
    -webkit-transition: font-weight 0.15s;
    -moz-transition: font-weight 0.15s;
    transition: font-weight 0.15s;
}
#cboxLoadedContent {
	margin-top: 0;
	overflow: visible !important;
}
</style>

 <div id="emailLoggedInIdquick" style="display: none"><spring:theme code="product.emailNonLoggedIn"/></div>
<div id="emailSendQuick" class="emailSend">
	
				<div class="click2chat-container send-email-container" id="myModalLabel">
					<h1><spring:theme code="email.mail"/></h1>
					
					<form>
				
				
				<label for="forgotPassword_email"><spring:theme code="email.mailto"/></label>
				<input type="text" id="emailId_quick" placeholder="abc@xyz.com,abc@xyz.com">
				<input type="hidden" id="pId_quick" value="${product.code}"/>
				
				<!-- <button type="button" id="sendEmail" class="blue" onClick="sendmail()">Send Mail</button> -->
				<div class="actions">
				
				<button id="sendEmail"  onClick="return sendmail_quick()" type="button">
				<spring:theme code="email.send"/>
				</button>
				<span id="email_quick" style="display:none"></span>
				<span id="emailError_quick" style="display:none;color:#ff1c47"><spring:theme code="email.emailError"/></span>
				<span id="emailSuccess_quick" style="display:none;color:#60A119"><spring:theme code="email.emailSuccess"/></span>
				<span id="emailUnSuccess_quick" style="display:none;color:#ff1c47"><spring:theme code="email.emailUnSuccess"/></span>
				<span id="emailEmpty_quick" style="display:none;color:#ff1c47"><spring:theme code="email.emailEmpty"/></span>
				<span id="validateemail_quick" style="display:none;color:#ff1c47"><spring:theme code="email.validate"/></span>

				</div>
				</form>
				</div>
	
</div>
<script>
$('ul.wish-share div.share').mouseleave(function(){
 $(this).find('[data-toggle="popover"]').popover('hide');
		$("#cboxContent #cboxLoadedContent .quickview.active").css("height",quickview_height);
		$("#cboxContent").css("max-height",quickview_height);
});
function sendmail_quick(){
	$('#emailSuccess_quick,#emailUnSuccess_quick,#emailError_quick,#validateemail_quick,#emailEmpty_quick').hide();
	var email = $('#emailId_quick').val();
	var productId = $('#pId_quick').val();
	var dataString = 'emailList=' + email+"&productId="+productId;
	if(email!=""){
	if(validEmail(email)){
		//var emailList = email.split(";");
		$.ajax({
			url : ACC.config.encodedContextPath + "/p-sendEmail",
			data : dataString,			
			type : "GET",
			cache : false,
			success : function(data) {
				if (data == true) {	
					$('#emailUnSuccess_quick,#emailError_quick,#validateemail_quick,#emailEmpty_quick').hide();
					$('#emailSuccess_quick').show();
				}
				else{
					$('#emailSuccess_quick,#emailError_quick,#validateemail_quick,#emailEmpty_quick').hide();
					$('#emailUnSuccess_quick').show();
					return false;
				}
			},
			error : function(resp) {
				$('#emailSuccess_quick,#emailUnSuccess_quick,#validateemail_quick,#emailEmpty_quick').hide();
				$('#emailError_quick').show();
				return false;
			}
		});
	}
	else{
		$('#emailSuccess_quick,#emailUnSuccess_quick,#emailError_quick,#emailEmpty_quick').hide();
		$('#validateemail_quick').show();
		return false;
	}
	}
	else{
		$('#emailSuccess_quick,#emailUnSuccess_quick,#emailError_quick,#validateemail_quick').hide();
		$('#emailEmpty_quick').show();
		return false;
	}
}

function validEmail(email) {
	  var arr = email.split(',');
	  for(var i=0;i<arr.length;i++){
	  var emaili = arr[i];
	 
	  var emailReg = /^([\w-\.]+@([\w-]+\.)+[\w-]{2,4})?$/;
	  if(!emailReg.test(emaili)){
		  return false;
	  }
	  }	  
	  return true;	  
	}
$('ul.wish-share a#mailQuick').popover({ 
	html : true,
	content: function() {
		var loggedIn = $("#loggedInQuick").val();
		if(loggedIn=='true') {
			return $(this).parents().find('#emailSendQuick').html();
		} else {
			return '<div style="padding: 10px;">'+$(this).parents().find('#emailLoggedInIdquick').html()+'</div>';
			
		}
	}
});


$(document).on("keypress","#defaultWishName_quick",function(e) {
	var wishlistname = $("#defaultWishName_quick").val();
	var mainDiv = 'defaultWishName_quick';
	var errorDiv = "#addedMessage_quick";
	validateSpcharWlName(e,wishlistname,mainDiv,errorDiv);
}); 

/*add to wishlist st*/
var wishQv;
wishQv = setInterval(function(){
	if($(".zoomContainer .wishlist-icon-qv.zoom-qv").length == 0) {
		$(".zoomContainer").append($(".wishlist-icon-qv.zoom-qv").clone());
		$(".zoomContainer .wishlist-icon-qv.zoom-qv").css({
			"left":$(".quickview .main-image").width() - 50,
			"display":"block"
			});
		$(".zoomContainer .wishlist-icon-qv.zoom-qv").hide();
	} else {
		clearInterval(wishQv);
	}
	
},50);
$(document).on("mouseover",".zoomContainer",function(e) {
	if($(".zoomContainer .wishlist-icon-qv.zoom-qv").length == 0) {
	    $(".zoomContainer").append($(".wishlist-icon-qv.zoom-qv").clone());
		$(".zoomContainer .wishlist-icon-qv.zoom-qv").css({
			"left":$(".quickview .main-image").width() - 50,
			"display":"block"
			});
	}
	$(".wishlist-icon-qv.normal").hide();
	$(".zoomContainer .wishlist-icon-qv.zoom-qv").show();
	$(".zoomContainer .wishlist-icon-qv.zoom-qv").css({
		"left":$(".quickview .main-image").width() - 50
		});
});
$(document).on("mouseleave",".zoomContainer",function(e) {
	$(".zoomContainer .wishlist-icon-qv.zoom-qv").remove();
	$(".wishlist-icon-qv.normal").show();
	$(".zoomContainer .wishlist-icon-qv.zoom-qv").hide();
});
$(window).resize(function(){
	if($(window).width() < 1024) {
		$(".wishlist-icon-qv.normal").show();
		
	}
	
});

/*add to wishlist st*/
</script>
