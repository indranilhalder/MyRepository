<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="cartData" required="true" type="de.hybris.platform.commercefacades.order.data.CartData" %>
<%@ attribute name="defaultPincode" required="true" type="java.lang.String" %>
<%@ attribute name="showDeliveryAddress" required="true" type="java.lang.Boolean" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>

<c:set var="hasShippedItems" value="${cartData.deliveryItemsQuantity > 0}" />
<c:set var="deliveryAddress" value="${cartData.deliveryAddress}"/>
<c:set var="firstShippedItem" value="true"></c:set>
<c:set var="defaultPinCode" value="${defaultPincode}"></c:set>

<c:if test="${hasShippedItems}">
<script>
	$(document).ready(function(){
		$(".click-and-collect").addClass("click-collect");
	});
</script>
	<div class="checkout-shipping-items">
		<h1>
			
			<spring:theme code="checkout.multi.deliveryMethod.chooseDeliveryOption" text="Choose Your Delivery Options"></spring:theme></br>
			
			 
		</h1>
		<p><spring:theme code="checkout.multi.deliveryMethod.chooseDeliveryOption.showPincode" text="Showing delivery options for pincode "></spring:theme>&nbsp;<span>${defaultPinCode}</span></p>
		<%-- <span><spring:theme code="checkout.multi.deliveryMethod.chooseDeliveryOption.forPincode" text="for PINCODE "></spring:theme>&nbsp;${defaultPinCode}</span> TISUAT-4587--%>
		<span></span>
		<%-- <div class="checkout-shipping-items-header">	
			<h3> <a href="${request.contextPath}/cart"><spring:theme code="checkout.multi.deliveryMethod.backToBag" text="Back to My Bag"></spring:theme> </a> </h3>
		</div> --%>
		<ul id="deliveryradioul" class="checkout-table product-block">
				<li class="header">
					<ul class="headline">
						<li id="header2"><spring:theme code="text.product.information"/></li>
						<li class="delivery" id="header4"><spring:theme code="text.delivery.modes"/></li>
					</ul>
				</li>
				
						<c:forEach items="${cartData.entries}" var="entry">
								<c:url value="${entry.product.url}" var="productUrl" />
				
								<li class="item delivery_options">
								<ul>
									<li>
										<div >
											<div class="thumb product-img">
												<a href="${productUrl}"><product:productPrimaryImage
														product="${entry.product}" format="thumbnail" /></a>
											</div>
													   
													   
											<div class="details product" >
												<h3 class="product-brand-name"><a href="${entryProductUrl}">${entry.product.brand.brandname}</a></h3>
												<ycommerce:testId code="cart_product_name">
													<a href="${productUrl}"><div class="name product-name">${entry.product.name}</div></a>
												</ycommerce:testId>
												
												<!-- start TISEE-4631 TISUAT-4229 -->
												
												 <c:if test="${fn:toUpperCase(entry.product.rootCategory) != 'ELECTRONICS'}">
												 	
												 	<ycommerce:testId code="cart_product_size">
														<div class="size"><b><spring:theme code="text.size"/>${entry.product.size}</b></div>
													</ycommerce:testId>
													<ycommerce:testId code="cart_product_colour">
																<div class="colour"><b><spring:theme code="text.colour"/>${entry.product.colour}</b></div>
													</ycommerce:testId>
												 </c:if>
												<!-- end TISEE-4631 TISUAT-4229 -->
												
												<ycommerce:testId code="cart_product_colour">
													<div class="colour"><b><spring:theme code="text.seller.name"/>	${entry.selectedSellerInformation.sellername}</b></div>
												</ycommerce:testId>
												
												<c:forEach items="${fullfillmentData}" var="fullfillmentData">
													<c:if test="${fullfillmentData.key == entry.entryNumber}">
														<c:set var="fulfilmentValue" value="${fn:toLowerCase(fullfillmentData.value)}"> </c:set>
														<c:choose>
															<c:when test="${fulfilmentValue eq 'tship'}">
																	<div class="colour">
																		Fulfilled By : <spring:theme code="product.default.fulfillmentType"></spring:theme>
																	</div>
															</c:when>
															<c:otherwise>
																	<div class="colour">
																		Fulfilled By : ${entry.selectedSellerInformation.sellername} 
																	</div>	
															</c:otherwise>
														</c:choose>
													</c:if>
												</c:forEach>
												
												<ycommerce:testId code="cart_product_colour">
												<div class="colour"><b><spring:theme code="text.qty"/>${entry.quantity}</b></div>
												</ycommerce:testId>
															
												<c:if
													test="${ycommerce:doesPotentialPromotionExistForOrderEntry(cartData, entry.entryNumber)}">
													<c:forEach items="${cartData.potentialProductPromotions}"
														var="promotion">
														<c:set var="displayed" value="false" />
														<c:forEach items="${promotion.consumedEntries}"
															var="consumedEntry">
															<c:if
																test="${not displayed && consumedEntry.orderEntryNumber == entry.entryNumber && not empty promotion.description}">
																<c:set var="displayed" value="true" />
																
																<!--  Commented to remove promotional description 24-aug-15
																<div class="promo">
																	<ycommerce:testId code="cart_potentialPromotion_label">
				                                           				  ${promotion.description}
				                                         			</ycommerce:testId>
																</div>
																 -->
															</c:if>
														</c:forEach>
													</c:forEach>
												</c:if>
												<c:if
													test="${ycommerce:doesAppliedPromotionExistForOrderEntry(cartData, entry.entryNumber)}">
													<c:forEach items="${cartData.appliedProductPromotions}"
														var="promotion">
														<c:set var="displayed" value="false" />
														<c:forEach items="${promotion.consumedEntries}"
															var="consumedEntry">
															<c:if
																test="${not displayed && consumedEntry.orderEntryNumber == entry.entryNumber}">
																<c:set var="displayed" value="true" />
																<!--  Commented to remove promotional description 24-aug-15
																<div class="promo">
																	<ycommerce:testId code="cart_appliedPromotion_label">
			                                          					  ${promotion.description}
			                                        				</ycommerce:testId>
																</div>
																-->
															</c:if>
														</c:forEach>
													</c:forEach>
												</c:if>
		
												<c:set var="entryStock"
													value="${entry.product.stock.stockLevelStatus.code}" />
		
												<c:forEach items="${entry.product.baseOptions}" var="option">
													<c:if
														test="${not empty option.selected and option.selected.url eq entry.product.url}">
														<c:forEach
															items="${option.selected.variantOptionQualifiers}"
															var="selectedOption">
															<div>
																<strong>${selectedOption.name}:</strong> <span>${selectedOption.value}</span>
															</div>
															<c:set var="entryStock"
																value="${option.selected.stock.stockLevelStatus.code}" />
														</c:forEach>
													</c:if>
												</c:forEach>
		
											<c:if
												test="${not empty sellerInfoMap}">
												<c:forEach items="${sellerInfoMap}"
													var="sellerInfoMap">
													<c:if
														test="${sellerInfoMap.key == entry.entryNumber}">
														<c:forEach items="${sellerInfoMap.value}"
															var="sellerInfoMapValue"> 
															<div><span><b><spring:theme code="text.seller.name"/> </b></span>${sellerInfoMapValue}</div>
														</c:forEach>
														
														<div class="size"><b><spring:theme code="text.size"/>${entry.product.size}</b></div>
												<div class="colour"><b><spring:theme code="text.colour"/>${entry.product.colour}</b></div>
													</c:if>
												</c:forEach>
											</c:if>
											<div class="item-price delivery-price">
											<ycommerce:testId code="cart_totalProductPrice_label">
											<c:choose>
											<c:when test="${not empty entry.totalSalePrice}">
												<format:price priceData="${entry.totalSalePrice}"
													displayFreeForZero="true" />
													</c:when>
													<c:otherwise>
													<format:price priceData="${entry.totalPrice}"
													displayFreeForZero="true" />
													</c:otherwise>
													</c:choose>
											</ycommerce:testId>
											
										</div> 

											</div>
											
										</div>
										
										<%-- <div class="item-price delivery-price">
											<ycommerce:testId code="cart_totalProductPrice_label">
												<format:price priceData="${entry.totalPrice}"
													displayFreeForZero="true" />
											</ycommerce:testId>
											
										</div>  --%>
									</li>
								
									<li class="delivery">
										<ul>
										<c:if test="${not empty deliveryModeData}">
												<c:forEach items="${deliveryModeData}" var="deliveryModeDataMap">
													
													<c:if test="${deliveryModeDataMap.key == entry.entryNumber}">
													
															<c:set var='count'  value='1' />
															
														<c:forEach items="${deliveryModeDataMap.value}"	var="deliveryMode">
													
															<c:if test="${count==1}">
																<form:input type="hidden" path="deliveryMethodEntry[${entry.entryNumber}].sellerArticleSKU" value="${deliveryMode.sellerArticleSKU}" />
																<form:input id="radio_${entry.entryNumber}" type="hidden" path="deliveryMethodEntry[${entry.entryNumber}].deliveryCode" value="${deliveryMode.code}" />
															</c:if>
															
															<c:set var='count'  value='${count+1}' />
															
															<c:choose>
																	<c:when test="${deliveryMode.code eq 'home-delivery'}">
																			<li class="${deliveryMode.code }">
																			<input type="radio"  name="${entry.entryNumber}" value="${deliveryMode.deliveryCost.value}" id="radio_${entry.entryNumber}_${deliveryMode.code}" onclick="return calculateDeliveryCost('radio_${entry.entryNumber}','${deliveryMode.code}');"   checked="checked"/>
																			<label class="deliveryModeLabel" for="radio_${entry.entryNumber}_${deliveryMode.code }" >${deliveryMode.name } -  <format:price priceData="${deliveryMode.deliveryCost}" displayFreeForZero="TRUE"/></label>
																			
																		<span>	${deliveryMode.description }</span></li>
																					
																	</c:when>
																	<c:otherwise>
																			<li class="${deliveryMode.code }">
																			<input type="radio"   name="${entry.entryNumber}"  value="${deliveryMode.deliveryCost.value}" id="radio_${entry.entryNumber}_${deliveryMode.code }" onclick="return calculateDeliveryCost('radio_${entry.entryNumber}','${deliveryMode.code}');"  />
																			<label class="deliveryModeLabel" for="radio_${entry.entryNumber}_${deliveryMode.code }" >${deliveryMode.name } -  <format:price priceData="${deliveryMode.deliveryCost}" displayFreeForZero="TRUE"/></label>
																			
																		<span>${deliveryMode.description }</span> </li>
																			
																	</c:otherwise>
															</c:choose>
									
											
												</c:forEach>
												</c:if>
												</c:forEach>
												</c:if>
										</ul>
									</li>
									</ul>
								</li>
							</c:forEach>
			</ul>

		<!--	
		<c:if test="${showDeliveryAddress and not empty deliveryAddress}">
			<p>
				${fn:escapeXml(deliveryAddress.title)}&nbsp;${fn:escapeXml(deliveryAddress.firstName)}&nbsp;${fn:escapeXml(deliveryAddress.lastName)}
				<br>
				<c:if test="${ not empty deliveryAddress.line1 }">
					${fn:escapeXml(deliveryAddress.line1)},&nbsp;
				</c:if>
				<c:if test="${ not empty deliveryAddress.line2 }">
					${fn:escapeXml(deliveryAddress.line2)},&nbsp;
				</c:if>
				<c:if test="${not empty deliveryAddress.town }">
					${fn:escapeXml(deliveryAddress.town)},&nbsp;
				</c:if>
				<c:if test="${ not empty deliveryAddress.region.name }">
					${fn:escapeXml(deliveryAddress.region.name)},&nbsp;
				</c:if>
				<c:if test="${ not empty deliveryAddress.state }">
					${fn:escapeXml(deliveryAddress.state)},&nbsp; <!--DSC_006 : Fix for Checkout Address State display issue -->
			<!-- 	</c:if>
				<c:if test="${ not empty deliveryAddress.postalCode }">
					${fn:escapeXml(deliveryAddress.postalCode)},&nbsp;
				</c:if>
				<c:if test="${ not empty deliveryAddress.country.name }">
					${fn:escapeXml(deliveryAddress.country.name)}
				</c:if>
			</p>	
		</c:if>
		<ul> 
			 
		 	<c:forEach items="${cartData.entries}" var="entry">
				<c:if test="${entry.deliveryPointOfService == null}">
					<c:url value="${entry.product.url}" var="productUrl"/>
					<li class="details">
						<span class="name">${entry.product.name}</span> 
						<span class="qty"><spring:theme code="basket.page.qty"/>&nbsp;${entry.quantity}</span>
					</li>
				</c:if>
			</c:forEach>
		
		</ul>
		 -->
		
		
	</div>
	</c:if>


