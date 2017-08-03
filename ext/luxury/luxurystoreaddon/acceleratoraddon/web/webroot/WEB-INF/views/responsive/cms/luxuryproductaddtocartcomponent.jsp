<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="action" tagdir="/WEB-INF/tags/responsive/action" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

	<div class="qty selectQty">
	
	<!-- Component for product count -->
		<cms:pageSlot position="ConfigureProductsCount" var="component">
			<cms:component component="${component}" />
		</cms:pageSlot>
	</div>
	
	<!-- changes for buy now button: start  -->
<%-- 	<c:if test="${isGigyaEnabled=='Y'}">
 <ul class="star-review" id="pdp_rating">
				<li class="empty"></li>
				<li class="empty"></li>
				<li class="empty"></li>
				<li class="empty"></li>
				<li class="empty"></li>
				<span class="gig-rating-readReviewsLink_pdp"> <spring:theme code="rating.noreviews"/></span>
				<!-- OOTB Code Commented to facilitate Rest Call -->
		<c:choose>
				<c:when test="${not empty product.ratingCount}">
					<a href="">${product.ratingCount} <spring:theme code="text.account.reviews"/></a> 
				</c:when>
				<c:otherwise>
					<span><spring:theme code="text.no.reviews"/></span>
					 
				</c:otherwise>
			</c:choose> 
			</ul>
</c:if> --%>
<div class="Cta">
	<span id="dListedErrorMsg" style="display: none"  class="dlist_message prod-dlisted-msg">
		<spring:theme code="pdp.delisted.message" />
	</span>
	<div id="addToCartFormTitle" class="addToCartTitle prod-dlisted-msg">
	<%-- <spring:theme code="basket.added.to.basket" /> --%>
	<spring:theme code="product.addtocart.success" />
</div>
	<div class="buy-btn-holder clearfix">
	<div id="buyNow" class="col-md-6  mt-10">
		<input type="hidden" id="showSize" name="showSize" value="${showSizeGuideForFA}" />		
		
	        <button  id="" type="button" class="btn-block js-add-to-cart btn btn-primary btn-lg btn-block">
				<spring:theme code="buyNow.button.pdp" />
			</button>
	        </div>
	<!-- changes for buy now button: end  -->

	<!-- <div class="size-guide">Size Guide</div> -->
	<div class="addtocart-component col-md-6">

		<%-- <div class="row addtocart-button">
			<action:actions element="div"  parentComponent="${component}"/>
		</div> --%>
		<c:if test="${empty showAddToCart ? true : showAddToCart}">
		<%-- <div class="qty-selector input-group js-qty-selector">
			<span class="input-group-btn">
				<button class="btn btn-primary js-qty-selector-minus" type="button">-</button>
			</span>
				<input type="text" maxlength="3" class="form-control js-qty-selector-input" size="1" value='1' data-max="${product.stock.stockLevel}" data-min="1" name="pdpAddtoCartInput"  id="pdpAddtoCartInput"  />
			<span class="input-group-btn">
				<button class="btn btn-primary js-qty-selector-plus" type="button">+</button>
			</span>
		</div> --%>

		</c:if>
		<c:if test="${product.stock.stockLevel gt 0}">
			<c:set var="productStockLevel">${product.stock.stockLevel} 
				<spring:theme code="product.variants.in.stock"/>
			</c:set>
		</c:if>
		<c:if test="${product.stock.stockLevelStatus.code eq 'lowStock'}">
			<c:set var="productStockLevel">
				<spring:theme code="product.variants.only.left" arguments="${product.stock.stockLevel}"/>
			</c:set>
		</c:if>
		<c:if test="${product.stock.stockLevelStatus.code eq 'inStock' and empty product.stock.stockLevel}">
			<c:set var="productStockLevel">
				<spring:theme code="product.variants.available"/>
			</c:set>
		</c:if>
		<div class="stock-status">
			${productStockLevel}
		</div>
		<div class="addtocart-button  mt-10">
			<action:actions element="div"  parentComponent="${component}"/>
		</div>
		</div>
		
			<div id="addtocart-popup" style="display:none">
			<span class="tick-icon"><i class="fa fa-check fa-4" aria-hidden="true"></i></span>
			<h3 class="mt-40 mb-40">Added to Bag</h3>
			<ul>
				<li>
				<div class="hidden-lg row">
					<div class="itemsImage col-xs-5 col-sm-3">
								<product:productPrimaryImage product="${product}" format="luxuryProduct" />
					</div>
					
					<div class="itemsDescription col-xs-7 col-sm-9 product">
					<ul>
						<li class="brand_name"><h3><strong>${product.brand.brandname}</strong></h3></li>
						<li class="product_title">${product.productTitle}</li>
						<li class="product_color">${product.colour} - <span class="product_size"> ${product.size} </span></li>
						<li class="product_price">${product.productMRP.currencyIso}${product_list_price}</li>
					</ul>								
				 </div>
			</div>
            </li>
				<li><a href="/cart" class="mb-10 btn btn-gloden btn-lg btn-block lux-cart-btn">VIEW BAG <span></span></a></li>
				<li><a href="/" class="mb-10 btn btn-default btn-lg btn-block lux-keepshop-btn">CONTINUE SHOPPING</a></li>
				<li><a href="/cart" class="btn btn-primary btn-lg btn-block">BUY NOW</a></li> 
			</ul>
		</div>
		<div class="luxury-over-lay" style="display:none"></div>
		
		
	</div>
</div>   