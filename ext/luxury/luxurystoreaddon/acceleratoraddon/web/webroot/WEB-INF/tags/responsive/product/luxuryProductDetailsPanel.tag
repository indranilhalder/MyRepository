<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="luxuryProduct" tagdir="/WEB-INF/tags/addons/luxurystoreaddon/responsive/product"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/addons/luxurystoreaddon/responsive/shared/theme" %>

<spring:eval expression="T(de.hybris.platform.util.Config).getParameter('luxury.static.resource.host')" var="staticHost"/>
<style type="text/css">
tr.d0 td {
  background-color:#E0E0E0 ;
  color: black;
}
</style>

 <script type="text/javascript">
 $(window).on("load",function(){
	 var po = document.createElement('script'); po.type = 'text/javascript'; po.async = true;
     po.src = 'https://apis.google.com/js/client:plusone.js';
     var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(po, s);
 });
 <!-- Change for INC_10528 -->
 $(document).ready(function(){
	 $('.gig-rating-readReviewsLink_pdp').css( 'cursor', 'pointer' );
	 $(".gig-rating-readReviewsLink_pdp").click(function() {
		    $('html,body').animate({scrollTop: $("#ReviewSecion").offset().top},'slow');
		});
 });
 
      $("#sellersSkuListId").val("");
      $("#skuIdForED").val("");
      $("#skuIdForHD").val("");
      $("#skuIdsWithNoStock").val("");
      $("#skuIdForCod").val("");
      $("#stockDataArray").val("");
      $("#isPinCodeChecked").val("");
  //    $("#selectedSizeVariant").val("");
    </script>
		<sec:authorize ifAnyGranted="ROLE_ANONYMOUS">
			<input type="hidden" id="loggedIn" value="false"/> 
		</sec:authorize>
		<sec:authorize ifNotGranted="ROLE_ANONYMOUS">
			<input type="hidden" id="loggedIn" value="true"/> 
		</sec:authorize>
<input type="hidden" name="selectedSize" id="selectedSize"	value="${selectedSize}"/>

<!--- START: INSERTED for MSD --->
<!-- TPR-1375 changes -->
<input type="hidden" value="true" id="isProductPage" name="isProductPage"/>

<c:if test="${isMSDEnabled}">
	<input type="hidden" value="${isMSDEnabled}" name="isMSDEnabled"/>
		<c:if test="${product.rootCategory eq 'Clothing'}">
		<input type="hidden" value="true" name="isApparelExist"/>
		<input type="hidden" value="${pageType}" name="currentPageMSD"/>	
		</c:if>
</c:if>
<c:forEach items="${product.categories}" var="categoryForMSD">
	<c:if test="${fn:startsWith(categoryForMSD.code, 'MSH')}">
	<input type="hidden" value="${categoryForMSD.code}" name="salesHierarchyCategoryMSD" />   
</c:if>
</c:forEach>
<input type="hidden" value="${product.rootCategory}" name="rootCategoryMSD" />   
<input type="hidden" name="productCodeMSD" class="cartMSD"	value="${product.code}" />							
<!-- End MSD -->  
<!-- Tealium -->
<input type="hidden" id="product_unit_price" value="${product_unit_price}" />
<input type="hidden" id="site_section" value="${site_section}" />
<input type="hidden" id="product_list_price" value="${product_list_price}" />
<input type="hidden" id="product_name" value="${product_name}" />
<input type="hidden" id="product_sku" value="${product_sku}" />
<input type="hidden" id="page_category_name" value="${page_category_name}" />
<input type="hidden" id="category_id" value="${category_id}" />
<input type="hidden" id="page_section_name" value="${page_section_name}" />
<input type="hidden" id="page_name" value="${page_name}" />
<input type="hidden" id="product_id" value="${product_id}" />
<input type="hidden" id="page_subcategory_name" value="${page_subcategory_name}" />
<input type="hidden" id="page_subcategory_name_l3" value="${page_subcategory_name_l3}" />
<input type="hidden" id="product_brand" value="${product_brand}" />
<input type="hidden" id="site_section_detail" value="${site_section_detail}" />
<input type="hidden" id="product_category" value="${product_category}" />
<!-- TPR-672 START -->
<input type="hidden" id="product_applied_promotion_title" value="${product_applied_promotion_title}" />
<input type="hidden" id="product_applied_promotion_code" value="${product_applied_promotion_code}" />
<!-- TPR-672 END -->
<!-- TPR-429 START-->
<input type="hidden" id="pdpSellerIDs" value='${pdpSellerIDs}'/>
<input type="hidden" id="pdpBuyboxWinnerSellerID" value=''/>
<input type="hidden" id="pdpOtherSellerIDs" value=''/>
<!-- TPR-429 END-->
<!-- For Data Layer Schema changes -->
<input type="hidden" id="product_stock_count" value="${product_stock_count}" />
<input type="hidden" id="out_of_stock" value="${out_of_stock}" />
<input type="hidden" id="product_discount" value="${product_discount}" />
<input type="hidden" id="product_discount_percentage" value="${product_discount_percentage}" />

<!-- End Tealium -->

<!-- TISPRM-56 -->
<input type="hidden" id="product_allVariantsListingId" value="${allVariantsString}"/>


<div itemscope itemtype="http://schema.org/Product" class="pdp">
	<div class="product-info wrapper pdp-view clearfix">
		<div class="product-image-container pdp-left colmn">
		<div class="clearfix">
			<div class="pdp-img-nav">
				<c:forEach items="${galleryImages}" var="container">
					<c:if test="${container.luxuryThumbnail.mediaType.code eq 'Image'}">
						<div><img src="${container.luxuryThumbnail.url}" alt="${product_name}" data-zoom-image="${container.luxurySuperZoom.url}"></div>
					</c:if>
					<c:if test="${container.thumbnail.mediaType.code eq 'Video'}">
						<c:set var="videoAvailable" value="true"/>
						<c:set var="videoUrl" value="${container.thumbnail.url}"/>
					</c:if>
				</c:forEach>
			</div>
			<div class="pdp-img">
				<div class="pdp-img-slider circle-pager">
						<c:set var="isImgAdded" value="false"/>
					<c:choose>
						<c:when test="${fn:length(galleryImages)>0}">
							<c:forEach items="${galleryImages}" var="container">
								<c:if test="${container.luxuryThumbnail.mediaType.code eq 'Image'}">
										<c:set var="isImgAdded" value="true"/>
									<div><img src="${container.luxuryProduct.url}" alt="${product_name}" data-zoom-image="${container.luxurySuperZoom.url}" class="zoomer"></div>
								</c:if>
								<c:if test="${container.thumbnail.mediaType.code eq 'Video'}">
									<c:set var="isImgAdded" value="true"/>
								</c:if>
								<c:if test="${empty container.luxuryProduct && isImgAdded ne 'true'}">
									<theme:luxuryImage imgClass="${imgClass}" code="img.missingProductImage.product" alt="${fn:escapeXml(product.name)}" title="${fn:escapeXml(product.name)}"/>
								</c:if>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<theme:luxuryImage imgClass="${imgClass}" code="img.missingProductImage.product" alt="${fn:escapeXml(product.name)}" title="${fn:escapeXml(product.name)}"/>
						</c:otherwise>
					</c:choose>
				</div>
				 <div class="pdp-video">
                   <video width="70%" height="auto" controls>
                      <source src="${videoUrl}" type="video/webm"> 
                      <source src="${videoUrl}" type="video/ogg"> 
                      <source src="${videoUrl}" type="video/mp4">
                      Your browser does not support HTML5 video.
                    </video>
                  </div>		
				<div class="offer-icon">
					<c:choose>
						<c:when test="${product.isProductNew eq true}">
							<span class="newarrival"></span>
						</c:when>
						<c:when test="${product.isOfferExisting eq true }">
							<span class="onsale"></span>
						</c:when>
						<c:when test="${product.isOnlineExclusive eq true}">
							<span class="exclusive"></span>
						</c:when>
					</c:choose>
				</div>
			</div>
			
						
		</div>		
			<%-- <cms:pageSlot position="ConfigureImagesCount" var="component">
				<cms:component component="${component}" />
			</cms:pageSlot>
			<product:productImagePanel galleryImages="${galleryImages}"
				product="${product}" /> --%>

				<%-- <input id="emiCuttOffAmount" type="hidden" value="${emiCuttOffAmount}"/>
				<!-- EMI section -->
				<product:emiDetail product="${product}" /> --%>
			<span id="productPromotionSection" style="display: none;"><!-- UF-60 wrapping product:productPromotionSection in a span -->
			<!-- promotion  section -->
			<%-- <product:productPromotionSection product="${product}" /> --%>
			</span>
			
				<div class="pdp-social-links text-center">
					<ul>
						<c:if test="${videoAvailable}">
							<li><a href="#" data-video-url="${videoUrl}" class="play">Play</a></li>
						</c:if>
						<li><a class="save add-to-wl-pdp">Save</a></li>
						<li><a data-toggle="modal" data-target="#share-modal" href="#" class="share">Share</a></li>
					</ul>
					<div id="share-modal" class="modal fade" role="dialog">
						<div class="modal-dialog">
							<div class="modal-content">
								<div class="modal-header">
									<button type="button" class="close" data-dismiss="modal">x</button>
									<h4 class="modal-title">Share</h4>
								</div>
								<div class="modal-body nopadding mt-10 clearfix">
								    <luxuryProduct:socialSharing product="${product}" /> 																
								</div>
							</div>
						</div>					
					</div>	
				</div>
			
		
		<!-- Added for carousel in mobile view -->
		
			<%-- <div class="product-image-container device">
			<a class="wishlist-icon" onclick="addToWishlist()"></a>
			<c:set var="thumbNailImageLengthDevice" value="${fn:length(galleryImages)}" />
				<div class="jcarousel-skin imageListCarousel" id="pdpProductCarousel"> 
					<c:forEach items="${galleryImages}" var="container" varStatus="varStatus" begin="0" end="${thumbNailImageLengthDevice}">	
		
		
						<div id="addiImageTab${varStatus.index}">
							<span>
							<c:if test="${container.thumbnail.mediaType.code eq 'Image'}">
								<img src="${container.product.url}" data-type="image" data-zoomimagesrc="${container.superZoom.url}"  data-primaryimagesrc="${container.product.url}" data-galleryposition="${varStatus.index}" alt="${container.thumbnail.altText}" title="${container.thumbnail.altText}" />	
							</c:if>
							<c:if test="${container.thumbnail.mediaType.code eq 'Video'}">
							<img src="${commonResourcePath}/images/video-play.png"  data-type="video" data-videosrc="${container.thumbnail.url}?rel=0&enablejsapi=1" />
							<iframe src="${commonResourcePath}/images/video-play.png"  data-type="video" data-videosrc="${container.thumbnail.url}?rel=0&enablejsapi=1" id="player"></iframe>
							</c:if>
		
							</span>
						</div>
					</c:forEach>
				</div>
			</div> --%>
			
			<div class="wishAddSucess">
				<span><spring:theme code="mpl.pdp.wishlistSuccess"></spring:theme></span>
			</div>
			
			<div class="wishRemoveSucess">
				<span><spring:theme code="mpl.pdp.wishlistRemoveSuccess"></spring:theme></span>
			</div>
			
			<div class="wishAddLogin">
				<span><spring:theme code="product.wishListNonLoggedIn"></spring:theme></span>
			</div>
			<%-- <div class="wishAlreadyAdded">
				<span><spring:theme code="mpl.pdp.wishlistAlreadyAdded"></spring:theme></span>
			</div> --%>
		</div>
		<div class="product-detail pdp-right">
			
			<c:set var="req" value="${pageContext.request}" />
  			<c:set var="baseURL" value="${fn:replace(req.requestURL, req.requestURI, '')}" />
  			<c:set var="params" value="${requestScope['javax.servlet.forward.query_string']}"/>
  			<c:set var="requestPath" value="${requestScope['javax.servlet.forward.request_uri']}"/>
  			<c:choose>
  				<c:when test="${not empty params}">	
  					<c:set var="mainurl" value="${baseURL}${requestPath}?${params}"></c:set>
  				</c:when>
  				<c:otherwise>
  					<c:set var="mainurl" value="${baseURL}${requestPath}"></c:set>
  				</c:otherwise>
  			</c:choose>
			<ycommerce:testId
				code="productDetails_productNamePrice_label_${product.code}">
				<h2 itemprop="brand" itemscope itemtype="http://schema.org/Organization" class="company"><span itemprop="name">${product.brand.brandname}</span></h2>
				<%-- <a itemprop="url" href="${mainurl}"> --%>		<!-- Commented as part of UF-181 -->
				<!-- For TPR-4358 -->
				<h1 itemprop="name" class="product-name">${product.productTitle}</h1>
				<meta itemprop="sku" content="${product_sku}"/>
				
				<div class="SoldWrap">
				<ycommerce:testId
					code="productDetails_productNamePrice_label_${product.code}">
					<div class="seller">Sold by <span id="sellerNameId"></span></div>
				</ycommerce:testId>
				<%-- <div class="fullfilled-by">
				<spring:theme code="mpl.pdp.fulfillment"></spring:theme>&nbsp;<span id="fulFilledByTship" style="display:none;"><spring:theme code="product.default.fulfillmentType"></spring:theme></span>
				<span id="fulFilledBySship"  style="display:none;"></span>
				</div> --%>
			</div>
				<!-- </a> -->
			</ycommerce:testId>
                
			<ycommerce:testId
				code="productDetails_productNamePrice_label_${product.code}">
				<luxuryProduct:luxuryProductPricePanel product="${product}" />
			</ycommerce:testId>
			
			<input id="emiCuttOffAmount" type="hidden" value="${emiCuttOffAmount}"/>
				<!-- EMI section -->
			
			<%-- <product:emiDetail product="${product}" /> --%>
			
			
			<spring:eval expression="T(de.hybris.platform.util.Config).getParameter('freebiePriceThreshold')" var="freebiePriceThreshVal"/>
	        <input type="hidden" id="freebiePriceThreshId" value="${freebiePriceThreshVal}">
	
			<div id="freebieProductMsgId" style="display:none">
			 <spring:theme code="freebie.product.message" text="Freebie: This product is not on sale" ></spring:theme>				
			</div>			
			<!-- TPR-275 ends -->
			<luxuryProduct:luxuryProductMainVariant /> 
			
			<!-- TISPRM-97 starts -->
				<!-- TPR-772 starts -->
			<div class="pdp-promo-block promo-block">
			<!-- TPR-772 ends -->
			<c:if test="${not empty product.potentialPromotions}">
			
			<c:choose>
				<c:when test="${not empty product.potentialPromotions[0].channels}">
				
				<c:forEach var="channel"
							items="${product.potentialPromotions[0].channels}">
				<c:if test="${channel eq 'Web'||channel eq ''||channel==null}">	
			<div class="pdp-promo-title pdp-title">
				<b>OFFER:</b> ${product.potentialPromotions[0].title}
				<!-- <a class="details">View more</a> --> <!-- commented for TPR-589  -->
			</div>
			</c:if> <!-- end if check for channel web -->
			</c:forEach>
			</c:when>
			
			<c:otherwise>
			<div class="pdp-promo-title pdp-title">
				<b>OFFER:</b> ${product.potentialPromotions[0].title}
			<!-- 	<a class="details">View more</a> --><!-- commented for TPR-589  -->
			</div>
			</c:otherwise>
			</c:choose>			
			</c:if>
			</div>
			
			<!--  Added for displaying offer messages other than promotion, TPR-589 -->
			<!--INC144313502-->
				 <!-- <div>
					<a class="pdp-promo-title-link" style="display:none">View more</a>
				</div> -->	
				
			<!-- TISPRM-97 ends -->
			<!-- TPR-275 starts  -->
			
			<cms:pageSlot position="AddToCart" var="component">
					<cms:component component="${component}" />
				</cms:pageSlot>
				
			<cms:pageSlot position="PinCodeService" var="component">
				<cms:component component="${component}" />
			</cms:pageSlot>
			
			<%-- <c:if test="${isGigyaEnabled=='Y'}">
				<div id='ratingDiv'></div>
				</ul>
			</c:if> --%>
			<%-- <cms:pageSlot position="AddToCart" var="component">
					<cms:component component="${component}" />
				</cms:pageSlot> --%>
			<%-- <div class="description">${product.summary}</div> --%>

		
			
			<!-- seller information section  -->
			<%-- <div class="seller-details">
			<product:sellerInfoDetailsSection/>
			</div> --%>

			

		</div>
	<%-- 	 <div class="product-content">
			<div class="swatch">
				<cms:pageSlot position="VariantSelector" var="component">
					<cms:component component="${component}" />
				</cms:pageSlot>
				
				
				
        
			</div> 
			
			
			<span id="defaultWishId" style="display:none"><spring:theme code="wishlist.defaultname"/></span>
			<span id="wishlistSuccess" style="display:none"><spring:theme code="wishlist.success"/></span>
			<span id="wishlistnotblank" style="display:none"><spring:theme code="wishlist.notblank"/></span>
			<span id="sharepretext" style="display:none"><spring:theme code="share.pretext"/></span>
			<span id="shareposttext" style="display:none"><spring:theme code="share.posttext"/></span>
			
			
	
			<!-- Social sharing -->
    	<script>
			$(".g-interactivepost").attr("data-contenturl",window.location);
			$(".g-interactivepost").attr("data-calltoactionurl",window.location);
			//$(".wish-share .share a.tw").attr("href","https://twitter.com/intent/tweet?text=Wow! I found this amazing product - check it out here"+window.location+". Like or comment to tell me what you guys think. Hit share to spread the love. ");
			
			var popUpWidth=500;
			var popUpHeight=450;
				 var PopUpLeftPosition = screen.width/2 - popUpWidth/2;
				    var PopUpTopPosition= screen.height/2 - popUpHeight/2;
			function openPopup(url) {
				    window.open(url, 'popup_id','scrollbars,resizable,height='+popUpHeight+',width='+ popUpWidth +',left='+ PopUpLeftPosition +',top='+ PopUpTopPosition);
			      return false;
			    }
			
  </script> 


			<!-- Social sharing -->


			<div id="fb-root"></div>
			<div class="Wrap">
			<cms:pageSlot position="PinCodeService" var="component">
				<cms:component component="${component}" />
			</cms:pageSlot>
          </div>
          <ul class="wish-share desktop">

				<li><!-- <span id="addedMessage" style="display:none"></span> -->
				<!-- Commented as per PDP CR Change -->
				<a onClick="openPop();" id="wishlist" class="wishlist" data-toggle="popover" data-placement="bottom"><spring:theme code="text.add.to.wishlist"/></a></li>
				<li>
				<product:socialSharing product="${product}" />
					
				</li>
			</ul>
		</div> --%>
</div>		
<div class="tabs-block">
				<luxuryProduct:luxuryProductPageTabs />
			</div>
	
	
	<c:set var="electronics"><spring:theme code='product.electronics'/></c:set>
	<c:set var="clothing"><spring:theme code='product.clothing'/></c:set>
	<!-- TISPRO-271 Changes -->
	<c:set var="footwear"><spring:theme code='product.footwear'/></c:set>
	
	<!-- Added for TATAUNISTORE-15 Start -->
	<c:set var="watches"><spring:theme code='product.watches'/></c:set>
	<c:set var="accessories"><spring:theme code='product.fashionAccessories'/></c:set>
	<!-- For Infinite Analytics Start -->
	<input type="hidden" value="${productCategoryType}" id="categoryType"/>
	<div id="productContentDivId"></div>
	
	
	<!-- Made For Living Section Starts -->
<%-- <div class="company-product">
  <div class="wrapper">
    <div class="first">
      <img src="${commonResourcePath}/images/madeforliving1.jpg">
      <div>
        <h3>How to style it:</h3>
        <h3>Jumpsuits</h3>
        <a href="#" class="play"></a>
      </div>
    </div>
    <div class="middle">
      <div>
        <div>
          <img src="${commonResourcePath}/images/lov-logo.jpg" class="logo">
          <h3>Made for living</h3>
          <p>The one thing that a woman looks for in any apparel is comfort. L.O.V.
          offers that perfect fit with everyday casual wear.</p>
        </div>
      </div>
    </div>
    <div class="last">
      <img src="${commonResourcePath}/images/madeforliving2.jpg">
    </div>
  </div>
</div> --%>
	<!-- Made For Living Section Ends -->




<%-- Start Gigya Rating & Reviews --%>
	<c:if test="${isGigyaEnabled=='Y'}">
<!-- Below line added for UF-60 -->
	<input type="hidden" value="${isGigyaEnabled}" name="isGigyaEnabled" id="isGigyaEnabled"/>
	<product:reviewComments/>
	</c:if>
	 
	<%-- End Gigya Rating & Reviews --%>
</div>


<div class="add-to-wishlist-container">
<form>
	<input type="hidden" value="${product.code}" id="product" />

				

					<div id="wishListDetailsId" class="other-sellers" style="display: none">
					<h2 class="title-popover">Select Wishlist:</h2>
						<table class="other-sellers-table add-to-wishlist-popover-table">
							<%-- <thead>
								<tr>
									<th><spring:theme code="product.wishlist"/></th>
									<th><spring:theme code="product.select"/></th>
								</tr>
							</thead> --%>

							<tbody id="wishlistTbodyId">
							</tbody>
						</table>

						 <input type="hidden" name="hidWishlist" id="hidWishlist">
						<span id="addedMessage" style="display:none"></span>
						<input type="hidden" name="alreadyAddedWlName_pdp" id="alreadyAddedWlName_pdp">
						<p id='wishlistErrorId_pdp' style="display: none ; color:red ;"> </p>
						<button type='button' onclick="addToWishlist($('#alreadyAddedWlName_pdp').val())" name='saveToWishlist' id='saveToWishlist' class="savetowishlistbutton"><spring:theme code="product.wishlistBt"/></button>
					</div>

					<div id="wishListNonLoggedInId" style="display: none"><spring:theme code="product.wishListNonLoggedIn"/></div>
                   
				
				
			
			<!-- /.modal-content -->
		
		<!-- /.modal-dialog -->

	<!-- /.modal -->

</form>
</div>



</div>
</div>
</div>
</div>

