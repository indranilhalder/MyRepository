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
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
		<div class="checkout-headers">
		<h1 class="title-name">
			<spring:theme code="checkout.multi.deliveryMethod.chooseDeliveryOption"></spring:theme></br>
		</h1>
		<p class="desk-view"><spring:theme code="checkout.multi.deliveryMethod.chooseDeliveryOption.showPincode" text="Showing delivery options for pincode"></spring:theme><span>${defaultPinCode}</span></p>
		</div>
		<%-- <span><spring:theme code="checkout.multi.deliveryMethod.chooseDeliveryOption.forPincode" text="for PINCODE "></spring:theme>&nbsp;${defaultPinCode}</span> TISUAT-4587--%>
		<span style="display:none"></span>
		<%-- <div class="checkout-shipping-items-header">	
			<h3> <a href="${request.contextPath}/cart"><spring:theme code="checkout.multi.deliveryMethod.backToBag" text="Back to My Bag"></spring:theme> </a> </h3>
		</div> --%>
		<ul id="deliveryradioul" class="checkout-table product-block mybag-items checkout-items">
				<li class="header">
					<ul class="headline mybag-item-head">
					
						<li id="header2" class="Product"><spring:theme code="text.product.delivery.product"/></li>
						<li id="header2" class="Price"><spring:theme code="text.product.delivery.price"/></li>
						<li id="header2" class="qty"><spring:theme code="text.product.delivery.quantity"/></li>
						<li class="delivery dev" id="header4"><spring:theme code="text.product.delivery.deliveryoption"/></li>
					</ul>
				</li>
				
						<c:forEach items="${cartData.entries}" var="entry">
								<c:url value="${entry.product.url}" var="productUrl" />
				
								<li class="item delivery_options">
								<ul>
									<li>
										<div >
											<div class="thumb product-img">
												<c:choose>
													<c:when test="${fn:toLowerCase(entry.product.luxIndicator)=='luxury'}">
															<a href="${productUrl}"><product:productPrimaryImage
																	product="${entry.product}" format="luxuryCartIcon" /></a>
													</c:when>
													<c:otherwise>
															<a href="${productUrl}"><product:productPrimaryImage
																	product="${entry.product}" format="thumbnail" /></a>
															
													</c:otherwise>
												</c:choose>
											</div>
													   
													   
											<div class="details product" >
												<h3 class="product-brand-name"><a href="${entryProductUrl}">${entry.product.brand.brandname}</a></h3>
												<ycommerce:testId code="cart_product_name">
													<a href="${productUrl}"><div class="name product-name">${entry.product.name}</div></a>
												</ycommerce:testId>
												
												<!-- start TISEE-4631 TISUAT-4229 -->
												
												 <c:if test="${fn:toUpperCase(entry.product.rootCategory) != 'ELECTRONICS'}">
												 	
												 	<ycommerce:testId code="cart_product_size">
														<div class="size"><spring:theme code="text.size"/>${entry.product.size}</div>
													</ycommerce:testId>
													 <%--  <div class="item-price delivery-price delivery-price-mobile">
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
											
										</div> --%>
													<ycommerce:testId code="cart_product_colour">
																<%-- <div class="colour"><spring:theme code="text.colour"/>${entry.product.colour}</div> --%>
													</ycommerce:testId>
												 </c:if>
												<!-- end TISEE-4631 TISUAT-4229 -->
												
												<ycommerce:testId code="cart_product_colour">
													<%-- <div class="colour"><spring:theme code="text.seller.name"/>	${entry.selectedSellerInformation.sellername}</div> --%>
												</ycommerce:testId>
												
												<c:forEach items="${fullfillmentData}" var="fullfillmentData">
													<c:if test="${fullfillmentData.key == entry.entryNumber}">
														<c:set var="fulfilmentValue" value="${fn:toLowerCase(fullfillmentData.value)}"> </c:set>
														<c:choose>
															<c:when test="${fulfilmentValue eq 'tship'}">
																	<%-- <div class="colour">
																		Fulfilled By : <spring:theme code="product.default.fulfillmentType"></spring:theme>
																	</div> --%>
															</c:when>
															<c:otherwise>
																	<%-- <div class="colour">
																		Fulfilled By : ${entry.selectedSellerInformation.sellername} 
																	</div> --%>	
															</c:otherwise>
														</c:choose>
													</c:if>
												</c:forEach>
												
												<%-- <ycommerce:testId code="cart_product_colour">
												<div class="colour"><spring:theme code="text.qty"/>${entry.quantity}</div>
												</ycommerce:testId> --%>
															
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
															<div><span><spring:theme code="text.seller.name"/> </span>${sellerInfoMapValue}</div>
														</c:forEach>
														
														<div class="size"><spring:theme code="text.size"/>${entry.product.size}</div>
												<div class="colour"><spring:theme code="text.colour"/>${entry.product.colour}</div>
													</c:if>
												</c:forEach>
											</c:if>
											<%-- <div class="item-price delivery-price">
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
											
										</div>  --%>

											</div>
											
										</div>
										
										<%-- <div class="item-price delivery-price">
											<ycommerce:testId code="cart_totalProductPrice_label">
												<format:price priceData="${entry.totalPrice}"
													displayFreeForZero="true" />
											</ycommerce:testId>
											
										</div>  --%>
									</li>
								    <li class="price">
								    
								    <div class="item-price delivery-price">
											<ycommerce:testId code="cart_totalProductPrice_label">
											<c:choose>
											<c:when test="${not empty entry.netSellingPrice}">
												<format:price priceData="${entry.netSellingPrice}"
													displayFreeForZero="true" />
													</c:when>
													<c:otherwise>
													<format:price priceData="${entry.totalPrice}"
													displayFreeForZero="true" />
													</c:otherwise>
													</c:choose>
											</ycommerce:testId>
											
										</div>
								    
								    
								    </li>
								    
								    <li class="quantity">
								      <div>
								     <ycommerce:testId code="cart_product_colour">
												<div class="colour"> <spring:theme code="text.qty"/>${entry.quantity}</div>
									</ycommerce:testId>
								     </div>
								     
								     
								      
									
									
									
									
									
									
									
									
									
									
									<li class="delivery">
										<ul>
										<c:if test="${not empty deliveryModeData}">
												<c:forEach items="${deliveryModeData}" var="deliveryModeDataMap">
													
													<c:if test="${deliveryModeDataMap.key == entry.entryNumber}">
													
															<c:set var='count'  value='1' />
														<c:set var="delModes" value="${fn:length(deliveryModeDataMap.value)}" />	
														<c:forEach var="i" begin="1" end="${delModes}" step="1">
														    <c:set var="delMode" value="${deliveryModeDataMap.value[delModes-i]}" />
															
														   <c:if test="${count==1}">
																<form:input type="hidden" path="deliveryMethodEntry[${entry.entryNumber}].sellerArticleSKU" value="${delMode.sellerArticleSKU}" />
																<form:input id="radio_${entry.entryNumber}" type="hidden" path="deliveryMethodEntry[${entry.entryNumber}].deliveryCode" value="${delMode.code}" />
															</c:if>
														   <c:set var='count'  value='${count+1}' />
															
															<c:choose>
																	<c:when test="${delMode.code eq 'home-delivery'}">
																			<li class="${delMode.code }">
																			<%-- PRDI-378 FIX STARTS HERE --%>
																			<c:choose>
																			   <c:when test="${entry.isBOGOapplied}">	
																			     <c:set var='bogoQualifyingCount'  value='${entry.qualifyingCount}' />
																			     <c:set var='bogoActualCount'  value='${entry.qualifyingCount-entry.freeCount}' />	
																			     <c:set var='factorCount'  value= '${bogoActualCount/bogoQualifyingCount}' />		
																			     <c:set var='price'  value= '${delMode.deliveryCost.value*factorCount}' />	
																			          <c:choose>
																			              <c:when test="${price > 0}" >
																			                   <input type="radio"  name="${entry.entryNumber}" value="${delMode.deliveryCost.value}" id="radio_${entry.entryNumber}_${delMode.code}" onclick="return calculateDeliveryCost('radio_${entry.entryNumber}','${delMode.code}');"   checked="checked"/>
																			                   <label class="deliveryModeLabel" for="radio_${entry.entryNumber}_${delMode.code }" >${delMode.name } -  ${cartData.currencySymbol}<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${price}" /></label>
																			              </c:when>
																			              <c:otherwise>
																			                   <input type="radio"  name="${entry.entryNumber}" value="${delMode.deliveryCost.value}" id="radio_${entry.entryNumber}_${delMode.code}" onclick="return calculateDeliveryCost('radio_${entry.entryNumber}','${delMode.code}');"   checked="checked"/>
																			                   <label class="deliveryModeLabel" for="radio_${entry.entryNumber}_${delMode.code }" >${delMode.name } -  FREE</label>
																			              </c:otherwise>	
																			         </c:choose>	
																			   </c:when>
																			<%-- PRDI-378 ENDS HERE --%>																			
																			   <c:otherwise>
																			<input type="radio"  name="${entry.entryNumber}" value="${delMode.deliveryCost.value}" id="radio_${entry.entryNumber}_${delMode.code}" onclick="return calculateDeliveryCost('radio_${entry.entryNumber}','${delMode.code}');"   checked="checked"/>
																			<label class="deliveryModeLabel" for="radio_${entry.entryNumber}_${delMode.code }" >${delMode.name } -  <format:price priceData="${delMode.deliveryCost}" displayFreeForZero="TRUE"/></label>
																			   </c:otherwise>																			
																			</c:choose>
																		<span>	${delMode.description }.</span></li>																					
																	</c:when>																	
																	<c:otherwise>
																			<li class="${delMode.code }">
																			<%-- PRDI-378 FIX STARTS HERE --%>
																			<c:choose>
																			   <c:when test="${entry.isBOGOapplied}">	
																			     <c:set var='bogoQualifyingCount'  value='${entry.qualifyingCount}' />
																			     <c:set var='bogoActualCount'  value='${entry.qualifyingCount-entry.freeCount}' />	
																			     <c:set var='factorCount'  value= '${bogoActualCount/bogoQualifyingCount}' />		
																			     <c:set var='price'  value= '${delMode.deliveryCost.value*factorCount}' />	
																			          <c:choose>
																			              <c:when test="${price > 0}" >
																			                   <input type="radio" name="${entry.entryNumber}" value="${delMode.deliveryCost.value}" id="radio_${entry.entryNumber}_${delMode.code }" onclick="return calculateDeliveryCost('radio_${entry.entryNumber}','${delMode.code}');"  />
																			                    <label class="deliveryModeLabel" for="radio_${entry.entryNumber}_${delMode.code }" >${delMode.name } -  ${cartData.currencySymbol}<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${price}" /></label>
																			              </c:when>
																			              <c:otherwise>
																			                    <input type="radio" name="${entry.entryNumber}" value="${delMode.deliveryCost.value}" id="radio_${entry.entryNumber}_${delMode.code }" onclick="return calculateDeliveryCost('radio_${entry.entryNumber}','${delMode.code}');"  />
																			                    <label class="deliveryModeLabel" for="radio_${entry.entryNumber}_${delMode.code }" >${delMode.name } -  FREE</label>
																			              </c:otherwise>	
																			         </c:choose>	
																			   </c:when>
																			    <%-- PRDI-378 ENDS HERE --%>
																			   <c:otherwise>
																			        <input type="radio"   name="${entry.entryNumber}"  value="${delMode.deliveryCost.value}" id="radio_${entry.entryNumber}_${delMode.code }" onclick="return calculateDeliveryCost('radio_${entry.entryNumber}','${delMode.code}');"  />
																			        <label class="deliveryModeLabel" for="radio_${entry.entryNumber}_${delMode.code }" >${delMode.name } -  <format:price priceData="${delMode.deliveryCost}" displayFreeForZero="TRUE"/></label>
																			   </c:otherwise>
																			</c:choose>
																		<span>${delMode.description }.</span> </li>
																			
																	</c:otherwise>
															</c:choose>
									
														</c:forEach>	
														<%-- <c:forEach items="${deliveryModeDataMap.value}"	var="deliveryMode">
													
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
									
											
												</c:forEach> --%>
												</c:if>
												</c:forEach>
												</c:if>
										</ul>
									</li>
									</ul>
								</li>
							</c:forEach>
			</ul>
		
	</div>
	</c:if>
