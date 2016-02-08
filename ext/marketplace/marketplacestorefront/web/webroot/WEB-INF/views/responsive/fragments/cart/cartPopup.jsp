<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>

<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>

<spring:theme code="text.addToCart" var="addToCartText"/>
<spring:theme code="text.popupCartTitle" var="popupCartTitleText"/>
<c:url value="/cart" var="cartUrl"/>
<c:url value="/cart/checkout" var="checkoutUrl"/>

		            
    
					<ul class="my-bag-ul">
						 <c:choose>
						<c:when test="${not empty entries}">		
         
             <c:forEach items="${entries}" var="entry" end="${numberShowing - 1}">
								<c:url value="${entry.product.url}" var="entryProductUrl"/>
							<li class="item">
								<ul>
									<li>
										<div class="product-img">
										<a href="${entryProductUrl}">
											<product:productPrimaryImage product="${entry.product}" format="cartIcon"/>
										</a>
										</div>
										<div class="product">
											<p class="company"></p>
											<h3 class="product-brand-name"><a href="${entryProductUrl}">${entry.product.brand.brandname}</a></h3>
											<%-- <h3 class="product-fullfilment"><spring:theme code="mpl.myBag.fulfillment"></spring:theme> --%>
											<h3 class="product-name"><a href="${entryProductUrl}">${entry.product.name}</a></h3>
																		
							<%-- <c:choose>
							
   							 <c:when test="${entry.product.deliveryFulfillMode eq 'TShip'}">
   							 <a href="${entryProductUrl}"><spring:theme code="product.default.fulfillmentType"></spring:theme></a>
        					</c:when>
    						<c:otherwise>
        					${entry.product.deliveryFulfillMode}
   							 </c:otherwise>
							</c:choose> --%>
							
										</h3>											
											<p class="item-info">
												<%-- <span><spring:theme code="cart.popup.fulfilled" /></span> --%>
											</p>
										</div>
										<ul class="item-edit-details">
											<li><spring:theme code="cart.popup.quantity" /> ${entry.quantity}</li>
											<c:if test="${not empty entry.product.size}">
											<li><spring:theme code="cart.popup.size" />&nbsp;${entry.product.size}</li>
											</c:if>
											<c:if test="${entry.giveAway ne true}">
											<li><a href="#nogo" class="removeFromCart" data-entry-no="${entry.entryNumber}" data-ussid="${entry.selectedUssid}"><spring:theme code="text.iconCartRemove" /></a></li>
											</c:if>
										</ul>
									</li>
									<c:if test="${entry.giveAway ne true}">
									<%-- <li class="price"><format:price priceData="${entry.basePrice}"/></li> --%>									
									<li class="price"><format:price priceData="${entry.totalPrice}"/></li>
									
									</c:if>
									<c:if test="${entry.giveAway eq true}">
									<li class="price">Free</li>
									</c:if>
								</ul>
							</li>
							</c:forEach>
							 
							<li><c:if test="${numberItemsInCart > numberShowing}">
							
							<div class="more-items">${numberItemsInCart -numberShowing} 
							
							<c:if test="${numberItemsInCart -numberShowing > 1}">&nbsp;<spring:theme code="cart.popup.more.items" /></c:if>
							<c:if test="${numberItemsInCart -numberShowing == 1}">&nbsp;<spring:theme code="cart.popup.more.item" /></c:if>
							</div>
							</c:if>
							<a href="${cartUrl}" class="go-to-bag mini-cart-checkout-button">
							<spring:theme code="mpl.minicart.myBag" /></a>
							</li>       
        
						</c:when>
         <c:otherwise>
        <div class="emptyMiniCart"><spring:message code="popup.cart.empty"/>  </div>     
</c:otherwise>
</c:choose>
						
						</ul>
	
		

<!-- OLD MINI CART  -->

<%-- <div class="mini-cart js-mini-cart">
	<ycommerce:testId code="mini-cart-popup">
		<div class="mini-cart-body">
			<c:choose>
				<c:when test="${numberShowing > 0 }">
						<div class="legend">
							<spring:theme code="popup.cart.showing" arguments="${numberShowing},${numberItemsInCart}"/>
							<c:if test="${numberItemsInCart > numberShowing}">
								<a href="${cartUrl}"><spring:theme code="popup.cart.showall"/></a>
							</c:if>
						</div>

						<ol class="mini-cart-list">
							<c:forEach items="${entries}" var="entry" end="${numberShowing - 1}">
								<c:url value="${entry.product.url}" var="entryProductUrl"/>
								
								
								<li class="mini-cart-item">
									<div class="thumb">
										<a href="${entryProductUrl}">
											<product:productPrimaryImage product="${entry.product}" format="cartIcon"/>
										</a>
									</div>
									<div class="price"><format:price priceData="${entry.basePrice}"/></div>
									<div class="details">
										<a class="name" href="${entryProductUrl}">${entry.product.name}</a>
										<div class="name"><spring:theme code="popup.cart.quantity"/>: ${entry.quantity}</div>
									<div class="size"><b>Size: ${entry.product.size}</b></div>
										<div class="color"><b>Color:${entry.product.colour}</b></div>
									 <input type="hidden" name="entryNumber" value="${entry.entryNumber}"/>
										<a class="name" href="" id="removeFromCart" onclick="removeFromCart(${entry.entryNumber});">Remove</a>
										<c:forEach items="${entry.product.baseOptions}" var="baseOptions">
											<c:forEach items="${baseOptions.selected.variantOptionQualifiers}" var="baseOptionQualifier">
												<c:if test="${baseOptionQualifier.qualifier eq 'style' and not empty baseOptionQualifier.image.url}">
													<div class="itemColor">
														<span class="label"><spring:theme code="product.variants.colour"/></span>
														<img src="${baseOptionQualifier.image.url}" alt="${baseOptionQualifier.value}" title="${baseOptionQualifier.value}"/>
													</div>
												</c:if>
												<c:if test="${baseOptionQualifier.qualifier eq 'size'}">
													<div class="itemSize">
														<span class="label"><spring:theme code="product.variants.size"/></span>
															${baseOptionQualifier.value}
													</div>
												</c:if>
											</c:forEach>
										</c:forEach>
										<c:if test="${not empty entry.deliveryPointOfService.name}">
											<div class="itemPickup"><span class="itemPickupLabel"><spring:theme code="popup.cart.pickup"/></span>&nbsp;${entry.deliveryPointOfService.name}</div>
										</c:if>
									</div>
									
								</li>
								<hr/>
							</c:forEach>
							<c:if test="${numberItemsInCart > numberShowing}">
							
							<div style="  background: darkgray;  border: 1px solid;"><span style="  margin-left: 25%;" ><strong>${numberItemsInCart -numberShowing} more items in the bag</strong>	</span></div>
								
							</c:if>
						</ol>

						<c:if test="${not empty lightboxBannerComponent && lightboxBannerComponent.visible}">
							<cms:component component="${lightboxBannerComponent}" evaluateRestriction="true"  />
						</c:if>

						<div class="mini-cart-totals">
							<div class="key"><spring:theme code="popup.cart.total"/></div>
							<div class="value"><format:price priceData="${cartData.totalPrice}"/></div>
						</div>
						<a href="${cartUrl}" class="btn btn-primary btn-block mini-cart-checkout-button">
							<spring:theme code="mpl.minicart.myBag" />
						</a>
						<a href="" class="btn btn-default btn-block js-mini-cart-close-button">
							<spring:theme text="Continue Shopping" code="cart.page.continue"/>
						</a>
				</c:when>

				<c:otherwise>
					<c:if test="${not empty lightboxBannerComponent && lightboxBannerComponent.visible}">
						<cms:component component="${lightboxBannerComponent}" evaluateRestriction="true"  />
					</c:if>

					<button class="btn btn-block" disabled="disabled">
						<spring:theme code="checkout.checkout" />
					</button>
					<a href="" class="btn btn-default btn-block js-mini-cart-close-button">
						<spring:theme text="Continue Shopping" code="cart.page.continue"/>
					</a>
				</c:otherwise>
			</c:choose>
		</div>
	</ycommerce:testId>
</div> --%>


