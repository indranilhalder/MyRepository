<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/luxurystoreaddon/responsive/template"%>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/addons/luxurystoreaddon/responsive/user"%>
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
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/addons/luxurycheckoutaddon/responsive/product"%>
<%@ taglib prefix="order" tagdir="/WEB-INF/tags/responsive/order"%>

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


<!-- LW-230 -->
<%-- <input type="hidden" id="isLuxury" value="${isLuxury}"/> --%>

<template:page pageTitle="${pageTitle}">
	<div class="account">
		<h1 class="account-header">
			<spring:theme code="text.account.headerTitle" text="My Tata CLiQ" />
		
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
		<div class="luxury-mobile-myaccount visible-xs">
			<select class="menu-select"
				onchange="window.location=this.options[this.selectedIndex].value;">
				<optgroup label="<spring:theme code="header.flyout.myaccount" />">
					<option value=/store/mpl/en/my-account data-href="/store/mpl/en/my-account/"><spring:theme code="header.flyout.overview" /></option>
					<%-- <option value=/store/mpl/en/my-account/marketplace-preference data-href="/store/mpl/en/my-account/marketplace-preference"><spring:theme code="header.flyout.marketplacepreferences" /></option> --%>
					<option value=/store/mpl/en/my-account/update-profile data-href="account-info.php"><spring:theme code="header.flyout.Personal" /></option>
					<option value=/store/mpl/en/my-account/orders data-href="order-history.php" selected>
						<spring:theme code="header.flyout.orders" /></option>
					<option value=/store/mpl/en/my-account/payment-details
						data-href="account-cards.php"><spring:theme
							code="header.flyout.cards" /></option>
					<option value=/store/mpl/en/my-account/address-book
						data-href="account-addresses.php"><spring:theme
							code="header.flyout.address" /></option>
							<option value=/store/mpl/en/my-account/reviews
						data-href="account-addresses.php"><spring:theme code="header.flyout.review" /></option>
						<option value=/store/mpl/en/faq data-href="faq.php"><spring:theme code="header.flyout.faq" /></option>
				</optgroup>

				<%-- <optgroup label="Share">
					<option value=/store/mpl/en/my-account/friendsInvite
						data-href="account-invite.php"><spring:theme
							code="header.flyout.invite" /></option>
				</optgroup> --%>
			</select>
		</div>
	 <div><h2 style="text-align: center;"><spring:theme code="text.heading.myaccount" /></h2></div>
		<div class="wrapper">


			<!----- Left Navigation Starts --------->
			
			<user:accountLeftNav pageName="orderHistory"/>
			<!----- Left Navigation ENDS --------->
			<!----- RIGHT Navigation STARTS --------->
			<div class="right-account">
				<h2>
					<spring:theme text="Order History" />
				</h2>
				<div class="order-history">
					<div class="navigation" id=order_pagination>
						
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

									<ul class="product-block">

										<li class="header">
											<ul>
												<li class="date"><span><spring:theme
															code="luxury.text.orderHistory.order.placed" /></span> <%-- <fmt:formatDate
														value="${orderHistoryDetail.created}"
														pattern="MMMMM dd, yyyy" /> ${formatedDate} --%> <c:if
														test="${orderDataMap[orderHistoryDetail.code] ne null}">
													${orderDataMap[orderHistoryDetail.code]}
												</c:if></li>
												<li class="order-number"><span><spring:theme
															code="text.orderHistory.number" /></span>#${orderHistoryDetail.code}</li>
												<!-- TISSIT-1773 -->
															<%-- <format:price priceData="${orderHistoryDetail.totalPrice}" /> --%>
												
															
															
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
													<li class="order-total"><span><spring:theme
															code="text.orderHistory.total" /></span>  
															
															
															<c:choose>
													<c:when test="${orderHistoryDetail.net}">
														<format:price priceData="${orderHistoryDetail.totalPriceWithTax}" />
													</c:when>
													<c:otherwise>
														<format:price priceData="${orderHistoryDetail.totalPriceWithConvCharge}" />
													</c:otherwise>
												</c:choose>	
											</ul>
										</li>
										
										<c:if test= "${empty orderHistoryDetail.sellerOrderList}">
								<li class="item"><a
									href="${orderDetailsUrl}?orderCode=${orderHistoryDetail.code}&pageAnchor=viewOrder"
									class="btn btn-primary btn-sm"><spring:theme
										code="text.orderHistory.view.order" /></a>
								<a
									href="${orderDetailsUrl}?orderCode=${orderHistoryDetail.code}&pageAnchor=trackOrder"
									class="btn btn-primary btn-sm"><spring:theme
										code="text.orderHistory.track.order" /></a></li>
								</c:if>
										
										<c:forEach items="${orderHistoryDetail.sellerOrderList}"
											var="subOrder" varStatus="status">
											<input type="hidden" id="subOrderCode"
												class="suborderEntryCode" value="${subOrder.code}" />

											<c:forEach items="${subOrder.entries}" var="entry"
												varStatus="entryStatus">

												<c:url value="${entry.product.url}" var="productUrl" />
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
																					product="${entry.product}" format="luxuryThumbnail" />
																			</a>
										
															</c:when>
															<c:otherwise>
																	<a href="${productUrl}"> <product:productPrimaryImage
																					product="${entry.product}" format="thumbnail" />
																			</a>
																	
															</c:otherwise>
														</c:choose>
													</div>
													<div class="details">
														<h2 class="product-name">${entry.brandName}</h2>
														<h2 class="product-name">
															<a href="${productUrl}">${entry.product.name}</a>
														</h2>
														<div class="attributes">

															<p>
																<c:if test="${entry.quantity > 1}">
																	<spring:theme code="text.orderHistory.quantity" />
												&nbsp;${entry.quantity}
												</c:if>
															</p>
															<p>
																<c:if test="${not empty entry.product.size}">
																	<spring:theme code="text.orderHistory.size" />
																	&nbsp;${entry.product.size}
																</c:if>
															</p>
															<p>
																<c:if test="${not empty entry.product.colour}">
																	<spring:theme code="text.orderHistory.color" />
																	&nbsp;${entry.product.colour}
																</c:if>
															</p>
															<p>
																<spring:theme code="text.orderHistory.price" />
																&nbsp;
																<ycommerce:testId
																	code="orderDetails_productTotalPrice_label">
																	<format:price priceData="${entry.amountAfterAllDisc}"
																		displayFreeForZero="true" />
																</ycommerce:testId>
															</p>
															<%-- <span>
																<spring:theme text="Schedule Delivery and Shipping Charges:" />
																&nbsp;
															<c:choose>
																<c:when test="${entry.currDelCharge.value=='0.0'}">
																	<spring:theme code="order.free"  />
																	
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
															</span> --%>
														</div>
														<c:if
															test="${not empty entry.imeiDetails.serialNum &&  fn:length(entry.imeiDetails.serialNum) > 0}">
															<p>
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

														<%-- <p>
															<spring:theme
																code="text.orderHistory.seller.order.number" />&nbsp;
															${subOrder.code}
														</p> --%>
														<p>
															<spring:theme
																code="text.orderHistory.shpping" />
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

																<a href=""  class="btn btn-primary btn-sm"
																 data-toggle="modal"
																	data-target="#cancelOrder${subOrder.code}${entry.mplDeliveryMode.sellerArticleSKU}${entryStatus.index}"
																	data-mylist="<spring:theme code="text.help" />"
																	data-dismiss="modal"
																	onClick="refreshModal('${bogoCheck}',${entry.transactionId})"><spring:theme
																		text="Cancel Order" /></a>
															</c:if>

														</c:if>
														<a
													href="${orderDetailsUrl}?orderCode=${orderHistoryDetail.code}&pageAnchor=viewOrder"  class="btn btn-primary btn-sm"
													><spring:theme
															code="text.orderHistory.view.order" /></a> <a
													href="${orderDetailsUrl}?orderCode=${orderHistoryDetail.code}&pageAnchor=trackOrder"  class="btn btn-primary btn-sm"
													><spring:theme
															code="text.orderHistory.track.order" /></a>

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
																	<a href="${request.contextPath}/my-account/order/returnPincodeCheck?orderCode=${subOrder.code}&ussid=${entry.mplDeliveryMode.sellerArticleSKU}&transactionId=${entry.transactionId}" class="btn btn-primary btn-sm" onClick="openReturnPage('${bogoCheck}',${entry.transactionId})">
																						<spring:theme code="text.account.returnReplace"
																							text="Return Item"/> 
																	</a>		 
														  	</c:when>
														  	<c:otherwise>
														  		<c:if test="${entry.isCancellationMissed eq 'true'}">
																						<div class="mt-10 mb-10"><spring:theme code="orderHistory.cancellationDeadlineMissed.msg" /></div>
																</c:if>
														  	</c:otherwise>
														</c:choose>

														<c:if test="${entry.showInvoiceStatus eq 'true'}">
															<a
																href="${request.contextPath}/my-account/order/requestInvoice?orderCode=${subOrder.code}&transactionId=${entry.transactionId}" class="btn btn-primary btn-sm"
																onclick="callSendInvoice();"><spring:theme
																	code="text.account.RequestInvoice"
																	text="Request Invoice" /></a>

														</c:if>

													</div>
												</li>

												<div class="modal cancellation-request fade"
													id="cancelOrder${subOrder.code}${entry.mplDeliveryMode.sellerArticleSKU}${entryStatus.index}">

													<div class="content">
														<button type="button" class="close pull-right"
															aria-hidden="true" data-dismiss="modal"></button>
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
																																	product="${entryCancel.product}" format="luxuryThumbnail" />
																															</a>
																
																					</c:when>
																					<c:otherwise>
																							<a href="${productUrl}"> <product:productPrimaryImage
																																	product="${entryCancel.product}" format="thumbnail" />
																															</a>
																							
																					</c:otherwise>
																				</c:choose>
																			</div>
																			<div class="product">
																				<!-- <p class="company">Nike</p> -->
																				<h2 class="product-name">
																					<a href="${productUrl}">${entryCancel.product.name}</a>
																				</h2>
																				
																				<p class="item-info">
																					<span><b><c:if test="${entryCancel.quantity > 1}"><spring:theme code="text.orderHistory.quantity"/>
																					&nbsp;${entryCancel.quantity}
																					</c:if></b> 
																					</span>
																					
																					<c:if test="${not empty entryCancel.product.size}">
													 									<span><b>Size:</b> ${entryCancel.product.size}</span>
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
																					code="text.cancel.requestDropdown.selected.error" text="Do let us know why you would like to cancel this item."/></div> 
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
															<h2>Cancellation Request</h2>

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
																																	product="${entryCancel.product}" format="luxuryThumbnail" />
																															</a>
																
																					</c:when>
																					<c:otherwise>
																							<a href="${productUrl}"> <product:productPrimaryImage
																																	product="${entryCancel.product}" format="thumbnail" />
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
													 									<span><b>Size:</b> ${entryCancel.product.size}</span>
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
																<div class="buttons mt-20">
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

						<%-- <div class="navigation2" >
							<span id="ofPagination"></span>
							<div class="navigation2" >
							<span id="ofPagination"></span>
							<c:if test="${fn:length(orderDataList)>pageSizeInoh}">
								<ul class="pagination orderhistory_address_pagination2">
									<li class="address_pagination2" id="paginationDiv2"></li>
	
								</ul>
							</c:if>
							</div>
							<!-- TISPRO-48 ---- call mpl-pagination.tag for pagination -->
							<nav:mpl-pagination top="false"
								supportShowPaged="${isShowPageAllowed}"
								supportShowAll="${isShowAllAllowed}"
								searchPageData="${searchPageData}"
								searchUrl="/my-account/orders?sort=${searchPageData.pagination.sort}"
								msgKey="text.account.orderHistory.page"
								numberPagesShown="${numberPagesShown}" />
						<!-- mycode -->
						</div> --%>
						<!-- mycode -->
					</c:if>

					<c:if test="${empty searchPageData.results}">
                        <div class="account-emptyOrderMessage">
                                <%-- <spring:theme code="text.account.orderHistory.noOrders.luxury"
                                    text="You have no orders" /> --%>
                            Due to site upgradations for orders placed prior to the 16th of October, please redirect to <a href='https://www.tatacliq.com/my-account/orders'>www.tatacliq.com/my-account/orders</a>
                            <br /> <br />
                            <div class="empty-order-btn">
                                <p class="mb-40">For any queries call us on +91- 9029108282 Alternatively, email Tata CLiQ Luxury Care at luxury@tatacliq.com</p>
                                <p><a class="lux-shopmen" href="${menLandingurl}">Shop Men</a><a class="lux-shopwomen" href="${womenLandingUrl}">Shop Women</a></p>
                            </div>
                        </div>
					</c:if>

				</div>

			</div>

		</div>
	</div>
</template:page>
