<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="order" tagdir="/WEB-INF/tags/responsive/order"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="order" tagdir="/WEB-INF/tags/responsive/order"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
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

<style>
.ordermargingalignment {
	height: 41px;
	padding-top: 7px;
	font-size: 12px;
	font-weight: 300;
}

.orderheadingalignment {
	font-size: 12px;
}

.orderbodyalignment {
	font-size: 12px;
}

.attributes {
	font-size: 12px;
}

.actions {
	font-size: 12px;
}
.pickupeditbtn:after{

     font-family: 'FontAwesome';
    content:"\f040";
    padding:0 5px 0 5px; 
}
.deliverymode {
	font-size: 12px;
	font-weight: 600;
	min-height: 17px;
}

.error_text {
	color: red;
}

#pickName {
	height: 25px !important;
	white-space: nowrap;
}
.order {
	margin-top: 8px !important;
}
</style>


<template:page pageTitle="${pageTitle}">
	<div class="account">
		<h1 class="account-header">
			<spring:theme code="text.account.headerTitle" text="My Marketplace" />
			<%--  <select class="menu-select">
          <optgroup label="<spring:theme code="header.flyout.myaccount" />">
                  <option value="Overview" data-href="account-overview.php"><spring:theme code="header.flyout.overview" /></option>
                  <option value="Marketplace Preferences" data-href="marketplace-preferences.php"><spring:theme code="header.flyout.marketplacepreferences" /></option>
                  <option value="Personal Information" data-href="account-info.php"><spring:theme code="header.flyout.Personal" /> </option>
                  <option value="Order History" data-href="order-history.php"> <spring:theme code="header.flyout.orders" /></option>
                  <option value="Saved Cards" data-href="account-cards.php"><spring:theme code="header.flyout.cards" /></option>
                  <option value="Saved Addresses" data-href="account-addresses.php"><spring:theme code="header.flyout.address" /></option>
              </optgroup>
         
          <optgroup label="Share">
                  <option value="Invite Friends" data-href="account-invite.php"><spring:theme code="header.flyout.invite" /></option>
              </optgroup>
      </select> --%>
		</h1>
		<div class="wrapper">


			<!----- Left Navigation Starts --------->
			<%-- <div class="left-nav">
				<h1>
					<spring:theme code="text.account.headerTitle" text="My MarketPlace" />
				</h1>
				<ul>
					<li><h3>
							<spring:theme code="header.flyout.myaccount" />
						</h3></li>
					<li><a href="<c:url value="/my-account/"/>"><spring:theme
								code="header.flyout.overview" /></a></li>
					<li><a
						href="<c:url value="/my-account/marketplace-preference"/>"><spring:theme
								code="header.flyout.marketplacepreferences" /></a></li>
					<li><a href="<c:url value="/my-account/update-profile"/>"><spring:theme
								code="header.flyout.Personal" /></a></li>
					<li><a class="active"
						href="<c:url value="/my-account/orders"/>"><spring:theme
								code="header.flyout.orders" /></a></li>
					<li><a href="<c:url value="/my-account/payment-details"/>"><spring:theme
								code="header.flyout.cards" /></a></li>
					<li><a href="<c:url value="/my-account/address-book"/>"><spring:theme
								code="header.flyout.address" /></a></li>
					<li><a href="<c:url value="/my-account/reviews"/>"><spring:theme
										code="header.flyout.review" /></a></li>
					<li><a href="<c:url value="/my-account/myInterest"/>"><spring:theme
								code="header.flyout.recommendations" /></a></li>
				</ul>
				<!-- <ul>
				<li class="header-SignInShare"><h3><spring:theme
									code="header.flyout.credits" /></h3></li>
						<li><a href="<c:url value="/my-account/coupons"/>"><spring:theme
									code="header.flyout.coupons" /></a></li>
				</ul> -->
				<ul>
					<li><h3>
							<spring:theme code="header.flyout.share" />
						</h3></li>
					<li><a href="<c:url value="/my-account/friendsInvite"/>"><spring:theme
								code="header.flyout.invite" /></a></li>

				</ul>

			</div> --%>
			<user:accountLeftNav pageName="orderDetail"/>
			<!----- Left Navigation ENDS --------->

			<!----- RIGHT Navigation STARTS --------->


			<div class="right-account">
				<p class="nav-orderHistory">
					<a href="<c:url value="/my-account/orders"/>" class="order-history"><spring:theme
							code="text.account.orderHistorylink" text="Back to Order History" />
					</a>
				</p>
				<div class="order-history order-details">
					<!-- Heading for saved Cards -->
					<div class="navigation">
						<h1>
							<spring:theme text="Order Details" />
						</h1>


					</div>


					<ul class="product-block order-details">
						<li class="header">

							<ul>
								<li><span><spring:theme
											code="text.orderHistory.order.placed" /></span> <c:if
										test="${not empty orderDate}">${orderDate}</c:if> <%-- <fmt:formatDate
										value="${subOrder.created}" pattern="MMMMM dd, yyyy" /> --%></li>
								<li><span>Total: </span> 
								<!-- TISSIT-1773 -->
								<%-- <format:price	priceData="${subOrder.totalPrice}" /> --%>
								
								<c:choose>
										<c:when test="${subOrder.net}">
											<span class="amt"><format:price
													priceData="${subOrder.totalPriceWithTax}" /></span>
										</c:when>
										<c:otherwise>
											<span class="amt"><format:price
													priceData="${subOrder.totalPriceWithConvCharge}" /></span>
										</c:otherwise>
								</c:choose>
								
								
								</li>
								<li class="recipient"><span><spring:theme
											code="text.orderHistory.recipient" /></span> <c:choose>
										<c:when test="${subOrder.deliveryAddress != null}">
												${subOrder.deliveryAddress.firstName}&nbsp;${subOrder.deliveryAddress.lastName}
										</c:when>
										<c:otherwise>
												${subOrder.mplPaymentInfo.cardAccountHolderName}
												</c:otherwise>
									</c:choose></li>
								<li><span>Order Reference Number: </span> ${subOrder.code}</li>
							</ul>


							<div class="totals">
								<h3>Total:</h3>
								<ul>
									<li><spring:theme code="text.account.order.subtotal"
											/>  <format:price
												priceData="${subOrder.subTotal}" />
									</li>
									<li><spring:theme code="text.account.order.delivery"
											text="Delivery" /><span class="amt"> <format:price
												priceData="${subOrder.deliveryCost}"
												displayFreeForZero="true" />
									</span></li>
									<!-- TISEE-2672 -->
									<c:if test="${subOrder.totalDiscounts.value > 0}">
										<li><spring:theme code="text.account.order.savings"
												text="Discount" /> <span class="amt"> -<format:price
													priceData="${subOrder.totalDiscounts}" />
										</span></li>
									</c:if>
									<!-- TISEE-2672 -->
									
									<!-- TISSTRT-136 -->
									<c:if test="${subOrder.couponDiscount.value > 0}">
										<li><spring:theme code="text.account.order.coupon"
												text="Coupon" /> <span class="amt"> -<format:price
													priceData="${subOrder.couponDiscount}" />
										</span></li>
									</c:if>
									<!-- TISSTRT-136 -->

									<c:if test="${subOrder.mplPaymentInfo.paymentOption eq 'COD'}">
										<li><spring:theme
												code="text.account.order.convinienceCharges"
												text="Convenience Charges" /> <format:price
													priceData="${subOrder.convenienceChargeForCOD}" />
										</li>
									</c:if>
									<%-- <li><spring:theme text="Gift Wrap:" /><span><format:price
												priceData="${subOrder.deliveryCost}"
												displayFreeForZero="true" /></span></li> --%>
									<li class="grand-total">
										<spring:theme code="text.account.order.total.new" text="Total" />
										<c:choose>
											<c:when test="${subOrder.net}">
													<span class="amt"><format:price
															priceData="${subOrder.totalPriceWithTax}" /></span>
												</c:when>
												<c:otherwise>
													<span class="amt"><format:price
															priceData="${subOrder.totalPriceWithConvCharge}" /></span>
												</c:otherwise>
											</c:choose></li>
								</ul>

							</div>
							<div class="payment-method">
								<h3>Payment Method:
									${subOrder.mplPaymentInfo.paymentOption}</h3>
								<c:set var="cardNumberMasked"
									value="${subOrder.mplPaymentInfo.cardIssueNumber}" />
								<c:set var="cardNumberLength"
									value="${fn:length(cardNumberMasked)}" />
								<c:set var="cardNumEnd"
									value="${fn:substring(cardNumberMasked, cardNumberLength-4, cardNumberLength)}" />

								<c:if
									test="${subOrder.mplPaymentInfo.paymentOption eq 'Credit Card'}">
									<c:set var="creditCardBillingAddress"
										value="${subOrder.mplPaymentInfo.billingAddress}" />
								</c:if>
								<!--  TISBOX-1182 -->
								<p>${subOrder.mplPaymentInfo.cardAccountHolderName}</p>
								<c:if
									test="${subOrder.mplPaymentInfo.paymentOption eq 'Credit Card' or 'EMI' or 'Debit Card'}">
									<p>${subOrder.mplPaymentInfo.cardCardType} ending in
										${cardNumEnd}</p>
									<p>Expires on:
										${subOrder.mplPaymentInfo.cardExpirationMonth}/${subOrder.mplPaymentInfo.cardExpirationYear}</p>
								</c:if>
								<c:if
									test="${subOrder.mplPaymentInfo.paymentOption eq 'Netbanking'}">
									<p>${subOrder.mplPaymentInfo.bank}</p>
								</c:if>





								<!-- }
								//TODO
								else if (paymentInfo.getPaymentOption().equalsIgnoreCase(MarketplacecommerceservicesConstants.EMI))
								{
									orderWsDTO.setPaymentCard(paymentInfo.getCardAccountHolderName()); //changed
									orderWsDTO.setPaymentCardDigit(paymentInfo.getCardAccountHolderName());
									if (paymentInfo.getEmiInfo() != null)
									{
										orderWsDTO.setPaymentCardExpire(paymentInfo.getEmiInfo().getTerm());
									}
					
								}
								else if (paymentInfo.getPaymentOption().equalsIgnoreCase(MarketplacecommerceservicesConstants.NETBANKING))
								{
									//reportDTO.setRiskScore();
									orderWsDTO.setPaymentCard(paymentInfo.getCardAccountHolderName()); //changed
									orderWsDTO.setPaymentCardDigit(paymentInfo.getBank());
									orderWsDTO.setPaymentCardExpire("NA");
								} -->
							</div>
							<div class="delivery-address">
								<c:if test="${not empty creditCardBillingAddress.firstName}">
									<h3>Billing Address:</h3>
									<address>
										${fn:escapeXml(creditCardBillingAddress.firstName)}&nbsp;
										${fn:escapeXml(creditCardBillingAddress.lastName)}<br>
										${fn:escapeXml(creditCardBillingAddress.line1)},&nbsp;
										${fn:escapeXml(creditCardBillingAddress.line2)},&nbsp;
										<c:if test="${not empty creditCardBillingAddress.line3}">
														${fn:escapeXml(creditCardBillingAddress.line3)},
													</c:if>
										<br>
										${fn:escapeXml(creditCardBillingAddress.town)},&nbsp;
										<c:if test="${not empty creditCardBillingAddress.state}">
														${fn:escapeXml(creditCardBillingAddress.state)},&nbsp;
													</c:if>
										${fn:escapeXml(creditCardBillingAddress.postalCode)}&nbsp;${fn:escapeXml(creditCardBillingAddress.country.isocode)}
										<br>
										<%-- 91&nbsp;${fn:escapeXml(creditCardBillingAddress.phone)} <br> --%>
									</address>
								</c:if>
								<%-- <c:if test="${empty creditCardBillingAddress.firstName}">
										<address>
											${fn:escapeXml(subOrder.deliveryAddress.firstName)}&nbsp;
											${fn:escapeXml(subOrder.deliveryAddress.lastName)}<br>
											${fn:escapeXml(subOrder.deliveryAddress.line1)},&nbsp;
											${fn:escapeXml(subOrder.deliveryAddress.line2)},
											<c:if test="${not empty subOrder.deliveryAddress.line3}">
														&nbsp;${fn:escapeXml(subOrder.deliveryAddress.line3)},
													</c:if>
											<br>
											${fn:escapeXml(subOrder.deliveryAddress.town)},&nbsp;
											<c:if test="${not empty subOrder.deliveryAddress.state}">
														${fn:escapeXml(subOrder.deliveryAddress.state)},&nbsp;
													</c:if>
											${fn:escapeXml(subOrder.deliveryAddress.postalCode)}&nbsp;IN
											<br>
											91&nbsp;${fn:escapeXml(subOrder.deliveryAddress.phone)} <br>
										</address>
									</c:if> --%>
							</div>
						</li>
	                            <c:set var="button" value="true" />
								<c:set var="entryCount" value="0"></c:set>
								<c:forEach items="${subOrder.sellerOrderList}" var="sellerOrder"
									varStatus="status">
									<c:forEach items="${sellerOrder.entries}" var="entry"
										varStatus="entryStatus">
										<c:set var="entryCount" value="${entryCount +1 }"></c:set>
										
										<c:if test="${entry.mplDeliveryMode.code ne 'click-and-collect'}">
									         <c:set var="HD_ED_Count" value="${HD_ED_Count +1 }" />
									         <c:set var="flag" value="true" />
								        </c:if>
									</c:forEach>
								</c:forEach>
									
								<c:if test="${flag}">
								<li class="item delivered first">
						    	<div class="item-header">
								<c:if test="${entryCount > 1}">
								<h3>${HD_ED_Count} Product(s)-ShippingAddress:</h3>
								</c:if>
								<c:if test="${entryCount  <= 1 }">
									<h3>
										<%-- ${entryCount}&nbsp; --%>
										Shipping Address:
									</h3>
								</c:if>
								<address>
									${fn:escapeXml(subOrder.deliveryAddress.firstName)}&nbsp;
									${fn:escapeXml(subOrder.deliveryAddress.lastName)}<br>
									${fn:escapeXml(subOrder.deliveryAddress.line1)},&nbsp;
									${fn:escapeXml(subOrder.deliveryAddress.line2)},
									<c:if test="${not empty subOrder.deliveryAddress.line3}">
												&nbsp;${fn:escapeXml(subOrder.deliveryAddress.line3)},
											</c:if>
									<br> ${fn:escapeXml(subOrder.deliveryAddress.town)},&nbsp;
									<c:if test="${not empty subOrder.deliveryAddress.state}">
												${fn:escapeXml(subOrder.deliveryAddress.state)},&nbsp;
											</c:if>
									${fn:escapeXml(subOrder.deliveryAddress.postalCode)}&nbsp;${fn:escapeXml(subOrder.deliveryAddress.country.isocode)}
									<br>
									91&nbsp;${fn:escapeXml(subOrder.deliveryAddress.phone)} <br>
								</address>
							</div>
							</c:if>
							<c:forEach items="${filterDeliveryMode}" var="deliveryType">
							 <c:forEach items="${subOrder.sellerOrderList}" var="sellerOrder"
								varStatus="status">
								<input type="hidden" id="subOrderCode"
									value="${sellerOrder.code}" />
								<input type="hidden" id="newCode" value="${subOrder.code}" />
								<c:forEach items="${sellerOrder.entries}" var="entry"
									varStatus="entryStatus">
									<c:if test="${deliveryType eq entry.mplDeliveryMode.code}">
									<c:if
											test="${entry.mplDeliveryMode.code eq 'click-and-collect'}">
								    <c:if test="${storeId ne entry.deliveryPointOfService.address.id}">
									   <c:set var="pos"
																value="${entry.deliveryPointOfService.address}" />
																<li class="item delivered first">
																	<div class="item-header">
															<c:set var="storeId" value="${pos.id}" />
															
											    <c:set value="${subOrder.entries}" var="parentRefEntries" />
	                                            <c:forEach items="${subOrder.entries}" var="parentRefEntry">
		                                           <c:if	test="${parentRefEntry.deliveryPointOfService.address.id eq pos.id}">
		                                                 <h3>${parentRefEntry.quantity} Product(s)-Collect</h3>
		                                          </c:if>
	                                            </c:forEach> 
															<p style="font-size: 12px; font-weight: 600;">Store
																Address:</p>
															<br>
															<br>
						                          <c:if test="${not empty entry.deliveryPointOfService.address}">
															<address
																style="line-height: 18px; font-size: 12px; padding-top: 5px;">
															  <c:if test="${not empty entry.deliveryPointOfService.name}"> ${fn:escapeXml(entry.deliveryPointOfService.name)}<br></c:if>
															  <c:if test="${not empty pos.line1}">	${fn:escapeXml(pos.line1)}&nbsp;</c:if>
															  <c:if test="${not empty pos.line2}">${fn:escapeXml(pos.line2)}&nbsp;</c:if>
															  <c:if test="${not empty pos.state}">${fn:escapeXml(pos.state)},&nbsp;</c:if>
															  <c:if test="${not empty pos.country.name}">${fn:escapeXml(pos.country.name)},&nbsp;</c:if>
															  <c:if test="${not empty pos.postalCode}">${fn:escapeXml(pos.postalCode)}&nbsp;</c:if>
															  <c:if test="${not empty pos.country.isocode}">${fn:escapeXml(pos.country.isocode)}<br></c:if>
															  <c:if test="${not empty pos.phone}">	+91&nbsp; ${fn:escapeXml(pos.phone)} <br></c:if>
															</address>
													</c:if>
															</div>
								  	        </c:if>
									</c:if>
									
									<div class="item-fulfillment">
									<c:if test="${entry.mplDeliveryMode.code ne 'click-and-collect'}">
										<p>
											<spring:message code="mpl.myBag.fulfillment"></spring:message>
											<!-- TISEE-6290 -->
											<c:forEach items="${fullfillmentData}" var="fullfillmentData">
												<c:if test="${fullfillmentData.key == entry.transactionId}">
													<c:set var="fulfilmentValue" value="${fn:toLowerCase(fullfillmentData.value)}"> </c:set>
													<c:choose>
														<c:when test="${fulfilmentValue eq 'tship'}">
															<span><spring:theme code="product.default.fulfillmentType"/> </span>
														</c:when>
														<c:otherwise>
															<span>${entry.selectedSellerInformation.sellername}</span> 
														</c:otherwise>
													</c:choose>
												</c:if>
											</c:forEach>
											<!-- TISEE-6290 -->
										</p>
									</c:if>
										<p>
											<spring:message code="text.orderHistory.seller.order.number"></spring:message>
											<span>${sellerOrder.code}</span>
										</p>
										
											
											
											<!--  Edit button and input box for  pickup Person details -->
											
														<%-- <div id="pickNo" style="font-size: 12px;padding-top: 5px;"> ${sellerOrder.pickupPhoneNumber}<br> </div>  --%>
														&nbsp; &nbsp;
														<c:if test="${entry.mplDeliveryMode.code eq 'click-and-collect'}">
														<c:set var="editButton" value="enable" />  
										                  <c:if test="${button ne false}">  
											             	 <c:choose>
												                <c:when test="${not empty entry.consignment.status}">
												                   <c:set var="status">${entry.consignment.status}</c:set>
												                   <c:forEach items="${subOrderStatus}" var="sellerOrderStatus">
														              <c:if test="${sellerOrderStatus eq status}">
														                 <c:set var="editButton" value="disable" />
														              </c:if>
													              </c:forEach>
												               </c:when> 
												               <c:otherwise>
                                                                 <c:forEach items="${subOrderStatus}" var="sellerOrderStatus">
														             <c:if test="${sellerOrderStatus eq sellerOrder.status}">
														              <c:set var="editButton" value="disable" />
														            </c:if>
														         </c:forEach>
                                                              </c:otherwise>
											               </c:choose>
										               </c:if>
														
												<c:if test="${editButton eq 'enable' and button ne false}">
														<p style="margin-top: -8px;">${entry.mplDeliveryMode.name} :</p> 
														<!-- <div id="pickName" 
														style="font-size: 12px; padding-top: 7px; padding-left: 128px; margin-top: -22px; font-weight: 100;margin-right: 0px !important;margin-left: 0px;"> -->
														<a type="button"  id="pickName" class="pickupeditbtn" style="color: #000;padding-left: 10px;">${sellerOrder.pickupName}</a><!--  </div> -->
														<!-- <a type="button" id="button" class="pickupeditbtn" 
														style="width: 11px; padding-top: 7px; padding-left: -45px; font-weight: 100;margin-left: 15pc;">Edit
													    </a> -->
													  <c:set var="button" value="false" />
													   <div class="container pickup_Edit"
														style="margin-left: 181px; margin-top: -22px;">
														
														<div class="row">
														
															<div class="col-md-5">
														
															<div class="row" style="float: left; z-index: 999;">
																		<div class="col-md-5">
																		
																		  <div class="col-md-5">
																			<label class="pickup_name"
																				style="padding-top: 12px; margin-left: 17px;font-weight: bold;">PickUpName</label>
																		 </div>
																		
																		<div class="col-md-7"
																			style="z-index: 99999 !important;">
																			<input id="pickUpName" class="pickUpName" type="Text" maxlength="30"
																				name="pickUpName1"
																				style="height: 28px; margin-top: 6px; z-index: 119; margin-left: 47px;"
																				value="${sellerOrder.getPickupName()}" /> <br />
																			<div class="error_text pickupPersonNameError"
																				style="width: 115px; font-size: 10px;margin-left: 49px;"></div>
																		</div>
																  </div>
													        </div>
													        </div>
													        
													        <div class="col-md-4" style="z-index: 99;">
																	<div class="row" style="z-index: 99;">
																	<div class="col-md-5">
																			<label class="pickup_mob"
																				style="margin-left: -86px; padding-top: 14px; width: 71px;font-weight: bold;">Mobile
																				No</label>
																		</div>
																		<div class="col-md-7">
																			<input id="pickMobileNo" class="pickMobileNo"
																				type="Text" name="mobileNo"   maxlength="10"
																				style="margin-left: -128px; height: 28px; margin-top: 7px; z-index: 10;"
																				value="${sellerOrder.getPickupPhoneNumber()}" />
																			<div class="error_text pickupPersonMobileError"
																				style="margin-left: -125px; width: 167px; font-size: 10px;"></div>
																				
																			</div>
																			</div>
																		</div>
																	<div class="col-md-1"></div>	
																	<div class="col-md-1">
																	<input type="button" value="Save" class="savebtn"
																		onclick="editPickUpDetails('${subOrder.code}')"
																		style="z-index: 99; min-width: 91px;margin-left: -188px;" />
																    </div>	
																    

															
													   </div>
													   </div>
														</c:if>
														</c:if>
														
													
									</div>
									<br> <br>
									<c:url value="${entry.product.url}" var="productUrl" />
									<c:set var="orderEntrySellerSKU"
										value="${entry.mplDeliveryMode.sellerArticleSKU}" />
									<c:forEach items="${entry.product.seller}" var="seller">
										<c:if
											test="${entry.mplDeliveryMode.sellerArticleSKU eq orderEntrySellerSKU}">
											<c:set var="sellerId" value="${seller.sellerID}" />
											<c:set var="sellerOrderId" value="${sellerOrder.code}" />
										</c:if>
									</c:forEach>

									<div class="order">
										<c:url value="${entry.product.url}" var="productUrl" />
										<div class="image">
											<a href="${productUrl}"> <product:productPrimaryImage
													product="${entry.product}" format="thumbnail" />
											</a>
										</div>
										<div class="details">
											<p>${entry.brandName}</p>
											<h3 class="product-name">
												<a href="${productUrl}">${entry.product.name}</a>
											</h3>
											<div class="attributes">
												<c:if test="${not empty entry.product.size}">
													<p>Size: ${entry.product.size}</p>
												</c:if>
												<c:if test="${not empty entry.product.colour}">
													<p>Color: ${entry.product.colour}</p>
												</c:if>
												<p>
													Price:
													<ycommerce:testId
														code="orderDetails_productTotalPrice_label">
														<format:price priceData="${entry.totalPrice}"
															displayFreeForZero="true" />
													</ycommerce:testId>
												</p>
											</div>
											<c:if test="${not empty entry.imeiDetails}">
												<p>Serial Number: ${entry.imeiDetails.serialNum}</p>
											</c:if>

										</div>
										<div class="actions">
											<c:if
												test="${entry.itemCancellationStatus eq 'true' and entry.giveAway eq false and entry.isBOGOapplied eq false}">
												<c:set var="bogoCheck"
													value="${entry.associatedItems ne null ? 'true': 'false'}"></c:set>
												<a href="" data-toggle="modal"
													data-target="#cancelOrder${sellerOrder.code}${entry.mplDeliveryMode.sellerArticleSKU}${entryStatus.index}"
													data-mylist="<spring:theme code="text.help" />"
													data-dismiss="modal" onClick="refreshModal('${bogoCheck}',${entry.transactionId})"><spring:theme
														text="Cancel Order" /></a>
											</c:if>
											<c:if
												test="${entry.itemReturnStatus eq 'true' and entry.giveAway eq false and entry.isBOGOapplied eq false}">
												<a
													href="${request.contextPath}/my-account/order/returnPincodeCheck?orderCode=${sellerOrder.code}&ussid=${entry.mplDeliveryMode.sellerArticleSKU}&transactionId=${entry.transactionId}">
													<spring:theme code="text.account.returnReplace"
														text="Return Item" />
												</a>
											</c:if>
											<c:if test="${entry.showInvoiceStatus eq 'true'}">
												<a
													href="${request.contextPath}/my-account/order/requestInvoice?orderCode=${sellerOrder.code}&transactionId=${entry.transactionId}"
													onclick="callSendInvoice();"><spring:theme
														code="text.account.RequestInvoice" text="Request Invoice" /></a>
											</c:if>
										</div>

										<div class="modal cancellation-request fade"
											id="cancelOrder${sellerOrder.code}${entry.mplDeliveryMode.sellerArticleSKU}${entryStatus.index}">

											<div class="content">
												<!-- 	<button type="button" class="close pull-right" 		
										           aria-hidden="true" data-dismiss="modal">		
										            </button> -->
												<div class="cancellation-request-block">
													<h2>Request Cancellation</h2>
													<!-- ../my-account/returnSuccess -->
													<form:form class="return-form"
														id="returnRequestForm${entryStatus.index}${sellerOrder.code}"
														action="../my-account/returnSuccess" method="post"
														commandName="returnRequestForm">
														<div class="return-container">
															<ul class="products">
																<c:forEach
																	items="${cancelProductMap[entry.orderLineId]}"
																	var="entryCancel">
																	<li class="item look">
																		<ul class="product-info">
																			<li>
																				<div class="product-img">
																					<a href="${productUrl}"> <product:productPrimaryImage
																							product="${entryCancel.product}"
																							format="thumbnail" />
																					</a>
																				</div>
																				<div class="product">
																					<!-- <p class="company">Nike</p> -->
																					<h3 class="product-name">
																						<a href="${productUrl}">${entryCancel.product.name}</a>
																					</h3>

																					<p class="item-info">
																						<span><b><c:if
																									test="${entryCancel.quantity > 1}">
																									<spring:theme code="text.orderHistory.quantity" />
																				&nbsp;${entryCancel.quantity}
																				</c:if></b> </span>

																						<c:if test="${not empty entryCancel.product.size}">
																							<span><b>Size:</b>
																								${entryCancel.product.size}</span>
																						</c:if>
																						<c:if
																							test="${not empty entryCancel.product.colour}">
																							<span><b>Color:</b>
																								${entryCancel.product.colour}</span>
																						</c:if>

																						<span class="price"><b>Price:</b> <ycommerce:testId
																								code="orderDetails_productTotalPrice_label">
																								<format:price
																									priceData="${entryCancel.totalPrice}"
																									displayFreeForZero="true" />
																							</ycommerce:testId> </span>
																						<c:if test="${not empty entryCancel.imeiDetails}">
																							<span><b>Serial Number:</b>
																								${entryCancel.imeiDetails.serialNum}</span>
																						</c:if>
																						<span class="sellerOrderNo"><b> <spring:theme
																									code="text.orderHistory.seller.order.number" />
																						</b> ${sellerOrder.code} </span>
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
															</div>
															<c:set var="ussidClass" value="${orderEntrySellerSKU}"></c:set>
															<!-- <c:forEach items="${entry.associatedItems}" var="associatedUssid">
															<c:set var="ussidClass" value="${ussidClass},${associatedUssid}" />
														</c:forEach>  -->
															<form:hidden path="ticketTypeCode"
																class="ticketTypeCodeClass" value="C" />
															<form:hidden path="orderCode" id="orderCode"
																class="subOrderCodeClass" value="${sellerOrder.code}" />
															<form:hidden path="transactionId"
																class="transactionIdClass"
																value="${entry.transactionId}" />
															<form:hidden path="ussid" class="ussidClass"
																value="${ussidClass}" />

															<input type="hidden" class="entryNumberClass"
																id="entryNumber" value="${entry.entryNumber}" />
														</div>
														<div class="buttons">
															<a class="close" data-dismiss="modal">Close</a>
															<button type="button"
																class="light-red cancel-confirm-detail" id="myaccount"
																data-dismiss="modal">Confirm Cancellation</button>
														</div>

													</form:form>

												</div>
												<!-- <button class="close" data-dismiss="modal"></button> -->
											</div>
											<div class="overlay" data-dismiss="modal"></div>
										</div>
										<div class=" modal account active fade"
											id="cancelSuccess${sellerOrder.code}${entry.mplDeliveryMode.sellerArticleSKU}">
											<div class="content">
												<button type="button" id="returnReject"
													onclick="reloadOrderDetailPage()" class="close pull-right"
													aria-hidden="true" data-dismiss="modal"></button>
												<div class="cancellation-request-block success">
													<h2>Return Cancellation</h2>

													<div>
														<h3>
															<span id="resultTitle"></span>
														</h3>
														<div>
															<span id="resultDesc"></span>
														</div>
													</div>
													<form class="return-form">
														<div class="return-container container">
															<ul class="products">
																<c:forEach
																	items="${cancelProductMap[entry.orderLineId]}"
																	var="entryCancel">
																	<li class="item look">
																		<ul class="product-info">
																			<li>
																				<div class="product-img">
																					<a href="${productUrl}"> <product:productPrimaryImage
																							product="${entryCancel.product}"
																							format="thumbnail" />
																					</a>
																				</div>
																				<div class="product">
																					<!-- <p class="company">Nike</p> -->
																					<h3 class="product-name">
																						<a href="${productUrl}">${entryCancel.product.name}</a>
																					</h3>

																					<p class="item-info">
																						<span><b><c:if
																									test="${entryCancel.quantity > 1}">
																									<spring:theme code="text.orderHistory.quantity" />
																				&nbsp;${entryCancel.quantity}
																				</c:if></b> </span>

																						<c:if test="${not empty entryCancel.product.size}">
																							<span><b>Size:</b>
																								${entryCancel.product.size}</span>
																						</c:if>
																						<c:if
																							test="${not empty entryCancel.product.colour}">
																							<span><b>Color:</b>
																								${entryCancel.product.colour}</span>
																						</c:if>

																						<span class="price"><b>Price:</b> <ycommerce:testId
																								code="orderDetails_productTotalPrice_label">
																								<format:price
																									priceData="${entryCancel.totalPrice}"
																									displayFreeForZero="true" />
																							</ycommerce:testId> </span>
																						<c:if test="${not empty entryCancel.imeiDetails}">
																							<span><b>Serial Number:</b>
																								${entryCancel.imeiDetails.serialNum}</span>
																						</c:if>
																						<span class="sellerOrderNo"><b> <spring:theme
																									code="text.orderHistory.seller.order.number" />
																						</b> ${sellerOrder.code} </span>
																					</p>
																					<%-- <form:hidden path="sellerId" value="${sellerId}" /> --%>
																				</div>
																			</li>
																		</ul>
																	</li>
																</c:forEach>
															</ul>
															<div class="reason questions">
																<label id="reasonTitle">Reason for Cancellation:</label>
																<span id="reasonDesc"></span>
															</div>
														</div>
														<div class="buttons">
															<button type="button" class="light-red blue"
																id="returnReject" onclick="reloadOrderDetailPage()">Close</button>

														</div>
													</form>
												</div>

											</div>
											<div class="overlay fade" data-dismiss="modal"></div>
										</div>
										<!-- Start Order Tracking diagram -->

										<c:set value="${trackStatusMap[entry.orderLineId]}"
											var="trackStatus" />
										<c:set value="${trackStatus['APPROVED']}" var="approvedStatus" />
										<c:set value="${trackStatus['PROCESSING']}"
											var="processingStatus" />
										<c:set value="${trackStatus['SHIPPING']}" var="shippingStatus" />
										<c:set value="${trackStatus['DELIVERY']}" var="deliveryStatus" />
										<c:set value="${trackStatus['CANCEL']}" var="cancelStatus" />
										<c:set value="${trackStatus['RETURN']}" var="returnStatus" />
										<c:set var="productDelivered" value="0"></c:set>
										 <!-- For RTO handling -->
											<c:forEach items="${shippingStatus}" var="productStatus"
												varStatus="loop">
												<c:if test="${productStatus.responseCode eq 'DELIVERED' or currentStatusMap[entry.orderLineId] eq 'ORDER_COLLECTED'}">
										 	<c:set var="productDelivered" value="1"></c:set>
										  </c:if>
										  </c:forEach>
										<div class="deliveryTrack status"
											id="tracker_${entry.transactionId}">
											<ul class="nav">

												<li>Approval</li>

												<c:choose>
													<c:when
														test="${(fn:length(cancelStatus) gt 0 && fn:length(processingStatus) gt 0) || fn:length(cancelStatus) eq 0}">
														<li>Processing</li>
													</c:when>

												</c:choose>


												<c:if test="${fn:length(cancelStatus) gt 0}">
													<li>Cancel</li>
												</c:if>
													<c:choose>
														<c:when
															test="${entry.mplDeliveryMode.code eq 'click-and-collect'}">
															<c:if test="${fn:length(cancelStatus) eq 0}">
																<li>READY for PickUp</li>
															</c:if>
														</c:when>
														<c:otherwise>
															<c:if test="${fn:length(cancelStatus) eq 0}">
																<li>Shipping</li>
															</c:if>
														</c:otherwise>
													</c:choose>

												<!-- For RTO handling productDelivered -->
													<c:choose>
														<c:when
															test="${entry.mplDeliveryMode.code eq 'click-and-collect'}">
															<c:if
																test="${fn:length(cancelStatus) eq 0  and not(productDelivered eq '0' and fn:length(returnStatus) gt 0)}">
																<li>PickedUp</li>
															</c:if>
														</c:when>
														<c:otherwise>
															<c:if
																test="${fn:length(cancelStatus) eq 0  and not(productDelivered eq '0' and fn:length(returnStatus) gt 0)}">
																<li>Delivery</li>
															</c:if>
														</c:otherwise>

													</c:choose>

													<c:if
														test="${fn:length(returnStatus) gt 0 and fn:length(cancelStatus) eq 0}">
														<li>Return</li>
													</c:if>

												<%-- <c:if
													test="${fn:length(cancelStatus) eq 0 and fn:length(returnStatus) gt 0 }">
													<li>Delivery</li>
												</c:if>
												--%>
											</ul>
											<ul class="progtrckr tabs">
												
												<!-------------------------------- Approval Block --------------------------------------->
												<c:set var="displayMsgVar" value="" />
												<li class="progress progtrckr-done orderStatus processing"
													orderlineid="${entry.orderLineId}"
													ordercode="${subOrder.code}">
													<span class="start"></span>
													<c:set value="0" var="dotCount" /> 
													<!-- Show only first and last result to restrict in 2 dots-->
													<c:forEach items="${approvedStatus}" var="productStatus" varStatus="loop">

														<c:choose>
															<c:when test="${productStatus.isSelected eq true && productStatus.isEnabled eq true}">
																<span class="dot trackOrder_${productStatus.colorCode}" index="${loop.index}"> 
																	<img src="${commonResourcePath}/images/thin_top_arrow_222.png" class="dot-arrow">
																</span>
																<c:set var="dotCount" value="${dotCount + 1}" />
															</c:when>
														</c:choose>

														<!-- Prepare popup message  -->
															<c:if test="${productStatus.isEnabled eq true}">
																<c:set var="displayMsgVar" value="${displayMsgVar}<p>${productStatus.shipmentStatus}</p>" />
																<c:forEach items="${productStatus.statusRecords}" var="statusRecord">
																	<c:set var="displayMsgVar" value="${displayMsgVar}<ul><li>${statusRecord.statusDescription}</li></ul>" />
																</c:forEach>
															</c:if>
															
														<!-- Prepare popup message  ends -->

														<c:set value="${ (productStatus.responseCode eq currentStatusMap[entry.orderLineId]) ? 'block' : 'none'}" var="showBlock" />

															<c:if test="${productStatus.isSelected eq true}">
																<div class="order message trackOrdermessage_${productStatus.colorCode} order-placed-arrow"
																	id="orderStatus${entry.orderLineId}_${loop.index}"
																	style="display: ${showBlock};">${displayMsgVar}
																</div>
															</c:if>

														<!-- setting to default if both enabled and selected are true -->
															<c:if test="${productStatus.isSelected eq true && productStatus.isEnabled eq true}">
																<c:set var="displayMsgVar" value="" />
															</c:if>

												</c:forEach> 
												
												<c:forEach var="i" begin="${dotCount}" end="0">
															<span class="dot inactive"></span>
														</c:forEach> <span class="end "></span>
												<span class="end "></span>
											</li>
											<!--End Approval Block -->

												<!-------------------------------------------- Processing Block ---------------------------->
												<c:set var="displayMsgVar" value="" />
												<c:if
													test="${(fn:length(cancelStatus) gt 0 && fn:length(processingStatus) gt 0) || fn:length(cancelStatus) eq 0}">

													<li
														class="progress progtrckr-done processingStatus processing"
														orderlineid="${entry.orderLineId}"
														ordercode="${subOrder.code}">
														<c:set value="${0}" var="dotCount" /> 
														
														<c:forEach items="${processingStatus}" var="productStatus" varStatus="loop">
														
															<c:if test="${loop.last}">
																<c:choose>
																	<c:when
																		test="${productStatus.isSelected eq true && productStatus.isEnabled eq true}">
																		<span class="dot  trackOrder_${productStatus.colorCode}" index="${loop.index}"> <img
																			src="${commonResourcePath}/images/thin_top_arrow_222.png"
																			class="dot-arrow">
																		</span>
																		<c:set var="dotCount" value="${dotCount + 1}" />
																	</c:when>
																</c:choose>

																<!-- Prepare popup message  -->
																<c:if test="${productStatus.isEnabled eq true}">
																	<c:set var="displayMsgVar"
																		value="${displayMsgVar}<p>${productStatus.shipmentStatus}</p>" />

																	<c:forEach items="${productStatus.statusRecords}"
																		var="statusRecord">
																		<c:set var="displayMsgVar"
																			value="${displayMsgVar}<ul><li>${statusRecord.statusDescription}</li></ul>" />
																	</c:forEach>
																</c:if>
																<!-- Prepare popup message  ends -->

																<c:set
																	value="${ (productStatus.responseCode eq currentStatusMap[entry.orderLineId]) ? 'block' : 'none'}"
																	var="showBlock" />

																<c:if test="${productStatus.isSelected eq true}">
																	<div class="order message trackOrdermessage_${productStatus.colorCode} processing-stage-arrow1"
																		id="processingStatus${entry.orderLineId}_${loop.index}"
																		style="display: ${showBlock};">${displayMsgVar}

																	</div>

																</c:if>

																<!-- setting to default if both enabled and selected are true -->
																<c:if
																	test="${productStatus.isSelected eq true && productStatus.isEnabled eq true}">
																	<c:set var="displayMsgVar" value="" />
																</c:if>

															</c:if>

														</c:forEach> <c:forEach var="i" begin="${dotCount}" end="0">
															<span class="dot inactive"></span>
														</c:forEach> <span class="end "></span>

													</li>
												</c:if>
												<!--End Processing Block -->


												<%-- <c:choose>
												<c:when test="${fn:length(cancelStatus) gt 0}"> --%>

												<c:if test="${fn:length(cancelStatus) gt 0}">
													<!--------------------------------------- Cancel Block ------------------------------------------>
													<c:set var="displayMsgVar" value="" />
													<li class="progress progtrckr-done cancelStatus processing"
														orderlineid="${entry.orderLineId}"
														ordercode="${subOrder.code}"><c:set value="${0}"
															var="dotCount" /> <c:forEach items="${cancelStatus}"
															var="productStatus" varStatus="loop">

															<c:choose>
																<c:when
																	test="${productStatus.isSelected eq true && productStatus.isEnabled eq true}">
																	<span class="dot trackOrder_${productStatus.colorCode}" index="${loop.index}">
																		<img
																		src="${commonResourcePath}/images/thin_top_arrow_222.png"
																		class="dot-arrow">
																	</span>
																	<c:set var="dotCount" value="${dotCount + 1}" />
																</c:when>
															</c:choose>

															<!-- Prepare popup message  -->
															<c:if test="${productStatus.isEnabled eq true}">
																<c:set var="displayMsgVar"
																	value="${displayMsgVar}<p>${productStatus.shipmentStatus}</p>" />

																<c:forEach items="${productStatus.statusRecords}"
																	var="statusRecord">
																	<c:set var="displayMsgVar"
																		value="${displayMsgVar}<ul><li>${statusRecord.statusDescription}</li></ul>" />
																</c:forEach>
															</c:if>
															<!-- Prepare popup message  ends -->

															<c:set
																value="${ (productStatus.responseCode eq currentStatusMap[entry.orderLineId]) ? 'block' : 'none'}"
																var="showBlock" />

															<c:if test="${productStatus.isSelected eq true}">
																<div class="cancellation message trackOrdermessage_${productStatus.colorCode}"
																	id="cancellation${entry.orderLineId}_${loop.index}"
																	style="display: ${showBlock}">${displayMsgVar}</div>
															</c:if>

															<!-- setting to default if both enabled and selected are true -->
															<c:if
																test="${productStatus.isSelected eq true && productStatus.isEnabled eq true}">
																<c:set var="displayMsgVar" value="" />
															</c:if>

														</c:forEach> <c:forEach var="i" begin="${dotCount}" end="1">
															<span class="dot inactive"></span>
														</c:forEach> <span class="end "></span></li>
													<!--End Cancel Block -->
												</c:if>
												<%-- </c:when>
												<c:otherwise> --%>

												<%-- </c:otherwise>
												</c:choose> --%>




												<!------------------------------- Shipping Block --------------------------------->
												<c:if test="${fn:length(cancelStatus) eq 0}">
													<c:set var="displayMsgVar" value="" />
													<li
														class="progress progtrckr-done shippingStatus processing"
														orderlineid="${entry.orderLineId}"
														ordercode="${subOrder.code}">
														<!-- <span class="start"></span> --> <c:set value="${0}"
															var="dotCount" /> <c:forEach items="${shippingStatus}"
															var="productStatus" varStatus="loop">

															<c:choose>
																<c:when
																	test="${productStatus.isSelected eq true && productStatus.isEnabled eq true}">
																	<span class="dot trackOrder_${productStatus.colorCode}" index="${loop.index}"> <img
																		src="${commonResourcePath}/images/thin_top_arrow_222.png"
																		class="dot-arrow">
																	</span>
																	<c:set var="dotCount" value="${dotCount + 1}" />
																</c:when>
															</c:choose>

															<!-- Prepare popup message  -->
															<c:if test="${productStatus.isEnabled eq true}">
																<c:set var="displayMsgVar"
																	value="${displayMsgVar}<p>${productStatus.shipmentStatus}</p>" />

																<c:forEach items="${productStatus.statusRecords}"
																	var="statusRecord">
																	<c:set var="displayMsgVar"
																		value="${displayMsgVar}
																		<ul>
																		<li>${statusRecord.statusDescription}</li>
																		</ul>" />
																</c:forEach>

															</c:if>

															<!-- Prepare popup message  ends -->


															<c:set
																value="${ (productStatus.responseCode eq currentStatusMap[entry.orderLineId]) ? 'block' : 'none'}"
																var="showBlock" />
															<c:if test="${productStatus.isSelected eq true}">
																<div class="order message trackOrdermessage_${productStatus.colorCode} shipping tracking-information"
																	id="shippingStatus${entry.orderLineId}_${loop.index}"
																	style="display: ${showBlock}">

																		${displayMsgVar}
																		<!-- TISEE-5433 -->
																		<c:if
																			test="${not empty logistic[entry.orderLineId] and fn:toLowerCase(logistic[entry.orderLineId]) ne 'null' and entry.mplDeliveryMode.code ne 'click-and-collect'
																			}">
																			<p>Logistics: ${logistic[entry.orderLineId]}</p>
																		</c:if>
																		<c:if
																			test="${not empty awbNum[entry.orderLineId] and fn:toLowerCase(awbNum[entry.orderLineId]) ne 'null' and entry.mplDeliveryMode.code ne 'click-and-collect'
																			}">
																			<c:choose>
																				<c:when
																					test="${not empty trackingurl[entry.orderLineId]}">
																					<p>
																						AWB No. <a
																							href="${trackingurl[entry.orderLineId]}">${awbNum[entry.orderLineId]}</a>
																				</c:when>
																				<c:otherwise>
																					<p>AWB No. ${awbNum[entry.orderLineId]}</p>
																				</c:otherwise>
																			</c:choose>
																		</c:if>

																		<c:if test="${productStatus.responseCode ne 'DELIVERED'}">
																			<c:if test="${entry.mplDeliveryMode.code ne 'click-and-collect'}">
																			<div id="track-more-info">
																				<p class="active">
																					<span class="view-more-consignment"
																						orderlineid="${entry.orderLineId}"
																						index="${loop.index}" ordercode="${subOrder.code}">
																						View more
																					</span>
																				</p>
																				<p>
																					<span class="view-more-consignment"
																						orderlineid="${entry.orderLineId}"
																						index="${loop.index}" ordercode="${subOrder.code}">View less</span>
																				</p>
																		  </div>
																		  <div id="shippingStatusRecord${entry.orderLineId}_${loop.index}" class="view-more-consignment-data"></div>
																	 </c:if>
																	 </c:if>
																	
																</div>
															</c:if>

															<!-- setting to default if both enabled and selected are true -->
															<c:if
																test="${productStatus.isSelected eq true && productStatus.isEnabled eq true}">
																<c:set var="displayMsgVar" value="" />
															</c:if>
															 
														</c:forEach> <c:forEach var="i" begin="${dotCount}" end="1">
															<span class="dot inactive"></span>
														</c:forEach> <!-- <span class="end"></span> -->
													</li>
												</c:if>
												<!-- End Shipping Block -->
												<!------------------------------- Delivery Block ------------------------------------->
												 <!-- For RTO handling productDelivered -->
												<c:if
													test="${fn:length(cancelStatus) eq 0  and not(productDelivered eq '0' and fn:length(returnStatus) gt 0)}">
													<li class="progress progtrckr-done delivery-status">
														<p> 
															<c:if test="${not empty orderActualDeliveryDateMap[entry.orderLineId]}">		
																	${orderActualDeliveryDateMap[entry.orderLineId]}
															</c:if>
														</p>
													</li>
												</c:if>
												
												<!------------------------------- Delivery Block ------------------------------------->
												<%-- 
												 <c:if
													test="${fn:length(cancelStatus) eq 0 }">
													<li class="progress progtrckr-done delivery-status">
														<p>
															<c:if
																test="${not empty orderActualDeliveryDateMap[entry.orderLineId]}">														
														${orderActualDeliveryDateMap[entry.orderLineId]}
														</c:if>

														</p>
													</li>
												</c:if>
												--%>
												<!-- End Delivery Block -->
												
												
												<!-- End Delivery Block -->

												<c:if test="${fn:length(returnStatus) gt 0 and fn:length(cancelStatus) eq 0}">
													<!--------------------------------- Return Block -------------------------------------->
													<c:set var="displayMsgVar" value="" />
													<li class="progress progtrckr-done returnStatus processing"
														orderlineid="${entry.orderLineId}"
														ordercode="${subOrder.code}"><span
														class="start return-start"></span> <c:set value="${0}"
															var="dotCount" /> <c:forEach items="${returnStatus}"
															var="productStatus" varStatus="loop">
															<c:choose>
																<c:when
																	test="${productStatus.isSelected eq true && productStatus.isEnabled eq true}">
																	<span class="dot trackOrder_${productStatus.colorCode}" index="${loop.index}"> <img
																		src="${commonResourcePath}/images/thin_top_arrow_222.png"
																		class="dot-arrow">
																	</span>
																	<c:set var="dotCount" value="${dotCount + 1}" />
																</c:when>
															</c:choose>

															<!-- Prepare popup message  -->
															<c:if test="${productStatus.isEnabled eq true}">
																<c:set var="displayMsgVar"
																	value="${displayMsgVar}<p>${productStatus.shipmentStatus}</p>" />

																<c:forEach items="${productStatus.statusRecords}"
																	var="statusRecord">
																	<c:set var="displayMsgVar"
																		value="${displayMsgVar}<ul><li>${statusRecord.statusDescription}</li></ul>" />
																</c:forEach>
															</c:if>
															<!-- Prepare popup message  ends -->

															<c:set
																value="${ (productStatus.responseCode eq currentStatusMap[entry.orderLineId]) ? 'block' : 'none'}"
																var="showBlock" />

															<c:if test="${productStatus.isSelected eq true}">
																<div class="return message trackOrdermessage_${productStatus.colorCode}"
																	id="return${entry.orderLineId}_${loop.index}"
																	style="display: ${showBlock};">

																	${displayMsgVar}
																	<!-- TISEE-5433 -->
																	<c:if test="${not empty returnLogistic[entry.orderLineId] and fn:toLowerCase(returnLogistic[entry.orderLineId]) ne 'null' and entry.mplDeliveryMode.code ne 'click-and-collect'
																	}">
																 	<p>Logistic : ${returnLogistic[entry.orderLineId]}</p>
																 	</c:if>
																 	<c:if test="${not empty returnAwbNum[entry.orderLineId] and fn:toLowerCase(returnAwbNum[entry.orderLineId]) ne 'null' and entry.mplDeliveryMode.code ne 'click-and-collect'
																 	}">
																 		<c:choose>
																 		<c:when test="${not empty trackingurl[entry.orderLineId]}">
																 			<p>AWB No. <a href="${trackingurl[entry.orderLineId]}">${returnAwbNum[entry.orderLineId]}</a>
																 		</c:when>
																 		<c:otherwise>
																 			<p>AWB No. ${returnAwbNum[entry.orderLineId]}</p>
																 		</c:otherwise>
																 		</c:choose>	
																 	</c:if>
																	<%-- 		
																	<div id="track-more-info-return">
																		<p class="active">
																			<span class="view-more-consignment-return"
																				orderlineid="${entry.orderLineId}"
																				index="${loop.index}" ordercode="${subOrder.code}">View
																				more</span>
																		</p>
																		<p>
																			<span>View less</span>
																		</p>
																	</div>

																	<div
																		id="returnRecord${entry.orderLineId}_${loop.index}">

																	</div>
--%>
																</div>
															</c:if>

															<!-- setting to default if both enabled and selected are true -->
															<c:if
																test="${productStatus.isSelected eq true && productStatus.isEnabled eq true}">
																<c:set var="displayMsgVar" value="" />
															</c:if>
														</c:forEach> <c:forEach var="i" begin="${dotCount}" end="1">
															<span class="dot inactive"></span>
														</c:forEach> <span class="end return-end"></span></li>
												</c:if>
												<!--End Return Block -->
												
											</ul>

										</div>
										<!-- End Order Tracking diagram -->
									</div>

                                   </c:if>
								</c:forEach>
								</c:forEach>
							</c:forEach>
						</li>



					</ul>
				</div>
			</div>




		</div>
	</div>
</template:page>
<%-- <script type="text/javascript"
	src="${commonResourcePath}/js/jquery-2.1.1.min.js"></script>
<template:javaScriptVariables />
<script type="text/javascript"
	src="${commonResourcePath}/js/acc.accountaddress.js"></script> --%>
<script>

/*--------- Start of track order UI -------*/

$(function(){
	$('body .right-account .order-details .deliveryTrack ul.nav').each(function(){
		var track_order_length= $(this).find('li').length;
		var track_order_children_width= 100 / track_order_length;
		var track_order_message_width= 100 * track_order_length;
		$(this).find('li').css('width',track_order_children_width+"%");
		$(this).next().find('li.progress').css('width',track_order_children_width+"%");
		$(this).next().find('li.progress').find('div.message').css('width',track_order_message_width+"%");
	
		
		if (($(this).next().offset() != undefined) && ($(this).next().find(".returnStatus").offset() != undefined)){
			var track_order_parent_windowOffset=$("ul.progtrckr").offset().left;
			var return_message_div_position=$("ul.progtrckr").offset().left - $(this).next().find(".returnStatus").offset().left;
			$(this).next().find('li.progress').find(".return.message").css("left",return_message_div_position);
		}
		if (($(this).next().offset() != undefined) && ($(this).next().find(".cancelStatus").offset() != undefined)){
			var cancel_order_parent_windowOffset=$("ul.progtrckr").offset().left;
			var cancel_message_div_position=$("ul.progtrckr").offset().left - $(this).next().find(".cancelStatus").offset().left;
			$(this).next().find('li.progress').find(".cancellation.message").css("left",cancel_message_div_position);
		}
	});
	/*$(".tracking-information").each(function(){
	
			//$(".view-more-consignment-data").hide();
			 $(this).find("#track-more-info p.active").click(function(){
				alert();
				$(this).parent().siblings(".view-more-consignment-data").slideToggle();
				$(this).toggleClass("active");
				$(this).siblings().toggleClass("active");
				//$(this).parents("#tracking-order").toggleClass("track-order-height");
			});
 	}); */
	
});

/*--------- END of track order UI -------*/



$(function() {
		//loadAWBstatus();
		
		$(".dot").each(function () {
			
		   $(this).mouseenter(function() {
		   	var orderLineId = $(this).parent().attr("orderlineid");
			var orderCode =$(this).parent().attr("ordercode");
			var index = $(this).attr("index");
		
			
			if($(this).parent().hasClass("orderStatus")){
				hideAll(orderLineId);
				showDiv("orderStatus" + orderLineId+"_"+index);
			}
			if($(this).parent().hasClass('processingStatus')){
				//alert("2")
				hideAll(orderLineId);
				showDiv("processingStatus" + orderLineId+"_"+index);
			}
			if($(this).parent().hasClass('cancelStatus')){
				hideAll(orderLineId);
				showDiv("cancellation" + orderLineId+"_"+index);
			}
			if($(this).parent().hasClass('shippingStatus')){
				hideAll(orderLineId);
				//checkAWBstatus(orderLineId,orderCode,"shippingStatusRecord" + orderLineId+"_"+index);
				showDiv("shippingStatus" + orderLineId+"_"+index);
			}
			if($(this).parent().hasClass('returnStatus')){
				hideAll(orderLineId);
				//checkAWBstatus(orderLineId,orderCode,"returnRecord" + orderLineId+"_"+index);
				showDiv("return" + orderLineId+"_"+index);
			}
			$(this).parents(".progtrckr").find('.dot-arrow').hide();
				$(this).find('.dot-arrow').stop(true, true).fadeIn();
		   });
		});
				
		$(".view-more-consignment").each(function () {
			$(this).parents('.tracking-information').find(".view-more-consignment-data").hide();
			$(this).click(function() {
			   	var orderLineId = $(this).attr("orderlineid");
				var orderCode =$(this).attr("ordercode");
				var index = $(this).attr("index");
				checkAWBstatus(orderLineId,orderCode,"shippingStatusRecord" + orderLineId+"_"+index,"N");
					$(this).parent().toggleClass("active");
					$(this).parent().siblings().toggleClass("active");				
			});
		});
		

		$(".view-more-consignment-return").each(function () {
			$(this).click(function() {
			   	var orderLineId = $(this).parent().attr("orderlineid");
				var orderCode =$(this).parent().attr("ordercode");
				var index = $(this).attr("index");
				checkAWBstatus(orderLineId,orderCode,"returnRecord" + orderLineId+"_"+index,"Y");
			});
		}); 
			
});

	function showCancelDiv(orderLineId) {
		var divId='cancellation' + orderLineId;
		showDiv(divId);

	}
	function hideAll(orderLineId) {
		$("li[orderlineid*='"+orderLineId+"']").each(function () {
	          //alert("Hi")
			if ($(this).children('div.message').length > 0) {
				//alert("hide");
				$(this).children('div.message').css("display","none")
	        }
	    });
	}

	function checkAWBstatus(orderLineId,orderCode,divId,returnAWB) {
		//var divId='shippingStatusRecord' + orderLineId;
		
		var enable=false;
		//alert(divId);
		var shipHTML=$('#'+divId+"_ul").html();
		if(shipHTML!=undefined){
			shipHTML= shipHTML.trim();
			enable=(shipHTML==="") ? true : false;
		}
		//alert(enable);
		//if(enable){
			$.ajax({
					dataType : "json",
					url : "/store/mpl/en/my-account/AWBStatusURL",
					data : {
						"orderLineId" : orderLineId,
						"orderCode" : orderCode,
						"returnAWB" : returnAWB
					},
					success : function(data) {
						//alert(data);
						var htmlData = "<ul>"
										+"<table width='100%' class='track-table-head' style='text-align:left;text-transform:capitalize'>"
											+"<tr>"
												+"<td width='25%'>Date</td>"
												+"<td width='25%'>Time</td>"
												+"<td width='25%'>Location</td>"
												+"<td width='25%'>Description</td>"
											+"</tr>";
										+"</table>"
										+"</ul>";
						htmlData = htmlData +"<ul><table class='track-table' width='100%' style='text-transform:capitalize'>";
						$.each(data.statusRecords,
								function(key, itemVal) {
								//	alert(itemVal)
									/* htmlData += "<li id='" + key + "'>"
											+ itemVal.date + "-" + itemVal.time
											+ " - " + itemVal.location + "-"
											+ itemVal.statusDescription
											+ "</li>"; */
									htmlData += "<tr align='left' id='"+key+"'>"
											+"<td width='25%'>"+ itemVal.date +"</td>"
											+"<td width='25%'>"+ itemVal.time +"</td>"
											+"<td width='25%'>"+ itemVal.location +"</td>"
											+"<td width='25%'>"+ itemVal.statusDescription +"</td>"
										+"</tr>";
								});
						htmlData +="</table></ul>";
						//alert(htmlData);
						$("#"+divId).html(htmlData);
						showDiv(divId);
					}
			});
		//}else{
		//	showDiv(divId);
		//}
	}
	function showDiv(divId) {
		
		if (false == $("#" + divId).is(':visible')) {
			$("#" + divId).stop(true, true).fadeIn();
			
		} else {
			$("#" + divId).stop(true, true).fadeOut();
		}
	}
	
	 function editPickUpDetails(orderId) {
		      var name=$("#pickUpName").val();
		      var mobile=$("#pickMobileNo").val(); 	 
		      var isString = isNaN(mobile);
		      var mobile=mobile.trim();
		      var regExp = new RegExp("^[a-zA-Z]+[ ]?[a-zA-Z]+$");
		      $(".pickupPersonNameError, .pickupPersonMobileError").hide();
		       if(name.length <= 3 ){    
		    	     $(".pickupPersonNameError").show();
		    	     $(".pickupPersonNameError").text("Enter Atleast 4 Letters");
		      }
		       else if(regExp.test(name) == false){
		    	     $(".pickupPersonNameError").show();
		    	     $(".pickupPersonNameError").text("Enter only Alphabet");
		       }	       
		       else if(isString==true || mobile.trim()==''){
		    	  $(".pickupPersonMobileError").show();
		          $(".pickupPersonMobileError").text("Enter only numbers");
		      }else if(mobile.length<=9 || mobile.length >= 11) {   
		    	  $(".pickupPersonMobileError").show();
		          $(".pickupPersonMobileError").text("Enter 10 Digit Number");
		      }	
		      else 
		      {     
		      $.ajax({	  
					type: "POST",
					url: ACC.config.encodedContextPath + "/my-account/updatepickUp_Details",
				    data: "orderId="+orderId + "&name=" + name+ "&mobile="+mobile,
					success: function (response) {
					    	var status=response; 	 	
					    	
					    	if(status="sucess"){
					    	
					    		
					    			$("#pickName").text(name);
					    			$("#pickNo").text(mobile);
					    			$(".pickup_Edit").css("display","none");
					    			$(".pickupeditbtn").css("display","block");
					    			if(status="sucess"){
							    		$.ajax({	  
											type: "POST",
											url: ACC.config.encodedContextPath + "/my-account/crmTicketCreateUpdatePickUpDetail",
										    data: "orderId="+orderId,
											success: function () {	
											}
										});
							    		
							    	}	      
					    	}
					    	
						}
				});
		      } 
	}	 
	$(document).ready(function(){
		    var length = $(".returnStatus .dot").length;
		    var percent = 100/parseInt(length);
		    $(".returnStatus .dot").css("width", percent+"%");
		    
		 $(".pickupeditbtn").click(function(){
			
		
			$(".pickup_Edit").css("display","block");
			$(".pickupeditbtn").css("display","none");		
		});
		 $(".savebtn").click(function(){	
		
			// $(".pickupeditbtn").css("display","block");
			 
		 });
		 //$(".pickupeditbtn").hide(); 
	 });	 
	
</script>
