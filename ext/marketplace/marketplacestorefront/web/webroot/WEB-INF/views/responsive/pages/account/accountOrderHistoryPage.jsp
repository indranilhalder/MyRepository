<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="formElement"
	tagdir="/WEB-INF/tags/desktop/formElement"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="order" tagdir="/WEB-INF/tags/responsive/order"%>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/responsive/user" %>

<spring:url value="/my-account/profile" var="profileUrl" />
<spring:url value="/my-account/update-profile" var="updateProfileUrl" />
<spring:url value="/my-account/update-password" var="updatePasswordUrl" />
<spring:url value="/my-account/update-email" var="updateEmailUrl" />
<spring:url value="/my-account/address-book" var="addressBookUrl" />
<spring:url value="/my-account/payment-details" var="paymentDetailsUrl" />
<spring:url value="/my-account/orders" var="ordersUrl" />
<spring:url value="/my-account/default/wishList" var="wishlistUrl" />
<spring:url value="/my-account/friendsInvite" var="friendsInviteUrl" />
<spring:url value="/my-account/order/" var="orderDetailsUrl" />

<spring:eval expression="T(de.hybris.platform.util.Config).getParameter('order.cancel.enabled')" var="cancelFlag"/> 
<spring:eval expression="T(de.hybris.platform.util.Config).getParameter('order.return.enabled')" var="returnFlag"/> 

<style>
#closePop {
	float: right;
}
</style>
<!-- LW-230 -->
<input type="hidden" id="isLuxury" value="${isLuxury}"/>

<template:page pageTitle="${pageTitle}">
	<div class="account">
		<h1 class="account-header">
			<%-- <spring:theme code="text.account.headerTitle" text="My Tata CLiQ" /> --%>
				<user:accountMobileViewMenuDropdown pageNameDropdown="orderHistory"/>
		
			<%-- <select class="menu-select"
				onchange="window.location=this.options[this.selectedIndex].value;">
				<optgroup label="<spring:theme code="header.flyout.myaccount" />">
					<option value=/store/mpl/en/my-account
						/ data-href="/store/mpl/en/my-account/"><spring:theme
							code="header.flyout.overview" /></option>
					<option value=/store/mpl/en/my-account/marketplace-preference
						data-href="/store/mpl/en/my-account/marketplace-preference"><spring:theme
							code="header.flyout.marketplacepreferences" /></option>
					<option value=/store/mpl/en/my-account/update-profile
						data-href="account-info.php"><spring:theme
							code="header.flyout.Personal" />
					</option>
					<option value=/store/mpl/en/my-account/orders
						data-href="order-history.php" selected>
						<spring:theme code="header.flyout.orders" /></option>
					<option value=/store/mpl/en/my-account/payment-details
						data-href="account-cards.php"><spring:theme
							code="header.flyout.cards" /></option>
					<option value=/store/mpl/en/my-account/address-book
						data-href="account-addresses.php"><spring:theme
							code="header.flyout.address" /></option>
							<option value=/store/mpl/en/my-account/reviews
						data-href="account-addresses.php"><spring:theme code="header.flyout.review" /></option>
				</optgroup>

				<optgroup label="Share">
					<option value=/store/mpl/en/my-account/friendsInvite
						data-href="account-invite.php"><spring:theme
							code="header.flyout.invite" /></option>
				</optgroup>
			</select> --%>


		</h1>

		<div class="wrapper">


			<!----- Left Navigation Starts --------->
			
			<user:accountLeftNav pageName="orderHistory"/>
			<!----- Left Navigation ENDS --------->
			<!----- RIGHT Navigation STARTS --------->
			<div class="right-account">
				<div class="order-history">
					<div class="navigation" id=order_pagination>
						<h2>
							<spring:theme text="My Orders" />		<!--  UF-249 text change -->

						</h2>
						<p class="commonAccountPara"><spring:theme text="Track your order status, cancel a product or request a return here." /></p>
						<c:if test="${not empty searchPageData.results}">
						<!-- TISPRO-48 ---- Set values in hidden filed for lazy loading pagination -->
							<input type="hidden" id="pageIndex" value="${pageIndex}" />
							<input type="hidden" id="pagableSize" value="${pageSize}" />
							<input type="hidden" id="totalNumberOfResults"
								value="${searchPageData.pagination.totalNumberOfResults}" />
							<div id="displayPaginationCountUp"></div>
						</c:if>
					</div>

					<c:if test="${not empty searchPageData.results}">
						
						<input type="hidden" id="pageSize" value="${pageSizeInoh}" />
						<input type="hidden" id="orderEntryCount"
							value="${fn:length(orderDataList)}" />
						<%-- <c:if test="${fn:length(orderDataList)>pageSizeInoh}">
							<ul class="pagination orderhistory_address_pagination">
								<li class="address_pagination" id="paginationDiv"></li>

							</ul>
						</c:if> --%>
						
						<!-- TISPRO-48 ---- call mpl-pagination.tag for pagination -->
						<nav:mpl-pagination top="true" supportShowPaged="${isShowPageAllowed}"
							supportShowAll="${isShowAllAllowed}"
							searchPageData="${searchPageData}"
							searchUrl="/my-account/orders?sort=${searchPageData.pagination.sort}"
							msgKey="text.account.orderHistory.page"
							numberPagesShown="${numberPagesShown}" />
						<c:forEach items="${orderDataList}" var="orderHistoryDetail"
							varStatus="status">
							<ul class="pagination_ul">
								<li>
									<%-- <c:set var="formatedDate" value="${orderDataMap.key}" />
							<c:set var="orderHistoryDetail" value="${orderDataMap.value}" /> --%>
									<c:set var="orderNumberMasked"
										value="${orderHistoryDetail.code}" /> <c:set
										var="orderNumberLength"
										value="${fn:length(orderNumberMasked)}" /> <c:set
										var="orderNumEnd"
										value="${fn:substring(orderNumberMasked, orderNumberLength-7, orderNumberLength)}" />

									<ul class="product-block orders-product-block">

										<%-- <li class="header">
											<ul>
												<li class="date"><span><spring:theme
															code="text.orderHistory.order.placed" /></span> <fmt:formatDate
														value="${orderHistoryDetail.created}"
														pattern="MMMMM dd, yyyy" /> ${formatedDate} <c:if
														test="${orderDataMap[orderHistoryDetail.code] ne null}">
													${orderDataMap[orderHistoryDetail.code]}
												</c:if></li>
												<li class="order-total"><span><spring:theme
															code="text.orderHistory.total" /></span>  
												<!-- TISSIT-1773 -->
															<format:price priceData="${orderHistoryDetail.totalPrice}" />
												<c:choose>
													<c:when test="${orderHistoryDetail.net}">
														<format:price priceData="${orderHistoryDetail.totalPriceWithTax}" />
													</c:when>
													<c:otherwise>
														<format:price priceData="${orderHistoryDetail.totalPriceWithConvCharge}" />
													</c:otherwise>
												</c:choose>	
															
															
															</li>
												<li class="recipient"><span><spring:theme
															code="text.orderHistory.recipient" /></span> 
															<c:choose>
														<c:when
															test="${orderHistoryDetail.deliveryAddress != null}">
																${orderHistoryDetail.deliveryAddress.firstName}&nbsp;${orderHistoryDetail.deliveryAddress.lastName}
														</c:when>
														<c:otherwise>
																${orderHistoryDetail.mplPaymentInfo.cardAccountHolderName}
																</c:otherwise>
													</c:choose>
													</li>
												<li class="order-number"><span><spring:theme
															code="text.orderHistory.number" /></span>#${orderHistoryDetail.code}</li>

												<li class="viewDetailsAnchor"><a
													href="${orderDetailsUrl}?orderCode=${orderHistoryDetail.code}&pageAnchor=viewOrder"><spring:theme
															code="text.orderHistory.view.orde" text="Order Details" /></a></li>
															<!-- &pageAnchor=trackOrder -->
												<li class="trackOrderAnchor"><a href="${orderDetailsUrl}?orderCode=${orderHistoryDetail.code}"><spring:theme
															code="text.orderHistory.track.order" /></a></li>
											</ul>
										</li> --%>
										
										<!-- TPR-6013 wrong UI --> <li class="header clearfix">
											<ul>
												<li class="viewDetails">
												<span class="orderNumber"><spring:theme code="text.orderHistory.order.place" text="Order"/>#${orderHistoryDetail.code}</span> 
												<span class="orderDate"><spring:theme code="text.orderHistory.order.place" text="Placed on:"/>&nbsp;
												<c:if test="${orderDataMap[orderHistoryDetail.code] ne null}">
													${orderDataMap[orderHistoryDetail.code]}
												</c:if></span>
												</li>
												<li class="viewDetailsAnchor">
												<c:if test="${orderHistoryDetail.isEGVOrder ne  true}">
												   <a
													href="${orderDetailsUrl}?orderCode=${orderHistoryDetail.code}&pageAnchor=viewOrder"><spring:theme
															code="text.orderHistory.view.orde" text="Order Details" /></a>
												 </c:if>
												 <c:if test="${not empty orderHistoryDetail.egvCardNumber}">
												          Card No: ${orderHistoryDetail.egvCardNumber}
												 </c:if>
											  </li>
															
															
												<li class="resendEmail">
												<c:if test="${not empty orderHistoryDetail.egvCardNumber and orderHistoryDetail.isEGVOrder eq  true}">
													<input type="hidden" class="order_id_for_resending" value="${orderHistoryDetail.code}" />
													<input type="hidden" class="resend_email_index" value="" />
													<!-- <span class="resend_order_email">RESEND EMAIL</span> -->
													<c:set var="evgflag" value="false"/>
													    <c:forEach items="${egvStatusMap}" var="entryforEgv">
																<c:if test="${entryforEgv.key eq orderHistoryDetail.code and entryforEgv.value eq 'REDEEMED'}">
																     <c:set var="evgflag" value="true"/>	
																</c:if>										   
														</c:forEach>
													<c:if test="${evgflag eq  'false'}">
													     <span class="resend_order_email">RESEND EMAIL</span>
													</c:if>
													</c:if>
													
											<c:if test="${orderHistoryDetail.isEGVOrder ne  true}">
												<input type="hidden" class="order_id_for_statement" value="${orderHistoryDetail.code}" />
												<span class="get_order_statement">SHOW STATEMENT</span>
											 </c:if>
													
												</li>
												
															<!-- &pageAnchor=trackOrder -->
												<li class="trackOrderAnchor">
												<c:if test="${orderHistoryDetail.isEGVOrder ne  true}">
												<a href="${orderDetailsUrl}?orderCode=${orderHistoryDetail.code}"><spring:theme
															code="text.orderHistory.track.order" /></a>
											   </c:if>
														<c:if test="${not empty orderHistoryDetail.egvCardExpDate}">
												          Exp. Date: ${orderHistoryDetail.egvCardExpDate}
												        </c:if>	
															
												</li>
											</ul>
											<div class="col-sm-12 alert alert-success resend_email_limit">
												
											</div>
										</li>
										
										<c:forEach items="${orderHistoryDetail.sellerOrderList}"
											var="subOrder" varStatus="status">
											<input type="hidden" id="subOrderCode"
												class="suborderEntryCode" value="${subOrder.code}" />

											<c:forEach items="${subOrder.entries}" var="entry"
												varStatus="entryStatus">
												
												<c:set var="isVolume" value="false"/>
												<c:set var="isWeight" value="false"/>

												<c:url value="${entry.product.url}" var="productUrl" />
												<!-- Egv Product url Changes-->
												  <c:if test="${orderHistoryDetail.isEGVOrder eq  true}">
												  <spring:eval expression="T(de.hybris.platform.util.Config).getParameter('marketplace.header.egvProductCode')" var="productCode"/>
												    <c:set var="myVar" value="/giftCard-" />
												    <c:set var ="egvProduct"  value="${myVar}${productCode}"/>
													<c:url value="${egvProduct}" var="productUrl" />	
												  </c:if>
												  
												<c:set var="orderEntrySellerSKU"
													value="${entry.mplDeliveryMode.sellerArticleSKU}" />
												<c:forEach items="${entry.product.seller}" var="seller">
													<c:if test="${seller.ussid eq orderEntrySellerSKU}">
														<c:set var="sellerId" value="${seller.sellerID}" />
														<c:set var="sellerOrderId" value="${seller.ussid}" />
													</c:if>
												</c:forEach>
												<li class="item">
													<div class="image">
														<c:choose>
															<c:when test="${fn:toLowerCase(entry.product.luxIndicator)=='luxury'}">
																	<a href="${productUrl}"> <product:productPrimaryImage
																					product="${entry.product}" format="luxuryCartIcon" lazyLoad="false" />
																			</a>
										
															</c:when>
															<c:otherwise>
																	<a href="${productUrl}"> <product:productPrimaryImage
																					product="${entry.product}" format="thumbnail" lazyLoad="false" />
																			</a>
																	
															</c:otherwise>
														</c:choose>
													</div>
													<div class="details">
														<h2 class="product-name">${entry.brandName}</h2>
														<h2 class="product-name">
															<a href="${productUrl}">${entry.product.name}</a>
														</h2>
														<!-- TPR-1083 Start-->
														  <c:if test="${not empty entry.exchangeApplied}">
		              			<p class="cart_exchange">

			              		<input type="hidden" id="exc_cart" value="${entry.exchangeApplied}">
			              		<spring:theme code="marketplace.exchange.messageLabel"/>
   										
			              		</p>
			              		<!-- TPR-1083 End-->
			              		</c:if>														


															<p>
																<c:if test="${entry.quantity > 1}">
																	<spring:theme code="text.orderHistory.quantity" />
												&nbsp;${entry.quantity}
												</c:if>
															</p>
															<c:if test="${orderHistoryDetail.isEGVOrder ne  true}">
															<p>
																<c:if test="${not empty entry.product.size}">
																	<c:choose>
																		<c:when test="${(not empty entry.product.rootCategory) && (entry.product.rootCategory == 'FineJewellery' || entry.product.rootCategory == 'FashionJewellery') }">
																			<spring:theme code="product.variant.size.noSize" var="noSize"/>
																			<c:if test="${entry.product.size ne noSize }">
																				<spring:eval expression="T(de.hybris.platform.util.Config).getParameter('mpl.jewellery.category')" var="lengthVariant"/>
																				<c:set var = "categoryListArray" value = "${fn:split(lengthVariant, ',')}" />
																				<c:forEach items="${entry.product.categories}" var="categories">
																					<c:forEach items = "${categoryListArray}" var="lengthVariantArray">
																						<c:if test="${categories.code eq lengthVariantArray}">
																							<c:set var="lengthSize" value="true"/>
																						</c:if> 
																					</c:forEach>
																				</c:forEach>	  
																				<c:choose>
																					<c:when test="${true eq lengthSize}">
																					  <spring:theme code="product.variant.length.colon"/>
																										&nbsp;${entry.product.size}
																					</c:when>
																					<c:otherwise>
																					  <spring:theme code="text.orderHistory.size" />
																										&nbsp;${entry.product.size}
																					</c:otherwise>
																				</c:choose>
																			</c:if>
																		</c:when>
																		
																		<c:when test="${not empty entry.product.rootCategory && entry.product.rootCategory=='HomeFurnishing'}">
																		
																		<spring:eval expression="T(de.hybris.platform.util.Config).getParameter('mpl.homefurnishing.category.weight')" var="weightVariant"/>
																			<c:set var = "categoryListArray" value = "${fn:split(weightVariant, ',')}" />
																			
																			<c:forEach items="${entry.product.categories}" var="categories">
																			<c:forEach items = "${categoryListArray}" var="weightVariantArray">
																					<c:if test="${categories.code eq weightVariantArray}">
																						<c:set var="isWeight" value="true"/>
																					</c:if> 
																			</c:forEach>				
																			</c:forEach> 
																			
																			<spring:eval expression="T(de.hybris.platform.util.Config).getParameter('mpl.homefurnishing.category.volume')" var="volumeVariant"/>
																			<c:set var = "categoryListArray" value = "${fn:split(volumeVariant, ',')}" />
																			
																			<c:forEach items="${entry.product.categories}" var="categories">
																			<c:forEach items = "${categoryListArray}" var="volumeVariantArray">
																					<c:if test="${categories.code eq volumeVariantArray}">
																						<c:set var="isVolume" value="true"/>
																					</c:if> 
																			</c:forEach>				
																			</c:forEach>
																			
																			<c:choose>
																					<c:when test="${true eq isWeight}">
																							<spring:theme code="product.variant.weight"/>:&nbsp;${entry.product.size}
																					</c:when>
																					<c:when test="${true eq isVolume}">
																							<spring:theme code="product.variant.volume"/>:&nbsp;${entry.product.size}
																					</c:when>
																					<c:otherwise>
																					<c:if test="${!fn:containsIgnoreCase(entry.product.size, 'No Size')}">
																							<spring:theme code="text.orderHistory.size" />
																										&nbsp;${entry.product.size};
																					</c:if>
																					</c:otherwise>
																			</c:choose>
																		
																		
																		</c:when>
																		<c:otherwise>
																		<c:if test="${!fn:containsIgnoreCase(entry.product.size, 'No Size')}">
																			<spring:theme code="text.orderHistory.size" />
																										&nbsp;${entry.product.size}
																		</c:if>
																		</c:otherwise>
																	</c:choose>
																</c:if>
															</p>
															<p>
																<c:if test="${not empty entry.product.colour}">
																<c:if test="${!fn:containsIgnoreCase(entry.product.colour, 'No Color')}">
																	<spring:theme code="text.orderHistory.color" />
																	&nbsp;${entry.product.colour}
																</c:if>
																</c:if>
															</p>
															</c:if>
														
														<div class="attributes">

															<p>
																<spring:theme code="text.orderHistory.price" />
																&nbsp;
																<ycommerce:testId
																	code="orderDetails_productTotalPrice_label">
																	<format:price priceData="${entry.amountAfterAllDisc}"
																		displayFreeForZero="true" />
																</ycommerce:testId>
															</p>
															<c:if test="${orderHistoryDetail.isEGVOrder ne  true}">
															<p>
																<%-- <spring:theme text="Delivery Charges:" /> --%>

																<span>	<spring:theme code="text.account.order.delivery2" text="Scheduled Delivery and Shipping Charges:"/>
																&nbsp;</span>
															<c:choose>
																<c:when test="${entry.currDelCharge.value=='0.0'}">
																	<%-- <spring:theme code="order.free"  /> --%>
																	
															      <c:choose>
																	<c:when test="${not empty entry.scheduledDeliveryCharge}">
																	    ${entry.scheduledDeliveryCharge}
																	</c:when>
																	<c:otherwise>
																	<ycommerce:testId
																		code="orderDetails_productTotalPrice_label">
																		<format:price priceData="${entry.currDelCharge}"
																			displayFreeForZero="true" />
																	</ycommerce:testId>
																    </c:otherwise>
																</c:choose>
																</c:when>
																<c:otherwise>
																	<format:price priceData="${entry.currDelCharge}" />
																</c:otherwise>
															</c:choose>
															</p>
															</c:if>
														</div>
														<c:if
															test="${not empty entry.imeiDetails.serialNum &&  fn:length(entry.imeiDetails.serialNum) > 0}">
															<p class="order-serial-num">

																<spring:theme code="text.orderHistory.serial.number" />
																&nbsp; ${entry.imeiDetails.serialNum}
															</p>
														</c:if>
														<%-- <c:forEach items="${productSerrialNumber}"
															var="productSerrialNumber">
															<c:choose>
																<c:when
																	test="${productSerrialNumber.key == subOrder.code}">
																	<c:forEach items="${productSerrialNumber.value}"
																		var="entryImeiNumber">
																		<c:choose>
																			<c:when
																				test="${entryImeiNumber.key == entry.entryNumber}">
																				<c:if
																					test="${not empty entryImeiNumber.value &&  fn:length(entryImeiNumber.value) > 0  }">
																					<p>
																						<spring:theme
																							code="text.orderHistory.serial.number" />&nbsp;
																						${entryImeiNumber.value}
																					</p>
																				</c:if>
																			</c:when>
																		</c:choose>
																	</c:forEach>
																</c:when>
															</c:choose>
														</c:forEach> --%>

														<p  class="order-serial-num">

															<spring:theme
																code="text.orderHistory.seller.order.number" />&nbsp;
															${subOrder.code}
														</p>
														<p>
															<c:forEach items="${entry.product.baseOptions}"
																var="option">
																<div class="thumb">
																	<c:if
																		test="${not empty option.selected and option.selected.url eq entry.product.url}">
																		<c:forEach
																			items="${option.selected.variantOptionQualifiers}"
																			var="selectedOption">
																			<dl>
																				<dt>${selectedOption.name}:</dt>
																				<dd>${selectedOption.value}</dd>
																			</dl>
																		</c:forEach>
																	</c:if>
																</div>
															</c:forEach>
															<c:if test="${entry.giveAway || entry.isBOGOapplied}">
																<c:if
																	test="${not empty orderHistoryDetail.appliedProductPromotions}">
																	<ul>
																		<c:forEach
																			items="${orderHistoryDetail.appliedProductPromotions}"
																			var="promotion">
																			<c:set var="displayed" value="false" />
																			<c:forEach items="${promotion.consumedEntries}"
																				var="consumedEntry">
																				<c:if test="${not displayed && not entry.isBOGOapplied && entry.giveAway && ((consumedEntry.adjustedUnitPrice - entry.amountAfterAllDisc.doubleValue) == '0.0' ||(consumedEntry.adjustedUnitPrice - entry.amountAfterAllDisc.doubleValue) == '0.00')}">
																					<c:set var="displayed" value="true" />
																					<li><span>${promotion.description}</span></li>
																				</c:if>
																			</c:forEach>
																		</c:forEach>
																	</ul>
																</c:if>
															</c:if>
														</p>
													</div>
													<c:set value="${entry.orderLineId}" var="code"/>
													<c:set value="${orderStatusMap[code]}" var="orderStatus"/>
													
													<c:set value="${orderStatus['APPROVED']}" var="approvedFlag"/>
													<c:set value="${orderStatus['SHIPPING']}" var="shippingFlag"/>
													<c:set value="${orderStatus['DELIVERY']}" var="deliveryFlag"/>
													<c:set value="${orderStatus['CANCEL']}" var="cancelFlags"/>
													<c:set value="${orderStatus['RETURN']}" var="returnFlags"/>
													
													
													<input type="hidden" value="${deliveryFlag.responseCode}"/>
													<input type="hidden" value="${shippingFlag.responseCode}"/>
													<input type="hidden" value="${approvedFlag.responseCode}"/>
													<input type="hidden" value="${cancelFlags.responseCode}"/>
													<input type="hidden" value="${returnFlags.responseCode}"/>
													
													
													<c:choose>
													<c:when test="${not empty returnFlags and returnFlags ne null}">
													<div class="orderUpdatesBlock">
														<%-- <div class="status statusCancel">
															<span><spring:theme code="text.orderHistory.seller.order.numbe" text="Returned" /></span> 
														</div> --%>
														<div class="statusDate">
															<span><spring:theme code="text.orderHistory.seller.order.numbe" text="Return Request Date: " /></span>&nbsp;
															<c:forEach items="${returnFlags.statusRecords}" var="recordDate">
															<span>${recordDate.date}</span>
															</c:forEach>
														</div>
														</div>
													</c:when>
													<c:otherwise>
													<c:choose>
													<c:when test="${not empty cancelFlags and cancelFlags ne null}">
														<div class="orderUpdatesBlock">
														<%-- <div class="status statusCancel">
															<span><spring:theme code="text.orderHistory.seller.order.numbe" text="Cancelled" /></span>
														</div> --%>
														<div class="statusDate">
															<span><spring:theme code="text.orderHistory.seller.order.numbe" text="Cancellation Request Date: " /></span>&nbsp;
															<c:forEach items="${cancelFlags.statusRecords}" var="recordDate">
															<span>${recordDate.date}</span>
															</c:forEach>
														</div>
														</div>
														</c:when>
														<c:otherwise>
														<c:choose>
														<c:when test="${not empty deliveryFlag and deliveryFlag ne null}">
														<div class="orderUpdatesBlock">
														<div class="status statusDelivered">
															<span><spring:theme code="text.orderHistory.seller.order.numbe" text="Delivered" /></span>
														</div>
														<div class="statusDate">
															<span><spring:theme code="text.orderHistory.seller.order.numbe" text="Delivered:" /></span>&nbsp;
															<c:forEach items="${deliveryFlag.statusRecords}" var="recordDate">
															<span>${recordDate.date}</span>
															</c:forEach>
														</div>
														</div>
														</c:when>
														<c:otherwise>
														<c:choose>
														<c:when test="${not empty shippingFlag and shippingFlag ne null}">
														<div class="orderUpdatesBlock">
														<div class="status statusShipped">
															<span><spring:theme code="text.orderHistory.seller.order.numbe" text="Shipped" /></span>
														</div>
														<div class="statusDate">
															<span><spring:theme code="text.orderHistory.seller.order.numbe" text="Shipped:" /></span>&nbsp;
															<c:forEach items="${shippingFlag.statusRecords}" var="recordDate">
															<span>${recordDate.date}</span>
															</c:forEach>
														</div>
														</div>
														</c:when>
														<c:otherwise>
														<c:choose>
															<c:when test="${not empty approvedFlag and approvedFlag ne null}">
														<div class="orderUpdatesBlock">
									                      <c:set value="false" var="egvredemStatusFlag"/>
															<c:forEach items="${egvStatusMap}" var="entryforEgv">
																
																	<c:if test="${entryforEgv.key eq orderHistoryDetail.code and entryforEgv.value eq 'REDEEMED'}">
																	<div class="status orderRedeemedStatusInfo"><span>VALIDATED</span></div><br />
																	<div class="statusDate">
																              <span><spring:theme code="text.orderHistory.seller.order.numbe" text="Redeemed:" /></span>&nbsp;
																              <c:forEach items="${approvedFlag.statusRecords}" var="recordDate">
																			<span>${recordDate.date}</span>
																			</c:forEach>
																	</div>
																	<c:set value="ture" var="egvredemStatusFlag"/>
																	</c:if>									   
															</c:forEach>
															<c:if test="${egvredemStatusFlag eq  'false'}">
															<div class="status statusConfirmed">
																<span><spring:theme code="text.orderHistory.seller.order.numbe" text="Confirmed" /></span>
															</div>
															
																<div class="statusDate">
																	<span><spring:theme code="text.orderHistory.seller.order.numbe" text="Confirmed:" /></span>&nbsp;
																	<c:forEach items="${approvedFlag.statusRecords}" var="recordDate">
																	<span>${recordDate.date}</span>
																	</c:forEach>
																</div>
															</c:if>
															
														</div>
														</c:when>
														</c:choose>
														</c:otherwise>
														</c:choose>
														</c:otherwise>
														</c:choose>
														</c:otherwise>
													</c:choose>
													</c:otherwise>
													</c:choose>
													

													<div class="actions">

														<c:set var="bogoCheck" value='false' />

														<c:if test="${entry.itemCancellationStatus eq 'true' and cancelFlag}">
															<c:if
																test="${entry.giveAway eq 'false' and entry.isBOGOapplied eq 'false'}">
																<c:forEach items="${entry.associatedItems}"
																	var="entryAssociated">
																	<c:forEach items="${subOrder.entries}"
																		var="entrySubOrder">
																		<c:if
																			test="${entrySubOrder.selectedUssid eq entryAssociated}">
																			<c:if
																				test="${entrySubOrder.giveAway eq 'true' || entrySubOrder.isBOGOapplied eq 'true'}">
																				<c:set var="bogoCheck" value='true' />

																			</c:if>
																		</c:if>

																	</c:forEach>
																</c:forEach>

																<%-- 	<c:set var="bogoCheck" value="${entry.associatedItems ne null ? 'true': 'false'}"></c:set> --%>
                                                       <c:if test="${orderHistoryDetail.isEGVOrder ne  true}">
																<a href="" data-toggle="modal"
																	data-target="#cancelOrder${subOrder.code}${entry.mplDeliveryMode.sellerArticleSKU}${entryStatus.index}"
																	data-mylist="<spring:theme code="text.help" />"
																	data-dismiss="modal"
																	onClick="refreshModal('${bogoCheck}',${entry.transactionId})"><spring:theme
																		text="Cancel Order" />
														</a>
															</c:if>
                                                         </c:if>
														</c:if>

														<!--Chairman Demo Changes: New Static Content Sheet: Checkout> Order Cancellation -->
														<!-- TISCR-410 -->  
															<%-- R2.3: Commented <c:if test="${entry.isCancellationMissed eq 'true'}">
																<spring:theme code="orderHistory.cancellationDeadlineMissed.msg" />
															</c:if> --%>

														<!-- TISCR-410 ends -->
														<!--Chairman Demo Changes end-->
														<!-- changes for TISSTRT-1173 -->
														<%-- R2.3: Commented --><!-- <c:if test="${entry.itemReturnStatus eq 'true'  and entry.giveAway eq false and entry.isBOGOapplied eq false}">
															<a href="${request.contextPath}/my-account/order/returnPincodeCheck?orderCode=${subOrder.code}&ussid=${entry.mplDeliveryMode.sellerArticleSKU}&transactionId=${entry.transactionId}" onClick="openReturnPage('${bogoCheck}',${entry.transactionId})">
																<spring:theme code="text.account.returnReplace"
																	text="Return Item"/> 
															</a>	
														</c:if> --%>
														<c:choose>
														 	 <c:when test="${entry.itemReturnStatus eq 'true'  and entry.giveAway eq false and entry.isBOGOapplied eq false}">
																	<a href="${request.contextPath}/my-account/order/returnPincodeCheck?orderCode=${subOrder.code}&ussid=${entry.mplDeliveryMode.sellerArticleSKU}&transactionId=${entry.transactionId}" onClick="openReturnPage('${bogoCheck}',${entry.transactionId})">
																						<spring:theme code="text.account.returnReplace"
																							text="Return Item"/> 
																	</a>		 
														  	</c:when>
														  	<c:otherwise>
														  		<c:if test="${entry.isCancellationMissed eq 'true'}">
																						<spring:theme code="orderHistory.cancellationDeadlineMissed.msg" />
																<span style="display: none;">${cancelPopover}</span>						
																</c:if>
														  	</c:otherwise>
														</c:choose>

														<c:if test="${entry.showInvoiceStatus eq 'true'}">
														<span  class="RequestInvoice"><a

																href="${request.contextPath}/my-account/order/requestInvoice?orderCode=${subOrder.code}&transactionId=${entry.transactionId}"
																onclick="callSendInvoice();"><spring:theme
																code="text.account.RequestInvoice"
																text="Request Invoice" /></a>
													    </span>

														</c:if>
														
														
														<!-- TPR-6013 My Profile Orders starts-->
														
														<%-- <div class="NotBeCancelled">
															<span><spring:theme code="orderHistory.cancellationDeadlineMissed.ms" text="This can not be cancelled. (Why?)" /></span>
															<span class="RequestInvoice"><spring:theme code="orderHistory.cancellationDeadlineMissed.ms" text="Request Invoice" /></span>
														</div> --%>
														
														<!-- TPR-6013 My Profile Orders ends-->

													</div>
												</li>
												
												<%-- <li class="header mobileHeader">
												<ul>
												<li class="viewDetails">
												<span class="orderNumber"><spring:theme code="text.orderHistory.order.place" text="Order"/>#${orderHistoryDetail.code}</span> 
												<span class="orderDate"><spring:theme code="text.orderHistory.order.place" text="Placed on:"/>&nbsp;
												<c:if test="${orderDataMap[orderHistoryDetail.code] ne null}">
													${orderDataMap[orderHistoryDetail.code]}
												</c:if></span>
												</li>
												

												<li class="viewDetailsAnchor"><a
													href="${orderDetailsUrl}?orderCode=${orderHistoryDetail.code}&pageAnchor=viewOrder"><spring:theme
															code="text.orderHistory.view.orde" text="Order Details" /></a></li>
												<li class="trackOrderAnchor"><a href="${orderDetailsUrl}?orderCode=${orderHistoryDetail.code}&pageAnchor=trackOrder"><spring:theme
															code="text.orderHistory.track.order" /></a></li>
												</ul>
											</li> --%>

												<div class="modal cancellation-request fade"
													id="cancelOrder${subOrder.code}${entry.mplDeliveryMode.sellerArticleSKU}${entryStatus.index}">

													<div class="content">
														<!-- <button type="button" class="close pull-right"
															aria-hidden="true" data-dismiss="modal"></button> -->
														<div class="cancellation-request-block">
															<h2>Request Cancellation </h2>

															<!-- ../my-account/returnSuccess -->
															<form:form
																id="returnRequestForm${entryStatus.index}${subOrder.code}"
																class="return-form" action="../my-account/returnSuccess"
																method="post" commandName="returnRequestForm">
																<div class="return-container">
																
																	<ul class="products">
																	<c:set var="orderCodeMap" value="${subOrder.code}${entry.orderLineId}"/>
																
																		<c:forEach items="${cancelProductMap[orderCodeMap]}" var="entryCancel" >	
																			
																		<li class="item look">
																		<ul class="product-info">
																			<li>
																			<div class="product-img">
																				<c:choose>
																					<c:when test="${fn:toLowerCase(entryCancel.product.luxIndicator)=='luxury'}">
																							<a href="${productUrl}"> <product:productPrimaryImage
																																	product="${entryCancel.product}" format="luxuryCartIcon" lazyLoad="false" />
																															</a>
																
																					</c:when>
																					<c:otherwise>
																							<a href="${productUrl}"> <product:productPrimaryImage
																																	product="${entryCancel.product}" format="thumbnail" lazyLoad="false" />
																															</a>
																							
																					</c:otherwise>
																				</c:choose>
																			</div>
																			<div class="product">
																				<!-- <p class="company">Nike</p> -->
																				<h2 class="product-name">
																					<a href="${productUrl}">${entryCancel.product.name}</a>
																				</h2>
																				<!-- 	TPR-6288:Order cancel changes -->
                                                                               <input type="hidden" id ="dtmPrdtCat" value ="${entryCancel.product.rootCategory}"/>
                                                                               <input type="hidden" id ="dtmPrdtCode" value ="${entryCancel.product.code}"/>
																				<p class="item-info">
																					<span><b><c:if test="${entryCancel.quantity > 1}"><spring:theme code="text.orderHistory.quantity"/>
																					&nbsp;${entryCancel.quantity}
																					</c:if></b> 
																					</span>
																					
																					<c:if test="${not empty entryCancel.product.size}">
													 									<%-- <span><b>Size:</b> ${entryCancel.product.size}</span> --%>
													 									<c:choose>
																							<c:when test="${(not empty entryCancel.product.rootCategory) && (entryCancel.product.rootCategory == 'FineJewellery' || entryCancel.product.rootCategory == 'FashionJewellery') }">
																								<spring:theme code="product.variant.size.noSize" var="noSize"/>
																								<c:if test="${entryCancel.product.size ne noSize}">
																									<spring:eval expression="T(de.hybris.platform.util.Config).getParameter('mpl.jewellery.category')" var="lengthVariant"/>
																									<c:set var = "categoryListArray" value = "${fn:split(lengthVariant, ',')}" />
																									<c:forEach items="${entryCancel.product.categories}" var="categories">
																										<c:forEach items = "${categoryListArray}" var="lengthVariantArray">
																											<c:if test="${categories.code eq lengthVariantArray}">
																												<c:set var="lengthSize" value="true"/>
																											</c:if> 
																										</c:forEach>
																									</c:forEach>	  
																									<c:choose>
																										<c:when test="${true eq lengthSize}">
																										  <span><b><spring:theme code="product.variant.length.colon"/></b> ${entryCancel.product.size}</span>
																										</c:when>
																										<c:otherwise>
																										  <span><b>Size:</b> ${entryCancel.product.size}</span>
																										</c:otherwise>
																									</c:choose>
																								</c:if>
																							</c:when>
																							
																							<c:when test="${not empty entryCancel.product.rootCategory && entryCancel.product.rootCategory=='HomeFurnishing'}">

																											<spring:eval
																												expression="T(de.hybris.platform.util.Config).getParameter('mpl.homefurnishing.category.weight')"
																												var="weightVariant" />
																											<c:set var="categoryListArray"
																												value="${fn:split(weightVariant, ',')}" />

																											<c:forEach
																												items="${entryCancel.product.categories}"
																												var="categories">
																												<c:forEach items="${categoryListArray}"
																													var="weightVariantArray">
																													<c:if
																														test="${categories.code eq weightVariantArray}">
																														<c:set var="isWeight" value="true" />
																													</c:if>
																												</c:forEach>
																											</c:forEach>

																											<spring:eval
																												expression="T(de.hybris.platform.util.Config).getParameter('mpl.homefurnishing.category.volume')"
																												var="volumeVariant" />
																											<c:set var="categoryListArray"
																												value="${fn:split(volumeVariant, ',')}" />

																											<c:forEach
																												items="${entryCancel.product.categories}"
																												var="categories">
																												<c:forEach items="${categoryListArray}"
																													var="volumeVariantArray">
																													<c:if
																														test="${categories.code eq volumeVariantArray}">
																														<c:set var="isVolume" value="true" />
																													</c:if>
																												</c:forEach>
																											</c:forEach>


																											<c:choose>
																												<c:when test="${true eq isWeight}">
																													<span>
																														<b><spring:theme
																															code="product.variant.weight" /></b>
																														:${entryCancel.product.size}
																													</span>
																												</c:when>
																												<c:when test="${true eq isVolume}">
																													<span>
																														<b><spring:theme
																															code="product.variant.volume" /></b>
																														:${entryCancel.product.size}
																													</span>
																												</c:when>
																												<c:otherwise>
																													<c:if
																														test="${!fn:containsIgnoreCase(entry.product.size, 'No Size')}">
																														<span><b>Size:</b> ${entryCancel.product.size}</span>
																													</c:if>
																												</c:otherwise>
																											</c:choose>

																										</c:when>
																							
																							
																							<c:otherwise>
																							<c:if test="${!fn:containsIgnoreCase(entryCancel.product.size, 'No Size')}">
																								<span><b>Size:</b> ${entryCancel.product.size}</span>
																							</c:if>
																							</c:otherwise>
																						</c:choose>
																					</c:if>
																					<c:if test="${not empty entryCancel.product.colour}">
																					 <c:if test="${!fn:containsIgnoreCase(entryCancel.product.colour, 'No Color')}">
																						<span><b>Color:</b> ${entryCancel.product.colour}</span>
																					</c:if>
																					</c:if>
																					 
																					 <span
																						class="price"><b>Price:</b> <ycommerce:testId
																							code="orderDetails_productTotalPrice_label">
																							<format:price priceData="${entryCancel.totalPrice}"
																								displayFreeForZero="true" />
																						</ycommerce:testId>
																					</span>
																					<c:if test="${not empty entryCancel.imeiDetails}"><span><b>Serial Number:</b> ${entryCancel.imeiDetails.serialNum}</span></c:if>
																					<span class="sellerOrderNo"><b>
																					<spring:theme code="text.orderHistory.seller.order.number" /></b> 
																					${subOrder.code}
																					</span>
																				</p>
																				<%-- <form:hidden path="sellerId" value="${sellerId}" /> --%>
																			</div>
																		</li>
																	</ul>
																</li>
																</c:forEach>
																	</ul>
																	<div class="questions">
																		<label>But why?</label>
																		 <form:select name="reasonList"
																			id="cancellationreasonSelectBox_${entry.transactionId}" path="reasonCode" onchange="setDropDownValue(${entry.transactionId})">
																			<option selected="selected" disabled="disabled" id="defaultReason"><spring:theme
																					code="text.requestDropdown.selected" /></option>
																			<c:forEach items="${cancellationReason}" var="reason">
																				<option value="${reason.reasonCode}">${reason.reasonDescription}</option>
																			</c:forEach>
																		</form:select>
																		<div id="blankReasonError" style="display:none; color:red; padding-top: 10px;"><spring:theme
																					code="text.cancel.requestDropdown.selected.error" text="Let us know why you would like to cancel this product."/></div> 
																	</div>
																	<form:hidden path="ticketTypeCode"
																		class="ticketTypeCodeClass" value="C" />
																	<form:hidden path="orderCode" id="orderCode"
																		class="subOrderCodeClass" value="${subOrder.code}" />
																	<form:hidden path="transactionId"
																		class="transactionIdClass"
																		value="${entry.transactionId}" />
																	<form:hidden path="ussid" class="ussidClass"
																		value="${entry.mplDeliveryMode.sellerArticleSKU}" />
																	<input type="hidden" class="entryNumberClass"
																		id="entryNumber" value="${entry.entryNumber}" />
																</div>
																<div class="buttons">
																<!-- TISPRDT - 995 -->
																	<!-- <a class="close" data-dismiss="modal" >Close</a> -->
																<!-- TISPRDT - 995 -->
																	<button type="button" class="light-red cancel-confirm"
																		id="myaccount" data-dismiss="modal" >Confirm
																		Cancellation</button>
																</div>

															</form:form>

														</div>
														<!-- <button class="close" data-dismiss="modal"></button> -->
													</div>
													<div class="overlay" data-dismiss="modal"></div>
												</div>




												<div class=" modal account active fade"
													id="cancelSuccess${subOrder.code}${entry.mplDeliveryMode.sellerArticleSKU}">

													<div class="content">
														<!-- <button type="button" id="returnReject"
															onclick="reloadOrderPage()" class="close pull-right"
															aria-hidden="true" data-dismiss="modal"></button> -->
														<div class="cancellation-request-block success">
															<h2>Request Cancellation</h2>

															<div>
																<h2 class="trackOrderLnHt">
																	<span id="resultTitle"></span>
																</h2>
																<div>
																	<span id="resultDesc"></span>
																</div>
															</div>
															<form class="return-form">
																<div class="return-container container">
																	<ul class="products">
																		<c:set var="orderCodeMap" value="${subOrder.code}-${entry.orderLineId}"/>
																		
																		<c:forEach items="${cancelProductMap[orderCodeMap]}" var="entryCancel" >	
																			
																		<li class="item look">
																		<ul class="product-info">
																			<li>
																			<div class="product-img">
																				<c:choose>
																					<c:when test="${fn:toLowerCase(entryCancel.product.luxIndicator)=='luxury'}">
																							<a href="${productUrl}"> <product:productPrimaryImage
																																	product="${entryCancel.product}" format="luxuryCartIcon" lazyLoad="false" />
																															</a>
																
																					</c:when>
																					<c:otherwise>
																							<a href="${productUrl}"> <product:productPrimaryImage
																																	product="${entryCancel.product}" format="thumbnail" lazyLoad="false" />
																															</a>
																							
																					</c:otherwise>
																				</c:choose>
																			</div>
																			<div class="product">
																				<!-- <p class="company">Nike</p> -->
																				<h3 class="product-name">
																					<a href="${productUrl}">${entryCancel.product.name}</a>
																				</h3>
																				
																				<p class="item-info">
																					<span><b><c:if test="${entryCancel.quantity > 1}"><spring:theme code="text.orderHistory.quantity"/>
																					&nbsp;${entryCancel.quantity}
																					</c:if></b> 
																					</span>
																					
																					<c:if test="${not empty entryCancel.product.size}">
													 									<%-- <span><b>Size:</b> ${entryCancel.product.size}</span> --%>
													 									<c:choose>
																							<c:when test="${(not empty entryCancel.product.rootCategory) && (entryCancel.product.rootCategory == 'FineJewellery' || entryCancel.product.rootCategory == 'FashionJewellery') }">
																								<spring:theme code="product.variant.size.noSize" var="noSize"/>
																								<c:if test="${entryCancel.product.size ne noSize}">
																									<spring:eval expression="T(de.hybris.platform.util.Config).getParameter('mpl.jewellery.category')" var="lengthVariant"/>
																									<c:set var = "categoryListArray" value = "${fn:split(lengthVariant, ',')}" />
																									<c:forEach items="${entryCancel.product.categories}" var="categories">
																										<c:forEach items = "${categoryListArray}" var="lengthVariantArray">
																											<c:if test="${categories.code eq lengthVariantArray}">
																												<c:set var="lengthSize" value="true"/>
																											</c:if> 
																										</c:forEach>
																									</c:forEach>	  
																									<c:choose>
																										<c:when test="${true eq lengthSize}">
																										  <span><b><spring:theme code="product.variant.length.colon"/></b> ${entryCancel.product.size}</span>
																										</c:when>
																										<c:otherwise>
																										  <span><b>Size:</b> ${entryCancel.product.size}</span>
																										</c:otherwise>
																									</c:choose>
																								</c:if>
																							</c:when>
																							<c:otherwise>
																								<span><b>Size:</b> ${entryCancel.product.size}</span>
																							</c:otherwise>
																						</c:choose>
																					</c:if>
																					<c:if test="${not empty entryCancel.product.colour}">
																						<span><b>Color:</b> ${entryCancel.product.colour}</span>
																					</c:if>
																					 
																					 <span
																						class="price"><b>Price:</b> <ycommerce:testId
																							code="orderDetails_productTotalPrice_label">
																							<format:price priceData="${entryCancel.totalPrice}"
																								displayFreeForZero="true" />
																						</ycommerce:testId>
																					</span>
																					<c:if test="${not empty entryCancel.imeiDetails}"><span><b>Serial Number:</b> ${entryCancel.imeiDetails.serialNum}</span></c:if>
																					<span class="sellerOrderNo"><b>
																					<spring:theme code="text.orderHistory.seller.order.number" /></b> 
																					${sellerOrder.code}
																					</span>
																				</p>
																				<%-- <form:hidden path="sellerId" value="${sellerId}" /> --%>
																			</div>
																		</li>
																	</ul>
																</li>
																</c:forEach>
																	</ul>
																	<div class="reason questions">
																		<label id="reasonTitle">Reason for
																			Cancellation:</label> <span id="reasonDesc"></span>
																	</div>
																</div>
																<div class="buttons">
																	<button class="light-red blue" id="returnReject"
																		onclick="reloadOrderPage()">Close</button>

																</div>
															</form>
														</div>

													</div>
													<div class="overlay fade" data-dismiss="modal"></div>
												</div>

											</c:forEach>
										</c:forEach>
									</ul>
								</li>
							</ul>
						</c:forEach>

						<div class="navigation2" >
							<span id="ofPagination"></span>
							<%-- <div class="navigation2" >
							<span id="ofPagination"></span>
							<c:if test="${fn:length(orderDataList)>pageSizeInoh}">
								<ul class="pagination orderhistory_address_pagination2">
									<li class="address_pagination2" id="paginationDiv2"></li>
	
								</ul>
							</c:if>
							</div> --%>
							<!-- TISPRO-48 ---- call mpl-pagination.tag for pagination -->
							<nav:mpl-pagination top="false"
								supportShowPaged="${isShowPageAllowed}"
								supportShowAll="${isShowAllAllowed}"
								searchPageData="${searchPageData}"
								searchUrl="/my-account/orders?sort=${searchPageData.pagination.sort}"
								msgKey="text.account.orderHistory.page"
								numberPagesShown="${numberPagesShown}" />
						<!-- mycode -->
						</div>
						<!-- mycode -->
					</c:if>

					<c:if test="${empty searchPageData.results}">
						<div class="account-emptyOrderMessage">
							<spring:theme code="text.account.orderHistory.noOrders"
								text="You have no orders" />
						</div>
					</c:if>

				</div>

			</div>

		</div>
	</div>
	<div class="modal fade track-order-modal" id="track-order-modal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
	  <button type="button" onclick="closepop()" id="closePop">Click</button>
		<div class="overlay" data-dismiss="modal"></div>
		<div class="content"></div>
	</div>
	
	<div class="showStatementModel" id="showStatementPopup">
	    <div class="showStatementModel-content">
	    	<span class="accountPopupClose close">&times;</span> 
		     <div id="showStatementData">
		      </div>
	    </div>
   </div>
   
</template:page>
<script>
$('.resend_email_index').val(1);
$(".resend_order_email").click(function () {
	$('.resend_email_limit').hide();
	/* $('.resend_email_limit').hide(); */
	var orderId = $(this).parents('.resendEmail').find(".order_id_for_resending").val();
	var resendCount = $(this).parents('.resendEmail').find(".resend_email_index").val();
	var msgSection = $(this).parents('.header').find('.resend_email_limit');
	var resendVariable = $(this).parents('.resendEmail').find(".resend_email_index");
	if(resendCount < 4){
		$.ajax({	  
			type: "POST",
			url: ACC.config.encodedContextPath + "/my-account/sendNotificationEGVOrder",
		    data: "orderId="+orderId,
			success: function () {	
			
				
				msgSection.text("Email sent successfully.");
				msgSection.removeClass('alert-danger');
				msgSection.addClass('alert-success');
				msgSection.show().delay(3000).fadeOut();
				resendCount++;
				resendVariable.val(resendCount);
				
			}
		});
	} else {
		msgSection.text("Maximum attempts reached.");
		msgSection.removeClass('alert-success');
		msgSection.addClass('alert-danger');
		msgSection.show().delay(3000).fadeOut();
	}
}); 
</script>

<script type="text/javascript">
var showStatementModel = document.getElementById('showStatementPopup');
var showStatementData = document.getElementById('showStatementData');
var showStatementPopup = document.getElementById('showStatementPopup');


$(".get_order_statement").click(function() {
	var orderCode = $(this).parents('.resendEmail').find(".order_id_for_statement").val();
	$.ajax({
		  type : "GET",
		  url: ACC.config.encodedContextPath + "/my-account/getStatement",
		  data: "orderId="+orderCode,
		  contentType : "html/text",
		  success : function(response){
		   showStatementData.innerHTML=response;
		   showStatementModel.style.display = "block";      
		      }, 
		    failure : function(data) {
		    }
		   });
});

$(".accountPopupClose").on('click', function () {
	showStatementModel.style.display = "none";
});
</script>