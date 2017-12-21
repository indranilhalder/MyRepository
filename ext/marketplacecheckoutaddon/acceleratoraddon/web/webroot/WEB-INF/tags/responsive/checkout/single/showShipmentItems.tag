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
<%-- <c:set var="hasShippedItems" value="${cartData.deliveryItemsQuantity > 0}" /> --%>
<c:set var="deliveryAddress" value="${cartData.deliveryAddress}"/>
<c:set var="firstShippedItem" value="true"></c:set>
<c:set var="defaultPinCode" value="${defaultPincode}"></c:set>

<%-- <c:if test="${hasShippedItems}"> --%>
<script>
	/* $(document).ready(function(){
		$(".click-and-collect").addClass("click-collect");
	}); */
</script>
	<!-- UF-281 changes -->
	<div class="checkout-shipping-items">
		<span style="display:none"></span>
		<ul id="deliveryradioul" class="checkout-table product-block mybag-items checkout-items">
						<c:set var="entryNumbersId" value=""/>
						<c:set var="hideChangeLink" value="true"/>
						<c:forEach items="${cartData.entries}" var="entry">
							<c:set var="numberOfDelModesInEntry" value="0"/>
								<c:url value="${entry.product.url}" var="productUrl" />
				
								<li class="item delivery_options" data-entryNumber="${entry.entryNumber}" data-ussid="${entry.selectedSellerInformation.ussid}">
								<ul>
									<li>
											<div class="thumb product-img">
											<!-- Commented as part JWLSPCUAT-38 -->
												<c:choose>
													<c:when test="${fn:toLowerCase(entry.product.luxIndicator)=='luxury'}">
															<a href="${productUrl}"><product:productPrimaryImage lazyLoad="false"
																	product="${entry.product}" format="luxuryCartIcon" /></a>
													</c:when>
													<%-- <c:when test="${fn:toLowerCase(entry.product.luxIndicator)=='marketplace' or empty entry.product.luxIndicator and (entry.product.rootCategory)=='FineJewellery'}">
															<a href="${entryProductUrl}"> <product:productPrimaryImage lazyLoad="false"
																	product="${entry.product}" format="fineJewelcartIcon" />
															</a>
													</c:when> --%>
													<c:otherwise>
															<a href="${productUrl}"><product:productPrimaryImage lazyLoad="false"
																	product="${entry.product}" format="thumbnail" /></a>
															
													</c:otherwise>
												</c:choose>
											</div>
													   
											<div class="delivery-details">		   
											<div class="details product" >
											<p class="delivery_title"><spring:theme code="checkout.single.deliveryMode.selectMode"/></p>
												<p class="product_name"><span class="desk_prod">for "</span><%-- <h3 class="product-brand-name"><a href="${entryProductUrl}">${entry.product.brand.brandname}</a></h3> --%>
												<ycommerce:testId code="cart_product_name">
													<%-- <a href="${productUrl}"><div class="name product-name">${entry.product.name}</div></a> --%>
													<span class="name product-name">${entry.product.name}</span></ycommerce:testId>
												<span class="desk_prod">"</span>
												</p>
												<!-- start TISEE-4631 TISUAT-4229 -->
												
												 <c:if test="${fn:toUpperCase(entry.product.rootCategory) != 'ELECTRONICS'}">
												 	
												 	<ycommerce:testId code="cart_product_size">
														<%-- <div class="size"><spring:theme code="text.size"/>${entry.product.size}</div> --%>
													</ycommerce:testId>
													<!-- INC_11620/INC_11466 -->
													 <!--<div class="item-price delivery-price delivery-price-mobile">
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
														
													   </div>-->
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
		
											<%-- <c:if
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
											</c:if> --%>
											

											</div>
											

									
									<!-- <li class="delivery"> -->
									<div>
										<c:set var='disclaimerIsset'  value='false' />
										<ul class="delivery-modes">
										<c:if test="${not empty deliveryModeData}">
												<c:forEach items="${deliveryModeData}" var="deliveryModeDataMap">
													
													<c:if test="${deliveryModeDataMap.key == entry.entryNumber}">
													
															<c:set var='count'  value='1' />
														<c:set var="delModes" value="${fn:length(deliveryModeDataMap.value)}" />
														<%-- <c:set var='delModeChecked'  value='false' /> --%>
															
														<c:forEach var="i" begin="1" end="${delModes}" step="1">
															<c:set var="numberOfDelModesInEntry" value="${numberOfDelModesInEntry+1}"/>
														    <c:set var="delMode" value="${deliveryModeDataMap.value[delModes-i]}" />
															
															<c:if test="${count==1}">
																<form:input type="hidden" path="deliveryMethodEntry[${entry.entryNumber}].sellerArticleSKU" value="${delMode.sellerArticleSKU}" />
																<form:input id="radio_${entry.entryNumber}" type="hidden" path="deliveryMethodEntry[${entry.entryNumber}].deliveryCode" value="${delMode.defaultSelectedDelMode}" />
																<form:input type="hidden" path="deliveryMethodEntry[${entry.entryNumber}].selectedStore" value="${delMode.storeSelected}" />
															</c:if>
														   <c:set var='count'  value='${count+1}' />

															<c:choose>
																	<c:when test="${delMode.code eq 'home-delivery'}">
																			
																			<li class="${delMode.code } <c:if test="${!delMode.isSelected eq true}"> hideDelModeMobile</c:if>">
																			<input type="radio"  name="${entry.entryNumber}" value="${delMode.deliveryCost.value}" id="radio_${entry.entryNumber}_${delMode.code}" onclick="return calculateDeliveryCost('radio_${entry.entryNumber}','${delMode.code}'); "  <c:if test="${delMode.isSelected eq true}">checked="checked"</c:if>/>
																			<%-- <label class="deliveryModeLabel" for="radio_${entry.entryNumber}_${delMode.code }" >${delMode.name } (<format:price priceData="${delMode.deliveryCost}" displayFreeForZero="TRUE"/>) --%>
																			<!-- UF-306 starts -->
																			<%-- <label class="deliveryModeLabel" for="radio_${entry.entryNumber}_${delMode.code }" ><spring:theme code="text.home.delivery"/> -  <format:price priceData="${delMode.deliveryCost}" displayFreeForZero="TRUE"/></label> --%>
																			<label class="deliveryModeLabel" for="radio_${entry.entryNumber}_${delMode.code }" ><spring:theme code="text.home.delivery"/> (<format:price priceData="${delMode.deliveryCost}" displayFreeForZero="TRUE"/>)
																			<!-- UF-306 ends -->
																			
																		<p>${delMode.description }</p>
																		<c:if test="${!disclaimerIsset}">
																		<p>Shipping Charges subject to change depending on final Order Value</p>
																		<c:set var='disclaimerIsset'  value='true' />
																		</c:if>
																		</label></li>
																		<%-- <c:set var='delModeChecked'  value='true' /> --%>			
																	</c:when>
																	<c:when test="${delMode.code eq 'click-and-collect'}">
																			<li class="${delMode.code } <c:if test="${!delMode.isSelected eq true}"> hideDelModeMobile</c:if>">
																			<input type="radio"  name="${entry.entryNumber}" value="${delMode.deliveryCost.value}" id="radio_${entry.entryNumber}_${delMode.code}" onclick="ACC.singlePageCheckout.fetchStores('${entry.entryNumber}','${delMode.sellerArticleSKU}','${delMode.code }','','');return calculateDeliveryCost('radio_${entry.entryNumber}','${delMode.code}');"  <c:if test="${delMode.isSelected eq true}">checked="checked"</c:if>/>
																			<%-- <label class="deliveryModeLabel" for="radio_${entry.entryNumber}_${delMode.code }" >${delMode.name } (<format:price priceData="${delMode.deliveryCost}" displayFreeForZero="TRUE"/>) --%>
																			<!-- UF-306 starts -->
																			<%-- <label class="deliveryModeLabel" for="radio_${entry.entryNumber}_${delMode.code }" ><spring:theme code="text.clickandcollect.shipping"/> -  <format:price priceData="${delMode.deliveryCost}" displayFreeForZero="TRUE"/></label> --%>
																			<label class="deliveryModeLabel" for="radio_${entry.entryNumber}_${delMode.code }" ><spring:theme code="text.clickandcollect.delivery"/> (<format:price priceData="${delMode.deliveryCost}" displayFreeForZero="TRUE"/>)
																			<!-- UF-306 ends -->
																		<p>${delMode.description }</p>
																		<c:if test="${!disclaimerIsset}">
																		<p>Shipping Charges subject to change depending on final Order Value</p>
																		<c:set var='disclaimerIsset'  value='true' />
																		</c:if>
																		</label></li>
																		<c:set var="isCncPresentInSinglePageCart" value="true"></c:set>
																		<%-- <c:if test="${delMode.isSelected eq true}">
																			<script>
																				$(document).ready(function(){
																					ACC.singlePageCheckout.fetchStores('${entry.entryNumber}','${delMode.sellerArticleSKU}','${delMode.code }','','');
																				});
																			</script>
																		</c:if> --%>
																		<%-- <c:set var='delModeChecked'  value='true' />	 --%>		
																	</c:when>
																	<c:otherwise>
																			<li class="${delMode.code } <c:if test="${!delMode.isSelected eq true}"> hideDelModeMobile</c:if>">
																			<input type="radio"   name="${entry.entryNumber}"  value="${delMode.deliveryCost.value}" id="radio_${entry.entryNumber}_${delMode.code }" onclick="return calculateDeliveryCost('radio_${entry.entryNumber}','${delMode.code}');"   <c:if test="${delMode.isSelected eq true}">checked="checked"</c:if>/>
																			<%-- <label class="deliveryModeLabel" for="radio_${entry.entryNumber}_${delMode.code }" >${delMode.name } (<format:price priceData="${delMode.deliveryCost}" displayFreeForZero="TRUE"/>) --%>
																			<!-- UF-306 starts -->
																			<%-- <label class="deliveryModeLabel" for="radio_${entry.entryNumber}_${delMode.code }" ><spring:theme code="text.express.shipping"/> -  <format:price priceData="${delMode.deliveryCost}" displayFreeForZero="TRUE"/></label> --%>
																			<label class="deliveryModeLabel" for="radio_${entry.entryNumber}_${delMode.code }" ><spring:theme code="text.express.shipping"/> (<format:price priceData="${delMode.deliveryCost}" displayFreeForZero="TRUE"/>)
																			<!-- UF-306 ends -->
																		<p>${delMode.description }</p>
																		<c:if test="${!disclaimerIsset}">
																		<p>Shipping Charges subject to change depending on final Order Value</p>
																		<c:set var='disclaimerIsset'  value='true' />
																		</c:if>
																		</label></li>
																		<%-- <c:set var='delModeChecked'  value='true' /> --%>
																	</c:otherwise>
															</c:choose>
									
														</c:forEach>
														<span id="slotMsgId_${entry.selectedSellerInformation.ussid}" class="preferred-delivery-type" style="display:none"><spring:theme code="checkout.single.deliveryMode.selectSlotDeliveryTime"/></span>
														<c:if test="${numberOfDelModesInEntry>1}">
															<c:set var="hideChangeLink" value="false"/>
														</c:if>
												</c:if>
												</c:forEach>
												</c:if>
										</ul>
										</div>
										</div>
									</li>
									<li class="cnc_store_wrapper">
										<div id="cncStoreContainer${entry.entryNumber}" style="display:none;"></div>
									</li>
									</ul>
								</li>
								<c:set var="entryNumbersId" value="${entry.entryNumber}#${entryNumbersId}"/>

							</c:forEach>
			</ul>
		
	</div>
	<input type="hidden" name="isCncPresentInSinglePageCart" id="isCncPresentInSinglePageCart" value="${isCncPresentInSinglePageCart}"/>
	<input type="hidden" name="entryNumbersId" id="entryNumbersId" value="${entryNumbersId}" />
	<input type="hidden" name="hideChangeLink" id="hideChangeLink" value="${hideChangeLink}" />
<%-- 	</c:if> --%>
