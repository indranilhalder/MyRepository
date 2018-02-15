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
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
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
<!-- For KIDSWEAR L4 is being added if present -->
<c:if test="${not empty page_subcategory_name_l4}">
<input type="hidden" id="page_subcategory_name_l4" value="${page_subcategory_name_l4}" />
</c:if>
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
<input type="hidden" id="browser_type" value='${browser_type}'/>
<!-- TPR-429 END-->
<!-- For Data Layer Schema changes -->
<input type="hidden" id="product_stock_count" value="${product_stock_count}" />
<input type="hidden" id="out_of_stock" value="${out_of_stock}" />
<input type="hidden" id="product_discount" value="${product_discount}" />
<input type="hidden" id="product_discount_percentage" value="${product_discount_percentage}" />

<!-- End Tealium -->

<!-- TISPRM-56 -->
<input type="hidden" id="product_allVariantsListingId" value="${allVariantsString}"/>


<!-- UF-160 -->
<input type="hidden" id="isLargeAppliance" value="${isLargeAppliance}" />

<!-- CKD:TPR-250 -->
<input type="hidden" id="msiteBuyBoxSellerId" value="${msiteBuyBoxSellerId}"/> 


<div itemscope itemtype="http://schema.org/Product" class="pdp">
	<div class="product-info wrapper">
<%-- 		<div class="product-image-container ${product.rootCategory}"> --%>
		<div class="product-image-container ${product.rootCategory}" id="pdp_gallery">
			<cms:pageSlot position="ConfigureImagesCount" var="component">
				<cms:component component="${component}" />
			</cms:pageSlot>
			<product:productImagePanel galleryImages="${galleryImages}"
				product="${product}" />

				<%-- <input id="emiCuttOffAmount" type="hidden" value="${emiCuttOffAmount}"/>
				<!-- EMI section -->
				<product:emiDetail product="${product}" /> --%>
			<span id="productPromotionSection"><!-- UF-60 wrapping product:productPromotionSection in a span -->
			<!-- promotion  section -->
			<product:productPromotionSection product="${product}" />
			</span>
		</div>
		<!-- Added for carousel in mobile view -->
		<div class="product-image-container ${product.rootCategory} device">
		<a class="wishlist-icon" onclick="addToWishlist()"></a>
		<c:set var="thumbNailImageLengthDevice" value="${fn:length(galleryImages)}" />
			<div class="jcarousel-skin imageListCarousel" id="pdpProductCarousel"> 
			<!-- TISJEWST-15 empty checking for galleryImages -->
				<c:choose>
					<c:when test="${not empty galleryImages}">
					<c:forEach items="${galleryImages}" var="container"
						varStatus="varStatus" begin="0" end="${thumbNailImageLengthDevice}">
						<div id="addiImageTab${varStatus.index}">
							<span> 
								<c:if test="${container.thumbnail.mediaType.code eq 'Image'}">
								<img src="${container.product.url}" data-type="image" data-zoomimagesrc="${container.superZoom.url}"  data-primaryimagesrc="${container.product.url}" data-galleryposition="${varStatus.index}" alt="${container.thumbnail.altText}" title="${container.thumbnail.altText}" />	
							</c:if>
							<c:if test="${container.thumbnail.mediaType.code eq 'Video'}">
							<img src="${commonResourcePath}/images/video-play.png"  data-type="video" data-videosrc="${container.thumbnail.url}?rel=0&enablejsapi=1" />
							<%-- <iframe src="${commonResourcePath}/images/video-play.png"  data-type="video" data-videosrc="${container.thumbnail.url}?rel=0&enablejsapi=1" id="player"></iframe> --%>
							</c:if>
							</span>
						</div>
					</c:forEach>
					</c:when>
					<c:otherwise>
						<theme:image code="img.missingProductImage.product" alt="${fn:escapeXml(product.name)}" title="${fn:escapeXml(product.name)}"/>
					</c:otherwise>
				</c:choose>
			</div>
			<!-- start change for INC144314454 -->
			<div class="modal fade" id="videoModal1" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  				<div class="overlay" data-dismiss="modal" onclick="closing()"></div>
      				<div class="modal-content content"  style="width:53%; height:60%; overflow:hidden;">
           			<button type="button" class="close pull-right" onclick="closingVideo()" aria-hidden="true" data-dismiss="modal"  style="width: 15px; height: 15px; top:0; right:0px;">     TISPRO-508
            		</button>
					<iframe name="videoFrame" id="player" width="100%" height="100%" frameborder="0" allowfullscreen ></iframe>
      			</div>
			</div>
			<style>
			#videoModal1 .content > .close:before {
			    content: "\00d7";
			    color: #fff;
			    font-family: "Montserrat";
			    font-size: 17px;
			    font-weight: 600;
			    -webkit-transition: font-weight 0.15s;
			    -moz-transition: font-weight 0.15s;
			    transition: font-weight 0.15s;
			}
			</style>
			<!-- end change for INC144314454 -->
		</div>
		
		<div class="wishAddSucess">	<!-- add style for TISCSS-166 -->
			<span style="display: none;"><spring:theme code="mpl.pdp.wishlistSuccess"></spring:theme></span>
		</div>
		
		<div class="wishRemoveSucess">	<!-- add style for TISCSS-166 -->
			<span style="display: none;"><spring:theme code="mpl.pdp.wishlistRemoveSuccess"></spring:theme></span>
		</div>
		
		<div class="wishAddLogin">	<!-- add style for TISCSS-166 -->
			<span  style="display: none;"><spring:theme code="product.wishListNonLoggedIn"></spring:theme></span>
		</div>
		<div class="wishAlreadyAdded">	<!-- add style for TISCSS-166 -->
			<span style="display: none;"><spring:theme code="mpl.pdp.wishlistAlreadyAdded"></spring:theme></span>
		</div>

		<div class="product-detail ${product.rootCategory}">
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
				<!-- CKD:TPR-250-Start-->
				<c:set var="clickableBrandname" value="${msiteBrandName}"/>
				<c:set var="clickableBrandCode" value="${msiteBrandCode}"/>
				<c:choose>
					<c:when test="${not empty pdpBrandURL}">
						<h3 itemprop="brand" itemscope itemtype="http://schema.org/Organization" class="company"><span itemprop="name"><a href="${pdpBrandURL}">${product.brand.brandname}</a></span></h3>
					</c:when>
					<c:otherwise>
						<h3 itemprop="brand" itemscope itemtype="http://schema.org/Organization" class="company"><span itemprop="name">${product.brand.brandname}</span></h3>
					</c:otherwise>
				</c:choose>
				<!-- CKD:TPR-250-End-->
				<%-- <a itemprop="url" href="${mainurl}"> --%>		<!-- Commented as part of UF-181 -->
				<!-- For TPR-4358 -->
				<span id="url" itemprop="url" style="display: none">${mainurl}</span> <!-- TISSQAEE-301 -->
				<h1 itemprop="name" class="product-name">${product.productTitle}</h1>
				<meta itemprop="sku" content="${product_sku}"/>
				<!-- </a> -->
				<!-- //TPR-3752 Jewel Feature Attribute Added -->
				<c:choose>
					<c:when test="${product.rootCategory=='FineJewellery' || product.rootCategory=='FashionJewellery' }">
						<div class="product-desc">
							<span class="key-label">
							 <c:forEach var="classification" items="${mapConfigurableAttributes}">
							 <c:if test="${not empty classification.value }">
   						 		<c:forEach var="classValue" items="${classification.value }">
   						 			<p id="jwlryTitle">${classValue.key} ${classValue.value}</p>
   						 			 </c:forEach>
   						 			 <span class="more-link">More</span>
							  </c:if> 
								</c:forEach>
							</span> 
						</div>
					</c:when>
				</c:choose>
			</ycommerce:testId>

			<ycommerce:testId
				code="productDetails_productNamePrice_label_${product.code}">
				<product:productPricePanel product="${product}" />
			</ycommerce:testId>
			
			<input id="emiCuttOffAmount" type="hidden" value="${emiCuttOffAmount}"/>
				<!-- EMI section -->
			
			<product:emiDetail product="${product}" />
			<!-- TPR-6907 -->
			<div id= "codEli"><a id="codLink" href=""><spring:theme code="cod.available"/></a></div>
			<!-- TPR-6907 -->				
			<!-- TPR-1083 Exchange Start -->
			<input id="l3category" type="hidden" value="${product.level3CategoryCode}"/>
 			<!-- Exchange section -->
 		
 			<product:exchangeDetails product="${product}" />
			<!-- TPR-1083 Exchange Ends -->
			<!-- TISPRM-97 starts -->
				<!-- TPR-772 starts -->
			<div class="pdp-promo-block promo-block" style="display:none">
			<!-- TPR-772 ends -->
			<c:if test="${not empty product.potentialPromotions}">
			
			<c:choose>
				<c:when test="${not empty product.potentialPromotions[0].channels}">
				
				<c:forEach var="channel"
							items="${product.potentialPromotions[0].channels}">
				<c:if test="${channel eq 'Web'||channel eq ''||channel==null}">	
			<%-- <div class="pdp-promo-title pdp-title">
				<b>OFFER:</b> ${product.potentialPromotions[0].title}
				<!-- <a class="details">View more</a> --> <!-- commented for TPR-589  --> <!-- commented for CAR-327  -->
			</div> --%>
			</c:if> <!-- end if check for channel web -->
			</c:forEach>
			</c:when>
			
			<c:otherwise>
			<%-- <div class="pdp-promo-title pdp-title">
				<b>OFFER:</b> ${product.potentialPromotions[0].title}
			<!-- 	<a class="details">View more</a> --><!-- commented for TPR-589  --> <!-- commented for CAR-327  -->
			</div> --%>
			</c:otherwise>
			</c:choose>			
			</c:if>
			</div>
			
			<!--  Added for displaying offer messages other than promotion, TPR-589 -->
			<!--INC144313502-->
				 <div>
					<a class="pdp-promo-title-link" style="display:none">View more</a>
				</div>	
				
			<!-- TISPRM-97 ends -->
			<!-- TPR-275 starts  -->
			<spring:eval expression="T(de.hybris.platform.util.Config).getParameter('freebiePriceThreshold')" var="freebiePriceThreshVal"/>
	        <input type="hidden" id="freebiePriceThreshId" value="${freebiePriceThreshVal}">
	
			<div id="freebieProductMsgId" style="display:none">
			 <spring:theme code="freebie.product.message" text="Freebie: This product is not on sale" ></spring:theme>				
			</div>			
			<!-- TPR-275 ends -->
			<product:productMainVariant /> 
			<cms:pageSlot position="AddToCart" var="component">
					<cms:component component="${component}" />
				</cms:pageSlot>
				
			<div class="sellerMonogramming"></div>	
			<div class="SoldWrap">
				<%-- <ycommerce:testId
					code="productDetails_productNamePrice_label_${product.code}">
					<div class="seller">Sold by <span id="sellerNameId"></span></div>
				</ycommerce:testId> --%>
				<div class="seller">
					<ycommerce:testId code="productDetails_productNamePrice_label_${product.code}">
						<span class="seller-details"><span>Sold by <span id="sellerNameId"></span><product:sellerInfoDetailsSection /></span></span>
					</ycommerce:testId>
					
				</div>
				<div class="fullfilled-by">
				<spring:theme code="mpl.pdp.fulfillment"></spring:theme>&nbsp;<span id="fulFilledByTship" style="display:none;"><spring:theme code="product.default.fulfillmentType"></spring:theme></span>
				<span id="fulFilledBySship"  style="display:none;"></span>
				</div>
			</div>
			
			<c:if test="${isGigyaEnabled=='Y'}">
				<ul class="star-review" id="pdp_rating">
					<li class="empty"></li>
					<li class="empty"></li>
					<li class="empty"></li>
					<li class="empty"></li>
					<li class="empty"></li>
				<%-- 	<span class="gig-rating-readReviewsLink_pdp"> <spring:theme
							code="rating.noreviews" /></span> --%>
							<span class="gig-rating-readReviewsLink_pdp"></span>			<!-- UF-29 -->
					<!-- OOTB Code Commented to facilitate Rest Call -->
					<%-- <c:choose>
				<c:when test="${not empty product.ratingCount}">
					<a href="">${product.ratingCount} <spring:theme code="text.account.reviews"/></a> 
				</c:when>
				<c:otherwise>
					<span><spring:theme code="text.no.reviews"/></span>
					 
				</c:otherwise>
			</c:choose>  --%>
				</ul>
			</c:if>
			<%-- <cms:pageSlot position="AddToCart" var="component">
					<cms:component component="${component}" />
				</cms:pageSlot> --%>
			<%-- <div class="description">${product.summary}</div> --%>

		
			
			<!-- seller information section  -->
			<%-- <div class="seller-details">
			<product:sellerInfoDetailsSection/>
			</div> --%>


			<!-- seller information section  -->
			<%-- <div class="seller-details">
			<product:sellerInfoDetailsSection/>
			</div> --%>

            <!-- BLOCK ADDED FOR JEWELLERY CERTIFICATION STARTS HERE-->
            <c:if test="${product.rootCategory =='FineJewellery' && not empty certificationfeatureValueDataList}">
             <div class="certified-by"> 
              <h2>Certified by:</h2>
              <ul>
               <!-- <li><img src="images/certified-by.jpg" alt="certified by"></li>
               <li>30 day returns</li>
               <li>tata guarantee</li> -->
            <c:forEach var="certification" items="${certificationfeatureValueDataList}">
              <c:if test="${certification.value=='AGL'}">
                 <%-- <li class="jwlryCerti"><img src="${commonResourcePath}/images/Visa.png" alt="certified by"></li> --%>
                 <li class="jwlryCertiAGL"></li>
             </c:if>
             <c:if test="${certification.value=='AGS'}">
                <%--  <img src="${commonResourcePath}/images/American_Express.png" alt="certified by"> --%>
                <li class="jwlryCertiAGS"></li>
            </c:if>
             <c:if test="${certification.value=='AGTA'}">
                 <%-- <img src="${commonResourcePath}/images/Maestro.png" alt="certified by"> --%>
                 <li class="jwlryCertiAGTA"></li>         
            </c:if>
             <c:if test="${certification.value=='HRD'}">  
                <%--  <img src="${commonResourcePath}/images/Discover.png" alt="certified by">  --%>
                <li class="jwlryCertiHRD"></li>  
            </c:if>
            <c:if test="${certification.value=='EGL'}">  
                <%--  <img src="${commonResourcePath}/images/Discover.png" alt="certified by">  --%>
                <li class="jwlryCertiEGL"></li>  
            </c:if>
            <c:if test="${certification.value=='GII'}">  
                <%--  <img src="${commonResourcePath}/images/Discover.png" alt="certified by">  --%>
                <li class="jwlryCertiGII"></li>  
            </c:if>
            <c:if test="${certification.value=='Gubelin'}">  
                <%--  <img src="${commonResourcePath}/images/Discover.png" alt="certified by">  --%>
                <li class="jwlryCertiGubelin"></li>  
            </c:if>
            <c:if test="${certification.value=='IDL'}">  
                <%--  <img src="${commonResourcePath}/images/Discover.png" alt="certified by">  --%>
                <li class="jwlryCertiIDL"></li>  
            </c:if>
            <c:if test="${certification.value=='Tanishq'}">  
                <%--  <img src="${commonResourcePath}/images/Master_Card.png" alt="certified by">   --%>
                <li class="jwlryCertiTanishq"></li>
             </c:if>
             <c:if test="${certification.value=='IGI'}">  
                <%--  <img src="${commonResourcePath}/images/Discover.png" alt="certified by">  --%>
                <li class="jwlryCertiIGI"></li>  
            </c:if>
             <c:if test="${certification.value=='BIS'}">  
                <%--  <img src="${commonResourcePath}/images/Discover.png" alt="certified by">  --%>
                <li class="jwlryCertiBIS"></li>  
            </c:if>
            <c:if test="${certification.value=='SGL'}">  
                <%--  <img src="${commonResourcePath}/images/Discover.png" alt="certified by">  --%>
                <li class="jwlryCertiSGL"></li>  
            </c:if>
             <c:if test="${certification.value=='GIA'}">  
                <%--  <img src="${commonResourcePath}/images/Discover.png" alt="certified by">  --%>
                <li class="jwlryCertiGIA"></li>  
            </c:if>
            <c:if test="${certification.value=='PGI'}">  
                <%--  <img src="${commonResourcePath}/images/Discover.png" alt="certified by">  --%>
                <li class="jwlryCertiPGI"></li>  
            </c:if>
            <c:if test="${certification.value=='GSL'}">  
                <%--  <img src="${commonResourcePath}/images/Discover.png" alt="certified by">  --%>
                <li class="jwlryCertiGSL"></li>  
            </c:if>
            <c:if test="${certification.value=='GSI'}">  
                <%--  <img src="${commonResourcePath}/images/Discover.png" alt="certified by">  --%>
                <li class="jwlryCertiGSI"></li>  
            </c:if>
            <c:if test="${certification.value=='IGL'}">  
                <%--  <img src="${commonResourcePath}/images/Discover.png" alt="certified by">  --%>
                <li class="jwlryCertiIGL"></li>  
            </c:if>
            <c:if test="${certification.value=='DGLA'}">  
                <%--  <img src="${commonResourcePath}/images/Discover.png" alt="certified by">  --%>
                <li class="jwlryCertiDGLA"></li>  
            </c:if>
           </c:forEach>
         </ul>
      </div>
          <!-- <div class="certified-by">
           <ul>
            <li>30 day returns</li>
            <li>tata guarantee</li>
           
           </ul>
          </div> -->
      </c:if>
            <!-- BLOCK ADDED FOR JEWELLERY CERTIFICATION ENDS HERE-->

		</div>

		<div class="product-content ${product.rootCategory}">
			<div class="swatch">
				<%-- <cms:pageSlot position="VariantSelector" var="component">
					<cms:component component="${component}" />
				</cms:pageSlot> --%>
				
				
				
        
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

				<%-- <li><!-- <span id="addedMessage" style="display:none"></span> -->
				<!-- Commented as per PDP CR Change -->
				<a onClick="openPop();" id="wishlist" class="wishlist" data-toggle="popover" data-placement="bottom"><spring:theme code="text.add.to.wishlist"/></a></li> --%>
				<li>
				<product:socialSharing product="${product}" />
					
				</li>
			</ul>
			
           <%-- <!-- BLOCK ADDED FOR JEWELLERY CERTIFICATION STARTS HERE-->
            <c:if test="${product.rootCategory =='FineJewellery'}">
             <div class="certified-by"> 
              <h2>certified by</h2>
              <ul>
               <!-- <li><img src="images/certified-by.jpg" alt="certified by"></li>
               <li>30 day returns</li>
               <li>tata guarantee</li> -->
            <c:forEach var="certification" items="${certificationfeatureValueDataList}">
            <li>
              <c:if test="${certification.value=='AGL'}">
                 <img src="${commonResourcePath}/images/Visa.png" alt="certified by">
             </c:if>
             <c:if test="${certification.value=='Tanishq'}">  
                 <img src="${commonResourcePath}/images/Master_Card.png" alt="certified by">  
            </c:if>
             <c:if test="${certification.value=='AGS'}">
                 <img src="${commonResourcePath}/images/American_Express.png" alt="certified by">
            </c:if>
             <c:if test="${certification.value=='AGTA'}">
                 <img src="${commonResourcePath}/images/Maestro.png" alt="certified by">         
            </c:if>
             <c:if test="${certification.value=='HRD'}">  
                 <img src="${commonResourcePath}/images/Discover.png" alt="certified by">    
            </c:if>
            </li>     
           </c:forEach>
         </ul>
      </div>
          <div class="certified-by">
           <ul>
            <li>30 day returns</li>
            <li>tata guarantee</li>
           
           </ul>
          </div>
      </c:if>
            <!-- BLOCK ADDED FOR JEWELLERY CERTIFICATION ENDS HERE--> --%>
          
		</div>
		<c:if test="${product.rootCategory=='FineJewellery'or product.rootCategory=='FashionJewellery' }">
		 <div class="jewellery-wrapper">
		 </c:if>
		 <div class="tabs-block ${product.rootCategory}">
				<product:productPageTabs product="${product}" />
			</div>
		<!-- CODE MOVED HERE FOR OTHER PRODUCTS APART FROM JEWELLERY TO DISPLAY DETAILS IN TAB STARTS HERE -->
		<%-- <c:set var="finejewellery">
			<spring:theme code='product.finejewellery' />
		</c:set>
		<c:choose>
			<c:when test="${product.rootCategory ne finejewellery}">
				<div class="tabs-block" id="tabsBlock">
					<product:productPageTabs />
				</div>
			</c:when>
		</c:choose> --%>
		<!-- CODE MOVED HERE FOR OTHER PRODUCTS APART FROM JEWELLERY TO DISPLAY DETAILS IN TAB ENDS HERE -->
    <!-- Fine Jewellery Details Tree Section  -->
		<c:if test="${product.rootCategory=='FineJewellery'or product.rootCategory=='FashionJewellery' }">
			<product:productDetailsClassifications product="${product}" />
			</c:if>
			<c:if test="${product.rootCategory=='FineJewellery'or product.rootCategory=='FashionJewellery' }">
		 </div>
		 </c:if>	
	</div>
	
	
	
	<c:set var="electronics"><spring:theme code='product.electronics'/></c:set>
	<c:set var="clothing"><spring:theme code='product.clothing'/></c:set>
	<!-- TISPRO-271 Changes -->
	<c:set var="footwear"><spring:theme code='product.footwear'/></c:set>
	
	<!-- Added for TATAUNISTORE-15 Start -->
	<c:set var="watches"><spring:theme code='product.watches'/></c:set>
	<c:set var="accessories"><spring:theme code='product.fashionAccessories'/></c:set>
	
		
<c:choose>
<c:when test="${product.rootCategory==electronics  || product.rootCategory==watches}">
<product:productDetailsClassifications product="${product}"/>
</c:when>
<c:otherwise>
</c:otherwise> 
</c:choose>
	
	<!-- For Infinite Analytics Start -->
	<input type="hidden" value="${productCategoryType}" id="categoryType"/>
	<!-- changes for UF-238 -->
	
	<div id="productContentDivId">
	<c:if test="${not empty aplusHTML}">${aplusHTML}</c:if>
	</div>
<c:choose>
		<c:when test="${product.rootCategory==clothing || product.rootCategory== footwear || product.rootCategory==accessories || product.rootCategory==finejewellery || product.rootCategory==fashionjewellery}">  <!-- Added for TISPRO-271 -->
			<div class="trending"  id="ia_products_complements"></div>
			<div class="trending"  id="ia_products"></div>
		</c:when>
		<c:when test="${product.rootCategory==electronics  || product.rootCategory==watches}">
			<div class="trending"  id="ia_products_bought_together"></div>
			<!-- Change for INC_10849 -->
			<!-- <div class="trending"  id="ia_products_similar"></div> -->
		</c:when>
</c:choose>
<!-- For Infinite Analytics End -->


<!--- START: INSERTED for MSD --->
	<br /> <br />
	<c:choose>
		<c:when
			test="${product.rootCategory==clothing || product.rootCategory== footwear || product.rootCategory==accessories|| product.rootCategory==finejewellery || product.rootCategory==fashionjewellery}">
			<div class="view-similar-items" id="view-similar-items"></div>
		</c:when>
		<c:otherwise>
		</c:otherwise>
	</c:choose>
	<!--- END:MSD --->
	
<%-- <c:choose>
<c:when test="${product.rootCategory==electronics  || product.rootCategory==watches}">
<product:productDetailsClassifications product="${product}"/>
</c:when>
<c:otherwise>
</c:otherwise> 
</c:choose>	 --%>
 <!-- Change for INC_10849 -->
<c:choose>
		<c:when test="${product.rootCategory==electronics  || product.rootCategory==watches}">
			<div class="trending"  id="ia_products_similar"></div>
		</c:when>
</c:choose>
	
	
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

<div id="stw_widget_hf" class="lazy-reached-stw"></div>
<div id="msd_widget_hf" class="lazy-reached-stw"></div>


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

<script type="text/javascript">
	var prop = '${mapConfigurableAttributes}';
	prop =prop.replace(/[{}]/g, '');
</script>
