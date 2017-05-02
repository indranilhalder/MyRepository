<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="cartData" required="true" type="de.hybris.platform.commercefacades.order.data.CartData" %>
<%@ attribute name="showDeliveryAddress" required="true" type="java.lang.Boolean" %>
<%@ attribute name="showPotentialPromotions" required="false" type="java.lang.Boolean" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>

<!-- TPR-629 orderData added to tag parameters -->
<%@ attribute name="isCart" required="false" type="java.lang.Boolean" %>
<%@ attribute name="orderData" required="false" type="de.hybris.platform.commercefacades.order.data.OrderData" %>

<style>
	.cart.wrapper .right-block .block.bottom.order-details, .confirmation .right-block .block.bottom.order-details span {
		padding-left: 10px;
	}
	.rightBlockSubHeading {
		font-weight: 600;
	}
/* 	.cart.wrapper .right-block .block.bottom.order-details > ul li.items-shipped-count {
		margin-left: 0px !important;
	} */
</style>

<c:choose>
<c:when test="${isCart eq true}">
<c:set var="hasShippedItems" value="${cartData.deliveryItemsQuantity > 0}" />
<c:set var="deliveryAddress" value="${cartData.deliveryAddress}"/>
<c:set var="deliveryAddressLine2" value="${fn:trim(deliveryAddress.line2)}"/>
<c:set var="deliveryAddressLine3" value="${fn:trim(deliveryAddress.line3)}"/>

<c:if test="${not hasShippedItems}">
	<%-- <spring:theme code="checkout.pickup.no.delivery.required"/> --%>
	<span class="rightBlockSubHeading">Collect In-Store</span>
</c:if>

<c:if test="${hasShippedItems}">

		<c:choose>
			<c:when test="${showDeliveryAddress and not empty deliveryAddress}">
			<!-- <h2>Delivery Details</h2> -->
				<p><spring:theme code="checkout.pickup.items.to.be.shipped" text="Shipping Address"/></p>
				<address>
				<span>
					<b>${fn:escapeXml(deliveryAddress.title)}${fn:escapeXml(deliveryAddress.firstName)}&nbsp;${fn:escapeXml(deliveryAddress.lastName)}</b>
					<!-- TISUATSE-69 starts -->
					<c:if test="${ empty deliveryAddressLine2  && empty deliveryAddressLine3}">
						${fn:escapeXml(deliveryAddress.line1)},
					</c:if>
					<c:if test="${ not empty deliveryAddressLine2 && empty deliveryAddressLine3}">
						${fn:escapeXml(deliveryAddress.line1)}${fn:escapeXml(deliveryAddress.line2)},
					</c:if>
					<c:if test="${empty deliveryAddressLine2 && not empty deliveryAddressLine3 }">
						${fn:escapeXml(deliveryAddress.line1)}${fn:escapeXml(deliveryAddress.line3)},
					</c:if>
					<c:if test="${not empty deliveryAddressLine2 && not empty deliveryAddressLine3 }">
						${fn:escapeXml(deliveryAddress.line1)}${fn:escapeXml(deliveryAddress.line2)}${fn:escapeXml(deliveryAddress.line3)},
					</c:if>
					<!-- TISUATSE-69 ends -->
					<!-- <br> -->
					<c:if test="${ not empty deliveryAddress.landmark && deliveryAddress.landmark != 'NA'}">
						<br>
						${fn:escapeXml(deliveryAddress.landmark)},
					</c:if>
					<br>
					<c:if test="${not empty deliveryAddress.town }">
						${fn:escapeXml(deliveryAddress.town)},&nbsp;
					</c:if>
					<c:if test="${ not empty deliveryAddress.region.name }">
						${fn:escapeXml(deliveryAddress.region.name)},&nbsp;
					</c:if>
					<br>
					<c:if test="${ not empty deliveryAddress.state }">
						${fn:escapeXml(deliveryAddress.state)},&nbsp;
					</c:if>
					<c:if test="${ not empty deliveryAddress.postalCode }">
						${fn:escapeXml(deliveryAddress.postalCode)}&nbsp;
					</c:if>
					<c:if test="${ not empty deliveryAddress.country.name }">
						${fn:escapeXml(deliveryAddress.country.isocode)}
					</c:if>
					<br>
					<c:if test="${ not empty deliveryAddress.phone }">
					  <spring:theme code="checkout.phone.no" text="+91"/>&nbsp;${fn:escapeXml(deliveryAddress.phone)}&nbsp;
					</c:if>
					<br>
					<!-- For TISST-11525 -->
					<c:if test="${ not empty deliveryAddress.addressType }">
					  <spring:theme code="checkout.pickup.addressType" text="Address Type:"/>&nbsp;
					  <c:if test="${deliveryAddress.addressType eq 'Home'}">
					  	<spring:theme code="checkout.pickup.addressType" text="Residential"/>&nbsp;
					  </c:if>
					   <c:if test="${deliveryAddress.addressType eq 'Office'}">
					  	<spring:theme code="checkout.pickup.addressType" text="Commercial"/>&nbsp;
					  </c:if>
					</c:if>
					<!-- For TISST-11525 Ends -->
					
					</span>
				</address>
			</c:when>
			<c:otherwise>
				<div class="alternatetitle"><spring:theme code="checkout.pickup.items.to.be.delivered" /></div>
			</c:otherwise>
		</c:choose>

</c:if>
<hr>
<ul class="prd-info">
<li class="items-shipped-count item-adds">
<c:if test="${ not empty cartData.totalUnitCount }">
<%-- 	${cartData.totalUnitCount}&nbsp;<spring:theme code="basket.page.totalQtyForAddress"/> --%>
		<c:if test="${not hasShippedItems}">
			<c:if test="${cartData.totalUnitCount > 0}">${cartData.totalUnitCount}&nbsp;<%-- <spring:theme code="basket.page.totalQtyForAddress.items" /> --%> Item(s) pickup from below store</c:if>
			<%-- <c:if test="${cartData.totalUnitCount <= 1}">${cartData.totalUnitCount}&nbsp;<spring:theme code="basket.page.totalQtyForAddress.item" /></c:if> --%>
		</c:if>
		<c:if test="${hasShippedItems}">
			<c:if test="${cartData.totalUnitCount > 0}">${cartData.deliveryItemsQuantity}&nbsp;<spring:theme code="basket.page.totalQtyForAddress.items" /></c:if>
		<%-- 	<c:if test="${cartData.totalUnitCount <= 1}">${cartData.totalUnitCount}&nbsp;<spring:theme code="basket.page.totalQtyForAddress.item" /></c:if> > --%>
		</c:if>
</c:if>
</li>
<c:forEach items="${cartData.entries}" var="entry">
	<c:if test="${entry.deliveryPointOfService == null}">
		<c:url value="${entry.product.url}" var="productUrl"/>
		<li class="item">
			<div class="product-img">
				<c:choose>
						<c:when test="${fn:toLowerCase(entry.product.luxIndicator)=='luxury'}">
												<a href="${productUrl}"> <product:productPrimaryImage
														product="${entry.product}" format="luxuryCartIcon" />
												</a>
																	</c:when>
																	<c:otherwise>
																			<a href="${productUrl}"> <product:productPrimaryImage
														product="${entry.product}" format="thumbnail" />
												</a>
												
						</c:otherwise>
					</c:choose>
			</div>
			<div class="product">
                  <p class="company"></p>
                  <h3 class="product-brand-name"><a href="${entryProductUrl}">${entry.product.brand.brandname}</a></h3>
                  <h3 class="product-name"><a href="${productUrl}">${entry.product.name}</a></h3>
                  <div class="prd-rate">
                  <p class="qty"><spring:theme code="basket.page.qty"/>&nbsp;${entry.quantity}</p>
                  <!--TISPRO-536-->     
				  <c:if test="${not empty entry.product.size}">
						<p class="size"><spring:theme code="text.size"/>&nbsp;${entry.product.size}</p>
				  </c:if>
                  <%-- <p class="delivery-price"><format:price priceData="${entry.totalPrice}" displayFreeForZero="true"/></p> --%>
                    
                   <!-- TISST-7955, 13538,  TISBOX-1719  -->
                   <p id="${entry.selectedUssid}_${entry.giveAway}_productPriceId" class="delivery-price">
                  	<c:choose>
                  	<c:when test="${entry.totalPrice.value<'1.00'}">
						<span>Free</span>
					</c:when>
					<c:otherwise>
						<span> <c:choose>
											<c:when test="${not empty entry.totalSalePrice}">
												<format:price priceData="${entry.totalSalePrice}"
													displayFreeForZero="true" />
													</c:when>
													<c:otherwise>
													<format:price priceData="${entry.totalPrice}"
													displayFreeForZero="true" />
													</c:otherwise>
													</c:choose></span>
					</c:otherwise>
					</c:choose>
                  </p>

                    </div>
                   

                    
                  <%--   <div class="method">
                   <h3> <spring:theme code="checkout.multi.shipmentMethod"/></h3>
                   <p class="delivery-method-description"><c:out value="${entry.mplDeliveryMode.name}"></c:out>&nbsp;-&nbsp;<c:if test="${entry.currDelCharge.value.unscaledValue() == 0}"><c:out value="FREE"></c:out></c:if><c:if test="${entry.currDelCharge.value.unscaledValue() != 0}"><c:out value="${entry.currDelCharge.formattedValue}"></c:out></c:if></p>
                  <p class="delivery-method-description delivery-method-description-time">
                  
                  <c:out value="${entry.mplDeliveryMode.description}"></c:out>
        
                  </p>
                  </div> --%>
                
      
                  <!-- <div class="method">
                    <h3>Shipping Method:</h3>
                    <p>Home Delivery - Free</p>
                    <p class="delivery-est">Delivered in 3-5 business days</p>
                  </div> -->
                  <div class="variants">

					<c:forEach items="${entry.product.baseOptions}" var="option">
						<c:if test="${not empty option.selected and option.selected.url eq entry.product.url}">
							<c:forEach items="${option.selected.variantOptionQualifiers}" var="selectedOption">
								<div>${selectedOption.name}: ${selectedOption.value}</div>
							</c:forEach>
						</c:if>
					</c:forEach> 
					
					<c:if test="${ycommerce:doesPotentialPromotionExistForOrderEntry(cartData, entry.entryNumber) && showPotentialPromotions && (entry.isBOGOapplied || entry.giveAway)}">
						<ul>
							<c:forEach items="${cartData.potentialProductPromotions}" var="promotion">
								<c:set var="displayed" value="false"/>
								<c:forEach items="${promotion.consumedEntries}" var="consumedEntry">
									<c:if test="${not displayed && consumedEntry.orderEntryNumber == entry.entryNumber}">
										<c:set var="displayed" value="true"/>
										<span>${promotion.description}</span>
									</c:if>
								</c:forEach>
							</c:forEach>
						</ul>
					</c:if>
					
					
					<c:if test="${ycommerce:doesAppliedPromotionExistForOrderEntry(cartData, entry.entryNumber) && (entry.isBOGOapplied || entry.giveAway) }">
						<ul>
							<c:forEach items="${cartData.appliedProductPromotions}" var="promotion">
								<c:set var="displayed" value="false"/>
								<c:forEach items="${promotion.consumedEntries}" var="consumedEntry">
									<c:if test="${not displayed && consumedEntry.orderEntryNumber == entry.entryNumber}">
										<c:set var="displayed" value="true"/>
										<span>${promotion.description}</span>
									</c:if>
								</c:forEach>
							</c:forEach>
						</ul>
					</c:if>
				</div>
				</div>
				</li>
				<li>
                    <div class="method item-mthd">
                   <h3> <spring:theme code="checkout.multi.shipmentMethod"/></h3>
                   <p class="delivery-method-description"><c:out value="${entry.mplDeliveryMode.name}"></c:out>&nbsp;-&nbsp;<c:if test="${entry.currDelCharge.value.unscaledValue() == 0}"><c:out value="FREE"></c:out></c:if><c:if test="${entry.currDelCharge.value.unscaledValue() != 0}"><c:out value="${entry.currDelCharge.formattedValue}"></c:out></c:if></p>
                  <p class="delivery-method-description delivery-method-description-time">
                  
                 <%--  <c:out value="${entry.mplDeliveryMode.description}"></c:out> --%>
        
                  </p>
                  
                  <c:choose>
                       <c:when test="${not empty entry.timeSlotFrom  && entry.timeSlotFrom !=null }">
                         <p class="delivery-method-description delivery-method-description-time">
                         Delivered ON ${entry.selectedDeliverySlotDate} Between  ${entry.timeSlotFrom} TO ${entry.timeSlotTo}
                        </p>
                       </c:when>
                       <c:otherwise>
                       <p class="delivery-method-description delivery-method-description-time">
                         Your Order Will Be Delivered   ${entry.eddDateBetWeen} 
                        </p>
                       
                       </c:otherwise>
                  
                  </c:choose>
                  </div>
             </li>
                  <!-- <div class="method">
                    <h3>Shipping Method:</h3>
                    <p>Home Delivery - Free</p>
                    <p class="delivery-est">Delivered in 3-5 business days</p>
                  </div> -->
                  
                
			
			
			
			<!-- TISSQAUAT-4062 starts --> 
				<!-- </div> -->
			<!-- TISSQAUAT-4062 ends -->
			<%-- <div class="price"><format:price priceData="${entry.basePrice}" displayFreeForZero="true"/></div>
			<div class="details">
				<div class="name"><a href="${productUrl}">${entry.product.name}</a></div>
				<div class="qty"><spring:theme code="basket.page.qty"/>&nbsp;${entry.quantity}</div>
				<div class="variants">
					<c:forEach items="${entry.product.baseOptions}" var="option">
						<c:if test="${not empty option.selected and option.selected.url eq entry.product.url}">
							<c:forEach items="${option.selected.variantOptionQualifiers}" var="selectedOption">
								<div>${selectedOption.name}: ${selectedOption.value}</div>
							</c:forEach>
						</c:if>
					</c:forEach>
					
					<c:if test="${ycommerce:doesPotentialPromotionExistForOrderEntry(cartData, entry.entryNumber) && showPotentialPromotions}">
						<ul>
							<c:forEach items="${cartData.potentialProductPromotions}" var="promotion">
								<c:set var="displayed" value="false"/>
								<c:forEach items="${promotion.consumedEntries}" var="consumedEntry">
									<c:if test="${not displayed && consumedEntry.orderEntryNumber == entry.entryNumber}">
										<c:set var="displayed" value="true"/>
										<span>${promotion.description}</span>
									</c:if>
								</c:forEach>
							</c:forEach>
						</ul>
					</c:if>
					
					
					<c:if test="${ycommerce:doesAppliedPromotionExistForOrderEntry(cartData, entry.entryNumber)}">
						<ul>
							<c:forEach items="${cartData.appliedProductPromotions}" var="promotion">
								<c:set var="displayed" value="false"/>
								<c:forEach items="${promotion.consumedEntries}" var="consumedEntry">
									<c:if test="${not displayed && consumedEntry.orderEntryNumber == entry.entryNumber}">
										<c:set var="displayed" value="true"/>
										<span>${promotion.description}</span>
									</c:if>
								</c:forEach>
							</c:forEach>
						</ul>
					</c:if>
				</div>
			</div> --%>
			<!--  <div class="stock-status">Item In Stock</div> -->

		<!-- TISSQAUAT-4062 starts -->
		<!-- </li> -->
		<!-- TISSQAUAT-4062 ends -->
		<%-- <li>
		    <div class="method item-mthd">
                   <h3> <spring:theme code="checkout.multi.shipmentMethod"/></h3>
                   <p class="delivery-method-description">
                   <c:out value="${entry.mplDeliveryMode.name}">
                   </c:out>&nbsp;-&nbsp;<c:if test="${entry.currDelCharge.value.unscaledValue() == 0}">
                   <c:out value="FREE"></c:out></c:if>
                   <c:if test="${entry.currDelCharge.value.unscaledValue() != 0}">
                   <c:out value="${entry.currDelCharge.formattedValue}"></c:out></c:if>
                   </p>
                  <p class="delivery-method-description delivery-method-description-time">
                  
                  <c:out value="${entry.mplDeliveryMode.description}"></c:out>
                  </p>
                   <c:choose>
                       <c:when test="${not empty entry.timeSlotFrom  && entry.timeSlotFrom !=null }">
                         <p class="delivery-method-description delivery-method-description-time">
                         Delivered ON ${entry.selectedDeliverySlotDate} Between  ${entry.timeSlotFrom} TO ${entry.timeSlotTo}
                        </p>
                       </c:when>
                       <c:otherwise>
                       <p class="delivery-method-description delivery-method-description-time">
                         Your Order Will Be Delivered   ${entry.eddDateBetWeen} 
                        </p>
                       
                       </c:otherwise>
                  
                  </c:choose>
                  </div>
		</li> --%>
	</c:if>
</c:forEach>
</ul>
</c:when>
<c:otherwise>
<c:set var="hasShippedItems" value="${orderData.deliveryItemsQuantity > 0}" />
<c:set var="deliveryAddress" value="${orderData.deliveryAddress}"/>
<c:set var="deliveryAddressLine2" value="${fn:trim(deliveryAddress.line2)}"/>
<c:set var="deliveryAddressLine3" value="${fn:trim(deliveryAddress.line3)}"/>

<c:if test="${not hasShippedItems}">
	<span class="rightBlockSubHeading">Collect In-Store</span>
</c:if>

<c:if test="${hasShippedItems}">
	
		<c:choose>
			<c:when test="${showDeliveryAddress and not empty deliveryAddress}">
			<!-- <h2>Delivery Details</h2> -->
				<p><spring:theme code="checkout.pickup.items.to.be.shipped" text="Shipping Address"/></p>
				<address>
					${fn:escapeXml(deliveryAddress.title)}${fn:escapeXml(deliveryAddress.firstName)}&nbsp;${fn:escapeXml(deliveryAddress.lastName)}
					<br>
					<c:if test="${ not empty deliveryAddress.line1 }">
						${fn:escapeXml(deliveryAddress.line1)},&nbsp;
					</c:if>
					<c:if test="${ not empty deliveryAddressLine2 }">
					
						${fn:escapeXml(deliveryAddress.line2)},
					</c:if>
					<c:if test="${ not empty deliveryAddressLine3 }">
					
						${fn:escapeXml(deliveryAddress.line3)},
					</c:if>
					<br>
					<c:if test="${not empty deliveryAddress.town }">
						${fn:escapeXml(deliveryAddress.town)},&nbsp;
					</c:if>
					<c:if test="${ not empty deliveryAddress.region.name }">
						${fn:escapeXml(deliveryAddress.region.name)},&nbsp;
					</c:if>
					<c:if test="${ not empty deliveryAddress.state }">
						${fn:escapeXml(deliveryAddress.state)},&nbsp;
					</c:if>
					<c:if test="${ not empty deliveryAddress.postalCode }">
						${fn:escapeXml(deliveryAddress.postalCode)}&nbsp;
					</c:if>
					<c:if test="${ not empty deliveryAddress.country.name }">
						${fn:escapeXml(deliveryAddress.country.isocode)}
					</c:if>
					<br>
					<c:if test="${ not empty deliveryAddress.phone }">
					  <spring:theme code="checkout.phone.no" text="+91"/>&nbsp;${fn:escapeXml(deliveryAddress.phone)}&nbsp;
					</c:if>
					<br>
					<!-- For TISST-11525 -->
					<c:if test="${ not empty deliveryAddress.addressType }">
					  <spring:theme code="checkout.pickup.addressType" text="Address Type:"/>&nbsp;
					  <c:if test="${deliveryAddress.addressType eq 'Home'}">
					  	<spring:theme code="checkout.pickup.addressType" text="Residential"/>&nbsp;
					  </c:if>
					   <c:if test="${deliveryAddress.addressType eq 'Office'}">
					  	<spring:theme code="checkout.pickup.addressType" text="Commercial"/>&nbsp;
					  </c:if>
					</c:if>
					<!-- For TISST-11525 Ends -->
					
					
				</address>
			</c:when>
			<c:otherwise>
				<div class="alternatetitle"><spring:theme code="checkout.pickup.items.to.be.delivered" /></div>
			</c:otherwise>
		</c:choose>

</c:if>
<ul>
<li class="items-shipped-count">
<c:if test="${ not empty orderData.totalUnitCount }">
		<c:if test="${not hasShippedItems}">
			<c:if test="${orderData.totalUnitCount > 0}">${orderData.totalUnitCount}&nbsp; ITEMS PICKUP FROM BELOW STORE</c:if>
		</c:if>
		<c:if test="${hasShippedItems}">
			<c:if test="${orderData.totalUnitCount > 0}">${orderData.deliveryItemsQuantity}&nbsp;<spring:theme code="basket.page.totalQtyForAddress.items" /></c:if>
		</c:if>
</c:if>
</li>
<c:forEach items="${orderData.entries}" var="entry">
	<c:if test="${entry.deliveryPointOfService == null}">
		<c:url value="${entry.product.url}" var="productUrl"/>
		<li class="item">
			<div class="product-img">
				<a href="${productUrl}">
					<product:productPrimaryImage product="${entry.product}" format="thumbnail"/>
				</a>
			</div>
			<div class="product">
                  <p class="company"></p>
                  <h3 class="product-brand-name"><a href="${entryProductUrl}">${entry.product.brand.brandname}</a></h3>
                  <h3 class="product-name"><a href="${productUrl}">${entry.product.name}</a></h3>
                  <p class="qty"><spring:theme code="basket.page.qty"/>&nbsp;${entry.quantity}</p>
                  <!--TISPRO-536-->     
				  <c:if test="${not empty entry.product.size}">
						<p class="size"><spring:theme code="text.size"/>&nbsp;${entry.product.size}</p>
				  </c:if>
                    
                   <!-- TISST-7955, 13538,  TISBOX-1719  -->
                   <p id="${entry.selectedUssid}_${entry.giveAway}_productPriceId" class="delivery-price">
                  	<c:choose>
                  	<c:when test="${entry.totalPrice.value<'1.00'}">
						<span>Free</span>
					</c:when>
					<c:otherwise>
						<span> <c:choose>
											<c:when test="${not empty entry.totalSalePrice}">
												<format:price priceData="${entry.totalSalePrice}"
													displayFreeForZero="true" />
													</c:when>
													<c:otherwise>
													<format:price priceData="${entry.totalPrice}"
													displayFreeForZero="true" />
													</c:otherwise>
													</c:choose></span>
					</c:otherwise>
					</c:choose>
                  </p>
                    <div class="variants">
					<c:forEach items="${entry.product.baseOptions}" var="option">
						<c:if test="${not empty option.selected and option.selected.url eq entry.product.url}">
							<c:forEach items="${option.selected.variantOptionQualifiers}" var="selectedOption">
								<div>${selectedOption.name}: ${selectedOption.value}</div>
							</c:forEach>
						</c:if>
					</c:forEach> 
					
					<%-- <c:if test="${ycommerce:doesPotentialPromotionExistForOrderEntry(cartData, entry.entryNumber) && showPotentialPromotions && (entry.isBOGOapplied || entry.giveAway)}">
						<ul>
							<c:forEach items="${orderData.potentialProductPromotions}" var="promotion">
								<c:set var="displayed" value="false"/>
								<c:forEach items="${promotion.consumedEntries}" var="consumedEntry">
									<c:if test="${not displayed && consumedEntry.orderEntryNumber == entry.entryNumber}">
										<c:set var="displayed" value="true"/>
										<span>${promotion.description}</span>
									</c:if>
								</c:forEach>
							</c:forEach>
						</ul>
					</c:if> --%>
					
					
					<c:if test="${ycommerce:doesAppliedPromoExistForOrderEntry(orderData, entry.entryNumber) && (entry.isBOGOapplied || entry.giveAway) }">
						<ul>
							<c:forEach items="${orderData.appliedProductPromotions}" var="promotion">
								<c:set var="displayed" value="false"/>
								<c:forEach items="${promotion.consumedEntries}" var="consumedEntry">
									<c:if test="${not displayed && consumedEntry.orderEntryNumber == entry.entryNumber}">
										<c:set var="displayed" value="true"/>
										<span>${promotion.description}</span>
									</c:if>
								</c:forEach>
							</c:forEach>
						</ul>
					</c:if>
				</div>
				</div>
				</li>
				<li>
                    <div class="method item-mthd">
                   <h3> <spring:theme code="checkout.multi.shipmentMethod"/></h3>
                   <p class="delivery-method-description"><c:out value="${entry.mplDeliveryMode.name}"></c:out>&nbsp;-&nbsp;<c:if test="${entry.currDelCharge.value.unscaledValue() == 0}"><c:out value="FREE"></c:out></c:if><c:if test="${entry.currDelCharge.value.unscaledValue() != 0}"><c:out value="${entry.currDelCharge.formattedValue}"></c:out></c:if></p>
                  <p class="delivery-method-description delivery-method-description-time"><c:out value="${entry.mplDeliveryMode.description}"></c:out></p>
                  </div>
                  <!-- <div class="method">
                    <h3>Shipping Method:</h3>
                    <p>Home Delivery - Free</p>
                    <p class="delivery-est">Delivered in 3-5 business days</p>
                  </div> -->
                  
                
			
		</li>
	</c:if>
</c:forEach>
</ul>
</c:otherwise>
</c:choose>
