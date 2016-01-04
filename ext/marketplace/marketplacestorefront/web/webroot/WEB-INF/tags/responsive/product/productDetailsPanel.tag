<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript" src="http://apis.google.com/js/plusone.js"></script>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<style type="text/css">
tr.d0 td {
  background-color:#E0E0E0 ;
  color: black;
}
</style>

 <script type="text/javascript">
      (function() {
       var po = document.createElement('script'); po.type = 'text/javascript'; po.async = true;
       po.src = 'https://apis.google.com/js/client:plusone.js';
       var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(po, s);
     })();
    
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





<div class="pdp">
	<div class="product-info wrapper">
		<div class="product-image-container">
			<cms:pageSlot position="ConfigureImagesCount" var="component">
				<cms:component component="${component}" />
			</cms:pageSlot>
			<product:productImagePanel galleryImages="${galleryImages}"
				product="${product}" />

			<input id="emiCuttOffAmount" type="hidden" value="${emiCuttOffAmount}"/>
				<!-- EMI section -->
				<product:emiDetail product="${product}" />
			
			<!-- promotion  section -->
			<product:productPromotionSection product="${product}" />

		</div>
		<div class="product-detail">
			<ycommerce:testId
				code="productDetails_productNamePrice_label_${product.code}">
				<h2 class="company">${product.brand.brandname} by <span id="sellerNameId"></span></h2>
				<h3 class="product-name">${product.productTitle}</h3>
				
			</ycommerce:testId>

			<ycommerce:testId
				code="productDetails_productNamePrice_label_${product.code}">
				<product:productPricePanel product="${product}" />
			</ycommerce:testId>
			
			<div class="fullfilled-by">
			<spring:theme code="mpl.pdp.fulfillment"></spring:theme>
			<span id="fulFilledByTship" style="display:none;"><spring:theme code="product.default.fulfillmentType"></spring:theme></span>
			<span id="fulFilledBySship"  style="display:none;"></span>
			</div>
			
			<%-- <div class="description">${product.summary}</div> --%>

		
			<div class="tabs-block">
				<product:productPageTabs />
			</div>
			<!-- seller information section  -->
			<div class="seller-details">
			<product:sellerInfoDetailsSection/>
			</div>

			

		</div>

		<div class="product-content">
			<div class="swatch">
				<%-- <cms:pageSlot position="VariantSelector" var="component">
					<cms:component component="${component}" />
				</cms:pageSlot> --%>
				
				<product:productMainVariant /> 
				<cms:pageSlot position="AddToCart" var="component">
					<cms:component component="${component}" />
				</cms:pageSlot>

			</div>

			<span id="defaultWishId" style="display:none"><spring:theme code="wishlist.defaultname"/></span>
			<span id="wishlistSuccess" style="display:none"><spring:theme code="wishlist.success"/></span>
			<span id="wishlistnotblank" style="display:none"><spring:theme code="wishlist.notblank"/></span>
			<span id="sharepretext" style="display:none"><spring:theme code="share.pretext"/></span>
			<span id="shareposttext" style="display:none"><spring:theme code="share.posttext"/></span>
			
			<ul class="wish-share">
				<li><!-- <span id="addedMessage" style="display:none"></span> -->
				<a onClick="openPop();" id="wishlist" class="wishlist" data-toggle="popover" data-placement="bottom"><spring:theme code="text.add.to.wishlist"/></a></li>
				<li>
				<product:socialSharing product="${product}" />
					
				</li>
			</ul>
	
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
			<cms:pageSlot position="PinCodeService" var="component">
				<cms:component component="${component}" />
			</cms:pageSlot>

		</div>


	</div>
	
	<c:set var="electronics"><spring:theme code='product.electronics'/></c:set>
	<c:set var="clothing"><spring:theme code='product.clothing'/></c:set>
	<!-- For Infinite Analytics Start -->
	<input type="hidden" value="${productCategoryType}" id="categoryType"/>
<c:choose>
		<c:when test="${productCategoryType==clothing}">
			<div class="trending"  id="ia_products_complements"></div>
			<div class="trending"  id="ia_products"></div>
		</c:when>
		<c:when test="${productCategoryType==electronics||productCategoryType=='Footwear'}">
			<div class="trending"  id="ia_products_bought_together"></div>
			<div class="trending"  id="ia_products_similar"></div>
		</c:when>
</c:choose>
<!-- For Infinite Analytics End -->


<!--- START: INSERTED for MSD --->
<br/><br/>
<c:choose>
<c:when test="${product.rootCategory==clothing}">
<div class="view-similar-items" id="view-similar-items"></div>
</c:when>
<c:otherwise>
</c:otherwise> 
</c:choose>	
<!--- END:MSD ---> 
	
<c:choose>
<c:when test="${product.rootCategory==electronics}">
<product:productDetailsClassifications product="${product}"/>
</c:when>
<c:otherwise>
</c:otherwise> 
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
	<product:reviewComments/>
	</c:if>
	 
	<%-- End Gigya Rating & Reviews --%>
</div>


<div class="add-to-wishlist-container">
<form>
	<input type="hidden" value="${product.code}" id="product" />

				

					<div id="wishListDetailsId" class="other-sellers" style="display: none">
					<h3 class="title-popover">Select Wishlist:</h3>
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
						
						<button type='button' onclick="addToWishlist()" name='saveToWishlist' id='saveToWishlist' class="savetowishlistbutton"><spring:theme code="product.wishlistBt"/></button>
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


