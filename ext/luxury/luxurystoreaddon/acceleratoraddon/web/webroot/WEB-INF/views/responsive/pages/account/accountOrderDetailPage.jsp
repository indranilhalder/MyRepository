<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/luxurystoreaddon/responsive/template"%>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/addons/luxurystoreaddon/responsive/user"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="order" tagdir="/WEB-INF/tags/addons/luxurystoreaddon/responsive/order"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="order" tagdir="/WEB-INF/tags/addons/luxurystoreaddon/responsive/order"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="return" tagdir="/WEB-INF/tags/responsive/returns"%> 
<!-- R2.3: Added above one line. responsive/returns -->

<spring:url value="/my-account/profile" var="profileUrl" />
<spring:url value="/my-account/update-profile" var="updateProfileUrl" />
<spring:url value="/my-account/update-password" var="updatePasswordUrl" />
<spring:url value="/my-account/update-email" var="updateEmailUrl" />
<spring:url value="/my-account/address-book" var="addressBookUrl" />
<spring:url value="/my-account/payment-details" var="paymentDetailsUrl" />
<spring:url value="/my-account/orders" var="ordersUrl" />
<spring:url value="/my-account/default/wishList" var="wishlistUrl" />
<spring:url value="/my-account/friendsInvite" var="friendsInviteUrl" />

<spring:eval expression="T(de.hybris.platform.util.Config).getParameter('order.cancel.enabled')" var="cancelFlag"/> 
<spring:eval expression="T(de.hybris.platform.util.Config).getParameter('order.return.enabled')" var="returnFlag"/> 

<template:page pageTitle="${pageTitle}">
	<div class="account" id="anchorHead">
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
	<div class="luxury-mobile-myaccount visible-xs">
		<select class="menu-select">
          <optgroup label="<spring:theme code="header.flyout.myaccount" />">
                  <option value="Overview" data-href="account-overview.php"><spring:theme code="header.flyout.overview" /></option>
                  <%-- <option value="Marketplace Preferences" data-href="marketplace-preferences.php"><spring:theme code="header.flyout.marketplacepreferences" /></option> --%>
                  <option value="Personal Information" data-href="account-info.php"><spring:theme code="header.flyout.Personal" /> </option>
                  <option value="Order History" data-href="order-history.php"> <spring:theme code="header.flyout.orders" /></option>
                  <option value="Saved Cards" data-href="account-cards.php"><spring:theme code="header.flyout.cards" /></option>
                  <option value="Saved Addresses" data-href="account-addresses.php"><spring:theme code="header.flyout.address" /></option>
                  <option value=/store/mpl/en/faq data-href="faq.php"><spring:theme code="header.flyout.faq" /></option>
              </optgroup>
         
         <%--  <optgroup label="Share">
                  <option value="Invite Friends" data-href="account-invite.php"><spring:theme code="header.flyout.invite" /></option>
              </optgroup> --%>
      </select>
    </div>
		<div class="wrapper">


			<!----- Left Navigation Starts --------->
			
			<user:accountLeftNav pageName="orderDetail"/>
			<!----- Left Navigation ENDS --------->

			<!----- RIGHT Navigation STARTS --------->

								<c:set var ="collectorder" scope="session" value=""/> 
								<c:forEach items="${subOrder.sellerOrderList}" var="sellerOrder"
									varStatus="status">
									<c:forEach items="${sellerOrder.entries}" var="entry"
										varStatus="entryStatus">
										<c:if test="${entry.mplDeliveryMode.code eq 'click-and-collect'}">
										<c:set var ="collectorder" scope="session" value="collect-order"/> 
										</c:if>
									</c:forEach>
								</c:forEach>
								
			<div class="right-account">
				<%-- <p class="nav-orderHistory">
					<a href="<c:url value="/my-account/orders"/>" class="order-history"><spring:theme
							code="text.account.orderHistorylink" text="Back to Order History" />
					</a>
				</p> --%>
				
				<div class="order-history order-details">
					<!-- Heading for saved Cards -->
					<div class="navigation">
						<h2>
							<spring:theme text="Order Details" />
						</h2>


					</div>
					<ul class="product-block order-details ${collectorder}">
						<li class="track-order-list">

							<ul class="list-top-title">
								<li><span><spring:theme
											code="text.orderHistory.order.placed" /></span> <c:if
										test="${not empty orderDate}">${orderDate}</c:if> <%-- <fmt:formatDate
										value="${subOrder.created}" pattern="MMMMM dd, yyyy" /> --%></li>
								<li><span>Order Number: </span> ${subOrder.code}</li>
								<li class="recipient"><span><spring:theme
									code="text.orderHistory.recipient" /></span> <c:choose>
								<c:when test="${subOrder.deliveryAddress != null}">
										${subOrder.deliveryAddress.firstName}&nbsp;${subOrder.deliveryAddress.lastName}
								</c:when>
								<c:otherwise>
										${subOrder.mplPaymentInfo.cardAccountHolderName}
										</c:otherwise>
									</c:choose></li>
								
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
								
								
							</ul>
						</li>
							<li class="totals" id="anchor">
								<!-- <h2>Total:</h2> -->
								<ul>
									<li><spring:theme code="text.account.order.subtotal"/>  <format:price
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
									
									<c:if test="${(subOrder.status eq 'PAYMENT_PENDING' || subOrder.status eq 'PAYMENT_TIMEOUT' || subOrder.status eq 'PAYMENT_FAILED') && empty subOrder.mplPaymentInfo}">
									<c:set var="paymentError" value="true" />
								</c:if> 
								<c:if test="${flag || paymentError}">
								<li class="item delivered first" id="shipping-track-order">
						    	<div class="item-header">
								<c:if test="${entryCount > 1 }">

								<h2>${HD_ED_Count} Product(s)-ShippingAddress:</h2>
								</c:if>
								<c:if test="${entryCount  <= 1 }">
									<h2>
										<%-- ${entryCount}&nbsp; --%>
										Shipping Address:
									</h2>
								</c:if>
								<c:set var="subOrderLine2" value="${fn:trim(subOrder.deliveryAddress.line2)}"/>
								<c:set var="subOrderLine3" value="${fn:trim(subOrder.deliveryAddress.line3)}"/>
								<div class="">
								<address>
									<span data-tribhuvan="addressType" style="display:none; ">${fn:escapeXml(subOrder.deliveryAddress.addressType)}</span>
									<span data-tribhuvan="firstName">${fn:escapeXml(subOrder.deliveryAddress.firstName)}</span>&nbsp;
									<span data-tribhuvan="lastName">${fn:escapeXml(subOrder.deliveryAddress.lastName)}</span><br>
									<span data-tribhuvan="addressLine1">${fn:escapeXml(subOrder.deliveryAddress.line1)}</span>,&nbsp;
									<c:if test="${not empty subOrderLine2}">
									<span data-tribhuvan="addressLine2">${fn:escapeXml(subOrder.deliveryAddress.line2)}</span>,
									</c:if>
									<c:if test="${not empty subOrderLine3}">
												&nbsp;<span data-tribhuvan="addressLine3">${fn:escapeXml(subOrder.deliveryAddress.line3)}</span>,
											</c:if>
									 <c:if test="${not empty subOrder.deliveryAddress.landmark}">
									   <span data-tribhuvan="landmark"> ${fn:escapeXml(subOrder.deliveryAddress.landmark)}</span>,
									</c:if>
									<br><span data-tribhuvan="city"> ${fn:escapeXml(subOrder.deliveryAddress.town)}</span>,&nbsp;
									<c:if test="${not empty subOrder.deliveryAddress.state}">
												<span data-tribhuvan="state">${fn:escapeXml(subOrder.deliveryAddress.state)}</span>,&nbsp;
											</c:if>
									<span data-tribhuvan="pincode">${fn:escapeXml(subOrder.deliveryAddress.postalCode)}</span>&nbsp;<span data-tribhuvan="country">${fn:escapeXml(subOrder.deliveryAddress.country.isocode)}</span>
									<br>
									<span data-tribhuvan="mobileNo">91&nbsp;${fn:escapeXml(subOrder.deliveryAddress.phone)}</span> <br>
								</address>
								</div>
								</div>
									<!-- R2.3: START -->
									<div class="">
										<div class="editIconCSS">
										<%-- <c:if test="${addressChangeEligible eq true}"> --%>
									       <a href="#" id="changeAddressLink">Edit / Change Address </a>
									  <%--  </c:if> --%>
										</div>
									</div>
								
							<p style="clear:both"></p>
							<div class="itemBorder">&nbsp;</div> 
							<!-- R2.3: END -->
							</li>
							</c:if>
							<c:choose>
							
										
								<c:when test="${(subOrder.status eq 'PAYMENT_PENDING' || subOrder.status eq 'PAYMENT_TIMEOUT' || subOrder.status eq 'PAYMENT_FAILED') && empty subOrder.mplPaymentInfo}">
									
									<div class="payment-method">
									Payment is yet to be confirmed
									<c:set var="paymentError" value="true" />
									</div>
									
								</c:when>
								<c:otherwise>
								<c:set var="creditCardBillingAddress"
										value="${subOrder.mplPaymentInfo.billingAddress}" />
								<c:set var="creditCardLine2" value="${fn:trim(creditCardBillingAddress.line2)}"/>
							<c:set var="creditCardLine3" value="${fn:trim(creditCardBillingAddress.line3)}"/>
							<li class="delivery-address">
								<c:if test="${not empty creditCardBillingAddress.firstName}">
									<h2>Billing Address:</h2>
									
									<address>
										${fn:escapeXml(creditCardBillingAddress.firstName)}&nbsp;
										${fn:escapeXml(creditCardBillingAddress.lastName)}<br>
										${fn:escapeXml(creditCardBillingAddress.line1)},&nbsp;
										<c:if test="${not empty creditCardLine2}">
										${fn:escapeXml(creditCardBillingAddress.line2)},&nbsp;
										</c:if>
										<c:if test="${not empty creditCardLine3}">
														${fn:escapeXml(creditCardBillingAddress.line3)},
													</c:if>
										<!-- R2.3: START -->
										<c:if test="${not empty creditCardBillingAddress.landmark}">
														${fn:escapeXml(creditCardBillingAddress.landmark)},
										</c:if>
										<br><%-- ${fn:escapeXml(creditCardBillingAddress.landmark)} --%>
										<!-- R2.3: END -->
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
							</li>
								<c:set var="paymentError" value="false"/>
									<div class="payment-method">
								<h2>Payment Method:
									${subOrder.mplPaymentInfo.paymentOption}</h2>
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
								<%-- <p>${subOrder.mplPaymentInfo.cardAccountHolderName}</p> --%>
								<c:if
									test="${subOrder.mplPaymentInfo.paymentOption eq 'Credit Card' or 'EMI' or 'Debit Card'}">
									<%-- <p>${subOrder.mplPaymentInfo.cardCardType} ending in
										${cardNumEnd}</p>
									<p>Expires on:
										${subOrder.mplPaymentInfo.cardExpirationMonth}/${subOrder.mplPaymentInfo.cardExpirationYear}</p> --%>
								</c:if>
								<c:if
									test="${subOrder.mplPaymentInfo.paymentOption eq 'Netbanking'}">
									<%-- <p>${subOrder.mplPaymentInfo.bank}</p> --%>
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
							
								</c:otherwise>
							</c:choose>
							<c:forEach items="${filterDeliveryMode}" var="deliveryType">
							
							<!-- TRP-1081 -->
							<%-- <c:out value="${entry.mplDeliveryMode.code}"></c:out> --%>
							<c:if test="${paymentError }">
							<%-- <c:out value="${subOrder.sellerOrderList}"></c:out> --%>
							<%-- <c:forEach items="${subOrder.sellerOrderList}" var="sellerOrder"
								varStatus="status"> --%>
								 <c:set var="sellerOrder" value="${subOrder}" />   <!-- // need to set for payment pending failed or timeout  -->
								<input type="hidden" id="subOrderCode"
									value="${sellerOrder.code}" />
								<input type="hidden" id="newCode" value="${subOrder.code}" />
								<c:forEach items="${sellerOrder.entries}" var="entry"
									varStatus="entryStatus">
									<c:if test="${deliveryType eq entry.mplDeliveryMode.code && paymentError}">
									
									<c:if
											test="${entry.mplDeliveryMode.code eq 'click-and-collect' }">
								    <c:if test="${storeId ne entry.deliveryPointOfService.address.id }">
									   <c:set var="pos"
																value="${entry.deliveryPointOfService.address}" />
																<li class="item delivered first" id="shipping-track-order">
																	<div class="item-header">
															<c:set var="storeId" value="${pos.id}" />
															
											    <c:set value="${subOrder.entries}" var="parentRefEntries" />
											    <c:set value="0" var="cncQuantity" />
	                                            <c:forEach items="${subOrder.entries}" var="parentRefEntry">
		                                           <c:if	test="${parentRefEntry.deliveryPointOfService.address.id eq pos.id 
		                                           and parentRefEntry.mplDeliveryMode.code eq 'click-and-collect'}">
		                                                  <c:set value="${cncQuantity+parentRefEntry.quantity}" var="cncQuantity" />     
		                                           </c:if>
	                                            </c:forEach> 
	                                                 <c:if test="${not empty cncQuantity}"> <h3>${cncQuantity} Product(s)-Collect</h3></c:if>
															<p style="font-size: 12px; font-weight: 600;">Store
																Address:</p>

						                          <c:if test="${not empty entry.deliveryPointOfService.address}">
															<address
																style="line-height: 18px; font-size: 12px; padding-top: 5px;">
															  <c:if test="${not empty entry.deliveryPointOfService.displayName}"> ${fn:escapeXml(entry.deliveryPointOfService.displayName)}<br></c:if>
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
												<c:if test="${fullfillmentData.key == entry.entryNumber}">
													<c:set var="fulfilmentValue" value="${fn:toLowerCase(fullfillmentData.value)}"> </c:set>
													<%-- <c:if test="${not paymentError}"> --%>
													<c:choose>
														<c:when test="${fulfilmentValue eq 'tship'}">
															<span><spring:theme code="product.default.fulfillmentType"/> </span> 
														</c:when>
														<c:otherwise>
															<span>${entry.selectedSellerInformation.sellername}</span> 
														</c:otherwise>
													</c:choose>
												<%-- </c:if> --%>
												</c:if>
											</c:forEach>
											<!-- TISEE-6290 -->
										</p>
									</c:if>
									<c:if test="${not paymentError}">
										<p>
											<spring:message code="text.orderHistory.seller.order.number"></spring:message>
											<span>${sellerOrder.code}</span>
										</p>
										</c:if>
								  <!--R2.3 TISRLEE-1615- Start   -->
									     <c:choose>
												   <c:when test="${not empty entry.selectedDeliverySlotDate}">
													   <p>
										                 <span style="font-weight: bold"> ${entry.mplDeliveryMode.name} :</span>
											             <span>${entry.selectedDeliverySlotDate} &nbsp;, ${entry.timeSlotFrom}-${entry.timeSlotTo}</span>
										              </p>
												  </c:when>
													<c:otherwise>
													<c:if test="${not empty entry.eddDateBetWeen}">
                                                         <span style="font-weight: bold"> ${entry.mplDeliveryMode.name} :</span>  ${entry.eddDateBetWeen}  
                                                     </c:if>
													</c:otherwise>
										 </c:choose>
									<!--R2.3 TISRLEE-1615- END   -->
											<!--  Edit button and input box for  pickup Person details -->
											
														<div id="pickNo" style="font-size: 12px;padding-top: 5px;"> ${sellerOrder.pickupPhoneNumber}<br> </div> 
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
												                    <c:set var="status">${sellerOrder.status}</c:set>
                                                                 <c:forEach items="${subOrderStatus}" var="sellerOrderStatus">
														             <c:if test="${sellerOrderStatus eq status}">
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
													   <div class="container pickup_Edit">
														
														<div class="row">
														
															<div class="col-md-5">
														
															<div class="row mobileWidth" style="float: left; z-index: 999;">
																		<div class="col-md-5">
																		
																		  <div class="col-md-5">
																			<label class="pickup_name">PickUpName</label>
																		 </div>
																		
																		<div class="col-md-7"
																			style="z-index: 99999 !important;">
																			<input id="pickUpName" class="pickUpName" type="Text" maxlength="30"
																				name="pickUpName1"
																				value="${sellerOrder.getPickupName()}" /> <br />
																			<div class="error_text pickupPersonNameError"></div>
																		</div>
																  </div>
													        </div>
													        </div>
													        
													        <div class="col-md-4" style="z-index: 99;">
																	<div class="row mobileWidth" style="z-index: 99;">
																	<div class="col-md-5">
																			<label class="pickup_mob">Mobile No</label>
																		</div>
																		<div class="col-md-7">
																			<input id="pickMobileNo" class="pickMobileNo"
																				type="Text" name="mobileNo"   maxlength="10"
																				value="${sellerOrder.getPickupPhoneNumber()}" />
																			<div class="error_text pickupPersonMobileError"
																				style=""></div>
																				
																			</div>
																			</div>
																		</div>
																	<div class="col-md-1"></div>	
																	<div class="col-md-1">
																    </div>	
																    <input type="button" value="Save" class="savebtn savebtnOther"
																		onclick="editPickUpDetails('${subOrder.code}')" />

															
													   </div>
													   </div>
														</c:if>
														</c:if>
													
									</div>
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
										<div class="details">
											<p>${entry.brandName}</p>
											<h2 class="product-name">
												<a href="${productUrl}">${entry.product.name}</a>
											</h2>
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
											<div class="col-md-6"> <!-- R2.3: START >
											<c:if
												test="${entry.itemCancellationStatus eq 'true' and entry.giveAway eq false and entry.isBOGOapplied eq false and cancelFlag}">
												<c:set var="bogoCheck"
													value="${entry.associatedItems ne null ? 'true': 'false'}"></c:set>
												<a href="" data-toggle="modal"
													data-target="#cancelOrder${sellerOrder.code}${entry.mplDeliveryMode.sellerArticleSKU}${entryStatus.index}"
													data-mylist="<spring:theme code="text.help" />"
													data-dismiss="modal" onClick="refreshModal('${bogoCheck}',${entry.transactionId})"><spring:theme
														text="Cancel Order" /></a>
												<!-- TISCR-410 -->
												<spring:theme code="trackOrder.cancellableBefore.msg" />
												
											</c:if>
											 <%-- R2.3: START:Commented: <c:if
												test="${entry.itemReturnStatus eq 'true' and entry.giveAway eq false and entry.isBOGOapplied eq false}">
											 	<a
													href="${request.contextPath}/my-account/order/returnPincodeCheck?orderCode=${sellerOrder.code}&ussid=${entry.mplDeliveryMode.sellerArticleSKU}&transactionId=${entry.transactionId}">
													<spring:theme code="text.account.returnReplace"
														text="Return Item" />
												</a>
											 </c:if>  --%> <!-- R2.3: START: -->
											 <%-- <a
																	href="${request.contextPath}/my-account/order/returnPincodeCheck?orderCode=${sellerOrder.code}&ussid=${entry.mplDeliveryMode.sellerArticleSKU}&transactionId=${entry.transactionId}">
																	<spring:theme code="text.account.returnReplace"
																		text="Return Item" />
																</a>  --%>
											 	<c:choose>
												 	 <c:when test="${entry.itemReturnStatus eq 'true' and entry.giveAway eq false and entry.isBOGOapplied eq false}">
															 <a
																	href="${request.contextPath}/my-account/order/returnPincodeCheck?orderCode=${sellerOrder.code}&ussid=${entry.mplDeliveryMode.sellerArticleSKU}&transactionId=${entry.transactionId}">
																	<spring:theme code="text.account.returnReplace"
																		text="Return Item" />
																</a> 	 
												  	</c:when>
												  	<c:otherwise>
												  			 <c:if test="${cancellationMsg eq 'true'}">
																<spring:theme code="orderHistory.cancellationDeadlineMissed.msg" />
															</c:if> 
												  	</c:otherwise>
												</c:choose>
												<!-- R2.3: END: -->

											<c:if test="${entry.showInvoiceStatus eq 'true'}">
												<a
													href="${request.contextPath}/my-account/order/requestInvoice?orderCode=${sellerOrder.code}&transactionId=${entry.transactionId}"
													onclick="callSendInvoice();"><spring:theme
														code="text.account.RequestInvoice" text="Request Invoice" /></a>
											</c:if>
											<!-- TISCR-410 -->
											<!-- R2.3: START -->
											<%--  <c:if test="${cancellationMsg eq 'true'}">
												<spring:theme code="orderHistory.cancellationDeadlineMissed.msg" />
											</c:if>  --%>
											
											<%--  <c:choose>
														 	 <c:when test="${entry.itemReturnStatus eq 'true'  and entry.giveAway eq false and entry.isBOGOapplied eq false}">
																	<a href="${request.contextPath}/my-account/order/returnPincodeCheck?orderCode=${subOrder.code}&ussid=${entry.mplDeliveryMode.sellerArticleSKU}&transactionId=${entry.transactionId}" onClick="openReturnPage('${bogoCheck}',${entry.transactionId})">
																						<spring:theme code="text.account.returnReplace"
																							text="Return Item"/> 
																	</a>		 
														  	</c:when>
														  	<c:otherwise>
														  		<c:if test="${entry.isCancellationMissed eq 'true'}">
																						<spring:theme code="orderHistory.cancellationDeadlineMissed.msg" />
																</c:if>
														  	</c:otherwise>
														</c:choose> --%>
											<!-- TISCR-410 ends -->
											
											</div>
											<div class="col-md-5">
												<c:if test="${fn:containsIgnoreCase(entry.returnMethodType , 'SELF_COURIER')}">
												<c:if test="${entry.isRefundable eq false }">
												<c:if test="${entry.consignment.status.code eq 'RETURN_INITIATED'}">
													<div class="awsInnerClass">
															Please provide AWB number, Logistics partner and upload POD <a class="awbNumberLink" id="awbNumberLink">here</a>
													</div>
													<!-- TISRLUAT-50 -->
														<return:lpDetailsUploadPopup entry="${entry}" />
												</c:if>
												</c:if>	
												</c:if>
											</div>
											
										</div>
										<!-- R2.3 : END -->
										<div class="modal cancellation-request fade"
											id="cancelOrder${sellerOrder.code}${entry.mplDeliveryMode.sellerArticleSKU}${entryStatus.index}">

											<div class="content">
												<button type="button" class="close pull-right" 		
										           aria-hidden="true" data-dismiss="modal">		
										            </button>
												<div class="cancellation-request-block">
													<h2 class="">Request Cancellation</h2>
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
																					<c:choose>
																						<c:when test="${fn:toLowerCase(entryCancel.product.luxIndicator)=='luxury'}">
																								<a href="${productUrl}"> <product:productPrimaryImage
																																			product="${entryCancel.product}"
																																			format="luxuryCartIcon" />
																																	</a>
																	
																						</c:when>
																						<c:otherwise>
																								<a href="${productUrl}"> <product:productPrimaryImage
																																			product="${entryCancel.product}"
																																			format="thumbnail" />
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
															<!-- TISPRDT - 995 -->
																	<!-- <a class="close" data-dismiss="modal" >Close</a> -->
																<!-- TISPRDT - 995 -->
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
														<h2>
															<span id="resultTitle"></span>
														</h2>
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
																					<c:choose>
																						<c:when test="${fn:toLowerCase(entryCancel.product.luxIndicator)=='luxury'}">
																								<a href="${productUrl}"> <product:productPrimaryImage
																																			product="${entryCancel.product}"
																																			format="luxuryCartIcon" />
																																	</a>
																	
																						</c:when>
																						<c:otherwise>
																								<a href="${productUrl}"> <product:productPrimaryImage
																																			product="${entryCancel.product}"
																																			format="thumbnail" />
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
														<div class="buttons mt-20">
															<button type="button" class="light-red blue"
																id="returnReject" onclick="reloadOrderDetailPage()">Close</button>

														</div>
													</form>
												</div>

											</div>
											<div class="overlay fade" data-dismiss="modal"></div>
										</div>
										<!-- Start Order Tracking diagram -->
										<c:set var="dotCode" value="${subOrder.code}${entry.entryNumber}" />	
										<c:set value="${trackStatusMap[entry.orderLineId]}"
											var="trackStatus" />
												<c:set value="${trackStatusMap[dotCode]}"
											var="trackPaymentStatus" />
											
										<c:set value="${trackStatus['APPROVED']}" var="approvedStatus" />
										<c:set value="${trackStatus['PROCESSING']}"
											var="processingStatus" />
										<c:set value="${trackStatus['SHIPPING']}" var="shippingStatus" />
										<c:set value="${trackStatus['DELIVERY']}" var="deliveryStatus" />
										<c:set value="${trackStatus['CANCEL']}" var="cancelStatus" />
										<c:set value="${trackStatus['RETURN']}" var="returnStatus" />
										<c:set value="${trackPaymentStatus['PAYMENT']}" var="paymentStatus" />
										
										<c:set var="productDelivered" value="0"></c:set>
										
										 <!-- For RTO handling -->
											<c:forEach items="${shippingStatus}" var="productStatus"

												varStatus="loop">
												<c:if test="${(productStatus.responseCode eq 'DELIVERED') or (productStatus.responseCode eq 'ORDER_COLLECTED')}">
										 	<c:set var="productDelivered" value="1"/>
										  </c:if>
										  </c:forEach>
										<div class="deliveryTrack status suman"
											id="tracker_${entry.transactionId}">
											<ul class="nav">
											
											 <c:if test="${paymentError}">
													<li>Payment</li>
										      </c:if> 
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
											
											<!-------------------------------------- Payment Block ------------------------------------>
											
											<c:if test="${paymentError}">
											
												<c:set var="displayMsgVar" value="" />
												<li class="progress progtrckr-done paymentStatus processing"
													orderlineid="${entry.orderLineId}"
													ordercode="${subOrder.code}">
													<span class="start"></span>
													<c:set value="0" var="dotCount" /> 
													<!-- Show only first and last result to restrict in 2 dots-->
													<c:forEach items="${paymentStatus}" var="productPaymentStatus" varStatus="loop">
														
														<c:choose>
															<c:when test="${productPaymentStatus.isSelected eq true && productPaymentStatus.isEnabled eq true}">
																<span class="dot trackOrder_${productPaymentStatus.colorCode}" index="${loop.index}"> 
																	<img src="${commonResourcePath}/images/thin_top_arrow_222.png" class="dot-arrow">
																</span>
																<c:set var="dotCount" value="${dotCount + 1}" />
															</c:when>
														</c:choose>
														
														<!-- Prepare popup message  -->
															<c:if test="${productPaymentStatus.isEnabled eq true}">
																<c:set var="displayMsgVar" value="${displayMsgVar}<p>${productPaymentStatus.shipmentStatus}</p>" />
																<c:forEach items="${productPaymentStatus.statusRecords}" var="statusRecord">
																	<c:set var="displayMsgVar" value="${displayMsgVar}<ul><li>${statusRecord.statusDescription}</li></ul>" />
																</c:forEach>
															</c:if>
															
														<!-- Prepare popup message  ends -->
																				
														<c:set value="${ (productPaymentStatus.responseCode eq currentStatusMap[dotCode]) ? 'block' : 'none'}" var="showBlock" />
														
															<c:if test="${productPaymentStatus.isSelected eq true}">
																<div class="order message trackOrdermessage_${productPaymentStatus.colorCode} order-placed-arrow"
																	id="orderStatus${entry.orderLineId}_${loop.index}"
																	style="display: ${showBlock};">${displayMsgVar}
																</div>
															</c:if>

														<!-- setting to default if both enabled and selected are true -->
															<c:if test="${productPaymentStatus.isSelected eq true && productPaymentStatus.isEnabled eq true}">
																<c:set var="displayMsgVar" value="" />
															</c:if>

													</c:forEach> 
												
													<c:forEach var="i" begin="${dotCount}" end="0">
																<span class="dot inactive"></span>
													</c:forEach>
											</li>
											</c:if>
											
											<!-- Payment Block End -->	
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
														<!-- <span class="start"></span> -->
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
														ordercode="${subOrder.code}"><span class="start"></span><c:set value="${0}"
															var="dotCount" /> <c:forEach items="${cancelStatus}"
															var="productStatus" varStatus="loop">

															<c:choose>
																<c:when
																	test="${productStatus.isSelected eq true && productStatus.isEnabled eq true}">
																	<span class="dot inactive trackOrder_${productStatus.colorCode}" index="${loop.index}">
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
															
							</c:if> 
							
							
							
							
							
							
							
							
							
							
							
							
							
							
							
							
							
							
							
							
							
							
							
							
							
							
							
							
							
							
							
							
							
							
							
							
							
							
												
							
							 <c:forEach items="${subOrder.sellerOrderList}" var="sellerOrder"
								varStatus="status">
								
								<input type="hidden" id="subOrderCode"
									value="${sellerOrder.code}" />
								<input type="hidden" id="newCode" value="${subOrder.code}" />
								<c:forEach items="${sellerOrder.entries}" var="entry"
									varStatus="entryStatus">
									<c:if test="${deliveryType eq entry.mplDeliveryMode.code}">
									
									<c:if
											test="${entry.mplDeliveryMode.code eq 'click-and-collect' }">
								    <c:if test="${storeId ne entry.deliveryPointOfService.address.id }">
									   <c:set var="pos"
																value="${entry.deliveryPointOfService.address}" />
																<li class="item delivered first  click-collect-option" id="shipping-track-order">
																	<div class="item-header">
															<c:set var="storeId" value="${pos.id}" />
															
											    <c:set value="${subOrder.entries}" var="parentRefEntries" />
											    <c:set value="0" var="cncQuantity" />
	                                            <c:forEach items="${subOrder.entries}" var="parentRefEntry">
		                                           <c:if	test="${parentRefEntry.deliveryPointOfService.address.id eq pos.id 
		                                           and parentRefEntry.mplDeliveryMode.code eq 'click-and-collect'}">
		                                                  <c:set value="${cncQuantity+parentRefEntry.quantity}" var="cncQuantity" />     
		                                           </c:if>
	                                            </c:forEach> 
	                                                 <c:if test="${not empty cncQuantity}"> <h3>${cncQuantity} Product(s)-Collect</h3></c:if>
															<p style="font-size: 12px; font-weight: 600;">Store
																Address:</p>
															
						                          <c:if test="${not empty entry.deliveryPointOfService.address}">
															<address
																style="line-height: 18px; font-size: 12px; padding-top: 5px;">
															  <c:if test="${not empty entry.deliveryPointOfService.displayName}"> ${fn:escapeXml(entry.deliveryPointOfService.displayName)}<br></c:if>
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

									<c:if test="${entry.mplDeliveryMode.code ne 'click-and-collect' }">
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
								   <!--R2.3 TISRLEE-1615- Start   -->
								   <c:if test="${entry.mplDeliveryMode.code ne 'click-and-collect'}">
								             <c:choose>
												   <c:when test="${not empty entry.selectedDeliverySlotDate}">
													   <p>
										                 <span style="font-weight: bold"> ${entry.mplDeliveryMode.name} :</span>
											             <span>${entry.selectedDeliverySlotDate} &nbsp;, ${entry.timeSlotFrom}-${entry.timeSlotTo}</span>
										              </p>
												  </c:when>
													<c:otherwise>
													<c:if test="${not empty entry.eddDateBetWeen}">
                                                         <span style="font-weight: bold"> ${entry.mplDeliveryMode.name} :</span>  ${entry.eddDateBetWeen}  
                                                     </c:if>
													</c:otherwise>
											  </c:choose>
								   </c:if>
									 
								 <!--R2.3 TISRLEE-1615- END   -->
											<!--  Edit button and input box for  pickup Person details -->
											
											  <div id="pickNo" style="font-size: 12px;padding-top: 5px; display:none;"> ${sellerOrder.pickupPhoneNumber}<br> </div> 
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
												                    <c:set var="status">${sellerOrder.status}</c:set>
                                                                 <c:forEach items="${subOrderStatus}" var="sellerOrderStatus">
														             <c:if test="${sellerOrderStatus eq status}">
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
													   <div class="pickup_Edit">											
															<div class="">
																  
																	<label class="pickup_name">PickUpName</label>						
																<div class="">
																	<input id="pickUpName" class="pickUpName" type="Text" maxlength="30"
																		name="pickUpName1"
																		value="${sellerOrder.getPickupName()}" /> <br />
																	<div class="error_text pickupPersonNameError"></div>
																</div>																  
													        </div>													        
													        <div class="">
																	<label class="pickup_mob">Mobile No</label>
																<div class="">
																	<input id="pickMobileNo" class="pickMobileNo"
																		type="Text" name="mobileNo"   maxlength="10"
																	value="${sellerOrder.getPickupPhoneNumber()}" />
																<div class="error_text pickupPersonMobileError"
																	style=""></div>
																	
																</div>
															</div>	
															<input type="button" value="Save" class="btn btn-block btn-primary savebtn savebtnOther"
																		onclick="editPickUpDetails('${subOrder.code}')" />													  
													   </div>
														</c:if>
														</c:if>
													
									</div>
									
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
										<div class="details">
											<p>${entry.brandName}</p>
											<h2 class="product-name">
												<a href="${productUrl}">${entry.product.name}</a>
											</h2>
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
														<format:price priceData="${entry.amountAfterAllDisc}"
															displayFreeForZero="true" />
													</ycommerce:testId>
												</p>
											</div>
											<c:if test="${not empty entry.imeiDetails}">
												<p>Serial Number: ${entry.imeiDetails.serialNum}</p>
											</c:if>

										</div>
										
											
										<div class="actions">
										<div class="col-md-6 col-sm-6">
											<c:if
												test="${entry.itemCancellationStatus eq 'true' and entry.giveAway eq false and entry.isBOGOapplied eq false}">
												<c:set var="bogoCheck"
													value="${entry.associatedItems ne null ? 'true': 'false'}"></c:set>
												<a href="" data-toggle="modal"
													data-target="#cancelOrder${sellerOrder.code}${entry.mplDeliveryMode.sellerArticleSKU}${entryStatus.index}"
													data-mylist="<spring:theme code="text.help" />"
													data-dismiss="modal" onClick="refreshModal('${bogoCheck}',${entry.transactionId})"><spring:theme
														text="Cancel Order" /></a>
												<!-- TISCR-410 -->
												<spring:theme code="trackOrder.cancellableBefore.msg" />
												
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
											<!-- TISCR-410 -->
											<c:if test="${cancellationMsg eq 'true'}">
												<spring:theme code="orderHistory.cancellationDeadlineMissed.msg" />
											</c:if>
											</div>
											<div class="col-md-6 col-sm-6 pull-right">
											<c:if test="${fn:containsIgnoreCase(entry.returnMethodType , 'SELF_COURIER')}">
												<c:if test="${entry.isRefundable eq false }">
												<c:if test="${entry.consignment.status.code eq 'RETURN_INITIATED'}">
													<div class="awsInnerClass">
															Please provide AWB number, 
															<br/>Logistics partner and upload POD <a id="awbNumberLink" class="awbNumberLink">here</a>
													</div>
													<!-- TISRLUAT-50 -->
														<return:lpDetailsUploadPopup entry="${entry}" />
												</c:if>	
												</c:if>
												</c:if>
												
											</div>
											<!-- TISCR-410 ends -->
										</div>

										<div class="modal cancellation-request fade"
											id="cancelOrder${sellerOrder.code}${entry.mplDeliveryMode.sellerArticleSKU}${entryStatus.index}">

											<div class="content">
												<!-- 	<button type="button" class="close pull-right" 		
										           aria-hidden="true" data-dismiss="modal">		
										            </button> -->
												<div class="cancellation-request-block">
													<h2 class="orderDetailCancel">Request Cancellation</h2>
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
																					<h2 class="product-name">
																						<a href="${productUrl}">${entryCancel.product.name}</a>
																					</h2>

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
															<!-- TISPRDT - 995 -->
																	<!-- <a class="close" data-dismiss="modal" >Close</a> -->
																<!-- TISPRDT - 995 -->
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
														<h2>
															<span id="resultTitle"></span>
														</h2>
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
																					<h2 class="product-name">
																						<a href="${productUrl}">${entryCancel.product.name}</a>
																					</h2>

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
												<c:if test="${(productStatus.responseCode eq 'DELIVERED') or (productStatus.responseCode eq 'ORDER_COLLECTED')}">
										 	<c:set var="productDelivered" value="1"/>
										  </c:if>
										  </c:forEach>
										<div class="deliveryTrack status suman"
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
                                    <!-- R2.3: One line -->
								</c:forEach>
								 </c:forEach> 
							
							</c:forEach>

						</li>



					</ul>
				</div>
			</div>




		</div>
		
	</div>
	<!-- R2.3: START -->
			<div class="removeModalAfterLoad" id="changeAddressPopup">
			  <order:changeDeliveryAddress orderDetails="${subOrder}" />
            </div>
            <div class="removeModalAfterLoad" id="otpPopup">
            </div>
             </div><!-- /.modal -->
	    
 
        <div class="wrapBG" style="background-color: rgba(0, 0, 0, 0.5); width: 100%; height: 600px; position: fixed; top: 0px; left: 0px; z-index: 99999; display: none;"></div>
	<!-- R2.3: END -->
</template:page>
<%-- <script type="text/javascript"
	src="${commonResourcePath}/js/jquery-2.1.1.min.js"></script>
<template:javaScriptVariables />
<script type="text/javascript"
	src="${commonResourcePath}/js/acc.accountaddress.js"></script> --%>
<script>

/*--------- Start of track order UI -------*/
<!-- R2.3: START --> 
 <!--   AWB Jquery codes PopUp  -->
	$(document).ready(function(){
		$(".uploadFile").change(function(){
			// TISRLUAT-50 changes 
			var tribhuvanUploadFile = $(this);
			var url = $(this).val();
			var res = url.split('\\');
			var filename = res[res.length - 1];
			// TISRLUAT-50 changes 
			//console.log("tribhuvanUploadFile "+tribhuvanUploadFile.parent().attr('style'));
			tribhuvanUploadFile.parent().find('.uploadDiv .textFile').text(filename);
		});
		});
	
	<!--  End of AWB Jquery codes PopUp  -->
<!-- R2.3: END -->	

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
					$(this).parents(".trackOrdermessage_00cbe9.shipping.tracking-information").toggleClass("active_viewMore");
					$(this).parents(".trackOrdermessage_00cbe9.shipping.tracking-information").prev().find('.dot-arrow').toggleClass("active_arrow");
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
	
	function checkWhiteSpace(text) {
        var letters = new RegExp(/^(\w+\s?)*\s*$/);
        var number = new RegExp(/\d/g);
        if(letters.test(text))
	        {
	        	if(number.test(text))
		        {
		            return false;
		        }
		        else
		        {
		            var enteredText = text.split(" ");
                    var length = enteredText.length;
                    var count = 0;
                    var countArray = new Array();
                    for(var i=0;i<=length-1;i++) {
                        if(enteredText[i]==" " || enteredText[i]=="" || enteredText[i]==null) {
                            countArray[i] = "space";
                            count++;
                        } else {
                            countArray[i] = "text";
                        }
                    }
                    var lengthC = countArray.length;
                    for(var i=0;i<=lengthC-1;i++) {
                        //console.log(countArray[i+1]);
                        if(countArray[i] == "space" && countArray[i+1] == "space" || countArray[i] == "text" && countArray[i+1] == "space" && countArray[i+2] == "text" || countArray[i] == "text" && countArray[i+1] == "space") {
                            return false;
                            break;
                        } else if (i == lengthC-1) {
                        	return true;
                        	break;
                        }   
                    }
		        }
	        }
	        else
	        {
	            return false;
	        }
    }
	
	 function editPickUpDetails(orderId) {
		      var name=$("#pickUpName").val();
		      var mobile=$("#pickMobileNo").val(); 	 
		      var isString = isNaN(mobile);
		      var mobile=mobile.trim();
		      //var regExp = new RegExp("^[a-zA-Z]+[ ]?[a-zA-Z]+$");
		      $(".pickupPersonNameError, .pickupPersonMobileError").hide();
		       if(name.length <= 3 ){    
		    	     $(".pickupPersonNameError").show();
		    	     $(".pickupPersonNameError").text("Enter Atleast 4 Letters");
		      }
		       else if(checkWhiteSpace(name) == false){
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
		console.log($('.item-fulfillment').length);
		<!-- R2.3: START -->
		 $("#changeAddressLink").click(function(){
			  $(".error_text").hide();
			  $(".addressListPop input[type='radio']").prop('checked',false);
			  $("#changeAddressPopup").show();
			  $(".wrapBG").show();
			  var height = $(window).height();
			  $(".wrapBG").css("height",height);
			  $("#changeAddressPopup").css("z-index","999999");
			  $("#deliveryAddressForm #pincode").val($("address span[data-tribhuvan='pincode']").text());
			  $("#deliveryAddressForm #new-address-option-1").val($("address span[data-tribhuvan='addressType']").text());
			  loadPincodeData('edit').done(function() {
					
					
				  
			      console.log($("#deliveryAddressForm #firstName").attr("value")); 
			      $("#deliveryAddressForm #firstName").val($("#deliveryAddressForm #firstName").attr("value"));
			      $("#deliveryAddressForm #lastName").val($("#deliveryAddressForm #lastName").attr("value"));
			      
			      $("#deliveryAddressForm #addressLine1").val($("#deliveryAddressForm #addressLine1").attr("value"));
			      $("#deliveryAddressForm #addressLine2").val($("#deliveryAddressForm #addressLine2").attr("value")); 
			      $("#deliveryAddressForm #addressLine3").val($("#deliveryAddressForm #addressLine3").attr("value")); 
			 	
			      console.log("blur line 394");
					 var value = $(".address_landmarkOtherDiv").attr("data-value");
					 console.log("blur line 396 "+value);
					 otherLandMarkTri(value,"defult");
			      $("#deliveryAddressForm #city").val($("#deliveryAddressForm #city").attr("value")); 
			      $("#deliveryAddressForm #state").val($("#deliveryAddressForm #state").attr("value")); 
			      $("#deliveryAddressForm #mobileNo").val($("#deliveryAddressForm #mobileNo").attr("value")); 
			     });
			  	
			  	//changeFuncLandMark("Other"); 
			 
		});
		
		
		    var length = $(".returnStatus .dot").length;
		    if(length >=3) {
			    var percent = 100/parseInt(length);
			    $(".returnStatus .dot").css("width", percent+"%");
		    }
		    
		 $(".pickupeditbtn").click(function(){
			
		
			$(".pickup_Edit").css("display","block");
			$(".pickupeditbtn").css("display","none");		
		});
		 $(".savebtn").click(function(){	
		
			// $(".pickupeditbtn").css("display","block");
			 
		 });
		 
		$("#saveBlockData").click(function(){
				$("#changeAddressPopup").hide();
				$("#showOrderDetails").show();
				$("#showOrderDetails").css("z-index","999999");
		});
		
 	$(".submitSchedule").click(function(){
			$("#changeAddressPopup, #showOrderDetails").hide();
			$("#showOTP").show();
			$("#showOTP").css("z-index","999999");
		});
		 
		 $(".close").click(function(){
			 $("#changeAddressPopup,#otpPopup").hide();
			 $(".wrapBG, #showOrderDetails").hide();
			 $("#showOTP").hide();
		 });
		 //$(".pickupeditbtn").hide(); 
		 
		 
		 <!--   AWB Jquery codes PopUp  -->
		 $(".awbNumberLink").click(function(){
				//   alert("awbNumberLink");
				// TISRLUAT-50 changes 
				var tribhuvanAwbLink = $(this);
				
				//alert(tribhuvanAwbLink.parent().next().attr('class'));
				
				
				   $(".awsNumError").hide();
				    $(".logisticPartnerError").hide();
				    $(".uploadError").hide(); 
				    $(".amountError").hide();
				 // TISRLUAT-50 changes 
				    tribhuvanAwbLink.parent().next().show();
			     $(".wrapBG").show();
			     var height = $(window).height();
			     $(".wrapBG").css("height",height);
			  // TISRLUAT-50 changes 
			     tribhuvanAwbLink.parent().next().css("z-index","999999");
			  });
		 
			  $(".submitButton").click(function(event){
				// TISRLUAT-50 changes 
				  var tribhuvaAwbSubmit = $(this);
				  var tribhuvanLocalPopUp =  tribhuvaAwbSubmit.closest(".awsNumberModal");
			   if(awbValidations(tribhuvaAwbSubmit)){
			  $(".awsNumberModal").hide(); 
			  $(".wrapBG").hide();
			// TISRLUAT-50 changes 
			  tribhuvanLocalPopUp.find("form").unbind('submit').submit();

			   }else{
			    //alert('elsepanchayati');
			    event.preventDefault();
			 // TISRLUAT-50 changes 
			    tribhuvanLocalPopUp.show();
			    $(".wrapBG").show();
			   }
			  });

			  $(".closeAWSNum").click(function(){
			   $(".awsNumberModal").hide();
			   $(".wrapBG").hide();
			  });
			  
			function awbValidations(arg1){
			 var validate = true;
			 $(".awsNumError").hide();
			 $(".logisticPartnerError").hide();
			 $(".uploadError").hide(); 
			 $(".amountError").hide();
			// TISRLUAT-50 changes 
			 var tribhuvanLocalPopUp =  arg1.closest(".awsNumberModal");
			 var awsNumber=tribhuvanLocalPopUp.find("#awsNum").val();
			 var logPart=tribhuvanLocalPopUp.find("#logisticPartner").val();
			 var fileName=tribhuvanLocalPopUp.find("#uploadFile").val();
			 var amount = tribhuvanLocalPopUp.find('#amount').val();
			 var ext = fileName.substring(fileName.lastIndexOf('.') + 1);
			 
			 if(awsNumber != null && awsNumber == '' && awsNumber < 2 && awsNumber.trim() == ''){
			       $(".awsNumError").show();
			     $(".awsNumError").text("AWB Number cannot be Blank");
			     validate = false;
			     }else if(/[^[a-zA-Z0-9]]*$/.test(awsNumber)){
			      $(".awsNumError").show();
			       $(".awsNumError").text("AWB Number cannot allow special characters");
			      validate = false;
			     }

			     if(logPart != null && logPart == '' && logPart < 2 && logPart.trim() == ''){
			        $(".logisticPartnerError").show();
			      $(".logisticPartnerError").text("Logistic partner cannot be Blank");
			      validate = false;
			      }else if(/[^[a-zA-Z]]*$/.test(logPart)){
			       $(".logisticPartnerError").show();
			        $(".logisticPartnerError").text("Logistic partner cannot allow special characters and numbers");
			       validate = false;
			 }
			 if(amount != null && amount == '' && amount < 2 && amount.trim() == ''){
			    
			        $(".amountError").show();
			      $(".amountError").text("Amount cannot be Blank");
			      validate = false;
			      }else if(isNaN(amount)){
			       
			       $(".amountError").show();
			        $(".amountError").text("Amount cannot allow special characters or letters");
			       validate = false;
			      }
			     
			   if(ext == " "){
				   $(".uploadError").show();
			       $(".uploadError").text("Proof of Delivery is mandatory");
			   }
			   else if(ext != "gif" && ext != "GIF" && ext != "JPEG" && ext != "jpeg" && ext != "jpg" && ext != "JPG" && ext != "pdf" && ext != "PDF" && ext != 'png' && ext != "PNG")
			    {
				 //  alert(ext);
			     $(".uploadError").show();
			       $(".uploadError").text("Upload images and pdf file only");
			       validate = false;
			    }
			  
			 return validate;
			 
			}
		   
		  <!-- End of  AWB Jquery codes PopUp  -->
		  });  
		</script>

		<!--   AWB CSS for PopUp -->
		<style>
		@media (max-width: 1365px){
		.changeAdddd {
		    height: initial !important;
		}
		}

@media (max-width: 1366px) {
	.awsNumberModal .changeAWS {
		height: 390px;
		width: 700px;
	}
}

.awsNumberModal .changeAWS {
    height: 390px;
    width: 700px;
}

@media (max-width: 720px) {
body .account .right-account .order-history.order-details li.item .item-header{margin-bottom:0px }
#changeAddressLink{line-height: 3;}
	.awsNumberModal .changeAWS{
		height: 400px;
		overflow-y: scroll;
		width: auto;
	}
	#awbNumberPopup{
		left:0 !important;
	}
	
	.submitButton {
		margin: 60px auto;
	}
	/*TISPRDT-1049 Start  */
	.textFile {
    display: inline-block;
    padding: 0px 4px;
    color: #8c8c8c;
    height: 32px;
    line-height: 32px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    width: 57%;
}
.uploadButton {
    background: #00cfe6;
    color: #fff;
    display: inline-block;
    padding: 0px 8px !important;
    font-size: 14px;
    height: 33px;
    line-height: 33px;
    margin: 0px 2px;
    vertical-align: top;
    width: 40%;
}
/*TISPRDT-1049 End  */
}
#awbNumberPopup {
	display: none;
	position: fixed;
	top: 50px;
	left:25%;
	
}

#awbNumberPopup .space{
	margin: 20px 10px;
	line-height: 20px;
	color: #8c8c8c;
}
#awbNumberPopup label{
	padding: 10px 0px;
	color: #8c8c8c;
	font-size: 14px;
}
#awbNumberPopup .control-label {
    font-weight: inherit;
}
#awbNumberPopup .awsUpload, #awbNumberPopup .awsUpload:focus {
	border: none;
} 
#awbNumberPopup .form-group {
    margin-bottom: 0px;
}
.closeAWSNum{
	border-radius: 50%;
	border: 1px solid #ccc !important;
	width: 40px;
	height: 40px;
    position: absolute;
    right: 17px;
    top: -6px;
    font-size: 35px;
    font-weight: 100;
    color: #ccc;
    padding: 9px 9px;
}
#awbNumberPopup h4{
    font-size: 22px !important;
    font-weight: 100 !important;
    margin-bottom: 25px !important;
}
@media (max-width: 620px){
#awbNumberPopup h4 {
    width: 85%;
}
}
#awbNumberPopup .awsTextinput{
	width: 100%;
}
.submitButton{
	background: #ff9900;
	color: #fff;
	width: 100px;
	margin: 16px;
	height: 46px;
    font-size: 20px !important;
}
.submitButton:focus, .submitButton:hover{
	border: none;
	background: #ff9900;
	color: #fff;
	width: 100px;
	margin: 16px;
	height: 46px;
    font-size: 20px;
}
#awbNumberLink{
	color: #00cfe6;
	display: inline-block;
}

.awsInnerClass {
	display: inline-block;
	position: relative;
	background: #fafafa;
	padding: 9px 12px 2px 12px;
	border: 2px solid #edad24;
	font-size: 13px;
}
.awsInnerClass:after {
	content: '';
	display: block;  
	position: absolute;
	right: 100%;
	top: 50%;
	margin-top: -18px;
	width: 0;
	height: 0;
	border-top: 10px solid transparent;
	border-right: 12px solid #edad24;
	border-bottom: 10px solid transparent;
	border-left: 10px solid transparent;
}
.errorText{
	color: red;
}
.awsNumError{
	padding: 3px 0;
}
.textFile{
	display: inline-block;
 	padding: 0px 10px;
 	color: #8c8c8c;
 	height: 32px;
 	line-height: 32px;
 	overflow: hidden;
 	text-overflow: ellipsis;
    white-space: nowrap
}
.uploadButton{
	background: #00cfe6;
	color: #fff;
	display: inline-block;
    padding: 0px 11px;
    font-size: 14px;
    height: 34px;
	line-height: 34px;
}
.uploadDiv{
    border: 1px solid #dfd1d5;
    width: 100%;
    height:35px;
}
input[type="radio"]:checked {
	background: #000;
}

</style>

<!-- R2.3: END: End of  AWB CSS for PopUp -->